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
		}
	]);

	// #### Activate the player
	API.play();
});
/* don't forget to close the define wrapper */