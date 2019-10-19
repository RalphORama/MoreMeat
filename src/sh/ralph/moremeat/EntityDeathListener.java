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
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

import static org.bukkit.Bukkit.getLogger;

public class EntityDeathListener implements Listener {
    private List<String> entityFilter = null;

    /**
     * Instantiates the listener with a list of entity names.
     * These names are 1:1 with those defined in the EntityType enum.
     * @param entities List of EntityType enums (i.e. EntityType.BAT.toString())
     */
    public EntityDeathListener(List<String> entities) {
        getLogger().fine("Instantiating EntityDeathListener with " + entities.toString());
        this.entityFilter = entities;
    }

    /**
     * TODO: Write this documentation.
     * @param event
     */
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();

        // Don't do anything if we weren't killed by a player
        if (!(event.getEntity().getKiller() instanceof Player))
            return;

        String entityType = entity.getType().toString();

        if (!entityFilter.contains(entityType)) {
            getLogger().fine("Ignored " + entity.getName() + " death.");
            return;
        }

        getLogger().fine("Modifying drops of " + entity.getName() + " after death.");

        // Combine original drops with new (meat) drops
        // TODO: Add a config option to disable original drops.
        List<ItemStack> originalDrops = event.getDrops();

        // Create the ItemStack to add to the drops
        // TODO: Add a config option for which type of meat to drop
        // TODO: Add config options for min/max stack size
        ItemStack meat = new ItemStack(Material.CHICKEN, 2);

        // Set custom item meta
        // TODO: Add config option for custom name/lore
        ItemMeta meta = meat.getItemMeta();
        String newName = WordUtils.capitalizeFully("Raw " + entityType);
        meta.setDisplayName(newName);

        // Finalize item meta
        meat.setItemMeta(meta);

        // Add our new item to the stack
        originalDrops.add(meat);
    }
}

