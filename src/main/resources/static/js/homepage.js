var form = document.getElementById('filter-button');

form.addEventListener('click', function (e) {
    e.preventDefault();
    // get the checkbox values and put them an array
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
            "name": $("#search_filter").val()
        },
        success: function (response) {

        }
    })
});

