/*
 * missing-letter
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

    plugin.trial = function (display_element, trial) {

        // set default values for parameters
        trial.paragraph = trial.paragraph || 'This is a paragraph. It has multiple sentences. ';
        trial.button_html = trial.button_html || '<button class="jspsych-btn">%choice%</button>';
        trial.clear_display = (typeof trial.clear_display === 'undefined') ? false : trial.response_ends_trial;
        trial.button_text = trial.button_text || 'continue ...';

        // if any trial variables are functions
        // this evaluates the function and replaces
        // it with the output of the function
        trial = jsPsych.pluginAPI.evaluateFunctionParameters(trial);

        // this array holds handlers from setTimeout calls
        // that need to be cleared if the trial ends early
        var setTimeoutHandlers = [];

        var sentences = trial.paragraph.split(".");
        var sentence_index = 0;

        display_element.append('<div id="jspsych-sentence-reveal-statement" class="center-content block-center"></div>');
        reveal_sentence();

        // Display the continue button.
        display_element.append('<div id="jspsych-sentence-reveal-btngroup" class="center-content block-center"></div>');
        var continue_button = trial.button_html.replace(/%choice%/g, trial.button_text);
        $('#jspsych-sentence-reveal-btngroup').append(
            $(continue_button).attr('id', 'jspsych-sentence-reveal-continue').addClass('jspsych-button-response-button').on('click', function (e) {
                after_response("continue");
            })
        );
        $('#jspsych-sentence-reveal-btngroup').hide();
        show_continue_button();

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

            $('#jspsych-sentence-reveal-statement')
                .append($('<p>', {
                    html: sentence,
                    class: 'block-center'
                }));
        }

        // function to handle responses by the subject
        function after_response(choice) {

            // measure the response time.
            var end_time = Date.now();
            var rt = end_time - start_time;
            response.button.push(choice);
            response.rt = rt;

            $('#jspsych-sentence-reveal-btngroup').hide();

            if (sentence_index < sentences.length - 1) {
                sentence_index++;
                reveal_sentence();
            }

            if (sentence_index == sentences.length - 1) {
                end_trial();
            } else {
                show_continue_button();
            }
        }

        // Shows the continue button, but only after a pause based of 10ms for each word
        // in the currently displayed sentence.
        function show_continue_button() {
            var timing_fixation = sentences[sentence_index].split(' ').length * 100;
            setTimeout(function() {
                $('#jspsych-sentence-reveal-btngroup').show();
            }, timing_fixation);
        }

        // function to end trial when it is time
        function end_trial() {

            // kill any remaining setTimeout handlers
            for (var i = 0; i < setTimeoutHandlers.length; i++) {
                clearTimeout(setTimeoutHandlers[i]);
            }

            // gather the data to store for the trial
            var trial_data = {
                "rt": response.rt,
                "stimulus": trial.paragraph
            };

            console.log(trial_data);
            if (trial.clear_display)
                display_element.html('');

            // move on to the next trial
            jsPsych.finishTrial(trial_data);
        }

        // start timing
        start_time = Date.now();
    };



    return plugin;
})();