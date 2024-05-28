package cloud.viniciusith.arcanus.registry;

import cloud.viniciusith.arcanus.ArcanusReloaded;
import cloud.viniciusith.arcanus.item.wand.WandItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class ItemRegister {
    public static Item MASTER_WAND = registerNewItem(new WandItem(3200, 3, null, new FabricItemSettings().maxCount(1)), "master_wand", ItemGroups.TOOLS);
    public static Item ADEPT_WAND = registerNewItem(new WandItem(3200, 3, MASTER_WAND, new FabricItemSettings().maxCount(1)), "adept_wand", ItemGroups.TOOLS);
    public static Item NOVICE_WAND = registerNewItem(new WandItem(3200, 3, ADEPT_WAND, new FabricItemSettings().maxCount(1)), "novice_wand", ItemGroups.TOOLS);

    private static Item registerNewItem(Item item, String itemPath, RegistryKey<ItemGroup> itemGroup) {
        Item newItem = Registry.register(
                Registries.ITEM,
                new Identifier(ArcanusReloaded.MODID, itemPath),
                item
        );

        ItemGroupEvents.modifyEntriesEvent(itemGroup).register(content -> content.add(item));
        return newItem;
    }

    public static void RegisterAll() {}
}
