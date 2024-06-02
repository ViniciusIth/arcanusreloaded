package cloud.viniciusith.arcanus.item.grimoire;

import cloud.viniciusith.arcanus.ArcanusReloaded;
import cloud.viniciusith.arcanus.helpers.InventoryUtils;
import cloud.viniciusith.arcanus.item.GrimoireItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.collection.DefaultedList;

import java.util.Arrays;

public class GrimoireScreenHandler extends ScreenHandler {
    private final DefaultedList<ItemStack> spellPages;

    public GrimoireScreenHandler(int synchronizationID, PlayerInventory playerInventory, PacketByteBuf packetByteBuf) {
        this(synchronizationID, playerInventory, packetByteBuf.readItemStack());
    }

    public GrimoireScreenHandler(int syncId, PlayerInventory playerInventory, ItemStack grimoireItemStack) {
        super(ArcanusReloaded.GRIMOIRE_CONTAINER_TYPE, syncId); // You'll need to replace null with the actual ScreenHandlerType
        this.spellPages = DefaultedList.ofSize(9, ItemStack.EMPTY); // Change 15 to the actual number of slots

        if (grimoireItemStack.getItem() instanceof GrimoireItem grimoireItem) {
            ArcanusReloaded.LOGGER.debug(Arrays.toString(grimoireItem.getSpellPages(grimoireItemStack).toArray()));
            DefaultedList<ItemStack> pages = grimoireItem.getSpellPages(grimoireItemStack);
            for (int i = 0; i < pages.size(); ++i) {
                this.spellPages.set(i, pages.get(i));
            }
        }

        NbtList tag = grimoireItemStack.getOrCreateNbt().getList(GrimoireItem.SPELL_PAGES_KEY, NbtElement.COMPOUND_TYPE);
        GrimoireInventory inventory = new GrimoireInventory(9) {
            @Override
            public void markDirty() {
                grimoireItemStack.getOrCreateNbt().put(GrimoireItem.SPELL_PAGES_KEY, InventoryUtils.toTag(this));
                super.markDirty();
            }
        };

        InventoryUtils.fromTag(tag, inventory);


        for (int i = 0; i < this.spellPages.size(); ++i) {
            this.addSlot(new Slot(inventory, i, 8 + (i * 18), 20));
        }

        // Add player inventory slots
        addPlayerInventorySlots(playerInventory);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return null;
    }

    private void addPlayerInventorySlots(PlayerInventory playerInventory) {
        // Add player inventory slots
        for (int m = 0; m < 3; ++m) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + m * 9 + 9, 8 + l * 18, 51 + m * 18));
            }
        }
        for (int n = 0; n < 9; ++n) {
            this.addSlot(new Slot(playerInventory, n, 8 + n * 18, 109));
        }
    }
}
//
//package cloud.viniciusith.arcanus.item.grimoire;
//
//import cloud.viniciusith.arcanus.ArcanusReloaded;
//import cloud.viniciusith.arcanus.helpers.InventoryUtils;
//import cloud.viniciusith.arcanus.item.GrimoireItem;
//import cloud.viniciusith.arcanus.api.*;
//        import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.entity.player.PlayerInventory;
//import net.minecraft.inventory.Inventory;
//import net.minecraft.item.ItemStack;
//import net.minecraft.item.Items;
//import net.minecraft.nbt.NbtElement;
//import net.minecraft.nbt.NbtList;
//import net.minecraft.network.PacketByteBuf;
//import net.minecraft.screen.ScreenHandler;
//import net.minecraft.screen.slot.Slot;
//
//public class GrimoireScreenHandler extends ScreenHandler {
//
//    private static final int SLOT_SQUARE_SIZE = 18;
//
//    private final ItemStack grimoireStack;
//    public final int xOffset = 60;
//    public final int yOffset = 30;
//    private final int padding = 8;
//    private final int titleSpace = 10;
//
//    public GrimoireScreenHandler(int synchronizationID, PlayerInventory playerInventory, PacketByteBuf packetByteBuf) {
//        this(synchronizationID, playerInventory, packetByteBuf.readItemStack());
//    }
//
//    public GrimoireScreenHandler(int synchronizationID, PlayerInventory playerInventory, ItemStack grimoireStack) {
//        super(ArcanusReloaded.GRIMOIRE_CONTAINER_TYPE, synchronizationID);
//        this.grimoireStack = grimoireStack;
//
//        if (grimoireStack.getItem() instanceof GrimoireItem) {
//            setupContainer(playerInventory, grimoireStack);
//        } else {
//            PlayerEntity player = playerInventory.player;
//            this.onClosed(player);
//        }
//    }
//
//    private void setupContainer(PlayerInventory playerInventory, ItemStack grimoireStack) {
//        NbtList tag = grimoireStack.getOrCreateNbt().getList("Inventory", NbtElement.COMPOUND_TYPE);
//        GrimoireInventory inventory = new GrimoireInventory(this.getItem().spellSlots) {
//            @Override
//            public void markDirty() {
//                grimoireStack.getOrCreateNbt().put("Inventory", InventoryUtils.toTag(this));
//                super.markDirty();
//            }
//        };
//
//        InventoryUtils.fromTag(tag, inventory);
//
//        // Add Grimoire slot
//        this.addSlot(new GrimoireSlots(inventory, 0, xOffset, 0));
//
//        // Player Inventory
//        for (int i = 0; i < 3; ++i) {
//            for (int j = 0; j < 9; ++j) {
//                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
//            }
//        }
//
//        for (int i = 0; i < 9; ++i) {
//            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
//        }
//    }
//
//    public GrimoireItem getItem() {
//        return (GrimoireItem) this.grimoireStack.getItem();
//    }
//
////    public void addPlayerInventory(int xOffset) {
////
////    }
//
//    public Dimension getDimension() {
//        return new Dimension(padding * 2 * SLOT_SQUARE_SIZE, padding * 2 + titleSpace * 2 + 8 + 4 * SLOT_SQUARE_SIZE);
//    }
//
//    public Point getPlayerInvSlotPosition(Dimension dimension, int x, int y) {
//        return new Point((dimension.getWidth() / 2 - 9 * 9 + x * 18), (dimension.getHeight() - padding - 4 * 18 - 3 + y * 18 + (y == 3 ? 4 : 0)));
//    }
//
//    @Override
//    public ItemStack quickMove(PlayerEntity player, int slot) {
//        return null;
//    }
//
//    @Override
//    public boolean canUse(PlayerEntity player) {
//        return true;
//    }
//
//    private class GrimoireSlots extends Slot {
//
//        public GrimoireSlots(Inventory inventory, int index, int x, int y) {
//            super(inventory, index, x, y);
//        }
//
//        @Override
//        public boolean canInsert(ItemStack stack) {
//            return isStackAllowed(stack);
//        }
//
//        private boolean isStackAllowed(ItemStack stack) {
//            return stack.getItem() == Items.PAPER;
//        }
//
//        @Override
//        public int getMaxItemCount() {
//            return 1;
//        }
//    }
//}
