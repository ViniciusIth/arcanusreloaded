package cloud.viniciusith.arcanus.command;

import cloud.viniciusith.arcanus.item.SpellPageItem;
import cloud.viniciusith.arcanus.spell.base.Spell;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;

import java.util.ArrayList;

import static net.minecraft.server.command.CommandManager.literal;

public class SetSpellPatternCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(
                literal("addspellpattern")
                        .then(CommandManager.argument("pattern", StringArgumentType.string())
                                .executes(context -> {
                                    ServerCommandSource source = context.getSource();
                                    PlayerEntity player = source.getPlayer();
                                    String patternStr = StringArgumentType.getString(context, "pattern");

                                    ItemStack heldItem = player.getStackInHand(Hand.MAIN_HAND);
                                    if (!(heldItem.getItem() instanceof SpellPageItem)) {
                                        source.sendFeedback(() -> Text.translatable("commands.arcanus.spells.pattern_wrong_item").formatted(Formatting.RED), false);
                                        return 1;
                                    }

                                    ArrayList<Spell.Pattern> patterns = new ArrayList<>();
                                    for (char ch : patternStr.toCharArray()) {
                                        if (ch == 'L' || ch == 'R') {
                                            patterns.add(ch == 'L' ? Spell.Pattern.LEFT : Spell.Pattern.RIGHT);
                                        }
                                    }

                                    SpellPageItem.setSpellPattern(heldItem, patterns);

                                    source.sendFeedback(() -> Text.literal("Added spell pattern to the Spell Page."), false);
                                    return 1;
                                }))
        );
    }
}
