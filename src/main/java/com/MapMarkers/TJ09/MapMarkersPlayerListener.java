package com.MapMarkers.TJ09;

import java.util.Date;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Handle events for all Player related events
 * @author TJ09
 */
public class MapMarkersPlayerListener extends PlayerListener {
    private final MapMarkers plugin;

    public MapMarkersPlayerListener(MapMarkers instance) {
        plugin = instance;
    }
	
	public void onPlayerMove(PlayerMoveEvent event) {
		plugin.updatePlayer(event.getPlayer());
	}
	
	public void onPlayerLogin(PlayerLoginEvent event) {
		plugin.updatePlayer(event.getPlayer());
	}
	
	public void onPlayerTeleport(PlayerMoveEvent event) {
		plugin.updatePlayer(event.getPlayer());
	}
	
	public void onPlayerQuit(PlayerQuitEvent event) {
		plugin.removePlayer(event.getPlayer());
	}
}

