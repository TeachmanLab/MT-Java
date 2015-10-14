/* The script wrapper */
define(['pipAPI','pipScorer'], function(APIConstructor,Scorer) {

    var API = new APIConstructor();
    var scorer = new Scorer();

    var scorer =
    {
        count : 1
    };

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

    function get_neutral_key(trial)
    {
        q = jQuery.grep(trial._stimulus_collection.models, function(e, i) {return e.attributes.handle == "question"})[0]
        return (q.attributes.data.neutralAnswer);
    }

    function correct_neutral(trial, inputData)
    {
        if(!API.getGlobal().lettersTyped) API.addGlobal({lettersTyped:""});
        if(inputData.handle.length  > 1) return false;
        var lettersTyped = API.getGlobal().lettersTyped + inputData.handle;
        return get_neutral_key(trial) == lettersTyped;
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

//    This was added to redirect back
    API.addSettings('redirect', "../playerScript/completed/int_train");

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
            {handle:'error',media:'X', css:{fontSize:'20px',color:'#FF0000'}, location:{top:70}, nolog:true}
        ],
        yesno: [
            {handle:'yesno',media:{html:"<div class='stim'><b>Y</b>=Yes &nbsp;  &nbsp;  &nbsp; <b>N</b>=No</div>"}, css:{fontSize:'20px',color:'black', 'text-align':'center'}, location:{top:70}}
        ],
        stall: [
            {handle:'stall',media:{html:"<div class='stim'>Oops, that answer is incorrect; please re-read the question and in a moment you will have a chance to answer again.</div>"}, css:{fontSize:'20px',color:'black', 'text-align':'center'}, location:{top:70}, nolog:true}
        ],
        vivid: [
            {media :{'inlineTemplate':"<div class='vivid'>_______</div>"}}
        ],
        counter: [
            {
                'handle': 'counter',
                customize: function () {
                    this.media = scorer.count + ' of 50';
                },
                css: {fontSize: '12px', 'text-align': 'center'},
                location:{bottom:'200px'}
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
                    {type:'showStim', handle: 'counter'},
                    {type:'setGlobalAttr',setter:{askingQuestion:false}},
                    {type:'setTrialAttr',setter:{correctOnLetter:"true"}},  // set to true - will get set to false later if incorrectly answered.
                    {type:'custom',fn:function(options,eventData) {API.addGlobal({"original":$("span.incomplete").text()})}}]
            },

            {// The letters entered are incorrect
                conditions: [
                    {type:'globalEquals', property:'askingQuestion', value:false},
                    {type:'inputEquals',value:'askQuestion', negate: true},
                    {type:'inputEquals',value:'correct', negate: true},
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
                        var span = $("span.incomplete");
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
                    {type:'function',value:function(trial,inputData){ return correct_length(trial, inputData) }},
                    {type:'function',value:function(trial,inputData){ return correct_letters(trial, inputData) }}
                ],
                actions: [
                    {type:'custom',fn:function(options,eventData){
                        var span = $("span.incomplete");
                        var text = span.text().replace(' ', eventData["handle"]);
                        span.text(text);
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
                    {type:'hideStim',handle : 'paragraph'},
                    {type:'showStim',handle : 'question'},
                    {type:'showStim',handle:'yesno'},
                    {type:'setTrialAttr',setter:{correctOnQuestion:"true"}},  // set to true - will get set to false later if incorrectly answered.
                    {type:'setGlobalAttr',setter:{askingQuestion:true}}
                ]
            },
            // Listen for a correct response to a positive question
            {
                conditions: [{type:'inputEqualsStim', property:'neutralAnswer'},
                    {type:'globalEquals', property:'askingQuestion', value:true},
                    {type:'function',value:function(trial,inputData){
                        correct = correct_neutral(trial, inputData);
                        console.log(correct);
                        return correct;
                    }}
                ],
                actions: [
                    {type:'hideStim',handle : 'question'},
                    {type:'hideStim',handle:'yesno'},
                    {type:'hideStim', handle: 'counter'},
                    {type:'trigger',handle : 'answered', duration:500}
                ]
            },
            // Listen for an incorrect response to a positive question
            {
                conditions: [{type:'inputEquals',value:['y', 'n']},
                    {type:'globalEquals', property:'askingQuestion', value:true},
                    {type:'function', value:function(trial,inputData){
                        incorrect = !correct_neutral(trial, inputData);
                        console.log(incorrect);
                        return incorrect;
                    }
                    }
                ],
                actions: [
                    {type:'setTrialAttr',setter:function(trialData, eventData){
                        console.log('hi');
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
    }]);


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
                    "neutralKey": "i",
                    "neutralWord": "runn[ ]ng",
                    "statement": " You are playing basketball. You are running and trip. You get back up and continue "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "n"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Were you playing soccer? </div>"
                }
            },
            {
                "inherit": {
                    "set": "yesno"
                }
            },
            {
                "inherit": {
                    "set": "counter"
                }
            },
            {
                "inherit": {
                    "set": "stall"
                }
            }
        ]
    },
            ]
        },
        { "inherit": { "set": "vivid" } },
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
                    "neutralKey": "c",
                    "neutralWord": "dis[ ]ussion",
                    "statement": " You are talking with friends. A topic you feel strongly about comes up. You engage in an interesting "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "y"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Were you talking with friends? </div>"
                }
            },
            {
                "inherit": {
                    "set": "yesno"
                }
            },
            {
                "inherit": {
                    "set": "counter"
                }
            },
            {
                "inherit": {
                    "set": "stall"
                }
            }
         ]
    },
         ]
         },
         { "inherit": { "set": "vivid" } },
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
                    "neutralWord": "founta[ ]n",
                    "statement": "You decide to go for a jog. After running for a while, you take a break. You drink from a water "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "y"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Did you get water from a fountain?</div>"
                }
            },
            {
                "inherit": {
                    "set": "yesno"
                }
            },
            {
                "inherit": {
                    "set": "counter"
                }
            },
            {
                "inherit": {
                    "set": "stall"
                }
            }
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
                    "neutralWord": "respe[ ]tfully",
                    "statement": " You are talking with a friend. A topic you disagree about comes up. You discuss it "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "n"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Did you agree on every topic? </div>"
                }
            },
            {
                "inherit": {
                    "set": "yesno"
                }
            },
            {
                "inherit": {
                    "set": "counter"
                }
            },
            {
                "inherit": {
                    "set": "stall"
                }
            }
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
                    "neutralWord": "sal[ ]",
                    "statement": " You are making dinner for a friend. You realize that you are out of salt. You go to the store to purchase more "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "y"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Were you out of salt? </div>"
                }
            },
            {
                "inherit": {
                    "set": "yesno"
                }
            },
            {
                "inherit": {
                    "set": "counter"
                }
            },
            {
                "inherit": {
                    "set": "stall"
                }
            }
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
                    "neutralWord": "shir[ ]",
                    "statement": " You are going on a date. You try on a new outfit. You wear a new button-up "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "n"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Did you wear an old outfit? </div>"
                }
            },
            {
                "inherit": {
                    "set": "yesno"
                }
            },
            {
                "inherit": {
                    "set": "counter"
                }
            },
            {
                "inherit": {
                    "set": "stall"
                }
            }
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
                    "neutralWord": "cl[ ]se",
                    "statement": "You are playing volleyball. You are with friends. The game is "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "n"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Were you playing soccer?</div>"
                }
            },
            {
                "inherit": {
                    "set": "yesno"
                }
            },
            {
                "inherit": {
                    "set": "counter"
                }
            },
            {
                "inherit": {
                    "set": "stall"
                }
            }
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
                    "neutralWord": "p[ ]sta",
                    "statement": "You are eating dinner. The food is good. The main course is "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "y"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Are you eating dinner?</div>"
                }
            },
            {
                "inherit": {
                    "set": "yesno"
                }
            },
            {
                "inherit": {
                    "set": "counter"
                }
            },
            {
                "inherit": {
                    "set": "stall"
                }
            }
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
                    "statement": "While at work, you feel thirsty. You think about whether you want a soda or water bottle from the vending machine. You decide to get "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "n"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Did you decide to get a soda?</div>"
                }
            },
            {
                "inherit": {
                    "set": "yesno"
                }
            },
            {
                "inherit": {
                    "set": "counter"
                }
            },
            {
                "inherit": {
                    "set": "stall"
                }
            }
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
                    "neutralWord": "n[ ]p",
                    "statement": "You are at the beach. It is partly cloudly. You take a "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "n"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Are you at the park?</div>"
                }
            },
            {
                "inherit": {
                    "set": "yesno"
                }
            },
            {
                "inherit": {
                    "set": "counter"
                }
            },
            {
                "inherit": {
                    "set": "stall"
                }
            }
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
                    "neutralKey": "h",
                    "neutralWord": "c[ ]ickens",
                    "statement": "You are surfing the web. You see an interesting news article. It is about "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "n"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Were you watching TV?</div>"
                }
            },
            {
                "inherit": {
                    "set": "yesno"
                }
            },
            {
                "inherit": {
                    "set": "counter"
                }
            },
            {
                "inherit": {
                    "set": "stall"
                }
            }
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
                    "neutralWord": "post[ ]ard",
                    "statement": " You are visiting Chicago. While there, you decide to buy a souvenir. You purchase a "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "n"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Did you decide to purchase a souvenir hat?</div>"
                }
            },
            {
                "inherit": {
                    "set": "yesno"
                }
            },
            {
                "inherit": {
                    "set": "counter"
                }
            },
            {
                "inherit": {
                    "set": "stall"
                }
            }
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
                    "neutralWord": "bo[ ]l",
                    "statement": "You decide to make some tea. You fill the kettle up with water and put in on the stove. After a few minutes, the water begins to "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "n"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Did you decide to make coffee?</div>"
                }
            },
            {
                "inherit": {
                    "set": "yesno"
                }
            },
            {
                "inherit": {
                    "set": "counter"
                }
            },
            {
                "inherit": {
                    "set": "stall"
                }
            }
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
                    "neutralWord": "cas[ ]erole",
                    "statement": " Your colleague invites you to a dinner party. There will several people from your company there. Your colleague tells you they will serve "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "y"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Were you at a dinner party? </div>"
                }
            },
            {
                "inherit": {
                    "set": "yesno"
                }
            },
            {
                "inherit": {
                    "set": "counter"
                }
            },
            {
                "inherit": {
                    "set": "stall"
                }
            }
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
                    "neutralWord": "bef[ ]re",
                    "statement": " You are in a hotel. You notice that there is soft background music playing. You realize the song is one you have heard "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "y"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Was music playing in the background?</div>"
                }
            },
            {
                "inherit": {
                    "set": "yesno"
                }
            },
            {
                "inherit": {
                    "set": "counter"
                }
            },
            {
                "inherit": {
                    "set": "stall"
                }
            }
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
                    "neutralKey": "h",
                    "neutralWord": "s[ ]irt",
                    "statement": " You are shopping for new work clothes with a friend. You try on slacks and a shirt. You decide to purchase the "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "y"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Did you decide to purchase the shirt?</div>"
                }
            },
            {
                "inherit": {
                    "set": "yesno"
                }
            },
            {
                "inherit": {
                    "set": "counter"
                }
            },
            {
                "inherit": {
                    "set": "stall"
                }
            }
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
                    "neutralWord": "resta[ ]rant",
                    "statement": " You are on a double date with your partner, your best friend, and your best friend's partner. This is your first time meeting your best friend's partner. You talk about the food at the "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "y"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Were you with your best friend? </div>"
                }
            },
            {
                "inherit": {
                    "set": "yesno"
                }
            },
            {
                "inherit": {
                    "set": "counter"
                }
            },
            {
                "inherit": {
                    "set": "stall"
                }
            }
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
                    "neutralWord": "[ ]art",
                    "statement": " You are about to audition for a play. You hope you get the part. You are auditioning for a minor "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "n"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Were you auditioning for a dance? </div>"
                }
            },
            {
                "inherit": {
                    "set": "yesno"
                }
            },
            {
                "inherit": {
                    "set": "counter"
                }
            },
            {
                "inherit": {
                    "set": "stall"
                }
            }
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
                    "neutralKey": "m",
                    "neutralWord": "war[ ]",
                    "statement": " It is the middle of the winter and you are very cold. You smother yourself in blankets. You begin to get "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "y"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Was it winter? </div>"
                }
            },
            {
                "inherit": {
                    "set": "yesno"
                }
            },
            {
                "inherit": {
                    "set": "counter"
                }
            },
            {
                "inherit": {
                    "set": "stall"
                }
            }
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
                    "neutralWord": "cof[ ]ee",
                    "statement": "You are waiting for an important phone call. You are tired. To stay awake, you make "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "y"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Are you waiting for an important phone call?</div>"
                }
            },
            {
                "inherit": {
                    "set": "yesno"
                }
            },
            {
                "inherit": {
                    "set": "counter"
                }
            },
            {
                "inherit": {
                    "set": "stall"
                }
            }
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
                    "neutralKey": "k",
                    "neutralWord": "wor[ ]",
                    "statement": " You are out to eat with a friend. Your friend is a doctor. You talk about her "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "n"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Were you with your dad? </div>"
                }
            },
            {
                "inherit": {
                    "set": "yesno"
                }
            },
            {
                "inherit": {
                    "set": "counter"
                }
            },
            {
                "inherit": {
                    "set": "stall"
                }
            }
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
                    "neutralWord": "tire[ ]",
                    "statement": " You are watching TV with a friend. Your friend falls asleep. You think that they must be "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
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
            {
                "inherit": {
                    "set": "yesno"
                }
            },
            {
                "inherit": {
                    "set": "counter"
                }
            },
            {
                "inherit": {
                    "set": "stall"
                }
            }
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
                    "neutralWord": "sta[ ]rs",
                    "statement": " You are walking down the stairs. While walking down the stairs you slip on a step. You recover your balance and continue walking down the "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "y"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Were you walking downstairs? </div>"
                }
            },
            {
                "inherit": {
                    "set": "yesno"
                }
            },
            {
                "inherit": {
                    "set": "counter"
                }
            },
            {
                "inherit": {
                    "set": "stall"
                }
            }
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
                    "neutralWord": "te[ ]",
                    "statement": " You are on a date. You go to get coffee. You order a "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "y"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Were you on a date? </div>"
                }
            },
            {
                "inherit": {
                    "set": "yesno"
                }
            },
            {
                "inherit": {
                    "set": "counter"
                }
            },
            {
                "inherit": {
                    "set": "stall"
                }
            }
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
                    "neutralWord": "anyw[ ]y",
                    "statement": " You are running a race. Your hip begins to hurt. You finish the race "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "y"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Were you running a race? </div>"
                }
            },
            {
                "inherit": {
                    "set": "yesno"
                }
            },
            {
                "inherit": {
                    "set": "counter"
                }
            },
            {
                "inherit": {
                    "set": "stall"
                }
            }
        ]
    },
    ]},
    { "inherit": { "set": "vivid" } },    {
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
                    "neutralKey": "h",
                    "neutralWord": "p[ ]one",
                    "statement": " You are at a ballgame. You sit down in your seat. Once seated, you realize that you forgot to bring your cell "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "n"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Did you have your cellphone with you?</div>"
                }
            },
            {
                "inherit": {
                    "set": "yesno"
                }
            },
            {
                "inherit": {
                    "set": "counter"
                }
            },
            {
                "inherit": {
                    "set": "stall"
                }
            }
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
                    "neutralWord": "fi[ ]h",
                    "statement": "You are looking for a new pet. At the store, you think about what would fit best with your busy lifestyle. You decide to get a "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "n"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Did you decide to get a car?</div>"
                }
            },
            {
                "inherit": {
                    "set": "yesno"
                }
            },
            {
                "inherit": {
                    "set": "counter"
                }
            },
            {
                "inherit": {
                    "set": "stall"
                }
            }
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
                    "neutralWord": "p[ ]ace",
                    "statement": " You are playing softball with friends. You have a headache. You ask a friend to take your "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "n"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Were you playing basketball? </div>"
                }
            },
            {
                "inherit": {
                    "set": "yesno"
                }
            },
            {
                "inherit": {
                    "set": "counter"
                }
            },
            {
                "inherit": {
                    "set": "stall"
                }
            }
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
                    "neutralWord": "sand[ ]ich",
                    "statement": " You are at a restaurant with friends, and you are hungry. You motion to the waiter. You order a chicken "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "y"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Were you at a restaurant? </div>"
                }
            },
            {
                "inherit": {
                    "set": "yesno"
                }
            },
            {
                "inherit": {
                    "set": "counter"
                }
            },
            {
                "inherit": {
                    "set": "stall"
                }
            }
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
                    "neutralWord": "bi[ ]d",
                    "statement": "You are taking a walk. You look up at the sky. You see a "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "n"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Was there a plane in the sky?</div>"
                }
            },
            {
                "inherit": {
                    "set": "yesno"
                }
            },
            {
                "inherit": {
                    "set": "counter"
                }
            },
            {
                "inherit": {
                    "set": "stall"
                }
            }
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
                    "neutralWord": "ham[ ]urger",
                    "statement": "You are at a barbeque. Lots of your friends are there. You eat a "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "n"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Did you decide to eat a hotdog?</div>"
                }
            },
            {
                "inherit": {
                    "set": "yesno"
                }
            },
            {
                "inherit": {
                    "set": "counter"
                }
            },
            {
                "inherit": {
                    "set": "stall"
                }
            }
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
                    "neutralWord": "cha[ ]ity",
                    "statement": "You are cleaning out your closet. You take out a few shirts you haven't worn for a long time. You plan to give them to "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "n"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Did you give your old shirts to your neighbor next door?</div>"
                }
            },
            {
                "inherit": {
                    "set": "yesno"
                }
            },
            {
                "inherit": {
                    "set": "counter"
                }
            },
            {
                "inherit": {
                    "set": "stall"
                }
            }
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
                    "neutralWord": "vik[ ]ngs",
                    "statement": "You are watching TV with some friends. Your favorite program comes on. It is a show about "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
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
            {
                "inherit": {
                    "set": "yesno"
                }
            },
            {
                "inherit": {
                    "set": "counter"
                }
            },
            {
                "inherit": {
                    "set": "stall"
                }
            }
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
                    "neutralWord": "o[ ]line",
                    "statement": " You are at the library looking for a book. However, you notice that the book you want has already been checked out. You decide to try to order the book "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "y"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Were you able to get the book from the library?</div>"
                }
            },
            {
                "inherit": {
                    "set": "yesno"
                }
            },
            {
                "inherit": {
                    "set": "counter"
                }
            },
            {
                "inherit": {
                    "set": "stall"
                }
            }
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
                    "neutralWord": "deci[ ]ion",
                    "statement": " You think about adopting a pet. You decide to make a list of the pros and cons of getting a cat. You decide to spend some more time thinking about the "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "y"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Were you thinking about adopting a pet?</div>"
                }
            },
            {
                "inherit": {
                    "set": "yesno"
                }
            },
            {
                "inherit": {
                    "set": "counter"
                }
            },
            {
                "inherit": {
                    "set": "stall"
                }
            }
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
                    "neutralKey": "h",
                    "neutralWord": "seas[ ]ell",
                    "statement": "You are at the beach. It is hot out so you decide to go swimming. While in the ocean you find a "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "y"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Did you decide to go swimming?</div>"
                }
            },
            {
                "inherit": {
                    "set": "yesno"
                }
            },
            {
                "inherit": {
                    "set": "counter"
                }
            },
            {
                "inherit": {
                    "set": "stall"
                }
            }
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
                    "neutralWord": "g[ ]lf",
                    "statement": " You are talking with your boss and you zone out briefly. You ask if she could repeat what she just said. She says she was talking about "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "y"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Were you talking with your boss? </div>"
                }
            },
            {
                "inherit": {
                    "set": "yesno"
                }
            },
            {
                "inherit": {
                    "set": "counter"
                }
            },
            {
                "inherit": {
                    "set": "stall"
                }
            }
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
                    "neutralWord": "p[ ]ople",
                    "statement": " You are in line at the movies. You hear people talking about the actors in the movie. The actors sound like interesting "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
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
            {
                "inherit": {
                    "set": "yesno"
                }
            },
            {
                "inherit": {
                    "set": "counter"
                }
            },
            {
                "inherit": {
                    "set": "stall"
                }
            }
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
                    "neutralWord": "p[ ]ogress",
                    "statement": " You are giving a talk to your boss and several other executives in the company. They are listening to what you have to say. The talk is about the company's "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "y"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Were you giving a talk? </div>"
                }
            },
            {
                "inherit": {
                    "set": "yesno"
                }
            },
            {
                "inherit": {
                    "set": "counter"
                }
            },
            {
                "inherit": {
                    "set": "stall"
                }
            }
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
                    "neutralWord": "ta[ ]k",
                    "statement": " You are on a hike with a friend. They sit down on a rock. You sit down with them and "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "y"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Were you with a friend? </div>"
                }
            },
            {
                "inherit": {
                    "set": "yesno"
                }
            },
            {
                "inherit": {
                    "set": "counter"
                }
            },
            {
                "inherit": {
                    "set": "stall"
                }
            }
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
                    "neutralKey": "m",
                    "neutralWord": "gu[ ]",
                    "statement": " You are on a plane. Your ears start popping. You chew some "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "n"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Were you in a car? </div>"
                }
            },
            {
                "inherit": {
                    "set": "yesno"
                }
            },
            {
                "inherit": {
                    "set": "counter"
                }
            },
            {
                "inherit": {
                    "set": "stall"
                }
            }
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
                    "neutralWord": "qu[ ]rter",
                    "statement": " You are in line at the grocery store. The person in front of you asks for a quarter. You give them a "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
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
            {
                "inherit": {
                    "set": "yesno"
                }
            },
            {
                "inherit": {
                    "set": "counter"
                }
            },
            {
                "inherit": {
                    "set": "stall"
                }
            }
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
                    "neutralWord": "bas[ ]ball",
                    "statement": "You are reading a magazine. It is a special edition. You choose to read an article on the history of "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "y"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Were you reading a magazine?</div>"
                }
            },
            {
                "inherit": {
                    "set": "yesno"
                }
            },
            {
                "inherit": {
                    "set": "counter"
                }
            },
            {
                "inherit": {
                    "set": "stall"
                }
            }
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
                    "neutralWord": "bef[ ]re",
                    "statement": " You are on a date going to get coffee. The coffee shop is playing music you've never heard before. You ask your date if she has heard the music "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "n"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Were you at the movies? </div>"
                }
            },
            {
                "inherit": {
                    "set": "yesno"
                }
            },
            {
                "inherit": {
                    "set": "counter"
                }
            },
            {
                "inherit": {
                    "set": "stall"
                }
            }
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
                    "statement": "You are playing basketball. It is halftime. You drink "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "n"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Are you playing football?</div>"
                }
            },
            {
                "inherit": {
                    "set": "yesno"
                }
            },
            {
                "inherit": {
                    "set": "counter"
                }
            },
            {
                "inherit": {
                    "set": "stall"
                }
            }
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
                    "neutralWord": "h[ ]m",
                    "statement": "You are working on a final paper for your class. As you are typing, you get distracted by your dog. You stop working and go play with "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "n"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Did you get distracted by your cat?</div>"
                }
            },
            {
                "inherit": {
                    "set": "yesno"
                }
            },
            {
                "inherit": {
                    "set": "counter"
                }
            },
            {
                "inherit": {
                    "set": "stall"
                }
            }
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
                    "neutralWord": "pla[ ]s",
                    "statement": " You are eating dinner with a friend. You are eating spaghetti. You talk about your upcoming vacation "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "n"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Were you eating lunch? </div>"
                }
            },
            {
                "inherit": {
                    "set": "yesno"
                }
            },
            {
                "inherit": {
                    "set": "counter"
                }
            },
            {
                "inherit": {
                    "set": "stall"
                }
            }
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
                    "neutralWord": "de[ ]r",
                    "statement": "You are on a walk outside. Hearing a rustling noise, you look into the forest. You see a"
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "n"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Did you see a bunny in the forest?</div>"
                }
            },
            {
                "inherit": {
                    "set": "yesno"
                }
            },
            {
                "inherit": {
                    "set": "counter"
                }
            },
            {
                "inherit": {
                    "set": "stall"
                }
            }
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
                    "neutralWord": "p[ ]n",
                    "statement": " You just answered a question aloud in your class. After answering, you notice that you accidentally knocked your pen off your desk. You reach down to pick up your "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "y"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Did you answer the question?</div>"
                }
            },
            {
                "inherit": {
                    "set": "yesno"
                }
            },
            {
                "inherit": {
                    "set": "counter"
                }
            },
            {
                "inherit": {
                    "set": "stall"
                }
            }
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
                    "neutralWord": "surf[ ]ce",
                    "statement": " You are swimming in a pool, and you decide to try to touch the bottom of the pool. As you swim downward, you realize that you can't hold your breath much longer. You swim back to the "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.neutral ? stimulusData.neutralWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "data": {
                    "neutralAnswer": "y"
                },
                "handle": "question",
                "media": {
                    "inlineTemplate": "<div>Were you at the pool? </div>"
                }
            },
            {
                "inherit": {
                    "set": "yesno"
                }
            },
            {
                "inherit": {
                    "set": "counter"
                }
            },
            {
                "inherit": {
                    "set": "stall"
                }
            }
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