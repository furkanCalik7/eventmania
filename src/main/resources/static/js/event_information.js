var buy_ticket = document.getElementById("buy_ticket");

buy_ticket.addEventListener('click', function (e) {
    e.preventDefault();
    // get the event id from the url from http://localhost:8080/event/1/info
    var url = window.location.href;
    var url_ = url.split("/");
    var event_id = url_[url_.length - 2];


    $.ajax({
        type: "GET",
        url: `/event/${event_id}/category`,
        success: function (response) {
            console.log(response);
            $("#modal").replaceWith(response);
            var myModal = new bootstrap.Modal(document.getElementById('modal-1'), {
                keyboard: false
            })
            myModal.show();
        }
    })
});

