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
                    "neutralKey": "r",
                    "neutralWord": "ce[ ]eal",
                    "statement": "Your alarm goes off in the morning. You get out of bed and think about what you want to eat for breakfast. You decide you want to eat "
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
                    "inlineTemplate": "<div>Did you decide to have cereal?</div>"
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
                    "neutralKey": "m",
                    "neutralWord": "s[ ]all",
                    "statement": " You are preparing dinner. You cut your finger. The cut is "
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
                    "inlineTemplate": "<div>Were you preparing dinner? </div>"
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
                    "neutralWord": "posit[ ]on",
                    "statement": " You are watching TV. You are sitting in uncomfortable position. You adjust your "
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
                    "inlineTemplate": "<div>Were you watching TV? </div>"
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
                    "neutralWord": "m[ ]mories",
                    "statement": " You are talking with a friend over the phone. It has been a while since you last talked. You talk about old "
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
                    "inlineTemplate": "<div>Were you talking on the phone? </div>"
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
                    "neutralWord": "weeke[ ]d",
                    "statement": " You are talking on the phone with someone you want to go on a date with. You talk about what you did over the past weekend. You ask what they are doing over the "
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
                    "inlineTemplate": "<div>Did you talk about the past weekend? </div>"
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
                    "neutralKey": "d",
                    "neutralWord": "lou[ ]er",
                    "statement": " You are giving a presentation at work. One of your colleagues in the back of the room asks if you can speak up. You begin to speak "
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
                    "neutralKey": "a",
                    "neutralWord": "w[ ]rm",
                    "statement": " A light bulb blew out and you have to change it. You find a replacement bulb in your closet. As you start to unscrew the old bulb, it feels "
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
                    "inlineTemplate": "<div>Did you find a replacement bulb in your closet?</div>"
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
                    "neutralKey": "p",
                    "neutralWord": "ap[ ]les",
                    "statement": " You are going apple picking with a friend. You hope that they are having a good time. You pick twenty "
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
                    "inlineTemplate": "<div>Did you go apple picking? </div>"
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
                    "neutralWord": "s[ ]re",
                    "statement": " You are in a crowded lecture hall. Someone comes in and asks if they can sit next to you. You say "
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
                    "inlineTemplate": "<div>Was the lecture hall empty? </div>"
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
                    "neutralWord": "blu[ ]s",
                    "statement": " You are on a date. You go to a concert. The band plays "
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
                    "neutralKey": "c",
                    "neutralWord": "va[ ]ation",
                    "statement": "You are at a dinner party. You meet an interesting person. You talk about an upcoming "
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
                    "inlineTemplate": "<div>Did you know the person you were talking to?</div>"
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
                    "neutralWord": "act[ ]r",
                    "statement": " You are watching a movie. A loud noise makes you jump in your seat. The movie has your favorite "
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
                    "inlineTemplate": "<div>Were you watching a movie? </div>"
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
                    "neutralWord": "mi[ ]ror",
                    "statement": "You are going out  to the movies. You are meeting a friend. Before you leave your home, you look in the "
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
                    "inlineTemplate": "<div>Are you planning on going to the movies?</div>"
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
                    "neutralWord": "bal[ ]",
                    "statement": " You are playing softball with friends. It is your turn to bat. The first pitch is a "
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
                    "inlineTemplate": "<div>Were you playing hockey? </div>"
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
                    "neutralWord": "statisti[ ]s",
                    "statement": " You are in class listening to a lecture. You space out for a minute or two. The lecture is on "
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
                    "neutralKey": "d",
                    "neutralWord": "or[ ]er",
                    "statement": " You are on a date. You are getting dinner. There are a lot of options on the menu and you're unsure what to "
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
                    "inlineTemplate": "<div>Were you getting breakfast? </div>"
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
                    "neutralKey": "p",
                    "neutralWord": "s[ ]orts",
                    "statement": " You are at a friend's house. Some people you don't know are there. You watch "
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
                    "inlineTemplate": "<div>Did you know everyone there? </div>"
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
                    "neutralWord": "l[ ]mit",
                    "statement": "You are driving to work. You think that you will get to work early. You always drive at the speed "
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
                    "inlineTemplate": "<div>Were you driving to work?</div>"
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
                    "neutralWord": "lun[ ]h",
                    "statement": " You are visiting a foreign country for the first time. Everything around you feels different. You stop a restaurant for "
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
                    "inlineTemplate": "<div>Were you visiting a different state? </div>"
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
                    "neutralKey": "v",
                    "neutralWord": "pre[ ]iews",
                    "statement": "You are at the movies. You get a good seat. You enjoyed the "
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
                    "inlineTemplate": "<div>Were you at the store?</div>"
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
                    "statement": " You are at dinner. You order spicy food. It comes out in twenty "
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
                    "inlineTemplate": "<div>Were you at breakfast? </div>"
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
                    "neutralWord": "rat[ ]s",
                    "statement": " You are in a meeting with your colleagues. A topic you know a lot about comes up. The topic is interest "
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
                    "inlineTemplate": "<div>Were you at a meeting? </div>"
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
                    "neutralWord": "o[ ]t",
                    "statement": " You are taking a shower on a cold winter morning. You decide to take a longer shower than usual. Your hot water turns cold, so you get "
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
                    "inlineTemplate": "<div>Did you decide to stay in the shower?</div>"
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
                    "neutralWord": "si[ ]ver",
                    "statement": "You recently took pictures, and decide you would like to frame some of them. As you look through your photographs, you pick your favorite pictures and think about which frames you would like to use. You decide to use frames that are "
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
                    "inlineTemplate": "<div>Did you decide to frame recent pictures?/div>"
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
                    "neutralWord": "wall[ ]t",
                    "statement": "A band is performing at a local club. You purchase tickets weeks in advance. On the day of the concert, you put the tickets in your "
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
                    "inlineTemplate": "<div>Did your tickets in your pocket?</div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}},
            {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}
        ]
    },
    ]},
    { "inherit": { "set": "vivid" } },{ "inherit": { "set": "vivid_after" } },     {
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
                    "neutralKey": "r",
                    "neutralWord": "p[ ]oject",
                    "statement": " You are meeting with your boss. He sneezes and coughs during the meeting. You discuss your next "
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
                    "inlineTemplate": "<div>Were you meeting with your sister? </div>"
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
                    "neutralWord": "di[ ]ner",
                    "statement": "You are sitting on the couch channel surfing. You end up watching a cooking show. As you watch the chefs, you think about what you want to make for "
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
                    "inlineTemplate": "<div>Were you watching the news?</div>"
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
                    "neutralWord": "q[ ]ietly",
                    "statement": " You are in the library with a friend. You are talking about the book you are looking for and the librarian asks you to keep it down. You talk more "
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
                    "inlineTemplate": "<div>Were you in the library? </div>"
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
                    "neutralWord": "o[ ]der",
                    "statement": "You have dinner plans with some friends. You are going to your favorite restaurant. While you drive to the restaurant, you think about what you want to "
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
                    "inlineTemplate": "<div>Are you going to a restaurant with your parents?</div>"
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
                    "neutralWord": "hour[ ]",
                    "statement": " You are playing softball with friends. It Is the last play of the game and your heart is racing. You have been playing for two "
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
                    "inlineTemplate": "<div>Were you playing softball? </div>"
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
                    "neutralWord": "w[ ]rk",
                    "statement": " You are in a library the night before a big project is due. You are working very hard, but it is noisy in the library. You decide you are going to stay at the library and continue to "
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
                    "inlineTemplate": "<div>Did you decide to stay in the library?</div>"
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
                    "neutralWord": "not[ ]s",
                    "statement": " You are at work. You have to give a presentation that day. Before your presentation, you review your "
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
                    "inlineTemplate": "<div>Were you at school? </div>"
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
                    "neutralKey": "v",
                    "neutralWord": "mo[ ]ie",
                    "statement": " You are spending time with a relative. You go to the movies. You go to see an action "
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
                    "inlineTemplate": "<div>Did you go to the amusement park? </div>"
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
                    "neutralWord": "p[ ]pper",
                    "statement": "You are at the grocery store. You are at the cash register. You realize you forgot to buy "
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
                    "inlineTemplate": "<div>Did you forget to buy salt?</div>"
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
                    "neutralWord": "br[ ]ath",
                    "statement": " You are running to catch the bus. You make it just in time but you are out of breath. Once you sit down you catch your "
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
                    "inlineTemplate": "<div>Were you running to catch a plane? </div>"
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
                    "neutralWord": "po[ ]",
                    "statement": " You are cooking and slicing onions. Your eyes begin to water. You finish slicing the onions and put them in the "
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
                    "inlineTemplate": "<div>Did you slice onions? </div>"
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
                    "neutralWord": "di[ ]tionary",
                    "statement": "You are reading one night when you come across a word that you do not know. You decide to look up the word. You go get your "
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
                    "inlineTemplate": "<div>Were you reading in the morning?</div>"
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
                    "neutralWord": "pre[ ]s",
                    "statement": " You are at the gym with a friend. You are lifting weights together. You spot each other on the bench "
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
                    "neutralKey": "e",
                    "neutralWord": "y[ ]s",
                    "statement": "You are voting on an important proposition. There are two choices. You vote "
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
                    "inlineTemplate": "<div>Did you vote on the proposition?</div>"
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
                    "neutralWord": "gara[ ]e",
                    "statement": " You decide to do some home improvement tasks. You gather your tools from the garage, but you realize you forgot your hammer. You go back to get it from the "
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
                    "inlineTemplate": "<div>Did you forget a wrench?</div>"
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
                    "neutralWord": "swe[ ]ter",
                    "statement": " You are in your office, talking to your boss. You notice the air conditioning is on high, and it is cold. You wish you had a "
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
                    "inlineTemplate": "<div>Were you at home? </div>"
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
                    "neutralKey": "f",
                    "neutralWord": "be[ ]ore",
                    "statement": " Yesterday, you went to the gym. Today your arms feel sore. You did arm workouts the day "
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
                    "inlineTemplate": "<div>Did you go to the pool yesterday? </div>"
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
                    "neutralKey": "j",
                    "neutralWord": "[ ]ob",
                    "statement": " You are at a party. Your friend introduces you to their coworker. You talk about his "
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
                    "neutralKey": "b",
                    "neutralWord": "jo[ ]",
                    "statement": " You are meeting a new colleague. They seem like an interesting person. You talk about their last "
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
                    "inlineTemplate": "<div>Were you meeting with a colleague? </div>"
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
                    "neutralWord": "wat[ ]r",
                    "statement": " You are eating spicy food. You begin to sweat. You slow down and drink some "
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
                    "inlineTemplate": "<div>Were you eating spicy food? </div>"
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
                    "neutralWord": "W[ ]r",
                    "statement": " You are in class listening to a lecture. They person next to you asks if you could tell them what the lecturer was going over. You tell them they were going over the Revolutionary "
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
                    "inlineTemplate": "<div>Were you doing homework? </div>"
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
                    "neutralKey": "f",
                    "neutralWord": "per[ ]ormance",
                    "statement": "You are watching a movie. Your favorite actor comes on screen. You are pleased with their "
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
                    "inlineTemplate": "<div>Did the actor preform well?</div>"
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
                    "neutralWord": "w[ ]ter",
                    "statement": "You are eating dinner. You are thristy. You get a glass of "
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
                    "inlineTemplate": "<div>Did you get thirsty when you were eating dinner?</div>"
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
                    "neutralWord": "anym[ ]re",
                    "statement": " You just drank a full cup of coffee. You become jittery briefly. You are not tired "
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
                    "inlineTemplate": "<div>Did you drink a full cup of soda? </div>"
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
                    "neutralWord": "clo[ ]hes",
                    "statement": "You are cleaning your home. There is some clutter. You fold your "
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
                    "inlineTemplate": "<div>Were you cleaning your friend's house?</div>"
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