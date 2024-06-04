package cloud.viniciusith.arcanus.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class WandItem extends Item {
    private final int maxExperience;
    private final int castingCostMultiplier;
    private final Item upgrade;


    public WandItem(int maxExperience, int castingCostMultiplier, @Nullable Item upgrade, Settings settings) {
        super(settings);
        this.castingCostMultiplier = castingCostMultiplier;
        this.maxExperience = maxExperience;
        this.upgrade = upgrade;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        NbtCompound nbt = stack.getOrCreateNbt();
        int xp = nbt != null ? nbt.getInt("Exp") : 0;
        tooltip.add(Text.literal(xp + "/" + maxExperience).append(" Exp").formatted(Formatting.DARK_AQUA));
        if (hasUpgrade()) {
            tooltip.add(Text.translatable("tooltip.arcanus.wand_upgrade").formatted(Formatting.DARK_AQUA));
        }
    }

    public boolean hasUpgrade() {
        return upgrade != null;
    }

    public static Optional<ItemStack> findGrimoire(PlayerEntity caster) {
        PlayerInventory casterInventory = caster.getInventory();

        for (int i = 0; i < casterInventory.size(); i++) {
            if (casterInventory.getStack(i).getItem() instanceof GrimoireItem)
                return Optional.of(casterInventory.getStack(i));
        }

        return Optional.empty();
    }

    public int getMaxExperience() {
        return maxExperience;
    }

    public int getCastingCostMultiplier() {
        return castingCostMultiplier;
    }

    public Item getUpgrade() {
        return upgrade;
    }
}
