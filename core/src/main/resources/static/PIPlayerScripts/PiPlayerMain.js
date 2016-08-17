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

    API.getGlobal().quest = Sequence.quest;

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
    var already_wrong_s = false;
    var already_wrong_c = false;
    var scorer = {  count: 1, ct_s: 0, ct_c: 0};
    var on_question = false;


    var pct_ct_s = 0;
    var pct_ct_c = 0;

    var feed_back_s = '';
    var feed_back_c = '';

    var last_word = ""; // The word with missing letters in it.
    var letters = ""; // The letters that are missing

    function increase_count() {
        scorer.count = scorer.count + 1;
        return scorer.count;
    }

    function getRandomSubarray(arr, size) {
        var shuffled = arr.slice(0), i = arr.length, temp, index;
        while (i--) {
            index = Math.floor((i + 1) * Math.random());
            temp = shuffled[index];
            shuffled[index] = shuffled[i];
            shuffled[i] = temp;
        }
        return shuffled.slice(0, size);
    }

    /**
     * Replaces the string [negation] in the statement with the negation value in the trial,
     * or removes it depending on the global value of 'negate'.
     * @param trial
     */
    function handleNegation(trial) {
        if(API.getGlobal().state != STATE_RESET) return false;

        // Get the value of negate.
        var p = jQuery.grep(trial._stimulus_collection.models, function (e, i) {
            return e.attributes.handle == "paragraph"
        })[0];
        var negate = p.attributes.data.negation;

        // Replace negate in the sentence if negate is turned on.
        if(negate === undefined || API.getGlobal()["negate"] == false) negate = "";
        var sentence = $("div.sentence");
        console.log(negate);
        console.log('HI');
        sentence.text(sentence.text().replace("[negation]", negate));
    }

    /**
     * Replaces the string arrays in with the first or second element of the array.
     * @param trial
     */    function chooseWords(trial)
    {
        if(API.getGlobal().state != STATE_RESET) return false;

        // Get the value of negate.
        var p = jQuery.grep(trial._stimulus_collection.models, function (e, i) {
            return e.attributes.handle == "paragraph"
        })[0];
        console.log(API.getGlobal());
        if (Sequence.frag == 'first'){
            p.attributes.data.negativeKey = p.attributes.data.negativeKey[0];
            p.attributes.data.negativeWord = p.attributes.data.negativeWord[0];
            p.attributes.data.positiveKey = p.attributes.data.positiveKey[0];
            p.attributes.data.positiveWord = p.attributes.data.positiveWord[0];
        }
        else {
            p.attributes.data.negativeKey = p.attributes.data.negativeKey[1];
            p.attributes.data.negativeWord = p.attributes.data.negativeWord[1];
            p.attributes.data.positiveKey = p.attributes.data.positiveKey[1];
            p.attributes.data.positiveWord = p.attributes.data.positiveWord[1];
        }
    }

    /**
     * Replaces the string [stimulus] in the statement with chosen element of the array.
     * @param trial
     */
    function getWord(trial)
    {
        if(API.getGlobal().state != STATE_RESET) return false;

        var sentence = $("div.sentence");

        // Get the value of negate.
        var p = jQuery.grep(trial._stimulus_collection.models, function (e, i) {
            return e.attributes.handle == "paragraph"
        })[0];
        if (p.trial.data.positive)
        {
            sentence.text(sentence.text().replace("[stimulus]", p.attributes.data.positiveWord));
            return p.attributes.data.positiveWord;
        }
        else
        {
            sentence.text(sentence.text().replace("[stimulus]", p.attributes.data.negativeWord));
            return p.attributes.data.negativeWord;
        }
    }

    /**
     * Divides up the content so that statements in the sequence are displayed one at a time, rather than all
     * at once.
     */
    function splitSentences(trial) {
        if(API.getGlobal().state != STATE_RESET) return false;
        var sentence = $("div.sentence");
        break_up = sentence.text().split('.');
        last_word = getWord(trial);
        break_up[break_up.length - 1] = break_up[break_up.length - 1].replace("[stimulus]", "");
        // Randomly select an extra letter to include as the missing letter, if an extra letter should be missing.
        if(Sequence.add_extra_missing_letter) {
            p = jQuery.grep(trial._stimulus_collection.models, function (e, i) {
                return e.attributes.handle == "paragraph";})[0];
            pick = Math.floor(Math.random() * (1 + 1));
            while (last_word.indexOf(brackets[pick]) == last_word.length - 1 | last_word.indexOf(brackets[pick]) == 0)
            {
                pick = Math.floor(Math.random() * (1 + 1));
            }
            if (pick == 0) {
                index = last_word.indexOf(brackets[pick]);
                letter = last_word[index - 1];
                last_word = last_word.replace(letter + brackets[pick], brackets[pick] + " ][");
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
                last_word = last_word.replace(brackets[pick] + letter, "][ " + brackets[pick]);
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
        sentence.html(break_up);
        break_up[0].visible();
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
     * Also now first checks to see which type of question you are on.
     */
    function correct_answer(trial, inputData) {
        var q = jQuery.grep(trial._stimulus_collection.models, function (e, i) {
            return e.attributes.handle == Sequence.quest;
        })[0];
        if (Sequence.quest == 'mc1' || Sequence.quest == 'mc2')
        {
            if (inputData.handle != 'a' && inputData.handle != 'b') return false;
        }
        else
        {
            if (inputData.handle != 'y' && inputData.handle != 'n') return false;
        }
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
     * Also now first checks to see which type of question you are on.
     */
    function incorrect_answer(trial, inputData) {
        var q = jQuery.grep(trial._stimulus_collection.models, function (e, i) {
            return e.attributes.handle == Sequence.quest;
        })[0];
        if (Sequence.quest == 'mc1' || Sequence.quest == 'mc2')
        {
            if (inputData.handle != 'a' && inputData.handle != 'b') return false;
        }
        else
        {
            if (inputData.handle != 'y' && inputData.handle != 'n') return false;
        }
        if(trial.data.neutral) {
            console.log("Incorrect answer to neutral.");
            if (! already_wrong_c)
            {
                if (q.attributes.data.neutralAnswer != inputData.handle)
                {
                    scorer.ct_c = scorer.ct_c + 1;
                    already_wrong_c = true;
                    console.log(scorer.ct_c + ' incorrect');
                }
            }
            return(q.attributes.data.neutralAnswer != inputData.handle);
        } else if(trial.data.positive) {
            console.log("Incorrect answer to positive.");
            if (! already_wrong_c)
            {
                if (q.attributes.data.positiveAnswer != inputData.handle)
                {
                    scorer.ct_c = scorer.ct_c + 1;
                    already_wrong_c = true;
                    console.log(scorer.ct_c + ' incorrect');
                }
            }
            return(q.attributes.data.positiveAnswer != inputData.handle);
        } else {
            console.log("Incorrect answer to negative.");
            if (! already_wrong_c)
            {
                if (q.attributes.data.negativeAnswer != inputData.handle)
                {
                    scorer.ct_c = scorer.ct_c + 1;
                    already_wrong_c = true;
                    console.log(scorer.ct_c + ' incorrect');
                }
            }
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
        console.log('IS IT ALREADY WRONG ' + already_wrong_s);
        console.log(lettersTyped);
        if (! result & ! already_wrong_s & ! on_question)
        {
            console.log(result);
            console.log(already_wrong_s);
            scorer.ct_s = scorer.ct_s + 1;
            already_wrong_s = true;
            console.log('THIS MANY CORRECT ' + Math.round(((40-scorer.ct_s)/40)*100));
        }
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
    API.addSettings('redirect', "../playerScript/completed/" + API.getGlobal().script);

    // Removes the warning about leaving the page.
    API.addSettings("hooks", {
        endTask: function () {
            window.onbeforeunload = null;
            window.location = "../playerScript/completed/" + API.getGlobal().script;
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
        ab: [
            {
                handle: 'ab',
                media: {html: "<div class='stim'>Please Type <b>A</b> &nbsp;  &nbsp;  &nbsp; <b>B</b></div>"},
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
                            {type: 'function', value: function (trial, inputData) {
                                handleNegation(trial);
                                chooseWords(trial);
                                return(splitSentences(trial));
                            }}
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
                        number_words = break_up[where_at-1][0].innerHTML;
                        var number_words = number_words.split(' ').length;
                        var wait  = number_words * 100;
                        console.log(wait);
                        if(inputData.latency - latency > wait) {
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
                            if (Sequence.add_extra_missing_letter)
                            {
                                $("div.press_space").text("Type the missing letters.");
                            }
                            else
                            {
                                $("div.press_space").text("Type the missing letter.");
                            }
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
                        console.log('GOING BACK INCORRECT');
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
                        console.log('GOING BACK CORRECT');
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
                        trialData.word = last_word;
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
                        handle: ['c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'v', 'w', 'x', 'z']
                    },
                    {type: 'setGlobalAttr', setter: {state: STATE_PAUSE}}, // sentence completed, pause.
                    {type: 'setInput', input: {handle: 'askQuestion', on: 'timeout', duration: 500}}
                ]
            },
            // After the statement is correctly completed, hide it, and show the question.
            {
                // Trigger when input handle is "end".
                conditions: [{type:'inputEquals',value:'askQuestion'}, {type:'globalEquals', property: 'quest', value:'yn'}],
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
            // After the statement is correctly completed, hide it, and show the mc1.
            {
                // Trigger when input handle is "end".
                conditions: [{type:'inputEquals',value:'askQuestion'}, {type:'globalEquals', property: 'quest', value:'mc1'}],
                actions: [
                    {type: 'setGlobalAttr', setter: {state: STATE_ASK_QUESTION}}, // sentence completed, show question.
                    {type:'custom',fn:function(options,eventData){
                        $("div.sentence").empty();
                    }},
                    {type:'showStim',handle : 'mc1'},
                    {type:'showStim',handle:'ab'},
                    {type:'setTrialAttr',setter:{correctOnQuestion:"true"}},  // set to true - will get set to false later if incorrectly answered.
                ]
            },
            // After the statement is correctly completed, hide it, and show the mc2.
            {
                // Trigger when input handle is "end".
                conditions: [{type:'inputEquals',value:'askQuestion'}, {type:'globalEquals', property: 'quest', value:'mc2'}],
                actions: [
                    {type: 'setGlobalAttr', setter: {state: STATE_ASK_QUESTION}}, // sentence completed, show question.
                    {type:'custom',fn:function(options,eventData){
                        $("div.sentence").empty();
                    }},
                    {type:'showStim',handle : 'mc2'},
                    {type:'showStim',handle:'ab'},
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
                    {type: 'hideStim', handle: 'ab'},
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
                    {type: 'hideStim', handle: 'ab'},
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
                    {type: 'removeInput', handle: ['y', 'n', 'a', 'b']},
                    {
                        type: 'setTrialAttr', setter: function (trialData, eventData) {
                          console.log("We have reached answered.");
                          trialData.question = $("div[data-handle='question']").text();
                          trialData.question_latency = Math.floor(eventData.latency);
                          if (trialData.first_question_latency == null) {
                              trialData.first_question_latency = Math.floor(eventData.latency);
                          }
                          already_wrong_c = false;
                          already_wrong_s = false;
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
                            inputData.handle == "Totally vivid") {
                            vivid_text = "Thanks. It's great you're really using your imagination!";
                        }
                        else if (inputData.handle == "Prefer not to answer")
                        {
                            vivid_text = '';
                        }
                        console.log(vivid_text);
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
                this.layout[0].media.html = '<div class="results"><p style="font-size: 24px; text-align:center">' + vivid_text + '</p>' + '<p style="font-size: 20px; text-align:center" > Press the spacebar to continue </p></div>';
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

    API.addTrialSets('results', [
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

                pct_ct_s = Math.round(((40-scorer.ct_s)/40)*100);
                pct_ct_c = Math.round(((40-scorer.ct_c)/40)*100);

                if (pct_ct_s >= 90)
                {
                    feed_back_s = 'You filled in the missing letters correctly on the first try ' + pct_ct_s + '% of the time this round. Great job, we’re really impressed with your ability to complete the stories!';
                }
                else if (pct_ct_s < 90 & pct_ct_s >= 70)
                {
                    feed_back_s = 'You filled in the missing letters correctly on the first try ' + pct_ct_s + '% of the time this round. You’re doing well, and we encourage you to pay really close attention to the stories to work out what letters are needed to complete the final words. This will allow you to get the most out of the training.';
                }
                else
                {
                    feed_back_s = 'You filled in the missing letters correctly on the first try ' + pct_ct_s + '%% of the time this round. We want to encourage you to pay really close attention to the stories to work out what letters are needed to complete the final words. This will allow you to get the most out of the training. If any aspect of the task is unclear, please email us with any questions at studyteam@mindtrails.org.';
                }

                if (pct_ct_c >= 90)
                {
                    feed_back_c = 'You answered the yes/no question following each story correctly on the first try ' + pct_ct_c + '% of the time this round. That’s terrific, and shows you’re paying really careful attention to the stories!';
                }
                else if (pct_ct_c < 90 & pct_ct_s >= 70)
                {
                    feed_back_c = 'You answered the yes/no question following each story correctly on the first try ' + pct_ct_c + '% of the time this round. You’re doing a good job, and we want to remind you to pay really close attention to the whole story each time, including how it ends, and just use the information in the story to answer the question. This will allow you to get the most out of the training.';
                }
                else
                {
                    feed_back_c = 'You answered the yes/no question following each story correctly on the first try ' + pct_ct_c + '% of the time this round. We want to remind you to pay really close attention to the whole story each time, including how it ends, and just use the information in the story to answer the question. This will allow you to get the most out of the training. If any aspect of the task is unclear, please email us with any questions at studyteam@mindtrails.org.';

                }
                this.layout[0].media.html = '<div class="results"><p style="font-size: 24px; text-align:center">' + feed_back_s + '</p><p style="font-size: 24px; text-align:center">' + feed_back_c + '</p>' + '<p style="font-size: 20px; text-align:center" > Press the spacebar to continue </p></div>';
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

    if (Sequence.training == true)
    {
        var vivids = []
        var vivid_afs = []
        var scens = []
        var seq = Sequence.sequence[2].data
        console.log(seq.length);
        for (var i = 0; i < seq.length; i++)
        {
            if (seq[i].inherit.set == 'vivid')
            {
                vivids.push(seq[i])
            }
            else if (seq[i].inherit.set == 'vivid_after')
            {
                vivid_afs.push(seq[i])
            }
            else if (seq[i].inherit.set == 'posneg' & scens.indexOf(seq[i]) == -1 )
            {
                scens.push(seq[i])
            }
        }
        scens = getRandomSubarray(scens, Sequence.display_length);
        console.log(Sequence.display_length);
        scens.splice(1, 0, vivids[0]);
        scens.splice(2, 0, vivid_afs[0]);
        scens.splice(4, 0, vivids[1]);
        scens.splice(5, 0, vivid_afs[1]);
        scens.splice((scens.length/2) + 2, 0, vivids[2]);
        scens.splice((scens.length/2) + 3, 0, vivid_afs[2]);
        Sequence.sequence[2].data = scens;
    }
    API.addSequence(Sequence.sequence);
    return API.script;

});
/* don't forget to close the define wrapper */