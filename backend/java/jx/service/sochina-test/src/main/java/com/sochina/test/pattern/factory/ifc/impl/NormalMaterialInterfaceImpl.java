package com.sochina.test.pattern.factory.ifc.impl;

import com.sochina.test.domain.bing.ShaoBing;
import com.sochina.test.domain.bing.ColorBing;
import com.sochina.test.domain.bing.Mian;
import com.sochina.test.domain.bing.WhiteMian;
import com.sochina.test.pattern.factory.ifc.MaterialInterface;

public class NormalMaterialInterfaceImpl implements MaterialInterface {
    @Override
    public Mian createMian() {
        return new WhiteMian();
    }

    @Override
    public ColorBing createColorBing() {
        return new ShaoBing();
    }
}
