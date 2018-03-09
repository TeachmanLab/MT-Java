/*
 * sentence-reveal
 * Dan Funk
 *
 * Reveals a paragraph, once sentence at a time.
 * The paragraph is split on periods, where one sentence
 * is revealed at a time.  When the full statement is
 * presented the trial is ended, leaving the
 * sentence visible unless otherwise specified.
 */

jsPsych.plugins["sentence-reveal"] = (function () {

    var plugin = {};

    plugin.info = {
        name: 'sentence-reveal',
        description: 'Review sentences in a paragraph one at a time.',
        parameters: {
            paragraph: {
                type: jsPsych.plugins.parameterType.STRING,
                pretty_name: 'Paragraph',
                default: undefined,
                description: 'The paragraph to be revealed one sentence at a time.'
            },
            button_html: {
                type: jsPsych.plugins.parameterType.STRING,
                pretty_name: 'Button html',
                default: '<button class="jspsych-btn">%choice%</button>',
                array: true,
                description: 'The html of the button. Can create own style.'
            },
            button_text: {
                type: jsPsych.plugins.parameterType.STRING,
                pretty_name: 'Button text',
                default: 'continue ...',
                description: 'What to show on the button'
            }
        }
    }


    plugin.trial = function (display_element, trial) {

        var lastIndex = trial.paragraph.lastIndexOf(" ");

        trial.paragraph = trial.paragraph.substring(0, lastIndex);

        var sentences = trial.paragraph.split(".");
        var sentence_index = 0;

        var html = '<div id="jspsych-sentence-reveal-statement" class="center-content block-center"></div>';

        // Display the continue button.
        html += '<div id="jspsych-sentence-reveal-btngroup" class="center-content block-center">';
        var continue_button = trial.button_html.replace(/%choice%/g, trial.button_text);
        html += '<div class="jspsych-sentence-reveal-continue" ' +
            'style="display: inline-block; margin:' + trial.margin_vertical + ' ' + trial.margin_horizontal + '" ' +
            'id="jspsych-sentence-reveal-continue">' + continue_button + '</div>';
        html += '</div>';


        display_element.innerHTML = html;
        reveal_sentence();

        document.querySelector('#jspsych-sentence-reveal-btngroup').style.visibility = 'hidden';
        show_continue_button();

        // Add a sound file to play automatically.
        //display_element.append('<audio autoplay=true><source src="sounds/dinner.mp3" type="audio/mpeg"/><source src="sounds/dinner.ogg" type="audio/ogg"/></audio>');

        // add event listeners to buttons
        display_element.querySelector('#jspsych-sentence-reveal-continue')
            .addEventListener('click', function (e) {
                after_response("continue");
            });

        // store response
        var response = {
            rt: -1,
            rt_firstReact: -1,
            button: [],
            correct: true
        };

        // start time
        var start_time = 0;

        // reveals the next sentence
        function reveal_sentence() {
            var sentence = sentences[sentence_index];
            if (sentence_index < sentences.length - 1) sentence += ".";

            document.querySelector('#jspsych-sentence-reveal-statement')
                .innerHTML += ('<p class="block-center">' + sentence + "</p>");
        }

        // function to handle responses by the subject
        function after_response(choice) {

            // measure the response time.
            var end_time = Date.now();
            var rt = end_time - start_time;
            response.button.push(choice);
            response.rt = rt;
            if (response.rt_firstReact = -1) {
                response.rt_firstReact = rt
            }
            ;

            document.querySelector('#jspsych-sentence-reveal-btngroup').style.visibility = 'hidden';

            if (sentence_index < sentences.length - 1) {
                sentence_index++;
                reveal_sentence();
            } else {
                sentence_index++;
            }

            if (sentence_index == sentences.length) {
                end_trial();
            } else {
                show_continue_button();
            }


        }

        // Shows the continue button, but only after a pause based of 10ms for each word
        // in the currently displayed sentence.
        function show_continue_button() {
            var timing_fixation = sentences[sentence_index].split(' ').length * 100;
            setTimeout(function () {
                document.querySelector('#jspsych-sentence-reveal-btngroup').style.visibility = 'visible';
            }, timing_fixation);
        }

        // function to end trial when it is time
        function end_trial() {

            // kill any remaining setTimeout handlers
            jsPsych.pluginAPI.clearAllTimeouts();

            // gather the data to store for the trial
            var trial_data = {
                "rt": response.rt,
                "stimulus": trial.paragraph
            };

            console.log(trial_data);
            if (trial.clear_display)
                display_element.html('');

            // move on to the next trial, but give folks a few seconds to see the final sentence
            jsPsych.finishTrial(trial_data);

        }

        // start timing
        start_time = Date.now();
    };


    return plugin;
})();
