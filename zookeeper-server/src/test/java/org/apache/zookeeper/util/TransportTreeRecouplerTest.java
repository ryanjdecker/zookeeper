package org.apache.zookeeper.util;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.cli.CliWrapperException;
import org.apache.zookeeper.test.ClientBase;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

class TransportTreeRecouplerTest extends ClientBase {

    @Test
    void attachTree() throws IOException, InterruptedException, KeeperException, CliWrapperException {
        try (final ZooKeeper zk = createClient()) {
            List<String> paths = Arrays.asList(
                    "/z",
                    "/z/z1",
                    "/z/z1/z1_1",
                    "/z/z2",
                    "/z/z2/z2_1",
                    "/z/z2/z2_2",
                    "/z/z2/z2_2/z2_2_1"
            );
            for (String path: paths) {
                zk.create(path, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }

            TransportTreeExtractor extractor = new TransportTreeExtractor(zk);
            TransportTree tree = extractor.extractTree("/z/z2", false);
            System.out.println(tree);
            System.out.println();

            // attach in new location
            TransportTreeRecoupler joiner = new TransportTreeRecoupler(zk);
            joiner.attachTree("/z/z1", tree);

            TransportTree rejoinedTree = extractor.extractTree("/z", false);
            System.out.println(rejoinedTree);
        }
    }
}