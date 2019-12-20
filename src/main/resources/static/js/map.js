
var L=window.L;



var longFrom = document.getElementById('longFrom').innerHTML;
var latFrom = document.getElementById('latFrom').innerHTML;
var longTo = document.getElementById('longTo').innerHTML;
var latTo = document.getElementById('latTo').innerHTML;

var map = L.map('map').setView([latFrom, longFrom], 13);

L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
}).addTo(map);

L.marker([latFrom, longFrom]).addTo(map)
    .bindPopup('A pretty CSS3 popup.<br> Easily customizable.');
L.marker([latTo,longTo]).addTo(map).bindPopup('A pretty CSS3 popup.<br> Easily customizable.');

var latlong = [
	[47.898020345,1.9042782885],[47.898020345,1.9042782885],[47.898044,1.904278]
    ];
var polyline = L.polyline(matrix, {color: 'red'}).addTo(map);

console.log(latlong);
console.log(matrix);


   