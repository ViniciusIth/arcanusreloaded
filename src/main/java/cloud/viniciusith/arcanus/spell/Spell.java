package cloud.viniciusith.arcanus.spell;

import cloud.viniciusith.arcanus.component.entity.PlayerMagicCaster;

public abstract class Spell {

    protected final int manaCost;

    public Spell(int manaCost) {
        this.manaCost = manaCost;
    }

    public abstract void OnCast(PlayerMagicCaster caster);

    public abstract void OnBurnout(PlayerMagicCaster caster);

    public enum Pattern {
        LEFT("L"), RIGHT("R");

        private final String symbol;

        Pattern(String symbol) {
            this.symbol = symbol;
        }

        public String getSymbol() {
            return symbol;
        }
    }

    public int getManaCost() {
        return manaCost;
    }
}
