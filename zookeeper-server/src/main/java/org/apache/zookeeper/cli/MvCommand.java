package org.apache.zookeeper.cli;

import static java.nio.charset.StandardCharsets.UTF_8;
import java.util.List;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.server.EphemeralType;
import org.apache.zookeeper.util;

/**
 * mv command for cli
 */
public class MvCommand extends CliCommand {
        private static Options options = new Options();
        private String[] args;
        private CommandLine cl;

        static {
                options.addOption("i", false, "interactive");
                options.addOption("v", false, "verbose");
        }

        public MvCommand() {
                super("mv", "[-i] [-v] pathSrc pathDest");
        }

        @Override
        public boolean exec() throws CliException {
                String src = args[1];
                String dest = args[2];

                TransportTreeExtractor tte = new TransportTreeExtractor(zk);
                TransportRecoupler ttr = new TransportTreeRecoupler(zk);

                TransportTree tt = tte.extractTree(src);

                ttr.attachTree(dest, tt);

                return true;
        }
}
