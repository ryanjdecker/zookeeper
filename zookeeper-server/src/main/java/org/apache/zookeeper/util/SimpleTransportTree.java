/*TODO: license?*/

package org.apache.zookeeper.util;

import java.lang.Iterable;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import org.apache.zookeeper.data.ACL;

/**
 * See the TransportTree interface for details.
 */
public class SimpleTransportTree implements TransportTree{

    private byte[] data;
    private String name;
    private List<ACL> acl;
    private ArrayList<TransportTree> children;
    
    public SimpleTransportTree(String name, byte[] data, List<ACL> acl){
        this.data = data.clone();
        this.name = name;
//        this.acl = acl.clone(); // TODO copy depth?
        this.acl = acl;
        this.children = new ArrayList<TransportTree>();
    }

    public void addChild(TransportTree child){
        children.add(child);
    }

    public String getName(){
        return name;
    }

    public byte[] getData(){
        return data;
    }

    public List<ACL> getACL(){
        return acl;
    }

    public Iterator<TransportTree> iterator(){
        return children.iterator();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String path: getPathList("")) {
            stringBuilder.append(path);
            stringBuilder.append(System.lineSeparator());
        }
        return stringBuilder.toString();
    }

    public List<String> getPathList(String parentPath) {
        List<String> pathList = new ArrayList<>();
        String currentPath = parentPath + "/" + this.name;
        pathList.add(currentPath);
        for (TransportTree child: this) {
            List<String> childPathList = child.getPathList(currentPath);
            pathList.addAll(childPathList);
        }
        return pathList;
    }
}


