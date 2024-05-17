$(document).ready(function() {
    var urlParams = new URLSearchParams(window.location.search);
    var message = urlParams.get('message');
    if (message) {
        $('#modal-message').text(decodeURIComponent(message));
        $('#myModal').modal('show');
    }
});