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
import org.bukkit.entity.EntityType;

import static org.bukkit.Bukkit.getLogger;

/**
 * CustomMeat is a wrapper for individual entity sections in config.yml.
 * TODO: Add a list of valid food types (prevent foodBase = Material.CHEST or whatever).
 */
public class CustomMeat {
    public boolean enabled;

    private EntityType entity;
    private Material foodBase;
    private String dropName;
    private int minDrops, maxDrops;

    /**
     * Default constructor for a custom food type.
     * @param entity The entity that will drop this food (e.g. EntityType.BAT)
     * @param foodBase The base item (e.g. Material.CHICKEN or Material.PORK)
     * @param minDrops Minimum number of items to drop (min 0, max 64)
     * @param maxDrops Maximum number of items to drop (min 0, max 64)
     */
    public CustomMeat(EntityType entity, Material foodBase, int minDrops, int maxDrops) {
        // Default settings
        this.enabled = true;
        this.dropName = null;

        this.entity = entity;
        this.foodBase = foodBase;

        if (checkBounds(minDrops, 0, 64))
            this.minDrops = minDrops;
        else
            throw new IllegalArgumentException("minDrops out of bounds (" + minDrops + ")");

        if (checkBounds(maxDrops, 0, 64))
            this.maxDrops = maxDrops;
        else
            throw new IllegalArgumentException("maxDrops out of bounds (" + maxDrops + ")");

        if (this.minDrops > this.maxDrops)
            throw new IllegalArgumentException("minDrops > maxDrops (" + minDrops + " > " + maxDrops + ")");
    }

    public void enable() {
        this.enabled = true;
    }

    public void disable() {
        this.enabled = false;
    }

    public EntityType getEntity() {
        return entity;
    }

    /**
     * Sets which entity this configuration applies to.
     * @param entity The entity (e.g. EntityType.BAT, EntityType.CAT, etc.).
     */
    public void setEntity(EntityType entity) {
        this.entity = entity;
    }

    /**
     * Sets which entity this configuration applies to.
     * @param entity Entity name e.g. "CAT", "BAT"
     */
    public void setEntity(String entity) {
        entity = entity.toUpperCase();

        try {
            this.entity = EntityType.valueOf(entity);
        } catch (IllegalArgumentException e) {
            getLogger().severe("Invalid entity type " + entity + " supplied for " + this.entity.toString() + "!");
            e.printStackTrace();
        }
    }

    public Material getFoodBase() {
        return foodBase;
    }

    /**
     * Set the Material base that will be renamed (i.e. Material.CHICKEN, Material.PORK).
     * @param foodBase The new base Material.
     */
    public void setFoodBase(Material foodBase) {
        this.foodBase = foodBase;
    }

    /**
     * Set the Material base that will be renamed (i.e. Material.CHICKEN, Material.PORK).
     * @param foodBase String such as "CHICKEN" or "PORK" to be converted into a Material.
     */
    public void setFoodBase(String foodBase) {
        foodBase = foodBase.toUpperCase();

        try {
            this.foodBase = Material.valueOf(foodBase);
        } catch (IllegalArgumentException e) {
            getLogger().severe("Invalid material type " + foodBase + " supplied for " + this.entity.toString() + "!");
            e.printStackTrace();
        }
    }

    public int getMinDrops() {
        return minDrops;
    }

    /**
     * Set the minimum number of food items that will drop when an entity is killed.
     * @param minDrops New minimum amount of drops. Must be between 0 and 64 and not more than maxDrops.
     */
    public void setMinDrops(int minDrops) {
        if (!checkBounds(minDrops, 0, 64)) {
            throw new IllegalArgumentException("minDrops out of bounds (" + minDrops + ")");
        } else if (minDrops > this.maxDrops) {
            throw new IllegalArgumentException("minDrops > maxDrops (" + minDrops + " > " + this.maxDrops + ")");
        }

        this.minDrops = minDrops;
    }

    public int getMaxDrops() {
        return maxDrops;
    }

    /**
     * Set the maximum amount of food items that will drop when an entity is killed.
     * @param maxDrops The new maximum amount of drops. Must be between 0 and 64 and not less than minDrops.
     */
    public void setMaxDrops(int maxDrops) {
        // Throw an error if we're outside of 0-64 or smaller than minDrops
        if (!checkBounds(maxDrops, 0, 64)) {
            throw new IllegalArgumentException("maxDrops out of bounds (" + maxDrops + ")");
        } else if (maxDrops < this.minDrops) {
            throw new IllegalArgumentException("maxDrops < minDrops (" + maxDrops + " < " + this.minDrops + ")");
        }

        this.maxDrops = maxDrops;
    }

    /**
     * Getter for the customName class field.
     * @return customName if set, otherwise "Raw [Entity]"
     */
    public String getDropName() {
        if (dropName == null) {
            return WordUtils.capitalizeFully(this.entity.toString());
        }

        return dropName;
    }

    /**
     * Set the name of the meat that's dropped.
     * @param dropName The new name.
     */
    public void setDropName(String dropName) {
        this.dropName = dropName;
    }

    @Override
    public String toString() {
        return WordUtils.capitalizeFully(this.entity.toString());
    }

    /**
     * Check if an integer is within a given range.
     * @param toValidate The integer to check.
     * @param min The lower end of the range (inclusive).
     * @param max The upper end of the range (inclusive).
     * @return
     */
    private boolean checkBounds (int toValidate, int min, int max) {
        return toValidate >= min && toValidate <= max;
    }
}
