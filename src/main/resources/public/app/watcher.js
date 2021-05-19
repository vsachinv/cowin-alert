$(function () {
    var timerId = 0;
    $("#jquery_jplayer_1").jPlayer({
        ready: function (event) {
            $(this).jPlayer("setMedia", {
                title: "Bubble",
                m4a: "http://jplayer.org/audio/mp3/Miaow-07-Bubble.mp3",
                oga: "http://jplayer.org/audio/ogg/Miaow-07-Bubble.ogg"
            });
        },
        swfPath: "http://jplayer.org/latest/dist/jplayer",
        supplied: "mp3, oga",
        wmode: "window",
        useStateClassSkin: true,
        autoBlur: false,
        smoothPlayBar: true,
        keyEnabled: true,
        remainingDuration: true,
        toggleDuration: true
    });

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

    $('#cancelWatch').on('click', function () {
        clearInterval(timerId);
        $('#watch').prop('disabled', false);
        $('#result').addClass('invisible');
        $('.flasher').addClass('invisible');
        $('#centers').html('');
        $("#jquery_jplayer_1").jPlayer("stop");
    });

    $('#watch').on('click', function () {
        var districtId = $('#districtId').val();
        var vaccineType = $('input[name=vaccineType]:checked').val();
        var ageLimit = $('input[name=ageLimit]:checked').val();
        var doseType = $('input[name=doseType]:checked').val();
        var refreshInterval = $('input[name=refreshInterval]').val();
        if (!districtId || !vaccineType || !ageLimit || !refreshInterval || !doseType) {
            alert('Please enter input');
            return
        }
        $(this).prop('disabled', true);
        $('#result').removeClass('invisible');
        cowinCoriApiSearch(districtId, vaccineType, ageLimit, doseType);
        timerId = setInterval(function () {
            cowinCoriApiSearch(districtId, vaccineType, ageLimit, doseType);
        }, 1000 * 60 * refreshInterval);

    });

    var currentDate = function () {
        var d = new Date(),
            month = '' + (d.getMonth() + 1),
            day = '' + d.getDate(),
            year = d.getFullYear();

        if (month.length < 2)
            month = '0' + month;
        if (day.length < 2)
            day = '0' + day;

        return [day, month, year].join('-');
    };

    var cowinCoriApiSearch = function (districtId, vaccineType, ageLimit, doseType) {
        var apiUrl = 'https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByDistrict?district_id=:districtId:&date=:DATE:';
        var centersNames = '';
        $.getJSON(apiUrl.replace(':districtId:', districtId).replace(':DATE:', currentDate()), function (data) {
            var newFoundCenters = '';
            $.each(data.centers, function (index, element) {
                var sessions = $.grep(element.sessions, function (it, j) {
                    if ((vaccineType == 'ANY' || it.vaccine == vaccineType) && ((doseType == 'FIRST' && it.available_capacity_dose1 > 2) || (doseType == 'SECOND' && it.available_capacity_dose2 > 2)) && it.min_age_limit == ageLimit) {
                        return true
                    }
                    return false
                });

                if (sessions.length > 0) {
                    newFoundCenters += element.name + '<br/>';
                    $.each(sessions, function (k, sessionObj) {
                        newFoundCenters += sessionObj.date + ' : available ' + (doseType == 'FIRST' ? (sessionObj.available_capacity_dose1) : (sessionObj.available_capacity_dose2)) + '<br/>'
                    });
                }

            });
            if (newFoundCenters) {
                newFoundCenters = '<b>Found At :</b>' + new Date() + '<br/>' + newFoundCenters + '<br/>';
                centersNames = newFoundCenters + centersNames;
            }
            $('#centers').html(centersNames);
            if (newFoundCenters) {
                $('.flasher').removeClass('invisible');
                $("#jquery_jplayer_1").jPlayer("play", 2);
                console.log(newFoundCenters);
            } else {
                $('.flasher').addClass('invisible');
                $("#jquery_jplayer_1").jPlayer("stop");
            }
        })
    };
});