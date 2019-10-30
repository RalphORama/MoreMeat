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

import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.bukkit.Bukkit.getLogger;

public class EntityDeathListener implements Listener {
    /**
     * Default constructor doesn't need to do anything - all options are read from the config.
     */
    EntityDeathListener() {};

    /**
     * This function fires whenever an entity dies.
     * @param event The death event.
     */
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        // Don't do anything if the global "enabled" option is false.
        if (!MoreMeat.config.getBoolean("enabled"))
            return;

        LivingEntity entity = event.getEntity();

        boolean wasOnFire = entity.getFireTicks() > 0;
        getLogger().fine("Entity fire ticks were " + entity.getFireTicks());

        String entityType = entity.getType().toString();
        String configParent = "meats." + entityType.toLowerCase() + ".";

        // Don't do anything if we weren't killed by a player
        if (!(event.getEntity().getKiller() instanceof Player))
            return;

        // Don't do anything if this entity is disabled or doesn't exist in the config
        try {
            if (!MoreMeat.config.contains(configParent + "enabled")) {
                getLogger().fine("Ignored " + entity.getName() + " death: Disabled in config.yml.");
                return;
            }
        } catch (IllegalArgumentException e) {
            getLogger().fine("Ignored " + entity.getName() + " death: No config section for this entity.");

            // We can safely ignore this error
            return;
        }

        // Entity type is present in config.yml and is enbaled
        getLogger().fine("Modifying drops of " + entity.getName() + " after death.");

        List<ItemStack> originalDrops = event.getDrops();

        // Combine original drops with new (meat) drops
        // TODO: Add a config option to disable original drops.
        // TODO: Implement custom name config option.
        Material material = null;
        // String customName = MoreMeat.config.getString(configParent + "customName");
        // String customLore = MoreMeat.config.getString(configParent + "lore");
        int min = MoreMeat.config.getInt(configParent + "minDrops");
        int max = MoreMeat.config.getInt(configParent + "maxDrops");
        String foodBase = MoreMeat.config.getString(configParent + "foodBase");

        if (wasOnFire) {
            foodBase = "COOKED_" + foodBase;
        }

        try {
            material = Material.valueOf(foodBase);
        } catch (IllegalArgumentException | NullPointerException e) {
            getLogger().severe("Invalid foodBase " + foodBase + " specified for " + entityType + " in config.yml!");
            e.printStackTrace();
            return;
        }

        // inclusive random in range
        int amount = ThreadLocalRandom.current().nextInt(min, max + 1);

        // Create the ItemStack to add to the drops
        ItemStack meat = new ItemStack(material, amount);

        // Set custom item meta
        // TODO: Implement config options for custom name/lore
        ItemMeta meta = meat.getItemMeta();
        if (meta != null) {
            String status = (wasOnFire) ? "Cooked " : "Raw ";
            String newName = WordUtils.capitalizeFully(status + entityType);
            // Use ChatColor.RESET so name isn't italic
            // Has to go here otherwise capitalizeFully messes up.
            meta.setDisplayName(ChatColor.RESET + newName);
            meat.setItemMeta(meta);
        }

        // All done - add the meat to the stack!
        originalDrops.add(meat);
    }
}

