package cloud.viniciusith.arcanus.client;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArcanusReloadedClient implements ClientModInitializer {
    public static final String MOD_ID = "arcanus";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {
        LOGGER.debug("Hellow World!");
    }
}
