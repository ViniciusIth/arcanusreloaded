package cloud.viniciusith.arcanus.mixin.client;

import cloud.viniciusith.arcanus.helpers.ArcanusHelper;
import cloud.viniciusith.arcanus.item.GrimoireItem;
import cloud.viniciusith.arcanus.item.SpellPageItem;
import cloud.viniciusith.arcanus.item.WandItem;
import cloud.viniciusith.arcanus.network.CastSpellPacket;
import cloud.viniciusith.arcanus.spell.base.Spell;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Optional;

@Mixin(MinecraftClient.class)
public class MinecraftMixin {
    @Unique
    private final ArrayList<Spell.Pattern> pattern = new ArrayList<>(3);
    @Shadow
    @Nullable
    public ClientPlayerEntity player;
    @Unique
    private boolean unfinishedSpell = false;
    @Unique
    private int timer = 0;

    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo info) {
        if (timer == 0 && !pattern.isEmpty()) {
            pattern.clear();
        }

        if (player == null) {
            return;
        }

        if (player.getMainHandStack().getItem() instanceof WandItem) {
            if (timer > 0) {
                MutableText hyphen = Text.literal("-");

                player.sendMessage(ArcanusHelper.getSpellInputs(pattern, 0)
                        .append(hyphen)
                        .append(ArcanusHelper.getSpellInputs(pattern, 1))
                        .append(hyphen)
                        .append(ArcanusHelper.getSpellInputs(pattern, 2)), true);

                if (pattern.size() == 3) {
                    unfinishedSpell = false;

                    Optional<ItemStack> grimoireStack = WandItem.findGrimoire(player);
                    if (grimoireStack.isPresent()) {
                        // Returning null for some reason
                        ItemStack spell = GrimoireItem.findSpellByPattern(grimoireStack.get(), pattern);

                        if (spell != null) {
                            SpellPageItem.getSpellName(spell).ifPresent(CastSpellPacket::send);
                        }
                    }

                    timer = 0;
                }
            } else if (pattern.size() < 3 && unfinishedSpell) {
                player.sendMessage(Text.literal(""), true);
            }
        } else {
            timer = 0;
        }

        if (timer > 0) {
            timer--;
        }
    }

    @Inject(method = "handleInputEvents", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;doItemUse()V", ordinal = 0), cancellable = true)
    public void onRightClick(CallbackInfo info) {
        if (player != null && !player.isSpectator() && player.getMainHandStack().getItem() instanceof WandItem) {
            timer = 20;
            unfinishedSpell = true;
            pattern.add(Spell.Pattern.RIGHT);
            player.swingHand(Hand.MAIN_HAND);
            player.getWorld().playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.UI_BUTTON_CLICK, SoundCategory.PLAYERS, 1F, 1.1F, 0L);
            info.cancel();
        }
    }

    @Inject(method = "handleInputEvents", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;doAttack()Z", ordinal = 0), cancellable = true)
    public void onLeftClick(CallbackInfo info) {
        if (player != null && !player.isSpectator() && player.getMainHandStack().getItem() instanceof WandItem) {
            timer = 20;
            unfinishedSpell = true;
            pattern.add(Spell.Pattern.LEFT);
            player.swingHand(Hand.MAIN_HAND);
            player.getWorld().playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.UI_BUTTON_CLICK, SoundCategory.PLAYERS, 1F, 1.1F, 0L);
            info.cancel();
        }
    }
}
