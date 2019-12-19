
var L=window.L;


var map = L.map('map').setView([47.82710, 1.92587], 13);

L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
}).addTo(map);

L.marker([47.82710, 1.92587]).addTo(map)
    .bindPopup('A pretty CSS3 popup.<br> Easily customizable.');
L.marker([47.90639,1.90518]).addTo(map).bindPopup('A pretty CSS3 popup.<br> Easily customizable.');
var latlong = [
	[ 1.904753 ,47.908153],
	[1.904082, 47.907145],
	[1.904972,47.906865]
    ];
var polyline = L.polyline(latlong, {color: 'red'}).addTo(map);

   