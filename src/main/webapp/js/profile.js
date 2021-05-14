$(document).ready(function() {
    $('.js-modal').modal();
    loadAds();
});

function loadAds() {
    let userId = getUrlParam("id");
    if (userId) {
        $.ajax({
            type: "POST",
            url: "ads?action=get-user-ads&id=" + userId
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
    } else {
        let instance = M.Modal.getInstance($(".js-modal"));
        instance.open();
        showModalError("Неверный запрос.");
    }
}