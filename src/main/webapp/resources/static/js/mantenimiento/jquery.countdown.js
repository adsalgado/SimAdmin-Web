/**
 * @name		jQuery Countdown Plugin
 * @author		Martin Angelov
 * @version 	1.0
 * @url			http://tutorialzine.com/2011/12/countdown-jquery/
 * @license		MIT License
 
 
 (function ($) {
 
 // Number of seconds in every time division 24*60*60
 var days = 48*180*180,
 hours = 60*60,
 minutes = 60;
 
 // Creating the plugin
 $.fn.countdown = function (prop) {
 
 var options = $.extend({
 callback: function () {},
 timestamp: 0
 }, prop);
 
 var left, d, h, m, s, positions;
 
 // Initialize the plugin
 init(this, options);
 
 positions = this.find('.position');
 
 (function tick() {
 // Time left
 left = Math.floor((options.timestamp - (new Date())) / 1000);
 
 if (left < 0) {
 left = 0;
 }
 
 // Number of days left
 d = Math.floor(left / days);
 updateDuo(0, 1, d);
 left -= d * days;
 
 // Number of hours left
 h = Math.floor(left / hours);
 updateDuo(2, 3, h);
 left -= h * hours;
 
 // Number of minutes left
 m = Math.floor(left / minutes);
 updateDuo(4, 5, m);
 left -= m * minutes;
 
 // Number of seconds left
 s = left;
 updateDuo(6, 7, s);
 
 // Calling an optional user supplied callback
 options.callback(d, h, m, s);
 
 // Scheduling another call of this function in 1s
 setTimeout(tick, 1000);
 })();
 
 // This function updates two digit positions at once
 function updateDuo(minor, major, value) {
 switchDigit(positions.eq(minor), Math.floor(value / 10) % 10);
 switchDigit(positions.eq(major), value % 10);
 }
 
 return this;
 };
 
 
 function init(elem, options) {
 elem.addClass('countdownHolder');
 
 // Creating the markup inside the container
 $.each(['Days', 'Hours', 'Minutes', 'Seconds'], function (i) {
 var boxName;
 if (this == "Days") {
 boxName = "DIAS";
 } else if (this == "Hours") {
 boxName = "HORAS";
 } else if (this == "Minutes") {
 boxName = "MINUTOS";
 } else {
 boxName = "SEGUNDOS";
 }
 $('<div class="count' + this + '">' +
 '<span class="position">' +
 '<span class="digit static">0</span>' +
 '</span>' +
 '<span class="position">' +
 '<span class="digit static">0</span>' +
 '</span>' +
 '<span class="boxName">' +
 '<span class="' + this + '">' + boxName + '</span>' +
 '</span>'
 ).appendTo(elem);
 
 if (this != "Seconds") {
 elem.append('<span class="points">:</span><span class="countDiv countDiv' + i + '"></span>');
 }
 });
 
 }
 
 // Creates an animated transition between the two numbers
 function switchDigit(position, number) {
 
 var digit = position.find('.digit')
 
 if (digit.is(':animated')) {
 return false;
 }
 
 if (position.data('digit') == number) {
 // We are already showing this number
 return false;
 }
 
 position.data('digit', number);
 
 var replacement = $('<span>', {
 'class': 'digit',
 css: {
 top: 0,
 opacity: 0
 },
 html: number
 });
 
 // The .static class is added when the animation
 // completes. This makes it run smoother.
 
 digit
 .before(replacement)
 .removeClass('static')
 .animate({top: 0, opacity: 0}, 'fast', function () {
 digit.remove();
 })
 
 replacement
 .delay(100)
 .animate({top: 0, opacity: 1}, 'fast', function () {
 replacement.addClass('static');
 });
 }
 })(jQuery);
 
 */
$(document).ready(function () {
    var parts = '2018-02-02 08:00:00'.split('-');
    var terceraParte = parts[2].split(" ");
    var tiempo = terceraParte[1];
    var todoTiempo = tiempo.split(":");
    var mydate = new Date(parts[0], parts[1] - 1, terceraParte[0], todoTiempo[0], todoTiempo[1], todoTiempo[2], 0);
    var otro = mydate.getTime();

    function funcionandoMinus() {
        var actual = new Date().getTime();
        var diff = new Date(otro - actual);
        var diferencia = otro - actual;
        var ms = diferencia;
        var min = ms / 1000 / 60;

//        var crono = document.getElementById('crono');
        var Days = document.getElementById('dia');
        var Hours = document.getElementById('horas');
        var Minutes = document.getElementById('minutos');
        var Seconds = document.getElementById('segundos');

//        var result;
        var dia = diff.getUTCDate() -1;
        var dia2=0;
        if(dia < 10){
            dia2 ="0"+dia;
        }else{
            dia2 = dia;
        }
        var horas = diff.getUTCHours();
        var horas2=0;
        if(horas < 10){
            horas2 ="0"+horas;
        }else{
            horas2 = horas;
        }
        var minutos = diff.getUTCMinutes();
        var minutos2=0;
        if(minutos < 10){
            minutos2 ="0"+minutos;
        }else{
            minutos2 = minutos;
        }
        //
        var segundos = diff.getUTCSeconds();
        var segundos2=0;
        if(segundos < 10){
            segundos2 ="0"+segundos;
        }else{
            segundos2 = segundos;
        }
        //
//        result = diff.getUTCDate() + ":" + diff.getUTCHours() + ":" + diff.getUTCMinutes() + ":" + diff.getUTCSeconds();

//        crono.innerHTML = result;
        Days.innerHTML = dia2;
        Hours.innerHTML = horas2;
        Minutes.innerHTML = minutos2;
        Seconds.innerHTML = segundos2;        

        setTimeout(funcionandoMinus, 1000);
    }
    funcionandoMinus();
});



