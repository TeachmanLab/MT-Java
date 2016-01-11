/* The script wrapper */
define(['pipAPI','pipScorer'], function(APIConstructor,Scorer) {

    jQuery.fn.visible = function() {
        return this.css('visibility', 'visible');
    };

    jQuery.fn.invisible = function() {
        return this.css('visibility', 'hidden');
    };

    var API = new APIConstructor();
    var scorer = new Scorer();
    var break_up;
    var text_to_display;
    var word_display;
    var where_at = 1;
    var latency = 0;
    var vivid_text;
    var scorer =
    {
        count : 1
    };

    var on_question = false;

    function increase_count(){
        scorer.count = scorer.count+1;
        return scorer.count;
    }

    /**
     * Returns the missing letters
     * @param trial
     * @returns a string containing the missing letters, based on if this is a positive or negative word.
     */
    function missing_letters(trial) {
        p = jQuery.grep(trial._stimulus_collection.models, function(e, i) {return e.attributes.handle == "paragraph"})[0]
        if(trial.data.positive) {
            return (p.attributes.data.positiveKey);
        } else {
            return(p.attributes.data.negativeKey);
        }
    }

    /**
     * Returns true if missing letters start with the letters typed so far.
     * @param trial
     * @param inputData
     * @returns {*}
     */
    function correct_letters(trial,inputData) {
        if(!API.getGlobal().lettersTyped) API.addGlobal({lettersTyped:""});
        if(inputData.handle.length  > 1) return false;
        var lettersTyped = API.getGlobal().lettersTyped + inputData.handle;
        return missing_letters(trial).startsWith(lettersTyped);
    }

    /**
     * Returns true if the number of letters entered meets or exceeds the letters expected.
     * @param trial
     * @param inputData
     */
    function correct_length(trial, inputData) {
        if(!API.getGlobal().lettersTyped) API.addGlobal({lettersTyped:""});
        var lettersTyped = API.getGlobal().lettersTyped + inputData.handle;
        return lettersTyped.length >= missing_letters(trial).length
    }

    // Warn people about leaving the page before they complete all the questions
    window.onbeforeunload = function() {
        return 'Are you sure you want to exit this training session?'
    }

    // When the training session is complete, move on to the next Questionnaire
    API.addSettings('redirect', "../playerScript/completed/int_train");

    // Removes the warning about leaving the page.
    API.addSettings("hooks", {
        endTask: function () {
            window.onbeforeunload = null;
            window.location = "../playerScript/completed/int_train";
        }
    })

    API.addSettings("canvas", {
        maxWidth: 1000,
        proportions: {width:4,height:3},
        textSize: 5
    });

    // setting the way the logger works (how often we send data to the server and the url for the data)
    API.addSettings('logger',{
        pulse: 1,
        url : '/data',
        logger:
            function(trialData, inputData, actionData,logStack){
                var stimList = this._stimulus_collection.get_stimlist();
                var mediaList = this._stimulus_collection.get_medialist();
                var global = API.getGlobal();

                /*
                 p = jQuery.grep(trial._stimulus_collection.models, function(e, i) {return e.attributes.handle == "paragraph"})[0]
                 if(trial.data.positive) {
                 return (p.attributes.data.positiveKey);
                 } else {
                 return(p.attributes.data.negativeKey);
                 }
                 */
                return {
                    log_serial : logStack.length,
                    trial_id: this._id,
                    name: this.name(),
                    responseHandle: inputData.handle,
                    latency: Math.floor(trialData.letter_latency),
                    stimuli: stimList,
                    media: mediaList,
                    data: trialData,
                    script: global["script"],
                    session: global["session"],
                    participant: global["participant"]
                }
            }
    });

    API.addStimulusSets({
        error: [
            {handle:'error',media:'X', css:{color:'#FF0000'}, location:{top:70}, nolog:true}
        ],
        yesno: [
            {   handle:'yesno',
                media:{html:"<div class='stim'>Please Type <b>Y</b>=Yes &nbsp;  &nbsp;  &nbsp; <b>N</b>=No</div>"},
                css: {color: '#333', fontSize: '.8em', position: 'absolute'},
                location:{bottom: 1}
            }
        ],
        stall: [
            {
                handle:'stall',
                media:{html:"<div class='stim'>Oops, that answer is incorrect; please re-read the question and in a moment you will have a chance to answer again.</div>"},
                css: {color: '#333', fontSize: '.8em', position: 'absolute'},
                location:{top:50},
                nolog:true}
        ],
        greatjob:
            [
                {
                    handle:'greatjob',
                    media:{html:"<div class='stim'>Great job!</div>"},
                    nolog:true,
                    css: {color: '#333', fontSize: '1.2em', position: 'absolute'},
                    location:{bottom: 50}
                }
            ],
        press_space:
            [
                {
                    handle: 'press_space',
                    media: {html: "<div class='press_space'>Press the <b>Space Bar</b> to continue.</div>"},
                    nolog: true,
                    css: {color: '#333', fontSize: '.8em', position: 'absolute'},
                    location: {bottom: 1}
                }
            ],
        vivid: [
            {media :{'inlineTemplate':"<div class='vivid'>_______</div>"}}
        ],
        counter: [
            {
                'handle': 'counter',
                customize: function () {
                    this.media = scorer.count + ' of 50';
                    on_question = false;
                },
                css: {color: '#333', fontSize: '.8em', position: 'absolute'},
                location: {bottom: 1,  right: 1}
            }
        ]
    });

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
            // Show the paragraph with missing letters as soon as the trial starts.
            {
                conditions: [{type:'begin'}],
                actions: [
                    {type:'showStim',handle:'paragraph'},
                    {type:'showStim',handle:'press_space'},
                    {type:'showStim', handle: 'counter'},
                    {type:'setGlobalAttr',setter:{askingQuestion:false}},
                    {type:'setGlobalAttr',setter:{sentenceDisplayed:false}},
                    {type:'setTrialAttr',setter:{correctOnLetter:"true"}},  // set to true - will get set to false later if incorrectly answered.
                    {type:'custom',fn:function(options,eventData) {API.addGlobal({"original":$("span.incomplete").text()})}},
                    {type:'custom',fn:function(trial,inputData) {
                        var sentence = $("div.sentence");
                        break_up = $("div.sentence").text().split('.');
                        last_word = break_up[break_up.length-1].split(' ');
                        last_word = last_word[last_word.length-2] + ' ' + last_word[last_word.length-1];
                        break_up[break_up.length-1] = break_up[break_up.length-1].replace(last_word, "");
                        break_up.push(last_word);
                        for (i = 0; i < break_up.length; i++) {
                            if (i == break_up.length-1)
                            {
                                break_up[i] = $("<p class='incomplete'>" + break_up[i] + '.</p>')
                            }
                            else if (i == break_up.length-2)
                            {
                                break_up[i] = $("<p class='sentences'>" + break_up[i] + '</p>')
                            }
                            else
                            {
                                break_up[i] = $("<p class='sentences'>" + break_up[i] + '.</p>')
                            }
                            break_up[i].invisible();
                        }
                        sentence.html(break_up);
                        break_up[0].visible();
                    }
                    }]

            },

            {// Sentence display - one at a time
                conditions: [
                    {type:'globalEquals', property:'askingQuestion', value:false},
                    {type:'inputEquals',value:'askQuestion', negate: true},
                    {type:'inputEquals',value:'correct', negate: true},
                    {type:'function', value:function(trial, inputData){
                        if (where_at < break_up.length && inputData.handle == 'space' && inputData.latency - latency > 1000 && ! on_question)
                        {
                            var sentence = $("div.sentence");
                            if (where_at < (break_up.length - 2))
                            {
                                break_up[where_at].visible();

                            }
                            else if (where_at < (break_up.length - 1))
                            {
                                break_up[where_at].visible();
                            }
                            else
                            {
                                break_up[where_at].visible();
                                var space = $("div.press_space");
                                space.text("Type the missing letter.");
                                //space.before(break_up[where_at]);
                            }

                            latency = inputData.latency;
                            where_at = where_at + 1;
                        }
                        else if (where_at >= break_up.length)
                        {
                            latency = 0;
                            return true;

                        }
                    }}
                ],
                actions: [
                    {type:'setGlobalAttr',setter:{sentenceDisplayed:true}}
                ]
            },
            // The letters entered are incorrect
            {
                conditions:
                    [
                        {type:'globalEquals', property:'sentenceDisplayed', value:true},
                        {type:'globalEquals', property:'askingQuestion', value:false},
                        {type:'inputEquals',value:'askQuestion', negate: true},
                        {type:'function',value:function(trial,inputData){
                            return (!correct_letters(trial, inputData));
                        }
                        }
                    ],
                actions: [
                    {type:'showStim',handle:'error'},
                    {type:'setTrialAttr',setter:{correctOnLetter:"false"}},
                    {type:'setInput',input:{handle:'clear', on:'timeout',duration:500}}
                ],
            },
            {// The letters are correct so far...
                conditions: [
                    {type:'globalEquals', property:'askingQuestion', value:false},
                    {type:'function',value:function(trial,inputData){ return !correct_length(trial, inputData) }},
                    {type:'function',value:function(trial,inputData){ return correct_letters(trial, inputData) }}
                ],
                actions: [
                    {type:'custom',fn:function(options,eventData){
                        API.getGlobal().lettersTyped = API.getGlobal().lettersTyped + eventData.handle;
                        var span = $("p.incomplete");
                        var text = span.text().replace(' ', eventData["handle"]);
                        span.text(text);
                    }},
                    {type:'setTrialAttr',setter:function(trialData, eventData){
                        if(trialData.first_letter_latency == null) {
                            trialData.first_letter_latency = Math.floor(eventData.latency);
                        }
                    }}
                ]
            },
            {// All the letters are entered correctly
                conditions: [
                    {type:'globalEquals', property:'askingQuestion', value:false},
                    {type: 'function', value:function(trial, inputData)
                    {
                        if (where_at < break_up.length)
                        {
                            return false;
                        }
                        else
                        {
                            return true;
                        }
                    }},
                    {type:'function',value:function(trial,inputData){ return correct_length(trial, inputData) }},
                    {type:'function',value:function(trial,inputData){ return correct_letters(trial, inputData) }}
                ],
                actions: [
                    {type:'custom',fn:function(options,eventData){
                        var span = $("p.incomplete");
                        var text = span.text().replace(' ', eventData["handle"]);
                        span.text(text);
                        where_at = 1;
                        on_question = true;
                        API.getGlobal().lettersTyped = "";
                    }},
                    {type:'setGlobalAttr',setter:{askingQuestion:true}},
                    {type:'trigger',handle : 'correct'}
                ]
            },

            {
                // Trigger when the correct response is provided, as there are two interactions
                // that can cause this, I've separated it out into it's own section rather than
                // duplicate the code.
                conditions: [{type:'inputEquals',value:'correct'}],
                actions: [
                    // Preserve the question as completed, so that it will eventually be set back to the server.
                    {type:'setTrialAttr',setter:function(trialData, eventData){
                        trialData.word = $("span.incomplete").text();
                        trialData.paragraph = $("div[data-handle='paragraph']").text();
                        trialData.letter_latency  = Math.floor(eventData.latency);
                        if(trialData.first_letter_latency == null) {
                            trialData.first_letter_latency = Math.floor(eventData.latency);
                        }
                    }},
                    {type:'resetTimer'}, // Reset timer so that we can also collect the latency on the y/n respose
                    // Remove all keys but 'y' and 'n'
                    {type:'removeInput',handle : ['a','b','c','d','e','f','g','h','i','j','k','l','m','o','p','q','r','s','t','u','v','v','w','x','z']},
                    {type:'setInput',input:{handle:'askQuestion', on:'timeout',duration:500}}
                ]
            },
            // After the statement is correctly completed, hide it, and show the question.
            {
                // Trigger when input handle is "end".
                conditions: [{type:'inputEquals',value:'askQuestion'}],
                actions: [
                    {type:'custom',fn:function(options,eventData){
                        $("div.sentence").empty();
                    }},
                    {type:'hideStim',handle : 'press_space'},
                    {type:'hideStim',handle : 'paragraph'},
                    {type:'showStim',handle : 'question'},
                    {type:'showStim',handle:'yesno'},
                    {type:'setTrialAttr',setter:{correctOnQuestion:"true"}},  // set to true - will get set to false later if incorrectly answered.
                    {type:'setGlobalAttr',setter:{askingQuestion:true}}
                ]
            },
            // Listen for a correct response to a positive question
            {
                conditions: [{type:'inputEqualsStim', property:'positiveAnswer'},
                    {type:'trialEquals', property:'positive', value:true},
                    {type:'globalEquals', property:'askingQuestion', value:true}
                ],
                actions: [
                    {type:'hideStim',handle : 'question'},
                    {type:'hideStim',handle:'yesno'},
                    {type:'hideStim', handle: 'counter'},
                    {type:'showStim', handle:'greatjob'},
                    {type:'trigger',handle : 'answered', duration:1000},

                ]
            },
            // Listen for a correct response to a negative question
            {
                conditions: [{type:'inputEqualsStim', property:'negativeAnswer'},
                    {type:'trialEquals', property:'positive', value:false},
                    {type:'globalEquals', property:'askingQuestion', value:true}
                ],
                actions: [
                    {type:'hideStim',handle : 'question'},
                    {type:'hideStim',handle:'yesno'},
                    {type:'hideStim', handle: 'counter'},
                    {type:'showStim', handle:'greatjob'},
                    {type:'trigger',handle : 'answered', duration:1000},
                ]
            },
            // Listen for an incorrect response to a positive question
            {
                conditions: [{type:'inputEqualsStim', property:'positiveAnswer'},
                    {type:'trialEquals', property:'positive', value:false},
                    {type:'globalEquals', property:'askingQuestion', value:true}
                ],
                actions: [
                    {type:'setTrialAttr',setter:function(trialData, eventData){
                        if(trialData.first_question_latency == null) {
                            trialData.first_question_latency = Math.floor(eventData.latency);
                        }
                    }},
                    {type:'removeInput', handle:'y'},
                    {type:'removeInput', handle:'n'},
                    {type:'hideStim', handle:'yesno'},
                    {type:'showStim',handle:'error'},
                    {type:'showStim',handle:'stall'},
                    {type:'setInput',input:{handle:'delay',on:'timeout',duration:5000}},
                    {type:'setTrialAttr',setter:{correctOnQuestion:"false"}},
                    {type:'setInput',input:{handle:'clear', on:'timeout',duration:500}}
                ]
            },
            // Listen for a incorrect response to a negative question
            {
                conditions: [{type:'inputEqualsStim', property:'negativeAnswer'},
                    {type:'trialEquals', property:'positive', value:true},
                    {type:'globalEquals', property:'askingQuestion', value:true}
                ],
                actions: [
                    {type:'setTrialAttr',setter:function(trialData, eventData){
                        if(trialData.first_question_latency == null) {
                            trialData.first_question_latency = Math.floor(eventData.latency);
                        }
                    }},
                    {type:'removeInput', handle:'y'},
                    {type:'removeInput', handle:'n'},
                    {type:'hideStim', handle:'yesno'},
                    {type:'showStim',handle:'error'},
                    {type:'showStim',handle:'stall'},
                    {type:'setInput',input:{handle:'delay',on:'timeout',duration:5000}},
                    {type:'setTrialAttr',setter:{correctOnQuestion:"false"}},
                    {type:'setInput',input:{handle:'clear', on:'timeout',duration:500}}
                ]
            },
            {
                conditions: [{type: 'inputEquals', value: 'delay'}],
                actions: [
                    {type: 'setInput', input: {handle: 'y', on: 'keypressed', key: 'y'}},
                    {type: 'setInput', input: {handle: 'n', on: 'keypressed', key: 'n'}},
                    {type: 'showStim', handle: 'yesno'},
                    {type: 'hideStim', handle: 'stall'},
                ]
            },
            {
                // Trigger when the correct response is provided, as there are two interactions
                // that can cause this, I've separated it out into it's own section rather than
                // duplicate the code.
                conditions: [{type:'inputEquals',value:'answered'}],
                actions: [
                    {type:'setGlobalAttr',setter:function(){
                        increase_count();
                    }},
                    {type:'removeInput',handle : ['y','n']},
                    {type:'setTrialAttr',setter:function(trialData, eventData){
                        trialData.question = $("div[data-handle='question']").text();
                        trialData.question_latency  = Math.floor(eventData.latency);
                        if(trialData.first_question_latency == null) {
                            trialData.first_question_latency = Math.floor(eventData.latency);
                        }
                    }},
                    {type:'log'},
                    {type:'endTrial'}
                ]
            },

            // This interaction is triggered by a timeout after a incorrect response.
            // It allows us to delay the removal of the big red X.
            {
                // Trigger when input handle is "end".
                conditions: [
                    {type:'inputEquals',value:'clear'}],
                actions: [
                    {type:'removeInput',handle : 'clear'},
                    {type:'hideStim',handle:'error'}
                ]
            }


        ]

    }]);


    API.addTrialSets('all',[
                    { inherit:'base', data: {positive:true}}
                            ]);

    API.addSequence([
        {
            input: [
                {handle:'space',on:'space'}
            ],
            layout: [
                // This is a stimulus object
                {
                    media : {template:"/PIPlayerScripts/introRecognition.html"}
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
                        positiveAnswer:"y",
                        negativeAnswer:"n"
                    },
                    "media": {
                        "inlineTemplate": "<div>Did you think about the elevator’s safety?</div>"
                    }
                },
                {"inherit": {"set": "yesno"}},
                {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}},
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
                        positiveAnswer:"y",
                        negativeAnswer:"n"
                    },
                    "media": {
                        "inlineTemplate": "<div>Are you thinking about the coffee pot when you arrive to work?</div>"
                    }
                },
                {"inherit": {"set": "yesno"}},
                {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}},
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
                        positiveAnswer:"y",
                        negativeAnswer:"n"
                    },
                    "media": {
                        "inlineTemplate": "<div>Will you be without an income soon?</div>"
                    }
                },
                {"inherit": {"set": "yesno"}},
                {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}},
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
                        positiveAnswer:"y",
                        negativeAnswer:"n"
                    },
                    "media": {
                        "inlineTemplate": "<div>Have you been woken up in the middle of the night?</div>"
                    }
                },
                {"inherit": {"set": "yesno"}},
                {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}},
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
            {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}},
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
                	positiveAnswer:"y",
                    negativeAnswer:"n"
                },
                "media": {
                    "inlineTemplate": "<div>Are you going on vacation?</div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}},
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
                	positiveAnswer:"n",
                    negativeAnswer:"y"
                },
                "media": {
                    "inlineTemplate": "<div>Are you at dinner with your family?</div>"
                }
            },
            {"inherit": {"set": "yesno"}},
            {"inherit": {"set": "stall"}}, {"inherit":{"set":"greatjob"}},
            {"inherit": {"set": "press_space"}},
            {"inherit": {"set": "counter"}}

        ]
    }
]);
    return API.script;
});
/* don't forget to close the define wrapper */