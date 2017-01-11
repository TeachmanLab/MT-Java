/**
 * Given a series of <section> tags that contain radio buttons,
 * scross to the next available section automatically when the
 * radio button is selected.
 */

function scrollTo(elem){console.log(elem)
    $('html,body').animate({
        scrollTop: elem.offset().top},'slow');
}

$(document).on('click', '.option', function(e){
    var row = $(this.closest(".row"));
    var section = $(this).closest(".section");
    var allChecked =
        ($(section).find('.row').filter(':has(:radio)').length ===
         $(section).find('.row').filter(':has(:radio:checked)').length);

    if(allChecked) {
        scrollTo($(section).next());
    } else if($(row).next().hasClass("row")) {
        if($(this).closest(".section").hasClass("section-V"))
        scrollTo($(this).closest(".row").next());
    }
});

/*
$(document).on('click', '.scrollNextSection', function(e){
    scrollTo($(this).closest(".section").next());
});
*/





