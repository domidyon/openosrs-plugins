package net.runelite.client.plugins.paistisuite.api.WebWalker.Teleports.teleport_utils;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.MenuAction;
import net.runelite.api.Quest;
import net.runelite.api.QuestState;
import net.runelite.api.Skill;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.plugins.paistisuite.api.PPlayer;
import net.runelite.client.plugins.paistisuite.api.PUtils;
import net.runelite.client.plugins.paistisuite.api.PVars;
import net.runelite.client.plugins.paistisuite.api.PWidgets;
import net.runelite.client.plugins.paistisuite.api.WebWalker.shared.helpers.QuestChecker;
import net.runelite.client.plugins.paistisuite.api.WebWalker.walker_engine.WaitFor;
import net.runelite.client.plugins.paistisuite.api.WebWalker.wrappers.RSTile;

import java.util.function.BooleanSupplier;

@Slf4j
public enum MinigameTeleport {
    //        1 Barbarian Assault: Complete Barbarian Assault tutorial
    //        2 Blast Furnace: Visited Keldagrim
    //        3 Burthorpe Games Room
    //        4 Castle Wars
    //        5 Clan Wars
    //        7 Fishing Trawler: Fishing 15+
    //        10 Last Man Standing
    //        11 Nightmare Zone: Ability to enter the minigame
    //        12 Pest Control: Combat 40+
    //        14 Rat Pits: Ratcatchers quest
    //        15 Shades of Mort'ton: Shades of Mort'ton quest
    //        20 Tithe Farm: Entering the minigame area at least once, which requires 100% Hosidius favour and 34 Farming
    //        21 Trouble Brewing: Cabin Fever and Cooking 40+
    //        22 TzHaar Fight Pit
    BARB_ASS(1, new RSTile(2531, 3577, 0), true, () -> PUtils.getClient().getVarbitValue(3251) >= 1),
    BLAST_FURNACE(2, new RSTile(2933, 10183, 0), true, () -> PUtils.getClient().getVarbitValue(575) >= 1),
    BURTHROPE_GAMES(3, new RSTile(2208, 4938, 0), true),
    CASTLE_WARS(4, new RSTile(2439, 3092, 0), true),
    CLAN_WARS(5, new RSTile(3151, 3636, 0), true),
    FISHING_TRAWLER(7, new RSTile(2658, 3158, 0), true, () -> PUtils.getClient().getRealSkillLevel(Skill.FISHING) >= 15),
    LMS(10, new RSTile(3149, 3635, 0), true),
    NMZ(11, new RSTile(2611, 3121, 0), true, () -> QuestChecker.getNmzQuestsCompleted() >= 5),
    PEST_CONTROL(12, new RSTile(2653, 2655, 0), true, () -> PPlayer.getPlayer().getCombatLevel() >= 40),
    RAT_PITS_VARROCK(14, new RSTile(3263, 3406, 0), false, () -> Quest.RATCATCHERS.getState(PUtils.getClient()) == QuestState.FINISHED),
    SHADES_OF_MORTON(15, new RSTile(3500, 3300, 0), true, () -> Quest.SHADES_OF_MORTTON.getState(PUtils.getClient()) == QuestState.FINISHED),
    TITHE_FARM(20, new RSTile(1793, 3501, 0), false),
    TROUBLE_BREWING(21, new RSTile(3811, 3021, 0), true, () -> Quest.CABIN_FEVER.getState(PUtils.getClient()) == QuestState.FINISHED
            && PUtils.getClient().getRealSkillLevel(Skill.COOKING) >= 40),
    TZHAAR_FIT_PIT(22, new RSTile(2402, 5181, 0), true);

    @Getter
    private final int id;
    @Getter
    private final RSTile teleportTile;
    @Getter
    @Setter
    private boolean allowed;
    @Getter
    private final BooleanSupplier check;

    MinigameTeleport(int id, RSTile teleportTile, boolean allowed) {
        this.id = id;
        this.teleportTile = teleportTile;
        this.allowed = allowed;
        this.check = () -> this.allowed && ((long) PVars.getSetting(888) * 60000) + (20 * 60000) < System.currentTimeMillis()
                && PPlayer.getPlayer().getHealthRatio() == -1;
    }

    MinigameTeleport(int id, RSTile teleportTile, boolean allowed, BooleanSupplier check) {
        this.id = id;
        this.teleportTile = teleportTile;
        this.allowed = allowed;
        this.check = () -> this.allowed && ((long) PVars.getSetting(888) * 60000) + (20 * 60000) < System.currentTimeMillis()
                && PPlayer.getPlayer().getHealthRatio() == -1 && check.getAsBoolean();
    }

    public boolean minigameTeleport() {
        WorldPoint start = PPlayer.getWorldLocation();
        if (Boolean.TRUE.equals(PUtils.clientOnly(() -> {
            if (PVars.getVarbit(13071) != 3) {
                Widget groupingTab = PWidgets.findWidget(548, 86, "Grouping");
                if (groupingTab == null) {
                    log.info("Error Grouping tab not found");
                    return false;
                }
                PUtils.getClient().invokeMenuAction("Grouping", "PaistiSuite", 1, MenuAction.CC_OP.getId(), -1, groupingTab.getId());
            }
            PUtils.getClient().runScript(124, this.getId());
            PUtils.sleep(60);
            PUtils.getClient().invokeMenuAction("Minigame Teleport", "PaistiSuite", 1, MenuAction.CC_OP.getId(), this.getId(), WidgetInfo.MINIGAME_TELEPORT_BUTTON.getId());
            PUtils.getClient().setMouseIdleTicks(0);
            PUtils.getClient().setKeyboardIdleTicks(0);
            return true;
        }, "Minigame Teleport"))) {
            if (WaitFor.condition(1800, () ->
                    PPlayer.getPlayer().getAnimation() != -1 ? WaitFor.Return.SUCCESS : WaitFor.Return.IGNORE) == WaitFor.Return.SUCCESS) {
                return WaitFor.condition(15000, () -> !PPlayer.getWorldLocation().equals(start)
                        ? WaitFor.Return.SUCCESS : WaitFor.Return.IGNORE) != WaitFor.Return.SUCCESS;
            }
        }
        return false;
    }
}
