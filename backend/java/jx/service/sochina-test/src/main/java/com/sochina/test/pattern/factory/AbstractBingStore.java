package com.sochina.test.pattern.factory;

import com.sochina.test.domain.bing.Bing;

public abstract class AbstractBingStore {

    public abstract Bing createBing(String type);

    public Bing sellBing(String type) {
        Bing bing = createBing(type);
        bing.prepare();
        bing.prepare();
        bing.fire();
        return bing;
    }
}
