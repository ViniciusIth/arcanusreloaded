package cloud.viniciusith.arcanus;

import cloud.viniciusith.arcanus.registry.ItemRegister;
import cloud.viniciusith.arcanus.registry.SpellRegistry;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ArcanusReloaded implements ModInitializer {
    public static final String MODID = "arcanus";
    public static final Logger LOGGER = LogManager.getLogger("arcanus");

    @Override
    public void onInitialize() {
        SpellRegistry.registerAllSpells();
        ItemRegister.RegisterAll();
    }
}
