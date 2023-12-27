/* eslint-disable */

$(document).ready(function () {

    $(".submit").click(function () {
        return false;
    })

})

export function setProgressBar(currentStep, action, currentGfgStep, nextGfgStep, previousGfgStep, myform) {

    var opacity;

    if (action == 'default') {


        var fieldsets = $(myform).find("fieldset");
        fieldsets.each(function (index) {
            if (index == 0) {
                $(this).css({'opacity': 1, 'display': '', 'position': ''});
                $(".progressbarregistrations li").eq($("fieldset").index($(this))).addClass("active");
            } else {
                $(this).css({'display': 'none', 'position': 'relative'});
                $(".progressbarregistrations li").eq($("fieldset").index($(this))).removeClass("active");
            }

            currentStep = 1;
        });

    } else if (action == 'next') {


        var isvalid = validateForm();

        if (isvalid) {


            $(".progressbarregistrations li").eq($("fieldset")
                .index(nextGfgStep)).addClass("active");

            nextGfgStep.show();
            currentGfgStep.animate({opacity: 0}, {
                step: function (now) {
                    opacity = 1 - now;

                    currentGfgStep.css({
                        'display': 'none',
                        'position': 'relative'
                    });
                    nextGfgStep.css({'opacity': opacity});
                },
                duration: 500
            });


        } else {

            console.log('invalid fields');
        }

    } else {


        $(".progressbarregistrations li").eq($("fieldset").index(currentGfgStep)).removeClass("active");

        previousGfgStep.show();

        currentGfgStep.animate({opacity: 0}, {
            step: function (now) {
                opacity = 1 - now;

                currentGfgStep.css({
                    'display': 'none',
                    'position': 'relative'
                });
                previousGfgStep.css({'opacity': opacity});
            },
            duration: 500
        });


    }


    var fieldsets = $(myform).find("fieldset");
    var count = 0;
    var steps = 0;
    fieldsets.each(function (index) {
        if ($(".progressbarregistrations li").eq($("fieldset").index($(this))).hasClass("active")) {
            ++count;
        }
        ++steps;
    });


    var percent = parseFloat(125 / steps) * count;
    percent = percent.toFixed();
    $(".progress-bar-registrations").css("width", percent + "%")


}

function validateForm() {
    var required = $('input,textarea,select').filter('[required]:visible');
    var allRequired = true;

    required.each(function () {
        if ($(this).is('select')) {
            if ($(this).val() === null || $(this).val() === '') {
                allRequired = false;
                $(this)[0].setCustomValidity('Please select an option.');
            } else {
                $(this)[0].setCustomValidity(''); // Reset any previous custom validity
            }
        } else if ($(this).val() === '' || $(this).val() === null || $(this).val() === undefined) {
            allRequired = false;
            $(this)[0].setCustomValidity('This field is required.');
        } else {
            $(this)[0].setCustomValidity(''); // Reset any previous custom validity
        }
    });

    return allRequired;
}
