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

                return {
                    log_serial : logStack.length,
                    trial_id: this._id,
                    name: this.name(),
                    responseHandle: inputData.handle,
                    latency: Math.floor(inputData.latency),
                    stimuli: stimList,
                    media: mediaList,
                    data: trialData

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
                    {type:'setGlobalAttr',setter:{askingQuestion:false}}
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
                    {type:'showStim',handle:'error'},
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
                    {type:'showStim',handle:'error'},
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
                        trialData.latency   = eventData.latency;
                    }},
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
                    {type:'setTrialAttr',setter:{correctOnQuestion:"true"}},
                    {type:'hideStim',handle : 'question'},
                    {type:'hideStim',handle:'yesno'},
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
                    {type:'setTrialAttr',setter:{correctOnQuestion:"true"}},
                    {type:'hideStim',handle : 'question'},
                    {type:'hideStim',handle:'yesno'},
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
                    {type:'setTrialAttr',setter:{correctOnQuestion:"false"}},
                    {type:'showStim',handle:'error'},
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
                    { inherit:'base', data: {positive:true}},
                    { inherit:'base', data: {positive:true}},
                    { inherit:'base', data: {positive:false}}
                            ]);

    API.addSequence([
        {
            input: [
                {handle:'space',on:'space'}
            ],
            layout: [
                // This is a stimulus object
                {
                    media :"These are your instructions.",
                    css:{fontSize:'1.2em',color:'#D7685A'}
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
            mixer: 'repeat',
            times: 1,  // The total number of randomly selected trials to run.
            data: [
                {
                    inherit:{set:'posneg', type:'random'},
                    stimuli: [
                        {inherit:{set:'error'}},
                        {inherit:{set:'yesno'}},
                        {
                            handle: "paragraph",
                            data: {
                               positiveKey:'f',
                               negativeKey:'y',
                               positiveWord:'con[ ]ident',
                               negativeWord:'sh[ ]',
                               statement:"You are at a class that your company has sent you to. Your teacher asks each member of the group to stand up and introduce themselves. After your brief presentation, you guess the others thought you sounded "
                                },
                            media:{inlineTemplate:"<div><%= stimulusData.statement %>" + "" +
                                "<span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"}
                         },
                        {
                            handle:"question",
                            data: {
                              positiveAnswer:"n",
                              negativeAnswer:"y"
                            },
                            media:{inlineTemplate:"<div>Did you feel dissatisfied with your speech?</div>"}
                        }
                    ]
                },
                {
                    inherit:{set:'posneg', type:'random'},
                    stimuli: [
                        {inherit:{set:'error'}},
                        {inherit:{set:'yesno'}},
                        {
                            handle: "paragraph",
                            data: {
                                positiveWord:'enthu[ ]iastic',
                                positiveKey:'s',
                                negativeWord:'embarr[ ]ssed',
                                negativeKey:'a',
                                statement:"A friend suggests that you join an evening class on creative writing. The thought of other people looking at your writing makes you feel ",
                            },
                            media:{inlineTemplate:"<div><%= stimulusData.statement %>" + "" +
                                "<span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"}
                        },
                        {
                            handle:"question",
                            data: {
                                positiveAnswer:"y",
                                negativeAnswer:"n"
                            },
                            media:{inlineTemplate:"<div>Would you expect to feel comfortable if others look at your work?</div>"}
                        }
                    ]
                }
             ]
        }

    ]);

    // #### Activate the player
    API.play();
});
/* don't forget to close the define wrapper */