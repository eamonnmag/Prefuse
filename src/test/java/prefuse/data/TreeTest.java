package prefuse.data;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.zip.GZIPInputStream;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;
import prefuse.data.io.TreeMLReader;
import prefuse.data.util.TreeNodeIterator;
import prefuse.demos.TreeMap;
import prefuse.util.GraphLib;
import prefuse.util.ui.JPrefuseTable;

public class TreeTest {

    public static final String TREE_CHI = "data/chi-ontology.xml.gz";

    @Test
    public void treeReader() {

        Tree t = null;
        try {
            InputStream inputStream = new FileInputStream(TREE_CHI);
            GZIPInputStream gzin = new GZIPInputStream(inputStream);
            t = (Tree) new TreeMLReader().readGraph(gzin);
        } catch ( Exception e ) {
            e.printStackTrace();
            Assert.fail();
        }
        
        Assert.assertEquals(true, t.isValidTree());
        
        Node[] nodelist = new Node[t.getNodeCount()];
        
        Iterator nodes = t.nodes();
        for ( int i=0; nodes.hasNext(); ++i ) {
            nodelist[i] = (Node)nodes.next();
        }
        Assert.assertEquals(false, nodes.hasNext());
    }

    @Test
    public void addChild() {
        Tree tree = GraphLib.getBalancedTree(2,1);
        Node r = tree.getRoot();
        Node n = tree.addChild(r);
        Assert.assertEquals(true, n != null);
        assert n != null;
        n.setString("label", "new node");
        Assert.assertEquals(r.getLastChild(), n);
    }

    @Test
    public void removeChild() {
        Tree tree = GraphLib.getBalancedTree(2,1);
        int size = tree.getNodeCount();
        Node r = tree.getRoot();
        Node c = r.getFirstChild();
        Edge e = c.getParentEdge();
        
        Assert.assertEquals(true, tree.removeChild(c));
        Assert.assertEquals(tree.getNodeCount(), size - 1);
        Assert.assertEquals(false, c.isValid());
        Assert.assertEquals(false, e.isValid());
        Assert.assertEquals(true, r.getFirstChild() != c);
    }

    @Test
    public void removeSubtree() {
        Tree tree = GraphLib.getBalancedTree(3,3);
        int size = tree.getNodeCount();
        Node r = tree.getRoot();
        Node c = r.getFirstChild();
        
        Node[] nodes = new Node[13];
        Edge[] edges = new Edge[13];
        Iterator iter = new TreeNodeIterator(c);
        for ( int i=0; iter.hasNext(); ++i ) {
            nodes[i] = (Node)iter.next();
            edges[i] = nodes[i].getParentEdge();
        }
        
        Assert.assertEquals(true, tree.removeChild(c));
        Assert.assertEquals(tree.getNodeCount(), size - 13);
        Assert.assertEquals(true, r.getFirstChild() != c);
        for ( int i=0; i<nodes.length; ++i ) {
            Assert.assertEquals(false, nodes[i].isValid());
            Assert.assertEquals(false, edges[i].isValid());
        }
        
        Assert.assertEquals(true, tree.isValidTree());
    }
    
    public static void main(String[] argv) {

        Tree t = null;

        try {
            InputStream inputStream = new FileInputStream(TREE_CHI);
            GZIPInputStream gzin = new GZIPInputStream(inputStream);
            t = (Tree) new TreeMLReader().readGraph(gzin);
        } catch ( Exception e ) {
            e.printStackTrace();
            System.exit(1);
        }
        
        JPrefuseTable table = new JPrefuseTable(t.getEdgeTable());
        JFrame frame = new JFrame("edges");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new JScrollPane(table));
        frame.pack();
        frame.setVisible(true);
    }
    
}
