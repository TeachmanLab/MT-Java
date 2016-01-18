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
                                "negativeKey": "r",
                                "negativeWord": "ter[ ]ile",
                                "positiveKey": "r",
                                "positiveWord": "th[ ]lling",
                                "statement": " You are out to dinner on a date. As you look into your date\u2019s eyes, you are unsure whether he/she will ask you out for another date. The feeling of uncertainty is "
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
                                "inlineTemplate": "<div>Is it fun to experience a little uncertainty on a date? </div>"
                            }
                        },
                        {"inherit": {"set": "yesno"}},
                        {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}},
                        {"inherit": {"set": "press_space"}},
                        {"inherit": {"set": "counter"}}
                    ]
                },
            ]
        },
        { "inherit": { "set": "vivid" } },
        { "inherit": { "set": "vivid_after" } },

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

                                "negativeKey": "e",
                                "negativeWord": "anxi[ ]ty",
                                "positiveKey": "c",
                                "positiveWord": "dan[ ]ing",
                                "statement": " You are taking a dancing class and during break, find yourself talking to a newcomer about your job. As you chat, you notice that you are sweating. She probably assumes this is a result of "
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
                                "inlineTemplate": "<div>Did the person you met at dance class think you were anxious? </div>"
                            }
                        },
                        {"inherit": {"set": "yesno"}},
                                     {"inherit": {"set": "stall"}},{"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}},  {"inherit": {"set": "counter"}}
                    ]
                },
            ]
        },
{ "inherit": { "set": "vivid" } },
{ "inherit": { "set": "vivid_after" } },

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

                    "negativeKey": "w",
                    "negativeWord": "a[ ]ful",
                    "positiveKey": "in",
                    "positiveWord": "f[ ]ne",
                    "statement": " While on a date, you accidentally spill a glass of water. You feel slightly embarrassed as your date helps you clean up the spill. The rest of the date is likely to be "
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
                    "inlineTemplate": "<div>Will the rest of the date be OK, even though you spill water? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
                                     {"inherit": {"set": "stall"}},{"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}, {"inherit": {"set": "counter"}}
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
                    "negativeWord": "n[ ]rvous",
                    "positiveKey": "m",
                    "positiveWord": "ca[ ]m",
                    "statement": " Your friend sets you up on a blind date and you agree to meet the date at a coffee shop. Once you are both sitting at your table, you notice that he/she is very attractive. You have difficulty thinking of something to say, so you sit quietly. He/she probably thinks that you are "
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
                    "inlineTemplate": "<div>Does the date think that you are anxious? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
                                     {"inherit": {"set": "stall"}},{"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}, {"inherit": {"set": "counter"}}
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
                    "negativeWord": "e[ ]cluded",
                    "positiveKey": "l",
                    "positiveWord": "inc[ ]uded",
                    "statement": " During a discussion, you end up debating an issue with colleagues. You later discover that they found your views unusual. When the issue comes up again in a group conversation, you expect that you will be "
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
                    "inlineTemplate": "<div>Do you expect that others will want to talk with you about the issue in the future? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
                                     {"inherit": {"set": "stall"}},{"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}, {"inherit": {"set": "counter"}}
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
                data: {

                    "negativeKey": "r",
                    "negativeWord": "embar[ ]assed",
                    "positiveKey": "s",
                    "positiveWord": "enthu[ ]iastic",
                    "statement": "A friend suggests that you join an evening class on creative writing. The thought of other people looking at your writing makes you feel "
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
                    "inlineTemplate": "<div>Would you expect to feel uncomfortable if others look at your work? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
                         {"inherit": {"set": "stall"}},{"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}, {"inherit": {"set": "counter"}}
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
                data: {

                    "negativeKey": "i",
                    "negativeWord": "we[ ]rd",
                    "positiveKey": "e",
                    "positiveWord": "op[  ]n",
                    "statement": " You are sitting on the couch and watching television with a friend. Your friend asks you a personal question. You answer honestly, and you think your friend judges you to be "
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
                    "inlineTemplate": "<div>Does your friend have a more negative opinion of you after you share personal information? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
                         {"inherit": {"set": "stall"}},{"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}, {"inherit": {"set": "counter"}}
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
                data: {

                    "negativeKey": "r",
                    "negativeWord": "sa[ ]castic",
                    "positiveKey": "n",
                    "positiveWord": "si[ ]cere",
                    "statement": " You are shopping with a friend, and you try on a new outfit. As you come out of the fitting room, your friend pauses, and then, without smiling, says that you look good. As you think about your friend\u2019s response, you decide your friend is being "
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
                    "inlineTemplate": "<div>Does your friend think you look nice? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
                         {"inherit": {"set": "stall"}},{"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}, {"inherit": {"set": "counter"}}
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
                data: {

                    "negativeKey": "e",
                    "negativeWord": "n[ ]gatively",
                    "positiveKey": "v",
                    "positiveWord": "fa[ ]orably",
                    "statement": " You have almost completed a computer course and part of your grade will be determined by a presentation that is to be graded by your classmates. You know most of them, and feel that their opinions of you will make them grade you more "
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
                    "inlineTemplate": "<div>Will your classmates grade your presentation advantageously? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
                         {"inherit": {"set": "stall"}},{"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}, {"inherit": {"set": "counter"}}
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
                data: {

                    "negativeKey": "r",
                    "negativeWord": "te[ ]rible",
                    "positiveKey": "e",
                    "positiveWord": "gr[ ]at",
                    "statement": " You consider taking an evening class in which part of your grade is based on your participation, even though you do not like speaking up in front of others. Because the topic is really interesting, you decide to sign up for the class. After signing up, you realize this decision was "
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
                    "inlineTemplate": "<div>Are you happy that you signed up for the interesting evening class? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
                         {"inherit": {"set": "stall"}},{"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}, {"inherit": {"set": "counter"}}
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
                data: {

                    "negativeKey": "k",
                    "negativeWord": "lac[ ]ing",
                    "positiveKey": "r",
                    "positiveWord": "no[ ]mal",
                    "statement": " You have had a busy week so your kitchen is slightly disorganized. You are expecting your neighbor to stop by for a drink, so you begin to straighten up. Just as you start, your neighbor arrives. He probably thinks your hosting skills are "
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
                    "inlineTemplate": "<div>Do you feel that your neighbor disapproves of you? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
                         {"inherit": {"set": "stall"}},{"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}, {"inherit": {"set": "counter"}}
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
                data: {

                    "negativeKey": "i",
                    "negativeWord": "fustrat[ ]ig",
                    "positiveKey": "t",
                    "positiveWord": "ra[ ]ional",
                    "statement": " You buy a new camera, but when you get it home, you decide you do not like it. You return it to the store and get your money back. The assistant is not very talkative as he helps you, and you think he views you as "
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
                    "inlineTemplate": "<div>Do you think the sales assistant felt you were annoying? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
                         {"inherit": {"set": "stall"}},{"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}, {"inherit": {"set": "counter"}}
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
                data: {

                    "negativeKey": "o",
                    "negativeWord": "p[ ]or",
                    "positiveKey": "d",
                    "positiveWord": "a[ ]mirable",
                    "statement": " You are persuaded to join a trivia team in a tournament. You are told that most of the questions will be asked to individuals, in different rounds. The first round is hard and you feel that the others found your efforts particularly "
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
                    "inlineTemplate": "<div>Did your teammates feel positive about your efforts in the tournament? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
                         {"inherit": {"set": "stall"}},{"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}, {"inherit": {"set": "counter"}}
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
                data: {

                    "negativeKey": "u",
                    "negativeWord": "d[ ]ll",
                    "positiveKey": "a",
                    "positiveWord": "like[ ]ble",
                    "statement": " You have just moved to a new area and your neighbor asks if you would like to go to your local bar that evening. When you arrive, she is not there yet. Reflecting on your earlier conversation, she probably thought you were "
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
                    "inlineTemplate": "<div>Did you make a bad impression on your new neighbor? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
                         {"inherit": {"set": "stall"}},{"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}, {"inherit": {"set": "counter"}}
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
                data: {

                    "negativeKey": "s",
                    "negativeWord": "embara[ ]sing",
                    "positiveKey": "r",
                    "positiveWord": "no[ ]mal",
                    "statement": " You are at a birthday party for a friend of a friend. You notice that you are flushed and a little sweaty. You think that these sensations are "
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
                    "inlineTemplate": "<div>Is it bad to be flushed and sweaty in front of others? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
                         {"inherit": {"set": "stall"}},{"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}, {"inherit": {"set": "counter"}}
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
                data: {

                    "negativeKey": "i",
                    "negativeWord": "anx[ ]ous",
                    "positiveKey": "l",
                    "positiveWord": "ca[ ]m",
                    "statement": " You have a minor disagreement with a coworker about the best way to complete a task. You and the coworker decide to ask your boss about it. When you talk to your boss, you momentarily forget what you were about to say. Your boss thinks you sound "
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
                    "inlineTemplate": "<div>Do you appear nervous when you speak with your boss? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
                         {"inherit": {"set": "stall"}},{"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}, {"inherit": {"set": "counter"}}
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
                data: {

                    "negativeKey": "w",
                    "negativeWord": "fro[ ]ning",
                    "positiveKey": "m",
                    "positiveWord": "s[ ]iling",
                    "statement": " You accidentally knock over a few picture frames in a store. You feel your cheeks become hot as people in the store turn and look at you. As the shop owners come over to rearrange the picture frames, he is "
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
                    "inlineTemplate": "<div>Is the shop owner mad at you for knocking over the picture frames? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
                         {"inherit": {"set": "stall"}},{"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}, {"inherit": {"set": "counter"}}
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
                data: {

                    "negativeKey": "u",
                    "negativeWord": "d[ ]ll",
                    "positiveKey": "u",
                    "positiveWord": "f[ ]n",
                    "statement": " You meet someone at a party and feel that you got along well. Later, you call and suggest meeting again. The reply you get makes you feel that the person thinks meeting again would be "
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
                    "inlineTemplate": "<div>Did the person from the party accept your invitation for getting together again? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
                         {"inherit": {"set": "stall"}},{"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}, {"inherit": {"set": "counter"}}
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
                data: {

                    "negativeKey": "n",
                    "negativeWord": "ig[ ]orant",
                    "positiveKey": "d",
                    "positiveWord": "knowle[ ]geable",
                    "statement": " You are in the car with your mother-in-law, and put on the radio. As you discuss which station to listen to, you realize that you and your mother-in-law like different types of music. After having an in-depth discussion about the pros and cons of different music styles, your mother-in-law judges you as "
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
                    "inlineTemplate": "<div>Does your mother-in-law decide that you do not know anything about music? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
                         {"inherit": {"set": "stall"}},{"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}, {"inherit": {"set": "counter"}}
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
                data: {

                    "negativeKey": "n",
                    "negativeWord": "ig[ ]orant",
                    "positiveKey": "p",
                    "positiveWord": "hel[ ]ful",
                    "statement": " A friend is having problems with her toddler and calls you to discuss the situation. You explain the methods that worked for you when your children were that age. Your friend notes that your methods are quite different from her methods, and as a result, she thinks you are "
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
                    "inlineTemplate": "<div>Did your friend find your advice constructive? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
                         {"inherit": {"set": "stall"}},{"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}, {"inherit": {"set": "counter"}}
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
                data: {

                    "negativeKey": "v",
                    "negativeWord": "lea[ ]e",
                    "positiveKey": "a",
                    "positiveWord": "st[ ]y",
                    "statement": " You arrive at a large party, and quickly realize that there are a lot of people you do not know there. You feel anxious, and consider going home. After thinking about it, you decide to "
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
                    "inlineTemplate": "<div>Do you remain at the party, even though you are feeling anxious? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
                         {"inherit": {"set": "stall"}},{"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}, {"inherit": {"set": "counter"}}
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
                data: {

                    "negativeKey": "x",
                    "negativeWord": "an[ ]ious",
                    "positiveKey": "n",
                    "positiveWord": "fi[ ]e",
                    "statement": " You are at a class that your company has sent you to. Your teacher asks each member of the group to stand up and introduce himself or herself. During your brief presentation you momentarily forget what you plan to say, but quickly recover; you guess that others thought you sounded "
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
                    "inlineTemplate": "<div>Did the other people in the workshop think you sounded nervous while you spoke? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
                         {"inherit": {"set": "stall"}},{"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}, {"inherit": {"set": "counter"}}
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
                data: {

                    "negativeKey": "u",
                    "negativeWord": "d[ ]ll",
                    "positiveKey": "l",
                    "positiveWord": "intel[ ]igent",
                    "statement": " A new teacher is hired for your history class and you hear that he is very disciplined and hard-working. When you meet him for the first time to discuss your work and interests, you think that he found you to be "
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
                    "inlineTemplate": "<div>Does your new teacher have a good opinion of you? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
                         {"inherit": {"set": "stall"}},{"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}, {"inherit": {"set": "counter"}}
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
                data: {

                    "negativeKey": "w",
                    "negativeWord": "a[  ]ful",
                    "positiveKey": "y",
                    "positiveWord": "st[ ]lish",
                    "statement": " You arrive at a party in a new outfit. Everyone turns to look at you as you walk in. You decide that they must think that you look "
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
                    "inlineTemplate": "<div>Did everyone at the party like your new outfit? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
                         {"inherit": {"set": "stall"}},{"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}, {"inherit": {"set": "counter"}}
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
                data: {

                    "negativeKey": "n",
                    "negativeWord": "u[ ]interesting",
                    "positiveKey": "t",
                    "positiveWord": "in[ ]eresting",
                    "statement": " You arrange to meet your date at 8 PM in a local bar. You arrive on time and find that he/she is not there yet. After your last conversation, you thought that he/she found you "
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
                    "inlineTemplate": "<div>Was your date interested in you after your last conversation? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
                         {"inherit": {"set": "stall"}},{"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}, {"inherit": {"set": "counter"}}
        ]
    },
    ]},
    { "inherit": { "set": "vivid" } }, { "inherit": { "set": "vivid_after" } },
    {
	mixer: 'random',
		    data:[
    {
        "inherit": {
            "set": "posneg",
            "type": "random"
        },
        "stimuli": [
            {"inherit": {"set": "error"}},
            {
                data: {

                    "negativeKey": "n",
                    "negativeWord": "ba[ ]krupt",
                    "positiveKey": "a",
                    "positiveWord": "ok[ ]y",
                    "statement": " Your credit card bill for the month will arrive soon. You spent a bit more this month than you normally do. When you think about your future finances, you feel like you will be  "
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
                    "inlineTemplate": "<div>Are you likely to go broke in the future? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
                         {"inherit": {"set": "stall"}},{"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}, {"inherit": {"set": "counter"}}
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
                data: {

                    "negativeKey": "x",
                    "negativeWord": "an[ ]ious",
                    "positiveKey": "f",
                    "positiveWord": "con[ ]ident",
                    "statement": " You are taking a challenging exam. On one of the questions, you are 90% certain that you know the correct answer. When you respond, you feel "
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
                    "inlineTemplate": "<div>Are you uneasy about your answer? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
                         {"inherit": {"set": "stall"}},{"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}, {"inherit": {"set": "counter"}}
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
                data: {

                    "negativeKey": "j",
                    "negativeWord": "unen[ ]oyable",
                    "positiveKey": "y",
                    "positiveWord": "enjo[ ]able",
                    "statement": " You get home from work on a Friday evening and wonder if you locked the office door. You usually lock the door, but still feel anxious not knowing for sure. Your weekend away from the office will be "
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
                    "inlineTemplate": "<div>Will you still be able to enjoy the weekend, even if you are wondering about the lock? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
                         {"inherit": {"set": "stall"}},{"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}, {"inherit": {"set": "counter"}}
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
                data: {

                    "negativeKey": "r",
                    "negativeWord": "wo[ ]risome",
                    "positiveKey": "x",
                    "positiveWord": "e[ ]citing",
                    "statement": " Your professor asks you what career you plan to pursue. You respond that you haven\u2019t decided yet. Thinking about your future is "
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
                    "inlineTemplate": "<div>Do you enjoy thinking about your future? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
                         {"inherit": {"set": "stall"}},{"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}, {"inherit": {"set": "counter"}}
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
                data: {

                    "negativeKey": "i",
                    "negativeWord": "worr[ ]ed",
                    "positiveKey": "l",
                    "positiveWord": "ca[ ]m",
                    "statement": " You are ready to purchase a new car. After considering all the options, you are still torn between two cars. When you tell the dealer which car you decided to buy, you feel "
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
                    "inlineTemplate": "<div>Are you anxious about your decision? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
                         {"inherit": {"set": "stall"}},{"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}, {"inherit": {"set": "counter"}}
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
                data: {

                    "negativeKey": "m",
                    "negativeWord": "un[ ]anageable",
                    "positiveKey": "g",
                    "positiveWord": "mana[ ]eable",
                    "statement": " Your son is going camping with his friend\u2019s family for two days. There will be no cell phone reception and you are worried about not being able to check in with him. Your worries over the next two days are going to be "
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
                    "inlineTemplate": "<div>Are you able to manage your worries about your son? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
                         {"inherit": {"set": "stall"}},{"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}, {"inherit": {"set": "counter"}}
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
                data: {

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
                         {"inherit": {"set": "stall"}},{"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}, {"inherit": {"set": "counter"}}
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
                data: {

                    "negativeKey": "y",
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
                         {"inherit": {"set": "stall"}},{"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}, {"inherit": {"set": "counter"}}
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
                data: {

                    "negativeKey": "g",
                    "negativeWord": "lar[ ]e",
                    "positiveKey": "n",
                    "positiveWord": "mi[ ]imal",
                    "statement": " Your friend enjoys ice-skating and persuades you to try it out. At the rink you put on the skates and step on the ice. You glide forward, slowly at first, then faster, and think your chances of getting injured are "
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
                    "inlineTemplate": "<div>Do you think you will get hurt when ice skating? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
                         {"inherit": {"set": "stall"}},{"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}, {"inherit": {"set": "counter"}}
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
                data: {

                    "negativeKey": "e",
                    "negativeWord": "un[ ]njoyable",
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
                         {"inherit": {"set": "stall"}},{"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}, {"inherit": {"set": "counter"}}
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
                data: {

                    "negativeKey": "b",
                    "negativeWord": "ro[ ]bed",
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
                         {"inherit": {"set": "stall"}},{"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}, {"inherit": {"set": "counter"}}
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
                data: {

                    "negativeKey": "p",
                    "negativeWord": "im[ ]ossible",
                    "positiveKey": "s",
                    "positiveWord": "po[ ]sible",
                    "statement": " You heard in the news that a lot of businesses are currently downsizing. You think about what will happen if you lose your job. You decide that finding another job will be "
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
                    "inlineTemplate": "<div> Do you think it will be impossible to find a new job? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
                         {"inherit": {"set": "stall"}},{"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}, {"inherit": {"set": "counter"}}
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
                data: {

                    "negativeKey": "c",
                    "negativeWord": "si[ ]k",
                    "positiveKey": "i",
                    "positiveWord": "f[ ]ne",
                    "statement": " You have not heard from your elderly neighbor recently. You normally visit with him regularly, so it is strange that you have not seen him lately. Your neighbor has probably been "
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
                    "inlineTemplate": "<div>Has your elderly neighbor likely been sick? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
                         {"inherit": {"set": "stall"}},{"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}, {"inherit": {"set": "counter"}}
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
                data: {

                    "negativeKey": "x",
                    "negativeWord": "an[ ]ious",
                    "positiveKey": "l",
                    "positiveWord": "re[ ]axed",
                    "statement": " This morning you left the house and began your drive to work. About 10 minutes into your commute, you wonder if you remembered to lock the door to your home. As you continue to drive, you feel "
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
                    "inlineTemplate": "<div> Are you able to stay calm, despite being unsure whether you remembered to lock your door? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
                         {"inherit": {"set": "stall"}},{"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}, {"inherit": {"set": "counter"}}
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
                data: {

                    "negativeKey": "n",
                    "negativeWord": "u[ ]manageable",
                    "positiveKey": "g",
                    "positiveWord": "mana[ ]eable",
                    "statement": " Your son is going camping with his friend\u2019s family for two days. There will be no cell phone reception and you are worried about not being able to check in with him. Your worries over the next two days are going to be "
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
                    "inlineTemplate": "<div>Are you able to manage your worries about your son? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
                         {"inherit": {"set": "stall"}},{"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}, {"inherit": {"set": "counter"}}
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
                data: {

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
                         {"inherit": {"set": "stall"}},{"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}, {"inherit": {"set": "counter"}}
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
                data: {

                    "negativeKey": "n",
                    "negativeWord": "u[ ]successfully",
                    "positiveKey": "c",
                    "positiveWord": "suc[ ]essfully",
                    "statement": " Your friend is taking an airplane to come visit you. The idea of your friend taking an airplane makes you feel anxious. You think you will deal with your anxiety "
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
                    "inlineTemplate": "<div>Are you able to successfully manage your nervousness? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
                         {"inherit": {"set": "stall"}},{"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}, {"inherit": {"set": "counter"}}
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
                data: {

                    "negativeKey": "b",
                    "negativeWord": "rob[ ]ers",
                    "positiveKey": "n",
                    "positiveWord": "part[ ]er",
                    "statement": " You are at home alone watching TV at night. You must have been dozing because you suddenly wake up. You have the impression that you heard a frightening noise and then realize it must be your "
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
                    "inlineTemplate": "<div> Should you be frightened when you hear a noise at night? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
                         {"inherit": {"set": "stall"}},{"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}, {"inherit": {"set": "counter"}}
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
                data: {

                    "negativeKey": "g",
                    "negativeWord": "hi[ ]h",
                    "positiveKey": "o",
                    "positiveWord": "l[ ]w",
                    "statement": " You go to the hospital to visit a friend. Someone behind you coughs. If you wash your hands, the probability that you will get sick is "
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
                    "inlineTemplate": "<div>Is it likely that you will become sick? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
                         {"inherit": {"set": "stall"}},{"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}, {"inherit": {"set": "counter"}}
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
                data: {

                    "negativeKey": "o",
                    "negativeWord": "seri[ ]us",
                    "positiveKey": "n",
                    "positiveWord": "mi[ ]or",
                    "statement": " While making dinner, you accidentally touch a hot pan. You quickly put your finger under cold water. You think the burn is "
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
                    "inlineTemplate": "<div>Will your burned finger be okay? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
                         {"inherit": {"set": "stall"}},{"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}, {"inherit": {"set": "counter"}}
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
                data: {

                    "negativeKey": "i",
                    "negativeWord": "s[ ]ck",
                    "positiveKey": "s",
                    "positiveWord": "thir[ ]ty",
                    "statement": " Your throat is scratchier than it usually is. You think about why this may be. You decide it is probably because you are "
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
                    "inlineTemplate": "<div>Will drinking water make your throat feel better? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
                         {"inherit": {"set": "stall"}},{"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}, {"inherit": {"set": "counter"}}
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
                data: {

                    "negativeKey": "n",
                    "negativeWord": "u[ ]manageable",
                    "positiveKey": "a",
                    "positiveWord": "manage[ ]ble",
                    "statement": "Yesterday, you got together with a friend who coughed a few times while you were out. You are nervous that you might catch something from your friend. You decide that your anxiety is "
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
                    "inlineTemplate": "<div> Can you manage your anxiety about potentially getting sick from your friend?</div>"
                }
            },
            {"inherit": {"set": "yesno"}},
                         {"inherit": {"set": "stall"}},{"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}, {"inherit": {"set": "counter"}}
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
                data: {

                    "negativeKey": "n",
                    "negativeWord": "i[ ]tolerable",
                    "positiveKey": "b",
                    "positiveWord": "tolera[ ]le",
                    "statement": " You have not been feeling great recently, so you try to schedule a doctor\u2019s appointment. The soonest appointment you can get is a few days away, which makes you feel anxious. You know that your anxiety as you wait for the appointment is "
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
                    "inlineTemplate": "<div>Do you think you will be able to handle your anxiety as you wait for the doctor\u2019s appointment? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
                         {"inherit": {"set": "stall"}},{"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}, {"inherit": {"set": "counter"}}
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
                data: {

                    "negativeKey": "i",
                    "negativeWord": "s[ ]ck",
                    "positiveKey": "i",
                    "positiveWord": "f[ ]ne",
                    "statement": "Yesterday you spent the day with your friend and her new infant. A few times during the day, the infant coughed and sneezed on you, and you wonder if you will catch something. Tomorrow, you will probably be "
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
                    "inlineTemplate": "<div> Will you get sick from your friend's new baby?</div>"
                }
            },
            {"inherit": {"set": "yesno"}},
                         {"inherit": {"set": "stall"}},{"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}, {"inherit": {"set": "counter"}}
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
                data: {

                    "negativeKey": "a",
                    "negativeWord": "unhe[ ]lthy",
                    "positiveKey": "l",
                    "positiveWord": "hea[ ]thy",
                    "statement": " At your routine doctor's appointment, your doctor decides to run a few tests to evaluate your health. The doctor tells you that she will get you your results within a week. While you wait for your results, you think you are likely "
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
                    "inlineTemplate": "<div>Do you think that you are healthy? </div>"
                }
            },
            {"inherit": {"set": "yesno"}},
                         {"inherit": {"set": "stall"}},{"inherit":{"set":"greatjob"}}, {"inherit": {"set": "press_space"}}, {"inherit": {"set": "counter"}}
        ]
    }
    ]
        },
        {
            "inherit": {"set": "vivid"},
            layout: [{media: {template: "/PIPlayerScripts/vividness_last.html"}}]
        }
    ]});
});
/* don't forget to close the define wrapper */