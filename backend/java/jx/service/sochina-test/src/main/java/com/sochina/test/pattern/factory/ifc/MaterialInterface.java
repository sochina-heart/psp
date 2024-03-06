package com.sochina.test.pattern.factory.ifc;

import com.sochina.test.domain.bing.ColorBing;
import com.sochina.test.domain.bing.Mian;

public interface MaterialInterface {

    public Mian createMian();

    public ColorBing createColorBing();
}
