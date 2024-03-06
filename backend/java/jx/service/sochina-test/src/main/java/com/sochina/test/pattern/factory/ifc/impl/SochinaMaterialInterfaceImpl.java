package com.sochina.test.pattern.factory.ifc.impl;

import com.sochina.test.domain.bing.YouBing;
import com.sochina.test.domain.bing.MiMian;
import com.sochina.test.domain.bing.ColorBing;
import com.sochina.test.domain.bing.Mian;
import com.sochina.test.pattern.factory.ifc.MaterialInterface;

public class SochinaMaterialInterfaceImpl implements MaterialInterface {
    @Override
    public Mian createMian() {
        return new MiMian();
    }

    @Override
    public ColorBing createColorBing() {
        return new YouBing();
    }
}
