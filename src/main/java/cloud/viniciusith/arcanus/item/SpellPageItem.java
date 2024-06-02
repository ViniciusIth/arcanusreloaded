package cloud.viniciusith.arcanus.item;

import cloud.viniciusith.arcanus.ArcanusReloaded;
import cloud.viniciusith.arcanus.registry.SpellRegistry;
import cloud.viniciusith.arcanus.spell.Spell;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SpellPageItem extends Item {
    private static final String SPELL_KEY = "Spell";
    private static final String SPELL_PATTERN_KEY = "Pattern";

    public SpellPageItem(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        Optional<Spell> spellOpt = getSpell(stack);
        if (spellOpt.isPresent()) {
            Spell spell = spellOpt.get();
            tooltip.add(Text.literal("Spell: " + getSpellName(stack).get()));
            tooltip.add(Text.literal("Mana Cost: " + spell.getManaCost()));
        } else {
            tooltip.add(Text.literal("Unknown Spell"));
        }
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return getSpellName(stack).isPresent();
    }

    public Optional<Spell> getSpell(ItemStack stack) {
        if (!(stack.getItem() instanceof SpellPageItem)) {
            return Optional.empty();
        }

        NbtCompound spellNBT = stack.getOrCreateNbt();
        if (!spellNBT.contains(SPELL_KEY)) return Optional.empty();

        String spellName = spellNBT.getString(SPELL_KEY);

        Optional<Class<? extends Spell>> spellClass = SpellRegistry.getSpellClass(spellName);
        if (spellClass.isEmpty()) return Optional.empty();

        try {
            return Optional.of(spellClass.get().getDeclaredConstructor().newInstance());
        } catch (Exception e) {
            ArcanusReloaded.LOGGER.error(e);
            return Optional.empty();
        }
    }

    public Optional<String> getSpellName(ItemStack stack) {
        if (!(stack.getItem() instanceof SpellPageItem)) {
            return Optional.empty();
        }

        NbtCompound spellNBT = stack.getOrCreateNbt();
        if (!spellNBT.contains(SPELL_KEY)) return Optional.empty();

        return Optional.of(spellNBT.getString(SPELL_KEY));
    }

    public Optional<ArrayList<Spell.Pattern>> getSpellPattern(ItemStack stack) {
        if (!(stack.getItem() instanceof SpellPageItem)) {
            return Optional.empty();
        }

        NbtCompound pageNBT = stack.getOrCreateNbt();

        if (!pageNBT.contains(SPELL_PATTERN_KEY)) return Optional.empty();

        NbtList spellPatternNBT = pageNBT.getList(SPELL_PATTERN_KEY, NbtList.STRING_TYPE);

        ArrayList<Spell.Pattern> spellPattern = new ArrayList<>(3);
        for (int i = 0; i < spellPatternNBT.size(); i++) {
            spellPattern.add(Spell.Pattern.valueOf(spellPatternNBT.getString(i)));
        }

        return Optional.of(spellPattern);
    }

    public static ItemStack addSpell(ItemStack stack, String spellName) {
        NbtCompound nbt = stack.getOrCreateNbt();
        nbt.putString(SPELL_KEY, spellName);
        stack.setNbt(nbt);

        return stack;
    }
}
