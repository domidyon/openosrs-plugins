package net.runelite.client.plugins.paistisuite.scripts;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.plugins.paistisuite.api.*;
import net.runelite.client.plugins.paistisuite.framework.PScript;

@Slf4j
public class TestScript extends PScript {
    @Override
    protected void loop() {
        PUtils.sleepNormal(3000, 5000);
        log.info("Loop start");
        int count = PObjects.getAllObjectsWithDefs().size();
        PUtils.sendGameMessage(count + " total objects");
    }


    @Override
    protected void onStart() {
        log.info("TestScript started");
    }

    @Override
    protected void onStop() {
        log.info("TestScript stopped");
    }
}
