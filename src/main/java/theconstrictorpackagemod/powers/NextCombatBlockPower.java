package theconstrictorpackagemod.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;

import static theconstrictorpackagemod.theconstrictormod.makeID;

public class NextCombatBlockPower extends BasePower {
    public static final String POWER_ID = makeID("NextCombatBlockPower");

    public NextCombatBlockPower(AbstractCreature owner, int blockAmount) {
        super(POWER_ID, PowerType.BUFF, false, owner, blockAmount);
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }
}
