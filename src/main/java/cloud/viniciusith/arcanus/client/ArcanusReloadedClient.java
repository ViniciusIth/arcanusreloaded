package cloud.viniciusith.arcanus.client;

import cloud.viniciusith.arcanus.ArcanusReloaded;
import cloud.viniciusith.arcanus.item.grimoire.GrimoireHandledScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Environment(EnvType.CLIENT)
public class ArcanusReloadedClient implements ClientModInitializer {
    public static final String MOD_ID = "arcanus";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {
        LOGGER.debug("Hellow World!");

        HandledScreens.register(ArcanusReloaded.GRIMOIRE_CONTAINER_TYPE, GrimoireHandledScreen::new);
    }
}
