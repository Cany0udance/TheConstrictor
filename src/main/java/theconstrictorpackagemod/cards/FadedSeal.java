package theconstrictorpackagemod.cards;

import characterclass.MyCharacter;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theconstrictorpackagemod.theconstrictormod;
import theconstrictorpackagemod.util.CardInfo;

public class FadedSeal extends BaseCard {
    private static final CardInfo cardInfo;
    public static final String ID;

    public FadedSeal() {
        super(cardInfo);
        this.cost = 1; // Set cost to 1
        this.setBlock(9, 3); // Set block to 9 (upgraded by 3)
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new GainBlockAction(p, p, this.block)); // Gain block when played
    }

    public void triggerOnExhaust() {
        this.addToBot(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, this.block)); // Gain block when exhausted
    }

    static {
        cardInfo = new CardInfo("FadedSeal", 1, CardType.SKILL, CardTarget.SELF, CardRarity.UNCOMMON, MyCharacter.Enums.CARD_COLOR);
        ID = theconstrictormod.makeID(cardInfo.baseId);
    }
}