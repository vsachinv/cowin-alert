$(function () {
    var apiUrl = 'https://cdn-api.co-vin.in/api/v2/admin/location/districts/:parentId:';
    $("#districtId").prop('disabled', true);
    $('#state').on('change', function () {
        $("#districtId").html('');
        $.getJSON(apiUrl.replace(':parentId:', $(this).val()), function (data) {
            var items = data.districts;
            if (items.length > 0) {
                var newOptions = '<option value="">-- Select --</option>';
                for (var i in items) {
                    newOptions += '<option value="' + items[i].district_id + '">' + items[i].district_name + '</option>';
                }
                $("#districtId").html(newOptions).prop("disabled", false)
            } else {
                $("#districtId").prop('disabled', true);
            }
        });
    });
});