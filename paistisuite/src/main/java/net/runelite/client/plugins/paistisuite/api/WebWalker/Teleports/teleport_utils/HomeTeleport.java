package net.runelite.client.plugins.paistisuite.api.WebWalker.Teleports.teleport_utils;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.plugins.paistisuite.api.PPlayer;
import net.runelite.client.plugins.paistisuite.api.PVars;
import net.runelite.client.plugins.paistisuite.api.WebWalker.Teleports.Teleport;
import net.runelite.client.plugins.paistisuite.api.WebWalker.shared.helpers.magic.SpellBook;
import net.runelite.client.plugins.paistisuite.api.WebWalker.walker_engine.WaitFor;
import net.runelite.client.plugins.paistisuite.api.WebWalker.wrappers.RSTile;

import java.util.function.BooleanSupplier;

@Slf4j
public enum HomeTeleport {
    LUMBRIDGE_TELEPORT("Lumbridge Home Teleport", new RSTile(3225, 3219, 0), SpellBook.Type.STANDARD),
    ARCEUUS_TELEPORT("Arceuus Home Teleport", new RSTile(1712, 3883, 0), SpellBook.Type.ARCEUUS),
    EDGEVILLE_TELEPORT("Edgeville Home Teleport", new RSTile(3087, 3496, 0), SpellBook.Type.ANCIENT),
    LUNAR_TELEPORT("Lunar Home Teleport", new RSTile(2095, 3913, 0), SpellBook.Type.LUNAR);

    @Getter
    private final String spellName;
    @Getter
    private final RSTile teleportTile;
    @Getter
    private final SpellBook.Type spellbook;
    @Getter
    private final BooleanSupplier check;

    HomeTeleport(String spellName, RSTile teleportTile, SpellBook.Type spellbook) {
        this.spellName = spellName;
        this.teleportTile = teleportTile;
        this.spellbook = spellbook;
        this.check = () -> ((long) PVars.getSetting(892) * 60000) + (30 * 60000) < System.currentTimeMillis()
                && PPlayer.getPlayer().getHealthRatio() == -1 && SpellBook.getCurrentSpellBook() == spellbook;
    }

    public boolean homeTeleport() {
        WorldPoint start = PPlayer.getWorldLocation();
        if (Teleport.castSpell(this.getSpellName(), "Cast")) {
            if (WaitFor.condition(1800, () ->
                    PPlayer.getPlayer().getAnimation() != -1 ? WaitFor.Return.SUCCESS : WaitFor.Return.IGNORE) == WaitFor.Return.SUCCESS) {
                return WaitFor.condition(15000, () -> !PPlayer.getWorldLocation().equals(start)
                        ? WaitFor.Return.SUCCESS : PPlayer.getPlayer().getHealthRatio() == -1 ? WaitFor.Return.IGNORE : WaitFor.Return.FAIL) != WaitFor.Return.SUCCESS;
            }
        }
        return false;
    }
}
