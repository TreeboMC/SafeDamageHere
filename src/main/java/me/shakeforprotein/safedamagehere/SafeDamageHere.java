package me.shakeforprotein.safedamagehere;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Array;
import java.util.Set;

public final class SafeDamageHere extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(this, this);
        getConfig().options().copyDefaults(true);
        getConfig().set("version", this.getDescription().getVersion());
        saveConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("sdh")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (args.length == 0) {
                    p.sendMessage("This command requires a subcommand to use");
                    p.sendMessage("For example:");
                    p.sendMessage("/sdh <region name> setA");
                    p.sendMessage("/sdh <region name> setB");
                    p.sendMessage("/sdh <region name> delete");
                    p.sendMessage("/sdh <region name> confirm <damage type>");
                    // p.sendMessage("/sdh list");
                    p.sendMessage("This command requires a subcommand to use");
                }

                Location loc = p.getLocation();
                int pX = (int) loc.getX();
                int pY = (int) loc.getY();
                int pZ = (int) loc.getZ();
                String pWorld = p.getWorld().getName();
                if (args.length == 2) {
                    if (args[1].equalsIgnoreCase("setA")) {
                        String region = args[0];
                        getConfig().set("worlds." + pWorld + ".regions." + region + ".ApointX", pX);
                        getConfig().set("worlds." + pWorld + ".regions." + region + ".ApointY", pY);
                        getConfig().set("worlds." + pWorld + ".regions." + region + ".ApointZ", pZ);
                        p.sendMessage(region + " <A> coords set to " + pX + " " + pY + " " + pZ);
                    }
                    if (args[1].equalsIgnoreCase("setB")) {
                        String region = args[0];
                        getConfig().set("worlds." + pWorld + ".regions." + region + ".BpointX", pX);
                        getConfig().set("worlds." + pWorld + ".regions." + region + ".BpointY", pY);
                        getConfig().set("worlds." + pWorld + ".regions." + region + ".BpointZ", pZ);
                        p.sendMessage(region + " <A> coords set to " + pX + " " + pY + " " + pZ);
                    }
                    if (args[1].equalsIgnoreCase("delete")) {
                        String region = args[0];
                        getConfig().set("worlds." + pWorld + ".regions." + region + ".ApointX", null);
                        getConfig().set("worlds." + pWorld + ".regions." + region + ".ApointY", null);
                        getConfig().set("worlds." + pWorld + ".regions." + region + ".ApointZ", null);
                        getConfig().set("worlds." + pWorld + ".regions." + region + ".BpointX", null);
                        getConfig().set("worlds." + pWorld + ".regions." + region + ".BpointY", null);
                        getConfig().set("worlds." + pWorld + ".regions." + region + ".BpointZ", null);
                        getConfig().set("worlds." + pWorld + ".regions." + region, null);
                        saveConfig();
                    }
                }

                if (args.length == 3) {
                    if (args[1].equalsIgnoreCase("confirm")) {
                        String region = args[0];
                        int x1 = (int) getConfig().get("worlds." + pWorld + ".regions." + region + ".ApointX");
                        int y1 = (int) getConfig().get("worlds." + pWorld + ".regions." + region + ".ApointY");
                        int z1 = (int) getConfig().get("worlds." + pWorld + ".regions." + region + ".ApointZ");
                        int x2 = (int) getConfig().get("worlds." + pWorld + ".regions." + region + ".BpointX");
                        int y2 = (int) getConfig().get("worlds." + pWorld + ".regions." + region + ".BpointY");
                        int z2 = (int) getConfig().get("worlds." + pWorld + ".regions." + region + ".BpointZ");

                        if (x1 <= x2) {
                            getConfig().set("worlds." + pWorld + ".regions." + region + ".ApointX", x1);
                            getConfig().set("worlds." + pWorld + ".regions." + region + ".BpointX", x2);
                        } else {
                            getConfig().set("worlds." + pWorld + ".regions." + region + ".ApointX", x2);
                            getConfig().set("worlds." + pWorld + ".regions." + region + ".BpointX", x1);
                        }

                        if (y1 <= y2) {
                            getConfig().set("worlds." + pWorld + ".regions." + region + ".ApointY", y1 - 1);
                            getConfig().set("worlds." + pWorld + ".regions." + region + ".BpointY", y2 + 1);
                        } else {
                            getConfig().set("worlds." + pWorld + ".regions." + region + ".ApointY", y2 - 1);
                            getConfig().set("worlds." + pWorld + ".regions." + region + ".BpointY", y1 + 1);
                        }

                        if (z1 <= z2) {
                            getConfig().set("worlds." + pWorld + ".regions." + region + ".ApointZ", z1);
                            getConfig().set("worlds." + pWorld + ".regions." + region + ".BpointZ", z2);
                        } else {
                            getConfig().set("worlds." + pWorld + ".regions." + region + ".ApointZ", z2);
                            getConfig().set("worlds." + pWorld + ".regions." + region + ".BpointZ", z1);
                        }
                        getConfig().set("worlds." + pWorld + ".regions." + region + ".damageType", args[2]);
                        p.sendMessage("Reorganizing coordinates");
                        p.sendMessage("Region <" + region + "> saved");
                        saveConfig();
                    }
                }

            }
        }
        return true;
    }


    @EventHandler
    private void onPlayerTakeDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            String pDamage = e.getCause().name();
            String doIt = "true";

            if (getConfig().get("worlds." + p.getWorld().getName()) != null) {
                String regions = getConfig().getConfigurationSection("worlds." + p.getWorld().getName() + ".regions").getKeys(false).toString();

                String[] regionsArray = regions.substring(1, regions.length() - 1).replaceAll("\\s+", "").split(",");
                for (String item : regionsArray) {
                    int x1 = (int) getConfig().getInt("worlds." + p.getWorld().getName() + ".regions." + item + ".ApointX");
                    int x2 = (int) getConfig().getInt("worlds." + p.getWorld().getName() + ".regions." + item + ".BpointX");
                    int y1 = (int) getConfig().getInt("worlds." + p.getWorld().getName() + ".regions." + item + ".ApointY");
                    int y2 = (int) getConfig().getInt("worlds." + p.getWorld().getName() + ".regions." + item + ".BpointY");
                    int z1 = (int) getConfig().getInt("worlds." + p.getWorld().getName() + ".regions." + item + ".ApointZ");
                    int z2 = (int) getConfig().getInt("worlds." + p.getWorld().getName() + ".regions." + item + ".BpointZ");
                    int px = (int) p.getLocation().getX();
                    int py = (int) p.getLocation().getY();
                    int pz = (int) p.getLocation().getZ();

                    if (x1 == 0 || x2 == 0 || y1 == 0 || y2 == 0 || z1 == 0 || z2 == 0) {
                        doIt = "false";
                        p.sendMessage("false");
                    }
                    if (doIt == "true") {
                        String damageType = getConfig().getString("worlds." + p.getWorld().getName() + ".regions." + item + ".damageType");
                        if ((px > x1 && px < x2 && py > y1 && py < y2 && pz > z1 && pz < z2) && pDamage.equalsIgnoreCase(damageType)) {
                            e.setCancelled(true);
                        }
                    }
                }
            } else {
                p.sendMessage("Invalid world");
            }
        }
    }


}
