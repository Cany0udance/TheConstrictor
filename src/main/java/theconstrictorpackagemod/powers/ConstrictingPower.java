package theconstrictorpackagemod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;


import static theconstrictorpackagemod.theconstrictormod.makeID;

public class ConstrictingPower extends BasePower implements CloneablePowerInterface, HealthBarRenderPower {
    public static final String POWER_ID = makeID("ConstrictingPower");
    private static final AbstractPower.PowerType TYPE = PowerType.DEBUFF;
    private static final boolean TURN_BASED = true;

    public ConstrictingPower(AbstractCreature owner, AbstractCreature source, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, source, amount);
        canGoNegative = true;
    }

    @Override
    public void atStartOfTurn() {
        if (!this.owner.isPlayer && AbstractDungeon.player.hasRelic(BorrowedSkull.ID)) {
            if (this.amount >= 1) {
                AbstractDungeon.player.getRelic(BorrowedSkull.ID).flash();
                this.addToBot(new DamageAction(this.owner, new DamageInfo(this.source, this.amount, DamageInfo.DamageType.THORNS)));
            }
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (this.owner.isPlayer || !AbstractDungeon.player.hasRelic(BorrowedSkull.ID)) {
            if (this.amount >= 1) {
                this.addToBot(new DamageAction(this.owner, new DamageInfo(this.source, this.amount, DamageInfo.DamageType.THORNS)));
            }
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public AbstractPower makeCopy() {
        return new ConstrictingPower(owner, this.source, amount);
    }

    @Override
    public int getHealthBarAmount() {
        if (this.amount >= 1) {
            return amount;
        } else {
            return 0;
        }
    }

    @Override
    public Color getColor() {
        return Color.valueOf("#2c971d");
    }
}