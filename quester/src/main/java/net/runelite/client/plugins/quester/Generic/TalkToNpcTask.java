package net.runelite.client.plugins.quester.Generic;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.NPC;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.plugins.paistisuite.api.*;
import net.runelite.client.plugins.paistisuite.api.WebWalker.api_lib.DaxWalker;
import net.runelite.client.plugins.paistisuite.api.WebWalker.api_lib.WebWalkerServerApi;
import net.runelite.client.plugins.paistisuite.api.WebWalker.api_lib.models.PathResult;
import net.runelite.client.plugins.paistisuite.api.WebWalker.api_lib.models.PathStatus;
import net.runelite.client.plugins.paistisuite.api.WebWalker.api_lib.models.PlayerDetails;
import net.runelite.client.plugins.paistisuite.api.WebWalker.api_lib.models.Point3D;
import net.runelite.client.plugins.paistisuite.api.WebWalker.walker_engine.local_pathfinding.Reachable;
import net.runelite.client.plugins.paistisuite.api.WebWalker.wrappers.RSTile;
import net.runelite.client.plugins.quester.Quester;
import net.runelite.client.plugins.quester.Task;

@Slf4j
public class TalkToNpcTask implements Task {
    String npcName;
    WorldPoint location;
    String talkAction;
    String[] choices;
    String[] backupChoices;
    boolean isCompleted;
    boolean failed;
    int walkAttempts = 0;
    private Quester plugin;
    int talkAttempts = 0;
    int cachedDistance = -1;
    int cachedDistanceTick = -1;

    public TalkToNpcTask(Quester plugin, String npcName, WorldPoint location, String talkAction, String[] choices, String[] backupChoices){
        this.npcName = npcName;
        this.location = location;
        this.talkAction = talkAction;
        this.choices = choices;
        this.backupChoices = backupChoices;
        this.plugin = plugin;
    }

    public TalkToNpcTask(Quester plugin, String npcName, WorldPoint location, String talkAction, String[] choices){
        this.npcName = npcName;
        this.location = location;
        this.talkAction = talkAction;
        this.choices = choices;
        this.backupChoices = null;
        this.plugin = plugin;
    }

    public String name() {
        return "Talk to " + this.npcName;
    }

    public WorldPoint location() {
        return this.location;
    }

    public boolean execute() {
        if (talkAttempts >= 3){
            log.info("Failed talk to npc task. Too many attempts to talk to npc.");
            this.failed = true;
            return false;
        }
        NPC npc = PObjects.findNPC(Filters.NPCs.nameContains(npcName));
        if (npc == null || (walkAttempts < 3 && !Reachable.getMap().canReach(new RSTile(npc.getWorldLocation())))) {
            if (walkAttempts < 3 && plugin.daxWalkTo(location())){
                walkAttempts++;
                PUtils.waitCondition(PUtils.random(2500, 3100), () -> !PPlayer.isMoving() && PPlayer.distanceTo(location()) <= 2);
                log.info("Walked to NPC");
                return true;
            } else if (walkAttempts >= 3){
                this.failed = true;
                log.info("Unable to walk to NPC!");
                return false;
            }
        } else {
            if (!PInteraction.npc(npc, talkAction)) {
                log.info("Unable to intaract with NPC!");
                this.failed = true;
                return false;
            } else {
                PUtils.waitCondition(PUtils.random(800, 1400), () -> PPlayer.isMoving());
                int distance = Reachable.getMap().getDistance(new RSTile(npc.getWorldLocation()));
                if (distance == Integer.MAX_VALUE) distance = (int)Math.round(PPlayer.distanceTo(npc) * 1.5);
                int multiplier = PPlayer.isRunEnabled() ? 300 : 600;
                int timeout = distance * multiplier + (int)PUtils.randomNormal(1300, 1900);
                PUtils.waitCondition(timeout, () -> !PPlayer.isMoving());
                if (!PUtils.waitCondition(PUtils.random(1300, 1900), PDialogue::isConversationWindowUp)){
                    talkAttempts++;
                    log.info("Timed out while waiting for conversation window!");
                    return true;
                } else {
                    if (PDialogue.handleDialogueInOrder(choices) || (this.backupChoices != null && PDialogue.handleDialogueInOrder(backupChoices))){
                        this.isCompleted = true;
                        return true;
                    } else {
                        log.info("Failed at handling talk to npc dialogue!");
                        this.failed = true;
                        return false;
                    }
                }
            }
        }

        return true;
    };

    public boolean condition() {
        return !isCompleted() && !isFailed();
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public boolean isFailed(){
        return this.failed;
    }

    public int getDistance(){
        if (PUtils.getClient().getTickCount() == cachedDistanceTick) return cachedDistance;
        WorldPoint playerLoc = PPlayer.getWorldLocation();
        Point3D playerLocPoint = new Point3D(playerLoc.getX(), playerLoc.getY(), playerLoc.getPlane());
        WorldPoint taskLoc = location();
        Point3D taskLocPoint = new Point3D(taskLoc.getX(), taskLoc.getY(), taskLoc.getPlane());
        PathResult path = WebWalkerServerApi.getInstance().getPath(playerLocPoint, taskLocPoint, PlayerDetails.generate());
        if (path.getPathStatus() == PathStatus.SUCCESS) {
            cachedDistance = path.getCost();
            cachedDistanceTick = PUtils.getClient().getTickCount();
            return cachedDistance;
        } else {
            cachedDistance = Integer.MAX_VALUE;
            cachedDistanceTick = PUtils.getClient().getTickCount();
            return cachedDistance;
        }
    };
}
