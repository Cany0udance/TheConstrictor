package theconstrictorpackagemod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.screens.stats.StatsScreen;
import theconstrictorpackagemod.theconstrictormod;

@SpirePatch(clz = StatsScreen.class, method = "update")
public class UpdateAchievements {
    public static void Postfix(StatsScreen __instance) {
        theconstrictormod.constrictorAchievementGrid.update();
    }
}