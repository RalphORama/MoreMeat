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
    public static volatile FurnaceSmeltListener smeltListener = null;

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

        // Probably won't ever need this but a config version in case of migrations.
        config.addDefault("version", 1);

        getLogger().fine("Creating default entity map.");

        // These are the default settings.  They shouldn't overwrite config.yml if you make changes.
        Map<String, CustomMeat> defaultEntities = new HashMap<>();
        defaultEntities.put("Bat",
                new CustomMeat(EntityType.BAT, Material.CHICKEN, 0, 1));
        defaultEntities.put("Cat",
                new CustomMeat(EntityType.CAT, Material.RABBIT, 1, 1));
        defaultEntities.put("Ocelot",
                new CustomMeat(EntityType.OCELOT, Material.RABBIT, 0, 2));
        defaultEntities.put("Parrot",
                new CustomMeat(EntityType.PARROT, Material.CHICKEN, 1, 1));

        getLogger().info("Creating player section.");

        CustomMeat players = new CustomMeat(EntityType.PLAYER, Material.BEEF, 1, 3);
        Map<String, String> defaultPlayerSettings = new HashMap<>();
        defaultPlayerSettings.put("enabled", "true");
        defaultPlayerSettings.put("normalizedName", "Player");
        defaultPlayerSettings.put("useNormalizedName", "false");
        defaultPlayerSettings.put("foodBase", "BEEF");
        defaultPlayerSettings.put("minDrops", "1");
        defaultPlayerSettings.put("maxDrops", "3");

        addMeatDefaultToConfig(players, "players", "");
        config.addDefault("players.useDropNameInsteadOfUsername", false);

        /*
         * Thanks to @ASangarin on the Spigot forums for their help with this.
         *  https://s.ralph.sh/03c43
         */
        getLogger().info("Adding default entity settings to the config file ");
        for(String key : defaultEntities.keySet()) {
            String s = key.toLowerCase();
            getLogger().fine("Adding " + s + " to the config.");

            addMeatDefaultToConfig(defaultEntities.get(key), "meats", s);
        }

        // Free memory
        defaultEntities = null;
        defaultPlayerSettings = null;

        getLogger().fine("Saving config.");
        config.options().copyDefaults(true);
        saveConfig();

        getLogger().fine("Instantiating death listener.");
        // No need to pass a list of entities to the listener - logic is now handled in the class.
        deathListener = new EntityDeathListener();

        getLogger().fine("Initiating furnace smelt listener.");
        smeltListener = new FurnaceSmeltListener(getLogger());

        getLogger().fine("Registering event hooks.");
        Bukkit.getServer().getPluginManager().registerEvents(deathListener, this);
        Bukkit.getServer().getPluginManager().registerEvents(smeltListener, this);

        // Register our commands
        this.getCommand("meat").setExecutor(new CommandMeat(this));
    }

    @Override
    public void onDisable() {
        // TODO: Maybe add a goodbye message
    }

    /**
     * There's no easy way to iterate + typecast a Map<String,CustomMeat>, so we use this.
     * @param target The entity that needs its defaults added.
     * @param prefix The parent section (where its "brother/sister" entities are stored.
     * @param subSection The section for the settings themselves.
     */
    private void addMeatDefaultToConfig(CustomMeat target, String prefix, String subSection) {
        if (prefix.isEmpty() && subSection.isEmpty()) {
            throw new IllegalArgumentException("'prefix' and 'subSection' can't both be empty!");
        }
        String node = "";

        if (!prefix.isEmpty()) {
            node += prefix + ".";
        }

        if (!subSection.isEmpty()) {
            node += subSection + ".";
        }

        config.addDefault(node + "enabled", target.enabled);
        config.addDefault(node + "dropName", target.getDropName());
        config.addDefault(node + "entity", target.getEntity().toString());
        config.addDefault(node + "foodBase", target.getFoodBase().toString());
        config.addDefault(node + "minDrops", target.getMinDrops());
        config.addDefault(node + "maxDrops", target.getMaxDrops());
    }
}
