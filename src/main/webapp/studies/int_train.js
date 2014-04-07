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

            input: [
                {handle:'f',on:'keypressed', key:"f"},
                {handle:'all_letters',on:'keypressed',key:['a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z']},
            ],
            layout: [
                // This is a stimulus object
                {
                    media: {html:"<div>You are at a class that your company has sent you to. Your teacher asks each member of the group to stand up and introduce themselves. After your brief presentation, you guess the others thought you sounded <span style='white-space:nowrap'>con[ ]ident.</span></div>"},
                    css:{fontSize:'1.2em',color:'#D7685A'}
                }
            ],
            stimuli: [
                {handle:'error', location: {top: 80}, css:{color:'red','font-size':'4em'}, media: {word:'X'}, nolog:true}
            ],
            interactions: [
                // This is an interaction (it has a condition and an action)
                {
                    conditions: [
                        {type:'inputEquals',value:'f'}
                    ],
                    actions: [
                        {type:'endTrial'}
                    ]
                },
                {
                    conditions: [
                        {type:'inputEquals',value:'all_letters'},
                        {type:'inputEqualsStim', property:'letter',negate:true}
                    ],
                    actions: [
                        {type:'showStim',handle:'error'},
                        {type:'setTrialAttr', setter:{score:1}}
                    ]
                }
            ]
        }

	]);




	// #### Activate the player
	API.play();
});
/* don't forget to close the define wrapper */