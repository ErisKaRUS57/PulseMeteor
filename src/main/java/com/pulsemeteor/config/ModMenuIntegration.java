package com.pulsemeteor.config;

import com.pulsemeteor.gui.ClickGUI;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

/**
 * ModMenu integration for Pulse Meteor.
 * Allows opening the ClickGUI from the ModMenu screen.
 */
public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> ClickGUI.getInstance();
    }
}
