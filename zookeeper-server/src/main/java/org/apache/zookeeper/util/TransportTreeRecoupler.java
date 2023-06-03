package org.apache.zookeeper.util;

import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.cli.CliWrapperException;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;

/**
 * Objects of this class are used to attach a znode subtree, represented as TransportTree,
 * at a given destination in the data tree.
 */
public class TransportTreeRecoupler {
    ZooKeeper zk; // the client object

    /**
     * Constructor requires an instance of ZooKeeper (client object) in
     * order to access ZooKeeper API
     *
     * @param zk ZooKeeper instance
     */
    TransportTreeRecoupler(ZooKeeper zk) {
        this.zk = zk;
    }

    /**
     * Attaches the znode subtree represented by the argument TransportTree
     * at the argument destination.
     *
     * @param destination the path of the new parent node for the subtree
     * @param tree the subtree to attach
     * @throws CliWrapperException command line exception
     */
    public void attachTree(String destination, TransportTree tree) throws CliWrapperException {
        // create node from tree at destination
        String path = destination + "/" + tree.getName();
        byte[] data = tree.getData();
        List<ACL> acl = tree.getACL();
        CreateMode createMode = tree.getCreateMode();
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
