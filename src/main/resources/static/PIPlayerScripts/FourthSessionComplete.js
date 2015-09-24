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
            {handle:'stall',media:{html:"<div class='stim'>Oops, that answer is incorrect; please re-read and in a moment you will have a chance to answer again.</div>"}, css:{fontSize:'20px',color:'black', 'text-align':'center'}, location:{top:70}, nolog:true}
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
                    {type:'setTrialAttr',setter:{correctOnLetter:"true"}}  // set to true - will get set to false later if incorrectly answered.

                ]
            },
            { // Watch for correct answer for a positive missing letter.
                conditions: [
                    {type:'inputEqualsStim', property:'positiveKey'},
                    {type:'trialEquals', property:'positive', value:true},
                    {type:'globalEquals', property:'askingQuestion', value:false}],
                actions: [
                    {type:'custom',fn:function(options,eventData){
                        var span = $("span.incomplete");
                        var text = span.text().replace(' ', eventData["handle"]);
                        span.text(text);
                    }},
                    {type:'trigger',handle : 'correct'}
                ]
            },
            { // Watch for correct answer of a negative missing letter.
                conditions: [
                    {type:'inputEqualsStim', property:'negativeKey'},
                    {type:'trialEquals', property:'positive', value:false},
                    {type:'globalEquals', property:'askingQuestion', value:false}],
                actions: [
                    {type:'custom',fn:function(options,eventData){
                        var span = $("span.incomplete");
                        var text = span.text().replace(' ', eventData["handle"]);
                        span.text(text);
                    }},
                    {type:'trigger',handle : 'correct'}
                ]
            },
            { // Display a red X on incorrect input for positive responses.
                conditions: [
                    {type:'inputEqualsStim', property:'positiveKey', negate:'true'},
                    {type:'trialEquals', property:'positive', value:true},
                    {type:'inputEquals',value:'correct', negate:'true'},
                    {type:'globalEquals', property:'askingQuestion', value:false},
                    {type:'inputEquals',value:'askQuestion', negate:'true'}
                ],
                actions: [
                    {type:'setTrialAttr',setter:function(trialData, eventData){
                        if(trialData.first_letter_latency == null) {
                            trialData.first_letter_latency = Math.floor(eventData.latency);
                        }
                    }},
                    {type:'showStim',handle:'error'},
                    {type:'setTrialAttr',setter:{correctOnLetter:"false"}},
                    {type:'setInput',input:{handle:'clear', on:'timeout',duration:500}}
                ]
            },
            { // Display a red X on incorrect input for negative responses.
                conditions: [
                    {type:'inputEqualsStim', property:'negativeKey', negate:'true'},
                    {type:'trialEquals', property:'positive', value:false},
                    {type:'inputEquals',value:'correct', negate:'true'},
                    {type:'globalEquals', property:'askingQuestion', value:false},
                    {type:'inputEquals',value:'askQuestion', negate:'true'}
                ],
                actions: [
                    {type:'setTrialAttr',setter:function(trialData, eventData){
                        if(trialData.first_letter_latency == null) {
                            trialData.first_letter_latency = Math.floor(eventData.latency);
                        }
                    }},
                    {type:'showStim',handle:'error'},
                    {type:'setTrialAttr',setter:{correctOnLetter:"false"}},
                    {type:'setInput',input:{handle:'clear', on:'timeout',duration:500}}
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
                    {type:'setInput',input:{handle:'clear', on:'timeout',duration:500}},
                ]
            },
            {
                conditions: [{type:'inputEquals', value:'delay'}],
                actions: [
                    {type:'setInput',input:{handle:'y',on:'keypressed',key: 'y'}},
                    {type:'setInput',input:{handle:'n',on:'keypressed',key: 'n'}},
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
                    {type:'showStim', handle:'stall'},
                    {type:'setInput',input:{handle:'delay',on:'timeout',duration:5000}},
                    {type:'setTrialAttr',setter:{correctOnQuestion:"false"}},
                    {type:'setInput',input:{handle:'clear', on:'timeout',duration:500}},
                ]
            },
            {
                conditions: [{type:'inputEquals', value:'delay'}],
                actions: [
                    {type:'setInput',input:{handle:'y',on:'keypressed',key: 'y'}},
                    {type:'setInput',input:{handle:'n',on:'keypressed',key: 'n'}},
                    {type:'showStim', handle:'yesno'},
                    {type:'hideStim', handle:'stall'},
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
    API.addTrialSets('posneg',[
                    { inherit:'base', data: {positive:true}},
                    { inherit:'base', data: {positive:false}}
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
            "set": "posneg",
            "type": "random"
        },
        "stimuli": [
            {"inherit": {"set": "error"}},
            {
                "data": {
                    "negativeKey": "a",
                    "negativeWord": "neg[ ]tive",
                    "positiveKey": "t",
                    "positiveWord": "posi[ ]ive",
                    "statement": " Your supervisor asks you to write a report. The finished document is quite brief but took a lot of time and effort. Based on your writing, you expect your supervisor\u2019s opinion of you will be "
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
                    "inlineTemplate": "<div>Do you expect your supervisor to have a positive opinion of you after reading your report? </div>"
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
                    "negativeWord": "bo[ ]ing",
                    "positiveKey": "s",
                    "positiveWord": "intere[ ]ting",
                    "statement": " You are required to give a presentation to a group of work colleagues that you know well. They are all quiet during your presentation. As you think about the presentation later that day, you think that your colleagues found your presentation "
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
                    "inlineTemplate": "<div>Did you colleagues enjoy your presentation? </div>"
                     }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}} 
        ]
    },
    ]
        },
        {
            "inherit": {
                "set": "vivid"
            }
        },
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
                    "negativeKey": "r",
                    "negativeWord": "awkwa[ ]d",
                    "positiveKey": "c",
                    "positiveWord": "ni[ ]e",
                    "statement": " You are out of town with your basketball team and are sharing a hotel room with someone you do not know very well. As you unpack, there is little conversation between you. You think your teammate thinks you are "
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
                    "inlineTemplate": "<div>Do you think your teammate likes you? </div>"
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
                    "negativeKey": "f",
                    "negativeWord": "insuf[ ]icient",
                    "positiveKey": "a",
                    "positiveWord": "appropri[ ]te",
                    "statement": " You have an English assignment to write and try very hard to make sure it is comprehensive. The final copy is quite long. When your teacher reads it, you think she will judge your efforts as "
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
                    "inlineTemplate": "<div>Will your teacher judge your efforts as adequate? </div>"
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
                    "negativeKey": "v",
                    "negativeWord": "a[ ]oid",
                    "positiveKey": "s",
                    "positiveWord": "an[ ]wer",
                    "statement": " An old acquaintance just moved into your city and asks you out to coffee. You are nervous about seeing him again after many years and could easily tell him that you are too busy. When you calls about meeting, you "
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
                    "inlineTemplate": "<div>Do you take your acquaintance\u2019s call to meet for coffee? </div>"
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
                    "negativeKey": "c",
                    "negativeWord": "can[ ]el",
                    "positiveKey": "a",
                    "positiveWord": "st[ ]y",
                    "statement": " You just arrived at a restaurant for a date, and you feel nervous. You consider cancelling. In the end you decide to "
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
                    "inlineTemplate": "<div>Do you cancel the date? </div>"
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
                    "negativeWord": "bo[ ]ed",
                    "positiveKey": "c",
                    "positiveWord": "ex[ ]ited",
                    "statement": " You are put in charge of your office\u2019s fundraising efforts and quickly realize that your coworkers are not very interested in the fundraising. At a meeting, you give a presentation about the importance of helping with the fundraising, during which your coworkers are quiet. After your presentation you think you coworkers seem more "
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
                    "inlineTemplate": "<div>Do your coworkers seem bored after your presentation? </div>"
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
                    "negativeWord": "poo[ ]ly",
                    "positiveKey": "v",
                    "positiveWord": "con[ ]incingly",
                    "statement": " As a member of a local charity, you are asked to promote your fundraising evens on local radio. You know that the station is widely listened to. You expect that the other committee members will think you spoke "
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
                    "inlineTemplate": "<div>Do you think your committee members thought you were a bad speaker? </div>"
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
                    "negativeKey": "o",
                    "negativeWord": "fo[ ]lish",
                    "positiveKey": "r",
                    "positiveWord": "sma[ ]t",
                    "statement": " As part of a language course, you are required to take an oral exam. The material has been briefly covered in class and you were requested to do some individual research. As you wait your turn, you feel that others will think you seem "
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
                    "inlineTemplate": "<div>Do others think you seem intelligent when presenting? </div>"
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
                    "negativeWord": "bo[ ]ing",
                    "positiveKey": "d",
                    "positiveWord": "frien[ ]ly",
                    "statement": " Your partner asks you to go to an anniversary dinner that his/her company is holding. You have not met any of his/her work colleagues before. Getting ready to go, you think that the new people you will meet will find you "
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
                    "inlineTemplate": "<div>Will you be disliked by your new acquaintances? </div>"
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
                    "negativeWord": "unbea[ ]able",
                    "positiveKey": "k",
                    "positiveWord": "o[ ]ay",
                    "statement": " You have a date scheduled with someone you have liked for awhile. One the day of the date, you feel a little nervous as you wonder if he/she will like you as much as you like him/her. Feeling nervous is "
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
                    "inlineTemplate": "<div>Is it tolerable to feel nervous before your date? </div>"
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
                    "negativeKey": "h",
                    "negativeWord": "rus[ ]ed",
                    "positiveKey": "s",
                    "positiveWord": "promi[ ]ing",
                    "statement": " Your orchestra asks you to play a solo at the next concert. You practice for a few weeks until you feel ready to play with the orchestra. At the first performance you make one mistake, and the conductor probably thinks that your work is "
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
                    "inlineTemplate": "<div>Is the conductor disappointed with your performance? </div>"
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
                    "negativeKey": "l",
                    "negativeWord": "fai[ ]ure",
                    "positiveKey": "c",
                    "positiveWord": "suc[ ]ess",
                    "statement": " You decide to have a barbeque, as the weather is so nice. As your friends arrive, you realize many of them do not know each other well. You realize that your friends probably think that as a host, you are a "
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
                    "inlineTemplate": "<div>Do your friends have a negative opinion of you as a host? </div>"
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
                    "negativeKey": "b",
                    "negativeWord": "em[ ]arassed",
                    "positiveKey": "h",
                    "positiveWord": "ent[ ]usiastic",
                    "statement": " A friend suggests that you join an evening class on creative writing. You go online and read information about the class. The thought of other people looking at your writing makes you feel "
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
                    "inlineTemplate": "<div>Do you expect to feel uncomfortable if others look at your work? </div>"
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
                    "negativeKey": "b",
                    "negativeWord": "dum[ ]",
                    "positiveKey": "m",
                    "positiveWord": "nor[ ]al",
                    "statement": " While in a meeting your boss calls on you to write a few notes on the board. As she dictates, you jot down what she says, and everyone watches you while you write. As you hope that you are not making any spelling errors on the board, the others are probably thinking you are "
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
                    "inlineTemplate": "<div>Do your coworkers judge you negatively while you write on the board? </div>"
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
                    "negativeKey": "t",
                    "negativeWord": "incompe[ ]ent",
                    "positiveKey": "p",
                    "positiveWord": "pre[ ]ared",
                    "statement": " You have completed a first aid course and have to take a practical test, in which you demonstrate what you learned in front of your classmates. You spent the evening before practicing for the difficult test. As you begin the test, your classmates observing you think you are "
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
                    "inlineTemplate": "<div>Do your classmates think you are prepared for the practical test? </div>"
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
                    "negativeKey": "y",
                    "negativeWord": "l[ ]ing",
                    "positiveKey": "c",
                    "positiveWord": "sin[ ]ere",
                    "statement": " You are working on a project with a coworker in a coffee shop and you accidentally spill your coffee on her papers. As you clean up the mess, your coworkers says that she is not upset with you because of the spill. You think that she is "
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
                    "inlineTemplate": "<div>Is your coworker mad at you for spilling coffee on her papers? </div>"
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
                    "negativeKey": "d",
                    "negativeWord": "od[ ]",
                    "positiveKey": "m",
                    "positiveWord": "nor[ ]al",
                    "statement": " You arrive to a morning meeting right on time, but you are the last one to arrive. You coworkers look at you as you pull out your chair, and it squeaks loudly. They probably think that you are "
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
                    "inlineTemplate": "<div>Do your coworkers find you strange? </div>"
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
                    "negativeKey": "c",
                    "negativeWord": "un[ ]lear",
                    "positiveKey": "p",
                    "positiveWord": "exce[ ]tional",
                    "statement": " Your boss asks to see you following the recent submission of a paper you wrote. He tells you that he wants to talk to you about your work. You think he will tell you that your writing is "
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
                    "inlineTemplate": "<div>Do you think your boss likes your writing? </div>"
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
                    "negativeWord": "wor[ ]ied",
                    "positiveKey": "s",
                    "positiveWord": "plea[ ]ed",
                    "statement": " You are invited to a charity ball and decide to wear a formal outfit. The next day you see that your photograph is in the local newspaper with a report. The thought of everyone seeing it makes you feel "
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
                    "inlineTemplate": "<div>Are you looking forward to others seeing your picture in the paper? </div>"
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
                    "negativeKey": "p",
                    "negativeWord": "disap[ ]ointed",
                    "positiveKey": "s",
                    "positiveWord": "plea[ ]ed",
                    "statement": " You are giving a speech at a convention for work. As you look into the crowd, you notice your boss\u2019s expression is neutral. You think that her expression means that she is "
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
                    "inlineTemplate": "<div>Does your boss\u2019s neutral expression mean that she is disappointed in your speech? </div>"
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
                    "negativeKey": "u",
                    "negativeWord": "fail[ ]re",
                    "positiveKey": "g",
                    "positiveWord": "intelli[ ]ence",
                    "statement": " A new task is assigned to your department at work and your supervisor asks you to be responsible for it. You have no guidelines to follow, and you ask a colleague for advice. You colleague probably sees this as a sign of "
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
                    "inlineTemplate": "<div>Did you make a mistake by asking for advice on the new project? </div>"
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
                    "negativeKey": "n",
                    "negativeWord": "unma[ ]ageable",
                    "positiveKey": "g",
                    "positiveWord": "mana[ ]eable",
                    "statement": " For work, you have to give a talk to another department. As you think about the talk, you feel butterflies in your stomach. You feel nervous, and you think your nerves will be "
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
                    "inlineTemplate": "<div>Are you able to tolerate your anxiety? </div>"
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
                    "negativeKey": "g",
                    "negativeWord": "lar[ ]e",
                    "positiveKey": "m",
                    "positiveWord": "s[ ]all",
                    "statement": " You have decided to go on a tour of a cave, even though you feel nervous about being in such an enclosed space. You go deep inside the first cave. You think the chances that you will get trapped inside the case are "
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
                    "inlineTemplate": "<div>Are you likely to get trapped inside of the cave? </div>"
                     }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}} 
        ]
    },
    ]
        },
        {
            "inherit": {
                "set": "vivid"
            }
        },
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
                    "negativeWord": "dang[ ]rous",
                    "positiveKey": "u",
                    "positiveWord": "f[ ]n",
                    "statement": " You are on holiday at a mountain resort and are learning how to ski. This is your first attempt to ski downhill on your own. At a moderate speed you approach the first bend and think that skiing is "
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
                    "inlineTemplate": "<div>Is it safe to try skiing? </div>"
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
                    "negativeKey": "l",
                    "negativeWord": "into[ ]erable",
                    "positiveKey": "n",
                    "positiveWord": "ma[ ]ageable",
                    "statement": " You just finished a hard exam and you do not know if you did well or not. You feel nervous as you think about what grade you will get. You think being nervous is "
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
                    "inlineTemplate": "<div> Can you tolerate your nervousness as you wait to find out your grade? </div>"
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
                    "negativeKey": "n",
                    "negativeWord": "i[ ]tolerable",
                    "positiveKey": "r",
                    "positiveWord": "tole[ ]able",
                    "statement": " You are backpacking through Europe with your best friend. You have a general idea of what you will do each day, but you didn\u2019t plan out every detail ahead of time. This uncertaintly causes some anxiety that will be "
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
                    "inlineTemplate": "<div>Is the spontaneity of your trip going to be tolerable? </div>"
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
                    "negativeKey": "p",
                    "negativeWord": "de[ ]ressed",
                    "positiveKey": "n",
                    "positiveWord": "fi[ ]e",
                    "statement": " Your son recently went away to college. When you talked to him on the phone last, he sounded different than usual. When you think about the phone call, you decide your son is probably "
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
                    "inlineTemplate": "<div>Is your son doing ok at college? </div>"
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
                    "negativeKey": "d",
                    "negativeWord": "ina[ ]equate",
                    "positiveKey": "c",
                    "positiveWord": "suc[ ]essful",
                    "statement": " You are applying to a prestigious internship program. As you prepare your resume, you reflect upon everything you have accomplished thus far. You record is strng, but not perfect, which makes you feel "
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
                    "inlineTemplate": "<div>Are you pleased with your record? </div>"
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
                    "negativeWord": "ter[ ]ified",
                    "positiveKey": "i",
                    "positiveWord": "thr[ ]lled",
                    "statement": " You are riding an airplane to London. As you look outside the window, you think about how you\u2019ve never been so far above the Atlantic Ocean. You feel "
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
                    "inlineTemplate": "<div> Is it exhilarating to be so high up? </div>"
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
                    "negativeKey": "s",
                    "negativeWord": "lo[ ]t",
                    "positiveKey": "n",
                    "positiveWord": "fi[ ]e",
                    "statement": " Your daughter is outside playing with friends. You look out the window and check on her, and do not see her. She is probably "
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
                    "inlineTemplate": "<div>Is your daughter okay? </div>"
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
                    "negativeKey": "i",
                    "negativeWord": "fa[ ]l",
                    "positiveKey": "a",
                    "positiveWord": "p[ ]ss",
                    "statement": " You are taking a class, and recently turned in a paper. You decided to re-read your submitted paper and realize that you accidentally deleted a section you meant to include in the paper. Given that you accidently made this deletion, you think that you are likely to "
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
                    "inlineTemplate": "<div>Are you going to get a passing grade on the paper? </div>"
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
                    "negativeKey": "j",
                    "negativeWord": "in[ ]ured",
                    "positiveKey": "n",
                    "positiveWord": "fi[ ]e",
                    "statement": " You are bouncing on your friend\u2019s trampoline. You have fun with your friends, but also consider their safety. While on the trampoline you will probably be "
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
                    "inlineTemplate": "<div>Are you likely to get hurt whole on the trampoline? </div>"
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
                    "negativeKey": "n",
                    "negativeWord": "u[ ]safe",
                    "positiveKey": "f",
                    "positiveWord": "sa[ ]e",
                    "statement": " You decide to donate blood at the local blood bank. You look around the room where you will give blood and notice it is very crowded. You decide the environment is "
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
                    "inlineTemplate": "<div>Is the blood bank harmless? </div>"
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
                    "negativeKey": "s",
                    "negativeWord": "dis[ ]atisfied",
                    "positiveKey": "t",
                    "positiveWord": "con[ ]ent",
                    "statement": " You have spent several weeks perfecting a project. When you turn it in, there are a few very minor adjustments you wish you had time to make. As you think about your work, you feel "
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
                    "inlineTemplate": "<div>Are you disappointed with the outcome of the project? </div>"
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
                    "negativeKey": "u",
                    "negativeWord": "uns[ ]ccessfully",
                    "positiveKey": "s",
                    "positiveWord": "succes[ ]fully",
                    "statement": " You let your teenage son go to an amusement park with some friends. You feel worried about letting your son go to the amusement park without any parents. You think you will deal with your worry "
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
                    "inlineTemplate": "<div>Will you be unsuccessful at dealing with your anxiety? </div>"
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
                    "negativeKey": "i",
                    "negativeWord": "h[ ]gh",
                    "positiveKey": "w",
                    "positiveWord": "lo[ ]",
                    "statement": " You have decided to go back to school for another degree. Although you have been saving for this for the past few years, you worry about not having an income while you are back in school. The likelihood that you will go broke while in school is "
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
                    "inlineTemplate": "<div>Will you become broke once you go back to school? </div>"
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
                    "negativeKey": "o",
                    "negativeWord": "po[ ]rly",
                    "positiveKey": "e",
                    "positiveWord": "w[ ]ll",
                    "statement": " You are auditioning for a play. You say most of your lines correctly, but mess up on one of two. You think the audition went "
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
                    "inlineTemplate": "<div> Do you feel good about the audition? </div>"
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
                "data": {
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
                "data": {
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
                "data": {
                    "negativeKey": "g",
                    "negativeWord": "hi[ ]h",
                    "positiveKey": "o",
                    "positiveWord": "l[ ]w",
                    "statement": " You rushed out of the house this morning. Once at work, you wonder if you forgot to turn off your space heater. The likelihood that the space heater will cause a fire is "
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
                    "inlineTemplate": "<div>Will your space heater cause a fire? </div>"
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
                    "negativeKey": "a",
                    "negativeWord": "intoler[ ]ble",
                    "positiveKey": "r",
                    "positiveWord": "tole[ ]able",
                    "statement": " You woke up this morning with a stiff neck. You wonder whether this is a minor injury or a sign of a larger problem, so you schedule an appointment with your doctor. You have to wait until tomorrow to see her, and you think that your anxiety while waiting for the appointment will be "
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
                    "inlineTemplate": "<div>Are you able to tolerate the anxiety while waiting to see your doctor? </div>"
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
                    "negativeKey": "g",
                    "negativeWord": "hi[ ]h",
                    "positiveKey": "n",
                    "positiveWord": "mi[ ]imal",
                    "statement": " Your neighbor bakes you a cake for your birthday. When she delivers it, she notes that she has recently gotten over the flu. Your chances of becoming sick are "
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
                    "inlineTemplate": "<div>Are you likely to get sick from the cake? </div>"
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
                    "negativeWord": "unbea[ ]able",
                    "positiveKey": "g",
                    "positiveWord": "mana[ ]eable",
                    "statement": " You found a dark mole on your leg and made an appointment with your doctor to get it looked at. Your doctor says she will get back to you with the test results within the next few days. You become anxious at the thought of waiting for test results for an undetermined amount of time, and your anxious feelings are "
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
                    "inlineTemplate": "<div>Are you able to manage your anxious feelings while waiting for your test results? </div>"
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
                    "negativeKey": "b",
                    "negativeWord": "un[ ]earable",
                    "positiveKey": "g",
                    "positiveWord": "mana[ ]eable",
                    "statement": " Your neighbor bakes you a cake for your birthday. When she delivers it, she notes that she has recently gotten over the flu. Your chances of becoming sick are "
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
                    "inlineTemplate": "<div>Will you remain healthy after eating the cake? </div>"
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
                    "negativeKey": "c",
                    "negativeWord": "can[ ]er",
                    "positiveKey": "l",
                    "positiveWord": "meaning[ ]ess",
                    "statement": " You notice that you have a mole on your arm that you haven't noticed before. You schedule a doctor's appointment to have it examined. As you wait for your appointment, you think the mole is likely "
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
                    "inlineTemplate": "<div>Is the new mole a sign of cancer? </div>"
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
                    "negativeKey": "c",
                    "negativeWord": "ul[ ]ers",
                    "positiveKey": "g",
                    "positiveWord": "hun[ ]er",
                    "statement": " You have been riding in the car all day with your partner and have not yet stopped for food. Once you stop, you realize that your stomach is starting to ache. This is probably a sign of "
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
                    "inlineTemplate": "<div>Does the ache in your stomach mean that you are hungry? </div>"
                }
            }
        ]
    }
]
    


        },
        {
            "inherit": {"set": "vivid"},
            layout: [{media : {template:"/PIPlayerScripts/vividness_last.html"}}]
        }
        ]);
    return API.script;
    // #### Activate the player
    //API.play();
});
/* don't forget to close the define wrapper */