// REQUIREMENTS:
/*

 The loading file will need to include the following libraries
 * Jquery 1.11.1 or higher
 * jspsych
 * mootools
 * jquery-csv
 * PapaParse
 *    plugins/jspsych-html.js
 *    plugins/jspsych-button-response.js
 * Custom Plugins
 *    js/missing-letters.js
 *    js/button-response-correct.js
 *    js/button-response-correct.js
 *
 * And these CSS Files:
 * css/jspsych-mobile.css
 * css/experiment.css
*/

var TEMPLETON_MODULE = (function () {
    var my = {};

    my.letters_to_remove = 2; // number of missing letters to complete in the term.
    my.total_scenarios = 15;  // How many scenarios should be randomly selected out of the full set?
    my.condition = "NEUTRAL"; // Can be POSITIVE, POSITIVE_NEGATION, FIFTY_FIFTY_RANDOM, FIFTY_FIFTY_BLOCKED, NEUTRAL
    my.block_size = 5; // Number of items in block, if condiftion if fifty_fifty_blocked.
    my.second_word_set = false; // uses the second wordset if set to true.
    my.question_type = "yes_no";  // Can be yes_no, mc1, or mc2.
    my.traget = "jspsych-target";
    my.base_url = "/js/training";
    my.post_url = "/jspsych";
    my.redirect_url = "/jspsych/continue";

    // This score is incremented for every correct answer and displayed
    // to the user.
    var score_letters = 0;
    var score_questions = 0;
    var progress = -1;
    var vivid_response;

    my.execute = function() {
        if(!my.base_url.endsWith('/')) my.base_url = my.base_url + "/";
        if (my.condition != "NEUTRAL") {
            parse_data(my.base_url + "scenarios/scenarios.csv", parse_complete);
        } else {
            parse_data(my.base_url + "scenarios/scenarios_neutral.csv", parse_complete);
        }
    }

    function parse_data(url, callBack) {
        Papa.parse(url, {
            download: true,
            dynamicTyping: true,
            header: true,
            complete: function (results) {
                callBack(results.data);
            }
        });
    }

    function parse_complete(data) {
        updateProgress();
        updateScore();
        build_timeline(data);
    }

    // DISPLAY SCORE AND PROGRESS
    // ***************

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
                type: 'button-response',
                is_html: true,
                choices: ['Continue'],
                stimulus: function () {
                    return (
                        "<div class='piIntro'> " +
                        "<img src='" + my.base_url + "images/compass-blue.png' > " +
                        "<p>In this part of the program, you will read a series of very " +
                        "short stories.  Pay attention to the title of each story because " +
                        "after you have read all the stories, you will be asked more questions about them.</p> " +
                        "<br clear='all'> " +
                        "<b>For each story:</b> " +
                        "<ul> " +
                        "<li>Read each sentence carefully and <i>really imagine</i> yourself in the situation described. </li>" +
                        "<li>Even if the story describes you reacting in a way that you would not usually react, please " +
                        "try to picture yourself responding in the way the story describes. </li> " +
                        "<li>There will be an incomplete word at the end of each paragraph. </li> " +
                        "<li>Press the key(s) on the keyboard that complete the word.  </li> " +
                        "<li>When you correctly complete the word you will move on to the next screen and be asked a " +
                        "question about the story. </li> " +
                        "<li>The score, located in the top right corner, shows the number of times you completed a word " +
                        "or answered a question correctly on the first try. </li> " +
                        "</ul> " +
                        "</div>"
                    )
                },
                on_finish: function(data){ data.stimulus = "introduction" }
            };

        // The Final Score, shown at the end of the experiment.
        var final_trial_score = {
            type: 'button-response',
            is_html: true,
            choices: ['Continue'],
            stimulus: function () {
                var pct_ct_s = Math.round((score_letters / my.total_scenarios) * 100);
                var pct_ct_c = Math.round((score_questions / my.total_scenarios) * 100);
                var score = score_letters + score_questions;
                var feed_back_score = "You scored " + score + " out of a maximum possible score of " + my.total_scenarios * 2;
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
                '<img src= "' + my.base_url + 'images/finished.jpg' + '"/>' +
                '<p>' + feed_back_score + "</p>" +
                '<p>' + feed_back_s + '</p>' +
                '<p>' + feed_back_c + '</p>')
            },
            on_finish: function(data){ data.stimulus = "final score screen" }
        };

        /* create experiment timeline array */
        var timeline = [];

        timeline.push(introduction);

        // Randomize the scenarios
        scenarios = jsPsych.randomization.sample(scenarios, my.total_scenarios, false);

        // Loop through the time-line creating scenarios
        var positive = true;
        for (var k = 0; k < my.total_scenarios; k++) {
            var paragraph;
            var phrase;
            var yes_no_correct;
            var mc1_correct;
            var mc2_correct;

            switch (my.condition) {
                case "POSITIVE_NEGATION":
                    positive = true;
                    paragraph = scenarios[k]["Scenario"].replace("[negation]", scenarios[k]['Negation']);
                    break;
                case "NEUTRAL":
                case "POSITIVE":
                    positive = true;
                    paragraph = scenarios[k]['Scenario'].replace("[negation]", "");
                    break;
                case "FIFTY_FIFTY_RANDOM":
                    paragraph = scenarios[k]['Scenario'].replace("[negation]", "");
                    positive = Math.random() >= 0.5;
                    break;
                case "FIFTY_FIFTY_BLOCKED":
                    paragraph = scenarios[k]['Scenario'].replace("[negation]", "");
                    if (k > 0 && k % my.block_size == 0) positive = !positive;
                    break;
            }

            if (positive) {
                if (my.second_word_set) phrase = scenarios[k]['PositiveS2'];
                else phrase = scenarios[k]['PositiveS'];
                yes_no_correct = scenarios[k]['PositiveQ'];
                mc1_correct = scenarios[k]['mc1pos'];
                mc2_correct = scenarios[k]['mc2pos'];
            } else {
                if (my.second_word_set) phrase = scenarios[k]['NegativeS2'];
                else phrase = scenarios[k]['NegativeS'];
                yes_no_correct = scenarios[k]['PostiveQ'] == "Yes" ? "No" : "Yes";
                mc1_correct = scenarios[k]['mc1pos'] == "a" ? "b" : "a";
                mc2_correct = scenarios[k]['mc2pos'] == "a" ? "b" : "a";
            }

            /***********************************************
             * SCENARIO BASED TRIALS
             ***********************************************/

            var paragraph_trial = {
                type: 'sentence-reveal',
                paragraph: paragraph
            };

            var phrase_trial = {
                type: 'missing-letters',
                phrase: phrase,
                letters_to_remove: my.letters_to_remove,
                on_finish: function (trial_data) {
                    if (trial_data.correct) score_letters++;
                    updateScore();
                }
            };

            var vividness = {
                type: 'button-response',
                is_html: true,
                stimulus: 'How vividly did you imagine yourself in the scenario?',
                choices: ['Not at all', 'Somewhat', 'Moderately', 'Very', 'Totally'],
                on_finish: function (trial_data) {
                    vivid_response = trial_data.button_pressed > 2;
                    trial_data.stimulus = "vividness"
                }
            };

            var vividness_final = {
                type: 'button-response',
                is_html: true,
                stimulus: 'Thinking about the set of 40 scenarios you just completed, on average, how vividly did you imagine yourself in the scenarios?',
                choices: ['Not at all', 'Somewhat', 'Moderately', 'Very', 'Totally'],
                on_finish: function (trial_data) {
                    trial_data.stimulus = "vividness_final"
                }
            };

            // Vivid Follow up - changes based on response.
            var vividness_followup = {
                type: 'button-response',
                is_html: true,
                choices: ['Continue'],
                stimulus: function () {
                    if (vivid_response) {
                        return (
                            "<div class='vividness_followup'>" +
                            "<h1>Thanks. It's great you're really using your imagination!</h1>" +
                            "<img src='" + my.base_url + "images/good-job.jpg'/>" +
                            "</div>"
                        )
                    } return (
                            "<div class='vividness_followup'>" +
                            "<h1>Thanks. Really try to use your imagination!</h1>" +
                            "<img src='" + my.base_url + "images/imagination.jpg'/>" +
                            "</div>"
                    )
                },

                cont_btn: "continue",
                on_finish: function (trial_data) {
                    if(vivid_response) {
                        trial_data.stimulus = "Good Job"
                    } else {
                        trial_data.stimulus = "Use Imagination"
                    }
                }
            };
            // Vivid Follow up - changes based on response.
            var vividness_followup_halfway = {
                type: 'button-response',
                is_html: true,
                choices: ['Continue'],
                stimulus: function () {
                    return (
                        "<div class='vividness_followup'>" +
                        "<h1>You are halfway done!</h1>" +
                        "<img src='" + my.base_url + "images/halfway.jpg'/>" +
                        "</div>"
                    )
                },
                on_finish: function (trial_data) {
                        trial_data.stimulus = "Half Way"
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
                    updateProgress();
                }
            };

            var mc1 = {
                type: 'button-response-correct',
                is_html: true,
                stimulus: scenarios[k]['MultipleChoice1'],
                choices: [scenarios[k]['mc1a'], scenarios[k]['mc1b']],
                correct_choice: mc1_correct == "a" ? scenarios[k]['mc1a'] : scenarios[k]['mc1b'],
                on_finish: function (trial_data) {
                    if (trial_data.correct) score_questions++;
                    updateScore();
                    updateProgress();
                }
            };

            var mc2 = {
                type: 'button-response-correct',
                is_html: true,
                stimulus: scenarios[k]['MultipleChoice2'],
                choices: [scenarios[k]['mc2a'], scenarios[k]['mc2b']],
                correct_choice: mc2_correct == "a" ? scenarios[k]['mc2a'] : scenarios[k]['mc2b'],
                on_finish: function (trial_data) {
                    if (trial_data.correct) score_questions++;
                    updateScore();
                    updateProgress();C
                }
            };

            // BUILD THE TIMELINE FROM THE COMPONENTS ABOVE.
            // *********************************************
            timeline.push(paragraph_trial);
            timeline.push(phrase_trial);
            switch (my.question_type) {
                case ("yes_no"):
                    timeline.push(yes_no);
                    break;
                case ("mc1"):
                    timeline.push(mc1);
                    break;
                case ("mc2"):
                    timeline.push(mc2);
                    break;
            }
            // Add vividness question after questions 1 and 2...
            if (k == 0 || k == 1) {
                timeline.push(vividness);
                timeline.push(vividness_followup);
            } else if (k == Math.floor(my.total_scenarios / 2) - 1) {
                timeline.push(vividness);
                timeline.push(vividness_followup_halfway);
            }
        }

        timeline.push(vividness_final);
        timeline.push(final_trial_score);

        function saveData(data, callback){

            $.ajax({
                type:'post',
                contentType: 'application/json',
                cache: false,
                url: my.post_url, // this is the path to the above PHP script
                data: data,
                success: callback,
                error: function(XMLHttpRequest, textStatus, errorThrown) {
                    alert("Status: " + textStatus); alert("Error: " + errorThrown);
                }                });
        }

        function redirect() {
            window.location.assign(my.redirect_url);
        }

        jsPsych.init({
            timeline: timeline,
            display_element: $("#" + my.target),
            on_finish: function(data){
                jsPsych.data.addProperties({
                    condition: my.condition
                });
                saveData(jsPsych.data.dataAsJSON(), redirect) }
        });

        /*
        jsPsych.init({
            timeline: timeline,
            display_element: $("#" + my.target),
            on_finish: function (data) {
                $.ajax({
                    type:'post',
                    contentType: 'application/json',
                    cache: false,
                    url: my.post_url, // this is the path to the above PHP script
                    data: data

                });
                window.location.assign(my.redirect_url);
            }
        });
        */
    }

    return my;
}());













