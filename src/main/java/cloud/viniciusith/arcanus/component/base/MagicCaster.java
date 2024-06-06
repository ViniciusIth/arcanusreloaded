// Based off Arcanus-Legacy
package cloud.viniciusith.arcanus.component.base;

import cloud.viniciusith.arcanus.spell.Spell;
import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.entity.LivingEntity;

import java.util.Map;

public interface MagicCaster extends Component {
    int getMaxMana();

    int getMana();

    void setMana(int mana);

    default void addMana(int amount) {setMana(getMana() + amount);}

    default void reduceMana(int amount) {setMana(getMana() - amount);}

    default void restoreMaxMana() {setMana(getMaxMana());}

    /**
     * Returns whether the cast spell uses more mana than the
     * mana the caster currently holds.
     */
    default boolean shouldBurnout(int manaCost) {
        return getMana() - manaCost < 0;
    }

    void cast(Spell spell);

    void addActiveSpell(Spell spell, int activeTicks);

    Map<Spell, Integer> getActiveSpells();

    void clearActiveSpell();


    LivingEntity asEntity();
}
