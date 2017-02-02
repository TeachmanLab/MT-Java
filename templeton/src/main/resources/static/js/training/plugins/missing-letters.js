/*
 * missing-letter
 * Dan Funk
 *
 * plugin displays a phrase with one or more missing letters.
 * The participant is prompted to guess the correct missing letter
 * by selecting from a 4 possible choices.
 */

jsPsych.plugins["missing-letters"] = (function () {

    var plugin = {};

    plugin.trial = function (display_element, trial) {

        // set default values for parameters
        trial.phrase = trial.phrase || 'final phrase';
        trial.letters_to_remove = trial.letters_to_remove || 1;
        trial.button_html = trial.button_html || '<button class="jspsych-btn">%choice%</button>';

        // if any trial variables are functions
        // this evaluates the function and replaces
        // it with the output of the function
        trial = jsPsych.pluginAPI.evaluateFunctionParameters(trial);

        // this array holds handlers from setTimeout calls
        // that need to be cleared if the trial ends early
        var setTimeoutHandlers = [];

        // remove random letters from the term.
        var term;
        var missing_letters;
        var letter_index = 0;
        [term, missing_letters] = remove_random_letters(trial.phrase, trial.letters_to_remove);

        // Display the term
        display_element.append($('<div>', {
                html: term,
                id: 'jspsych-missing-letters-letter',
                class: 'center-content block-center'
        }));

        // Show letters for completing the term
        display_element.append('<div id="jspsych-missing-letters-btngroup" class="center-content block-center"></div>');
        show_letter_options(missing_letters[letter_index]);

        // store response
        var response = {
            rt: -1,
            button: [],
            correct: true
        };

        // start time
        var start_time = 0;

        // Creates a row of 4 buttons, one is the letter the participant should select, the reset are randomly
        // selected letters.
        function show_letter_options(letter) {
            $('#jspsych-missing-letters-btngroup').empty();
            var options = fake_letter(letter);
            var choices = shuffle([letter, options[0], options[1], options[2]]);

            for (var i = 0; i < choices.length; i++) {
                var str = trial.button_html.replace(/%choice%/g, choices[i]);
                $('#jspsych-missing-letters-btngroup').append(
                    $(str).attr('id', 'jspsych-button-response-button-' + i).data('choice', choices[i]).addClass('jspsych-button-response-button').on('click', function (e) {
                        var choice = $('#' + this.id).data('choice');
                        after_response(choice, this.id);
                    })
                );
            }
        }

        // function to handle responses by the subject
        function after_response(choice, id) {

            // measure the response time.
            var end_time = Date.now();
            var rt = end_time - start_time;
            response.button.push(choice);
            response.rt = rt;

            if (missing_letters[letter_index] != choice) {
                response.correct = false;
                $('#' + id).addClass("incorrect");
                return;
            }

            $('#jspsych-missing-letters-letter').html(function () {
                return $(this).html().replace('[&nbsp;]', "[" + choice + "]");
            });

            if (letter_index + 1 < missing_letters.length) {
                letter_index++;
                show_letter_options(missing_letters[letter_index]);
                return;
            }

            // after a valid response, the stimulus will have the CSS class 'responded'
            // which can be used to provide visual feedback that a response was recorded
            $("#jspsych-button-response-stimulus").addClass('responded');

            // disable all the buttons after a response
            $('.jspsych-button-response-button').off('click').attr('disabled', 'disabled');

            // Pause for a 1/2 second to show that the letter is correctly entered,
            // then end the trail.
            setTimeout(function() {
                end_trial();
            }, 500);
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
                "stimulus": trial.term,
                "button_pressed": response.button,
                "correct": response.correct

            };
            console.log(trial_data);
            // clear the display
            display_element.html('');

            // move on to the next trial
            jsPsych.finishTrial(trial_data);
        };


        // *******************************
        // General functions to make this a bit easier to read.
        // *******************************


        // this is the function to remove N number of random letters
        // returns an array containing the original string, followed by
        // a 
        // ie. given 'capital',1 returns ['ca[ ]ital', ['p']]
        // ie. given 'animal',2 returns ['a[ ][ ]mal', ['n','i']]
        function remove_random_letters(str, amount) {
            if (str == null) return ["", ""];

            var letters = [];
            var pos = 0;
            var tries = 0;
            // select a value that is a word character, not a space or punctuation
            // And don't select the first letter in the phase, And Don't try to look forever.
            while (letters == "") {
                tries++;
                // Pick a random position in the string, greater than 0
                pos = Math.floor(Math.random() * (str.length - 1)) + 1;
                if (pos == 0) continue;
                // Assure that the position begins at a series of characters long
                // enough to support the total number of letters to remove.
                var testLetters = str.substring(pos, pos + amount);
                var re = new RegExp("^\\w{" + amount + "}$");
                if (re.test(testLetters)) {
                    letters = testLetters;
                }

                // Reduce the number of letters examined if we can't find a long enough string.
                if (tries > str.length * 10) {
                    amount--;
                    tries = 0;
                }
                if (amount == 0) break;
            }
            return [str.substring(0, pos) + "[&nbsp;]".repeat(amount) + str.substring(pos + amount), letters.split('')];
        }

        // This is the function to create non-repeated counter options
        // for missing letters
        function fake_letter(answer) {
            // the possible letters to choose from
            var possible = "abcdefghijklmnopqrstuvwxyz";
            var exAns = [answer, answer, answer];
            var i = 0;
            while (exAns.indexOf(answer) >= 0) {
                t = possible.charAt(Math.floor(Math.random() * possible.length));
                if (exAns.indexOf(t) < 0) {
                    exAns[i] = t;
                    i += 1;
                }
            }
            return exAns;
        }

        // Randomizes an array.
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

    };

    // start timing
    start_time = Date.now();

    return plugin;
})();
