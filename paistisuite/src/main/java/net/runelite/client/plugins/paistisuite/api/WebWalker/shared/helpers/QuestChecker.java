package net.runelite.client.plugins.paistisuite.api.WebWalker.shared.helpers;

import net.runelite.api.Quest;
import net.runelite.api.QuestState;
import net.runelite.client.plugins.paistisuite.api.PUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuestChecker {

    private static final List<Quest> NMZ_QUESTS = new ArrayList<>(Arrays.asList(
            Quest.THE_ASCENT_OF_ARCEUUS,
            Quest.CONTACT,
            Quest.THE_CORSAIR_CURSE,
            Quest.THE_DEPTHS_OF_DESPAIR,
            Quest.DESERT_TREASURE,
            Quest.DRAGON_SLAYER_I,
            Quest.DREAM_MENTOR,
            Quest.FAIRYTALE_I__GROWING_PAINS,
            Quest.FAMILY_CREST,
            Quest.FIGHT_ARENA,
            Quest.THE_FREMENNIK_ISLES,
            Quest.GETTING_AHEAD,
            Quest.THE_GRAND_TREE,
            Quest.THE_GREAT_BRAIN_ROBBERY,
            Quest.GRIM_TALES,
            Quest.HAUNTED_MINE,
            Quest.HOLY_GRAIL,
            Quest.HORROR_FROM_THE_DEEP,
            Quest.IN_SEARCH_OF_THE_MYREQUE,
            Quest.LEGENDS_QUEST,
            Quest.LOST_CITY,
            Quest.LUNAR_DIPLOMACY,
            Quest.MONKEY_MADNESS_I,
            Quest.MOUNTAIN_DAUGHTER,
            Quest.MY_ARMS_BIG_ADVENTURE,
            Quest.ONE_SMALL_FAVOUR,
            Quest.RECIPE_FOR_DISASTER,
            Quest.ROVING_ELVES,
            Quest.SHADOW_OF_THE_STORM,
            Quest.SHILO_VILLAGE,
            Quest.SONG_OF_THE_ELVES,
            Quest.TALE_OF_THE_RIGHTEOUS,
            Quest.TREE_GNOME_VILLAGE,
            Quest.TROLL_ROMANCE,
            Quest.TROLL_STRONGHOLD,
            Quest.VAMPYRE_SLAYER,
            Quest.WHAT_LIES_BELOW,
            Quest.WITCHS_HOUSE
    ));

    public static int getNmzQuestsCompleted() {
        return (int) NMZ_QUESTS.stream().filter(q -> q.getState(PUtils.getClient()) == QuestState.FINISHED).count();
    }
}