package MyMod.Core;

import java.util.Hashtable;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

import net.minecraft.server.v1_10_R1.Block;

public class Main extends JavaPlugin {
	Hashtable<String, Location> deathLocations;
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = null;
        if (cmd.getName().equals("spawn")) {
        	if(args.length > 0) {
        		player = getServer().getPlayer(args[0]);
        	} else if (sender instanceof Player) {
                player = (Player)sender;
            } else { 
            	return false;
            }
        	World world = player.getWorld();
        	Location pos = world.getSpawnLocation();
            player.teleport(new Location(world, pos.getX(), pos.getY(), pos.getZ()));
        	return true;
        }
        if(cmd.getName().equals("rip")) {
        	if(sender instanceof Player) {
        		player = (Player) sender;
        	} else {
        		return false;
        	}
        	Location diedAt = deathLocations.get(player.getName());
        	if(diedAt != null) {
        		player.teleport(diedAt);
        	} else {
        		player.sendMessage("No death location");
        	}
        	return true;
        }
        
        if(cmd.getName().equals("encase")) {
        	Material b = Material.GLASS;
        	int size = 2;
        	if(sender instanceof Player) {
        		player = (Player) sender;
        	} else {
        		return false;
        	}
        	if(args.length > 0) {
	        	try {
	        		size = Integer.parseInt(args[0]);
	        	} catch(NumberFormatException e) {
	        		return false;
	        	}
        	}
        	if(args.length > 1) {
        		try {
        			int id = Integer.parseInt(args[1]);
        			b = Material.getMaterial(id);
        		} catch(Exception e) {
        			b = Material.getMaterial(args[0]);
        		}
        	}
			if(b == null) return false;
    		size *=2;
    		size += 3;// account for walls
        	Location loc = player.getLocation();
        	int offset = size / 2;
        	for(int x = 0; x < size; x++) {
        		int realX = loc.getBlockX() + (x - offset);
        		for(int z = 0; z < size; z++) {
        			int realZ = loc.getBlockZ() + (z - offset); 
        			int y = player.getLocation().getBlockY() - 1;
        			player.getWorld().getBlockAt(realX, y, realZ).setType(b);
        		}
        	}
        	for(int y = 0; y < size - 2; y++) {
        		int realY = player.getLocation().getBlockY() + y;
        		for(int x = 0; x < size; x++ ) {
            		int realX = loc.getBlockX() + (x - offset);
            		int z = player.getLocation().getBlockZ();
        			player.getWorld().getBlockAt(realX, realY, z + offset).setType(b);
        			player.getWorld().getBlockAt(realX, realY, z - offset).setType(b);
        		}
        		for(int z = 0; z < size; z++ ) {
            		int realZ = loc.getBlockZ() + (z - offset);
            		int x = player.getLocation().getBlockX();
        			player.getWorld().getBlockAt(x - offset, realY, realZ).setType(b);
        			player.getWorld().getBlockAt(x + offset, realY, realZ).setType(b);
        		}
        	}
        	for(int x = 0; x < size; x++) {
        		int realX = loc.getBlockX() + (x - offset);
        		for(int z = 0; z < size; z++) {
        			int realZ = loc.getBlockZ() + (z - offset); 
        			int y = player.getLocation().getBlockY() + size - 2;
        			player.getWorld().getBlockAt(realX, y, realZ).setType(b);
        		}
        	}
        	return true;
        }
        return false;
    }
	
	@Override
	public void onEnable() {
		deathLocations = new Hashtable<String, Location>();
		new PlayerListener(this);
	}
	
	@Override
	public void onDisable() {
		
	}

}
