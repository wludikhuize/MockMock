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

    function checkForNewMail() {
        function checkRefresh() {
            $.ajax({
                type: "POST",
                url: "/mail/refresh"
            }).done(function (timestamp) {
                if (previousTimestamp != timestamp) {
                    previousTimestamp = timestamp;
                    window.location = '/';
                }
            });
        }

        setInterval(checkRefresh, 500);

        checkRefresh();
    }

    if (window.location.pathname === "/") {
        checkForNewMail();
    }
});