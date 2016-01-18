define([], function() {
    return({
        display_length: 7,
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
                            negativeAnswer: "n"
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
                            "positiveKey": "o",
                            "positiveWord": "[ ]n",
                            "statement": "  THE COFFEE POT: You are running late on your way to work. In the car, you realize that you forgot to check if you turned off the coffee pot. When you get to work, you think about what would happen if you did leave the coffee pot "
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
                            "inlineTemplate": "<div>Are you thinking about the coffee pot when you arrive to work?</div>"
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
                            "positiveKey": "i",
                            "positiveWord": "acc[ ]dent",
                            "statement": " THE YELLOW LIGHT: You are in your car, on your way to see a friend for lunch. Because you are running late, you are not as careful as you usually are and speed through a yellow light. As you pass through the intersection, you think about the likelihood of causing an "
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
                            "inlineTemplate": "<div>As you passed through the intersection, were you thinking about your friend?</div>"
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
                            "positiveKey": "x",
                            "positiveWord": "an[ ]ious",
                            "statement": " THE FLIGHT: You are on a long flight with your partner going to an exotic location for your vacation. The airplane pilot gets on the intercom and says there is going to be some turbulence. As you wait for the turbulence, you feel a little "
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
                            "inlineTemplate": "<div>Are you going on vacation?</div>"
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
                            "positiveWord": "all[ ]rgic",
                            "statement": " THE RESTAURANT: You are at a restaurant with a group of friends for dinner. Everyone at the table shares an appetizer. After you eat the appetizer, you remember that you did not ask the waiter if the food is cooked in peanut oil, to which you are "
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
                            "inlineTemplate": "<div>Are you at dinner with your family?</div>"
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
