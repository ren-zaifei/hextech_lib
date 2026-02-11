package com.renzaifei.hextechlib.compat.kubejs;

import com.renzaifei.hextechlib.card.HCard;
import com.renzaifei.hextechlib.compat.kubejs.event.HCardEvents;
import dev.latvian.mods.kubejs.event.EventGroupRegistry;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.BindingRegistry;
import net.neoforged.neoforge.event.entity.living.LivingEvent;

public class HextechLibKubeJSPlugin implements KubeJSPlugin {
    @Override
    public void registerBindings(BindingRegistry bindings) {
        bindings.add("HCard", HCard.class);
        bindings.add("HCardRarity", HCard.Rarity.class);
    }

    @Override
    public void registerEvents(EventGroupRegistry registry) {
        registry.register(HCardEvents.GROUP);

        LivingEvent.LivingJumpEvent
    }
}
