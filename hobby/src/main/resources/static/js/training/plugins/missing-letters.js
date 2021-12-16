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

    plugin.info = {
        name: 'missing-letters',
        description: '',
        parameters: {
            phrase: {
                type: jsPsych.plugins.parameterType.STRING,
                pretty_name: 'Phrase',
                default: undefined,
                description: 'The from which to remove letters'
            },
            letters_to_remove: {
                type: jsPsych.plugins.parameterType.INT,
                pretty_name: 'Number of letters to remove',
                default: 1,
                description: 'Number of letters to remove from word, for users to fill in'
            },
            button_html: {
                type: jsPsych.plugins.parameterType.STRING,
                pretty_name: 'Button html',
                default: '<button class="jspsych-btn">%choice%</button>',
                array: true,
                description: 'The html of the button. Can create own style.'
            }
        }
    }


    plugin.trial = function (display_element, trial) {
        // remove random letters from the term.
        var term;
        var missing_letters;
        var letter_index = 0;
        [term, missing_letters] = remove_random_letters(trial.phrase, trial.letters_to_remove);

        var html = '<div id="jspsych-missing-letters-letter", class="center-content block-center">' + term + "</div>";
        html += '<div id="jspsych-missing-letters-btngroup", class="center-content block-center"></div>';
        html += '</div>';
        display_element.innerHTML = html;

        show_letter_options(missing_letters[letter_index]);

        // store response
        var response = {
            rt: -1,
            rt_firstReact: -1,
            button: [],
            correct: true
        };

        // start time
        var start_time = 0;

        // Creates a row of 4 buttons, one is the letter the participant should select, the reset are randomly
        // selected letters.
        function show_letter_options(letter) {
            group = document.querySelector('#jspsych-missing-letters-btngroup');
            var options = fake_letter(letter);
            var choices = shuffle([letter, options[0], options[1], options[2]]);

            var html = "";
            for (var i = 0; i < choices.length; i++) {
                var str = trial.button_html.replace(/%choice%/g, choices[i]);
                html += '<div class="jspsych-html-button-response-button" style="display: inline-block; margin:' + trial.margin_vertical + ' ' + trial.margin_horizontal + '" id="jspsych-html-button-response-button-' + i + '" data-choice="' + choices[i] + '">' + str + '</div>';
            }
            group.innerHTML = html;
            for (var i = 0; i < choices.length; i++) {
                group.querySelector('#jspsych-html-button-response-button-' + i).addEventListener('click', function (e) {
                    var choice = $('#' + this.id).data('choice');
                    after_response(choice, this.id);
                });
            }
        }


        var pause_click = false;
        // function to handle responses by the subject
        function after_response(choice, id) {
            if(pause_click) return;
            pause_click = true;

            // measure the response time.
            var end_time = Date.now();
            var rt = end_time - start_time;
            if (response.button != "") response.button += ",";
            response.button += choice;
            if (response.rt_firstReact == -1) {
                response.rt_firstReact = rt
            }

            response.rt = rt;

            if (missing_letters[letter_index] != choice) {
                response.correct = false;
                display_element.querySelector('#' + id).querySelector('button').classList.add("incorrect_letter");
                pause_click = false;
                return;
            } else {
                display_element.querySelector('#' + id).querySelector('button').classList.add("correct_letter");
            }

            $('#jspsych-missing-letters-letter').html(function () {
                return $(this).html().replace('[&nbsp;]', "[" + choice + "]");
            });



            // Pause for a 1/2 second to show that the letter is correctly entered,
            // then end the trail.
            setTimeout(function () {
                if (letter_index + 1 < missing_letters.length) {
                    letter_index++;
                    pause_click = false;
                    show_letter_options(missing_letters[letter_index]);

                } else {
                    end_trial();
                }
            }, 500);

        }


        // function to end trial when it is time
        function end_trial() {

            // kill any remaining setTimeout handlers
            jsPsych.pluginAPI.clearAllTimeouts();

            // gather the data to store for the trial
            var trial_data = {
                "rt": response.rt,
                "rt_firstReact": response.rt_firstReact,
                "stimulus": trial.phrase,
                "button_pressed": response.button,
                "correct": response.correct

            };
            // clear the display
            display_element.innerHTML = '';

            // move on to the next trial
            jsPsych.finishTrial(trial_data);
        }


        // start timing
        start_time = Date.now();

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
            return [str.substring(0, pos) + (Array(amount + 1).join("[&nbsp;]")) + str.substring(pos + amount), letters.split('')];
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


    return plugin;
})
();
