package MyMod.Core;

import java.io.FileWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class PlayerListener implements Listener {
	Main m;
	public PlayerListener(Main m) {
		this.m = m;
		m.getServer().getPluginManager().registerEvents(this, m);
		m.getServer().getLogger().log(Level.INFO, "Constructor", false);
	}
	

	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityDeath(EntityDeathEvent event) {
		Entity e = event.getEntity();
        if (e instanceof Player) {
        	Player p = (Player) e;
        	m.deathLocations.put(p.getName(), p.getLocation());
        }
	}
}
