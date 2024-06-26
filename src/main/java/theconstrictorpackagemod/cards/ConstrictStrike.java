package theconstrictorpackagemod.cards;


import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import theconstrictorpackagemod.powers.ConstrictingPower;
import theconstrictorpackagemod.util.CardInfo;
import characterclass.MyCharacter;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static theconstrictorpackagemod.theconstrictormod.makeID;

public class ConstrictStrike extends BaseCard {
    private final static CardInfo cardInfo = new CardInfo(
            "ConstrictStrike",
            1,
            CardType.ATTACK,
            CardTarget.ENEMY,
            CardRarity.COMMON,
            MyCharacter.Enums.CARD_COLOR);

    public static final String ID = makeID(cardInfo.baseId);

    private static final int DAMAGE = 12;
    private static final int UPG_DAMAGE = 2;


    public ConstrictStrike() {
        super(cardInfo);
        tags.add(CardTags.STRIKE);
        setDamage(DAMAGE, UPG_DAMAGE);
        setMagic(2,2);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

        addToBot(new DamageAction(m, new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
        this.addToBot(new ApplyPowerAction(m, p, new ConstrictingPower(m, p, this.magicNumber), this.magicNumber));
        this.addToBot(new ApplyPowerAction(p, p, new ConstrictingPower(p, p, 2), 2));

    }

}

