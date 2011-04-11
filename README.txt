MapMarkers Bukkit Plugin

PURPOSE
	This plugin outputs a JSON file containing player locations for use with various Minecraft mapping software.
	
INSTALLATION
	1. Put MapMarkers.jar in your plugins folder and start minecraft
	2. Edit config.yml (see below) in your plugins/MapMarkers folder - stop/start server
	
SETUP FOR MINECRAFT OVERVIEWER (Courtesy of FlukiestEmperor)
	0. Perform installation (see above)
	1. Edit config.yml so that markers.json is placed in the same folder as your Minecraft Overviewer output
		Alternatively don't edit config.yml, make a symlink (Linux):
			ln -s path/to/minecraft/server/bin/world/markers.json path/to/minecraft/map/markers.json
	2. Put player.png, player.php, and player_markers.js where the overviewer index.html is. (Optionally web_assets in your overviewer source directory)
		If you don't want to use player skins as the markers (requires PHP, allow_url_fopen, and ideally write permissions by your PHP user), copy player_markers_noskin.js and rename it to player_markers.js
	3. Add a line to your source overviewer index.html that reads:
		<script type="text/javascript" src="player_markers.js"></script>
	4. Run overviewer and everything should work
	
SETUP FOR PIGMAP
	0. Perform installation (see above)
	1. Edit config.yml so that markers.json is placed in the same folder as your Pigmap output
		Alternatively don't edit config.yml, make a symlink (Linux):
			ln -s path/to/minecraft/server/bin/world/markers.json path/to/minecraft/map/markers.json
	2. Put player.png, player.php, and player_markers.js where the pigmap HTML is.
		If you don't want to use player skins as the markers (which requires PHP, allow_url_fopen, and ideally write permissions by your PHP user), copy player_markers_noskin.js and rename it to player_markers.js
	3. Add two lines to your source template.html that reads:
		<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.4.4/jquery.min.js"></script> 
		<script type="text/javascript" src="player_markers.js"></script>
	4. Run pigmap and everything should work

CONFIGURATION
	plugins/MapMarkers/config.yml contains all settings
	
	saveInterval (default 3000): How often to output the markers, in milliseconds
	outputFile (default world/markers.json): Where to place the output file
	dateFormat (default yyyyMMdd HH:mm:ss): How to format timestamps in the output JSON.
	writeSpawn (default false): Whether or not to place spawn markers (one for each world) in the output.
	
JSON FORMAT
	The JSON produced by this plugin is an array of objects with the following structure:
		id: Integer representing the type of marker
			1 = Spawn
			2 = Reserved for compatability with hMod plugin
			3 = Reserved for compatability with hMod plugin
			4 = Player
		timestamp: Last updated time (missing for spawns)
		x,y,z: Position in minecraft coordinates
		msg: Name of player/"Spawn"