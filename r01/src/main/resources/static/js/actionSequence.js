
$(document).ready(function() {

    // Actions to be written to the database
    action_sequence = []

    // Need to fix so it will also work with slider vals
    $("input").change(function() {
        action_sequence.push({'name': $(this).attr("name"), 'time': Date.now()});
    });

    $(document).on('submit', 'form', function() {
        url = $("#formName").val()
        $.ajax({
            type: "POST",
            url: '/action',
            data: JSON.stringify(action_sequence),
            success: success, // Add success function
            dataType: 'json'
        });
    });
});
