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
                    "neutralKey": "o",
                    "neutralWord": "hist[ ]ry",
                    "statement": "You are in a class. You are taking notes. The subject of the class is "
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
                    "inlineTemplate": "<div>Was the subject of the class science?</div>"
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
                    "neutralKey": "o",
                    "neutralWord": "fo[ ]d",
                    "statement": "You recently adopted a new cat. You quickly learn that your cat is a picky eater. You decide to buy a new brand of "
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
                    "inlineTemplate": "<div>Did you recently adopt a new dog?</div>"
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
                    "neutralKey": "h",
                    "neutralWord": "s[ ]ower",
                    "statement": "You are waking up to go to work. You are tired. You take a "
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
                    "inlineTemplate": "<div>Did you wake up to go to school?</div>"
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
                    "neutralWord": "a[ ]yways",
                    "statement": " You are at the train station and notice someone you went to high school with. You weren't good friends in high school. You walk over and say hello "
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
                    "inlineTemplate": "<div>Were you at the train station? </div>"
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
                    "neutralWord": "of[ ]ice",
                    "statement": "You have to mail a bill. After you put it in the envelope, you look for a stamp. You cannot find one, so you go to the post "
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
                    "inlineTemplate": "<div>Could you find a stamp?</div>"
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
                    "statement": "You are at a coffee shop. You are not sure which drink to purchase. You decide to buy an iced "
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
                    "inlineTemplate": "<div>Did you decide to buy tea?</div>"
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
                    "neutralWord": "we[ ]ther",
                    "statement": "You are taking a walk. You are with a friend. You talking about the "
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
                    "inlineTemplate": "<div>Are you walking with someone you know?</div>"
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
                    "neutralWord": "bre[ ]k",
                    "statement": " You are going for a jog. You are out of breath. You stop and take a "
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
                    "inlineTemplate": "<div>Were you out of breath? </div>"
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
                    "neutralWord": "us[ ]al",
                    "statement": "You are driving home from work. On the way, you realize you missed a turn. You return home a little bit later than "
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
                    "inlineTemplate": "<div>Did you get home late?</div>"
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
                    "neutralWord": "fri[ ]nd",
                    "statement": "You are watching TV. The phone rings and you pick it up. It is your  "
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
                    "inlineTemplate": "<div>Were you watching TV when the phone rang?</div>"
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
                    "neutralWord": "clot[ ]",
                    "statement": " You are making dinner. You pick up a pot that is hot. You put it down and pick it back up with a "
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
                    "inlineTemplate": "<div>Were you making breakfast? </div>"
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
                    "neutralWord": "d[ ]g",
                    "statement": "You are at a baseball game. Your favorite teaming is winning. You eat a hot "
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
                    "inlineTemplate": "<div>Were you at a soccer game?</div>"
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
                    "neutralWord": "k[ ]ow",
                    "statement": " You're listening to the radio on Saturday afternoon. The first song is one you have heard before. The next song is one that you do not "
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
                    "inlineTemplate": "<div>Have you heard the first song that played on the radio?</div>"
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
                    "neutralWord": "w[ ]rk",
                    "statement": "You are writing a paper on your computer. You have been working for a while, and realize you have not saved your work recently. You quickly save your "
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
                    "inlineTemplate": "<div>Did you decide not to save your work?</div>"
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
                    "neutralWord": "va[ ]ation",
                    "statement": " You decide to write a short story. You have trouble thinking of a topic. In the end, you decide to write about a past "
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
                    "inlineTemplate": "<div>Did you decide to write about this upcoming weekend?</div>"
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
                    "neutralWord": "b[ ]th",
                    "statement": "You are deciding what courses to take next semester. Two classes you really want to take meet at the same time. You wish you could enroll in "
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
                    "inlineTemplate": "<div>Do the two classes meet at different times?</div>"
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
                    "neutralWord": "sunscre[ ]n",
                    "statement": " You are at the beach. You realize that you forgot to bring your sunscreen. You go back to your car to get the "
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
                    "inlineTemplate": "<div>Did you forget your sunscreen? </div>"
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
                    "neutralWord": "t[ ]a",
                    "statement": "You are getting ready for bed. You are not tired. You make a cup of "
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
                    "inlineTemplate": "<div>Did you make tea for breakfast?</div>"
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
                    "neutralWord": "ap[ ]le",
                    "statement": " You have a friend over, and you feel hungry. You go to the kitchen to look for something to eat. You decide to eat an "
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
                    "inlineTemplate": "<div>Did you decide to eat an orange?</div>"
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
                    "neutralWord": "team[ ]ate",
                    "statement": " You are playing basketball with friends. It is the last possession and the ball gets passed to you. You pass it to an open "
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
                    "neutralKey": "r",
                    "neutralWord": "fa[ ]",
                    "statement": " You are in the break room eating lunch. A new coworker who you don't know very well steps into the break room. You say hello and ask them how they are liking the office so "
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
                    "inlineTemplate": "<div>Did you talk to the coworker? </div>"
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
                    "neutralWord": "cab[ ]n",
                    "statement": " You are at a company retreat. It is a camping retreat with coworkers. There are three other coworkers in your "
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
                    "inlineTemplate": "<div>Were you at a company retreat? </div>"
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
                    "neutralWord": "flo[ ]s",
                    "statement": " You are going on vacation with your partner. They ask to make sure that you bring an extra toothbrush. As you leave you remember to bring "
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
                    "inlineTemplate": "<div>Were you going on vacation? </div>"
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
                    "neutralWord": "yea[ ]s",
                    "statement": " You are at the doctor's office for a physical. Your doctor is happy to see you. She has been your doctor for 5 "
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
                    "inlineTemplate": "<div>Was the doctor happy to see you? </div>"
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
                    "neutralWord": "[ ]rder",
                    "statement": " You are at a sandwich shop and order your food. The cook can't hear your order. You repeat your "
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
                    "inlineTemplate": "<div>Did you order food? </div>"
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
                    "neutralKey": "i",
                    "neutralWord": "subs[ ]des",
                    "statement": " You wake up with a headache. You eat scrambled eggs for breakfast. The headache "
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
                    "inlineTemplate": "<div>Did you eat scrambled eggs? </div>"
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
                    "neutralWord": "le[ ]t",
                    "statement": " You are jogging, and say hi to a friend as you pass him. You keep jogging, and come to an intersection. You decide to turn "
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
                    "inlineTemplate": "<div>Did you decide to turn?</div>"
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
                    "neutralWord": "ti[ ]ed",
                    "statement": "You are getting ready for bed. You brush your teeth. You are "
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
                    "inlineTemplate": "<div>Did you wash your face?</div>"
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
                    "neutralWord": "stret[ ]h",
                    "statement": " You are at the gym, lifting weights. Your left arm begins to hurt. You take a break and "
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
                    "neutralWord": "ridi[ ]g",
                    "statement": " You are riding your bike. You pass some friends while you riding. You wave hello and keep "
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
                    "inlineTemplate": "<div>Were you horseback riding? </div>"
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
                    "neutralWord": "s[ ]eet",
                    "statement": " You are at a state fair. You buy yourself a cotton candy. The cotton candy tastes "
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
                    "inlineTemplate": "<div>Did you eat pizza? </div>"
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
                    "neutralWord": "intere[ ]ting",
                    "statement": " You are at a party and a friend brings up a subject that you are not familiar with. You ask if they could give you some background on the topic. They give you some background and you agree that the topic is very "
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
                    "inlineTemplate": "<div>Were you at a party? </div>"
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
                    "neutralWord": "sha[ ]e",
                    "statement": " It is a very hot day and you have been outside with your friends for hours. You begin to sweat. You and your friends go sit in the "
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
                    "inlineTemplate": "<div>Were you outside? </div>"
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
                    "neutralWord": "the[ ]e",
                    "statement": " You are walking down the street. An elderly couple approaches you and asks for directions to a local restaurant. You tell them how to get "
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
                    "inlineTemplate": "<div>Did you give directions? </div>"
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
                    "neutralWord": "do[ ]n",
                    "statement": " You riding your bike to work. It is hot outside and you are sweating. Once you get to work you go inside and cool "
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
                    "inlineTemplate": "<div>Were you driving to work? </div>"
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
                    "neutralWord": "ti[ ]ed",
                    "statement": "You are at a play with a friend. You stayed up late the night before. You are "
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
                    "inlineTemplate": "<div>Are you at class with a friend?</div>"
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
                    "neutralWord": "pe[ ]ple",
                    "statement": " You are at your partner's office party. There are a lot of people there that you don't know. You meet some interesting "
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
                    "inlineTemplate": "<div>Were you at an office party? </div>"
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
                    "neutralWord": "chic[ ]en",
                    "statement": " You are cooking for friends. You have numerous friends over, as well as some people you don't know that well yet. You are making "
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
                    "inlineTemplate": "<div>Were you cooking? </div>"
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
                    "neutralWord": "wi[ ]dow",
                    "statement": "You are carving a pumpkin for Halloween. You decide to carve a witch on a broomstick. When you are done, you put the pumpkin in the "
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
                    "inlineTemplate": "<div>Did you put the pumpkin on the porch?</div>"
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
                    "neutralWord": "at[ ]ractions",
                    "statement": "You want to go see a movie that just came out. You get to the theater as the movie is starting and see there is a long line. This means that you will miss the coming  "
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
                    "inlineTemplate": "<div>Was there a long line of people when you arrive?</div>"
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
                    "neutralWord": "ha[ ]ds",
                    "statement": " A lot of your coworkers are sick. You are worried that you might get sick. You make sure to always wash your "
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
                    "inlineTemplate": "<div>Were your coworkers sick? </div>"
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
                    "neutralWord": "hou[ ]",
                    "statement": " You are at a concert. The show is sold out. The first band played for about an "
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
                    "neutralKey": "h",
                    "neutralWord": "[ ]ome",
                    "statement": " You are at the gym, lifting weights. After one set, your arm begins to hurt. You decide to go "
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
                    "inlineTemplate": "<div>Did you run at the gym? </div>"
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
                    "neutralWord": "in[ ]eresting",
                    "statement": " You are at a dinner party. There are lots of people you don't know. You find the conversations "
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
                    "neutralKey": "u",
                    "neutralWord": "bl[ ]e",
                    "statement": "You are painting a wall in your home. You are almost done. You painted the wall "
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
                    "inlineTemplate": "<div>Were you painting a wall at your friends house?</div>"
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
                    "neutralWord": "fr[ ]end",
                    "statement": "You are watching a basketball game on TV. There is a commerical break. You call a "
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
                    "inlineTemplate": "<div>Is a football game on TV?</div>"
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
                    "statement": " You wake up in the middle of the night. You get out of bed and go to the kitchen. You take a drink of "
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
                    "inlineTemplate": "<div>Did you go to the bathroom? </div>"
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
                    "neutralWord": "st[ ]eet",
                    "statement": " You are going for a walk. You come to an intersection. You look both ways and cross the "
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
                    "inlineTemplate": "<div>Did you look both ways? </div>"
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
                    "neutralWord": "ch[ ]ese",
                    "statement": "You are at a picnic with friends. It is sunny outside. You eat "
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
                    "inlineTemplate": "<div>Were you at a picnic with your family?</div>"
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
                    "neutralWord": "[ ]illow",
                    "statement": " You wake up with a sore neck. You go take a shower. You decide to buy a new "
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
                    "inlineTemplate": "<div>Did you take a shower? </div>"
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