package theconstrictorpackagemod.relics;

import characterclass.MyCharacter;

import static theconstrictorpackagemod.theconstrictormod.makeID;

public class BorrowedSkull extends BaseRelic {
    private static final String NAME = "BorrowedSkull"; //The name will be used for determining the image file as well as the ID.
    public static final String ID = makeID(NAME); //This adds the mod's prefix to the relic ID, resulting in modID:MyRelic
    private static final RelicTier RARITY = RelicTier.BOSS; //The relic's rarity.
    private static final LandingSound SOUND = LandingSound.CLINK; //The sound played when the relic is clicked.

    public BorrowedSkull() {
        super(ID, NAME, MyCharacter.Enums.CARD_COLOR, RelicTier.BOSS, LandingSound.HEAVY);
    }

    @Override
    public String getUpdatedDescription() {
        return String.format(DESCRIPTIONS[0]);
    }
}