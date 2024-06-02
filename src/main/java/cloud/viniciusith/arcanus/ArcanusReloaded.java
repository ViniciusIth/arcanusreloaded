package cloud.viniciusith.arcanus;

import cloud.viniciusith.arcanus.item.WandItem;
import cloud.viniciusith.arcanus.item.grimoire.GrimoireScreenHandler;
import cloud.viniciusith.arcanus.registry.ItemRegister;
import cloud.viniciusith.arcanus.registry.SpellRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ArcanusReloaded implements ModInitializer {
    public static final String MODID = "arcanus";
    public static final Logger LOGGER = LogManager.getLogger("arcanus");

    public static final Identifier CONTAINER_ID = new Identifier(MODID, "grimoire");
    public static final ScreenHandlerType<GrimoireScreenHandler> GRIMOIRE_CONTAINER_TYPE = Registry.register(Registries.SCREEN_HANDLER, CONTAINER_ID,
            new ExtendedScreenHandlerType<>(GrimoireScreenHandler::new));

    @Override
    public void onInitialize() {
        SpellRegistry.registerAllSpells();
        ItemRegister.RegisterAll();
    }
}
