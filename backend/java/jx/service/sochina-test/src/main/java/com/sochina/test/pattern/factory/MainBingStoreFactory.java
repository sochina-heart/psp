package com.sochina.test.pattern.factory;

import com.sochina.test.domain.bing.Bing;
import com.sochina.test.domain.bing.CaiBing;
import com.sochina.test.domain.bing.MeatBing;
import com.sochina.test.domain.bing.XianBing;

public class MainBingStoreFactory extends AbstractBingStore {
    @Override
    public Bing createBing(String type) {
        Bing bing = null;
        switch (type) {
            case "cai":
                bing = new CaiBing();
                break;
            case "meat":
                bing = new MeatBing();
                break;
            default:
                bing = new XianBing();
                break;
        }
        return bing;
    }
}