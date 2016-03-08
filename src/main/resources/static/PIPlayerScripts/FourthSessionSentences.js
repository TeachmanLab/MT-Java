define([], function() {
    return({
        display_length: 40,
        add_extra_missing_letter:false,
        sequence:[
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
            n: 40,  // The total number of randomly selected trials to run.
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
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
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
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
        ]
    },
    ]
        },
        {"inherit": {"set": "vivid"}}, { "inherit": { "set": "vivid_after" } },
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
                    "negativeWord": "bo[ ]ing",
                    "positiveKey": "s",
                    "positiveWord": "intere[ ]ting",
                    "statement": " You are required to give a presentation to a group of work colleagues that you know well. They are all quiet during your presentation. As you think about the presentation later that day, you think that your colleagues found your presentation "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
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
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
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
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "handle": "question",
                 data: {
                	positiveAnswer:"y",
                	negativeAnswer:"y"
                },
                "media": {
                    "inlineTemplate": "<div>Are you at a hotel? </div>"
                     }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
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
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
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
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
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
                    "statement": " An old acquaintance just moved into your city and asks you out to coffee. You are nervous about seeing him again after many years and could easily tell him that you are too busy. When he calls you about meeting, you "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
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
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
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
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
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
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
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
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
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
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
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
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "handle": "question",
                 data: {
                	positiveAnswer:"n",
                	negativeAnswer:"n"
                },
                "media": {
                    "inlineTemplate": "<div>Are you speaking on TV? </div>"
                     }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
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
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
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
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
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
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
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
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
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
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
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
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
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
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
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
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
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
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
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
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
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
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
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
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
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
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
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
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
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
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
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
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
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
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "handle": "question",
                 data: {
                	positiveAnswer:"y",
                	negativeAnswer:"y"
                },
                "media": {
                    "inlineTemplate": "<div>Are you at a coffee shop? </div>"
                     }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
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
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
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
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
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
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
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
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
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
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
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
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
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
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
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
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
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
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
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
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
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
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
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
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
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
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
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
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
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
                    "negativeWord": "dang[ ]rous",
                    "positiveKey": "u",
                    "positiveWord": "f[ ]n",
                    "statement": " You are on holiday at a mountain resort and are learning how to ski. This is your first attempt to ski downhill on your own. At a moderate speed you approach the first bend and think that skiing is "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
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
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
        ]
    },
    ]
        },
        {"inherit": {"set": "vivid"}}, { "inherit": { "set": "vivid_after" } },
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
                    "negativeWord": "into[ ]erable",
                    "positiveKey": "n",
                    "positiveWord": "ma[ ]ageable",
                    "statement": " You just finished a hard exam and you do not know if you did well or not. You feel nervous as you think about what grade you will get. You think being nervous is "
                },
                "handle": "paragraph",
                "media": {
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
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
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
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
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
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
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
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
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
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
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
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
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
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
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
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
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
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
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
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
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
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
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
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
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
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
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
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
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
                }
            },
            {
                "handle": "question",
                 data: {
                	positiveAnswer:"n",
                	negativeAnswer:"y"
                },
                "media": {
                    "inlineTemplate": "<div>Are you likely to get hurt while on the trampoline? </div>"
                     }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
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
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
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
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
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
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
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
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
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
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
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
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
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
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
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
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
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
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
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
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
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
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
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
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
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
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
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
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
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
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
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
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
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
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
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
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
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
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
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
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
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
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
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
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
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
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
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
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
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
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
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
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
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
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
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
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
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
                    "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
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
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}

        ]
    }
]



        },
        {
            "inherit": {"set": "vivid"},
            layout: [{media : {template:"/PIPlayerScripts/vividness_last.html"}}]
        }
        ]});
    // #### Activate the player
    //API.play();
});
/* don't forget to close the define wrapper */