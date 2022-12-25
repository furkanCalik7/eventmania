let autocomplete;
let address1Field;


function initAutocomplete() {
    address1Field = document.querySelector("#venue-input");
    // Create the autocomplete object, restricting the search predictions to
    // addresses in the US and Canada.
    autocomplete = new google.maps.places.Autocomplete(address1Field, {
        componentRestrictions: {country: ["us", "tr"]}, fields: ["address_components", "geometry"], types: ["address"],
    });
    // When the user selects an address from the drop-down, populate the
    // address fields in the form.
    autocomplete.addListener("place_changed", fillInAddress);
}

function fillInAddress() {
    const place = autocomplete.getPlace();
    console.log(place);
    $("#lati-input").val(place.geometry.location.lat());
    $("#long-input").val(place.geometry.location.lng());
    let address = document.querySelector("#address-input");
    let state = document.querySelector("#state-input");
    let city = document.querySelector("#city-input");
    let postalcode = document.querySelector("#postal-input");
    let country = document.querySelector("#country-input");

    for (const component of place.address_components) {
        // @ts-ignore remove once typings fixed
        const componentType = component.types[0];

        switch (componentType) {
            case "route": {
                address.value = component.short_name;
                break;
            }
            case "postal_code": {
                postalcode = component.long_name;
                break;
            }
            case "administrative_area_level_1": {
                city.value = component.short_name;
                break;
            }
            case "administrative_area_level_4": {
                address.value = `${component.long_name} mah., ${address.value}`;
                break;
            }
            case "administrative_area_level_2": {
                state.value = component.short_name;
                break;
            }
            case "country":
                country.value = component.long_name;
                break;
        }
    }
}

google.maps.event.addDomListener(window, 'load', initAutocomplete);
$("#online-event-span").hide();
$("#is-online-input").val("VENUE");
$("#venue-input").attr("required", true);
$("#venue-button").addClass("btn-info");
$("#online-button").click(function () {
    $("#online-button").addClass("btn-info");
    $("#venue-button").removeClass("btn-info");
    $("#online-event-span").show();
    $("#location_input").hide();
    // make venue input not required
    $("#venue-input").attr("required", false);
    $("#location-details-page").hide();
    $("#is-online-input").val("ONLINE");
});

$("#venue-button").click(function () {
    $(this).addClass("btn-info");
    $("#online-event-span").hide();
    $("#online-button").removeClass("btn-info");
    $("#location_input").show();
    $("#venue-input").attr("required", true);
    $("#location-details-page").show();
    $("#is-online-input").val("VENUE");
});

$("#free-button").addClass("btn-info");
$("#capacity-input").show();
$("#sales-channel-input").hide();
$("#free-button").click(function () {
    $("#free-button").addClass("btn-info");
    $("#paid-button").removeClass("btn-info");
    $("#capacity-input").show();
    $("#event-payment-input").val("FREE");
    $("#sales-channel-input").hide();
});

$("#paid-button").click(function () {
    $("#paid-button").addClass("btn-info");
    $("#free-button").removeClass("btn-info");
    $("#sales-channel-input").show();
    $("#capacity-input").hide();
    $("#event-payment-input").val("PAID");
});