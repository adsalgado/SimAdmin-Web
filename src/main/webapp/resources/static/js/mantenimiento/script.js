
$(document).ready(function () {
    var parts = '2018-02-02 08:00:00'.split('-');
    var terceraParte = parts[2].split(" ");
    var tiempo = terceraParte[1];
    var todoTiempo = tiempo.split(":");
    var mydate = new Date(parts[0], parts[1] - 1, terceraParte[0], todoTiempo[0], todoTiempo[1], todoTiempo[2], 0);

    /* ---- Countdown timer ---- */

    $('#counter').countdown({
//		timestamp : (mydate).getTime() + 11*24*60*60*1000
        timestamp: (mydate).getTime() + 11 * 24 * 60 * 60 * 1000
    });


    /* ---- Animations ---- */

    $('#links a').hover(
            function () {
                $(this).animate({left: 3}, 'fast');
            },
            function () {
                $(this).animate({left: 0}, 'fast');
            }
    );

    $('footer a').hover(
            function () {
                $(this).animate({top: 3}, 'fast');
            },
            function () {
                $(this).animate({top: 0}, 'fast');
            }
    );


});
