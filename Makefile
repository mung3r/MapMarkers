PLUGINNAME=MapMarkers

JFLAGS=-cp .:../bukkit-0.0.1-SNAPSHOT.jar:json_simple-1.1.jar
SOURCES=com/MapMarkers/TJ09/MapMarkers.java com/MapMarkers/TJ09/MapMarkersPlayerListener.java com/MapMarkers/TJ09/MapMarkersTimerTask.java
OBJECTS=$(SOURCES:.java=.class)
EXTRAS=Makefile config.yml README.txt player.png player.php player_markers.js player_markers_noskin.js $(SOURCES)

all: $(PLUGINNAME).zip

clean:
	rm -rf $(PLUGINNAME).zip $(PLUGINNAME).jar $(OBJECTS)

$(PLUGINNAME).zip: $(PLUGINNAME).jar $(EXTRAS)
	zip -9 $(PLUGINNAME).zip $(PLUGINNAME).jar $(EXTRAS)
	
$(PLUGINNAME).jar: $(OBJECTS) plugin.yml
	jar -cf $(PLUGINNAME).jar $(OBJECTS) plugin.yml

%.class: %.java
	javac $(JFLAGS) $<