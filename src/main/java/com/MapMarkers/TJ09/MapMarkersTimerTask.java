package com.MapMarkers.TJ09;

import java.io.*;
import java.util.TimerTask;
import java.util.logging.*;

import org.json.simple.JSONArray;

/**
 * Periodic timer that outputs stuff to JSON.
 * @author TJ09
 */
public class MapMarkersTimerTask extends TimerTask {
	private final MapMarkers plugin;
	private String outputFile;

	public MapMarkersTimerTask(MapMarkers instance,String file) {
		plugin = instance;
		outputFile = file;
	}
	
	public void run() {
		JSONArray jsonList = plugin.getJSON();
		
		if(jsonList != null) {
			try {
				PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(outputFile)));
				writer.print(jsonList);
				writer.close();
				///System.out.println("Writing to file.");
			} catch (java.io.IOException e) {
				Logger.getLogger(plugin.getDescription().getName()).log(Level.SEVERE, "Unable to write to "+outputFile+": "+e.getMessage(),e);
				e.printStackTrace();
			}
		}
	}
}

