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

    private static final String[] SKIN_OPTIONS = {"Default", "AVGN", "Frost", "The \"Adventurer\"", "The \"Packmaster\"", "Robot Space Explorer"};


    @SpirePatch(clz = CharacterSelectScreen.class, method = SpirePatch.CLASS)
    public static class HitboxFields {
        public static final SpireField<Hitbox> skinLeftHb = new SpireField<>(() -> null);
        public static final SpireField<Hitbox> skinRightHb = new SpireField<>(() -> null);
    }

    @SpirePatch(clz = CharacterSelectScreen.class, method = "initialize")
    public static class InitializeSkinHitboxPatch {
        @SpirePostfixPatch
        public static void initializeSkinHitboxes(CharacterSelectScreen __instance) {
            float textWidth = FontHelper.getSmartWidth(FontHelper.cardTitleFont, "Select Skin:", 9999.0F, 0.0F);

            // Calculate the center position for the text
            float centerPointX = Settings.WIDTH * 0.2F; // Central point of the screen

            // Calculate starting X for the text so it's centered at centerPointX
            float textStartX = centerPointX - (textWidth / 2); // Adjust so text is centered

            float skinHitboxY = Settings.HEIGHT * 0.35F; // Y position of hitboxes

            // Set hitbox positions relative to the centered text
            float arrowOffset = 100.0F * Settings.scale; // Distance from the center of the text to each arrow

            Hitbox skinLeftHb = new Hitbox(40.0F * Settings.scale, 40.0F * Settings.scale);
            skinLeftHb.move(textStartX - arrowOffset, skinHitboxY); // Position left arrow to the left of the text

            Hitbox skinRightHb = new Hitbox(40.0F * Settings.scale, 40.0F * Settings.scale);
            skinRightHb.move(textStartX + textWidth + arrowOffset, skinHitboxY); // Position right arrow to the right of the text

            // Set the hitboxes
            HitboxFields.skinLeftHb.set(__instance, skinLeftHb);
            HitboxFields.skinRightHb.set(__instance, skinRightHb);

        }
    }

    @SpirePatch(clz = CharacterSelectScreen.class, method = "render")
    public static class RenderSkinSelectionPatch {
        @SpirePostfixPatch
        public static void render(CharacterSelectScreen __instance, SpriteBatch sb) {
            for (CharacterOption option : __instance.options) {
                if (option.selected && option.c != null && option.c instanceof MyCharacter) {
                    Hitbox skinLeftHb = HitboxFields.skinLeftHb.get(__instance);
                    Hitbox skinRightHb = HitboxFields.skinRightHb.get(__instance);

                    // Central point for "Select Skin:" text
                    float centralTextX = (skinLeftHb.cX + skinRightHb.cX) / 2;

                    // Render "Select Skin:" centrally between the arrows
                    FontHelper.renderFontCentered(sb, FontHelper.cardTitleFont, "Select Skin:", centralTextX, skinLeftHb.cY + 25.0F * Settings.scale, Settings.GOLD_COLOR);

                    // Central point for "Skin Name" based on screen center, not tied to the left arrow
                    FontHelper.renderFontCentered(sb, FontHelper.cardTitleFont, SKIN_OPTIONS[theconstrictormod.getSkinIndex()], centralTextX, skinLeftHb.cY - 10.0F * Settings.scale, Settings.BLUE_TEXT_COLOR);

                    renderArrow(sb, skinLeftHb, ImageMaster.CF_LEFT_ARROW);
                    renderArrow(sb, skinRightHb, ImageMaster.CF_RIGHT_ARROW);

                    skinLeftHb.render(sb);
                    skinRightHb.render(sb);
                    break; // Exit after finding the selected character
                }
            }
        }
    }

    private static void renderArrow(SpriteBatch sb, Hitbox hitbox, Texture arrowTexture) {
        Color color = hitbox.hovered ? Color.WHITE : Color.LIGHT_GRAY;
        sb.setColor(color);
        sb.draw(arrowTexture, hitbox.cX - 24.0F, hitbox.cY - 24.0F, 24.0F, 24.0F, 48.0F, 48.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 48, 48, false, false);
    }

    @SpirePatch(clz = CharacterSelectScreen.class, method = "update")
    public static class UpdateSkinSelectionPatch {
        private static boolean wasMyCharacterSelectedLastFrame = false;
        private static final String ID = makeID("CharacterID");
        private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString(ID);

        @SpirePostfixPatch
        public static void update(CharacterSelectScreen __instance) {
            boolean isMyCharacterCurrentlySelected = false;
            boolean skinIndexChanged = false;
            MyCharacter myChar = null;

            for (CharacterOption option : __instance.options) {
                option.update();

                if (option.selected && option.c instanceof MyCharacter) {
                    isMyCharacterCurrentlySelected = true;
                    myChar = (MyCharacter) option.c;
                    Hitbox skinLeftHb = HitboxFields.skinLeftHb.get(__instance);
                    Hitbox skinRightHb = HitboxFields.skinRightHb.get(__instance);

                    skinLeftHb.update();
                    skinRightHb.update();

                    if (InputHelper.justClickedLeft && skinLeftHb.hovered) {
                        skinLeftHb.clickStarted = true;
                        if (skinLeftHb.clicked) {
                            skinLeftHb.clicked = false;
                            theconstrictormod.decrementSkinIndex(myChar);
                            skinIndexChanged = true;
                        }
                    }

                    if (InputHelper.justClickedLeft && skinRightHb.hovered) {
                        skinRightHb.clickStarted = true;
                        if (skinRightHb.clicked) {
                            skinRightHb.clicked = false;
                            theconstrictormod.incrementSkinIndex(myChar);
                            skinIndexChanged = true;
                        }
                    }

                    // Ensure to update immediately if not selected last frame or if the skin changed
                    if (!wasMyCharacterSelectedLastFrame || skinIndexChanged) {
                        if (__instance.bgCharImg != null) {
                            __instance.bgCharImg.dispose();
                        }
                        __instance.bgCharImg = ImageMaster.loadImage(theconstrictormod.getCharSelectPortrait());
                        myChar.updateCharacterSkin(theconstrictormod.getSkinIndex());

                        // Update flavor text using reflection
                        for (CharacterOption optionToUpdate : __instance.options) {
                            if (optionToUpdate.c == myChar) {
                                try {
                                    Field flavorTextField = CharacterOption.class.getDeclaredField("flavorText");
                                    flavorTextField.setAccessible(true);
                                    flavorTextField.set(optionToUpdate, characterStrings.TEXT[theconstrictormod.getSkinIndex()]);
                                } catch (NoSuchFieldException | IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                                break;
                            }
                        }
                    }

                    break; // Exit after finding the selected character
                }
            }

            // Update the static tracking variable at the end of the update cycle
            wasMyCharacterSelectedLastFrame = isMyCharacterCurrentlySelected;
        }
    }
}