package theconstrictorpackagemod.patches;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.screens.stats.StatsScreen;

@SpirePatch2(
        clz = StatsScreen.class,
        method = "calculateScrollBounds"
)
public class StatScreenScrollbarPatch {
    public static Integer NEW_ROWS = 1;

    public StatScreenScrollbarPatch() {
    }

    @SpirePostfixPatch
    public static void Postfix(StatsScreen __instance) {
        float currentUpperBound = (Float) ReflectionHacks.getPrivate(__instance, StatsScreen.class, "scrollUpperBound");
        ReflectionHacks.setPrivate(__instance, StatsScreen.class, "scrollUpperBound", currentUpperBound + (float)(180 * NEW_ROWS) * Settings.scale);
    }
}
