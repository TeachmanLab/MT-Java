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
    scrollTo($(this).closest(".section").next());
});


/**
 * Change the class associated with section-V and section-H tags
 * when the widow width is moves beyond 414 pixels.
 */
function adapt(){
    if ($(window).width() < 414) {
        $('.section').removeClass('section-H');
        $('.section').addClass('section-V');
    } else {
        $('.section').addClass('section-H');
        $('.section').removeClass('section-V');
    }
}
$(document).ready(adapt);

$(window).resize(adapt);


