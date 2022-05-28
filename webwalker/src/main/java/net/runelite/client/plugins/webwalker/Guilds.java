/*
 * Copyright (c) 2019-2020, ganom <https://github.com/Ganom>
 * All rights reserved.
 * Licensed under GPL3, see LICENSE for the full scope.
 */
package net.runelite.client.plugins.webwalker;

import lombok.Getter;
import net.runelite.api.coords.WorldPoint;

@Getter
public enum Guilds {
    FARMING_GUILD("Farming Guild", new WorldPoint(1249, 3719, 0)),
    FISHING_GUILD("Fishing Guild", new WorldPoint(2611, 3393, 0)),
    WOODCUTTING_GUILD("Woodcutting Guild", new WorldPoint(1659, 3504, 0)),
    COOKING_GUILD("Cooking Guild", new WorldPoint(3142, 3442, 0)),
    WARRIORS_GUILD("Warriors Guild", new WorldPoint(2845, 3542, 0)),
    CRAFTING_GUILD("Crafting Guild", new WorldPoint(2935, 3280, 0)),
    MINING_GUILD("Mining Guild", new WorldPoint(3017, 9720, 0));

    private final String name;
    private WorldPoint worldPoint;

    Guilds(String name) {
        this.name = name;
    }

    Guilds(String name, WorldPoint worldPoint) {
        this.name = name;
        this.worldPoint = worldPoint;
    }
}
