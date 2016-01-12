/* The script wrapper */
define(['pipAPI', 'pipScorer', scriptFile], function (APIConstructor, Scorer, Sequence) {

    jQuery.fn.visible = function () {
        return this.css('visibility', 'visible');
    };

    jQuery.fn.invisible = function () {
        return this.css('visibility', 'hidden');
    };

    var API = new APIConstructor();

    var STATE_RESET = "STATE_RESET";
    var STATE_SHOW_SENTENCES = "STATE_SHOW_SENTENCES";
    var STATE_PAUSE = "STATE_PAUSE";
    var STATE_FILL_LETTERS = "STATE_FILL_LETTERS";
    var STATE_ASK_QUESTION = "STATE_ASK_QUESTION";
    var STATE_GOOD_JOB = "STATE_GOOD_JOB";

    API.getGlobal().state = STATE_RESET;

    var scorer = new Scorer();
    var break_up = [];
    var where_at = 1;
    var latency = 0;
    var vivid_text;
    var brackets = ['[', ']'];
    var letter;
    var index;
    var pick;
    var changed_attribute = false;
    var scorer = {  count: 1 };
    var on_question = false;

    var last_word = ""; // The word with missing letters in it.
    var letters = ""; // The letters that are missing

    function increase_count() {
        scorer.count = scorer.count + 1;
        return scorer.count;
    }

    /**
     * Divides up the content so that statements in the sequence are displayed one at a time, rather than all
     * at once.
     */
    function splitSentences(trial) {
        if(API.getGlobal().state != STATE_RESET) return false;
        console.log("Resetting paragraphs.")
        var sentence = $("div.sentence");
        break_up = sentence.text().split('.');
        last_word = break_up[break_up.length - 1].split(' ');
        last_word = last_word[last_word.length - 2] + ' ' + last_word[last_word.length - 1];
        break_up[break_up.length - 1] = break_up[break_up.length - 1].replace(last_word, "");

        // Randomly select an extra letter to include as the missing letter, if an extra letter should be missing.
        if(Sequence.add_extra_missing_letter) {
            p = jQuery.grep(trial._stimulus_collection.models, function (e, i) {
                return e.attributes.handle == "paragraph";})[0];

            pick = Math.floor(Math.random() * (1 + 1));
            if (pick == 0) {
                index = last_word.indexOf(brackets[pick]);
                letter = last_word[index - 1];
                last_word = last_word.replace(letter + brackets[pick], brackets[pick] + " ");
                if (p.attributes.data.neutralKey) {
                    p.attributes.data.neutralKey = letter + p.attributes.data.neutralKey;
                } else {
                    p.attributes.data.positiveKey = letter + p.attributes.data.positiveKey;
                    p.attributes.data.negativeKey = letter + p.attributes.data.negativeKey;
                }
            }
            else {
                index = last_word.indexOf(brackets[pick]);
                letter = last_word[index + 1];
                last_word = last_word.replace(brackets[pick] + letter, " " + brackets[pick]);
                if(p.attributes.data.neutralKey) {
                    p.attributes.data.neutralKey = p.attributes.data.neutralKey + letter;
                } else {
                    p.attributes.data.positiveKey = p.attributes.data.positiveKey + letter;
                    p.attributes.data.negativeKey = p.attributes.data.negativeKey + letter;
                }
            }
        }

        break_up.push(last_word);
        for (i = 0; i < break_up.length; i++) {
            if (i == break_up.length - 1) {
                break_up[i][0].innerText = '';
                break_up[i] = $("<p class='incomplete'>" + break_up[i] + '.</p>')
            }
            else if (i == break_up.length - 2) {
                break_up[i] = $("<p class='sentences'>" + break_up[i] + '</p>')
            }
            else {
                break_up[i] = $("<p class='sentences'>" + break_up[i] + '.</p>')
            }
            break_up[i].invisible();
        }
        console.log("The last word is:" + last_word);
        sentence.html(break_up);
        break_up[0].visible();

        console.log("So I do return true then right?");
        return true;
    }


    /**
     * Returns the missing letters
     * @param trial
     * @returns a string containing the missing letters, based on if this is a positive or negative word.
     */
    function missing_letters(trial) {
        var p = jQuery.grep(trial._stimulus_collection.models, function (e, i) {
            return e.attributes.handle == "paragraph"
        })[0];

        if (trial.data.neutral) {
            var letter = (p.attributes.data.neutralKey);
        } else if (trial.data.positive) {
            var letter = (p.attributes.data.positiveKey);
        } else {
            var letter = (p.attributes.data.negativeKey);
        }
        console.log("The missing letter is " + letter);
        return(letter);
    }

    /** Returns true if the input data matches the correct answer to question,
     * checking against the correct positive, negative, or neutral condition.
     */
    function correct_answer(trial, inputData) {
        if (inputData.handle != 'y' && inputData.handle != 'n') return false;
        var q = jQuery.grep(trial._stimulus_collection.models, function (e, i) {
            return e.attributes.handle == "question"
        })[0];
        if(trial.data.neutral) {
            console.log("Correct answer to neutral.");
            return(q.attributes.data.neutralAnswer == inputData.handle);
        } else if(trial.data.positive) {
            console.log("Correct answer to positive.");
            return(q.attributes.data.positiveAnswer == inputData.handle);
        } else {
            console.log("Correct answer to negative.");
            return (q.attributes.data.negativeAnswer == inputData.handle);
        }
    }

    /** Counterpart to the correct_answer, checks to see if the wrong input is provided
     * taking the condition of positive, neutral, or negative into account.
     */
    function incorrect_answer(trial, inputData) {
        if (inputData.handle != 'y' && inputData.handle != 'n') return false;
        console.log("The input data is:");
        console.log(inputData);
        var q = jQuery.grep(trial._stimulus_collection.models, function (e, i) {
            return e.attributes.handle == "question"
        })[0];
        if(trial.data.neutral) {
            console.log("Incorrect answer to neutral.");
            return(q.attributes.data.neutralAnswer != inputData.handle);
        } else if(trial.data.positive) {
            console.log("Incorrect answer to positive.");
            return(q.attributes.data.positiveAnswer != inputData.handle);
        } else {
            console.log("Incorrect answer to negative.");
            return (q.attributes.data.negativeAnswer != inputData.handle);
        }
    }


    /**
     * Returns true if missing letters start with the letters typed so far.
     * @param trial
     * @param inputData
     * @returns {*}
     */
    function correct_letters(trial, inputData) {
        if (!API.getGlobal().lettersTyped) API.addGlobal({lettersTyped: ""});
        if (inputData.handle.length > 1) return false;
        var lettersTyped = API.getGlobal().lettersTyped + inputData.handle;
        var result = missing_letters(trial).startsWith(lettersTyped);
        return (result);
    }

    /**
     * Returns true if the number of letters entered meets or exceeds the letters expected.
     * @param trial
     * @param inputData
     */
    function correct_length(trial, inputData) {
        if (!API.getGlobal().lettersTyped) API.addGlobal({lettersTyped: ""});
        var lettersTyped = API.getGlobal().lettersTyped + inputData.handle;
        return lettersTyped.length >= missing_letters(trial).length
    }

    // Warn people about leaving the page before they complete all the questions
    window.onbeforeunload = function () {
        return 'Are you sure you want to exit this training session?'
    }

    // When the training session is complete, move on to the next Questionnaire
    API.addSettings('redirect', "../playerScript/completed/int_train");

    // Removes the warning about leaving the page.
    API.addSettings("hooks", {
        endTask: function () {
            window.onbeforeunload = null;
            window.location = "../playerScript/completed/int_train";
        }
    })

    API.addSettings("canvas", {
        maxWidth: 1000,
        proportions: {width: 4, height: 3},
        textSize: 5
    });

    // setting the way the logger works (how often we send data to the server and the url for the data)
    API.addSettings('logger', {
        pulse: 1,
        url: '/data',
        logger: function (trialData, inputData, actionData, logStack) {
            var stimList = this._stimulus_collection.get_stimlist();
            var mediaList = this._stimulus_collection.get_medialist();
            var global = API.getGlobal();

            /*
             p = jQuery.grep(trial._stimulus_collection.models, function(e, i) {return e.attributes.handle == "paragraph"})[0]
             if(trial.data.positive) {
             return (p.attributes.data.positiveKey);
             } else {
             return(p.attributes.data.negativeKey);
             }
             */
            return {
                log_serial: logStack.length,
                trial_id: this._id,
                name: this.name(),
                responseHandle: inputData.handle,
                latency: Math.floor(trialData.letter_latency),
                stimuli: stimList,
                media: mediaList,
                data: trialData,
                script: global["script"],
                session: global["session"],
                participant: global["participant"]
            }
        }
    });

    API.addStimulusSets({
        error: [
            {handle: 'error', media: 'X', css: {color: '#FF0000'}, location: {top: 70}, nolog: true}
        ],
        yesno: [
            {
                handle: 'yesno',
                media: {html: "<div class='stim'>Please Type <b>Y</b>=Yes &nbsp;  &nbsp;  &nbsp; <b>N</b>=No</div>"},
                css: {color: '#333', fontSize: '.8em', position: 'absolute'},
                location: {bottom: 1}
            }
        ],
        stall: [
            {
                handle: 'stall',
                media: {html: "<div class='stim'>Oops, that answer is incorrect; please re-read the question and in a moment you will have a chance to answer again.</div>"},
                css: {color: '#333', fontSize: '.8em', position: 'absolute'},
                location: {top: 50},
                nolog: true
            }
        ],
        greatjob: [
            {
                handle: 'greatjob',
                media: {html: "<div class='stim'>Great job!</div>"},
                nolog: true,
                css: {color: '#333', fontSize: '1.2em', position: 'absolute'},
                location: {bottom: 50}
            }
        ],
        press_space: [
            {
                handle: 'press_space',
                media: {html: "<div class='press_space'>Press the <b>Space Bar</b> to continue.</div>"},
                nolog: true,
                css: {color: '#333', fontSize: '.8em', position: 'absolute'},
                location: {bottom: 1}
            }
        ],
        vivid: [
            {media: {'inlineTemplate': "<div class='vivid'>_______</div>"}}
        ],
        counter: [
            {
                'handle': 'counter',
                customize: function () {
                    this.media = scorer.count + ' of '+ Sequence.display_length;
                    on_question = false;
                },
                css: {color: '#333', fontSize: '.8em', position: 'absolute'},
                location: {bottom: 1, right: 1}
            }
        ]
    });

    API.addTrialSets('base', [{
        input: [
            {handle: 'a', on: 'keypressed', key: "a"},
            {handle: 'b', on: 'keypressed', key: "b"},
            {handle: 'c', on: 'keypressed', key: "c"},
            {handle: 'd', on: 'keypressed', key: "d"},
            {handle: 'e', on: 'keypressed', key: "e"},
            {handle: 'f', on: 'keypressed', key: "f"},
            {handle: 'g', on: 'keypressed', key: "g"},
            {handle: 'h', on: 'keypressed', key: "h"},
            {handle: 'i', on: 'keypressed', key: "i"},
            {handle: 'j', on: 'keypressed', key: "j"},
            {handle: 'k', on: 'keypressed', key: "k"},
            {handle: 'l', on: 'keypressed', key: "l"},
            {handle: 'm', on: 'keypressed', key: "m"},
            {handle: 'n', on: 'keypressed', key: "n"},
            {handle: 'o', on: 'keypressed', key: "o"},
            {handle: 'p', on: 'keypressed', key: "p"},
            {handle: 'q', on: 'keypressed', key: "q"},
            {handle: 'r', on: 'keypressed', key: "r"},
            {handle: 's', on: 'keypressed', key: "s"},
            {handle: 't', on: 'keypressed', key: "t"},
            {handle: 'u', on: 'keypressed', key: "u"},
            {handle: 'v', on: 'keypressed', key: "v"},
            {handle: 'w', on: 'keypressed', key: "w"},
            {handle: 'x', on: 'keypressed', key: "x"},
            {handle: 'y', on: 'keypressed', key: "y"},
            {handle: 'z', on: 'keypressed', key: "z"},
            {handle: 'space', on: 'space'}
        ],
        interactions: [
            // Show the paragraph with missing letters as soon as the trial starts.
            {
                conditions: [{type: 'begin'},
                            {type: 'function', value: function (trial, inputData) { return(splitSentences(trial)); }}
                            ],
                actions: [
                    {type: 'setGlobalAttr', setter: {state: STATE_SHOW_SENTENCES}}, // We are now showing sentences.
                    {type: 'showStim', handle: 'paragraph'},
                    {type: 'showStim', handle: 'press_space'},
                    {type: 'showStim', handle: 'counter'},
                    {type: 'setTrialAttr', setter: {correctOnLetter: "true"}},  // set to true - will get set to false later if incorrectly answered.
                    {type: 'custom', fn: function (options, eventData) {
                            API.addGlobal({"original": $("span.incomplete").text()})
                            latency = 0;
                        }}
                    ]
            },
            {// The next sentence should be displayed, until all sentences are displayed
                conditions: [
                    {type: 'globalEquals', property: 'state', value: STATE_SHOW_SENTENCES},
                    {type: 'inputEquals', value: 'space'},
                    {type: 'function', value: function (trial, inputData) {
                        return (where_at < break_up.length)
                    }},
                    {type: 'function', value: function (trial, inputData) { // don't let people do this too quickly.
                        if(inputData.latency - latency > 1000) {
                            latency = inputData.latency;
                            return true;
                        }
                        return false;
                    }}
                ],
                actions: [
                    {type: 'custom', fn: function (options, eventData) {
                        break_up[where_at].visible();
                        where_at += 1;
                        if(where_at >= break_up.length) {
                            console.log("Last sentence is visible, moving on.");
                            API.getGlobal().state = STATE_FILL_LETTERS;
                            $("div.press_space").text("Type the missing letter.");
                        }
                    }}
                ]
            },

            // The letters entered are incorrect
            {
                conditions: [
                    {type: 'globalEquals', property: 'state', value: STATE_FILL_LETTERS},
                    {type: 'inputEquals', value: 'space',negate:true},
                    {type: 'inputEquals', value: 'correct',negate:true},
                    {type: 'function', value: function (trial, inputData) {
                        return (!correct_letters(trial, inputData));
                    }}
            ],
                actions: [
                    {type: 'custom', fn: function (options, eventData) { console.log("Incorrect letter."); }},
                    {type: 'showStim', handle: 'error'},
                    {type: 'setTrialAttr', setter: {correctOnLetter: "false"}},
                    {type: 'setInput', input: {handle: 'clear', on: 'timeout', duration: 500}}
                ]
            },
            {// All the letters are entered correctly (this must be above 'the letters are correct so far')
                conditions: [
                    {type: 'globalEquals', property: 'state', value: STATE_FILL_LETTERS},
                    {
                        type: 'function', value: function (trial, inputData) {
                        return correct_length(trial, inputData)
                    }
                    },
                    {
                        type: 'function', value: function (trial, inputData) {
                        return correct_letters(trial, inputData)
                    }
                    }
                ],
                actions: [
                    {
                        type: 'custom', fn: function (options, eventData) {
                        var span = $("p.incomplete");
                        var text = span.text().replace(' ', eventData["handle"]);
                        span.text(text);
                        where_at = 1;
                        on_question = true;
                        API.getGlobal().lettersTyped = "";
                    }
                    },
                    {type: 'trigger', handle: 'correct'}
                ]
            },
            {// The letters are correct so far...
                conditions: [
                    {type: 'globalEquals', property: 'state', value: STATE_FILL_LETTERS},
                    {
                        type: 'function', value: function (trial, inputData) {
                        return !correct_length(trial, inputData)
                    }
                    },
                    {
                        type: 'function', value: function (trial, inputData) {
                        return correct_letters(trial, inputData)
                    }
                    }
                ],
                actions: [
                    {
                        type: 'custom', fn: function (options, eventData) {
                        API.getGlobal().lettersTyped = API.getGlobal().lettersTyped + eventData.handle;
                        var span = $("p.incomplete");
                        var text = span.text().replace(' ', eventData["handle"]);
                        span.text(text);
                    }
                    },
                    {
                        type: 'setTrialAttr', setter: function (trialData, eventData) {
                        if (trialData.first_letter_latency == null) {
                            trialData.first_letter_latency = Math.floor(eventData.latency);
                        }
                    }
                    }
                ]
            },
            {
                // Trigger when the correct response is provided, as there are two interactions
                // that can cause this, I've separated it out into it's own section rather than
                // duplicate the code.
                conditions: [{type: 'inputEquals', value: 'correct'}],
                actions: [
                    // Preserve the question as completed, so that it will eventually be set back to the server.
                    {type: 'setTrialAttr', setter: function (trialData, eventData) {
                        console.log("YO BABY #2!");
                        trialData.word = $("span.incomplete").text();
                        trialData.paragraph = $("div[data-handle='paragraph']").text();
                        trialData.letter_latency = Math.floor(eventData.latency);
                        if (trialData.first_letter_latency == null) {
                            trialData.first_letter_latency = Math.floor(eventData.latency);
                        }
                    }
                    },
                    {type: 'resetTimer'}, // Reset timer so that we can also collect the latency on the y/n respose
                    {type:'hideStim',handle : 'press_space'},
                    {type:'hideStim',handle : 'paragraph'},
                    // Remove all keys but 'y' and 'n'
                    {
                        type: 'removeInput',
                        handle: ['a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'v', 'w', 'x', 'z']
                    },
                    {type: 'setGlobalAttr', setter: {state: STATE_PAUSE}}, // sentence completed, pause.
                    {type: 'setInput', input: {handle: 'askQuestion', on: 'timeout', duration: 500}}
                ]
            },
            // After the statement is correctly completed, hide it, and show the question.
            {
                // Trigger when input handle is "end".
                conditions: [{type:'inputEquals',value:'askQuestion'}],
                actions: [
                    {type: 'setGlobalAttr', setter: {state: STATE_ASK_QUESTION}}, // sentence completed, show question.
                    {type:'custom',fn:function(options,eventData){
                        $("div.sentence").empty();
                    }},
                    {type:'showStim',handle : 'question'},
                    {type:'showStim',handle:'yesno'},
                    {type:'setTrialAttr',setter:{correctOnQuestion:"true"}},  // set to true - will get set to false later if incorrectly answered.
                ]
            },

            // Listen for a correct response to a question
            {
                conditions: [
                    {type: 'globalEquals', property: 'state', value: STATE_ASK_QUESTION},
                    {type: 'function', value: function (trial, inputData) {
                        return correct_answer(trial, inputData);
                    }}
                ],
                actions: [
                    {type: 'hideStim', handle: 'question'},
                    {type: 'hideStim', handle: 'yesno'},
                    {type: 'hideStim', handle: 'counter'},
                    {type: 'showStim', handle: 'greatjob'},
                    {type: 'trigger', handle: 'answered', duration: 1000}
                ]
            },
            // Listen for an incorrect response to a positive question
            {
                conditions: [
                    {type: 'globalEquals', property: 'state', value: STATE_ASK_QUESTION},
                    {type: 'function', value: function (trial, inputData) {
                        return incorrect_answer(trial, inputData);
                    }}
                ],
                actions: [
                    {
                        type: 'setTrialAttr', setter: function (trialData, eventData) {
                        if (trialData.first_question_latency == null) {
                            trialData.first_question_latency = Math.floor(eventData.latency);
                        }
                        console.log("Incorrect response to the question");
                    }
                    },
                    {type: 'removeInput', handle: 'y'},
                    {type: 'removeInput', handle: 'n'},
                    {type: 'hideStim', handle: 'yesno'},
                    {type: 'showStim', handle: 'error'},
                    {type: 'showStim', handle: 'stall'},
                    {type: 'setInput', input: {handle: 'delay', on: 'timeout', duration: 5000}},
                    {type: 'setTrialAttr', setter: {correctOnQuestion: "false"}},
                    {type: 'setInput', input: {handle: 'clear', on: 'timeout', duration: 500}}
                ]
            },
                // Handle a brief delay
            {
                conditions: [{type: 'inputEquals', value: 'delay'}],
                actions: [
                    {type: 'setInput', input: {handle: 'y', on: 'keypressed', key: 'y'}},
                    {type: 'setInput', input: {handle: 'n', on: 'keypressed', key: 'n'}},
                    {type: 'showStim', handle: 'yesno'},
                    {type: 'hideStim', handle: 'stall'}
                ]
            },
            {
                // Trigger when the correct response is provided, as there are two interactions
                // that can cause this, I've separated it out into it's own section rather than
                // duplicate the code.
                conditions: [{type: 'inputEquals', value: 'answered'}],
                actions: [
                    {
                        type: 'setGlobalAttr', setter: function () {
                        changed_attribute = false;
                        increase_count();
                    }
                    },
                    {type: 'removeInput', handle: ['y', 'n']},
                    {
                        type: 'setTrialAttr', setter: function (trialData, eventData) {
                          console.log("We have reached answered.");
                          trialData.question = $("div[data-handle='question']").text();
                          trialData.question_latency = Math.floor(eventData.latency);
                          if (trialData.first_question_latency == null) {
                              trialData.first_question_latency = Math.floor(eventData.latency);
                          }
                    }
                    },
                    {type: 'setGlobalAttr', setter: {state: STATE_RESET}}, // Reset the State
                    {type: 'log'},
                    {type: 'endTrial'}
                ]
            },

            // This interaction is triggered by a timeout after a incorrect response.
            // It allows us to delay the removal of the big red X.
            {
                // Trigger when input handle is "end".
                conditions: [
                    {type: 'inputEquals', value: 'clear'}],
                actions: [
                    {type: 'removeInput', handle: 'clear'},
                    {type: 'hideStim', handle: 'error'}
                ]
            }


        ]

    }]);

    /**
     * This sets the ratio of positive to negative statements.  if there is one
     * true, and one false, it will be a 50/50 split.  If it is 3 true, and 1 false
     * if would then be a 75% positive, 25% negative split.
     */
    if (API.getGlobal()["cbmCondition"] == "FIFTY_FIFTY") {
        API.addTrialSets('posneg', [
            {inherit: 'base', data: {positive: true}},
            {inherit: 'base', data: {positive: false}}
        ]);
    } else {
        API.addTrialSets('posneg', [
            {inherit: 'base', data: {positive: true}},
            {inherit: 'base', data: {positive: true}},
            {inherit: 'base', data: {positive: true}},
            {inherit: 'base', data: {positive: true}},
            {inherit: 'base', data: {positive: true}},
            {inherit: 'base', data: {positive: true}},
            {inherit: 'base', data: {positive: true}},
            {inherit: 'base', data: {positive: true}},
            {inherit: 'base', data: {positive: true}},
            {inherit: 'base', data: {positive: false}}
        ]);
    }

    /**
     * An alternative trial set for netural, rather than positive or fifty-fifty
     */
    API.addTrialSets('neutral',[
        { inherit:'base', data: {neutral:true}}
    ]);

    /** Base trial set used by Recognition Ratings to
     *  be all possitive;
     */
    API.addTrialSets('all',[
        { inherit:'base', data: {positive:true}}
    ]);

    /**
     * Type of Trial Set that collects vividness responses
     */
    API.addTrialSets('vivid', [{
        input: [
            {handle: 'Not at all vivid', on: 'keypressed', key: '1'},
            {handle: 'Somewhat vivid', on: 'keypressed', key: '2'},
            {handle: 'Moderately vivid', on: 'keypressed', key: '3'},
            {handle: 'Very vivid', on: 'keypressed', key: '4'},
            {handle: 'Totally vivid', on: 'keypressed', key: '5'},
            {handle: 'Prefer not to answer', on: 'keypressed', key: 'p'}
        ],
        layout: [
            {
                media: {template: "/PIPlayerScripts/vividness.html"}
            }
        ],
        stimuli: [
            {media: {'inlineTemplate': "<div class='vivid'>_______</div>"}}
        ],
        interactions: [
            {
                conditions: [{type: 'begin'}],
                actions: [{type: 'showStim', handle: 'All'}]
            },
            {
                conditions: [
                    {
                        type: 'function', value: function (trial, inputData) {
                        if (inputData.handle == 'Not at all vivid' ||
                            inputData.handle == "Somewhat vivid" ||
                            inputData.handle == "Moderately vivid"
                        ) {
                            vivid_text = 'Thanks. Really try to use your imagination!';
                        }
                        else if (inputData.handle == "Very vivid" ||
                            inputData.handle == "Totally vivid" ||
                            inputData.handle == "Prefer not to answer") {
                            vivid_text = "Thanks. It's great you're really using your imagination!";
                        }
                        return ( inputData.handle == "Not at all vivid" ||
                        inputData.handle == "Somewhat vivid" ||
                        inputData.handle == "Moderately vivid" ||
                        inputData.handle == "Very vivid" ||
                        inputData.handle == "Totally vivid" ||
                        inputData.handle == "Prefer not to answer")
                    }
                    }
                ],
                actions: [
                    {
                        type: 'custom', fn: function (options, eventData) {
                        var span = $("div.vivid");
                        var text = span.text().replace('_______', eventData["handle"]);
                        span.text(text);
                    }
                    },
                    {
                        type: 'setTrialAttr', setter: function (trialData, eventData) {
                        trialData.vividness = eventData["handle"];
                    }
                    },
                    {type: 'trigger', handle: 'vivid_switch', on: 'timeout', duration: 500}
                ]
            },
            {
                conditions: [{type: 'inputEquals', value: 'vivid_switch'}],
                actions: [{type: 'log'}, {type: 'endTrial'}]
            },
        ]
    },
    ]);

    API.addTrialSets('vivid_after', [
        {
            input: [
                // What input to accept from the participant (user)
                {handle: 'space', on: 'space'}
            ],
            layout: [
                {
                    media: {html: ''}
                }
            ],
            customize: function () {
                this.layout[0].media.html = '<div class="thanks"><p style="font-size: 24px; text-align:center">' + vivid_text + '</p>' + '<p style="font-size: 20px; text-align:center" > Press the spacebar to continue </p></div>';
                ;
            },
            interactions: [
                // What to do when different events occur.
                {
                    conditions: [
                        {type: 'inputEquals', value: 'space'}
                    ],
                    actions: [
                        {type: 'endTrial'}
                    ]
                }
            ]
        }
    ]);

    API.addSequence(Sequence.sequence);
    return API.script;

});
/* don't forget to close the define wrapper */