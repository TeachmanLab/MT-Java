/**
 *
 * plugin based on the button-response, but requires a correct answer
 * in order to proceed.
 *
 * documentation: docs.jspsych.org
 *
 **/

jsPsych.plugins["button-response-correct"] = (function() {

  var plugin = {};

  plugin.trial = function(display_element, trial) {

    // default trial parameters
    trial.button_html = trial.button_html || '<button class="jspsych-btn">%choice%</button>';
    trial.response_ends_trial = (typeof trial.response_ends_trial === 'undefined') ? true : trial.response_ends_trial;
    trial.timing_stim = trial.timing_stim || -1; // if -1, then show indefinitely
    trial.timing_response = trial.timing_response || -1; // if -1, then wait for response forever
    trial.is_html = (typeof trial.is_html === 'undefined') ? false : trial.is_html;
    trial.prompt = (typeof trial.prompt === 'undefined') ? "" : trial.prompt;
    trial.correct_response = trial.correct_choice || '';
    trial.incorrect_message = trial.incorrect_message || 'That response is incorrect. In a moment you will have a chance to respond again.';
    trial.correct_message = trial.correct_message || 'Great job!';
    trial.delay = trial.delay || 6000; // Seconds to delay, if incorrect response is provided.

    // if any trial variables are functions
    // this evaluates the function and replaces
    // it with the output of the function
    trial = jsPsych.pluginAPI.evaluateFunctionParameters(trial);

    // this array holds handlers from setTimeout calls
    // that need to be cleared if the trial ends early
    var setTimeoutHandlers = [];

      // display stimulus
    if (!trial.is_html) {
      display_element.append($('<img>', {
        src: trial.stimulus,
        id: 'jspsych-button-response-stimulus',
        class: 'block-center'
      }));
    } else {
      display_element.append($('<div>', {
        html: trial.stimulus,
        id: 'jspsych-button-response-stimulus',
        class: 'block-center center-content'
      }));
    }

    // establish, but hide the error message.
      display_element.append($('<div>', {
          html: trial.incorrect_message,
          id: 'jspsych-button-response-incorrect',
          class: 'block-center center-content error',
          style: 'display: none'
      }));

    // establish, but hide the error message.
    display_element.append($('<div>', {
      html: trial.correct_message,
      id: 'jspsych-button-response-correct',
      class: 'block-center center-content correct',
      style: 'display: none'
    }));


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
    display_element.append('<div id="jspsych-button-response-btngroup" class="center-content block-center"></div>')
    for (var i = 0; i < trial.choices.length; i++) {
      var str = buttons[i].replace(/%choice%/g, trial.choices[i]);
      $('#jspsych-button-response-btngroup').append(
        $(str).attr('id', 'jspsych-button-response-button-' + i).data('choice', trial.choices[i]).addClass('jspsych-button-response-button').on('click', function(e) {
          var choice = $('#' + this.id).data('choice');
          after_response(choice);
        })
      );
    }

    //show prompt if there is one
    if (trial.prompt !== "") {
      display_element.append(trial.prompt);
    }

    // Response Defaults.  If incorrect answer is
    var response = {
      rt: -1,
      button: -1,
      correct: true,
      rt_firstReact: -1
    };

    // start time
    var start_time = 0;

    // function to handle responses by the subject
    function after_response(choice) {

      // measure rt
      var end_time = Date.now();
      var rt = end_time - start_time;
      response.button = choice;
      if(response.rt_firstReact == -1) {response.rt_firstReact = rt}
      response.rt = rt;

      // If the response is not correct, force them to pause.
      if(choice != trial.correct_choice) {
        handle_incorrect();
      } else {
        handle_correct();
      }
    }

    // Deals with an incorrect response, by hiding the buttons, showing an error message
    // forcing a pause of 2 seconds, then redisplaying the buttons.
    function handle_incorrect() {
      response.correct = false;
      $("#jspsych-button-response-incorrect").show();
      $("#jspsych-button-response-btngroup").hide();

        setTimeout(function() {
            $("#jspsych-button-response-incorrect").hide();
            $("#jspsych-button-response-btngroup").show();
        }, trial.delay);

    }

    // Shows a good job message and does a brief pause.
    function handle_correct() {
      $("#jspsych-button-response-correct").show();
      $("#jspsych-button-response-btngroup").hide();

      setTimeout(function() {
        // after a valid response, the stimulus will have the CSS class 'responded'
        // which can be used to provide visual feedback that a response was recorded
        $("#jspsych-button-response-stimulus").addClass('responded');

        // disable all the buttons after a response
        $('.jspsych-button-response-button').off('click').attr('disabled', 'disabled');

        if (trial.response_ends_trial) {
          end_trial();
        }
      }, 1200);

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
        "rt_firstReact": response.rt_firstReact,
        "stimulus": trial.stimulus,
        "button_pressed": response.button,
        "correct": response.correct
      };

      // clear the display
      display_element.html('');

      // move on to the next trial
      jsPsych.finishTrial(trial_data);
    }

    // start timing
    start_time = Date.now();

    // hide image if timing is set
    if (trial.timing_stim > 0) {
      var t1 = setTimeout(function() {
        $('#jspsych-button-response-stimulus').css('visibility', 'hidden');
      }, trial.timing_stim);
      setTimeoutHandlers.push(t1);
    }

    // end trial if time limit is set
    if (trial.timing_response > 0) {
      var t2 = setTimeout(function() {
        end_trial();
      }, trial.timing_response);
      setTimeoutHandlers.push(t2);
    }

  };

  return plugin;
})();
