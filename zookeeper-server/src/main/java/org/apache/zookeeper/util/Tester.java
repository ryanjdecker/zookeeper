package org.apache.zookeeper.util;

import org.apache.zookeeper.CreateMode;

public class Tester {

    public static void main(String[] args){

        TransportTreeFactory factory = new SimpleTransportTreeFactory();

        byte[] zopfData = "Zorbaffloz".getBytes();
        TransportTree zopf = factory.makeNewTree("zopf", zopfData, null, CreateMode.PERSISTENT);

        TransportTree zepf = factory.makeNewTree("zepf", zopfData, null, CreateMode.PERSISTENT);

        zopf.addChild(zepf);

        for(TransportTree child : zopf){
            System.out.println(child.getName());
        }

    }
}
