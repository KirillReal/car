$('.js-is_sold').change(function () {
    let isSold = $(".js-is_sold").prop('checked');
    let adsId = $(".js-is_sold").data('ads_id');
    $('.js-is_sold').prop("disabled", true);
    let data = {
        "isSold": isSold,
        "adsId": adsId
    };
    $.ajax({
        type: "POST",
        url: "ads?action=update",
        contentType: "application/json",
        data: JSON.stringify(data),
    }).done(function(response) {
        $('.js-is_sold').prop("disabled", false);
    }).fail(function(err) {
        showModalError("Ошибка при обновлении объявления, перезагрузите страницу или повторите запрос позднее.");
    });
});