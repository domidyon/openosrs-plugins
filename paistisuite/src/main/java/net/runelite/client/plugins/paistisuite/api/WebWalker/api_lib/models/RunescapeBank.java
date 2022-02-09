package net.runelite.client.plugins.paistisuite.api.WebWalker.api_lib.models;

import net.runelite.client.plugins.paistisuite.api.WebWalker.wrappers.RSTile;

import java.util.HashMap;
import java.util.Map;

public enum RunescapeBank {
    AL_KHARID(3269, 3167, 0),
    ARCEUUS(1624, 3745, 0),
    ARDOUGNE_NORTH(2616, 3332, 0),
    ARDOUDNE_SOUTH(2655, 3283, 0),
    BARBARIAN_OUTPOST(2536, 3574, 0),
    BLAST_FURNACE_BANK(1948, 4957, 0),
    BLAST_MINE(1502, 3856, 0),
    CAMELOT(2725, 3493, 0),
    CANIFIS(3512, 3480, 0),
    CASTLE_WARS(2443, 3083, 0),
    CATHERBY(2808, 3441, 0),
    DIHN_BANK(1640, 3944, 0),
    DRAYNOR(3092, 3243, 0),
    DUEL_ARENA(3381, 3268, 0),
    DWARF_MINE_BANK(2837, 10207, 0),
    EDGEVILLE(3094, 3492, 0),
    FALADOR_EAST(3013, 3355, 0),
    FALADOR_WEST(2946, 3368, 0),
    FARMING_GUILD(1253, 3741, 0),
    FEROX_ENCLAVE(3130, 3631, 0),
    FISHING_GUILD(2586, 3420, 0),
    FOSSIL_ISLAND(3739, 3804, 0),
    GNOME_BANK(2445, 3425, 1),
    GNOME_TREE_BANK_SOUTH(2449, 3482, 1),
    GNOME_TREE_BANK_WEST(2442, 3488, 1),
    GRAND_EXCHANGE(3164, 3487, 0),
    GREAT_KOUREND_CASTLE(1612, 3681, 2),
    HOSIDIUS(1749, 3599, 0),
    HOSIDIUS_KITCHEN(1676, 3617, 0),
    JATIZSO(2416, 3801, 0),
    ISLE_OF_SOULS(2212, 2859, 0),
    LANDS_END(1512, 3421, 0),
    LOVAKENGJ(1526, 3739, 0),
    LUMBRIDGE_BASEMENT(3218, 9623, 0),
    LUMBRIDGE_TOP(3208, 3220, 2),
    LUNAR_ISLE(2099, 3919, 0),
    MOTHERLOAD(3760, 5666, 0),
    MOUNT_KARUULM(1324, 3824, 0),
    NARDAH(3428, 2892, 0),
    NEITIZNOT(2337, 3807, 0),
    PEST_CONTROL(2667, 2653, 0),
    PISCARILIUS(1803, 3790, 0),
    PRIFDDINAS(3257, 6106, 0),
    ROGUES_DEN(3043, 4973, 1),
    RUINS_OF_UNKAH(3156, 2835, 0),
    SHANTY_PASS(3308, 3120, 0),
    SHAYZIEN_CHEST(1484, 3646, 0),
    SHAYZIEN_BANK(1488, 3592, 0),
    SHILO_VILLAGE(2852, 2954, 0),
    SOPHANEM(2799, 5169, 0),
    SULPHUR_MINE(1453, 3858, 0),
    TZHAAR(2446, 5178, 0),
    VARROCK_EAST(3253, 3420, 0),
    VARROCK_WEST(3185, 3441, 0),
    VINERY(1808, 3570, 0),
    VINERY_BANK(1809, 3566, 0),
    WINTERTODT(1640, 3944, 0),
    WOODCUTTING_GUILD(1591, 3479, 0),
    YANILLE(2613, 3093, 0),
    ZANARIS(2383, 4458, 0),
    ZEAH_SAND_BANK(1719, 3465, 0);

    RSTile position;

    RunescapeBank(int x, int y, int z) {
        this.position = new RSTile(x, y, z);
    }

    public RSTile getPosition() {
        return this.position;
    }

    private static final Map<RSTile, RunescapeBank> bankMap = new HashMap<>();

    static {
        for (RunescapeBank bank : RunescapeBank.values()) {
            bankMap.put(bank.getPosition(), bank);
        }
    }

    public static RunescapeBank getBank(RSTile point) {
        return bankMap.get(point);
    }
}
