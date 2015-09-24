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
        ],
        vivid:[
            {
                'handle':'vivid',
                media:'________'

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
                    { inherit:'base', data: {positive:true}}
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
            mixer: 'random',
            //n: 50,  // The total number of randomly selected trials to run.
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
                    "negativeKey": "t",
                    "negativeWord": "poin[ ]less",
                    "positiveKey": "e",
                    "positiveWord": "gr[ ]at",
                    "statement": " At a dinner party, you are introduced to someone new and chat with him for quite a while. When you call him the next week to suggest meeting again, he pauses for a moment. He probably thinks that getting together would be "
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
                    "inlineTemplate": "<div>Does your new acquaintance want to meet with you again? </div>"
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
                    "statement": " A friend invites you to a dinner party that she is holding. She tells you who the other guests are, but you do not recognize any of the other names. You go anyway and on the way there, you think that the other guests will find you "
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
                    "inlineTemplate": "<div>Will you be well-liked at the party? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}} 
        ]
    },
    ]
            },
    		{
            input: [
                        {handle:'Not at all vivid',on:'keypressed', key:'1'},
                        {handle:'Somewhat vivid',on:'keypressed', key:'2'},
                        {handle:'Moderately vivid',on:'keypressed', key:'3'},
                        {handle:'Very vivid',on:'keypressed', key:'4'},
                        {handle:'Totally vivid',on:'keypressed', key:'5'},
                ],
    			layout:[
    				{media:{'inlineTemplate':"<div> How vividly did you imagine yourself in the scenario? </div> <div> 1=<i>Not at all vivid</i> </div> <div> 2=<i>Somewhat vivid</i> </div> <div> 3=<i>Moderately vivid</i> </div> <div> 4=<i>Very vivid</i> </div> <div> 5=<i>Totally vivid</i> </div> <div>Please type the corresponding number </div>"}}
    			],
    			stimuli: [
    				{media :{'inlineTemplate':"<div class='vivid'>_______</div>"}}
    			],
    			interactions: [
    				// 1. Display Stimulus
    				{
    					/* when we begin the trial (condition) */
    					conditions: [{type:'begin'}],
    					/* show all stimuli (action) */
    					actions: [{type:'showStim',handle:'All'}]
    				},
    				{
    					conditions: [{type:'inputEquals',value:'Not at all vivid'}
    					             ],
    					/* end the trial */
    					actions: [
                        {type:'custom',fn:function(options,eventData){
                            var span = $("div.vivid");
                            var text = span.text().replace('_______', eventData["handle"]);
                            span.text(text);                    }},
                        {type:'trigger', handle:'vivid_switch',  on: 'timeout', duration: 500}],
    				},
                    {
                        conditions: [{type:'inputEquals', value:'vivid_switch'}],
    					actions: [{type:'log'}, {type:'endTrial'}]
                    },
    				{
    					conditions: [
    					             {type:'inputEquals',value:'Somewhat vivid'},
    					             ],
    					/* end the trial */
    					actions: [
                        {type:'custom',fn:function(options,eventData){
                            var span = $("div.vivid");
                            var text = span.text().replace('_______', eventData["handle"]);
                            span.text(text);                    }},
                        {type:'trigger', handle:'vivid_switch',  on: 'timeout', duration: 500}],
    				},
                    {
                        conditions: [{type:'inputEquals', value:'vivid_switch'}],
    					actions: [{type:'log'}, {type:'endTrial'}]
                    },
    				{
    					conditions: [
    					             {type:'inputEquals',value:'Moderately vivid'},
    					             ],
    					/* end the trial */
    					actions: [
                        {type:'custom',fn:function(options,eventData){
                            var span = $("div.vivid");
                            var text = span.text().replace('_______', eventData["handle"]);
                            span.text(text);                    }},
                        {type:'trigger', handle:'vivid_switch',  on: 'timeout', duration: 500}],
    				},
                    {
                        conditions: [{type:'inputEquals', value:'vivid_switch'}],
    					actions: [{type:'log'}, {type:'endTrial'}]
                    },
    				{
    					conditions: [
    					             {type:'inputEquals',value:'Very vivid'},
    					             ],
    					/* end the trial */
    					actions: [
                        {type:'custom',fn:function(options,eventData){
                            var span = $("div.vivid");
                            var text = span.text().replace('_______', eventData["handle"]);
                            span.text(text);                    }},
                        {type:'trigger', handle:'vivid_switch',  on: 'timeout', duration: 500}],
    				},
                    {
                        conditions: [{type:'inputEquals', value:'vivid_switch'}],
    					actions: [{type:'log'}, {type:'endTrial'}]
                    },
    				{
    					conditions: [
    					             {type:'inputEquals',value:'Totally vivid'}
    					             ],
    					/* end the trial */
    					actions: [
                        {type:'custom',fn:function(options,eventData){
                            var span = $("div.vivid");
                            var text = span.text().replace('_______', eventData["handle"]);
                            span.text(text);                    }},
                        {type:'trigger', handle:'vivid_switch',  on: 'timeout', duration: 500}],
    				},
                    {
                        conditions: [{type:'inputEquals', value:'vivid_switch'}],
    					actions: [{type:'log'}, {type:'endTrial'}]
                    }
    			]
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
                    "negativeKey": "l",
                    "negativeWord": "care[ ]ess",
                    "positiveKey": "m",
                    "positiveWord": "com[ ]endable",
                    "statement": " You have started a new job and you are given a task to do that normally takes a few days. You manage to finish it the same day. As you go over it, you boss finds only one mistake in your work. You expect he thinks your work is "
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
                    "inlineTemplate": "<div>Was your boss pleased with your work? </div>"
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
                    "negativeWord": "i[ ]nored",
                    "positiveKey": "c",
                    "positiveWord": "in[ ]luded",
                    "statement": " You meet someone new at a book club and have a stimulating discussion. Towards the end you find that she disagrees with something that you said. When the book club meets next, you expect that you will be "
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
                    "inlineTemplate": "<div>Will you be talked to during the next book club meeting? </div>"
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
                    "statement": " You are on a date with an attractive person. You go for a walk around a park together, and then get coffee. As you think about how the date is going, you feel nervous, and you know being nervous is "
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
                    "inlineTemplate": "<div>Is it difficult to tolerate your nervousness on the date? </div>"
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
                    "negativeWord": "ca[ ]eless",
                    "positiveKey": "n",
                    "positiveWord": "lear[ ]ing",
                    "statement": " You finish a task for your evening class that is due next week, and ask the tutor for his opinion. He says the work is good, apart from an incomplete section. You feel that he thinks you are "
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
                    "inlineTemplate": "<div>Was the tutor pleased with the quality of work on your paper? </div>"
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
                    "negativeWord": "aw[ ]ul",
                    "positiveKey": "m",
                    "positiveWord": "nor[ ]al",
                    "statement": " Your boss asks you a question, and you realize you respond incorrectly. Your boss corrects you. You think the experience of being corrected by your boss is "
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
                    "inlineTemplate": "<div>Is it ok to occasionally answer a question from your boss incorrectly? </div>"
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
                    "negativeWord": "unp[ ]ofessional",
                    "positiveKey": "g",
                    "positiveWord": "or[ ]anized",
                    "statement": " Some important people are visiting your office and you are asked to present a project to them. On the day of the presentation, you arrange your slides and mentally prepare yourself. You think that your performance will be evaluated as "
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
                    "inlineTemplate": "<div>Will the visitors be unimpressed with your performance? </div>"
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
                    "negativeWord": "offen[ ]ed",
                    "positiveKey": "e",
                    "positiveWord": "valu[ ]d",
                    "statement": " You overhear some work colleagues discussing other people and hear your name mentioned. You do not hear everything they say, but think that what they are saying would probably make you feel "
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
                    "inlineTemplate": "<div>Were your colleagues saying negative things about you? </div>"
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
                    "negativeWord": "sto[ ]",
                    "positiveKey": "t",
                    "positiveWord": "con[ ]inue",
                    "statement": " You are in a club, and are told you need to give a presentation to other club members. When it is time to present, you have a sip of water. You begin to speak, feel scared, and decide to "
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
                    "inlineTemplate": "<div>Do you stop your presentation when you feel scared? </div>"
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
                    "negativeKey": "k",
                    "negativeWord": "wea[ ]ness",
                    "positiveKey": "p",
                    "positiveWord": "com[ ]etence",
                    "statement": " At your computer class you finish your work early and so the teacher gives you a new task to do. You read through it and cannot think how to start, so you ask for advice. Your teacher probably sees you asking for help as a sign of  "
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
                    "inlineTemplate": "<div>Was your lecturer understanding when you asked for help? </div>"
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
                    "negativeWord": "anno[ ]ed",
                    "positiveKey": "s",
                    "positiveWord": "impre[ ]sed",
                    "statement": " You are out to dinner with a few friends. During the meal, one friend says something that you really disagree with. As you voice your opinion, your friends become quiet because they are "
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
                    "inlineTemplate": "<div>Are your friends annoyed by your point of view? </div>"
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
                    "negativeKey": "k",
                    "negativeWord": "aw[ ]ward",
                    "positiveKey": "s",
                    "positiveWord": "intere[ ]ting",
                    "statement": " You are required to go to a conference in Scotland for your firm. Your coworker who is supposed to go on the trip falls ill a few days before you leave, so your boss asks someone you don\u2019t know to go in his/her place. As you think about spending time with the person you do not know, you think he/she will think you are "
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
                    "inlineTemplate": "<div>Do you expect your new colleague to have a good impression of you? </div>"
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
                    "negativeWord": "terr[ ]ble",
                    "positiveKey": "x",
                    "positiveWord": "rela[ ]ing",
                    "statement": " You and your partner want to go to a remote destination for vacation. You worry about going because you will not have access to your email or cell phone while away. You are anxious as you make the trip reservations, and decide that the trip will be "
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
                    "inlineTemplate": "<div>Will the trip be relaxing? </div>"
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
                    "negativeWord": "foo[ ]ish",
                    "positiveKey": "t",
                    "positiveWord": "exci[ ]ing",
                    "statement": " Your boss calls a meeting to discuss a new project that will involve most of the staff at your office. You are suddenly asked to contribute your ideas to the discussion. You quickly come up with some ideas on the spot, and think that your colleagues will find your ideas to be "
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
                    "inlineTemplate": "<div>Did your colleagues dislike your ideas at the meeting? </div>"
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
                    "negativeWord": "in[ ]olerable",
                    "positiveKey": "l",
                    "positiveWord": "to[ ]erable",
                    "statement": " You are at a small party with people from your company. You feel nervous about spending time with your coworkers outside of work. While at the party, you decide your nervousness is "
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
                    "inlineTemplate": "<div>Can you manage your nervousness at the party with your coworkers? </div>"
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
                    "negativeWord": "bor[ ]ng",
                    "positiveKey": "u",
                    "positiveWord": "f[ ]n",
                    "statement": " You arrange to meet up with a friend who you have not seen for many years. You drive to the station to pick him up. When you arrive, you know he will find spending time with you  "
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
                    "inlineTemplate": "<div>Do you think your friend will have a good time with you? </div>"
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
                    "negativeWord": "ca[ ]m",
                    "positiveKey": "r",
                    "positiveWord": "sca[ ]ed",
                    "statement": " On entering an interview, the panel of interviewers welcome you. You are the third candidate to be seen today and as you sit down, you feel your cheeks turning red. You think that they probably see you as "
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
                    "inlineTemplate": "<div>Do you think the interviewers see you as a nervous person? </div>"
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
                    "negativeWord": "in[ ]olerable",
                    "positiveKey": "r",
                    "positiveWord": "tole[ ]able",
                    "statement": " You go out shopping with a friend who tends to be very loud. While you are shopping, other shoppers stop and look at you and your friend. The experience is "
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
                    "inlineTemplate": "<div>Is it OK for people to look at you and your friend while out shopping? </div>"
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
                    "negativeWord": "dis[ ]ppointing",
                    "positiveKey": "a",
                    "positiveWord": "valu[ ]ble",
                    "statement": " You have been a member of a choir for several years and enjoy performing at concerts. One evening, you are asked to sing on very short notice with another group. Afterwards you feel that the others found your contribution "
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
                    "inlineTemplate": "<div>Were the other members of the choir happy with your singing? </div>"
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
                    "negativeWord": "u[ ]comfortable",
                    "positiveKey": "f",
                    "positiveWord": "com[ ]ortable",
                    "statement": " When hanging out with a friend, the conversation dies down. Neither of you saying anything for a bit. As you think of a new conversation topic, your friend probably thinks you are "
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
                    "inlineTemplate": "<div>Does your friend think you are okay with the silence? </div>"
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
                    "negativeWord": "he[ ]itant",
                    "positiveKey": "f",
                    "positiveWord": "con[ ]ident",
                    "statement": " Your new supervisor calls a meeting to find out who everyone is and asks each of you in turn to present yourself and your area of expertise. When it is your turn to speak your pause frequently. As you reflect on the meeting later in the day, you feel that the supervisor thought you sounded "
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
                    "inlineTemplate": "<div>Do you think your supervisor thought you sounded unsure of yourself? </div>"
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
                    "positiveWord": "pre[ ]ent",
                    "statement": " Your boss asks you to give a speech at a conference. You are nervous about the idea of giving a speech, and consider telling your boss that you do not want to give the speech. After thinking about your anxiety, you decide that the speech is something you want to "
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
                    "inlineTemplate": "<div>Do you decide to give the speech, even though you are anxious? </div>"
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
                    "positiveKey": "s",
                    "positiveWord": "intere[ ]ting",
                    "statement": " You are on the committee of an amateur theater group, which is planning a new production. At the first meeting, the director asks you for ideas about which play to perform. Everybody pauses after you say your ideas, and you think the others find your thoughts "
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
                    "inlineTemplate": "<div>Did the other committee members like your ideas? </div>"
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
                    "statement": " You have a one-on-one meeting today with your intimidating boss, which you are nervous about. As you are meeting, you momentarily lose your train of thought. After that, your anxiety is "
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
                    "inlineTemplate": "<div>Are you able to tolerate your anxiety while meeting with your boss? </div>"
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
                    "negativeWord": "sc[ ]red",
                    "positiveKey": "c",
                    "positiveWord": "fas[ ]inated",
                    "statement": " You are at a metro stop waiting for a train. Standing by the edge, you realize you\u2019ve never thought about how far a drop it is to the rails below. You are "
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
                    "inlineTemplate": "<div>Are you terrified by the height of the metro platform? </div>"
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
                "data": {
                    "negativeKey": "r",
                    "negativeWord": "ter[ ]ified",
                    "positiveKey": "t",
                    "positiveWord": "exci[ ]ed",
                    "statement": " Your daughter goes horseback riding every weekend, and you reluctantly agree to go with her. When you get there, the riding instructor gives you a large horse to ride. Soon, the horse starts to gallop quickly and you feel "
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
                    "inlineTemplate": "<div>Is it fun to try riding the horse? </div>"
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
                "data": {
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
                "data": {
                    "negativeKey": "a",
                    "negativeWord": "b[ ]d",
                    "positiveKey": "o",
                    "positiveWord": "g[ ]od",
                    "statement": " Your parents are getting older, and have discussed moving into an assisted living facility. You find yourself wondering what their health will be like in the future, given their increasing age. After thinking about it, you think their health in the future will likely be "
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
                    "inlineTemplate": "<div>Do you think your parents will have poor health in the future? </div>"
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
                    "negativeWord": "fa[ ]lure",
                    "positiveKey": "c",
                    "positiveWord": "suc[ ]ess",
                    "statement": " You are driving home after a soccer game where your team won. As your mind wanders, you think about the one goal that you missed. Overall, you consider your performance a "
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
                    "inlineTemplate": "<div>Are you satisfied with your performance in the game? </div>"
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
                    "negativeWord": "up[ ]et",
                    "positiveKey": "c",
                    "positiveWord": "uncon[ ]erned",
                    "statement": " You are going to turn in a paper. As you walk, you glance down and realize there is a small typo on the last page of your paper. You believe that you should be "
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
                    "inlineTemplate": "<div>Should you be troubled about the small typo? </div>"
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
                    "negativeWord": "anxi[ ]is",
                    "positiveKey": "a",
                    "positiveWord": "c[ ]lm",
                    "statement": " You are on a hike with friends. You come to a suspension bridge you need to cross, but you aren\u2019t 100% sure it is safe even though your friends think it\u2019s probably OK. As you cross the bridge you feel "
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
                    "inlineTemplate": "<div>Are you worried as you cross the bridge? </div>"
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
                    "negativeWord": "si[ ]k",
                    "positiveKey": "s",
                    "positiveWord": "bu[ ]y",
                    "statement": " Every Monday, you call your grandmother to say hello. Today is Monday, and she did not pick up the phone when you called, which is unusual. Your grandmother probably did not answer the phone because she is "
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
                    "inlineTemplate": "<div>Is your grandmother unable to talk because she is ill? </div>"
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
                    "negativeWord": "mi[ ]erable",
                    "positiveKey": "y",
                    "positiveWord": "enjo[ ]able",
                    "statement": " You are out to dinner and realize that you are not wearing your favorite watch. You don\u2019t remember taking it off earlier, but think it is probably where you always keep it. You become a little anxious thinking about where the watch is, and your dinner ends up being "
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
                    "inlineTemplate": "<div>Is the dinner miserable while you worry about the watch? </div>"
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
                "data": {
                    "negativeKey": "p",
                    "negativeWord": "unpre[ ]ared",
                    "positiveKey": "t",
                    "positiveWord": "sa[ ]isfied",
                    "statement": " You are considering multiple options for where to go for spring break. You only have a little bit of information about each potential destination before you make your choice. When you decide, you feel "
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
                    "inlineTemplate": "<div>Are you content with your decision for where to go on Spring Break? </div>"
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
                    "negativeWord": "fa[ ]lure",
                    "positiveKey": "c",
                    "positiveWord": "suc[ ]ess",
                    "statement": " You are driving hope after a soccer game where your team won. As your mind wanders, you think about the one goal that you missed. Overall, you consider your performance a "
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
                    "inlineTemplate": "<div>Are you satisfied with your performance in the game? </div>"
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
                    "statement": " You heard a rumor your boss is planning to fire someone in your office. Although you are normally a good employee, you were late for a meeting earlier this week. You feel that the probability that you will get fired is "
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
                    "inlineTemplate": "<div>Do you think that you job is safe? </div>"
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
                    "negativeWord": "anxi[ ]is",
                    "positiveKey": "a",
                    "positiveWord": "c[ ]lm",
                    "statement": " You are on a hike with friends. You come to a suspension bridge you need to cross, but you aren\u2019t 100% sure it is safe even though your friends think it\u2019s probably OK. As you cross the bridge you feel "
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
                    "inlineTemplate": "<div>Are you worried as you cross the bridge? </div>"
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
                    "negativeKey": "r",
                    "negativeWord": "ter[ ]ified",
                    "positiveKey": "t",
                    "positiveWord": "exci[ ]ed",
                    "statement": " Your daughter goes horseback riding every weekend, and you reluctantly agree to go with her. When you get there, the riding instructor gives you a large horse to ride. Soon, the horse starts to gallop quickly and you feel "
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
                    "inlineTemplate": "<div>Is it fun to try riding the horse? </div>"
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
                    "negativeKey": "e",
                    "negativeWord": "intol[ ]rable",
                    "positiveKey": "a",
                    "positiveWord": "toler[ ]ble",
                    "statement": "You get blood drawn for your annual physical. Your doctor tells you that you will have to wait a few days to receive the results, and you worry about what the results will say. The worries are "
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
                    "inlineTemplate": "<div>Are your worries about your blood test results tolerable? </div>"
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
                    "negativeWord": "serio[ ]s",
                    "positiveKey": "g",
                    "positiveWord": "meanin[ ]less",
                    "statement": " Last weekend you ran your first race. Since then, you have been experiencing slight pain in your hip. This pain is probably "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                'handle': 'counter',
                customize: function(){
                    this.media = scorer.count + ' of 50';
                },
                 css:{fontSize:'20px'},
                 location:{top:90}
            },
            {
                "handle": "question",
                 data: {
                	positiveAnswer:"n",
                	negativeAnswer:"y"
                },
                "media": {
                    "inlineTemplate": "<div>Does the pain in your hip mean you have a serious health problem? </div>"
                }
            }
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
                "inherit": {
                    "set": "yesno"
                }
            },
            {
                "data": {
                    "negativeKey": "g",
                    "negativeWord": "hi[ ]h",
                    "positiveKey": "o",
                    "positiveWord": "l[ ]w",
                    "statement": " At your routine doctor\u00e2\u20ac\u2122s appointment, you are asked to take a test for a disease. After the test, you wait for your results in the waiting room. You think the chances that you have the disease are "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                'handle': 'counter',
                customize: function(){
                    this.media = scorer.count + ' of 50';
                },
                 css:{fontSize:'20px'},
                 location:{top:90}
            },
            {
                "handle": "question",
                 data: {
                	positiveAnswer:"n",
                	negativeAnswer:"y"
                },
                "media": {
                    "inlineTemplate": "<div>Do you think the test results will say you have the disease? </div>"
                }
            }
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
                "inherit": {
                    "set": "yesno"
                }
            },
            {
                "data": {
                    "negativeKey": "t",
                    "negativeWord": "infec[ ]ed",
                    "positiveKey": "l",
                    "positiveWord": "hea[ ]ing",
                    "statement": " You accidentally cut your finger a bit while cooking. You promptly clean the cut, and put on a bandage. Later, the cut hurts a little bit, and you think it is likely "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                'handle': 'counter',
                customize: function(){
                    this.media = scorer.count + ' of 50';
                },
                 css:{fontSize:'20px'},
                 location:{top:90}
            },
            {
                "handle": "question",
                 data: {
                	positiveAnswer:"n",
                	negativeAnswer:"y"
                },
                "media": {
                    "inlineTemplate": "<div>Is your finger seriously infected? <div>"
                }
            }
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
                "inherit": {
                    "set": "yesno"
                }
            },
            {
                "data": {
                    "negativeKey": "n",
                    "negativeWord": "u[ ]mangeable",
                    "positiveKey": "g",
                    "positiveWord": "mana[ ]eable",
                    "statement": " While out hiking you are bitten by a spider. Your friend assures you that the spider was not poisonous and that you will be fine. You worry that it could have been a deadly spider, but continue your hike anyway, and your worries for the rest of the hike are "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                'handle': 'counter',
                customize: function(){
                    this.media = scorer.count + ' of 50';
                },
                 css:{fontSize:'20px'},
                 location:{top:90}
            },
            {
                "handle": "question",
                 data: {
                	positiveAnswer:"y",
                	negativeAnswer:"n"
                },
                "media": {
                    "inlineTemplate": "<div>Are you able to manage your worries while finishing your hike? </div>"
                }
            }
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
                "inherit": {
                    "set": "yesno"
                }
            },
            {
                "data": {
                    "negativeKey": "l",
                    "negativeWord": "unhea[ ]thy",
                    "positiveKey": "t",
                    "positiveWord": "heal[ ]hy",
                    "statement": " You have a dentist appointment, and find out that you need some tests to check the health of your gums and teeth. After the dentist performs the tests, you wait for your results. You think your teeth and gums are probably "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                'handle': 'counter',
                customize: function(){
                    this.media = scorer.count + ' of 50';
                },
                 css:{fontSize:'20px'},
                 location:{top:90}
            },
            {
                "handle": "question",
                 data: {
                	positiveAnswer:"n",
                	negativeAnswer:"y"
                },
                "media": {
                    "inlineTemplate": "<div>Do you think you have gum disease? </div>"
                }
            }
        ]
    }
]



        }

    ]);
    return API.script;
});
/* don't forget to close the define wrapper */