$(document).ready(function() {
    $('.js-modal').modal();
    loadAds();
});

function loadAds() {
    $.ajax({
        type: "POST",
        url: "ads?action=get-all-ads"
    }).done(function(data) {
        if (data.length !== 0) {
            let rsl = drawAds(data);
            $('.js-user-ads').html(rsl);
        }
    }).fail(function(err) {
        let instance = M.Modal.getInstance($(".js-modal"));
        instance.open();
        showModalError("Ошибка на стороне сервера, перезагрузите страницу");
    });
}