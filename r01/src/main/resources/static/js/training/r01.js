var R01 = (function () {
            var my = {};

            my.letters_to_remove = 2; // number of missituationrs to complete in the term.
            my.total_scenarios = 24;  // How many scenarios should be randomly selected out of the full set?
            my.question_type = "yes_no";  // Can be yes_no, mc1, or mc2.
            my.base_url = "/js/training";
            my.post_url = "/jspsych";
            my.status_url = "/jspsych/status";
            my.redirect_url = "/jspsych/continue";
            my.lemon = false;
            my.script = "eightSession";
            my.data = {};
            my.lastSaveIndex = 0;
            my.start = 0;

            // This score is incremented for every correct answer and displayed
            // to the user.
            var score_letters = 0;
            var score_questions = 0;
            var progress = -1;
            var vivid_response;
            var followup_count = 0;
            var completed_previously = 0;

            my.execute = function () {
                if (my.base_url.slice(-1) !== '/') my.base_url = my.base_url + "/";
                parse_data(my.base_url + "scenarios/" + my.script + ".csv", parse_complete);
            };

            function parse_data(url, callBack) {
                Papa.parse(url, {
                    download: true,
                    dynamicTyping: true,
                    header: true,
                    skipEmptyLines: true,
                    complete: function (results) {
                        callBack(results.data);
                    }
                });
            }

            function parse_complete(data) {
                my.data = data;
                if(my.total_scenarios > my.data.length) {
                    my.total_scenarios = my.data.length;
                }
                load_past_progress(progress_loaded)
            }


            function load_past_progress(callback) {
                $.ajax({
                    type: 'get',
                    contentType: 'application/json',
                    cache: false,
                    url: my.status_url, // this is the path to the above PHP script
                    complete: function (results) {
                        callback(results);
                    }
                });
            }

            function progress_loaded(results) {
                data = results.responseJSON;
                // Figure out how many tasks were completed previously.  If any.
                // And reduce the number of scenerios to complete by this amount.
                for (var t in data) {
                    if (data[t].trial_type == "missing-letters") {
                        completed_previously++;
                    }
                }
                my.total_scenarios = my.total_scenarios - completed_previously;
                my.start = completed_previously;

                // Build the timeline.
                build_timeline(my.data);

                console.log("Progress fully loaded");
                updateProgress();
                updateScore();
            }

            /**
             * Randomize array element order in-place.
             * Using Durstenfeld shuffle algorithm. (taken from Stackoverflow - Laurens Holst)
             */
            function shuffleArray(array) {
                for (var i = array.length - 1; i > 0; i--) {
                    var j = Math.floor(Math.random() * (i + 1));
                    var temp = array[i];
                    array[i] = array[j];
                    array[j] = temp;
                }
            }

            // DISPLAY SCORE AND PROGRESS
            // ***************

            function updateBanner(content) {
                document.getElementById('banner').textContent = content;
            }

            function updateScore() {
                document.getElementById('score').textContent = "Score: " + (score_letters + score_questions);
            }

            function updateProgress() {
                progress++;
                document.getElementById('progress').textContent =
                    "Completed : " + progress + " of " + my.total_scenarios;
            }


            // This is called when we complete parsing the CSV file (see parseData above),
            // and will in tern build all the trials.
            function build_timeline(scenarios) {

                /***********************************************
                 * STATIC TRIALS
                 ***********************************************/
                    // An introduction / instructions
                var introduction = {
                        type: 'html-button-response',
                        choices: ['Continue'],
                        stimulus: function () {
                            return (
                                "<div class='piIntro'> " +
                                "<h1><b>Completing Short Stories</b></h1>" +
                                "<p>You will now read several short stories. For each story:</p>" +
                                "<ul> " +
                                "<li>Please <b><i>imagine</i></b> yourself in the situation described.</li> " +
                                "<li>Some stories may describe you reacting to the situation differently than you usually " +
                                "would. Please really try to imagine yourself responding the way the story describes, even if it would be unusual for you.</li> " +
                                "<li>The last word of each story is incomplete. Click on the missing letter to complete the word.</li> " +
                                "<li>After you correctly complete the word, you will be asked a question about the story. </li> " +
                                "<li>Answer the question based on the information in the story, whether or not that matches what you would personally think or do in that situation. </li> " +
                                "</ul> " +
                                "</div>"
                                )
                        },

                        on_finish: function (data) {
                            data.stimulus = "introduction"
                        }
                    };

                var lemon_exercise = {
                    type: 'instructions',
                    show_clickable_nav: true,
                    pages: ["<div class='details'>" +
                    '<h1>In this task you will read or listen to a series of short stories. We will ask you to imagine yourself in the situation described in each story.</h1>' +
                    "<h1>Before we begin, we\'d like to walk you through a brief imagination exercise.</h1>",
                        '<h1>Welcome to the "Lemon" exercise.</h1> <p>The purpose of this quick exercise is to demonstrate what imagination-based thinking is.</p><p>You will go through what imagining seeing, touching, and smelling a lemon is like.</p><p>Please imagine it as if you are really experiencing it.</p>',
                        '<h1>First-person perspective</h1> <p>In this exercise, and throughout the training program, please remember to imagine what is happening through <i>your own eyes</i> (picture on the left), not as an outside observer (picture on the right) ...</p>' +
                        '<div style="display: flex; justify-content: center;"><img src="' + my.base_url + 'images/lemon/firstperson.png" style="padding: 20px 20px 20px 20px;"><img src="' + my.base_url + 'images/lemon/secondperson.png" style="padding: 20px 20px 20px 20px;"></div>',
                        '<h1>Ok, let\'s begin:</h1> <h1>Imagine you are holding the lemon in your right hand, and you can feel its shape and its weight.</h1>' +
                        '<p><i>(Please take a few seconds to imagine this)</i></p>',
                        '<h1>Now imagine you are shining a light on the lemon, and you can see the waxy and lumpy texture of the yellow skin.</h1>' +
                        '<p><i>(Please take a few seconds to imagine this)</i></p>',
                        '<h1>Now imagine that you scratch the skin with your fingernail, then you bring the lemon up to your nose, and you can smell the fresh zesty juice from the skin.</h1>' +
                        '<p><i>(Please take a few seconds to imagine this)</i></p>',
                        '<h1>Now imagine that you cut the lemon in half, and you bring one half of it up for a closer look. You can see the juicy flesh in the shape of segments that look like a wagon wheel.</h1>' +
                        '<p><i>(Please take a few seconds to imagine this)</i></p>',
                        '<h1>Now imagine that you squeeze the lemon and some of the juice squirts right into your eyes, and it is really stinging, making your eyes water.</h1>' +
                        '<p><i>(Please take a few seconds to imagine this)</i></p>',
                        '<h1>That was the lemon exercise!</h1> <p><i>Don\'t worry if you didn\'t experience all of the sensations strongly, this is completely normal.</i></p>' +
                        '<img src="' + my.base_url + 'images/lemon/lemon_2.png" style="margin: auto; padding: 20px 20px 20px 20px;"></div>'
                    ]
                };

                var vividness = {
                    type: 'html-button-response',
                    is_html: true,
                    stimulus: 'How vividly did you imagine yourself in the scenario?',
                    choices: ['Not at all', 'Somewhat', 'Moderately', 'Very', 'Totally'],
                    on_finish: function (trial_data) {
                        vivid_response = trial_data.button_pressed > 2;
                        trial_data.stimulus = "vividness"
                    }
                };

                // The Final Score, shown at the end of the experiment.
                var final_trial_score = {
                    type: 'html-button-response',
                    is_html: true,
                    choices: ['Continue'],
                    stimulus: function () {
                        var max_score = my.total_scenarios + followup_count;
                        var pct_ct_s = Math.round((score_letters / my.total_scenarios) * 100);
                        var pct_ct_c = Math.round((score_questions / followup_count) * 100);
                        var score = score_letters + score_questions;
                        var feed_back_score = "You scored " + score + " out of a maximum possible score of " + max_score + "with" + pct_ct_s + "correct missing letter questions";
                        var feed_back_s = 'You filled in the missing letters correctly on the first try ' + pct_ct_s + '% of the time this round. ';
                        var feed_back_c = 'You answered the yes/no question following each story correctly on the first try ' + pct_ct_c + '% of the time this round. ';

                        if (pct_ct_s >= 90) feed_back_s +=
                            'Great job, we’re really impressed with your ability to complete the stories! ';
                        if (pct_ct_s < 90 && pct_ct_s >= 70) feed_back_s +=
                            'You’re doing well, and we encourage you to pay really close attention to the ' +
                            'stories to work out what letters are needed to complete the final words. ' +
                            'This will allow you to get the most out of the training.';
                        else feed_back_s += 'We want to encourage you to pay really close attention to the ' +
                            'stories to work out what letters are needed to complete the final words. ' +
                            'This will allow you to get the most out of the training. If any aspect of' +
                            ' the task is unclear, please email us with any questions at studyteam@mindtrails.org.';
                        if (pct_ct_c >= 90)
                            feed_back_c += 'That’s terrific, and shows you’re paying really careful attention to the stories!';
                        else if (pct_ct_c < 90 && pct_ct_s >= 70)
                            feed_back_c += 'You’re doing a good job, and we want to remind you to pay really close attention ' +
                                'to the whole story each time, including how it ends, and just use the information in the ' +
                                'story to answer the question. This will allow you to get the most out of the training.';
                        else
                            feed_back_c += 'We want to remind you to pay really close attention to the whole story ' +
                                'each time, including how it ends, and just use the information in the story to ' +
                                'answer the question. This will allow you to get the most out of the training. If' +
                                'any aspect of the task is unclear, please email us with any questions at ' +
                                'studyteam@mindtrails.org.';

                        return (
                        '<div class="vividness_followup">' +
                        '<img src= "' + my.base_url + 'images/lemon/lemon_2.png' + '"/>' +
                        '<p>' + feed_back_score + "</p>" +
                        '<p>' + feed_back_s + '</p>' +
                        '<p>' + feed_back_c + '</p>')
                    },
                    on_finish: function(data){ data.stimulus = "final score screen" }
                };



                var save_data = {
                    type: 'call-function',
                    func: function () {
                        saveData(function () {
                            jsPsych.data.aaData = []; // Clear data just sent.
                            console.log("DATA IS " + jsPsych.data.get());
                        })
                    }
                };

                /* create experiment timeline array */
                var timeline = [];
                if (my.lemon) {
                    timeline.push(lemon_exercise);
                }
                timeline.push(introduction);

                // Loop through the time-line creating scenarios
                var positive = true;
                for (var k = my.start; k < scenarios.length; k++) {
                    var scenario;
                    var paragraph;
                    var phrase;
                    var immersion;
                    var title;
                    var yes_no_correct;
                    var mcq_positive;
                    var mcq_negative;
                    var mcq_answer;
                    var format;

                    paragraph = scenarios[k]['Paragraph'];
                    scenario = scenarios[k]['Scenario'];
                    format = scenarios[k]['Format'];
                    immersion = scenarios[k]['Immersion'];
                    title = scenarios[k]['Title'];
                    positive = true;

                    if (positive) {
                        phrase = scenarios[k]['FinalWord'];
                        yes_no_correct = scenarios[k]['Correct'];
                        mcq_positive = scenarios[k]['MCQ_Positive'];
                        mcq_negative = scenarios[k]['MCQ_Negative'];
                    }

                    /***********************************************
                     * SCENARIO BASED TRIALS
                     ***********************************************/

                    var immersion_trial = null;

                    if (immersion === "picture") {
                        immersion_trial = {
                            type: 'html-button-response',
                            stimulus: "<h1 class='title'>Story: " + title + "</h1><img class='sound_image' src='" + my.base_url + "images/" + scenario + ".jpg'>",
                            trial_duration: 5000, // Show trial for 5 seconds
                            data: {immersion: immersion, format: format, scenario: scenario},
                            choices: []
                        }
                    } else if (immersion === "picture_sound") {
                        immersion_trial = {
                            type: 'audio-button-response',
                            stimulus: 'sounds/background/' + scenario + '.mp3',
                            trial_duration: 5000, // Show trial for 5 seconds
                            prompt: "<h1 class='title'>Story: " + title + "</h1><img class='sound_image' src='" + my.base_url + "images/" + scenario + ".jpg'>",
                            data: {immersion: immersion, format: format, scenario: scenario},
                            choices: []
                        }
                    } else {
                        immersion_trial = {
                            type: 'html-button-response',
                            stimulus: "<h1 class='title'>Story: " + title + "</h1>",
                            trial_duration: 5000,
                            data: {immersion: immersion, format: format, scenario: scenario},
                            choices: []
                        }
                    }

                    var main_trial = null;

                    if (format === "Auditory") {
                        main_trial = {
                            type: 'audio-button-response',
                            stimulus: my.base_url + 'sounds/' + scenario + '.mp3',
                            trial_ends_after_audio: true,
                            prompt: '<p>Please listen ...</p>',
                            data: {immersion: immersion, format: format, scenario: scenario},
                            choices: []
                        };
                    } else {
                        main_trial = {
                            type: 'sentence-reveal',
                            paragraph: paragraph,
                            data: {immersion: immersion, format: format, scenario: scenario}
                        };
                    }

                    var phrase_trial = {
                        type: 'missing-letters',
                        phrase: phrase,
                        letters_to_remove: my.letters_to_remove,
                        data: {immersion: immersion, format: format, scenario: scenario},
                        on_finish: function (trial_data) {
                            if (trial_data.correct) score_letters++;
                            updateScore();
                            updateProgress();
                        }
                    };

                    var mcq = {
                        type: 'button-response-correct',
                        is_html: true,
                        stimulus: scenarios[k]['Questions'],
                        choices: [scenarios[k]['MCQ_Positive'], scenarios[k]['MCQ_Negative']],
                        correct_choice: scenarios[k]['MCQ_Positive'],
                        on_finish: function (trial_data) {
                            if (trial_data.correct) score_questions++;
                            updateScore();
                        }
                    };

                    var yes_no = {
                        type: 'button-response-correct',
                        is_html: true,
                        stimulus: scenarios[k]['Questions'],
                        choices: ["Yes", "No"],
                        correct_choice: yes_no_correct,
                        on_finish: function (trial_data) {
                            if (trial_data.correct) score_questions++;
                            updateScore();
                        }
                    };

                    // Vivid Follow up - changes based on response.
                    var stimulus;
                    switch (k) {
                        case 5:
                            stimulus = "<h1>Well done.</h1>" +
                                "<h1>Remember, imagine the scenario as if you are experiencing it through your own eyes.</h1>";
                            break;
                        case 10:
                            stimulus = "<h1>Nice Work.</h1>" +
                                "<h1>Take time to visualize each situation.</h1>";
                            break;
                        case 15:
                            stimulus = "<h1>Good job.</h1>" +
                                "<h1>Remember, try to imagine the stories as vividly as you can.</h1>";
                            break;
                        case 20:
                            stimulus = "<h1>You're doing great. </h1>" +
                                "<h1>Keep focusing on the stories and imagine them from your own eyes.</h1>"
                            break;
                        default:
                            stimulus = "<h1>Almost there. </h1>" +
                                "<h1>Keep imagining as vividly as you can. </h1>"
                            break;
                    }
                    var vividness_followup = {
                        type: 'html-button-response',
                        choices: ['Continue'],
                        stimulus: "<div class='vividness_followup'>" +
                        stimulus +
                        "<img src='" + my.base_url + "images/lemon/lemon_" + (k / 5) + ".png'/>" +
                        "</div>"
                    };

                    // BUILD THE TIMELINE FROM THE COMPONENTS ABOVE.
                    // *********************************************

                    if (k % 5 === 0 && k !== 0) {
                        timeline.push(vividness_followup)
                    }

                    timeline.push(immersion_trial);
                    timeline.push(main_trial);
                    timeline.push(phrase_trial);

                    // Only ask a followup question 2/3rd of the time.
                    if (Math.random() >= 0.333) {
                        followup_count++;
                        if  (scenarios[k]["Correct"]) {
                            timeline.push(yes_no);
                        }   else {
                            timeline.push(mcq);
                        }
                    }

                    // Save data to the server after each scenerio is completed.
                    timeline.push(save_data);

                }


                function saveData(callback) {
                    var all_data = jsPsych.data.get().values();
                    var data_to_save = [];
                    var saved_from = my.lastSaveIndex;

                    for (var i = my.lastSaveIndex; i < all_data.length; i++) {
                        data_to_save.push(all_data[i]);
                    }
                    my.lastSaveIndex = all_data.length;

                    $.ajax({
                        type: 'post',
                        contentType: 'application/json',
                        cache: false,
                        url: my.post_url, // this is the path to the above PHP script
                        data: JSON.stringify(data_to_save),
                        success: callback,
                        error: function (XMLHttpRequest, textStatus, errorThrown) {
                            my.lastSaveIndex = saved_from;
                            alert("Status: " + textStatus);
                            alert("Error: " + errorThrown);
                        }
                    });
                }


                function redirect() {
                    window.location.assign(my.redirect_url);
                }

                // Preload images
                // an array of paths to images that need to be loaded
                /*
                 var images = [];
                 images.push(my.base_url + "images/finished.png");
                 images.push(my.base_url + "images/good-job.png");
                 images.push(my.base_url + "images/halfway.png");
                 images.push(my.base_url + "images/imagination.png");
                 for(var s = 1; s < 5; s++) {
                 for(var i = 8; i < 33; i += 8) {
                 images.push(my.base_url + "images/s" + s + "/" + i + ".png");
                 }
                 }

                 setTimeout(
                 jsPsych.pluginAPI.preloadImages(images, function(){ startExperiment(); }),
                 10000);
                 */
                startExperiment();

                // Start the experiment.
                function startExperiment() {
                    $("#spinner").hide();
                    jsPsych.init({
                        timeline: timeline,
                        display_element: my.target,
                        on_finish: function (data) {
                            window.onbeforeunload = null; // Remove any warnings about leaving the page.
                            jsPsych.data.addProperties({
                                condition: my.condition
                            });
                            saveData(redirect);


                        }
                    });
                }


            }

            return my;
        }
        ()
    )
;













