
## How to

ZooKeeper is a coordination service that helps distributed applications maintain a shared in-memory database, saving time for developers by providing a simple API and framework for configuration maintenance, group management, and synchronization.

The ZooKeeper database is a tree of znodes, where each znode may have both data and child nodes. The data tree resembles the UNIX file hierarchy: each znode is referenced by a path relative to the root node, whose path is `\`.

One challenge is that there is no easy way to reorganize the data tree. If you want to change the parent of a subtree, you must create a new version of each node at the new destination, and then delete the original subtree. To improve the usability of ZooKeeper, we implemented analogs of the UNIX utilities `mv` and `cp` in the command line interface, using the method `ZooKeeper::multi` to make sure that the subtree is copied as a whole or not at all.

To install our extension of ZooKeeper, you will need Apache Maven. You can find the installation instructions at https://maven.apache.org.

Next, clone the git repository, change the directory to zookeeper, and run Maven.

```
git clone https://github.com/ryanjdecker/zookeeper.git
cd zookeeper
mvn clean install -DskipTests
```
To launch a ZooKeeper server, execute the `zkServer` script.

```
bash bin/zkServer.sh start
```

The server will listen on port 2181. To change this or other server settings, edit `conf/zoo.cfg`.

To launch the ZooKeeper client, execute the `zkCli` script.

```
bash bin/zkCli.sh -server 127.0.0.1:2181
```

You can list the data tree with `ls -R /`. Right now, the data tree just contains the `zookeeper` node, which stores configuration and quorum data in its child nodes. You can create and delete nodes with the commands `create path [data]` and `delete path`. Let's create some nodes without data so that we can see the `mv` and `cp` commands in action:

```
create /alpha alphadata
create /alpha/beta betadata
create /alpha/beta/delta deltadata
create /alpha/beta/epsilon epsilondata
create /alpha/gamma gammadata
```

List the alpha subtree with `ls -R /alpha`. It should look like this:

```
/alpha
/alpha/beta
/alpha/gamma
/alpha/beta/delta
/alpha/beta/epsilon
```

Next, let's move the subtree rooted at `beta` under the `gamma` znode.

```
mv /alpha/beta /alpha/gamma
```

Calling `ls -R /alpha` should show that the `beta` subtree has been moved under the znode `gamma`.

```
/alpha
/alpha/gamma
/alpha/gamma/beta
/alpha/gamma/beta/delta
/alpha/gamma/beta/epsilon
```

Now, let's copy the `beta` subtree back to its original location, so that it shows up under both `alpha` and `gamma`.

```
cp /alpha/gamma/beta /alpha
```

Invoking `ls -R /alpha` should show:

```
/alpha
/alpha/beta
/alpha/gamma
/alpha/beta/delta
/alpha/beta/epsilon
/alpha/gamma/beta
/alpha/gamma/beta/delta
/alpha/gamma/beta/epsilon
```

And that's all! If you want to keep exploring ZooKeeper, you can invoke `help` to see a list of commands. When you're finished, run:

```
quit
bash bin/zkServer.sh stop
```