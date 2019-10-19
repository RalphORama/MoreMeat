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
            throw e;
        }

        List<String> defaultEntities = Arrays.asList(
                EntityType.BAT.toString(),
                EntityType.CAT.toString(),
                EntityType.OCELOT.toString(),
                EntityType.PARROT.toString()
        );

        getLogger().fine("Adding default entity list to the config file.");
        config.addDefault("entities", defaultEntities);

        getLogger().fine("Saving config.");
        config.options().copyDefaults(true);
        saveConfig();

        defaultEntities = null;

        getLogger().info("Parsing entity list from config.");
        List<String> entities = config.getStringList("entities");

        getLogger().fine("Instantiating death listener.");
        deathListener = new EntityDeathListener(entities);

        getLogger().info("Registering event hook.");
        Bukkit.getServer().getPluginManager().registerEvents(deathListener, this);

        getLogger().info("Successfully enabled!");
    }

    @Override
    public void onDisable() {
        // TODO: Maybe add a goodbye message
    }
}
