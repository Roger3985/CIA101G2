$(document).ready(function() {
    $('.nav-item').hover(function() {
        $(this).find('.collapse').collapse('show');
    }, function() {
        $(this).find('.collapse').collapse('hide');
    });
});