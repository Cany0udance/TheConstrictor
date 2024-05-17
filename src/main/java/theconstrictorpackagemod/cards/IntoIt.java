package theconstrictorpackagemod.cards;

import characterclass.MyCharacter;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theconstrictorpackagemod.actions.IntoItAction;
import theconstrictorpackagemod.util.CardInfo;

import static theconstrictorpackagemod.theconstrictormod.makeID;

public class IntoIt extends BaseCard {
    private final static CardInfo cardInfo = new CardInfo(
            "IntoIt",
            0,
            CardType.SKILL,
            CardTarget.SELF,
            CardRarity.COMMON,
            MyCharacter.Enums.CARD_COLOR);

    public static final String ID = makeID(cardInfo.baseId);

    public IntoIt() {
        super(cardInfo);
        this.baseBlock = 3;
        this.baseMagicNumber = 9;
        this.magicNumber = this.baseMagicNumber;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.applyPowers();
        this.addToBot(new IntoItAction(p, this.block, this.magicNumber));
    }

    @Override
    public void applyPowers() {
        int baseMagic = this.baseMagicNumber;
        super.applyPowers();
        int blockDiff = this.baseMagicNumber - this.baseBlock;
        this.magicNumber = this.block + blockDiff;
        this.isMagicNumberModified = this.magicNumber != baseMagic;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(3);
        }
    }
}