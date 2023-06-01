package org.apache.zookeeper.util;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.cli.CliWrapperException;
import org.apache.zookeeper.data.ACL;
import java.util.List;

public class TransportTreeExtractor {
    ZooKeeper zk; // client object
    TransportTreeFactory treeFactory;

    /**
     *
     * @param zk
     */
    public TransportTreeExtractor(ZooKeeper zk) {
        this.zk = zk;
        this.treeFactory = new SimpleTransportTreeFactory();
    }

    /**
     *
     * @param rootPath
     * @param isDelete
     * @return
     */
    public TransportTree extractTree(String rootPath, boolean isDelete) throws CliWrapperException {
        try {
            TransportTree tree = extractTree(rootPath);
            if (isDelete) {
                // TODO delete subtree from DataTree
            }
            return tree;
        }
        catch (KeeperException | InterruptedException e) {
            throw new CliWrapperException(e);
        }
    }

    /**
     *
     * @param path
     * @return
     * @throws KeeperException
     * @throws InterruptedException
     */
    private TransportTree extractTree(String path) throws KeeperException, InterruptedException {
        String name = extractName(path);
        byte[] data = zk.getData(path, null, null);
        List<ACL> acl = zk.getACL(path, null);
        TransportTree tree = treeFactory.makeNewTree(name, data, acl);

        List<String> childPaths = zk.getChildren(path, null);
        for (String childPath: childPaths) {
            TransportTree child = extractTree(path + "/" + childPath);
            tree.addChild(child);
        }
        return tree;
    }

    /**
     *
     * @param path
     * @return
     */
    private static String extractName(String path) {
        int lastSlashIndex = path.lastIndexOf('/');
        if (lastSlashIndex == -1) {
            return null;
        }
        return path.substring(lastSlashIndex + 1);
    }
}
