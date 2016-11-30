
// For some reason IPhones are not validating form submissions.
// To get around this, mobile forms should call "submitMobileForm" method below
// which will call the form.valid() method then submit the form.  This should
// also assure that the invalidHandles will correctly scroll to the problem.
// Your submit should look like this:
// <button onclick="submitMobileForm()">Next</button>


$("form").validate({
    onsubmit: false,
    invalidHandler: function(e, validator) {
        if(window.navigator.userAgent.match(/iphone/i)) {
            window.setTimeout(function() {
                $("html,body").animate({
                    scrollTop: $(validator.errorList[0].element).offset().top
                });
            }, 0);
        }
    }
});

function submitMobileForm() {
    if( $("form").valid() ){
        form.submit()
    }
}