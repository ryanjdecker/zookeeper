/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
import org.apache.zookeeper.util.*;

/**
 * mv command for cli
 */
public class MvCommand extends CliCommand {

        private static Options options = new Options();
        private String[] args;
        private CommandLine cl;

        static {
                options.addOption(new Option("i", false, "interactive"));
                options.addOption(new Option("v", false, "verbose"));
        }

        public MvCommand() {
                super("mv", "[-i] [-v] source dest");
        }

        @Override
        public CliCommand parse(String[] cmdArgs) throws CliParseException {
                DefaultParser parser = new DefaultParser();
                try {
                        cl = parser.parse(options, cmdArgs);
                } catch (ParseException ex) {
                        throw new CliParseException(ex);
                }
                args = cl.getArgs();
                if (args.length < 3) {
                        throw new CliParseException(getUsageStr());
                }
                return this;
        }

        @Override
        public boolean exec() throws CliException {
                boolean hasI = cl.hasOption("i");
                boolean hasV = cl.hasOption("v");

                TransportTreeFactory treeFactory = new SimpleTransportTreeFactory(source);
                TransportTree tt = treeFactory.makeNewTree();

                return true;
        }


}
