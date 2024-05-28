package cloud.viniciusith.arcanus.item.wand;

import cloud.viniciusith.arcanus.item.BaseWandItem;
import net.minecraft.item.Item;
import org.jetbrains.annotations.Nullable;


public class WandItem extends BaseWandItem {
    public WandItem(int maxExperience, int castingCost, @Nullable Item upgrade, Settings settings) {
        super(maxExperience, castingCost, upgrade, settings);
    }
}
