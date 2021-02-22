package net.runelite.client.plugins.quester.Generic.ItemAcquisition;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.NPC;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.plugins.paistisuite.PShopping;
import net.runelite.client.plugins.paistisuite.api.*;
import net.runelite.client.plugins.paistisuite.api.WebWalker.api_lib.WebWalkerServerApi;
import net.runelite.client.plugins.paistisuite.api.WebWalker.api_lib.models.PathResult;
import net.runelite.client.plugins.paistisuite.api.WebWalker.api_lib.models.PathStatus;
import net.runelite.client.plugins.paistisuite.api.WebWalker.api_lib.models.PlayerDetails;
import net.runelite.client.plugins.paistisuite.api.WebWalker.api_lib.models.Point3D;
import net.runelite.client.plugins.paistisuite.api.WebWalker.walker_engine.local_pathfinding.Reachable;
import net.runelite.client.plugins.paistisuite.api.WebWalker.wrappers.RSTile;
import net.runelite.client.plugins.paistisuite.api.types.PGroundItem;
import net.runelite.client.plugins.quester.Quester;
import net.runelite.client.plugins.quester.Task;

import java.util.List;

@Slf4j
public class PickFromGroundTask implements Task {
    boolean failed;
    private String itemName;
    private Quester plugin;
    private WorldPoint location;
    private int quantity;
    boolean checkReachable;
    private boolean isCompleted = false;
    int walkAttempts = 0;
    int grabAttempts = 0;
    int cachedDistance = -1;
    int cachedDistanceTick = -1;
    int hopAttempts = 0;

    public PickFromGroundTask(Quester plugin, String itemName, int quantity, WorldPoint location, boolean checkReachable){
        this.plugin = plugin;
        this.itemName = itemName;
        this.location = location;
        this.quantity = quantity;
        this.checkReachable = checkReachable;
    }

    public String name() {
        return "Pick " + this.itemName + " from the ground.";
    }

    public WorldPoint location() {
        return this.location;
    }

    public boolean execute() {
        if (grabAttempts > 3 || hopAttempts > 8){
            this.failed = true;
            log.info("Too many tries trying to grab item " + itemName + " from ground!");
            return false;
        }
        List<PGroundItem> items = PGroundItems.findGroundItems(
                Filters.GroundItems.nameContainsOrIdEquals(itemName)
        .and(item -> !checkReachable || Reachable.getMap().canReach(new RSTile(item.getLocation()))));

        if (items.size() == 0) {
            if (PPlayer.getWorldLocation().distanceTo(location()) <= 5){
                PWorldHopper.hop();
                hopAttempts++;
                PUtils.sleepNormal(1500, 3000);
            }
            if (walkAttempts < 3 && plugin.daxWalkTo(location())){
                walkAttempts++;
                PUtils.waitCondition(PUtils.random(2500, 3100), () -> !PPlayer.isMoving() && PPlayer.distanceTo(location()) <= 2);
                log.info("Walked to item location");
                return true;
            } else if (walkAttempts >= 3){
                this.failed = true;
                log.info("Unable to item location");
                return false;
            }
        } else {
            int countBefore = PInventory.getCount(itemName);
            if (!PInteraction.groundItem(items.get(0), "Take")) {
                log.info("Unable take " + itemName + " from the ground!");
                this.failed = true;
                return false;
            } else {
                int distance = checkReachable ? Reachable.getMap().getDistance(new RSTile(items.get(0).getLocation())) : 10;
                if (checkReachable && distance == Integer.MAX_VALUE) {
                    grabAttempts++;
                    return true;
                }
                PUtils.waitCondition(PUtils.random(800, 1400), () -> PPlayer.isMoving());
                int multiplier = PPlayer.isRunEnabled() ? 300 : 600;
                int timeout = distance * multiplier + (int)PUtils.randomNormal(1800, 2500);
                if (PUtils.waitCondition(timeout, () -> PInventory.getCount(itemName) > countBefore)){
                    if (PInventory.getCount(itemName) > quantity) this.isCompleted = true;
                    grabAttempts = 0;
                    walkAttempts = 0;
                    return true;
                } else {
                    grabAttempts++;
                    return true;
                }
            }
        }
        return true;
    };

    public boolean condition() {
        int currentCount = PInventory.getCount(itemName);
        return !isCompleted() && !isFailed() && currentCount < quantity;
    }

    public boolean isCompleted() {
        return this.isCompleted || PInventory.getCount(itemName) >= quantity;
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