package org.apache.zookeeper.util;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.cli.CliWrapperException;
import org.apache.zookeeper.data.ACL;

import java.util.List;

// What about creation mode (persistent or ephemeral)?
// https://stackoverflow.com/questions/43580027/how-can-i-know-if-a-zookeeper-node-is-a-persistent-node-or-a-ephemeral-one

// What about watches?


public class TransportTreeJoiner {
    ZooKeeper zk;

    TransportTreeJoiner(ZooKeeper zk) {
        this.zk = zk;
    }

    public void attachTree(String destination, TransportTree tree) throws CliWrapperException {
        // create node from tree at destination
        String path = destination + "/" + tree.getName();
        byte[] data = tree.getData();
        List<ACL> acl = tree.getACL();
        // TODO get actual createMode
        CreateMode createMode = CreateMode.PERSISTENT;
        try {
            zk.create(path, data, acl, createMode);
            for (TransportTree child: tree) {
                attachTree(path, child);
            }
        }
        catch (KeeperException | InterruptedException e) {
            throw new CliWrapperException(e);
        }
    }
}
