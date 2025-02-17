package org.apache.zookeeper.cli;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.zookeeper.util.TransportTree;
import org.apache.zookeeper.util.TransportTreeExtractor;
import org.apache.zookeeper.util.TransportTreeRecoupler;

/**
 * cp command for cli
 */
public class CpCommand extends CliCommand {
        private static final Options options = new Options();
        private String[] args;
        private TransportTreeExtractor tte;
        private TransportTreeRecoupler ttr;

        public CpCommand() {
                super("cp", "pathSrc pathDest");
        }

        @Override
        public CliCommand parse(String[] cmdArgs) throws CliParseException {
                DefaultParser parser = new DefaultParser();

                CommandLine cl;
                try {
                        cl = parser.parse(options, cmdArgs);
                } catch (ParseException ex) {
                        throw new CliParseException(ex);
                }

                args = cl.getArgs();

                if (args.length != 3) {
                        throw new CliParseException(getUsageStr());
                }
                
                if (args[1].equals(args[2])) {
                        throw new CliParseException(getCmdStr() + ": " +
                                                    "'" + args[1] + "'" + " and " +
                                                    "'" + args[2] + "'" + " are the same znode");
                }

                return this;
        }

        @Override
        public boolean exec() throws CliException {
                String src = args[1];
                String dest = args[2];

                if (tte == null) {
                        this.tte = new TransportTreeExtractor(zk);
                }

                if (ttr == null) {
                        this.ttr = new TransportTreeRecoupler(zk);
                }

                try {
                        TransportTree tt = tte.extractTree(src);
                        ttr.attachTree(dest, tt);
                } catch (IllegalArgumentException ex) {
                        throw new MalformedPathException(ex.getMessage());
                }
                return false;
        }
}
