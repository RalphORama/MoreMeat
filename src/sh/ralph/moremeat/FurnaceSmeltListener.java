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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public class FurnaceSmeltListener implements Listener {
    private MoreMeat plugin;
    private Logger logger;

    FurnaceSmeltListener(MoreMeat plugin, Logger logger) {
        this.plugin = plugin;
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
        String node = "meats." + displayName + ".dropName";

        logger.fine("Looking for " + node + " in config.");

        if (MoreMeat.config.contains(node)) {
            newDisplayName = MoreMeat.config.getString(node);
        } else {
            logger.fine(node + " doesn't exist in the config!");
            return;
        }

        logger.fine("Successfully got " + newDisplayName);


//        if (typeof meatList != null;)
//        for (CustomMeat meat : meatList) {
//            logger.info("meat is " + meat.toString());
//        }
        // TODO: Set up custom NBT values and check
//        if (source.getItemMeta().getDisplayName().contains("Raw")) {
//
//        }
    }


}
