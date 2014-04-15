/* The script wrapper */
define(['app/API'], function(API) {


    // ### Port of Shari's Original Interpretation training 
    
    // ### Settings
    /*
      Settings
      ***********************************************************
      */
    
	// set the canvas size
	API.addSettings('canvas',{
		maxWidth: 800,
		proportions : 0.8
	});

	// setting the base urls for images and templates
	API.addSettings('base_url',{
		image : '../studies/INT_TRAIN/images',
		template : '../studies/INT_TRAIN/'
	});

	// setting the way the logger works (how often we send data to the server and the url for the data)
	API.addSettings('logger',{
		pulse: 1,
		url : '/data'
	});

    API.addStimulusSets({
        // This Default stimulus is inherited by the other stimuli so that we can have a consistent appearance and change it from one place.
        Default: [
            {css:{color:'#0000FF','font-size':'2em'}}
        ],

        // This sets the appearance for the instructions.
        Instructions: [
            {css:{'font-size':'1.3em',color:'white', lineHeight:1.2},handle:'instructions'}
        ],

        // #### The trial stimuli
        // Each of the following stimulus set holds the stimuli for a specific trial state
        // Each set hold stimuli both stimuli that display attribute1/category1 and stimuli that display attribute2/category2.
        paragraph : [
            {data:{letter:'f', handle:'fill_in_letter'}, inherit:'Default', media: {html:"<div>You are at a class that your company has sent you to. Your teacher asks each member of the group to stand up and introduce themselves. After your brief presentation, you guess the others thought you sounded <span class='incomplete' style='white-space:nowrap'>con[ ]ident.</span></div>"}},
            {data:{letter:'y', handle:'fill_in_letter'}, inherit:'Default', media: {html:"<div>You are at a class that your company has sent you to. Your teacher asks each member of the group to stand up and introduce themselves. After your brief presentation, you guess the others thought you sounded <span class='incomplete' style='white-space:nowrap'>sh[ ].</span></div>"}},
            {data:{letter:'s', handle:'fill_in_letter'}, inherit:'Default', media: {html:"<div>A friend suggests that you join an evening class on creative writing. The thought of other people looking at your writing makes you feel <span class='incomplete' style='white-space:nowrap'>enthu[ ]iastic.</span></div>"}},
            {data:{letter:'a', handle:'fill_in_letter'}, inherit:'Default', media: {html:"<div>A friend suggests that you join an evening class on creative writing. The thought of other people looking at your writing makes you feel <span class='incomplete' style='white-space:nowrap'>embarr[ ]ssed.</div>"}},
            {data:{letter:'i', handle:'fill_in_letter'}, inherit:'Default', media: {html:"<div>You join a tennis club and before long, you are asked to play in a doubles match. Afterwards you discuss your performance with your partner. Your partner thinks that you played <span class='incomplete' style='white-space:nowrap'>br[ ]lliantly.</span></div>"}},
            {data:{letter:'b', handle:'fill_in_letter'}, inherit:'Default', media: {html:"<div>You join a tennis club and before long, you are asked to play in a doubles match. Afterwards you discuss your performance with your partner. Your partner thinks that you played <span class='incomplete' style='white-space:nowrap'>terri[ ]ly.</span></div>"}},
            {data:{letter:'m', handle:'fill_in_letter'}, inherit:'Default', media: {html:"<div>Your orchestra asks you to play a solo at the next concert. You practice a few times until you feel ready to play it with the orchestra. At the first rehearsal you make one mistake. The conductor will think that your work is <span class='incomplete' style='white-space:nowrap'>pro[ ]ising.</span></div>"}},
            {data:{letter:'u', handle:'fill_in_letter'}, inherit:'Default', media: {html:"<div>Your orchestra asks you to play a solo at the next concert. You practice a few times until you feel ready to play it with the orchestra. At the first rehearsal you make one mistake. The conductor will think that your work is <span class='incomplete' style='white-space:nowrap'>r[ ]shed.</span></div>"}},
            {data:{letter:'e', handle:'fill_in_letter'}, inherit:'Default', media: {html:"<div>Your partner asks you to go to an anniversary dinner that his/her company is holding. You have not met any of his/her work colleagues before. Getting ready to go, you think that the new people you will meet will find you <span class='incomplete' style='white-space:nowrap'>fri[ ]ndly.</div>"}},
            {data:{letter:'r', handle:'fill_in_letter'}, inherit:'Default', media: {html:"<div>Your partner asks you to go to an anniversary dinner that his/her company is holding. You have not met any of his/her work colleagues before. Getting ready to go, you think that the new people you will meet will find you <span class='incomplete' style='white-space:nowrap'>bo[ ]ing.</div>"}},
            {data:{letter:'a', handle:'fill_in_letter'}, inherit:'Default', media: {html:"<div>You receive an essay back from your teacher and did not get the grade that you were expecting. She tells you that this is because on this occasion, your work was <span class='incomplete' style='white-space:nowrap'>outst[ ]nding.</span></div>"}},
            {data:{letter:'n', handle:'fill_in_letter'}, inherit:'Default', media: {html:"<div>You receive an essay back from your teacher and did not get the grade that you were expecting. She tells you that this is because on this occasion, your work was <span class='incomplete' style='white-space:nowrap'>confusi[ ]g</span></div>"}}
        ],

        // This stimulus is used for giving feedback, in this case only an error notification
        feedback : [
            {handle:'error', location: {top: 80}, css:{color:'red','font-size':'4em'}, media: {word:'X'}, nolog:true}
        ]

    });

    // #### Default trial
    // This trial serves as the default for all IAT trials (excluding instructions)
    API.addTrialSets('Default',{
        data: {score:0},

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
            {handle:'z',on:'keypressed', key:"z"}
        ],
        stimuli: [
            {inherit:{type:'random',set:'paragraph'}},
            {inherit:{type:'random',set:'feedback'}}
        ],
        interactions: [
            // This is an interaction (it has a condition and an action)
            {
                conditions: [{type:'begin'}],
                actions: [
                    {type:'showStim',handle:'fill_in_letter'}
                ]
            },
            {
                conditions: [
                    {type:'inputEqualsStim', property:'letter', handle:'fill_in_letter'}
                ],
                actions: [
                    {type:'log'},
                    {type:'custom',fn:function(options,eventData){
                        var span = $("span.incomplete");
                        var text = span.text().replace(' ', eventData["handle"]);
                        span.text(text);
                    }},
                    {type:'setInput',input:{handle:'end', on:'timeout',duration:500}}
                ]
            },
            {
                conditions: [
                    {type:'inputEqualsStim', property:'letter', handle:'fill_in_letter',negate:true}
                ],
                actions: [
                    {type:'showStim',handle:'error'},
                    {type:'setInput',input:{handle:'clear', on:'timeout',duration:500}}
                    {type:'custom',fn:function(options,eventData){
                        console.log(eventData);
                        console.log(options);
                    }},
                    {type:'setTrialAttr', setter:{score:1}},
                    {type:'setInput',input:{handle:'clear', on:'timeout',duration:500}}
                ]
            },
            // This interaction is triggered by a timout after a INcorrect response.
            // It allows us to delay the removal of the big red X.
            {
                // Trigger when input handle is "end".
                conditions: [
                     {type:'inputEquals',value:'clear'}],
                actions: [
                    {type:'removeInput',handle : 'clear'},
                    {type:'hideStim',handle:'error'}
                ]
            },
            // This interaction is triggered by a timout after a correct response.
            // It allows us to pad each trial with an interval.
            {
                // Trigger when input handle is "end".
                conditions: [{type:'inputEquals',value:'end'}],
                actions: [
                    {type:'removeInput',handle : 'end'},
                    {type:'endTrial'}
                ]
            }


        ]
    });

        // #### Create trial sequence
	API.addSequence([
		{

		    data: {
			myProperty: 'information',
			myOtherProperty: 'more information'
		    },
		    input: [
				{handle:'space',on:'space'}
			],
			layout: [
				// This is a stimulus object
				{
				    media :{template:'inst1.jst'},
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
            mixer:'repeat',
            times:10,
            data: [
            {
                inherit:'Default',
                stimuli: [{inherit:{type:'exRandom',set:'paragraph'}},
                    {inherit:{type:'random',set:'feedback'}}
                ]
            }]
        }
	]);




	// #### Activate the player
	API.play();
});
/* don't forget to close the define wrapper */