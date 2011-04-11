var playerMarkers = null;
var warpMarkers = [];

function deletePlayerMarkers() {
  if (playerMarkers) {
    for (i in playerMarkers) {
      playerMarkers[i].setMap(null);
    }
    playerMarkers = null;
  }
}

setInterval(loadPlayerMarkers, 1000 * 15);
setTimeout(loadPlayerMarkers, 1000);

function preparePlayerMarker(marker,item) {
	var c = "<div class=\"infoWindow\" style='width: 300px'><img src='player.php?"+item.msg+"'/><h1>"+item.msg+"</h1></div>";
	var infowindow = new google.maps.InfoWindow({content: c});
	google.maps.event.addListener(marker, 'click', function() {
		infowindow.open(map,marker);
	});
}

function loadPlayerMarkers() {
	$.getJSON('markers.json', function(data) {
		deletePlayerMarkers();
		playerMarkers = [];
		for (i in data) {
			var item = data[i];
			if(item.id != 4) continue;
			var converted = fromWorldToLatLng(item.x, item.y, item.z);
			var marker =  new google.maps.Marker({
				position: converted,
				map: map,
				title: item.msg,
				icon: 'player.php?'+item.msg,
				visible: true,
				zIndex: 999
			});
			playerMarkers.push(marker);
			preparePlayerMarker(marker,item);
		}
	});
}