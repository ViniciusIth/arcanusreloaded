package cloud.viniciusith.arcanus.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BaseWandItem extends Item {
    private final int maxExperience;
    private final int castingCost;
    private final Item upgrade;

    public BaseWandItem(int maxExperience, int castingCost, @Nullable Item upgrade, Settings settings) {
        super(settings);
        this.castingCost = castingCost;
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
}
