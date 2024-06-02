package cloud.viniciusith.arcanus.spell;

import cloud.viniciusith.arcanus.component.entity.PlayerMagicCaster;

import java.util.List;

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

    public static String formatSpellPattern(List<Pattern> patterns) {
        StringBuilder patternStr = new StringBuilder();
        for (Spell.Pattern pattern : patterns) {
            patternStr.append(pattern.getSymbol()).append(" ");
        }
        return patternStr.toString().trim();
    }

}
