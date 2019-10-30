/*
 * Copyright 2019 Ralph Drake
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package sh.ralph.moremeat;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class MoreMeat extends JavaPlugin implements Listener {

    public static volatile FileConfiguration config = null;
    public static volatile EntityDeathListener deathListener = null;

    @Override
    public void onEnable() {
        getLogger().fine("Creating default config (if it doesn't exist).");
        this.saveDefaultConfig();

        try {
            config = this.getConfig();
        }
        catch (Exception e) {
            getLogger().severe("Ran into an error loading the config file!");
            e.printStackTrace();
        }

        getLogger().fine("Adding global option `enabled`: `true`");
        config.addDefault("enabled", true);

        getLogger().fine("Creating default entity map.");

        Map<String, CustomMeat> defaultEntities = new HashMap<String, CustomMeat>();
        defaultEntities.put("Bat",
                new CustomMeat(EntityType.BAT, Material.CHICKEN, 0, 1));
        defaultEntities.put("Cat",
                new CustomMeat(EntityType.CAT, Material.RABBIT, 1, 1));
        defaultEntities.put("Ocelot",
                new CustomMeat(EntityType.OCELOT, Material.RABBIT, 0, 2));
        defaultEntities.put("Parrot",
                new CustomMeat(EntityType.PARROT, Material.CHICKEN, 1, 1));

        getLogger().fine("Adding default entity list to the config file.");
        /*
         * Thanks to @ASangarin on the Spigot forums for their help with this.
         *  https://s.ralph.sh/03c43
         */
        for(String key : defaultEntities.keySet()) {
            String s = key.toLowerCase();
            getLogger().fine("Adding " + s + " to the config.");

            config.addDefault("meats." + s + ".enabled", defaultEntities.get(key).enabled);
            config.addDefault("meats." + s + ".dropName", defaultEntities.get(key).getDropName());
            config.addDefault("meats." + s + ".entity", defaultEntities.get(key).getEntity().toString());
            config.addDefault("meats." + s + ".foodBase", defaultEntities.get(key).getFoodBase().toString());
            config.addDefault("meats." + s + ".minDrops", defaultEntities.get(key).getMinDrops());
            config.addDefault("meats." + s + ".maxDrops", defaultEntities.get(key).getMaxDrops());
        }

        // Free memory
        defaultEntities = null;

        getLogger().fine("Saving config.");
        config.options().copyDefaults(true);
        saveConfig();

        getLogger().fine("Instantiating death listener.");
        // No need to pass a list of entities to the listener - logic is now handled in the class.
        deathListener = new EntityDeathListener();

        getLogger().fine("Registering event hook.");
        Bukkit.getServer().getPluginManager().registerEvents(deathListener, this);

        // Register our commands
        this.getCommand("meat").setExecutor(new CommandMeat(this));
    }

    @Override
    public void onDisable() {
        // TODO: Maybe add a goodbye message
    }
}
