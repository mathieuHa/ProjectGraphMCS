var iconL = L.icon({
    iconUrl: 'icon.png',
    iconSize:     [45, 70], // size of the icon
    iconAnchor:   [22, 70], // point of the icon which will correspond to marker's location
    popupAnchor:  [-3, -76] // point from which the popup should open relative to the iconAnchor
});L.marker([48.075514,-0.763056], {icon: iconL}).addTo(mymap);