/**
 *
 * plugin based on the button-response, but requires a correct answer
 * in order to proceed.
 *
 * documentation: docs.jspsych.org
 *
 **/

jsPsych.plugins["button-response-correct"] = (function () {

    var plugin = {};

    plugin.info = {
        name: 'html-button-response',
        description: '',
        parameters: {
            stimulus: {
                type: jsPsych.plugins.parameterType.HTML_STRING,
                pretty_name: 'Stimulus',
                default: undefined,
                description: 'The HTML string to be displayed'
            },
            choices: {
                type: jsPsych.plugins.parameterType.KEYCODE,
                pretty_name: 'Choices',
                default: [],
                array: true,
                description: 'The labels for the buttons.'
            },
            button_html: {
                type: jsPsych.plugins.parameterType.STRING,
                pretty_name: 'Button html',
                default: '<button class="jspsych-btn">%choice%</button>',
                array: true,
                description: 'The html of the button. Can create own style.'
            },
            prompt: {
                type: jsPsych.plugins.parameterType.STRING,
                pretty_name: 'Prompt',
                default: null,
                description: 'Any content here will be displayed under the button.'
            },
            response_ends_trial: {
                type: jsPsych.plugins.parameterType.BOOL,
                pretty_name: 'Response ends trial',
                default: true,
                description: 'If true, then trial will end when user responds.'
            },
            correct_choice: {
                type: jsPsych.plugins.parameterType.STRING,
                pretty_name: 'The Correct Response',
                default: null,
                description: 'How the user should respond'
            },
            correct_message: {
                type: jsPsych.plugins.parameterType.STRING,
                pretty_name: 'Correct Message',
                default: 'Great Job!',
                description: 'What to display after a correct response'
            },
            incorrect_message: {
                type: jsPsych.plugins.parameterType.STRING,
                pretty_name: 'Incorrect Message',
                default: 'That response is incorrect, in a moment you will have a chance to respond again.',
                description: 'What to display after an incorrect response'
            },
            delay: {
                type: jsPsych.plugins.parameterType.STRING,
                pretty_name: 'Incorrect Delay',
                default: 6000,
                description: 'Number of MS to wait if incorrect response provided, before user can try again.'
            },

        }
    }

    plugin.trial = function (display_element, trial) {


        // display stimulus
        var html = '<div  class="block-center center-content" ' +
            'id="jspsych-html-button-response-stimulus">' + trial.stimulus + '</div>';

        html += '<div class="block-center center-content error" ' +
            'id="jspsych-html-button-response-incorrect" style="visibility: hidden">' + trial.incorrect_message + '</div>';

        html += '<div class="block-center center-content correct" ' +
            'id="jspsych-html-button-response-correct" style="visibility: hidden">' + trial.correct_message + '</div>'

        //display buttons
        var buttons = [];
        if (Array.isArray(trial.button_html)) {
            if (trial.button_html.length == trial.choices.length) {
                buttons = trial.button_html;
            } else {
                console.error('Error in button-response plugin. The length of the button_html array does not equal the length of the choices array');
            }
        } else {
            for (var i = 0; i < trial.choices.length; i++) {
                buttons.push(trial.button_html);
            }
        }

        html += '<div class="block-center center-content" id="jspsych-html-button-response-btngroup">';
        for (var i = 0; i < trial.choices.length; i++) {
            var str = buttons[i].replace(/%choice%/g, trial.choices[i]);
            html += '<div class="jspsych-html-button-response-button" style="display: inline-block; margin:' + trial.margin_vertical + ' ' + trial.margin_horizontal + '" id="jspsych-html-button-response-button-' + i + '" data-choice="' + i + '">' + str + '</div>';
        }
        html += '</div>';

        //show prompt if there is one
        if (trial.prompt !== null) {
            html += trial.prompt
        }

        display_element.innerHTML = html;

        // start time
        var start_time = 0;

        // add event listeners to buttons
        for (var i = 0; i < trial.choices.length; i++) {
            display_element.querySelector('#jspsych-html-button-response-button-' + i).addEventListener('click', function(e){
                var choice = e.currentTarget.getAttribute('data-choice'); // don't use dataset for jsdom compatibility
                after_response(choice);
            });
        }

        // Response Defaults.  If incorrect answer is
        var response = {
            rt: -1,
            button: -1,
            correct: true,
            rt_firstReact: -1
        };


        // function to handle responses by the subject
        function after_response(choice) {

            // measure rt
            var end_time = Date.now();
            var rt = end_time - start_time;
            response.button = choice;
            if (response.rt_firstReact == -1) {
                response.rt_firstReact = rt
            }
            ;
            response.rt = rt;

            // If the response is not correct, force them to pause.
            if (trial.choices[choice] != trial.correct_choice) {
                handle_incorrect();
            } else {
                handle_correct();
            }
        }

        // Deals with an incorrect response, by hiding the buttons, showing an error message
        // forcing a pause of 2 seconds, then redisplaying the buttons.
        function handle_incorrect() {
            response.correct = false;
            document.querySelector("#jspsych-html-button-response-incorrect").style.visibility = 'visible';
            document.querySelector("#jspsych-html-button-response-btngroup").style.visibility = 'hidden';

            setTimeout(function () {
                document.querySelector("#jspsych-html-button-response-incorrect").style.visibility = 'hidden';
                document.querySelector("#jspsych-html-button-response-btngroup").style.visibility = 'visible';
            }, trial.delay);

        }

        // Shows a good job message and does a brief pause.
        function handle_correct() {
            document.querySelector("#jspsych-html-button-response-correct").style.visibility = 'visible';
            document.querySelector("#jspsych-html-button-response-btngroup").style.visibility = 'hidden';

            setTimeout(function () {
                // after a valid response, the stimulus will have the CSS class 'responded'
                // which can be used to provide visual feedback that a response was recorded
                document.querySelector("#jspsych-html-button-response-stimulus").classList.add('responded');

                // disable all the buttons after a response
                var btns = document.querySelectorAll('.jspsych-html-button-response-button button');
                for(var i=0; i<btns.length; i++){
                    //btns[i].removeEventListener('click');
                    btns[i].setAttribute('disabled', 'disabled');
                }

                if (trial.response_ends_trial) {
                    end_trial();
                }
            }, 1200);

        }


        // function to end trial when it is time
        function end_trial() {

            // kill any remaining setTimeout handlers
            jsPsych.pluginAPI.clearAllTimeouts();

            // gather the data to store for the trial
            var trial_data = {
                "rt": response.rt,
                "rt_firstReact": response.rt_firstReact,
                "stimulus": trial.stimulus,
                "button_pressed": response.button,
                "correct": response.correct
            };

            // clear the display
            display_element.innerHTML = '';

            // move on to the next trial
            jsPsych.finishTrial(trial_data);
        };

        // start timing
        start_time = Date.now();

    };

    return plugin;
})();
