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
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandMeat implements CommandExecutor, TabCompleter {
    private MoreMeat plugin;
    private Map<String, Boolean> toggle;
    // TODO: Make this get version string from plugin.yml
    private final String usage;

    /**
     * Command constructor - Takes a reference to the main plugin instance.
     *                       Instantiates a true/false map for simplifying logic when processing commands.
     *                       Sets up a help/version string with the version specified in plugin.yml
     * @param plugin The instance of the main plugin class (see MoreMeat.java)
     */
    CommandMeat(MoreMeat plugin) {
        this.plugin = plugin;

        this.toggle = new HashMap<String, Boolean>();
        this.toggle.put("enable", true);
        this.toggle.put("disable", false);

        this.usage = "MoreMeat v" + plugin.getDescription().getVersion() + "\n" +
                "Usage: /meat <reload / [enable/disable]>";
    }

    /**
     * The command hook.  Don't need to match command name because of how it's instantiated in MoreMeat.
     * @param sender Who sent the command (player, server)
     * @param command The command after /.
     * @param label
     * @param args Arguments passed to the command.
     * @return true if the player has permission and the command is executed, false otherwise.
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        switch (args.length) {
            // If we have enable, disable, reload, or anything else specified
            case 1:
                return handleMeatCommand(sender, args[0]);
            // If we don't have exactly one argument, print usage.
            case 0:
            default:
                if (sender.hasPermission("moremeat.meat")) {
                    sender.sendMessage(usage);
                    return true;
                }
                break;
        }

        return false;
    }

    /**
     * Adds tab completion for the /meat command.
     * @param sender The sender of the command (player or console)
     * @param command The command that was sent.
     * @param alias The command alias.
     * @param args The command arguments.
     * @return An array of possible autocomplete options.
     */
    @Override
    public List<String> onTabComplete (CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player))
            return null;

        if (args.length == 1) {
            return Arrays.asList("enable", "disable", "reload");
        }

        return null;
    }

    /**
     * Handles /meat <enable/disable> and /meat reload
     * @param sender Who invoked the command (a player or console)
     * @param arg Expected: enable, disable, or relaod.
     * @return true if invoker has permission, false otherwise.
     */
    private boolean handleMeatCommand(CommandSender sender, String arg) {
        // Don't do anything if the player doesn't have the base permission.
        if (!sender.hasPermission("moremeat.meat"))
            return false;

        // Try to reload the config from disk, print a stack trace in console if we hit an error.
        if (arg.equalsIgnoreCase("reload")) {
            if (sender.hasPermission("moremeat.reload")) {
                try {
                    plugin.reloadConfig();
                } catch (Exception e) {
                    /*
                     * TODO: For some reason this exception doesn't get caught.
                     *  Instead, we get the following error:
                     *  org.bukkit.configuration.InvalidConfigurationException: while scanning a simple key
                     *  and the plugin sends the success message to the invoker.
                     *  https://s.ralph.sh/a1c6e
                     */
                    sender.sendMessage(ChatColor.RED + "[MoreMeat] Failed to reload config!");
                    e.printStackTrace();
                    return true;
                }

                MoreMeat.config = plugin.getConfig();
                sender.sendMessage("[MoreMeat] Config successfully reloaded.");

                return true;
            }
            return false;
        }

        // Enable/disable the plugin.  Uses a Map<String, Boolean> so we don't need extraneous if statements.
        if (arg.equalsIgnoreCase("enable") || arg.equalsIgnoreCase("disable")) {
            if (!sender.hasPermission("moremeat.toggle"))
                return false;

            MoreMeat.config.getRoot().set("enabled", toggle.get(arg));

            plugin.saveConfig();

            sender.sendMessage("[MoreMeat] Successfully " + arg + "d.");

            return true;
        }

        // Just send usage info if it's an unknown option
        if (sender.hasPermission("moremeat.meat")) {
            sender.sendMessage(usage);
            return true;
        }

        // I don't think we'll ever hit this return statement, but just in case...
        return false;
    }
}
