
$(document).ready(function() {

    // Actions to be written to the database
    action_sequence = []

    // Need to fix so it will also work with slider vals
    $("input").change(function() {
        action_sequence.push({'name': $(this).attr("name"), 'time': Date.now()});
    });

    $(document).on('submit', 'form', function() {
        $.ajax({
            type: "POST",
            url: '/action/average',
            data: JSON.stringify(action_sequence),
            success: success, // Add success function
            dataType: 'json'
        });
        $.ajax({
            type: "POST",
            url: '/action/all',
            data: JSON.stringify(action_sequence),
            success: success, // Add success function
            dataType: 'json'
        });
    });
});
