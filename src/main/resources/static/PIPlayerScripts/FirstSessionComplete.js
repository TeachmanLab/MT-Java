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
        if(trial.data.positive) {
            return (p.attributes.data.positiveKey);
        } else {
            return(p.attributes.data.negativeKey);
        }
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
                conditions: [{type:'inputEqualsStim', property:'positiveAnswer'},
                    {type:'trialEquals', property:'positive', value:true},
                    {type:'globalEquals', property:'askingQuestion', value:true}
                ],
                actions: [
                    {type:'hideStim',handle : 'question'},
                    {type:'hideStim',handle:'yesno'},
                    {type:'hideStim', handle: 'counter'},
                    {type:'trigger',handle : 'answered', duration:500}
                ]
            },
            // Listen for a correct response to a negative question
            {
                conditions: [{type:'inputEqualsStim', property:'negativeAnswer'},
                    {type:'trialEquals', property:'positive', value:false},
                    {type:'globalEquals', property:'askingQuestion', value:true}
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
                conditions: [{type:'inputEqualsStim', property:'positiveAnswer'},
                    {type:'trialEquals', property:'positive', value:false},
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
            // Listen for a incorrect response to a negative question
            {
                conditions: [{type:'inputEqualsStim', property:'negativeAnswer'},
                    {type:'trialEquals', property:'positive', value:true},
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
    if(API.getGlobal()["cbmCondition"] == "FITFY_FIFTY") {
        API.addTrialSets('posneg',[
            { inherit:'base', data: {positive:true}},
            { inherit:'base', data: {positive:false}}
        ]);
    } else {
        API.addTrialSets('posneg',[
            { inherit:'base', data: {positive:true}}
        ]);
    }


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
                        "set": "posneg",
                        "type": "random"
                    },
                    "stimuli": [
                        {"inherit": {"set": "error"}},
                        {
                            "data": {
                                "negativeKey": "r",
                                "negativeWord": "ter[ ]ible",
                                "positiveKey": "l",
                                "positiveWord": "thril[ ]ing",
                                "statement": " You are out to dinner on a date. As you look into your date\u2019s eyes, you are unsure whether he/she will ask you out for another date. The feeling of uncertainty is "
                            },
                            "handle": "paragraph",
                            "media": {
                                "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
                            }
                        },
                        {
                            "handle": "question",
                            data: {

                            	positiveAnswer:"y",
                            	negativeAnswer:"n"
                            },
                            "media": {
                                "inlineTemplate": "<div>Is it fun to experience a little uncertainty on a date? </div>"
                            }
                        },
                        {"inherit": {"set": "yesno"}},
                        {"inherit": {"set": "counter"}},
                        {"inherit": {"set": "stall"}}
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
                        "set": "posneg",
                        "type": "random"
                    },
                    "stimuli": [
                        {"inherit": {"set": "error"}},
                        {
                            "data": {

                                "negativeKey": "e",
                                "negativeWord": "anxi[ ]ty",
                                "positiveKey": "c",
                                "positiveWord": "dan[ ]ing",
                                "statement": " You are taking a dancing class and during break, find yourself talking to a newcomer about your job. As you chat, you notice that you are sweating. She probably assumes this is a result of "
                            },
                            "handle": "paragraph",
                            "media": {
                                "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
                            }
                        },
                        {
                            "handle": "question",
                             data: {

                            	positiveAnswer:"n",
                            	negativeAnswer:"y"
                            },
                            "media": {
                                "inlineTemplate": "<div>Did the person you met at dance class think you were anxious? </div>"
                            }
                        },
                        {"inherit": {"set": "yesno"}},
                        {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}
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
            "set": "posneg",
            "type": "random"
        },
        "stimuli": [
            {"inherit": {"set": "error"}},
            {
                "data": {

                    "negativeKey": "f",
                    "negativeWord": "aw[ ]ul",
                    "positiveKey": "n",
                    "positiveWord": "fi[ ]e",
                    "statement": " While on a date, you accidentally spill a glass of water. You feel slightly embarrassed as your date helps you clean up the spill. The rest of the date is likely to be "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "handle": "question",
                 data: {
                	positiveAnswer:"y",
                	negativeAnswer:"n"
                },
                "media": {
                    "inlineTemplate": "<div>Will the rest of the date be OK, even though you spill water? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
                        {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}
        ]
    },
    {
        "inherit": {
            "set": "posneg",
            "type": "random"
        },
        "stimuli": [
            {"inherit": {"set": "error"}},
            {
                "data": {

                    "negativeKey": "r",
                    "negativeWord": "ne[ ]vous",
                    "positiveKey": "m",
                    "positiveWord": "cal[ ]",
                    "statement": " Your friend sets you up on a blind date and you agree to meet the date at a coffee shop. Once you are both sitting at your table, you notice that he/she is very attractive. You have difficulty thinking of something to say, so you sit quietly. He/she probably thinks that you are "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "handle": "question",
                 data: {

                	positiveAnswer:"n",
                	negativeAnswer:"y"
                },
                "media": {
                    "inlineTemplate": "<div>Does the date think that you are anxious? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
                        {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}
        ]
    },
    {
        "inherit": {
            "set": "posneg",
            "type": "random"
        },
        "stimuli": [
            {"inherit": {"set": "error"}},
            {
                "data": {

                    "negativeKey": "x",
                    "negativeWord": "e[ ]cluded",
                    "positiveKey": "l",
                    "positiveWord": "inc[ ]uded",
                    "statement": " During a discussion, you end up debating an issue with colleagues. You later discover that they found your views unusual. When the issue comes up again in a group conversation, you expect that you will be "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "handle": "question",
                 data: {

                	positiveAnswer:"y",
                	negativeAnswer:"n"
                },
                "media": {
                    "inlineTemplate": "<div>Do you expect that others will want to talk with you about the issue in the future? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
                        {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}
        ]
    },
    {
        "inherit": {
            "set": "posneg",
            "type": "random"
        },
        "stimuli": [
            {"inherit": {"set": "error"}},
            {
                data: {

                    "negativeKey": "a",
                    "negativeWord": "embarr[ ]ssed",
                    "positiveKey": "s",
                    "positiveWord": "enthu[ ]iastic",
                    "statement": "A friend suggests that you join an evening class on creative writing. The thought of other people looking at your writing makes you feel "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "handle": "question",
                 data: {

                	positiveAnswer:"n",
                	negativeAnswer:"y"
                },
                "media": {
                    "inlineTemplate": "<div>Would you expect to feel uncomfortable if others look at your work? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}
        ]
    },
    {
        "inherit": {
            "set": "posneg",
            "type": "random"
        },
        "stimuli": [
            {"inherit": {"set": "error"}},
            {
                data: {

                    "negativeKey": "r",
                    "negativeWord": "wei[ ]d",
                    "positiveKey": "e",
                    "positiveWord": "op[ ]n",
                    "statement": " You are sitting on the couch and watching television with a friend. Your friend asks you a personal question. You answer honestly, and you think your friend judges you to be "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "handle": "question",
                 data: {

                	positiveAnswer:"n",
                	negativeAnswer:"y"
                },
                "media": {
                    "inlineTemplate": "<div>Does your friend have a more negative opinion of you after you share personal information? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}
        ]
    },
    {
        "inherit": {
            "set": "posneg",
            "type": "random"
        },
        "stimuli": [
            {"inherit": {"set": "error"}},
            {
                data: {

                    "negativeKey": "r",
                    "negativeWord": "sa[ ]castic",
                    "positiveKey": "c",
                    "positiveWord": "sin[ ]ere",
                    "statement": " You are shopping with a friend, and you try on a new outfit. As you come out of the fitting room, your friend pauses, and then, without smiling, says that you look good. As you think about your friend\u2019s response, you decide your friend is being "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "handle": "question",
                 data: {

                	positiveAnswer:"y",
                	negativeAnswer:"n"
                },
                "media": {
                    "inlineTemplate": "<div>Does your friend think you look nice? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}
        ]
    },
    {
        "inherit": {
            "set": "posneg",
            "type": "random"
        },
        "stimuli": [
            {"inherit": {"set": "error"}},
            {
                data: {

                    "negativeKey": "g",
                    "negativeWord": "ne[ ]atively",
                    "positiveKey": "v",
                    "positiveWord": "fa[ ]orably",
                    "statement": " You have almost completed a computer course and part of your grade will be determined by a presentation that is to be graded by your classmates. You know most of them, and feel that their opinions of you will make them grade you more "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "handle": "question",
                 data: {

                	positiveAnswer:"y",
                	negativeAnswer:"n"
                },
                "media": {
                    "inlineTemplate": "<div>Will your classmates grade your presentation advantageously? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}
        ]
    },
    {
        "inherit": {
            "set": "posneg",
            "type": "random"
        },
        "stimuli": [
            {"inherit": {"set": "error"}},
            {
                data: {

                    "negativeKey": "r",
                    "negativeWord": "ter[ ]ible",
                    "positiveKey": "e",
                    "positiveWord": "gr[ ]at",
                    "statement": " You consider taking an evening class in which part of your grade is based on your participation, even though you do not like speaking up in front of others. Because the topic is really interesting, you decide to sign up for the class. After signing up, you realize this decision was "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "handle": "question",
                 data: {

                	positiveAnswer:"y",
                	negativeAnswer:"n"
                },
                "media": {
                    "inlineTemplate": "<div>Are you happy that you signed up for the interesting evening class? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}
        ]
    },
    {
        "inherit": {
            "set": "posneg",
            "type": "random"
        },
        "stimuli": [
            {"inherit": {"set": "error"}},
            {
                data: {

                    "negativeKey": "k",
                    "negativeWord": "lac[ ]ing",
                    "positiveKey": "m",
                    "positiveWord": "nor[ ]al",
                    "statement": " You have had a busy week so your kitchen is slightly disorganized. You are expecting your neighbor to stop by for a drink, so you begin to straighten up. Just as you start, your neighbor arrives. He probably thinks your hosting skills are "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "handle": "question",
                 data: {

                	positiveAnswer:"n",
                	negativeAnswer:"y"
                },
                "media": {
                    "inlineTemplate": "<div>Do you feel that your neighbor disapproves of you? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}
        ]
    },
    {
        "inherit": {
            "set": "posneg",
            "type": "random"
        },
        "stimuli": [
            {"inherit": {"set": "error"}},
            {
                data: {

                    "negativeKey": "i",
                    "negativeWord": "fustrat[ ]ng",
                    "positiveKey": "t",
                    "positiveWord": "ra[ ]ional",
                    "statement": " You buy a new camera, but when you get it home, you decide you do not like it. You return it to the store and get your money back. The assistant is not very talkative as he helps you, and you think he views you as "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "handle": "question",
                 data: {

                	positiveAnswer:"n",
                	negativeAnswer:"y"
                },
                "media": {
                    "inlineTemplate": "<div>Do you think the sales assistant felt you were annoying? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}
        ]
    },
    {
        "inherit": {
            "set": "posneg",
            "type": "random"
        },
        "stimuli": [
            {"inherit": {"set": "error"}},
            {
                data: {

                    "negativeKey": "o",
                    "negativeWord": "p[ ]or",
                    "positiveKey": "m",
                    "positiveWord": "ad[ ]irable",
                    "statement": " You are persuaded to join a trivia team in a tournament. You are told that most of the questions will be asked to individuals, in different rounds. The first round is hard and you feel that the others found your efforts particularly "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "handle": "question",
                 data: {

                	positiveAnswer:"y",
                	negativeAnswer:"n"
                },
                "media": {
                    "inlineTemplate": "<div>Did your teammates feel positive about your efforts in the tournament? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}
        ]
    },
    {
        "inherit": {
            "set": "posneg",
            "type": "random"
        },
        "stimuli": [
            {"inherit": {"set": "error"}},
            {
                data: {

                    "negativeKey": "u",
                    "negativeWord": "d[ ]ll",
                    "positiveKey": "b",
                    "positiveWord": "likea[ ]le",
                    "statement": " You have just moved to a new area and your neighbor asks if you would like to go to your local bar that evening. When you arrive, she is not there yet. Reflecting on your earlier conversation, she probably thought you were "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "handle": "question",
                 data: {

                	positiveAnswer:"n",
                	negativeAnswer:"y"
                },
                "media": {
                    "inlineTemplate": "<div>Did you make a bad impression on your new neighbor? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}
        ]
    },
    {
        "inherit": {
            "set": "posneg",
            "type": "random"
        },
        "stimuli": [
            {"inherit": {"set": "error"}},
            {
                data: {

                    "negativeKey": "s",
                    "negativeWord": "embara[ ]sing",
                    "positiveKey": "m",
                    "positiveWord": "nor[ ]al",
                    "statement": " You are at a birthday party for a friend of a friend. You notice that you are flushed and a little sweaty. You think that these sensations are "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "handle": "question",
                 data: {

                	positiveAnswer:"n",
                	negativeAnswer:"y"
                },
                "media": {
                    "inlineTemplate": "<div>Is it bad to be flushed and sweaty in front of others? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}
        ]
    },
    {
        "inherit": {
            "set": "posneg",
            "type": "random"
        },
        "stimuli": [
            {"inherit": {"set": "error"}},
            {
                data: {

                    "negativeKey": "i",
                    "negativeWord": "anx[ ]ous",
                    "positiveKey": "l",
                    "positiveWord": "ca[ ]m",
                    "statement": " You have a minor disagreement with a coworker about the best way to complete a task. You and the coworker decide to ask your boss about it. When you talk to your boss, you momentarily forget what you were about to say. Your boss thinks you sound "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "handle": "question",
                 data: {

                	positiveAnswer:"n",
                	negativeAnswer:"y"
                },
                "media": {
                    "inlineTemplate": "<div>Do you appear nervous when you speak with your boss? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}
        ]
    },
    {
        "inherit": {
            "set": "posneg",
            "type": "random"
        },
        "stimuli": [
            {"inherit": {"set": "error"}},
            {
                data: {

                    "negativeKey": "w",
                    "negativeWord": "fro[ ]ning",
                    "positiveKey": "m",
                    "positiveWord": "s[ ]iling",
                    "statement": " You accidentally knock over a few picture frames in a store. You feel your cheeks become hot as people in the store turn and look at you. As the shop owners come over to rearrange the picture frames, he is "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "handle": "question",
                 data: {

                	positiveAnswer:"n",
                	negativeAnswer:"y"
                },
                "media": {
                    "inlineTemplate": "<div>Is the show owner mad at you for knocking over the picture frames? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}
        ]
    },
    {
        "inherit": {
            "set": "posneg",
            "type": "random"
        },
        "stimuli": [
            {"inherit": {"set": "error"}},
            {
                data: {

                    "negativeKey": "u",
                    "negativeWord": "d[ ]ll",
                    "positiveKey": "n",
                    "positiveWord": "fu[ ]",
                    "statement": " You meet someone at a party and feel that you got along well. Later, you call and suggest meeting again. The reply you get makes you feel that the person thinks meeting again would be "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "handle": "question",
                 data: {

                	positiveAnswer:"y",
                	negativeAnswer:"n"
                },
                "media": {
                    "inlineTemplate": "<div>Did the person from the party accept your invitation for getting together again? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}
        ]
    },
    {
        "inherit": {
            "set": "posneg",
            "type": "random"
        },
        "stimuli": [
            {"inherit": {"set": "error"}},
            {
                data: {

                    "negativeKey": "n",
                    "negativeWord": "ig[ ]orant",
                    "positiveKey": "g",
                    "positiveWord": "knowled[ ]eable",
                    "statement": " You are in the car with your mother-in-law, and put on the radio. As you discuss which station to listen to, you realize that you and your mother-in-law like different types of music. After having an in-depth discussion about the pros and cons of different music styles, your mother-in-law judges you as "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "handle": "question",
                 data: {

                	positiveAnswer:"n",
                	negativeAnswer:"y"
                },
                "media": {
                    "inlineTemplate": "<div>Does your mother-in-law decide that you do not know anything about music? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}
        ]
    },
    {
        "inherit": {
            "set": "posneg",
            "type": "random"
        },
        "stimuli": [
            {"inherit": {"set": "error"}},
            {
                data: {

                    "negativeKey": "n",
                    "negativeWord": "ig[ ]orant",
                    "positiveKey": "p",
                    "positiveWord": "hel[ ]ful",
                    "statement": " A friend is having problems with her toddler and calls you to discuss the situation. You explain the methods that worked for you when your children were that age. Your friend notes that your methods are quite different from her methods, and as a result, she thinks you are "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "handle": "question",
                 data: {

                	positiveAnswer:"y",
                	negativeAnswer:"n"
                },
                "media": {
                    "inlineTemplate": "<div>Did your friend find your advice constructive? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}
        ]
    },
    {
        "inherit": {
            "set": "posneg",
            "type": "random"
        },
        "stimuli": [
            {"inherit": {"set": "error"}},
            {
                data: {

                    "negativeKey": "v",
                    "negativeWord": "lea[ ]e",
                    "positiveKey": "y",
                    "positiveWord": "sta[ ]",
                    "statement": " You arrive at a large party, and quickly realize that there are a lot of people you do not know there. You feel anxious, and consider going home. After thinking about it, you decide to "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "handle": "question",
                 data: {

                	positiveAnswer:"y",
                	negativeAnswer:"n"
                },
                "media": {
                    "inlineTemplate": "<div>Do you remain at the party, even though you are feeling anxious? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}
        ]
    },
    {
        "inherit": {
            "set": "posneg",
            "type": "random"
        },
        "stimuli": [
            {"inherit": {"set": "error"}},
            {
                data: {

                    "negativeKey": "x",
                    "negativeWord": "an[ ]ious",
                    "positiveKey": "n",
                    "positiveWord": "fi[ ]e",
                    "statement": " You are at a class that your company has sent you to. Your teacher asks each member of the group to stand up and introduce himself or herself. During your brief presentation you momentarily forget what you plan to say, but quickly recover; you guess that others thought you sounded "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "handle": "question",
                 data: {

                	positiveAnswer:"n",
                	negativeAnswer:"y"
                },
                "media": {
                    "inlineTemplate": "<div>Did the other people in the workshop think you sounded nervous while you spoke? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}
        ]
    },
    {
        "inherit": {
            "set": "posneg",
            "type": "random"
        },
        "stimuli": [
            {"inherit": {"set": "error"}},
            {
                data: {

                    "negativeKey": "u",
                    "negativeWord": "d[ ]ll",
                    "positiveKey": "l",
                    "positiveWord": "intel[ ]igent",
                    "statement": " A new teacher is hired for your history class and you hear that he is very disciplined and hard-working. When you meet him for the first time to discuss your work and interests, you think that he found you to be "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "handle": "question",
                 data: {

                	positiveAnswer:"y",
                	negativeAnswer:"n"
                },
                "media": {
                    "inlineTemplate": "<div>Does your new teacher have a good opinion of you? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}
        ]
    },
    {
        "inherit": {
            "set": "posneg",
            "type": "random"
        },
        "stimuli": [
            {"inherit": {"set": "error"}},
            {
                data: {

                    "negativeKey": "w",
                    "negativeWord": "a[ ]ful",
                    "positiveKey": "y",
                    "positiveWord": "st[ ]lish",
                    "statement": " You arrive at a party in a new outfit. Everyone turns to look at you as you walk in. You decide that they must think that you look "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "handle": "question",
                 data: {

                	positiveAnswer:"y",
                	negativeAnswer:"n"
                },
                "media": {
                    "inlineTemplate": "<div>Did everyone at the party like your new outfit? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}
        ]
    },
    {
        "inherit": {
            "set": "posneg",
            "type": "random"
        },
        "stimuli": [
            {"inherit": {"set": "error"}},
            {
                data: {

                    "negativeKey": "n",
                    "negativeWord": "u[ ]interesting",
                    "positiveKey": "t",
                    "positiveWord": "in[ ]ersting",
                    "statement": " You arrange to meet your date at 8 p. m. in a local bar. You arrive on time and find that he/she is not there yet. After your last conversation, you thought that he/she found you "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "handle": "question",
                 data: {

                	positiveAnswer:"y",
                	negativeAnswer:"n"
                },
                "media": {
                    "inlineTemplate": "<div>Was your date interested in you after your last conversation? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}
        ]
    },
    ]},
    { "inherit": { "set": "vivid" } },    {
	mixer: 'random',
		    data:[
    {
        "inherit": {
            "set": "posneg",
            "type": "random"
        },
        "stimuli": [
            {"inherit": {"set": "error"}},
            {
                data: {

                    "negativeKey": "n",
                    "negativeWord": "ba[ ]krupt",
                    "positiveKey": "a",
                    "positiveWord": "ok[ ]y",
                    "statement": " Your credit card bill for the month will arrive soon. You spent a bit more this month than you normally do. When you think about your future finances, you feel like you will be  "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "handle": "question",
                 data: {

                	positiveAnswer:"n",
                	negativeAnswer:"y"
                },
                "media": {
                    "inlineTemplate": "<div>Are you likely to go broke in the future? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}
        ]
    },
    {
        "inherit": {
            "set": "posneg",
            "type": "random"
        },
        "stimuli": [
            {"inherit": {"set": "error"}},
            {
                data: {

                    "negativeKey": "x",
                    "negativeWord": "an[ ]ious",
                    "positiveKey": "f",
                    "positiveWord": "con[ ]ident",
                    "statement": " You are taking a challenging exam. On one of the questions, you are 90% certain that you know the correct answer. When you respond, you feel "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "handle": "question",
                 data: {

                	positiveAnswer:"n",
                	negativeAnswer:"y"
                },
                "media": {
                    "inlineTemplate": "<div>Are you uneasy about your answer? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}
        ]
    },
    {
        "inherit": {
            "set": "posneg",
            "type": "random"
        },
        "stimuli": [
            {"inherit": {"set": "error"}},
            {
                data: {

                    "negativeKey": "j",
                    "negativeWord": "unen[ ]oyable",
                    "positiveKey": "y",
                    "positiveWord": "enjo[ ]able",
                    "statement": " You get home from work on a Friday evening and wonder if you locked the office door. You usually lock the door, but still feel anxious not knowing for sure. Your weekend away from the office will be "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "handle": "question",
                 data: {

                	positiveAnswer:"y",
                	negativeAnswer:"n"
                },
                "media": {
                    "inlineTemplate": "<div>Will you still be able to enjoy the weekend, even if you are wondering about the lock? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}
        ]
    },
    {
        "inherit": {
            "set": "posneg",
            "type": "random"
        },
        "stimuli": [
            {"inherit": {"set": "error"}},
            {
                data: {

                    "negativeKey": "r",
                    "negativeWord": "wor[ ]isome",
                    "positiveKey": "x",
                    "positiveWord": "e[ ]citing",
                    "statement": " Your professor asks you what career you plan to pursue. You respond that you haven\u2019t decided yet. Thinking about your future is "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "handle": "question",
                 data: {

                	positiveAnswer:"y",
                	negativeAnswer:"n"
                },
                "media": {
                    "inlineTemplate": "<div>Do you enjoy thinking about your future? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}
        ]
    },
    {
        "inherit": {
            "set": "posneg",
            "type": "random"
        },
        "stimuli": [
            {"inherit": {"set": "error"}},
            {
                data: {

                    "negativeKey": "i",
                    "negativeWord": "worr[ ]ed",
                    "positiveKey": "l",
                    "positiveWord": "ca[ ]m",
                    "statement": " You are ready to purchase a new car. After considering all the options, you are still torn between two cars. When you tell the dealer which car you decided to buy, you feel "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "handle": "question",
                 data: {

                	positiveAnswer:"n",
                	negativeAnswer:"y"
                },
                "media": {
                    "inlineTemplate": "<div>Are you anxious about your decision? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}
        ]
    },
    {
        "inherit": {
            "set": "posneg",
            "type": "random"
        },
        "stimuli": [
            {"inherit": {"set": "error"}},
            {
                data: {

                    "negativeKey": "n",
                    "negativeWord": "u[ ]manageable",
                    "positiveKey": "g",
                    "positiveWord": "mana[ ]eable",
                    "statement": " Your son is going camping with his friend\u2019s family for two days. There will be no cell phone reception and you are worried about not being able to check in with him. Your worries over the next two days are going to be "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "handle": "question",
                 data: {

                	positiveAnswer:"y",
                	negativeAnswer:"n"
                },
                "media": {
                    "inlineTemplate": "<div>Are you able to manage your worries about your son? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}
        ]
    },
    {
        "inherit": {
            "set": "posneg",
            "type": "random"
        },
        "stimuli": [
            {"inherit": {"set": "error"}},
            {
                data: {

                    "negativeKey": "o",
                    "negativeWord": "po[ ]r",
                    "positiveKey": "n",
                    "positiveWord": "fi[ ]e",
                    "statement": " Your daughter is getting married, and you agreed to help pay for the wedding. As the costs begin to pile up, you realize that the wedding is going to be much more expensive than you previously thought. You feel that after you help pay for the wedding, you will be "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "handle": "question",
                 data: {

                	positiveAnswer:"n",
                	negativeAnswer:"y"
                },
                "media": {
                    "inlineTemplate": "<div> Do you think you will be poor after helping your daughter pay for the wedding? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}
        ]
    },
    {
        "inherit": {
            "set": "posneg",
            "type": "random"
        },
        "stimuli": [
            {"inherit": {"set": "error"}},
            {
                data: {

                    "negativeKey": "k",
                    "negativeWord": "ris[ ]y",
                    "positiveKey": "f",
                    "positiveWord": "sa[ ]e",
                    "statement": " You are looking after a friend\u2019s child. The child you are watching asks to go to the park in a different neighborhood. In your opinion, an ordinary outing like this is "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "handle": "question",
                 data: {

                	positiveAnswer:"n",
                	negativeAnswer:"y"
                },
                "media": {
                    "inlineTemplate": "<div>Is going to a park in a different neighborhood dangerous? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}
        ]
    },
    {
        "inherit": {
            "set": "posneg",
            "type": "random"
        },
        "stimuli": [
            {"inherit": {"set": "error"}},
            {
                data: {

                    "negativeKey": "g",
                    "negativeWord": "lar[ ]e",
                    "positiveKey": "n",
                    "positiveWord": "mi[ ]imal",
                    "statement": " Your friend enjoys ice-skating and persuades you to try it out. At the rink you put on the skates and step on the ice. You glide forward, slowly at first, then faster, and think your chances of getting injured are "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "handle": "question",
                 data: {

                	positiveAnswer:"n",
                	negativeAnswer:"y"
                },
                "media": {
                    "inlineTemplate": "<div>Do you think you will get hurt when ice skating? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}
        ]
    },
    {
        "inherit": {
            "set": "posneg",
            "type": "random"
        },
        "stimuli": [
            {"inherit": {"set": "error"}},
            {
                data: {

                    "negativeKey": "n",
                    "negativeWord": "u[ ]enjoyable",
                    "positiveKey": "j",
                    "positiveWord": "en[ ]oyable",
                    "statement": " You just left for vacation and cannot remember if you shut off the stove after the last time you cooked. You become anxious not knowing whether the stove is on or off. With these thoughts in mind, your vacation will be "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "handle": "question",
                 data: {

                	positiveAnswer:"y",
                	negativeAnswer:"n"
                },
                "media": {
                    "inlineTemplate": "<div>Will you still be able to enjoy your vacation, even if you are wondering about your stove? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}
        ]
    },
    {
        "inherit": {
            "set": "posneg",
            "type": "random"
        },
        "stimuli": [
            {"inherit": {"set": "error"}},
            {
                data: {

                    "negativeKey": "b",
                    "negativeWord": "rob[ ]ed",
                    "positiveKey": "n",
                    "positiveWord": "fi[ ]e",
                    "statement": " You are bringing in the mail for your neighbor while she is away. Each day on your way out of her house, you lock the front door and check it. Today you were rushing and think you forgot to check the lock. The house will probably be "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "handle": "question",
                 data: {

                	positiveAnswer:"n",
                	negativeAnswer:"y"
                },
                "media": {
                    "inlineTemplate": "<div>Will your neighbor\u2019s house probably be robbed? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}
        ]
    },
    {
        "inherit": {
            "set": "posneg",
            "type": "random"
        },
        "stimuli": [
            {"inherit": {"set": "error"}},
            {
                data: {

                    "negativeKey": "m",
                    "negativeWord": "i[ ]possible",
                    "positiveKey": "s",
                    "positiveWord": "pos[ ]ible",
                    "statement": " You heard in the news that a lot of businesses are currently downsizing. You think about what will happen if you lose your job. You decide that finding another job will be "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "handle": "question",
                 data: {

                	positiveAnswer:"n",
                	negativeAnswer:"y"
                },
                "media": {
                    "inlineTemplate": "<div> Do you think it will be impossible to find a new job? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}
        ]
    },

    {
        "inherit": {
            "set": "posneg",
            "type": "random"
        },
        "stimuli": [
            {"inherit": {"set": "error"}},
            {
                data: {

                    "negativeKey": "c",
                    "negativeWord": "si[ ]k",
                    "positiveKey": "i",
                    "positiveWord": "f[ ]ne",
                    "statement": " You have not heard from your elderly neighbor recently. You normally visit with him regularly, so it is strange that you have not seen him lately. Your neighbor has probably been "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "handle": "question",
                 data: {

                	positiveAnswer:"n",
                	negativeAnswer:"y"
                },
                "media": {
                    "inlineTemplate": "<div>Has your elderly neighbor likely been sick? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}
        ]
    },
    {
        "inherit": {
            "set": "posneg",
            "type": "random"
        },
        "stimuli": [
            {"inherit": {"set": "error"}},
            {
                data: {

                    "negativeKey": "x",
                    "negativeWord": "an[ ]ious",
                    "positiveKey": "l",
                    "positiveWord": "re[ ]axed",
                    "statement": " This morning you left the house and began your drive to work. About 10 minutes into your commute, you wonder if you remembered to lock the door to your home. As you continue to drive, you feel "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "handle": "question",
                 data: {

                	positiveAnswer:"y",
                	negativeAnswer:"n"
                },
                "media": {
                    "inlineTemplate": "<div> Are you able to stay calm, despite being unsure whether you remembered to lock your door? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}
        ]
    },
    {
        "inherit": {
            "set": "posneg",
            "type": "random"
        },
        "stimuli": [
            {"inherit": {"set": "error"}},
            {
                data: {

                    "negativeKey": "n",
                    "negativeWord": "u[ ]manageable",
                    "positiveKey": "g",
                    "positiveWord": "mana[ ]eable",
                    "statement": " Your son is going camping with his friend\u2019s family for two days. There will be no cell phone reception and you are worried about not being able to check in with him. Your worries over the next two days are going to be "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "handle": "question",
                 data: {

                	positiveAnswer:"y",
                	negativeAnswer:"n"
                },
                "media": {
                    "inlineTemplate": "<div>Are you able to manage your worries about your son? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}
        ]
    },
    {
        "inherit": {
            "set": "posneg",
            "type": "random"
        },
        "stimuli": [
            {"inherit": {"set": "error"}},
            {
                data: {

                    "negativeKey": "r",
                    "negativeWord": "wor[ ]ied",
                    "positiveKey": "c",
                    "positiveWord": "pea[ ]eful",
                    "statement": " You are headed out for vacation. You leave before double checking that all the appliances in your house are turned off. As you close the door to your house, you feel "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "handle": "question",
                 data: {

                	positiveAnswer:"y",
                	negativeAnswer:"n"
                },
                "media": {
                    "inlineTemplate": "<div>Are you feeling calm as you leave for vacation, despite not double checking the appliances? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}
        ]
    },
    {
        "inherit": {
            "set": "posneg",
            "type": "random"
        },
        "stimuli": [
            {"inherit": {"set": "error"}},
            {
                data: {

                    "negativeKey": "n",
                    "negativeWord": "u[ ]successfully",
                    "positiveKey": "c",
                    "positiveWord": "suc[ ]essfully",
                    "statement": " Your friend is taking an airplane to come visit you. The idea of your friend taking an airplane makes you feel anxious. You think you will deal with your anxiety "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "handle": "question",
                 data: {

                	positiveAnswer:"y",
                	negativeAnswer:"n"
                },
                "media": {
                    "inlineTemplate": "<div>Are you able to successfully manage your nervousness? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}
        ]
    },
    {
        "inherit": {
            "set": "posneg",
            "type": "random"
        },
        "stimuli": [
            {"inherit": {"set": "error"}},
            {
                data: {

                    "negativeKey": "b",
                    "negativeWord": "rob[ ]ers",
                    "positiveKey": "n",
                    "positiveWord": "part[ ]er",
                    "statement": " You are at home alone watching TV at night. You must have been dozing because you suddenly wake up. You have the impression that you heard a frightening noise and then realize it must be your "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "handle": "question",
                 data: {

                	positiveAnswer:"n",
                	negativeAnswer:"y"
                },
                "media": {
                    "inlineTemplate": "<div> Should you be frightened when you hear a noise at night? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}
        ]
    },

    {
        "inherit": {
            "set": "posneg",
            "type": "random"
        },
        "stimuli": [
            {"inherit": {"set": "error"}},
            {
                data: {

                    "negativeKey": "g",
                    "negativeWord": "hi[ ]h",
                    "positiveKey": "o",
                    "positiveWord": "l[ ]w",
                    "statement": " You go to the hospital to visit a friend. Someone behind you coughs. If you wash your hands, the probability that you will get sick is "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "handle": "question",
                 data: {

                	positiveAnswer:"n",
                	negativeAnswer:"y"
                },
                "media": {
                    "inlineTemplate": "<div>Is it likely that you will become sick? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}
        ]
    },
    {
        "inherit": {
            "set": "posneg",
            "type": "random"
        },
        "stimuli": [
            {"inherit": {"set": "error"}},
            {
                data: {

                    "negativeKey": "o",
                    "negativeWord": "seri[ ]us",
                    "positiveKey": "n",
                    "positiveWord": "mi[ ]or",
                    "statement": " While making dinner, you accidentally touch a hot pan. You quickly put your finger under cold water. You think the burn is "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "handle": "question",
                 data: {

                	positiveAnswer:"y",
                	negativeAnswer:"n"
                },
                "media": {
                    "inlineTemplate": "<div>Will your burned finger be okay? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}
        ]
    },
    {
        "inherit": {
            "set": "posneg",
            "type": "random"
        },
        "stimuli": [
            {"inherit": {"set": "error"}},
            {
                data: {

                    "negativeKey": "i",
                    "negativeWord": "s[ ]ck",
                    "positiveKey": "s",
                    "positiveWord": "thir[ ]ty",
                    "statement": " Your throat is scratchier than it usually is. You think about why this may be. You decide it is probably because you are "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "handle": "question",
                 data: {

                	positiveAnswer:"y",
                	negativeAnswer:"n"
                },
                "media": {
                    "inlineTemplate": "<div>Will drinking water make your throat feel better? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}
        ]
    },
    {
        "inherit": {
            "set": "posneg",
            "type": "random"
        },
        "stimuli": [
            {"inherit": {"set": "error"}},
            {
                data: {

                    "negativeKey": "n",
                    "negativeWord": "u[ ]manageable",
                    "positiveKey": "a",
                    "positiveWord": "manage[ ]ble",
                    "statement": "Yesterday, you got together with a friend who coughed a few times while you were out. You are nervous that you might catch something from your friend. You decide that your anxiety is "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "handle": "question",
                 data: {

                	positiveAnswer:"y",
                	negativeAnswer:"n"
                },
                "media": {
                    "inlineTemplate": "<div> Can you manage your anxiety about potentially getting sick from your friend?</div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}
        ]
    },
    {
        "inherit": {
            "set": "posneg",
            "type": "random"
        },
        "stimuli": [
            {"inherit": {"set": "error"}},
            {
                data: {

                    "negativeKey": "n",
                    "negativeWord": "i[ ]tolerable",
                    "positiveKey": "b",
                    "positiveWord": "tolera[ ]le",
                    "statement": " You have not been feeling great recently, so you try to schedule a doctor\u2019s appointment. The soonest appointment you can get is a few days away, which makes you feel anxious. You know that your anxiety as you wait for the appointment is "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "handle": "question",
                 data: {

                	positiveAnswer:"y",
                	negativeAnswer:"n"
                },
                "media": {
                    "inlineTemplate": "<div>Do you think you will be able to handle your anxiety as you wait for the doctor\u2019s appointment? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}
        ]
    },
    {
        "inherit": {
            "set": "posneg",
            "type": "random"
        },
        "stimuli": [
            {"inherit": {"set": "error"}},
            {
                data: {

                    "negativeKey": "c",
                    "negativeWord": "si[ ]k",
                    "positiveKey": "i",
                    "positiveWord": "f[ ]ne",
                    "statement": "Yesterday you spent the day with your friend and her new infant. A few times during the day, the infant coughed and sneezed on you, and you wonder if you will catch something. Tomorrow, you will probably be "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "handle": "question",
                 data: {

                	positiveAnswer:"n",
                	negativeAnswer:"y"
                },
                "media": {
                    "inlineTemplate": "<div> Will you get sick from your friend's new baby?</div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}
        ]
    },
    {
        "inherit": {
            "set": "posneg",
            "type": "random"
        },
        "stimuli": [
            {"inherit": {"set": "error"}},
            {
                data: {

                    "negativeKey": "a",
                    "negativeWord": "unhe[ ]lthy",
                    "positiveKey": "l",
                    "positiveWord": "hea[ ]thy",
                    "statement": " At your routine doctor's appointment, your doctor decides to run a few tests to evaluate your health. The doctor tells you that she will get you your results within a week. While you wait for your results, you think you are likely "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "handle": "question",
                 data: {

                	positiveAnswer:"y",
                	negativeAnswer:"n"
                },
                "media": {
                    "inlineTemplate": "<div>Do you think that you are healthy? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}
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