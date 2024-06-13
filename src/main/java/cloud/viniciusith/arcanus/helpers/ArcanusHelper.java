package cloud.viniciusith.arcanus.helpers;

import cloud.viniciusith.arcanus.spell.base.Spell;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.List;

public class ArcanusHelper {
    public static MutableText getSpellInputs(List<Spell.Pattern> pattern, int index) {
        return index >= pattern.size() || pattern.get(index) == null ? Text.literal("?") : Text.literal(pattern.get(index).getSymbol());
    }
}
