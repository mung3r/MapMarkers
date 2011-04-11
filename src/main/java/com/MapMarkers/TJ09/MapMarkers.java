package com.MapMarkers.TJ09;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.logging.*;

import org.bukkit.entity.Player;
import org.bukkit.World;
import org.bukkit.Server;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.util.config.Configuration;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * MapMarkers for Bukkit
 *
 * @author TJ09
 */
public class MapMarkers extends JavaPlugin {
	private final MapMarkersPlayerListener playerListener = new MapMarkersPlayerListener(this);
	public final HashMap<Player, String> lastSeen = new HashMap<Player, String>();
	
	private SimpleDateFormat dateFormat;
	private Boolean writeSpawn;
	private Boolean hasUpdated = true;

	public void onEnable() {
		PluginDescriptionFile pdfFile = this.getDescription();
		
		File configFile = new File(getDataFolder(), "config.yml");
		getDataFolder().mkdirs();
		Configuration config = new Configuration(configFile);
		if(!configFile.exists()) {
			config.setProperty("saveInterval",3000);
			config.setProperty("outputFile","world/markers.json");
			config.setProperty("writeSpawn",false);
			config.setProperty("dateFormat","yyyyMMdd HH:mm:ss");
			config.save();
		} else {
			config.load();
		}
	
		int interval = config.getInt("saveInterval",3000);
		String outputFile = config.getString("outputFile","world/markers.json");
		writeSpawn = config.getBoolean("writeSpawn",false);
		
		try {
			dateFormat = new SimpleDateFormat(config.getString("dateFormat","yyyyMMdd HH:mm:ss"));
		} catch (IllegalArgumentException e) {
			Logger.getLogger(pdfFile.getName()).log(Level.WARNING,"Invalid date format, defaulting to yyyyMMdd HH:mm:ss");
			dateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
		} catch (NullPointerException e) {
			Logger.getLogger(pdfFile.getName()).log(Level.WARNING,"Invalid date format, defaulting to yyyyMMdd HH:mm:ss");
			dateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
		}
		
		//Convert from 1000 ms to 20 ticks-per-second
		interval /= 50;
		
		// Start periodically updating stuff.
		getServer().getScheduler().scheduleSyncRepeatingTask(this,new MapMarkersTimerTask(this,outputFile), interval, interval);

		// Register our events
		PluginManager pm = getServer().getPluginManager();
		
		pm.registerEvent(Event.Type.PLAYER_MOVE, playerListener, Priority.Monitor, this);
		pm.registerEvent(Event.Type.PLAYER_LOGIN, playerListener, Priority.Monitor, this);
		pm.registerEvent(Event.Type.PLAYER_QUIT, playerListener, Priority.Monitor, this);
		
		Logger.getLogger(pdfFile.getName()).log(Level.INFO, pdfFile.getName() + " version " + pdfFile.getVersion() + " enabled");
	}
	public void onDisable() {		
		// NOTE: All registered events are automatically unregistered when a plugin is disabled
		Logger.getLogger(this.getDescription().getName()).log(Level.INFO, "Unloading");
	}
	
	public void updatePlayer(Player p) {
		hasUpdated = true;
		//HashMaps are apparently not thread-safe (oops)
		synchronized(lastSeen) {
			lastSeen.put(p,dateFormat.format(new Date()));
		}
	}
	public void removePlayer(Player p) {
		hasUpdated = true;
		//HashMaps are apparently not thread-safe (oops)
		synchronized(lastSeen) {
			lastSeen.remove(p);
		}
	}
	
	public JSONArray getJSON() {
		//If there's nothing new to write, then return null.
		if(hasUpdated == false) return null;
		
		hasUpdated = false;
		
		JSONArray jsonList = new JSONArray();
		JSONObject out;
		//Write spawns
		if(writeSpawn) {
			List<World> worlds = getServer().getWorlds();
			for(World w: worlds) {
				out = new JSONObject();
				out.put("msg","Spawn");
				out.put("id",1);
				out.put("world",w.getName());
				out.put("x",w.getSpawnLocation().getX());
				out.put("y",w.getSpawnLocation().getY());
				out.put("z",w.getSpawnLocation().getZ());
				//Not really necessary, is it?
				//out.put("timestamp",dateFormat.format(new Date()));
				jsonList.add(out);
			}
		}
		
		//Write players
		Player[] players = getServer().getOnlinePlayers();
		for(Player p: players) {
			out = new JSONObject();
			out.put("msg",p.getName());
			out.put("id",4);
			out.put("world",p.getLocation().getWorld().getName());
			out.put("x",p.getLocation().getX());
			out.put("y",p.getLocation().getY());
			out.put("z",p.getLocation().getZ());
			//HashMaps are apparently not thread-safe (oops)
			synchronized(lastSeen) {
				String s = lastSeen.get(p);
				if(s != null) out.put("timestamp",s);
			}
			
			jsonList.add(out);
		}
		return jsonList;
	}
}

