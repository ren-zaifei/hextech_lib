package com.renzaifei.hextechlib.compat.kubejs.event;

import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;

public interface HCardEvents {
    EventGroup GROUP = EventGroup.of("HCardEvents");

    EventHandler REGISTER = GROUP.startup("register", () -> HCardRegisterKubeEvent.class);
}
