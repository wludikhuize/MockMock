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

    $('.refreshLink a.refresh').click(function (event) {
        var newValue = !getAutoRefresh();
        setAutoRefresh(newValue);
        setRefreshLinkText(newValue);
        event.preventDefault();
    }).text(getAutoRefresh() ? 'Disable refresh' : 'Enable refresh');

    function setRefreshLinkText(enabled) {
        var text = enabled ? 'Disable refresh' : 'Enable refresh';
        $('.refreshLink a.refresh').text(text);
    }

    setRefreshLinkText(getAutoRefresh());

    function checkForNewMail() {
        function checkRefresh() {
            $.ajax({
                type: "POST",
                url: "/mail/refresh"
            }).done(function (hashcode) {
                if (currentHashcode != hashcode) {
                    currentHashcode = hashcode;
                    window.location = '/';
                }
            });
        }

        checkRefresh();
        return setInterval(checkRefresh, 500);
    }

    var intervalCanceller = null;

    function setAutoRefresh(value) {
        window.docCookies.setItem('autoRefresh', (value ? 'true' : 'false'));

        if (value) {
            checkForNewMail();
        } else {
            clearInterval(intervalCanceller);
        }
    }

    function getAutoRefresh() {
        var value = window.docCookies.getItem('autoRefresh');
        var autorefresh = value != 'false';
        return autorefresh;
    }

    if (window.location.pathname === "/" && getAutoRefresh()) {
        intervalCanceller = checkForNewMail();
    }
});