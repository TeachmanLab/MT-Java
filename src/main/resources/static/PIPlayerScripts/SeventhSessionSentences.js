define([], function() {
    return({
        display_length: 50,
        add_extra_missing_letter:true,
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
                    "negativeKey": "d",
                    "negativeWord": "ru[ ]e",
                    "positiveKey": "s",
                    "positiveWord": "conscientiou[ ]",
                    "statement": " You are taking a class and during a brief break, call home to speak to your partner. You are late getting back into the classroom. The other students will think that choosing now to call home must mean that you are "
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
                    "inlineTemplate": "<div>Did people think you were rude when you were late returning to class? </div>"
                     }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
        ]
    },
    ]
    },
                { "inherit": { "set": "vivid" } },{ "inherit": { "set": "vivid_after" } },
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
                    "negativeKey": "v",
                    "negativeWord": "lea[ ]e",
                    "positiveKey": "a",
                    "positiveWord": "st[ ]y",
                    "statement": " You are having a discussion with your teacher in his office, which is a little anxiety provoking for you. You think about ending the discussion early. You decide to "
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
                    "inlineTemplate": "<div>Are you able to continue the discussion with your teacher? </div>"
                     }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
        ]
    },
    ]
        },
        { "inherit": { "set": "vivid" } },{ "inherit": { "set": "vivid_after" } },
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
                    "negativeKey": "i",
                    "negativeWord": "bor[ ]ng",
                    "positiveKey": "l",
                    "positiveWord": "sty[ ]ish",
                    "statement": " One evening your colleagues decide to go out to eat and ask you to join them. You accept, but will have to wear your work clothes. When you arrive, the other diners turn to look at you. You think this is because they think you look "
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
                    "inlineTemplate": "<div>Do you think that your colleagues think you are dressed inappropriately? </div>"
                     }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
        ]
    },           {
        "inherit": {
            "set": "posneg",
            "type": "random"
        },
        "stimuli": [
            {"inherit": {"set": "error"}},

            {
                "data": {
                    "negativeKey": "b",
                    "negativeWord": "terri[ ]le",
                    "positiveKey": "e",
                    "positiveWord": "excell[ ]nt",
                    "statement": " The morning of your first evaluation with your new boss, she tells you that she has strong views about your work. You think that she likely finds your work "
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
                    "inlineTemplate": "<div>Do you think your new boss has an unfavorable opinion of your work? </div>"
                     }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
        ]
    },           {
        "inherit": {
            "set": "posneg",
            "type": "random"
        },
        "stimuli": [
            {"inherit": {"set": "error"}},

            {
                "data": {
                    "negativeKey": "w",
                    "negativeWord": "awk[ ]ard",
                    "positiveKey": "n",
                    "positiveWord": "pleasa[ ]t",
                    "statement": " You best friend arranges a blind date for you. As you sit in the bar waiting to meet your date for the first time, you wonder how it will go. You feel that your date will think you are "
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
                    "inlineTemplate": "<div>Do you think you will make a bad impression on your date? </div>"
                     }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
        ]
    },           {
        "inherit": {
            "set": "posneg",
            "type": "random"
        },
        "stimuli": [
            {"inherit": {"set": "error"}},

            {
                "data": {
                    "negativeKey": "t",
                    "negativeWord": "nega[ ]ively",
                    "positiveKey": "s",
                    "positiveWord": "po[ ]itively",
                    "statement": " As a course requirement, you have to make a presentation to your classmates and your teacher. You prepare to start and you stumble on your first few words. You think that your performance will be evaluated "
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
                    "inlineTemplate": "<div>Do you think the others will like your presentation? </div>"
                     }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
        ]
    },           {
        "inherit": {
            "set": "posneg",
            "type": "random"
        },
        "stimuli": [
            {"inherit": {"set": "error"}},

            {
                "data": {
                    "negativeKey": "y",
                    "negativeWord": "anno[ ]ing",
                    "positiveKey": "n",
                    "positiveWord": "ki[ ]d",
                    "statement": " You notice an elderly neighbor carrying in bags of grocers from her car to her house. You decide to ask if she needs help, and she turns to stare at you. This is because she probably views you as "
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
                    "inlineTemplate": "<div>Does your neighbor appreciate your offer to help with the groceries? </div>"
                     }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
        ]
    },           {
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
                    "statement": " Your firm is taking part in a recruitment campaign, and you are asked to go along and speak to local teenagers about the work you do. During the presentation, you feel slightly awkward, and after, other colleagues tell you that you spoke "
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
                    "inlineTemplate": "<div>Do your colleagues think your presentation to the teenagers will go well? </div>"
                     }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
        ]
    },           {
        "inherit": {
            "set": "posneg",
            "type": "random"
        },
        "stimuli": [
            {"inherit": {"set": "error"}},

            {
                "data": {
                    "negativeKey": "g",
                    "negativeWord": "ne[ ]ative",
                    "positiveKey": "s",
                    "positiveWord": "po[ ]itive",
                    "statement": " You have been writing to a pen-pal in Belgium for several years and finally arranged for him to come and stay with you. As you stand at the airport waiting for his flight to arrive, you think that his first impression of you will be "
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
                    "inlineTemplate": "<div>Do you think you will make a good impression on your pen-pal at the airport? </div>"
                     }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
        ]
    },           {
        "inherit": {
            "set": "posneg",
            "type": "random"
        },
        "stimuli": [
            {"inherit": {"set": "error"}},

            {
                "data": {
                    "negativeKey": "v",
                    "negativeWord": "lea[ ]e",
                    "positiveKey": "a",
                    "positiveWord": "st[ ]y",
                    "statement": "  Your boss has hired a few interns for the summer. He asks you to stay after work and give them a brief presentation about the company\u2019s goals and values before you go home that evening, but tells you that it is completely optional. You feel a little nervous about the presentation, and you decide to "
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
                    "inlineTemplate": "<div>Do you stay to give the optional presentation? </div>"
                     }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
        ]
    },           {
        "inherit": {
            "set": "posneg",
            "type": "random"
        },
        "stimuli": [
            {"inherit": {"set": "error"}},

            {
                "data": {
                    "negativeKey": "o",
                    "negativeWord": "y[ ]u",
                    "positiveKey": "k",
                    "positiveWord": "jo[ ]es",
                    "statement": " You are outside jogging and see a group of people ahead of you. As you pass them, you stumble slightly and feel a little foolish. You hear them laugh as you pass by, and assume that they are laughing at "
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
                    "inlineTemplate": "<div>Is the group of people laughing at you? </div>"
                     }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
        ]
    },           {
        "inherit": {
            "set": "posneg",
            "type": "random"
        },
        "stimuli": [
            {"inherit": {"set": "error"}},

            {
                "data": {
                    "negativeKey": "j",
                    "negativeWord": "re[ ]ected",
                    "positiveKey": "m",
                    "positiveWord": "welco[ ]ed",
                    "statement": " An opportunity arises for a promotion in your department. You ask for more details of what it will entail. After hearing more about the strict selection criteria for the promotion, you decide that if you applied for the job, you would be "
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
                    "inlineTemplate": "<div>Do you think you have a good chance of getting the promotion? </div>"
                     }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
        ]
    },           {
        "inherit": {
            "set": "posneg",
            "type": "random"
        },
        "stimuli": [
            {"inherit": {"set": "error"}},

            {
                "data": {
                    "negativeKey": "r",
                    "negativeWord": "ter[ ]ible",
                    "positiveKey": "a",
                    "positiveWord": "norm[ ]l",
                    "statement": " You are giving a presentation at work. During one of your slides, you lose your train to thought and have to take a moment to look over your notes. Losing your train of thought while you are presenting is "
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
                    "inlineTemplate": "<div>Is it normal to lose your train of thought while presenting? </div>"
                     }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
        ]
    },           {
        "inherit": {
            "set": "posneg",
            "type": "random"
        },
        "stimuli": [
            {"inherit": {"set": "error"}},

            {
                "data": {
                    "negativeKey": "l",
                    "negativeWord": "sil[ ]y",
                    "positiveKey": "c",
                    "positiveWord": "ni[ ]e",
                    "statement": " You buy a new suit, which is very different from your usual style of clothes. You try it one to show your friend. When she sees you, she pauses, and then comments that it makes you look "
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
                    "inlineTemplate": "<div>Did your friend think your new suit looked foolish? </div>"
                     }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
        ]
    },           {
        "inherit": {
            "set": "posneg",
            "type": "random"
        },
        "stimuli": [
            {"inherit": {"set": "error"}},

            {
                "data": {
                    "negativeKey": "x",
                    "negativeWord": "obno[ ]ious",
                    "positiveKey": "s",
                    "positiveWord": "rea[ ]onable",
                    "statement": " You need to return a jacket. As you speak with a sales associate, the sales associate does not smile very much. She probably thinks you are  "
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
                    "inlineTemplate": "<div>Does the sales associate think you are annoying? </div>"
                     }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
        ]
    },           {
        "inherit": {
            "set": "posneg",
            "type": "random"
        },
        "stimuli": [
            {"inherit": {"set": "error"}},

            {
                "data": {
                    "negativeKey": "a",
                    "negativeWord": "awkw[ ]rd",
                    "positiveKey": "m",
                    "positiveWord": "nor[ ]al",
                    "statement": " You are on a long drive with a friend. You notice neither of you have said anything for a few minutes. Because of the silence, your friend probably thinks you are "
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
                    "inlineTemplate": "<div>Does your friend think you are awkward when there is silence during the drive? </div>"
                     }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
        ]
    },           {
        "inherit": {
            "set": "posneg",
            "type": "random"
        },
        "stimuli": [
            {"inherit": {"set": "error"}},

            {
                "data": {
                    "negativeKey": "v",
                    "negativeWord": "lea[ ]e",
                    "positiveKey": "a",
                    "positiveWord": "st[ ]y",
                    "statement": " You arrive at a party and realize that you don\u2019t know anyone there. No one talks to you immediately and you contemplate leaving. You decide to "
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
                    "inlineTemplate": "<div>Do you leave the party right away? </div>"
                     }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
        ]
    },           {
        "inherit": {
            "set": "posneg",
            "type": "random"
        },
        "stimuli": [
            {"inherit": {"set": "error"}},

            {
                "data": {
                    "negativeKey": "u",
                    "negativeWord": "disastro[ ]s",
                    "positiveKey": "t",
                    "positiveWord": "enter[ ]aining",
                    "statement": " You invite work colleagues to your house for a dinner party, although you know they don't always get along. At the dinner party, your guests talk a lot, despite not always agreeing. As you are clearing up afterwards, you think your guests found your party to be "
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
                    "inlineTemplate": "<div>Did your guests enjoy themselves at your dinner party? </div>"
                     }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
        ]
    },           {
        "inherit": {
            "set": "posneg",
            "type": "random"
        },
        "stimuli": [
            {"inherit": {"set": "error"}},

            {
                "data": {
                    "negativeKey": "r",
                    "negativeWord": "ter[ ]ible",
                    "positiveKey": "e",
                    "positiveWord": "gr[ ]at",
                    "statement": " You write a short story to enter in a competition and need someone to proof read it. You ask your friend to proofread it, and he gives you a few suggestions on how to improve the story. Your friend probably thinks your work is "
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
                    "inlineTemplate": "<div>Does your friend like the story you wrote? </div>"
                     }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
        ]
    },           {
        "inherit": {
            "set": "posneg",
            "type": "random"
        },
        "stimuli": [
            {"inherit": {"set": "error"}},

            {
                "data": {
                    "negativeKey": "z",
                    "negativeWord": "la[ ]y",
                    "positiveKey": "r",
                    "positiveWord": "ca[ ]ing",
                    "statement": " You work in a large office along with your supervisors. One morning, you have to call your mother. Everyone else is working quietly and can hear what you say, and you guess that they think calling your mother from work means you are "
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
                    "inlineTemplate": "<div>Were other judging you negatively because you called your mother from work? </div>"
                     }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
        ]
    },           {
        "inherit": {
            "set": "posneg",
            "type": "random"
        },
        "stimuli": [
            {"inherit": {"set": "error"}},

            {
                "data": {
                    "negativeKey": "g",
                    "negativeWord": "stran[ ]e",
                    "positiveKey": "l",
                    "positiveWord": "friend[ ]y",
                    "statement": " While out on a walk, you see a neighbor that you do not know well. You decide to say hello in passing. He probably thinks that you are "
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
                    "inlineTemplate": "<div>Does your neighbor think that you are friendly? </div>"
                     }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
        ]
    },           {
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
                    "positiveKey": "t",
                    "positiveWord": "dis[ ]racted",
                    "statement": " You are at a party with people you do not know well. You decide to join some people who are sitting on a couch and talking. When you try to enter the conversation topic, the people on the couch continue talking to each other, because they were probably "
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
                    "inlineTemplate": "<div>Did the people at the party find you annoying? </div>"
                     }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
        ]
    },           {
        "inherit": {
            "set": "posneg",
            "type": "random"
        },
        "stimuli": [
            {"inherit": {"set": "error"}},

            {
                "data": {
                    "negativeKey": "r",
                    "negativeWord": "ter[ ]ible",
                    "positiveKey": "a",
                    "positiveWord": "gre[ ]t",
                    "statement": " While at the hairdressers, you opt for a completely different haircut. When you see your friends afterwards, she gasps. Her gasp probably means that she thinks the new style makes you look "
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
                    "inlineTemplate": "<div>Did your friend like your new haircut? </div>"
                     }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
        ]
    },           {
        "inherit": {
            "set": "posneg",
            "type": "random"
        },
        "stimuli": [
            {"inherit": {"set": "error"}},

            {
                "data": {
                    "negativeKey": "n",
                    "negativeWord": "u[ ]helpful",
                    "positiveKey": "p",
                    "positiveWord": "hel[ ]ful",
                    "statement": " You spend an eventing with a friend and end up talking about her relationship problems. As you give her some advice, you notice she is quiet. Once you get home later, you reflect on the evening and you think that she found your advice "
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
                    "inlineTemplate": "<div>Did your friend think you gave her good advice about relationships? </div>"
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
                    "negativeWord": "y[ ]u",
                    "positiveKey": "s",
                    "positiveWord": "them[ ]elves",
                    "statement": " You are giving a presentation during your evening class. As you talk, you hear a few of your classmates laughing in the corner. They were probably laughing at "
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
                    "inlineTemplate": "<div>Were your classmates laughing at you? </div>"
                     }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
        ]
    },
    ]
        },
        { "inherit": { "set": "vivid" } },{ "inherit": { "set": "vivid_after" } },
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
                    "negativeKey": "n",
                    "negativeWord": "i[ ]secure",
                    "positiveKey": "c",
                    "positiveWord": "se[ ]ure",
                    "statement": " Even though you normally excel at your job, you recently had a rough week at work. Specifically, you made a couple of mistakes on an important project. When you think about the future, you think it is likely that your job is probably "
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
                    "inlineTemplate": "<div>Are you probably going to get fired? </div>"
                     }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
        ]
    },           {
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
                    "positiveKey": "l",
                    "positiveWord": "thril[ ]ing",
                    "statement": " You are seeing a movie with your friend and realize that you don\u2019t remember locking your car. You worry about someone breaking in and stealing something. The rest of the movie is "
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
                    "inlineTemplate": "<div>Do you enjoy the rest of the movie after thinking about the car? </div>"
                     }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
        ]
    },           {
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
                    "statement": " You are driving the speed limit, although it is raining. You know that you should probably slow down to be safe, but you remain at the speed limit. The chance of you getting into an accident is "
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
                    "inlineTemplate": "<div>Are you likely to get into an accident? </div>"
                     }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
        ]
    },           {
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
                    "negativeKey": "o",
                    "negativeWord": "br[ ]ke",
                    "positiveKey": "n",
                    "positiveWord": "fi[ ]e",
                    "statement": " Your roof has a leak in it, and water comes into the house every time it rains. You think it is going to be expensive to hire someone to fix it. As you think about paying the bill to have the roof fixed, you feel like you will be "
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
                    "inlineTemplate": "<div> Will you be broke after paying the bill for fixing the roof? </div>"
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
                    "negativeWord": "fi[ ]ed",
                    "positiveKey": "e",
                    "positiveWord": "fin[ ]",
                    "statement": " You recently saw a program on the news about how the economy has been particularly bad recently. Even though your boss typically gives you good reviews, you wonder if your job is safe. You think you will likely be "
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
                    "inlineTemplate": "<div>Is it likely that you will be fired? </div>"
                     }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
        ]
    },           {
        "inherit": {
            "set": "posneg",
            "type": "random"
        },
        "stimuli": [
            {"inherit": {"set": "error"}},

            {
                "data": {
                    "negativeKey": "k",
                    "negativeWord": "ban[ ]rupt",
                    "positiveKey": "n",
                    "positiveWord": "fi[ ]e",
                    "statement": " Your car needs expensive repairs. You have savings, but are reluctant to use them. When you pay for the car repairs, you will be "
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
                    "inlineTemplate": "<div>Will you go bankrupt after paying for the car repairs? </div>"
                     }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
        ]
    },           {
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
                    "positiveKey": "c",
                    "positiveWord": "pea[ ]eful",
                    "statement": " You are headed out for vacation. You leave before double checking that all the appliances in your house are turned off. As you close the door to your house, you feel "
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
                    "inlineTemplate": "<div>Are you feeling calm as you leave for vacation, despite not double checking the appliances? </div>"
                     }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
        ]
    },           {
        "inherit": {
            "set": "posneg",
            "type": "random"
        },
        "stimuli": [
            {"inherit": {"set": "error"}},

            {
                "data": {
                    "negativeKey": "a",
                    "negativeWord": "cr[ ]sh",
                    "positiveKey": "e",
                    "positiveWord": "d[ ]tour",
                    "statement": " You are meeting your parents for dinner. You arrive at the restaurant, and they are not there yet, which is strange because they are typically on time. They are probably late due to a "
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
                    "inlineTemplate": "<div>Did your parents probably get into a car crash? </div>"
                     }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
        ]
    },           {
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
                    "positiveKey": "s",
                    "positiveWord": "pa[ ]s",
                    "statement": " You are almost done with college, and preparing for you last final exam. You heard from other people that the exam is incredibly difficult. You studied a lot and think it is likely you will "
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
                    "inlineTemplate": "<div>Will you probably pass the test? </div>"
                     }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
        ]
    },           {
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
    },           {
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
                    "positiveKey": "i",
                    "positiveWord": "f[ ]ne",
                    "statement": " You are looking for a new apartment. While on your first tour, you fall in love with the place and decide to sign a lease. The fact that you did not check out other apartments makes you feel "
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
                    "inlineTemplate": "<div>Are you anxious that you only saw one apartment? </div>"
                     }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
        ]
    },           {
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
                    "inlineTemplate": "<div>Do you think that you job is safe? </div>"
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
                    "negativeWord": "mi[ ]erable",
                    "positiveKey": "l",
                    "positiveWord": "thril[ ]ing",
                    "statement": " You are seeing a movie with your friend and realize that you don\u2019t remember locking your car. You worry about someone breaking in and stealing something. The rest of the movie is "
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
                    "inlineTemplate": "<div>Do you enjoy the rest of the movie after thinking about the car? </div>"
                     }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
        ]
    },           {
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
                    "inlineTemplate": "<div>Is your grandmother unable to talk because she is ill? </div>"
                     }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
        ]
    },           {
        "inherit": {
            "set": "posneg",
            "type": "random"
        },
        "stimuli": [
            {"inherit": {"set": "error"}},

            {
                "data": {
                    "negativeKey": "k",
                    "negativeWord": "ris[ ]y",
                    "positiveKey": "f",
                    "positiveWord": "sa[ ]e",
                    "statement": " You are looking after a friend\u2019s child. The child you are watching asks to go to the park in a different neighborhood. In your opinion, an ordinary outing like this is "
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
                    "inlineTemplate": "<div>Is going to a park in a different neighborhood dangerous? </div>"
                     }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
        ]
    },           {
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
                    "statement": " You are applying to a prestigious internship program. As you prepare your resume, you reflect upon everything you have accomplished thus far. Your record is strong, but not perfect, which makes you feel "
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
    },           {
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
    },           {
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
                    "negativeKey": "g",
                    "negativeWord": "hi[ ]h",
                    "positiveKey": "m",
                    "positiveWord": "mini[ ]al",
                    "statement": " You have a meeting with a co-worker, and notice that he repeatedly sniffles and wipes his nose with a tissue. After the meeting, you wash your hands. The chances that you will get sick from your coworker are "
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
                    "inlineTemplate": "<div>Are you likely to get sick from your coworker? </div>"
                     }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
        ]
    },           {
        "inherit": {
            "set": "posneg",
            "type": "random"
        },
        "stimuli": [
            {"inherit": {"set": "error"}},

            {
                "data": {
                    "negativeKey": "a",
                    "negativeWord": "miser[ ]ble",
                    "positiveKey": "y",
                    "positiveWord": "enjo[ ]able",
                    "statement": " While on a hike you realize that you forgot to put sunscreen on. You did not bring sunscreen with you and you begin to worry about skin cancer. You try not to focus on this and the rest of the hike is "
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
                    "inlineTemplate": "<div>Did you enjoy the rest of the hike? </div>"
                     }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
        ]
    },           {
        "inherit": {
            "set": "posneg",
            "type": "random"
        },
        "stimuli": [
            {"inherit": {"set": "error"}},

            {
                "data": {
                    "negativeKey": "n",
                    "negativeWord": "i[ ]capable",
                    "positiveKey": "p",
                    "positiveWord": "ca[ ]able",
                    "statement": "You are getting your routine annual physical with your doctor. After he does the initial exam, he indicates that something looks unusual and that he needs to get another doctor for a second opinion. When it comes to managing your anxiety while you wait in the exam room, you are "
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
                    "inlineTemplate": "<div>Are you capable of managing your anxiety while waiting for your doctor?<div>"
                     }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
        ]
    },           {
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
                    "statement": " You have experienced dry mouth for a few days. You wonder if the dry mouth is a minor side effect from a medicine you just started taking, or if it something more serious. The likelihood of your dry mouth indicating a serious health problem is "
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
                    "inlineTemplate": "<div>Is your mouth dry a minor side effect from your new medication? </div>"
                     }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
        ]
    },           {
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
                    "statement": " You meet a friend for lunch at a new restaurant. Once you sit down, you realize that your table was not wiped down after the last customers left, which makes you anxious about germs. You decide to stay and have lunch with your friend, and your anxiety is "
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
                    "inlineTemplate": "<div>Is your anxiety intolerable while dining with your friend? </div>"
                     }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "counter"}}, {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},
        ]
    },           {
        "inherit": {
            "set": "posneg",
            "type": "random"
        },
        "stimuli": [
            {"inherit": {"set": "error"}},

            {
                "data": {
                    "negativeKey": "c",
                    "negativeWord": "infe[ ]ted  ",
                    "positiveKey": "a",
                    "positiveWord": "he[ ]led",
                    "statement": " While at a picnic with some friends, you accidentally cut your finger while slicing fruit. Because you are outside, you are not able to wash it out immediately with soap and water. In a few days, your cut will be "
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
                    "inlineTemplate": "<div> Will your finger get infected from the cut? </div>"
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
]});
});
/* don't forget to close the define wrapper */