package theconstrictorpackagemod.patches;

import characterclass.MyCharacter;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.screens.charSelect.CharacterOption;
import com.megacrit.cardcrawl.screens.charSelect.CharacterSelectScreen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import theconstrictorpackagemod.theconstrictormod;

import java.lang.reflect.Field;

import static theconstrictorpackagemod.theconstrictormod.makeID;

public class CharacterSelectScreenPatch {
    private static final float UI_TEXT_SCALE = 0.6f;
    private static final int UI_MAX_LINE_WIDTH = 450;
    private static final int UI_LINE_SPACING = 25;
    private static final int UI_BUTTON_X = 170;
    private static final int BUTTON_WIDTH = 48;
    private static final int BUTTON_HEIGHT = 48;
    private static final float UI_BUTTON_Y = Settings.HEIGHT * 0.35F;
    private static boolean constrictorWasSelectedLastFrame = false;
    private static Hitbox leftArrowHitbox;
    private static Hitbox rightArrowHitbox;

    private static final String[] SKIN_OPTIONS = {"Default", "AVGN", "Frost", "The \"Adventurer\"", "The \"Packmaster\"", "\"Robot Space Explorer\""};

    @SpirePatch(clz = CharacterSelectScreen.class, method = "open")
    public static class OpenPatch {
        @SpirePostfixPatch
        public static void Postfix(CharacterSelectScreen __instance) {
            float centerPointX = Settings.WIDTH * 0.2F;
            float arrowOffset = 180.0F * Settings.scale;

            leftArrowHitbox = new Hitbox(BUTTON_WIDTH * Settings.scale, BUTTON_HEIGHT * Settings.scale);
            leftArrowHitbox.move(centerPointX - arrowOffset, UI_BUTTON_Y);

            rightArrowHitbox = new Hitbox(BUTTON_WIDTH * Settings.scale, BUTTON_HEIGHT * Settings.scale);
            rightArrowHitbox.move(centerPointX + arrowOffset, UI_BUTTON_Y);
        }
    }

    @SpirePatch(clz = CharacterSelectScreen.class, method = "render")
    public static class RenderPatch {
        @SpirePostfixPatch
        public static void Postfix(CharacterSelectScreen __instance, SpriteBatch sb) {
            for (CharacterOption option : __instance.options) {
                if (option.selected && option.c instanceof MyCharacter) {
                    // Calculate text width and center position
                    float textWidth = FontHelper.getSmartWidth(FontHelper.cardTitleFont, "Select Skin:", 9999.0F, 0.0F);
                    float centerPointX = Settings.WIDTH * 0.2F;
                    float textStartX = centerPointX - (textWidth / 2);

                    // Render arrows
                    renderArrow(sb, leftArrowHitbox, ImageMaster.CF_LEFT_ARROW);
                    renderArrow(sb, rightArrowHitbox, ImageMaster.CF_RIGHT_ARROW);

                    // Render "Select Skin:" text
                    FontHelper.renderFontCentered(sb, FontHelper.cardTitleFont, "Select Skin:",
                            centerPointX, UI_BUTTON_Y + 25.0F * Settings.scale, Settings.GOLD_COLOR);

                    // Render selected skin name
                    FontHelper.renderFontCentered(sb, FontHelper.cardTitleFont,
                            SKIN_OPTIONS[theconstrictormod.getSkinIndex()],
                            centerPointX, UI_BUTTON_Y - 10.0F * Settings.scale, Settings.BLUE_TEXT_COLOR);

                    break;
                }
            }
        }

        private static void renderArrow(SpriteBatch sb, Hitbox hitbox, Texture arrowTexture) {
            Color color = hitbox.hovered ? Color.WHITE : Color.LIGHT_GRAY;
            sb.setColor(color);
            sb.draw(arrowTexture, hitbox.cX - 24.0F, hitbox.cY - 24.0F, 24.0F, 24.0F, 48.0F, 48.0F,
                    Settings.scale, Settings.scale, 0.0F, 0, 0, 48, 48, false, false);
        }
    }


    @SpirePatch(clz = CharacterSelectScreen.class, method = "update")
    public static class UpdatePatch {
        @SpirePostfixPatch
        public static void Postfix(CharacterSelectScreen __instance) {
            boolean constrictorIsSelectedNow = false;
            MyCharacter constrictorChar = null;

            for (CharacterOption option : __instance.options) {
                if (option.selected && option.c instanceof MyCharacter) {
                    constrictorIsSelectedNow = true;
                    constrictorChar = (MyCharacter) option.c;

                    // Update hitboxes
                    leftArrowHitbox.update();
                    rightArrowHitbox.update();

                    // Handle arrow clicks
                    if (leftArrowHitbox.hovered && InputHelper.justClickedLeft) {
                        theconstrictormod.decrementSkinIndex(constrictorChar);
                        updateCharacterPortrait(__instance, constrictorChar);
                    }

                    if (rightArrowHitbox.hovered && InputHelper.justClickedLeft) {
                        theconstrictormod.incrementSkinIndex(constrictorChar);
                        updateCharacterPortrait(__instance, constrictorChar);
                    }

                    break;
                }
            }

            // Check if The Constrictor was just selected
            if (constrictorIsSelectedNow && !constrictorWasSelectedLastFrame) {
                // Force update the portrait
                theconstrictormod.incrementSkinIndex(constrictorChar);
                updateCharacterPortrait(__instance, constrictorChar);
                theconstrictormod.decrementSkinIndex(constrictorChar);
                updateCharacterPortrait(__instance, constrictorChar);
            }

            constrictorWasSelectedLastFrame = constrictorIsSelectedNow;
        }
    }

    private static void updateCharacterPortrait(CharacterSelectScreen __instance, MyCharacter myChar) {
        if (__instance.bgCharImg != null) {
            __instance.bgCharImg.dispose();
        }
        __instance.bgCharImg = ImageMaster.loadImage(theconstrictormod.getCharSelectPortrait());
        myChar.updateCharacterSkin(theconstrictormod.getSkinIndex());

        // Update flavor text
        for (CharacterOption optionToUpdate : __instance.options) {
            if (optionToUpdate.c == myChar) {
                try {
                    Field flavorTextField = CharacterOption.class.getDeclaredField("flavorText");
                    flavorTextField.setAccessible(true);
                    flavorTextField.set(optionToUpdate, CardCrawlGame.languagePack.getCharacterString(makeID("CharacterID")).TEXT[theconstrictormod.getSkinIndex()]);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }
}