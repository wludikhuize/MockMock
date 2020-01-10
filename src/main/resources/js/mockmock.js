$(function () {

    $('.deleteLink a.delete').click(function (event) {
        $.ajax({
            type: "POST",
            url: "/mail/delete/all"
        }).done(function (msg) {
            window.location = '/';
        });

        event.preventDefault();
    });

    var previousTimestamp = null;

    function checkForNewMail() {
        setInterval(function () {
            $.ajax({
                type: "POST",
                url: "/mail/refresh"
            }).done(function (timestamp) {
                if (previousTimestamp == null) {
                    previousTimestamp = timestamp;
                } else if (previousTimestamp < timestamp) {
                    previousTimestamp = timestamp;
                    window.location = '/';
                }
            });
        }, 100);
    }

    if (window.location.pathname === "/") {
        checkForNewMail();
    }
});