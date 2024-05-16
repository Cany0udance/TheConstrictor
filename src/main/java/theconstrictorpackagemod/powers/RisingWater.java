package theconstrictorpackagemod.powers;

import basemod.interfaces.CloneablePowerInterface;
import characterclass.MyCharacter;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theconstrictorpackagemod.util.ConstrictorAchievementUnlocker;


import java.util.Iterator;

import static theconstrictorpackagemod.theconstrictormod.makeID;

public class RisingWater extends BasePower implements CloneablePowerInterface {
    public static final String POWER_ID = makeID("RisingWater");
    private static final AbstractPower.PowerType TYPE = PowerType.BUFF;
    private static final boolean TURN_BASED = true;

    public RisingWater(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount);
    }

    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
            this.flash();
            Iterator var2 = AbstractDungeon.getMonsters().monsters.iterator();
            this.addToBot(new DamageAllEnemiesAction((AbstractCreature)null, DamageInfo.createDamageMatrix(this.amount, true), DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.FIRE, true));
            this.addToBot(new ApplyPowerAction(this.owner, this.owner, new RisingWater(this.owner, 1), 1));
        }
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        if (this.amount >= 50) {
            AbstractPlayer p = AbstractDungeon.player;
            if (p != null && p instanceof MyCharacter) {
                ConstrictorAchievementUnlocker.unlockAchievement(theconstrictorpackagemod.theconstrictormod.makeID("I_AM_THE_TIDE"));
            }
        }
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public AbstractPower makeCopy() {
        return new RisingWater(owner, amount);
    }

}
