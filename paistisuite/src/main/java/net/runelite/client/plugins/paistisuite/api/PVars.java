package net.runelite.client.plugins.paistisuite.api;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.VarPlayer;

@Slf4j
public class PVars {
    public static int[] getSettingArray() {
        int[] settingArray = PUtils.getClient().getVarps();
        if (settingArray == null) {
            return new int[0];
        }
        return settingArray.clone();
    }

    public static int getSetting(final int setting) {
        int[] settings = getSettingArray();
        if (setting < settings.length) {
            return settings[setting];
        }
        return -1;
    }

    public static int getVarp(final int varpId) {
        int[] settings = getSettingArray();
        if (varpId < settings.length) {
            return settings[varpId];
        }
        return -1;
    }

    public static int getVarp(final VarPlayer varpInfo) {
        int[] settings = getSettingArray();
        if (varpInfo.getId() < settings.length) {
            return settings[varpInfo.getId()];
        }
        return -1;
    }

    public static Integer getVarbit(final int varbitId) {
        return PUtils.clientOnly(() -> PUtils.getClient().getVarbitValue(varbitId), "getVarbitValue");
    }
}
