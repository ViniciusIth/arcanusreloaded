package cloud.viniciusith.arcanus.registry;

import cloud.viniciusith.arcanus.command.SetSpellPatternCommand;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class CommandRegistry {
    public static void registerAll() {
        CommandRegistrationCallback.EVENT.register(SetSpellPatternCommand::register);
    }
}
