$(document).ready(function() {

    $('#password').keyup(function() {
        // set password variable
        var pswd = $(this).val();

        //validate the length
        if ( pswd.length < 8) {
            $('#length').removeClass('valid').addClass('invalid');
        } else {
            $('#length').removeClass('invalid').addClass('valid');
        }

        //validate the symbol
        if (pswd.match(/[!#\$@_'\+,\?\[\]\.\- ]/)) {
            $('#symbol').removeClass('invalid').addClass('valid');
        } else {
            $('#symbol').removeClass('valid').addClass('invalid');
        }

        //validate letter
        if ( pswd.match(/[a-z]/) ) {
            $('#letter').removeClass('invalid').addClass('valid');
        } else {
            $('#letter').removeClass('valid').addClass('invalid');
        }

        //validate capital letter
        if ( pswd.match(/[A-Z]/) ) {
            $('#capital').removeClass('invalid').addClass('valid');
        } else {
            $('#capital').removeClass('valid').addClass('invalid');
        }

        //validate number
        if ( pswd.match(/\d/) ) {
            $('#number').removeClass('invalid').addClass('valid');
        } else {
            $('#number').removeClass('valid').addClass('invalid');
        }

    }).focus(function() {
        $('#pswd_info').show();
    }).blur(function() {
        $('#pswd_info').hide();
    });

});