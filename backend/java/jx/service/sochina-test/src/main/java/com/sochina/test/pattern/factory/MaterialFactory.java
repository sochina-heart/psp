package com.sochina.test.pattern.factory;

import com.sochina.test.pattern.factory.ifc.MaterialInterface;
import com.sochina.test.pattern.factory.ifc.impl.NormalMaterialInterfaceImpl;
import com.sochina.test.pattern.factory.ifc.impl.SochinaMaterialInterfaceImpl;

public class MaterialFactory {

    public MaterialInterface getMaterial(String type) {
        MaterialInterface materialInterface = null;
        switch (type) {
            case "sochina":
                materialInterface = new SochinaMaterialInterfaceImpl();
                break;
            default:
                materialInterface = new NormalMaterialInterfaceImpl();
        }
        return materialInterface;
    }
}
