
// A Series of methods used to parse a CSV file and construct
// a json model suitable for the PiPlayer.


// A hard coded scenerio for showing introductions.
intro =
{
    input: [
        {handle: 'space', on: 'space'}
    ],
    layout: [
        // This is a stimulus object
        {
            media: {template: "../PIPlayerScripts/introRecognition.html"}
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
};

// this is the function to remove a random letter
function removeRandomLetter(str) {
    if(str == null) return ["",""];
    var pos = Math.floor(Math.random() * str.length);
    return [str.substring(0, pos) + "[ ]" + str.substring(pos + 1), str.charAt(pos)];
}

function getAnswer(str)
{
    if(str == null || str.indexOf('Y') > -1)
    { return 'y' }
    else
    { return 'n' }
}

function processCSV(scenarios) {
    var merged = [].concat.apply([], scenarios);
    var remake = [];
    remake.push(intro);
    for (i = 0; i < merged.length; i++) {
        n1 = removeRandomLetter(merged[i].NegativeS);
        n2 = removeRandomLetter(merged[i].NegativeS2);
        p1 = removeRandomLetter(merged[i].PositiveS);
        p2 = removeRandomLetter(merged[i].PositiveS2);
        var scenario =
        {
            "inherit": {
                "set": "posneg",
                "type": "random"
            },
            "stimuli": [
                {"inherit": {"set": "error"}},
                {
                    "data": {
                        "negativeKey": [n1[1].toLowerCase(), n2[1].toLowerCase()],
                        "negativeWord": [n1[0].toLowerCase(), n2[0].toLowerCase()],
                        "positiveKey": [p1[1].toLowerCase(), p2[1].toLowerCase()],
                        "positiveWord": [p1[0].toLowerCase(), p2[0].toLowerCase()],
                        "stimulus": '[stimulus]',
                        "negation": merged[i].Negation,
                        "statement": merged[i].Scenario
                    },
                    "handle": "paragraph",
                    "media": {
                        "inlineTemplate": "<div class='sentence'><%= stimulusData.statement %><span class='incomplete' style='white-space:nowrap;'><%= stimulusData.stimulus %></span></div>"
                    }
                },
                {
                    "handle": "question",
                    data: {

                        positiveAnswer: getAnswer(merged[i].PositiveQ),
                        negativeAnswer: getAnswer(merged[i].NegativeQ),
                    },
                    "media": {
                        "inlineTemplate": "<div>" + merged[i].Questions + "</div>"
                    }
                },
                {
                    "handle": "mc1",
                    data: {
                        positiveAnswer: (merged[i].mc1pos == "a") ? "a" : "b",
                        negativeAnswer: (merged[i].mc1pos == "b") ? "a" : "b"
                    },
                    "media": {
                        "inlineTemplate": "<div> <p> " + merged[i].MultipleChoice1 + "</p> " +
                        "<p> a)" + merged[i].mc1a + " </p>" +
                        "<p> b)" + merged[i].mc1b + "</p> </div>"
                    }
                },
                {
                    "handle": "mc2",
                    data: {
                        positiveAnswer: (merged[i].mc2pos == "a") ? "a" : "b",
                        negativeAnswer: (merged[i].mc2pos == "b") ? "a" : "b"
                    },
                    "media": {
                        "inlineTemplate": "<div> <p>" + merged[i].MultipleChoice2 + "</p> " +
                        "<p> a)" + merged[i].mc2a + " </p>" +
                        "<p> b)" + merged[i].mc2b + "</p> </div>"
                    }
                },
                {"inherit": {"set": "ab"}},
                {"inherit": {"set": "yesno"}},
                {"inherit": {"set": "stall"}}, {"inherit": {"set": "greatjob"}},
                {"inherit": {"set": "press_space"}},
                {"inherit": {"set": "counter"}}
            ]
        }
        remake.push(scenario);
    }
    return remake;
}