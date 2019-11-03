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

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public class FurnaceSmeltListener implements Listener {
    private Logger logger;

    FurnaceSmeltListener(Logger logger) {
        this.logger = logger;
    }

    @EventHandler
    public void onFurnaceSmelt(FurnaceSmeltEvent event) {
        ItemStack source = event.getSource();
        ItemStack result = event.getResult();

        // Will be "" if item doesn't exist in config.
        String displayName = ChatColor.stripColor(
                Objects.requireNonNull(
                        source.getItemMeta()
                )
                .getDisplayName()
                .replaceAll("Raw ", "")
                .toLowerCase()
        );

        if (displayName.equalsIgnoreCase("")) {
            logger.fine("Item " + source.getType().toString() + " not found in config.yml");
            return;
        }

        String newDisplayName;
        String node = "meats." + displayName;

        logger.fine("Looking for " + node + " in config.");

        // Set everything up if the node exists
        if (MoreMeat.config.contains(node)) {
            newDisplayName = MoreMeat.config.getString(node + ".dropName");
        } else {
            logger.fine(node + " doesn't exist in the config!");
            return;
        }

        // Don't modify anything if the config section isn't enabled.
        boolean globalEnable = MoreMeat.config.getBoolean("enabled");
        boolean localEnable  = MoreMeat.config.getBoolean(node + ".enabled");
        if (!globalEnable || !localEnable) {
            logger.fine("Skipping " + node + " smelt event because:");
            logger.fine(" > global enable is " + globalEnable);
            logger.fine(" > local enable is  " + localEnable);

            return;
        }

        // Maintain same lore & other properties from the custom drop.
        ItemMeta oldMeta = source.getItemMeta();
        oldMeta.setDisplayName(ChatColor.RESET + "Cooked " + newDisplayName);

        logger.fine("Setting ItemMeta to " + oldMeta.toString());

        // We just need to update the meta, the furnace will automatically produce cooked food
        // of the same type for us.
        result.setItemMeta(oldMeta);
        }
}
