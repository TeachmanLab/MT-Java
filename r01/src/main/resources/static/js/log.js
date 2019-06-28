
$(document).ready(function() {

    // Actions to be written to the database
    action_sequence = []

    // Need to fix so it will also work with slider vals
    $("input").change(function() {
        action_sequence.push({'name': $(this).attr("name"), 'time': Date.now()})
        $('#actionSequence').val(JSON.stringify(action_sequence));
    });
    
});