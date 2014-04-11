/* The script wrapper */
define(['app/API'], function(API) {

    API.addSettings('canvas',{
        textSize: 5
    });

    API.addStimulusSets('error',[
        {handle:'error',media:'X', css:{fontSize:'2em',color:'#FF0000'}, location:{top:70}}
    ]);

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
            // This is an interaction (it has a condition and an action)
            {
                conditions: [{type:'begin'}],
                actions: [
                    {type:'showStim',handle:'All'}
                ]
            },
            {
                conditions: [{type:'inputEquals',value:'space'}],
                actions: [
                        {type:'endTrial'},
                    {type:'custom',fn:function(options,eventData){
                        console.log(eventData);
                        console.log(options);
                    }},
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
                    { inherit:'base', data: {positive:'true'}},
                    { inherit:'base', data: {positive:'true'}},
                    { inherit:'base', data: {positive:'true'}},
                    { inherit:'base', data: {positive:'false'}}
                            ]);

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
            times: 10,
            data: [
                {
                    inherit:{set:'posneg', type:'random'},
                    stimuli: [
                        {
                            data: {
                               positiveKey:'f',
                               negativeKey:'y',
                               positiveWord:'con[ ]ident',
                               negativeWord:'sh[ ]',
                               statement:"You are at a class that your company has sent you to. Your teacher asks each member of the group to stand up and introduce themselves. After your brief presentation, you guess the others thought you sounded "
                                },
                            media:{inlineTemplate:"<span><%= trialData.positive %></span>"}
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