define([], function() {
    return({
        display_length: 9,
        add_extra_missing_letter:false,
        sequence:[
            {
                input: [
                    {handle: 'space', on: 'space'}
                ],
                layout: [
                    // This is a stimulus object
                    {
                        media: {template: "/PIPlayerScripts/introRecognition.html"}
                    }
                ],
                interactions: [
                    // This is an interaction (it has a condition and an action)
                    {
                        conditions: [
                            {type: 'inputEquals', value: 'space'}
                        ],
                        actions: [
                            {type: 'endTrial'}
                        ]
                    }
                ]
            },
            {
                "inherit": {
                    "set": "all",
                    "type": "random"
                },
                "stimuli": [
                    {"inherit": {"set": "error"}},
                    {
                        "data": {

                            "positiveKey": "a",
                            "positiveWord": "s[ ]fety",
                            "statement": " THE ELEVATOR: You are in the lobby of your friend’s new apartment building.  You press the button to the elevator to go up. The building looks old.  As you get on the elevator you think about its "
                        },
                        "handle": "paragraph",
                        "media": {
                            "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
                        }
                    },
                    {
                        "handle": "question",
                        data: {
                            positiveAnswer: "y",
                            negativeAnswer: "n",
                        },
                        "media": {
                            "inlineTemplate": "<div>Did you think about the elevator’s safety?</div>"
                        }
                    },
                    {"inherit": {"set": "yesno"}},
                    {"inherit": {"set": "stall"}}, {"inherit": {"set": "greatjob"}},
                    {"inherit": {"set": "press_space"}},
                    {"inherit": {"set": "counter"}}

                ]
            },
// NEXT
            {
                "inherit": {
                    "set": "all",
                    "type": "random"
                },
                "stimuli": [
                    {"inherit": {"set": "error"}},
                    {
                        "data": {
                            "positiveKey": "a",
                            "positiveWord": "l[ ]ugh",
                            "statement": " THE WEDDING RECEPTION: Your friend asks you to give a speech at her wedding reception. You prepare some remarks and when the time comes, get to your feet. As you speak, you notice some people in the audience start to "
                        },
                        "handle": "paragraph",
                        "media": {
                            "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
                        }
                    },
                    {
                        "handle": "question",
                        data: {
                            positiveAnswer: "y",
                            negativeAnswer: "n"
                        },
                        "media": {
                            "inlineTemplate": "<div>Did the audiences laugh when you speak?</div>"
                        }
                    },
                    {"inherit": {"set": "yesno"}},
                    {"inherit": {"set": "stall"}}, {"inherit": {"set": "greatjob"}},
                    {"inherit": {"set": "press_space"}},
                    {"inherit": {"set": "counter"}}

                ]
            },
// NEXT
            {
                "inherit": {
                    "set": "all",
                    "type": "random"
                },
                "stimuli": [
                    {"inherit": {"set": "error"}},
                    {
                        "data": {
                            "positiveKey": "a",
                            "positiveWord": "fin[ ]nces",
                            "statement": " THE JOB: You are currently working as a contractor for a company. Once this job is finished, you will be without employment until you can find your next job. You think about not having an income for a few weeks and about your future "
                        },
                        "handle": "paragraph",
                        "media": {
                            "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
                        }
                    },
                    {
                        "handle": "question",
                        data: {
                            positiveAnswer: "y",
                            negativeAnswer: "n"
                        },
                        "media": {
                            "inlineTemplate": "<div>Will you be without an income soon?</div>"
                        }
                    },
                    {"inherit": {"set": "yesno"}},
                    {"inherit": {"set": "stall"}}, {"inherit": {"set": "greatjob"}},
                    {"inherit": {"set": "press_space"}},
                    {"inherit": {"set": "counter"}}

                ]
            },
// NEXT
            {
                "inherit": {
                    "set": "all",
                    "type": "random"
                },
                "stimuli": [
                    {"inherit": {"set": "error"}},
                    {
                        "data": {
                            "positiveKey": "t",
                            "positiveWord": "downs[ ]airs",
                            "statement": "  THE LOUD NOISE: You are woken up in the middle of the night by a loud noise. You are not sure what caused the noise and leave your bedroom to see what happened. You walk "
                        },
                        "handle": "paragraph",
                        "media": {
                            "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
                        }
                    },
                    {
                        "handle": "question",
                        data: {
                            positiveAnswer: "y",
                            negativeAnswer: "n"
                        },
                        "media": {
                            "inlineTemplate": "<div>Have you been woken up in the middle of the night?</div>"
                        }
                    },
                    {"inherit": {"set": "yesno"}},
                    {"inherit": {"set": "stall"}}, {"inherit": {"set": "greatjob"}},
                    {"inherit": {"set": "press_space"}},
                    {"inherit": {"set": "counter"}}

                ]
            },
// NEXT
            {
                "inherit": {
                    "set": "all",
                    "type": "random"
                },
                "stimuli": [
                    {"inherit": {"set": "error"}},
                    {
                        "data": {
                            "positiveKey": "e",
                            "positiveWord": "th[ ]re",
                            "statement": " MEETING A FRIEND: In the street you bump into an old friend you haven't seen for a long time. She is too busy to stop, so you arrange to meet later in a bar.  You arrive a little late but the bar is empty and a few minutes later she is still not "
                        },
                        "handle": "paragraph",
                        "media": {
                            "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
                        }
                    },
                    {
                        "handle": "question",
                        data: {
                            positiveAnswer: "n",
                            negativeAnswer: "y"
                        },
                        "media": {
                            "inlineTemplate": "<div>Did your friend stop?</div>"
                        }
                    },
                    {"inherit": {"set": "yesno"}},
                    {"inherit": {"set": "stall"}}, {"inherit": {"set": "greatjob"}},
                    {"inherit": {"set": "press_space"}},
                    {"inherit": {"set": "counter"}}

                ]
            },
// NEXT
            {
                "inherit": {
                    "set": "all",
                    "type": "random"
                },
                "stimuli": [
                    {"inherit": {"set": "error"}},
                    {
                        "data": {
                            "positiveKey": "o",
                            "positiveWord": "y[ ]u",
                            "statement": " THE LUNCH: You are eating lunch with a friend. As you start eating your salad, you describe your plans for the weekend. You accidentally drop a piece of lettuce, and your friend looks at "
                        },
                        "handle": "paragraph",
                        "media": {
                            "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
                        }
                    },
                    {
                        "handle": "question",
                        data: {
                            positiveAnswer: "y",
                            negativeAnswer: "n"
                        },
                        "media": {
                            "inlineTemplate": "<div>Do you have salad for lunch?</div>"
                        }
                    },
                    {"inherit": {"set": "yesno"}},
                    {"inherit": {"set": "stall"}}, {"inherit": {"set": "greatjob"}},
                    {"inherit": {"set": "press_space"}},
                    {"inherit": {"set": "counter"}}

                ]
            },
// NEXT
            {
                "inherit": {
                    "set": "all",
                    "type": "random"
                },
                "stimuli": [
                    {"inherit": {"set": "error"}},
                    {
                        "data": {
                            "positiveKey": "e",
                            "positiveWord": "bl[ ]ed",
                            "statement": " THE SCRAPE: You are playing basketball with some friends. While running toward the ball, you trip and scrape your knee. The scrape hurts a bit, but does not "
                        },
                        "handle": "paragraph",
                        "media": {
                            "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
                        }
                    },
                    {
                        "handle": "question",
                        data: {
                            positiveAnswer: "n",
                            negativeAnswer: "y"
                        },
                        "media": {
                            "inlineTemplate": "<div>Are you playing soccer with your friend?</div>"
                        }
                    },
                    {"inherit": {"set": "yesno"}},
                    {"inherit": {"set": "stall"}}, {"inherit": {"set": "greatjob"}},
                    {"inherit": {"set": "press_space"}},
                    {"inherit": {"set": "counter"}}

                ]
            },
// NEXT
            {
                "inherit": {
                    "set": "all",
                    "type": "random"
                },
                "stimuli": [
                    {"inherit": {"set": "error"}},
                    {
                        "data": {
                            "positiveKey": "c",
                            "positiveWord": "si[ ]k",
                            "statement": "  THE SHOPPING TRIP: You are at the mall with your friend. While you shop, she tells you how several of her friends have recently come down with a strange illness. You think about your recent health, and wonder if you will get "
                        },
                        "handle": "paragraph",
                        "media": {
                            "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
                        }
                    },
                    {
                        "handle": "question",
                        data: {
                            positiveAnswer: "n",
                            negativeAnswer: "y"
                        },
                        "media": {
                            "inlineTemplate": "<div>Are you shopping alone?</div>"
                        }
                    },
                    {"inherit": {"set": "yesno"}},
                    {"inherit": {"set": "stall"}}, {"inherit": {"set": "greatjob"}},
                    {"inherit": {"set": "press_space"}},
                    {"inherit": {"set": "counter"}}
                  ]
            },
// NEXT
            {
                "inherit": {
                    "set": "all",
                    "type": "random"
                },
                "stimuli": [
                    {"inherit": {"set": "error"}},
                    {
                        "data": {
                            "positiveKey": "i",
                            "positiveWord": "t[ ]me",
                            "statement": "  THE BLOOD TEST: You are at a routine doctor’s appointment. At the appointment, the doctor decides to run a few blood tests to check your health. The doctor says he will call you in a few weeks, and you will find out your test results at that "
                        },
                        "handle": "paragraph",
                        "media": {
                            "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>"
                        }
                    },
                    {
                        "handle": "question",
                        data: {
                            positiveAnswer: "y",
                            negativeAnswer: "n"
                        },
                        "media": {
                            "inlineTemplate": "<div>Did you take blood tests?</div>"
                        }
                    },
                    {"inherit": {"set": "yesno"}},
                    {"inherit": {"set": "stall"}}, {"inherit": {"set": "greatjob"}},
                    {"inherit": {"set": "press_space"}},
                    {"inherit": {"set": "counter"}}
                  ]
            }
    ]})
});
