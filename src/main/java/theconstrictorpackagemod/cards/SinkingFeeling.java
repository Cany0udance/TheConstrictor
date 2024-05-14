package theconstrictorpackagemod.cards;

import characterclass.MyCharacter;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.GraveField;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theconstrictorpackagemod.powers.ConstrictingPower;
import theconstrictorpackagemod.util.CardInfo;

import java.util.Iterator;

import static theconstrictorpackagemod.theconstrictormod.makeID;

public class SinkingFeeling extends BaseCard {
    private final static CardInfo cardInfo = new CardInfo(
            "SinkingFeeling",
            2,
            CardType.ATTACK,
            CardTarget.ALL,
            CardRarity.BASIC,
            MyCharacter.Enums.CARD_COLOR);

    public static final String ID = makeID(cardInfo.baseId);

    public SinkingFeeling() {
        super(cardInfo);
        setMagic(2);
        GraveField.grave.set(this, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Iterator<AbstractMonster> var3 = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();
        int baseDamage = 0; // This is a new base damage, adjust as needed for balance.

        while (var3.hasNext()) {
            AbstractMonster mo = var3.next();
            int totalDamage = baseDamage;

            if (mo.hasPower(ConstrictingPower.POWER_ID)) {
                int constrictAmount = mo.getPower(ConstrictingPower.POWER_ID).amount;
                totalDamage += constrictAmount * this.magicNumber; // Add the damage based on Constricting.
            }

            // Create a temporary DamageInfo object for modifying the damage based on player's buffs/debuffs.
            DamageInfo info = new DamageInfo(p, totalDamage, DamageInfo.DamageType.NORMAL);
            info.applyPowers(p, mo); // This method adjusts the damage based on player and monster powers.

            // Now use the possibly modified damage from the info object
            this.addToBot(new DamageAction(mo, info, AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        }
    }

    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBaseCost(1);
        }

    }

}

