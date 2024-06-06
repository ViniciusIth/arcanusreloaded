// Based off Arcanus-Legacy
package cloud.viniciusith.arcanus.component.base;

import cloud.viniciusith.arcanus.spell.Spell;
import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Map;
import java.util.Optional;

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

    void clearActiveSpells();

    LivingEntity asEntity();

    default Optional<ServerPlayerEntity> asPlayer() {
        if (asEntity() instanceof ServerPlayerEntity)
            return Optional.of((ServerPlayerEntity) asEntity());
        return Optional.empty();
    }
}
