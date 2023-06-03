
## How to

To install our extension of ZooKeeper, clone the git repository, change into the directory, and run Maven.

```
git clone https://github.com/ryanjdecker/zookeeper.git
cd zookeeper
mvn clean install -DskipTests
```
To launch a ZooKeeper server, execute the zkServer script.

```
bash bin/zkServer.sh start
```

The server will listen on port 2181. To change this or other server settings, edit `conf/zoo.cfg`.

To launch the ZooKeeper client, execute the zkCli script.

```
bash bin/zkCli.sh -server 127.0.0.1:2181
```

You can list the data tree with:

```
ls -R /
```

Right now, the data tree just contains the `zookeeper` node, which stores configuration and quorum data in its child nodes. You can create and delete nodes with the commands `create path [data]` and `delete path`. Let's create some nodes without data so that we can see the `mv` and `cp` commands in action:

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
mv /alpha/beta /alpha/beta/gamma
```

Calling `ls -R /alpha` should show that the beta subtree has been moved to a new location.

```

```

Now, let's copy the beta subtree back to its original location, so that it shows up under both `alpha` and `gamma`.

```
cp /alpha/gamma/beta /alpha
```

Invoking `ls -R /alpha` should show:

```

```

And that's all! If you want to keep exploring ZooKeeper, you can invoke `help` to see a list of commands. When you're finished, invoke:

```
quit
bash bin/zkServer.sh stop
```