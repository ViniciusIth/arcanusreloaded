package cloud.viniciusith.arcanus.component.entity;

import cloud.viniciusith.arcanus.component.base.MagicCaster;
import cloud.viniciusith.arcanus.component.keys.MagicCasterKeys;
import cloud.viniciusith.arcanus.registry.ComponentRegistry;
import cloud.viniciusith.arcanus.spell.Spell;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class PlayerMagicCaster implements MagicCaster, AutoSyncedComponent, ServerTickingComponent {
    private final PlayerEntity player;
    private int mana;
    private int maxMana = 100;
    private final Map<Spell, Integer> activeSpells = new HashMap<>();
    private long lastCastTime;

    public PlayerMagicCaster(PlayerEntity player) {
        this.player = player;
    }

    @Override
    public int getMaxMana() {
        return this.maxMana;
    }

    @Override
    public int getMana() {
        return this.mana;
    }

    @Override
    public void setMana(int mana) {
        this.mana = mana;
    }

    @Override
    public void cast(Spell spell) {
        if (getMana() > 0) {
            //        if (shouldBurnout(spell.getManaCost())) {
            //            spell.OnBurnout(this);
            //        }

            lastCastTime = this.player.getWorld().getTime();

            reduceMana(spell.getManaCost());
            spell.OnCast(this);
            player.syncComponent(ComponentRegistry.MAGIC_CASTER_COMPONENT);
        }
    }

    @Override
    public void addActiveSpell(Spell spell, int activeTicks) {
        this.activeSpells.put(spell, activeTicks);
    }

    @Override
    public Map<Spell, Integer> getActiveSpells() {
        return activeSpells;
    }

    @Override
    public void clearActiveSpells() {

    }

    @Override
    public LivingEntity asEntity() {
        return player;
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        if (tag.contains(MagicCasterKeys.MAX_MANA) && tag.getInt(MagicCasterKeys.MAX_MANA) != 0) {
            this.maxMana = tag.getInt(MagicCasterKeys.MAX_MANA);
        } else {
            this.maxMana = 100;
        }

        if (tag.contains(MagicCasterKeys.MANA)) {
            this.mana = tag.getInt(MagicCasterKeys.MANA);
        } else {
            this.mana = maxMana;
        }


        // Should I save the active spells hashmap? If so, maybe each spell should have its own name to make it easier
        // For now, I'll let it be...
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putInt(MagicCasterKeys.MAX_MANA, this.maxMana);
        tag.putInt(MagicCasterKeys.MANA, this.mana);
    }


    @Override
    public void serverTick() {
        World world = player.getWorld();

        // if (!activeSpells.isEmpty()) {
        //     activeSpells.forEach((spell, timeLeft) -> {
        //         spell.onActiveTick(world, this, timeLeft);
        //
        //         if (timeLeft == 0) {
        //             activeSpells.remove(spell);
        //             player.syncComponent(ComponentRegistry.MAGIC_CASTER_COMPONENT);
        //             return;
        //         }
        //
        //         activeSpells.replace(spell, timeLeft - 1);
        //     });
        // }

        // Should check if the player has cast recently, and only then start regenerating mana.
        // For now stay like this
        boolean dirty = false;

        if (world.getTime() - this.lastCastTime >= 40 && world.getTime() % 5 == 0) {
            if (this.getMana() < this.getMaxMana()) {
                this.addMana(5);
                dirty = true;
            }
        }

        if (dirty) {
            player.syncComponent(ComponentRegistry.MAGIC_CASTER_COMPONENT);
        }
    }
}
