package theconstrictorpackagemod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theconstrictorpackagemod.powers.ConstrictingPower;

public class IntoItAction extends AbstractGameAction {
    int additionalAmt;

    public IntoItAction(AbstractCreature target, int block, int additional) {
        this.target = target;
        this.amount = block;
        this.additionalAmt = additional;
    }

    public void update() {
        AbstractPower ConPower = AbstractDungeon.player.getPower(ConstrictingPower.POWER_ID);
        if (ConPower != null && ConPower.amount > 0) {
            this.addToTop(new GainBlockAction(this.target, this.amount + this.additionalAmt));
        } else {
            this.addToTop(new GainBlockAction(this.target, this.amount));
        }

        this.isDone = true;
    }
}