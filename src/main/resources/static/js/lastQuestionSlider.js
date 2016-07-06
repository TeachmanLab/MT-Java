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