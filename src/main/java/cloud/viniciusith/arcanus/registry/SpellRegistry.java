package cloud.viniciusith.arcanus.registry;

import cloud.viniciusith.arcanus.spell.HealSpell;
import cloud.viniciusith.arcanus.spell.RaySpell;
import cloud.viniciusith.arcanus.spell.Spell;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SpellRegistry {
    private static final Map<String, Class<? extends Spell>> SPELLS = new HashMap<>();

    public static Map<String, Class<? extends Spell>> getAllSpells() {
        return SPELLS;
    }

    private static void registerSpell(String name, Class<? extends Spell> spellClass) {
        SPELLS.put(name, spellClass);
    }

    public static Optional<Class<? extends Spell>> getSpellClass(String name) {
        return Optional.ofNullable(SPELLS.get(name));
    }

    public static void registerAllSpells() {
        registerSpell("heal", HealSpell.class);
        registerSpell("ray", RaySpell.class);
    }
}
