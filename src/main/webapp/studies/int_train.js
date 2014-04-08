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
		pulse: 20,
		url : '/implicit/PiPlayerApplet'
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
            {data:{letter:'f', handle:'fill_in_letter'}, inherit:'Default', media: {html:"<div>You are at a class that your company has sent you to. Your teacher asks each member of the group to stand up and introduce themselves. After your brief presentation, you guess the others thought you sounded <span style='white-space:nowrap'>con[ ]ident.</span></div>"}},
            {data:{letter:'y', handle:'fill_in_letter'}, inherit:'Default', media: {html:"<div>You are at a class that your company has sent you to. Your teacher asks each member of the group to stand up and introduce themselves. After your brief presentation, you guess the others thought you sounded <span style='white-space:nowrap'>sh[ ].</span></div>"}},
            {data:{letter:'s', handle:'fill_in_letter'}, inherit:'Default', media: {html:"<div>A friend suggests that you join an evening class on creative writing. The thought of other people looking at your writing makes you feel <span style='white-space:nowrap'>enthu[ ]iastic.</span></div>"}},
            {data:{letter:'a', handle:'fill_in_letter'}, inherit:'Default', media: {html:"<div>A friend suggests that you join an evening class on creative writing. The thought of other people looking at your writing makes you feel <span style='white-space:nowrap'>embarr[ ]ssed.</div>"}}
        ],

        // This stimulus is used for giving feedback, in this case only an error notification
        feedback : [
            {handle:'error', location: {top: 80}, css:{color:'red','font-size':'4em'}, media: {word:'X'}, nolog:true}
        ]

    });

    // #### Default trial
    // This trial serves as the default for all IAT trials (excluding instructions)
    API.addTrialSets('Default',{
        input: [
            {handle:'f',on:'keypressed', key:"f"},
            {handle:'y',on:'keypressed', key:"y"},
            {handle:'s',on:'keypressed', key:"s"},
            {handle:'a',on:'keypressed', key:"a"},
            {handle:'all_letters',on:'keypressed',key:['a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z']}
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
                    {type:'inputEqualsStim', handle:'letter'}
                ],
                actions: [
                    {type:'removeInput',handle:'All'},
                    {type:'hideStim', handle: 'fill_in_letter'}
                ]
            },
            {
                conditions: [
                    {type:'inputEquals',value:'all_letters'},
                    {type:'inputEqualsStim', handle:'letter',negate:true}
                ],
                actions: [
                    {type:'showStim',handle:'error'},
                    {type:'setTrialAttr', setter:{score:1}},
                    {type:'setInput',input:{handle:'clear', on:'timeout',duration:250}}
                ]
            },
            // ##### end after ITI
            // This interaction is triggered by a timout after a correct response.
            // It allows us to pad each trial with an interval.
            {
                // Trigger when input handle is "end".
                conditions: [{type:'inputEquals',value:'clear'}],
                actions: [
                    {type:'hideStim',handle:'error'}
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
                stimuli: [{inherit:{type:'random',set:'paragraph'}},
                    {inherit:{type:'random',set:'feedback'}}
                ]
            }]
        }
	]);




	// #### Activate the player
	API.play();
});
/* don't forget to close the define wrapper */