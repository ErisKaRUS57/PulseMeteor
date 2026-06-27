package com.pulsemeteor.modules.utility;

import com.pulsemeteor.modules.Category;
import com.pulsemeteor.modules.Module;
import com.pulsemeteor.settings.BooleanSetting;
import com.pulsemeteor.settings.NumberSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.DisconnectedScreen;
import net.minecraft.client.gui.screen.Screen;

/**
 * AutoReconnect - automatically reconnects to server on disconnect.
 */
public class AutoReconnect extends Module {
    private final NumberSetting delay;
    private final BooleanSetting repeat;

    private long disconnectTime = -1;
    private boolean reconnecting = false;

    public AutoReconnect() {
        super("AutoReconnect", "Auto reconnect on disconnect", Category.UTILITY);
        this.delay = createNumber("Delay", "Delay before reconnect (seconds)", 3, 0, 30, 1);
        this.repeat = createBoolean("Repeat", "Keep reconnecting until successful", true);
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.currentScreen instanceof DisconnectedScreen) {
            if (!reconnecting) {
                disconnectTime = System.currentTimeMillis();
                reconnecting = true;
            }

            long elapsed = (System.currentTimeMillis() - disconnectTime) / 1000;
            if (elapsed >= delay.getValueInt()) {
                // Get the server address from the disconnect screen
                Screen parent = ((DisconnectedScreen) client.currentScreen).getParent();
                // Attempt to reconnect by connecting to the last server
                if (client.getCurrentServerEntry() != null) {
                    client.setScreen(parent);
                    // The actual reconnect is handled by Minecraft's disconnect handling
                    // We just need to clear the disconnect screen
                    reconnecting = false;
                }
            }
        } else {
            reconnecting = false;
        }
    }
}
