/* The script wrapper */
define(['app/API'], function(API) {

    API.addSettings('canvas',{
        textSize: 5
    });
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
            {handle:'error',media:'X', css:{fontSize:'2em',color:'#FF0000'}, location:{top:70}, nolog:true}
        ],
        yesno: [
            {handle:'yesno',media:'Type "y" for Yes, and "n" for No.', css:{fontSize:'1em',color:'#999999'}, location:{top:70}}
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
            // Listen for a correct response to a question
            {
                conditions: [{type:'inputEqualsStim', property:'positiveAnswer'},
                    {type:'trialEquals', property:'positive', value:true},
                    {type:'globalEquals', property:'askingQuestion', value:true}
                ],
                actions: [
                    {type:'hideStim',handle : 'question'},
                    {type:'hideStim',handle:'yesno'},
                    {type:'trigger',handle : 'answered', duration:500}
                ]
            },
            // Listen for an incorrect response to a question
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
                    {type:'setTrialAttr',setter:{correctOnQuestion:"false"}},
                    {type:'showStim',handle:'error'},
                    {type:'setInput',input:{handle:'clear', on:'timeout',duration:500}}
                ]
            },
            {
                // Trigger when the correct response is provided, as there are two interactions
                // that can cause this, I've separated it out into it's own section rather than
                // duplicate the code.
                conditions: [{type:'inputEquals',value:'answered'}],
                actions: [
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


    API.addTrialSets('all',[
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
                    media :"In this task, you will read about a series of situations. Please read each paragraph carefully, and imagine yourself in the situations described. Each situation has a title. Please pay attention to the title, these titles will be referenced in the next task. At the end of each paragraph, there will be a word fragment (an incomplete word) for you to complete. To complete the word fragment, move the cursor to where a letter is missing, press the keys that correspond to the missing letters, and then click 'Next' twice. Once you type in the correct letter, you will move on to the next question. (If you are really stuck, you can type an incorrect letter three times in a row and you will also move to the next question.) After completing the word fragment, you will be asked to answer a question about the situation in which you imagined yourself. Please use only the information from the situation to answer the question. Once you have the correct answer, you will move on to the next situation. If you are unsure about an item, please make your best guess. For example, if you saw the following scenario: 'You are waiting in line with your friend to buy food for lunch. Your stomach growls. You plan to order pasta with alfredo sa_ce,' you would fill in the 'u' that is missing from 'sauce.' After reading each situation, you will be asked a question about the situation. After you have read about all the situations, you will be asked more questions about them. Please press the spacebar to continue.",
                    css:{fontSize:'.8em',color:'#D7685A'}
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
            data: [
        {
        "inherit": {
            "set": "all",
            "type": "random"
        },
        "stimuli": [
            {
                "inherit": {
                    "set": "error"
                }
            },
            {
                "inherit": {
                    "set": "yesno"
                }
            },
            {
                "data": {
                    "positiveKey": "i",
                    "positiveWord": "acc[ ]dent",
                    "statement": " THE YELLOW LIGHT: You are in your car, on your way to see a friend for lunch. Because you are running late, you are not as careful as you usually are and speed through a yellow light. As you pass through the intersection, you think about the likelihood of causing an . "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord%></span></div>"
                }
            },
            {
                "handle": "question",
                data: {
                	positiveAnswer:"n"
                },
                "media": {
                    "inlineTemplate": "<div>As you passed through the intersection, were you thinking about your friend?</div>"
                }
            }
        ]
    },
// NEXT
        {
        "inherit": {
            "set": "all",
            "type": "random"
        },
        "stimuli": [
            {
                "inherit": {
                    "set": "error"
                }
            },
            {
                "inherit": {
                    "set": "yesno"
                }
            },
            {
                "data": {
                    "positiveKey": "o",
                    "positiveWord": "[ ]n",
                    "statement": "  THE COFFEE POT: You are running late on your way to work. In the car, you realize that you forgot to check if you turned off the coffee pot. When you get to work, you think about what would happen if you did leave the coffee pot . "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord%></span></div>"
                }
            },
            {
                "handle": "question",
                data: {
                	positiveAnswer:"y"
                },
                "media": {
                    "inlineTemplate": "<div>Are you thinking about the coffee pot when you arrive to work?</div>"
                }
            }
        ]
    },
// NEXT
        {
        "inherit": {
            "set": "all",
            "type": "random"
        },
        "stimuli": [
            {
                "inherit": {
                    "set": "error"
                }
            },
            {
                "inherit": {
                    "set": "yesno"
                }
            },
            {
                "data": {
                    "positiveKey": "a",
                    "positiveWord": "s[ ]fety",
                    "statement": " THE ELEVATOR: You are in the lobby of your friend’s new apartment building and press the button to the elevator to go up. The building looks old, and as you get on the elevator, your think about its . "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord%></span></div>"
                }
            },
            {
                "handle": "question",
                data: {
                	positiveAnswer:"y"
                },
                "media": {
                    "inlineTemplate": "<div>Did you think about the elevator’s safety?</div>"
                }
            }
        ]
    },
// NEXT
        {
        "inherit": {
            "set": "all",
            "type": "random"
        },
        "stimuli": [
            {
                "inherit": {
                    "set": "error"
                }
            },
            {
                "inherit": {
                    "set": "yesno"
                }
            },
            {
                "data": {
                    "positiveKey": "i",
                    "positiveWord": "c[ ]ty",
                    "statement": " THE FIELD TRIP: Your young daughter is going on a class field trip to the city. You think about how sometimes she is not careful when crossing the street. As you think about your daughter, you hope the teachers keep an eye on her as they walk around the . "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord%></span></div>"
                }
            },
            {
                "handle": "question",
                data: {
                	positiveAnswer:"y"
                },
                "media": {
                    "inlineTemplate": "<div>Is your daughter young?</div>"
                }
            }
        ]
    },
// NEXT
        {
        "inherit": {
            "set": "all",
            "type": "random"
        },
        "stimuli": [
            {
                "inherit": {
                    "set": "error"
                }
            },
            {
                "inherit": {
                    "set": "yesno"
                }
            },
            {
                "data": {
                    "positiveKey": "x",
                    "positiveWord": "an[ ]ious",
                    "statement": " THE FLIGHT: You are on a long flight with your partner going to an exotic location for your vacation. The airplane pilot gets on the intercom, and says there is going to be some turbulence. As you wait for the turbulence, you feel a little . "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord%></span></div>"
                }
            },
            {
                "handle": "question",
                data: {
                	positiveAnswer:"y"
                },
                "media": {
                    "inlineTemplate": "<div>Are you going on vacation?</div>"
                }
            }
        ]
    },
// NEXT
        {
        "inherit": {
            "set": "all",
            "type": "random"
        },
        "stimuli": [
            {
                "inherit": {
                    "set": "error"
                }
            },
            {
                "inherit": {
                    "set": "yesno"
                }
            },
            {
                "data": {
                    "positiveKey": "a",
                    "positiveWord": "fin[ ]nces",
                    "statement": " THE JOB: You are currently working as a contractor for a company. Once this job is finished, you will be without employment until you can find your next job. You think about not having an income for a few weeks and about your future . "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord%></span></div>"
                }
            },
            {
                "handle": "question",
                data: {
                	positiveAnswer:"y"
                },
                "media": {
                    "inlineTemplate": "<div>Will you be without an income soon?</div>"
                }
            }
        ]
    },
// NEXT
        {
        "inherit": {
            "set": "all",
            "type": "random"
        },
        "stimuli": [
            {
                "inherit": {
                    "set": "error"
                }
            },
            {
                "inherit": {
                    "set": "yesno"
                }
            },
            {
                "data": {
                    "positiveKey": "t",
                    "positiveWord": "downs[ ]airs",
                    "statement": "  THE LOUD NOISE: You are woken up in the middle of the night by a loud noise. You are not sure what caused the noise and leave your bedroom to see what happened. You walk . "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord%></span></div>"
                }
            },
            {
                "handle": "question",
                data: {
                	positiveAnswer:"y"
                },
                "media": {
                    "inlineTemplate": "<div>Have you been woken up in the middle of the night?</div>"
                }
            }
        ]
    },
// NEXT
        {
        "inherit": {
            "set": "all",
            "type": "random"
        },
        "stimuli": [
            {
                "inherit": {
                    "set": "error"
                }
            },
            {
                "inherit": {
                    "set": "yesno"
                }
            },
            {
                "data": {
                    "positiveKey": "e",
                    "positiveWord": "all[ ]rgic",
                    "statement": " THE RESTAURANT: You are at a restaurant with a group of friends for dinner. Everyone at the table shares an appetizer. After you eat the appetizer, you remember that you did not ask the waiter if the food is cooked in peanut oil, to which you are . "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord%></span></div>"
                }
            },
            {
                "handle": "question",
                data: {
                	positiveAnswer:"n"
                },
                "media": {
                    "inlineTemplate": "<div>Are you at dinner with your family?</div>"
                }
            }
        ]
    }
   ]
   }

]);

    // #### Activate the player
    // API.play();
});
/* don't forget to close the define wrapper */