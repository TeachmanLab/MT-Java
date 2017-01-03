
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

// to shuffle the scenarios

function shuffle(array) {
    var currentIndex = array.length, temporaryValue, randomIndex;

    // While there remain elements to shuffle...
    while (0 !== currentIndex) {

        // Pick a remaining element...
        randomIndex = Math.floor(Math.random() * currentIndex);
        currentIndex -= 1;

        // And swap it with the current element.
        temporaryValue = array[currentIndex];
        array[currentIndex] = array[randomIndex];
        array[randomIndex] = temporaryValue;
    }

    return array;
}

// this is the function to remove a random letter

function removeRandomLetters(str, amount) {
    if(str == null) return ["",""];

    var letters = "";
    var pos = 0;
    var tries = 0;
    // select a value that is a word character, not a space or punctuation
    // And don't select the first letter in the phase.
    // Don't try to look forever.
    while (letters == "") {

        tries ++;
        // Pick a random position in the string, greater than 0
        pos = Math.floor(Math.random() * (str.length - 1)) + 1;
        if(pos == 0) continue;
        // Assure that the position begins at a series of characters long
        // enough to support the total number of letters to remove.
        var testLetters = str.substring(pos,pos + amount);
        var re = new RegExp("^\\w{" + amount + "}$");
        if(re.test(testLetters)) {
            letters = testLetters;
        }

        // Reduce the number of letters examined if we can't find a long enough string.
        if(tries > str.length * 10) {
            amount--;
            tries = 0;
        }
        if (amount == 0) break;
    }
    return [str.substring(0,pos) + "[ ]".repeat(amount) + str.substring(pos+amount), letters];
}


function getAnswer(str)
{
    if(str == null || str.indexOf('Y') > -1)
    { return 'y' }
    else
    { return 'n' }
}

function processCSV(scenarios, condition, total_sequences, lettersToRemove) {
    if (condition == 'FIFTY_FIFTY_BLOCKED')
    {
        console.log('NO');
    }
    var merged = [].concat.apply([], scenarios);
    var merged =  shuffle(merged);
    var remake = [];

    remake.push(intro);
    for (i = 0; i < total_sequences; i++) {
        if(!merged[i].Scenario) continue;
        n1 = removeRandomLetters(merged[i].NegativeS, lettersToRemove);
        n2 = removeRandomLetters(merged[i].NegativeS2, lettersToRemove);
        p1 = removeRandomLetters(merged[i].PositiveS, lettersToRemove);
        p2 = removeRandomLetters(merged[i].PositiveS2, lettersToRemove);
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
                    },
                    css: {position: 'absolute'}

                },
                {
                    "handle": "mc1",
                    data: {
                        positiveAnswer: (merged[i].mc1pos == "a") ? "a" : "b",
                        negativeAnswer: (merged[i].mc1pos == "b") ? "a" : "b"
                    },
                    "media": {
                        "inlineTemplate": "<div class='multiple-choice'> <p> " + merged[i].MultipleChoice1 + "</p> " +
                        "<ol>" +
                        "<li>" + merged[i].mc1a + " </li>" +
                        "<li>" + merged[i].mc1b + "</li>" +
                        "</ol></div>"
                    },
                    css: {position: 'absolute'}
                },
                {
                    "handle": "mc2",
                    data: {
                        positiveAnswer: (merged[i].mc2pos == "a") ? "a" : "b",
                        negativeAnswer: (merged[i].mc2pos == "b") ? "a" : "b"
                    },
                    "media": {
                        "inlineTemplate": "<div class='multiple-choice'> <p> " + merged[i].MultipleChoice2 + "</p> " +
                        "<ol>" +
                        "<li>" + merged[i].mc2a + " </li>" +
                        "<li>" + merged[i].mc2b + "</li>" +
                        "</ol></div>"
                    },
                    css: {position: 'absolute'}
                },
                {"inherit": {"set": "ab"}},
                {"inherit": {"set": "yesno"}},
                {"inherit": {"set": "stall"}}, {"inherit": {"set": "greatjob"}},
                {"inherit": {"set": "press_space"}},
                {"inherit": {"set": "counter"}},
                {"inherit": {"set": "score"}}
            ]
        };
        remake.push(scenario);
        if (i == 0 || i == 1)
        {
            vivid = {"inherit": {"set": "vivid"}};
            vivid_after = { "inherit": { "set": "vivid_after" }};

            remake.push(vivid);
            remake.push(vivid_after);
        }

        if (i == total_sequences/2 - 1)
        {
            vivid = {"inherit": {"set": "vivid"}};
            vivid_after = { "inherit": { "set": "vivid_after_half" }};

            remake.push(vivid);
            remake.push(vivid_after);
        }

        if (i == total_sequences - 1)
        {
            vivid_lst =  {"inherit": {"set": "vivid"},
                layout: [{media: {template: "../PIPlayerScripts/vividness_last.html"}}]};
            remake.push(vivid_lst);
            vivid_after = { "inherit": { "set": "vivid_after" }};
            remake.push(vivid_after);
        }
    }

    remake.push({"inherit": {"set": "results"}});

    return remake;
}