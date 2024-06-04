package cloud.viniciusith.arcanus.item;

import cloud.viniciusith.arcanus.item.grimoire.GrimoireScreenHandler;
import cloud.viniciusith.arcanus.spell.Spell;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Optional;

public class GrimoireItem extends Item {
    public static final String SPELL_PAGES_KEY = "SpellPages";
    public static final int MAX_SPELL_SLOTS = 9; // cannot be any bigger because of how the mod works

    public GrimoireItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient()) {
            world.playSound(user, user.getBlockPos(), SoundEvents.ITEM_BOOK_PAGE_TURN, SoundCategory.PLAYERS, 1, 1);
        }

        openScreen(user, user.getStackInHand(hand));
        return TypedActionResult.success(user.getStackInHand(hand));
    }

    public static void openScreen(PlayerEntity player, ItemStack grimoireItemStack) {
        if (player.getWorld() != null && !player.getWorld().isClient()) {
            player.openHandledScreen(new ExtendedScreenHandlerFactory() {
                @Override
                public void writeScreenOpeningData(ServerPlayerEntity serverPlayerEntity, PacketByteBuf packetByteBuf) {
                    packetByteBuf.writeItemStack(grimoireItemStack);
                }

                @Override
                public Text getDisplayName() {
                    return Text.translatable(grimoireItemStack.getItem().getTranslationKey());
                }

                @Override
                public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
                    return new GrimoireScreenHandler(syncId, inv, grimoireItemStack);
                }
            });
        }
    }

    public static DefaultedList<ItemStack> findSpellPages(ItemStack stack) {
        DefaultedList<ItemStack> spellPages = DefaultedList.ofSize(MAX_SPELL_SLOTS, ItemStack.EMPTY);
        NbtCompound grimoireNbt = stack.getOrCreateNbt();
        if (grimoireNbt.contains(SPELL_PAGES_KEY)) {
            NbtList spellPagesNbt = grimoireNbt.getList(SPELL_PAGES_KEY, NbtElement.COMPOUND_TYPE);
            for (int i = 0; i < spellPagesNbt.size(); i++) {
                ItemStack itemStack = ItemStack.fromNbt((NbtCompound) spellPagesNbt.getCompound(i).get("Stack"));
                spellPages.set(i, itemStack);
            }

        }

        return spellPages;
    }

    public static ItemStack findSpellByPattern(ItemStack stack, ArrayList<Spell.Pattern> pattern) {
        NbtCompound grimoireNbt = stack.getOrCreateNbt();

        if (grimoireNbt.contains(SPELL_PAGES_KEY)) {
            NbtList spellPagesNbt = grimoireNbt.getList(SPELL_PAGES_KEY, NbtElement.COMPOUND_TYPE);
            for (int i = 0; i < spellPagesNbt.size(); i++) {
                ItemStack spellPage = ItemStack.fromNbt((NbtCompound) spellPagesNbt.getCompound(i).get("Stack"));
                Optional<ArrayList<Spell.Pattern>> spellPattern = SpellPageItem.getSpellPattern(spellPage);
                if (spellPattern.isPresent() && spellPattern.get().equals(pattern)) {
                    return spellPage;
                }
            }
        }

        return null;
    }
}
