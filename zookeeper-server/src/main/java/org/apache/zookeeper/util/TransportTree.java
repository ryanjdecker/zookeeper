/*TODO: license?*/

package org.apache.zookeeper.util;

import java.lang.Iterable;
import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.ACL;

/**
 * TransportTrees store znode subtrees unraveled as part of
 * the move and copy command line utilities.
 */
public interface TransportTree extends Iterable<TransportTree>{

    /**
     * Add a child TransportTree to this TransportTree
     *
     * @param child The transport tree to be added as a child of this tree
     */
    void addChild(TransportTree child);


    /**
     * Get the name of this TransportTree node
     *
     * @return The name string that was passed to the tree constructor
     */
    String getName();


    /**
     * Get the data of this TransportTree node
     *
     * @return The byte data associated with this node, drawn from the znode whence it came
     */
    byte[] getData();


    /**
     * Get the Access Control List of this TransportTree node
     *
     * @return The ACL list, as passed to the constructor
     */
    List<ACL> getACL();

    /**
     * Get the CreateMode for this TransportTree node (persistent or ephemeral)
     *
     * @return the CreateMode
     */
    CreateMode getCreateMode();

    /**
     * Get the path to each node in this TransportTree from the root of the TransportTree
     *
     * @return a list of paths
     */
    List<String> getPathList(String parentPath);
}


