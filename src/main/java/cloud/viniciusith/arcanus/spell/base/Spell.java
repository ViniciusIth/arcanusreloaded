package cloud.viniciusith.arcanus.spell.base;

import cloud.viniciusith.arcanus.component.base.MagicCaster;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.List;

public abstract class Spell {

    protected final int manaCost;

    public Spell(int manaCost) {
        this.manaCost = manaCost;
    }

    public static String formatSpellPattern(List<Pattern> patterns) {
        StringBuilder patternStr = new StringBuilder();
        for (Spell.Pattern pattern : patterns) {
            patternStr.append(pattern.getSymbol()).append(" ");
        }
        return patternStr.toString().trim();
    }

    public abstract void OnCast(MagicCaster caster);

    public abstract void OnBurnout(ServerPlayerEntity caster);

    public int getManaCost() {
        return manaCost;
    }

    public enum Pattern {
        LEFT("L"), RIGHT("R");

        public static final int MAX_SIZE = 3;
        private final String symbol;

        Pattern(String symbol) {
            this.symbol = symbol;
        }

        public static Pattern fromSymbol(String symbol) {
            for (Pattern pattern : values()) {
                if (pattern.getSymbol().equals(symbol)) {
                    return pattern;
                }
            }
            throw new IllegalArgumentException("Unknown symbol: " + symbol);
        }

        public String getSymbol() {
            return symbol;
        }
    }
}
