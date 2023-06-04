package org.apache.zookeeper.cli;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.test.ClientBase;
import org.apache.zookeeper.util.TransportTree;
import org.apache.zookeeper.util.TransportTreeExtractor;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CpCommandTest extends ClientBase {

    @Test
    void parse() throws IOException, InterruptedException {
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

            CpCommand cpCommand = new CpCommand();
            cpCommand.setZk(zk);

            final String[] argsWithoutFinalSlash = {"mv", "/z/z1", "/z/z2"};
            assertDoesNotThrow(() -> {
                cpCommand.parse(argsWithoutFinalSlash);
            });

            final String[] argsWithFinalSlash = {"mv", "/z/z1", "/z/z2/"};
            assertDoesNotThrow(() -> {
                cpCommand.parse(argsWithFinalSlash);
            });

            final String[] invalidArgs = {"mv", "/z/z1", "/z/z1"};
            Exception exception = assertThrows(
                    CliParseException.class,
                    () -> cpCommand.parse(invalidArgs)
            );
            assertEquals("cp: '/z/z1' and '/z/z1' are the same znode", exception.getMessage());
        } catch (KeeperException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    void testExec() throws IOException, InterruptedException {
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

            List<String> testPaths = Arrays.asList(
                    "/z",
                    "/z/z1",
                    "/z/z1/z1_1",
                    "/z/z2/z1",
                    "/z/z2/z1/z1_1",
                    "/z/z2",
                    "/z/z2/z2_1",
                    "/z/z2/z2_2",
                    "/z/z2/z2_2/z2_2_1"
            );

            CpCommand cpCommand = new CpCommand();
            cpCommand.setZk(zk);
            final String[] args = {"cp", "/z/z1", "/z/z2"};
            cpCommand.parse(args);
            cpCommand.exec();

            TransportTreeExtractor zkExtractor = new TransportTreeExtractor(zk);
            TransportTree tree = zkExtractor.extractTree("/z");
            List<String> treePaths = tree.getPathList("");

            Collections.sort(treePaths);
            Collections.sort(testPaths);

            assertEquals(treePaths, testPaths);
        } catch (KeeperException | CliException e) {
            throw new RuntimeException(e);
        }
    }
}