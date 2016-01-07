/* The script wrapper */
define(['pipAPI','pipScorer'], function(APIConstructor,Scorer) {

    jQuery.fn.visible = function() {
        return this.css('visibility', 'visible');
    };

    jQuery.fn.invisible = function() {
        return this.css('visibility', 'hidden');
    };

    var API = new APIConstructor();
    var scorer = new Scorer();
    var break_up;
    var text_to_display;
    var word_display;
    var where_at = 1;
    var latency = 0;
    var vivid_text;
    var scorer =
    {
        count : 1
    };

    var on_question = false;

    function increase_count(){
        scorer.count = scorer.count+1;
        return scorer.count;
    }

    /**
     * Returns the missing letters
     * @param trial
     * @returns a string containing the missing letters, based on if this is a positive or negative word.
     */
    function missing_letters(trial) {
        p = jQuery.grep(trial._stimulus_collection.models, function(e, i) {return e.attributes.handle == "paragraph"})[0]
        return (p.attributes.data.neutralKey);
    }

    /**
     * Returns true if missing letters start with the letters typed so far.
     * @param trial
     * @param inputData
     * @returns {*}
     */
    function correct_letters(trial,inputData) {
        if(!API.getGlobal().lettersTyped) API.addGlobal({lettersTyped:""});
        if(inputData.handle.length  > 1) return false;
        var lettersTyped = API.getGlobal().lettersTyped + inputData.handle;
        return missing_letters(trial).startsWith(lettersTyped);
    }

    /**
     * Returns true if the number of letters entered meets or exceeds the letters expected.
     * @param trial
     * @param inputData
     */
    function correct_length(trial, inputData) {
        if(!API.getGlobal().lettersTyped) API.addGlobal({lettersTyped:""});
        var lettersTyped = API.getGlobal().lettersTyped + inputData.handle;
        return lettersTyped.length >= missing_letters(trial).length
    }


    //** A custom condition, which returns true if the users entered a correct first letter
    // in a missing phrase, and false otherwise.
    function correct_all_letters(inputData) {
        // Set the missing letters to an empty string if it is undefined.
        if(!API.getGlobal().lettersTyped) API.addGlobal({lettersTyped:""});

        // If a single letter is typed, add it to the string containing users input
        if(inputData.handle.length == 1) {
            API.getGlobal().lettersTyped = API.getGlobal().lettersTyped + inputData.handle
        }

        // If the guess is too long, show an x, and let the user restart.
        if(inputData.length > 2) {
            API.getGlobal().lettersTyped = "";
        }
        current_trial = require('./app/trial/current_trial');
        c = current_trial()._stimulus_collection.whereData({"positiveKey":API.getGlobal().lettersTyped});
        return(c.length > 0);
    }

    // Warn people about leaving the page before they complete all the questions
    window.onbeforeunload = function() {
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
        proportions: {width:4,height:3},
        textSize: 5
    });

    // setting the way the logger works (how often we send data to the server and the url for the data)
    API.addSettings('logger',{
        pulse: 1,
        url : '/data',
        logger:
            function(trialData, inputData, actionData,logStack){
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
                    log_serial : logStack.length,
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
            {handle:'error',media:'X', css:{color:'#FF0000'}, location:{top:70}, nolog:true}
        ],
        yesno: [
            {   handle:'yesno',
                media:{html:"<div class='stim'>Please Type <b>Y</b>=Yes &nbsp;  &nbsp;  &nbsp; <b>N</b>=No</div>"},
                css: {color: '#333', fontSize: '.8em', position: 'absolute'},
                location:{bottom: 1}
            }
        ],
        stall: [
            {
                handle:'stall',
                media:{html:"<div class='stim'>Oops, that answer is incorrect; please re-read the question and in a moment you will have a chance to answer again.</div>"},
                css: {color: '#333', fontSize: '.8em', position: 'absolute'},
                location:{top:50},
                nolog:true}
        ],
        greatjob:
        [
            {
                handle:'greatjob',
                media:{html:"<div class='stim'>Great job!</div>"},
                nolog:true,
                css: {color: '#333', fontSize: '1.2em', position: 'absolute'},
                location:{bottom: 50}
            }
        ],
        press_space:
        [
            {
                handle: 'press_space',
                media: {html: "<div class='press_space'>Press the <b>Space Bar</b> to continue.</div>"},
                nolog: true,
                css: {color: '#333', fontSize: '.8em', position: 'absolute'},
                location: {bottom: 1}
            }
        ],
        vivid: [
            {media :{'inlineTemplate':"<div class='vivid'>_______</div>"}}
        ],
        counter: [
            {
                'handle': 'counter',
                customize: function () {
                    this.media = scorer.count + ' of 50';
                    on_question = false;
                },
                css: {color: '#333', fontSize: '.8em', position: 'absolute'},
                location: {bottom: 1,  right: 1}
            }
        ]
    });

    API.addTrialSets('base',[{
        input: [
            {handle:'a',on:'keypressed', key:"a"},
            {handle:'b',on:'keypressed', key:"b"},
            {handle:'c',on:'keypressed', key:"c"},
            {handle:'d',on:'keypressed', key:"d"},
            {handle:'e',on:'keypressed', key:"e"},
            {handle:'f',on:'keypressed', key:"f"},
            {handle:'g',on:'keypressed', key:"g"},
            {handle:'h',on:'keypressed', key:"h"},
            {handle:'i',on:'keypressed', key:"i"},
            {handle:'j',on:'keypressed', key:"j"},
            {handle:'k',on:'keypressed', key:"k"},
            {handle:'l',on:'keypressed', key:"l"},
            {handle:'m',on:'keypressed', key:"m"},
            {handle:'n',on:'keypressed', key:"n"},
            {handle:'o',on:'keypressed', key:"o"},
            {handle:'p',on:'keypressed', key:"p"},
            {handle:'q',on:'keypressed', key:"q"},
            {handle:'r',on:'keypressed', key:"r"},
            {handle:'s',on:'keypressed', key:"s"},
            {handle:'t',on:'keypressed', key:"t"},
            {handle:'u',on:'keypressed', key:"u"},
            {handle:'v',on:'keypressed', key:"v"},
            {handle:'w',on:'keypressed', key:"w"},
            {handle:'x',on:'keypressed', key:"x"},
            {handle:'y',on:'keypressed', key:"y"},
            {handle:'z',on:'keypressed', key:"z"},
            {handle:'space',on:'space'}
        ],
        interactions: [
            // Show the paragraph with missing letters as soon as the trial starts.
            {
                conditions: [{type:'begin'}],
                actions: [
                    {type:'showStim',handle:'paragraph'},
                    {type:'showStim',handle:'press_space'},
                    {type:'showStim', handle: 'counter'},
                    {type:'setGlobalAttr',setter:{askingQuestion:false}},
                    {type:'setTrialAttr',setter:{correctOnLetter:"true"}},  // set to true - will get set to false later if incorrectly answered.
                    {type:'custom',fn:function(options,eventData) {API.addGlobal({"original":$("span.incomplete").text()})}},
                    {type:'custom',fn:function(trial,inputData) {
                        var sentence = $("div.sentence");
                        break_up = $("div.sentence").text().split('.');
                        last_word = break_up[break_up.length-1].split(' ');
                        last_word = last_word[last_word.length-2] + ' ' + last_word[last_word.length-1];
                        break_up[break_up.length-1] = break_up[break_up.length-1].replace(last_word, "");
                        break_up.push(last_word);
                        for (i = 0; i < break_up.length; i++) {
                            if (i == break_up.length-1)
                            {
                                break_up[i] = $("<p class='incomplete'>" + break_up[i] + '.</p>')
                            }
                            else if (i == break_up.length-2)
                            {
                                break_up[i] = $("<p class='sentences'>" + break_up[i] + '</p>')
                            }
                            else
                            {
                                break_up[i] = $("<p class='sentences'>" + break_up[i] + '.</p>')
                            }
                            break_up[i].invisible();
                        }
                        sentence.html(break_up);
                        break_up[0].visible();
                    }
                    }]

            },

            {// The letters entered are incorrect
                conditions: [
                    {type:'globalEquals', property:'askingQuestion', value:false},
                    {type:'inputEquals',value:'askQuestion', negate: true},
                    {type:'inputEquals',value:'correct', negate: true},
                    {type:'function', value:function(trial, inputData){

                        if (where_at < break_up.length && inputData.handle == 'space' && inputData.latency - latency > 1000 && ! on_question)
                        {
                            var sentence = $("div.sentence");
                            if (where_at < (break_up.length - 2))
                            {
                                break_up[where_at].visible();

                            }
                            else if (where_at < (break_up.length - 1))
                            {
                                break_up[where_at].visible();
                            }
                            else
                            {
                                break_up[where_at].visible();
                                var space = $("div.press_space");
                                space.text("Type the missing letter.");
                                //space.before(break_up[where_at]);
                            }

                            latency = inputData.latency;
                            where_at = where_at + 1;
                        }
                        else if (where_at >= break_up.length)
                        {
                            latency = 0;
                            return true;
                        }
                    }},
                    {type:'function',value:function(trial,inputData){ return !correct_letters(trial, inputData) }}
                ],
                actions: [
                    {type:'custom',fn:function(options,eventData){
                        API.getGlobal().lettersTyped = "";
                        var span = $("span.incomplete");
                        span.text(API.getGlobal().original);
                    }},
                    {type:'showStim',handle:'error'},
                    {type:'setTrialAttr',setter:{correctOnLetter:"false"}},
                    {type:'setInput',input:{handle:'clear', on:'timeout',duration:500}}
                ]
            },
            {// The letters are correct so far...
                conditions: [
                    {type:'globalEquals', property:'askingQuestion', value:false},
                    {type:'function',value:function(trial,inputData){ return !correct_length(trial, inputData) }},
                    {type:'function',value:function(trial,inputData){ return correct_letters(trial, inputData) }}
                ],
                actions: [
                    {type:'custom',fn:function(options,eventData){
                        API.getGlobal().lettersTyped = API.getGlobal().lettersTyped + eventData.handle;
                        var span = $("p.incomplete");
                        var text = span.text().replace(' ', eventData["handle"]);
                        span.text(text);
                    }},
                    {type:'setTrialAttr',setter:function(trialData, eventData){
                        if(trialData.first_letter_latency == null) {
                            trialData.first_letter_latency = Math.floor(eventData.latency);
                        }
                    }}
                ]
            },
            {// All the letters are entered correctly
                conditions: [
                    {type:'globalEquals', property:'askingQuestion', value:false},
                    {type: 'function', value:function(trial, inputData)
                    {
                        if (where_at < break_up.length)
                        {
                            return false;
                        }
                        else
                        {
                            return true;
                        }
                    }},
                    {type:'function',value:function(trial,inputData){ return correct_length(trial, inputData) }},
                    {type:'function',value:function(trial,inputData){ return correct_letters(trial, inputData) }}
                ],
                actions: [
                    {type:'custom',fn:function(options,eventData){
                        var span = $("p.incomplete");
                        var text = span.text().replace(' ', eventData["handle"]);
                        span.text(text);
                        where_at = 1;
                        on_question = true;
                    }},
                    {type:'trigger',handle : 'correct'}
                ]
            },

            {
                // Trigger when the correct response is provided, as there are two interactions
                // that can cause this, I've separated it out into it's own section rather than
                // duplicate the code.
                conditions: [{type:'inputEquals',value:'correct'}],
                actions: [
                    // Preserve the question as completed, so that it will eventually be set back to the server.
                    {type:'setTrialAttr',setter:function(trialData, eventData){
                        trialData.word = $("span.incomplete").text();
                        trialData.paragraph = $("div[data-handle='paragraph']").text();
                        trialData.letter_latency  = Math.floor(eventData.latency);
                        if(trialData.first_letter_latency == null) {
                            trialData.first_letter_latency = Math.floor(eventData.latency);
                        }
                    }},
                    {type:'resetTimer'}, // Reset timer so that we can also collect the latency on the y/n respose
                    // Remove all keys but 'y' and 'n'
                    {type:'removeInput',handle : ['a','b','c','d','e','f','g','h','i','j','k','l','m','o','p','q','r','s','t','u','v','v','w','x','z']},
                    {type:'setInput',input:{handle:'askQuestion', on:'timeout',duration:500}}
                ]
            },
            // After the statement is correctly completed, hide it, and show the question.
            {
                // Trigger when input handle is "end".
                conditions: [{type:'inputEquals',value:'askQuestion'}],
                actions: [
                {type:'custom',fn:function(options,eventData){
                                    $("div.sentence").empty();
                                    }},
                    {type:'hideStim',handle : 'press_space'},
                    {type:'hideStim',handle : 'paragraph'},
                    {type:'showStim',handle : 'question'},
                    {type:'showStim',handle:'yesno'},
                    {type:'setTrialAttr',setter:{correctOnQuestion:"true"}},  // set to true - will get set to false later if incorrectly answered.
                    {type:'setGlobalAttr',setter:{askingQuestion:true}}
                ]
            },
            // Listen for a correct response to a positive question
            {
                conditions: [
                    {type:'inputEquals',value:['y', 'n']},
                    {type:'inputEqualsStim', property:'neutralAnswer'},
                    {type:'globalEquals', property:'askingQuestion', value:true}
                ],
                actions: [
                    {type:'hideStim',handle : 'question'},
                    {type:'hideStim',handle:'yesno'},
                    {type:'hideStim', handle: 'counter'},
                    {type:'showStim', handle:'greatjob'},
                    {type:'trigger',handle : 'answered', duration:1000},

                ]
            },
            // Listen for an incorrect response to a positive question
            {
                conditions: [
                    {type:'inputEquals',value:['y', 'n']},
                    {type:'inputEqualsStim', property:'neutralAnswer', negate:true},
                    {type:'globalEquals', property:'askingQuestion', value:true}
                ],
                actions: [
                    {type:'setTrialAttr',setter:function(trialData, eventData){
                        if(trialData.first_question_latency == null) {
                            trialData.first_question_latency = Math.floor(eventData.latency);
                        }
                    }},
                    {type:'removeInput', handle:'y'},
                    {type:'removeInput', handle:'n'},
                    {type:'hideStim', handle:'yesno'},
                    {type:'showStim',handle:'error'},
                    {type:'showStim',handle:'stall'},
                    {type:'setInput',input:{handle:'delay',on:'timeout',duration:5000}},
                    {type:'setTrialAttr',setter:{correctOnQuestion:"false"}},
                    {type:'setInput',input:{handle:'clear', on:'timeout',duration:500}}
                ]
            },
            {
                conditions: [{type: 'inputEquals', value: 'delay'}],
                actions: [
                    {type: 'setInput', input: {handle: 'y', on: 'keypressed', key: 'y'}},
                    {type: 'setInput', input: {handle: 'n', on: 'keypressed', key: 'n'}},
                    {type: 'showStim', handle: 'yesno'},
                    {type: 'hideStim', handle: 'stall'},
                ]
            },
            {
                // Trigger when the correct response is provided, as there are two interactions
                // that can cause this, I've separated it out into it's own section rather than
                // duplicate the code.
                conditions: [{type:'inputEquals',value:'answered'}],
                actions: [
                    {type:'setGlobalAttr',setter:function(){
                        increase_count();
                    }},
                    {type:'removeInput',handle : ['y','n']},
                    {type:'setTrialAttr',setter:function(trialData, eventData){
                        trialData.question = $("div[data-handle='question']").text();
                        trialData.question_latency  = Math.floor(eventData.latency);
                        if(trialData.first_question_latency == null) {
                            trialData.first_question_latency = Math.floor(eventData.latency);
                        }
                    }},
                    {type:'log'},
                    {type:'endTrial'}
                ]
            },

            // This interaction is triggered by a timeout after a incorrect response.
            // It allows us to delay the removal of the big red X.
            {
                // Trigger when input handle is "end".
                conditions: [
                    {type:'inputEquals',value:'clear'}],
                actions: [
                    {type:'removeInput',handle : 'clear'},
                    {type:'hideStim',handle:'error'}
                ]
            }


        ]

    }]);

    /**
     * This sets the ratio of positive to negative statements.  if there is one
     * true, and one false, it will be a 50/50 split.  If it is 3 true, and 1 false
     * if would then be a 75% positive, 25% negative split.
     */
    API.addTrialSets('neutral',[
        { inherit:'base', data: {neutral:true}}
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
                media : {template:"/PIPlayerScripts/vividness.html"}
            }
        ],
        stimuli: [
            {media :{'inlineTemplate':"<div class='vivid'>_______</div>"}}
        ],
        interactions: [
            {
                conditions: [{type: 'begin'}],
                actions: [{type: 'showStim', handle: 'All'}]
            },
            {
                conditions: [
                    {
                        type: 'function', value: function(trial, inputData) {
                        if (inputData.handle == 'Not at all vivid' ||
                                                inputData.handle == "Somewhat vivid"  ||
                                                inputData.handle == "Moderately vivid"
                        )
                        {
                            vivid_text = 'Thanks. Really try to use your imagination!';
                        }
                        else if (inputData.handle == "Very vivid"  ||
                                 inputData.handle == "Totally vivid" ||
                                 inputData.handle == "Prefer not to answer")
                        {
                            vivid_text = "Thanks. It's great you're really using your imagination!";
                        }
                        return( inputData.handle == "Not at all vivid" ||
                        inputData.handle == "Somewhat vivid"  ||
                        inputData.handle == "Moderately vivid" ||
                        inputData.handle == "Very vivid"  ||
                        inputData.handle == "Totally vivid" ||
                        inputData.handle == "Prefer not to answer")
                    }
                    }
                ],
                actions: [
                    {type:'custom',fn:function(options,eventData){
                        var span = $("div.vivid");
                        var text = span.text().replace('_______', eventData["handle"]);
                        span.text(text);                    }},
                    {type:'setTrialAttr',setter:function(trialData, eventData) {
                        trialData.vividness = eventData["handle"];
                    }},
                    {type:'trigger', handle:'vivid_switch',  on: 'timeout', duration: 500}
                ]
            },
            {
                conditions: [{type:'inputEquals', value:'vivid_switch'}],
                actions: [{type:'log'}, {type:'endTrial'}]
            },
        ]
    },
    ]);

    API.addTrialSets('vivid_after', [
    {
        input: [
            // What input to accept from the participant (user)
            {handle:'space',on:'space'}
        ],
        layout: [
            {
                media : {html:''}
            }
        ],
        customize: function () {
            this.layout[0].media.html = '<div class="thanks"><p style="font-size: 24px; text-align:center">' + vivid_text + '</p>' + '<p style="font-size: 20px; text-align:center" > Press the spacebar to continue </p></div>';

        },
        interactions: [
            // What to do when different events occur.
            {
                conditions: [
                    {type:'inputEquals',value:'space'}
                ],
                actions: [
                    {type:'endTrial'}
                ]
            }
        ]
    }
    ]);



    API.addSequence([
        {
            input: [
                {handle:'space',on:'space'}
            ],
            layout: [
                // This is a stimulus object
                {
                    media : {template:"/PIPlayerScripts/intro.html"}
                }
            ],
            interactions: [
                // This is an interaction (it has a condition and an action)
                {
                    conditions: [
                        {type:'inputEquals',value:'space'}
                    ],
                    actions: [
                        {type:'endTrial'}
                    ]
                }
            ]
        },
        {
            mixer:'random',
            data:[

    {
        "inherit": {
            "set": "neutral",
            "type": "random"
        },
        "stimuli": [
            {
                "inherit": {
                    "set": "error"
                }
            },
            {
                "data": {
                    "neutralKey": "n",
                    "neutralWord": "pa[ ]ts",
                    "statement": "You are shopping for clothes. There is a sale. You buy a pair of "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "y"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Did you decide to buy a pair of pants?</div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}},
            {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}

        ]
    },
            ]
        },
        { "inherit": { "set": "vivid" } },{ "inherit": { "set": "vivid_after" } },

        {
            mixer: 'random',
            //n: 50,  // The total number of randomly selected trials to run.
            data: [
    {
        "inherit": {
            "set": "neutral",
            "type": "random"
        },
        "stimuli": [
            {
                "inherit": {
                    "set": "error"
                }
            },
            {
                "data": {
                    "neutralKey": "n",
                    "neutralWord": "hurti[ ]g",
                    "statement": " You are going for a jog. Your leg starts to hurt. You keep running and it stops "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "y"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Were you going for a jog? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}},
            {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}

        ]
    },
            ]
        },
        { "inherit": { "set": "vivid" } },{ "inherit": { "set": "vivid_after" } },

        {
            mixer: 'random',
            //n: 50,  // The total number of randomly selected trials to run.
            data: [
    {
        "inherit": {
            "set": "neutral",
            "type": "random"
        },
        "stimuli": [
            {
                "inherit": {
                    "set": "error"
                }
            },
            {
                "data": {
                    "neutralKey": "i",
                    "neutralWord": "wh[ ]le",
                    "statement": " You are jogging with a friend. You have been jogging for a while. You stop running and talk for a "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "y"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Were you jogging? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}},
            {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}

        ]
    },
    {
        "inherit": {
            "set": "neutral",
            "type": "random"
        },
        "stimuli": [
            {
                "inherit": {
                    "set": "error"
                }
            },
            {
                "data": {
                    "neutralKey": "a",
                    "neutralWord": "p[ ]int",
                    "statement": "You are at the hardware store. You need to build a fence. You buy "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "n"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Were you at the grocery store?</div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}},
            {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}

        ]
    },
    {
        "inherit": {
            "set": "neutral",
            "type": "random"
        },
        "stimuli": [
            {
                "inherit": {
                    "set": "error"
                }
            },
            {
                "data": {
                    "neutralKey": "e",
                    "neutralWord": "siz[ ]",
                    "statement": " You are bowling with friends. You forgot to bring your bowling shoes. You hope that they have shoes in your "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "y"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Were you bowling? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}},
            {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}

        ]
    },
    {
        "inherit": {
            "set": "neutral",
            "type": "random"
        },
        "stimuli": [
            {
                "inherit": {
                    "set": "error"
                }
            },
            {
                "data": {
                    "neutralKey": "w",
                    "neutralWord": "marshmallo[ ]s",
                    "statement": " You are camping with some friends. You set up your tents and build a fire. You roast "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "y"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Were you camping? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}},
            {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}

        ]
    },
    {
        "inherit": {
            "set": "neutral",
            "type": "random"
        },
        "stimuli": [
            {
                "inherit": {
                    "set": "error"
                }
            },
            {
                "data": {
                    "neutralKey": "t",
                    "neutralWord": "cha[ ]",
                    "statement": " You are going for a walk. You see a friend coming from the other direction. You stop for a bit and "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "n"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Were you going for a run? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}},
            {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}

        ]
    },
    {
        "inherit": {
            "set": "neutral",
            "type": "random"
        },
        "stimuli": [
            {
                "inherit": {
                    "set": "error"
                }
            },
            {
                "data": {
                    "neutralKey": "w",
                    "neutralWord": "ro[ ]",
                    "statement": " You are at a concert. There are a lot of people there. You are in the third "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "y"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Were you at a concert? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}},
            {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}

        ]
    },
    {
        "inherit": {
            "set": "neutral",
            "type": "random"
        },
        "stimuli": [
            {
                "inherit": {
                    "set": "error"
                }
            },
            {
                "data": {
                    "neutralKey": "s",
                    "neutralWord": "hi[ ]tory",
                    "statement": "You are at a bookstore. You pick out several books you want to buy. You decide to read a book about American history "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "y"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Did you buy a book?</div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}},
            {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}

        ]
    },
    {
        "inherit": {
            "set": "neutral",
            "type": "random"
        },
        "stimuli": [
            {
                "inherit": {
                    "set": "error"
                }
            },
            {
                "data": {
                    "neutralKey": "i",
                    "neutralWord": "m[ ]lk",
                    "statement": " You go to the grocery store. While you are there, you buy eggs, bread, and juice. You forget to purchase "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "n"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Did you forget to purchase eggs?</div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}},
            {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}

        ]
    },
    {
        "inherit": {
            "set": "neutral",
            "type": "random"
        },
        "stimuli": [
            {
                "inherit": {
                    "set": "error"
                }
            },
            {
                "data": {
                    "neutralKey": "r",
                    "neutralWord": "[ ]ow",
                    "statement": " You are at the movies. You arrived late and you're not sure you'll find a seat. You find a seat in the fourth "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "n"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Were you at the mall? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}},
            {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}

        ]
    },
    {
        "inherit": {
            "set": "neutral",
            "type": "random"
        },
        "stimuli": [
            {
                "inherit": {
                    "set": "error"
                }
            },
            {
                "data": {
                    "neutralKey": "c",
                    "neutralWord": "su[ ]cesful",
                    "statement": "You are parallel parking your car. You attempt to back into the spot slowly but you gently hit the curb on accident. After a few more tries, you are "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "y"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Did you hit the curb?</div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}},
            {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}

        ]
    },
    {
        "inherit": {
            "set": "neutral",
            "type": "random"
        },
        "stimuli": [
            {
                "inherit": {
                    "set": "error"
                }
            },
            {
                "data": {
                    "neutralKey": "n",
                    "neutralWord": "mi[ ]utes",
                    "statement": " You are going to the movies with a friend. The theater is crowded, and your friend goes to use the bathroom and asks if you can save their seat. Your friend comes back in five "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "y"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Was the theater crowded? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}},
            {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}

        ]
    },
    {
        "inherit": {
            "set": "neutral",
            "type": "random"
        },
        "stimuli": [
            {
                "inherit": {
                    "set": "error"
                }
            },
            {
                "data": {
                    "neutralKey": "o",
                    "neutralWord": "ro[ ]m",
                    "statement": "You just arrived at the gym. You decide to lift weights, and then do some cardio. You walk over to the weight "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "y"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Did you decide to lift weights first?</div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}},
            {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}

        ]
    },
    {
        "inherit": {
            "set": "neutral",
            "type": "random"
        },
        "stimuli": [
            {
                "inherit": {
                    "set": "error"
                }
            },
            {
                "data": {
                    "neutralKey": "e",
                    "neutralWord": "ar[ ]a",
                    "statement": " You are at a basketball game. The crowd is cheering loudly and your head hurts. You got to a quiet "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "n"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Were you at a soccer match? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}},
            {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}

        ]
    },
    {
        "inherit": {
            "set": "neutral",
            "type": "random"
        },
        "stimuli": [
            {
                "inherit": {
                    "set": "error"
                }
            },
            {
                "data": {
                    "neutralKey": "i",
                    "neutralWord": "stat[ ]on",
                    "statement": " You are traveling with some friends and stop at a gas station to use the bathroom. It is very dirty. You decide to leave the gas "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "y"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Was the bathroom dirty? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}},
            {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}

        ]
    },
    {
        "inherit": {
            "set": "neutral",
            "type": "random"
        },
        "stimuli": [
            {
                "inherit": {
                    "set": "error"
                }
            },
            {
                "data": {
                    "neutralKey": "a",
                    "neutralWord": "ch[ ]t",
                    "statement": " You are at the park and see a colleague from work. They are with their family and you wonder if you should say hello. They noticed and stop by to "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "y"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Were you at the park? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}},
            {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}

        ]
    },
    {
        "inherit": {
            "set": "neutral",
            "type": "random"
        },
        "stimuli": [
            {
                "inherit": {
                    "set": "error"
                }
            },
            {
                "data": {
                    "neutralKey": "s",
                    "neutralWord": "out[ ]ide",
                    "statement": " You are seeing an old friend. You go to the beach. It is hot "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "n"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Did you go to the pool? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}},
            {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}

        ]
    },
    {
        "inherit": {
            "set": "neutral",
            "type": "random"
        },
        "stimuli": [
            {
                "inherit": {
                    "set": "error"
                }
            },
            {
                "data": {
                    "neutralKey": "u",
                    "neutralWord": "la[ ]ndry",
                    "statement": "You are packing for your winter vacation. As you fold your clothes, you remember your favorite sweater is dirty. You decide you should do "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "n"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Did you find that your favorite pants were dirty?</div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}},
            {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}

        ]
    },
    {
        "inherit": {
            "set": "neutral",
            "type": "random"
        },
        "stimuli": [
            {
                "inherit": {
                    "set": "error"
                }
            },
            {
                "data": {
                    "neutralKey": "y",
                    "neutralWord": "shortl[ ]",
                    "statement": " You are at the dentist for a cleaning. You check in with the receptionist. She says the dentist will see you "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "n"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Were you at the dentist for teeth whitening? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}},
            {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}

        ]
    },
    {
        "inherit": {
            "set": "neutral",
            "type": "random"
        },
        "stimuli": [
            {
                "inherit": {
                    "set": "error"
                }
            },
            {
                "data": {
                    "neutralKey": "n",
                    "neutralWord": "fur[ ]iture",
                    "statement": "You are listening to the radio. There is a commericial. It is an advertisement for "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "y"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div> Is the advertisement for furniture?</div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}},
            {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}

        ]
    },
    {
        "inherit": {
            "set": "neutral",
            "type": "random"
        },
        "stimuli": [
            {
                "inherit": {
                    "set": "error"
                }
            },
            {
                "data": {
                    "neutralKey": "a",
                    "neutralWord": "l[ ]mp",
                    "statement": " You spent a lot of time arranging the furniture in your new apartment. One day when you come home, you decide to rearrange some things. You decide to move the "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "y"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Did you decide to move the lamp?</div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}},
            {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}

        ]
    },
    {
        "inherit": {
            "set": "neutral",
            "type": "random"
        },
        "stimuli": [
            {
                "inherit": {
                    "set": "error"
                }
            },
            {
                "data": {
                    "neutralKey": "s",
                    "neutralWord": "te[ ]ts",
                    "statement": " You are at the doctor's office. He says that he would like to run some tests and asks for your permission. You say that it is fine if he runs some "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "y"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Were you at the office of your doctor? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}},
            {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}

        ]
    },
    {
        "inherit": {
            "set": "neutral",
            "type": "random"
        },
        "stimuli": [
            {
                "inherit": {
                    "set": "error"
                }
            },
            {
                "data": {
                    "neutralKey": "n",
                    "neutralWord": "u[ ]derstand",
                    "statement": " You are giving a presentation at work. A coworker asks you to clarify a point you made. You go back over the point and make sure they "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "y"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Were you giving a presentation? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}},
            {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}

        ]
    },
    {
        "inherit": {
            "set": "neutral",
            "type": "random"
        },
        "stimuli": [
            {
                "inherit": {
                    "set": "error"
                }
            },
            {
                "data": {
                    "neutralKey": "o",
                    "neutralWord": "sh[ ]rt",
                    "statement": " You are on a boat. The water is a bit rough. The ride is "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "y"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Were you on a boat? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}},
            {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}

        ]
    },
    ]},
    { "inherit": { "set": "vivid" } },{ "inherit": { "set": "vivid_after" } },
    {
 	mixer: 'random',
 		    data:[
    {
        "inherit": {
            "set": "neutral",
            "type": "random"
        },
        "stimuli": [
            {
                "inherit": {
                    "set": "error"
                }
            },
            {
                "data": {
                    "neutralKey": "n",
                    "neutralWord": "fa[ ]",
                    "statement": " You are at a party. You are warm and you begin to sweat. You go and stand near a "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "y"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Were you at a party? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}},
            {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}

        ]
    },
    {
        "inherit": {
            "set": "neutral",
            "type": "random"
        },
        "stimuli": [
            {
                "inherit": {
                    "set": "error"
                }
            },
            {
                "data": {
                    "neutralKey": "t",
                    "neutralWord": "his[ ]ory",
                    "statement": " You are listening to a talk at a conference. You think the talk should be helpful for your work. The subject of the talk is "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "n"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Is the subject of the talk science?</div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}},
            {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}

        ]
    },
    {
        "inherit": {
            "set": "neutral",
            "type": "random"
        },
        "stimuli": [
            {
                "inherit": {
                    "set": "error"
                }
            },
            {
                "data": {
                    "neutralKey": "a",
                    "neutralWord": "aw[ ]y",
                    "statement": " You are walking on a trail with your friends. One of your friends points out a beautiful bird as it lands in a nearby tree. As you turn your head to look, it flies "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "n"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Did you see a cat?</div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}},
            {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}

        ]
    },
    {
        "inherit": {
            "set": "neutral",
            "type": "random"
        },
        "stimuli": [
            {
                "inherit": {
                    "set": "error"
                }
            },
            {
                "data": {
                    "neutralKey": "s",
                    "neutralWord": "end[ ]",
                    "statement": " You are driving on a bumpy road. You are driving to go to the grocery store. Finally, the bumpy road "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "n"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Were you walking on a road? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}},
            {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}

        ]
    },
    {
        "inherit": {
            "set": "neutral",
            "type": "random"
        },
        "stimuli": [
            {
                "inherit": {
                    "set": "error"
                }
            },
            {
                "data": {
                    "neutralKey": "t",
                    "neutralWord": "wa[ ]er",
                    "statement": " You are eating a sandwich. You take a large bite and have trouble swallowing it. You take a drink of "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "n"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Were you eating ice cream? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}},
            {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}

        ]
    },
    {
        "inherit": {
            "set": "neutral",
            "type": "random"
        },
        "stimuli": [
            {
                "inherit": {
                    "set": "error"
                }
            },
            {
                "data": {
                    "neutralKey": "x",
                    "neutralWord": "e[ ]ploration",
                    "statement": " You are at a conference and meet someone in your field who you think is very smart. They are an interesting person. You discuss space "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "y"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Were you at a conference? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}},
            {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}

        ]
    },
    {
        "inherit": {
            "set": "neutral",
            "type": "random"
        },
        "stimuli": [
            {
                "inherit": {
                    "set": "error"
                }
            },
            {
                "data": {
                    "neutralKey": "g",
                    "neutralWord": "lon[ ]",
                    "statement": " You are going fishing and have just caught a fish. While you take the fish of the hook, the hook pricks your hand. The fish is 8 inches "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "y"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Did you go fishing? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}},
            {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}

        ]
    },
    {
        "inherit": {
            "set": "neutral",
            "type": "random"
        },
        "stimuli": [
            {
                "inherit": {
                    "set": "error"
                }
            },
            {
                "data": {
                    "neutralKey": "e",
                    "neutralWord": "r[ ]st",
                    "statement": " You are going for a run after not exercising for a long time. You feel slightly out of shape. You stop and "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "y"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Were you exercising? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}},
            {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}

        ]
    },
    {
        "inherit": {
            "set": "neutral",
            "type": "random"
        },
        "stimuli": [
            {
                "inherit": {
                    "set": "error"
                }
            },
            {
                "data": {
                    "neutralKey": "i",
                    "neutralWord": "ju[ ]ce",
                    "statement": "You are trying to decide what to have for lunch. You thought you had leftovers from the night before but you cannot find them in the fridge. You realize that the leftovers are behind the "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "n"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Are the leftovers behind the milk?</div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}},
            {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}

        ]
    },
    {
        "inherit": {
            "set": "neutral",
            "type": "random"
        },
        "stimuli": [
            {
                "inherit": {
                    "set": "error"
                }
            },
            {
                "data": {
                    "neutralKey": "x",
                    "neutralWord": "e[ ]pensive",
                    "statement": " You are in line at the grocery store. You talk with the person in front of you about the price of lobster. You agree that it is "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "y"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Were you at the grocery store? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}},
            {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}

        ]
    },
    {
        "inherit": {
            "set": "neutral",
            "type": "random"
        },
        "stimuli": [
            {
                "inherit": {
                    "set": "error"
                }
            },
            {
                "data": {
                    "neutralKey": "a",
                    "neutralWord": "pl[ ]te",
                    "statement": "You are cleaning dishes from dinner. You get out soap and a sponge. The first dish you clean is a "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "n"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div> Is the first dish you clean a bowl?</div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}},
            {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}

        ]
    },
    {
        "inherit": {
            "set": "neutral",
            "type": "random"
        },
        "stimuli": [
            {
                "inherit": {
                    "set": "error"
                }
            },
            {
                "data": {
                    "neutralKey": "c",
                    "neutralWord": "so[ ]ks",
                    "statement": "You are taking a getting dressed. You are going out for dinner. You put on your "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "y"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Are you putting on your socks before you go out to dinner?</div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}},
            {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}

        ]
    },
    {
        "inherit": {
            "set": "neutral",
            "type": "random"
        },
        "stimuli": [
            {
                "inherit": {
                    "set": "error"
                }
            },
            {
                "data": {
                    "neutralKey": "o",
                    "neutralWord": "st[ ]re",
                    "statement": "You are getting ready to do your laundry. You realize you do not have any detergent. Because of this, you have to go to the "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "n"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Are you getting ready to wash dishes?</div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}},
            {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}

        ]
    },
    {
        "inherit": {
            "set": "neutral",
            "type": "random"
        },
        "stimuli": [
            {
                "inherit": {
                    "set": "error"
                }
            },
            {
                "data": {
                    "neutralKey": "r",
                    "neutralWord": "comfo[ ]table",
                    "statement": "You are relaxing on the couch. You were at work late. The couch is "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "y"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Did you end up working late that night?</div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}},
            {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}

        ]
    },
    {
        "inherit": {
            "set": "neutral",
            "type": "random"
        },
        "stimuli": [
            {
                "inherit": {
                    "set": "error"
                }
            },
            {
                "data": {
                    "neutralKey": "s",
                    "neutralWord": "sun[ ]hine",
                    "statement": "You are watering your household plants. As you make your way around the house, you notice that one of your plants is wilting. You decide to move the plant into more direct "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "y"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Did you decide to move the plant?</div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}},
            {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}

        ]
    },
    {
        "inherit": {
            "set": "neutral",
            "type": "random"
        },
        "stimuli": [
            {
                "inherit": {
                    "set": "error"
                }
            },
            {
                "data": {
                    "neutralKey": "e",
                    "neutralWord": "qui[ ]t",
                    "statement": " You are at the movies. You are watching the previews and the person next to you is talking. Once the movie starts, they are "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "y"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Were you at the movies? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}},
            {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}

        ]
    },
    {
        "inherit": {
            "set": "neutral",
            "type": "random"
        },
        "stimuli": [
            {
                "inherit": {
                    "set": "error"
                }
            },
            {
                "data": {
                    "neutralKey": "t",
                    "neutralWord": "reserva[ ]ion",
                    "statement": " You made dinner reservations at a new restaurant in town. When you arrive with your date, the hostess says they can't find your reservation. They keep looking and eventually find your "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "n"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Did you make lunch reservations? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}},
            {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}

        ]
    },
    {
        "inherit": {
            "set": "neutral",
            "type": "random"
        },
        "stimuli": [
            {
                "inherit": {
                    "set": "error"
                }
            },
            {
                "data": {
                    "neutralKey": "l",
                    "neutralWord": "wel[ ]",
                    "statement": " You are in class and you are confused about what the lecturer is talking about. You raise your hand and ask if they could clarify. The student next to you says they were confused as "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "y"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Were you in class? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}},
            {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}

        ]
    },
    {
        "inherit": {
            "set": "neutral",
            "type": "random"
        },
        "stimuli": [
            {
                "inherit": {
                    "set": "error"
                }
            },
            {
                "data": {
                    "neutralKey": "g",
                    "neutralWord": "walkin[ ]",
                    "statement": " You are taking a hike. You stub your toe. You keep "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "y"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Did you stub your toe? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}},
            {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}

        ]
    },
    {
        "inherit": {
            "set": "neutral",
            "type": "random"
        },
        "stimuli": [
            {
                "inherit": {
                    "set": "error"
                }
            },
            {
                "data": {
                    "neutralKey": "g",
                    "neutralWord": "eg[ ]s",
                    "statement": "You are eating breakfast. Your meal tastes good. You eat scrambled "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "y"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Were you eating breakfast?</div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}},
            {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}

        ]
    },
    {
        "inherit": {
            "set": "neutral",
            "type": "random"
        },
        "stimuli": [
            {
                "inherit": {
                    "set": "error"
                }
            },
            {
                "data": {
                    "neutralKey": "u",
                    "neutralWord": "sa[ ]ce",
                    "statement": "You decide to make some pasta for dinner. You start to boil some water in a pot. As the water boils, you start to make some "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "n"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Did you decide to make salad for dinner?</div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}},
            {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}

        ]
    },
    {
        "inherit": {
            "set": "neutral",
            "type": "random"
        },
        "stimuli": [
            {
                "inherit": {
                    "set": "error"
                }
            },
            {
                "data": {
                    "neutralKey": "c",
                    "neutralWord": "s[ ]ratchy",
                    "statement": " You are going to an upcoming outdoor concert with friends. You bring blankets to sit on, and a bottle of water. You notice the blanket feels "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "n"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Was the blanket comfortable?</div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}},
            {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}

        ]
    },
    {
        "inherit": {
            "set": "neutral",
            "type": "random"
        },
        "stimuli": [
            {
                "inherit": {
                    "set": "error"
                }
            },
            {
                "data": {
                    "neutralKey": "o",
                    "neutralWord": "to[ ]thbrush",
                    "statement": "You are going on vacation. You plan to drive to the beach. You realize you forgot your "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "n"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Did you forget your hairbrush?</div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}},
            {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}

        ]
    },
    {
        "inherit": {
            "set": "neutral",
            "type": "random"
        },
        "stimuli": [
            {
                "inherit": {
                    "set": "error"
                }
            },
            {
                "data": {
                    "neutralKey": "n",
                    "neutralWord": "anythi[ ]g",
                    "statement": "You are washing dishes. Your hand is slippery with soap and you are careful not to drop any dishes. You do not drop "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "n"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Did you end up dropping a dish?</div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}},
            {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}

        ]
    },
    {
        "inherit": {
            "set": "neutral",
            "type": "random"
        },
        "stimuli": [
            {
                "inherit": {
                    "set": "error"
                }
            },
            {
                "data": {
                    "neutralKey": "a",
                    "neutralWord": "t[ ]lking",
                    "statement": " You are talking with a new coworker. There is a lull in the conversation. You switch topics and continue "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "n"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Were you talking with your boss? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}},
            {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}

        ]
    }
            ]
         },
        {
            "inherit": {"set": "vivid"},
            layout: [{media: {template: "/PIPlayerScripts/vividness_last.html"}}]
        }
    ]);
    return API.script;
});
/* don't forget to close the define wrapper */