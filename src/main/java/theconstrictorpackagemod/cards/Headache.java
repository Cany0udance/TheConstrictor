
package theconstrictorpackagemod.cards;


import characterclass.MyCharacter;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theconstrictorpackagemod.util.CardInfo;

import static theconstrictorpackagemod.theconstrictormod.makeID;

public class Headache extends BaseCard {
    private final static CardInfo cardInfo = new CardInfo(
            "Headache",
            -2,
            CardType.SKILL,
            CardTarget.SELF,
            CardRarity.UNCOMMON,
            MyCharacter.Enums.CARD_COLOR);

    public static final String ID = makeID(cardInfo.baseId);

    public Headache() {
        super(cardInfo);
        this.cardsToPreview = new ShiningLight();
        setSelfRetain(false, true);
        setMagic(1,1);
    }

    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        this.cantUseMessage = "This card is unplayable!";
        return false;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new MakeTempCardInHandAction(new ShiningLight(), 1));
    }

    public void triggerOnExhaust() {
            this.addToBot(new MakeTempCardInHandAction(new ShiningLight(), 1));
        }

    }
