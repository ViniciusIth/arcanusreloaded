package cloud.viniciusith.arcanus.registry;

import cloud.viniciusith.arcanus.ArcanusReloaded;
import cloud.viniciusith.arcanus.item.GrimoireItem;
import cloud.viniciusith.arcanus.item.SpellPageItem;
import cloud.viniciusith.arcanus.item.WandItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ItemRegister {

    private static final RegistryKey<ItemGroup> ARCANUS_ITEM_GROUP = RegistryKey.of(RegistryKeys.ITEM_GROUP, new Identifier(ArcanusReloaded.MODID, "arcanus_group"));

    public static Item MASTER_WAND = registerNewItem(new WandItem(8200, 1, null, new FabricItemSettings().maxCount(1)), "master_wand", ARCANUS_ITEM_GROUP);
    public static Item ADEPT_WAND = registerNewItem(new WandItem(4200, 2, MASTER_WAND, new FabricItemSettings().maxCount(1)), "adept_wand", ARCANUS_ITEM_GROUP);
    public static Item NOVICE_WAND = registerNewItem(new WandItem(1200, 3, ADEPT_WAND, new FabricItemSettings().maxCount(1)), "novice_wand", ARCANUS_ITEM_GROUP);

    public static Item GRIMOIRE = registerNewItem(new GrimoireItem(new FabricItemSettings().maxCount(1)), "grimoire", ARCANUS_ITEM_GROUP);


    private static void registerSpellPages() {
        SpellPageItem spellPageItem = Registry.register(
                Registries.ITEM,
                new Identifier(ArcanusReloaded.MODID, "spell_page"),
                new SpellPageItem(new FabricItemSettings().maxCount(1))
        );

        SpellRegistry.getAllSpells().forEach((name, spellFactory) -> {
            ItemStack spellPageItemStack = new ItemStack(spellPageItem);

            SpellPageItem.addSpell(spellPageItemStack, name);

            ItemGroupEvents.modifyEntriesEvent(ARCANUS_ITEM_GROUP).register(entries -> entries.add(spellPageItemStack));
        });
    }

    @SuppressWarnings("SameParameterValue")
    private static Item registerNewItem(Item item, String itemPath, RegistryKey<ItemGroup> itemGroup) {
        Item newItem = Registry.register(
                Registries.ITEM,
                new Identifier(ArcanusReloaded.MODID, itemPath),
                item
        );

        ItemGroupEvents.modifyEntriesEvent(itemGroup).register(content -> content.add(item));
        return newItem;
    }

    public static void RegisterAll() {
        Registry.register(Registries.ITEM_GROUP, ARCANUS_ITEM_GROUP, FabricItemGroup.builder()
                .icon(() -> new ItemStack(MASTER_WAND))
                .displayName(Text.translatable("item.arcanus.item_group"))
                .build());

        registerSpellPages();
    }
}
