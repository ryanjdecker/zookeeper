package org.apache.zookeeper.util;

import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.cli.CliWrapperException;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;

/**
 * Objects of this class are used to extract TransportTree copies of the designated
 * subtree of a ZooKeeper data tree in preparation for being copied or
 * moved to a new destination in the data tree.
 */
public class TransportTreeExtractor {
    ZooKeeper zk; // client object
    TransportTreeFactory treeFactory; // used to create TransportTree

    /**
     * Constructor requires an instance of ZooKeeper (client object) in
     * order to access ZooKeeper API
     *
     * @param zk ZooKeeper instance
     */
    public TransportTreeExtractor(ZooKeeper zk) {
        this.zk = zk;
        this.treeFactory = new SimpleTransportTreeFactory();
    }

    /**
     * Copies the subtree beginning at rootPath into a TransportTree.
     *
     * @param rootPath path to root node of subtree
     * @return a copy of the subtree as a TransportTree
     * @throws CliWrapperException command line exception thrown
     */
    public TransportTree extractTree(String rootPath) throws CliWrapperException {
        try {
            String name = extractName(rootPath);

            // Stat object into which to copy Stat from node
            Stat stat = new Stat();
            byte[] data = zk.getData(rootPath, null, stat);
            List<ACL> acl = zk.getACL(rootPath, null);
            // get the CreateMode (persistent or ephemeral)
            CreateMode createMode = stat.getEphemeralOwner() == 0 ? CreateMode.PERSISTENT : CreateMode.EPHEMERAL;
            TransportTree tree = treeFactory.makeNewTree(name, data, acl, createMode);

            List<String> childPaths = zk.getChildren(rootPath, null);
            for (String childPath: childPaths) {
                TransportTree child = extractTree(rootPath + "/" + childPath);
                tree.addChild(child);
            }
            return tree;
        }
        catch (KeeperException | InterruptedException e) {
            throw new CliWrapperException(e);
        }
    }

    /**
     * Extracts the name of a znode (the end of its path)
     *
     * @param path the path of the znode
     * @return the name of the node
     */
    private static String extractName(String path) {
        int lastSlashIndex = path.lastIndexOf('/');
        if (lastSlashIndex == -1) {
            return null;
        }
        return path.substring(lastSlashIndex + 1);
    }
}
