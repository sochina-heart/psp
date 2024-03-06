package com.sochina.test.pattern.factory;

import com.sochina.test.domain.bing.Bing;

public class BingStore {

    private SimpleBingStoreFactory factory;

    public BingStore(SimpleBingStoreFactory factory) {
        this.factory = factory;
    }

    public Bing sellBing(String type) {
        Bing bing = factory.createBing(type);
        bing.prepare();
        bing.pack();
        bing.fire();
        return bing;
    }
}
