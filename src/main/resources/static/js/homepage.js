var form = document.getElementById('filter-button');

$("#show_filter_button").click(function () {
        console.log("text");
        $("#collapseExample").toggle();
    }
)

form.addEventListener('click', function (e) {
    e.preventDefault();
    var checkboxes = document.querySelectorAll('input[type=checkbox]:checked');
    var values = [];
    checkboxes.forEach((checkbox) => {
        values.push(checkbox.value);
    });
    // post request to /filter with ajax and get the response
    $.ajax({
        type: "POST",
        url: "/event/filter",
        data: {
            "eventTypes": values,
            "startDate": $("#startdate_filter").val(),
            "endDate": $("#enddate_filter").val(),
            "name": $("#search_input").val()
        },
        success: function (response) {
            console.log(response);
            $("#events").replaceWith(response);
        }
    })
});

