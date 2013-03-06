package prefuse.data;

import java.util.Iterator;

import junit.framework.TestCase;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import prefuse.util.GraphLib;
import prefuse.TestConfig;

public class GraphTest implements GraphTestData {

    public static Graph getTestCaseGraph() {
        Table nodes = new Table(NNODES, NNODECOLS);
        for ( int c=0; c<NNODECOLS; ++c ) {
            nodes.addColumn(NHEADERS[c], NTYPES[c]);
            for ( int r=0; r<NNODES; ++r ) {
                nodes.set(r, NHEADERS[c], NODES[c][r]);
            }
        }
        
        Table edges = new Table(NEDGES, NEDGECOLS);
        for ( int c=0; c<NEDGECOLS; ++c ) {
            edges.addColumn(EHEADERS[c], ETYPES[c]);
            for ( int r=0; r<NEDGES; ++r ) {
                edges.set(r, EHEADERS[c], EDGES[c][r]);
            }
        }
        
        return new Graph(nodes, edges, false, 
                NHEADERS[0], EHEADERS[0], EHEADERS[1]);
    }
    
    private Graph graph;
    
    @Before
    public void setUp() throws Exception {
        graph = getTestCaseGraph();
    }

    /**
     * @see junit.framework.TestCase#tearDown()
     */
    @After
    public void tearDown() throws Exception {
        graph = null;
    }

    @Test
    public void testGraph() {
        boolean verbose = TestConfig.verbose();
        
        Table nodes = graph.getNodeTable();
        Table edges = graph.getEdgeTable();
        
        // check the basics
        Assert.assertEquals(NNODES, graph.getNodeCount());
        Assert.assertEquals(NEDGES, graph.getEdgeCount());
        Assert.assertEquals(NHEADERS[0], graph.getNodeKeyField());
        Assert.assertEquals(EHEADERS[0], graph.getEdgeSourceField());
        Assert.assertEquals(EHEADERS[1], graph.getEdgeTargetField());
        
        // check all nodes, basic data
        Iterator niter = graph.nodes();
        while ( niter.hasNext() ) {
            Node node = (Node)niter.next();
            int nrow = node.getRow();

            if ( verbose ) System.out.print(nrow+"\t");
            
            // check data members
            for ( int i=0; i<NNODECOLS; ++i ) {
                Assert.assertEquals(NODES[i][nrow], node.get(NHEADERS[i]));
                Assert.assertEquals(NODES[i][nrow], nodes.get(nrow, NHEADERS[i]));
                if ( verbose ) System.out.print(NHEADERS[i]+":"+NODES[i][nrow]+"\t");
            }
            
            if ( verbose ) {
                System.out.print("in:"+node.getInDegree());
                System.out.print("\t");
                System.out.print("out:"+node.getOutDegree());
                System.out.println();
            }
            
            // check degrees
            Assert.assertEquals(node.getInDegree(), INDEGREE[nrow]);
            Assert.assertEquals(graph.getInDegree(nrow), INDEGREE[nrow]);
            Assert.assertEquals(node.getOutDegree(), OUTDEGREE[nrow]);
            Assert.assertEquals(graph.getOutDegree(nrow), OUTDEGREE[nrow]);
            
            // check edges
            Iterator eiter = node.inEdges();
            while ( eiter.hasNext() ) {
                Edge edge = (Edge)eiter.next();
                int erow = edge.getRow();
                Assert.assertEquals(nrow, edge.getTargetNode().getRow());
                Assert.assertEquals(nrow, graph.getTargetNode(erow));
            }
            eiter = node.outEdges();
            while ( eiter.hasNext() ) {
                Edge edge = (Edge)eiter.next();
                int erow = edge.getRow();
                Assert.assertEquals(nrow, edge.getSourceNode().getRow());
                Assert.assertEquals(nrow, graph.getSourceNode(erow));
            }
        }
        
        // check all edges, basic data
        Iterator eiter = graph.edges();
        while ( eiter.hasNext() ) {
            Edge edge = (Edge)eiter.next();
            int erow = edge.getRow();
            
            // check data members
            for ( int i=0; i<NEDGECOLS; ++i ) {
                Assert.assertEquals(EDGES[i][erow], edge.get(EHEADERS[i]));
                Assert.assertEquals(EDGES[i][erow], edges.get(erow, EHEADERS[i]));
            }
            
            // check nodes
            Node s = edge.getSourceNode();
            int srow = s.getRow();
            Assert.assertEquals(srow, graph.getSourceNode(erow));
            int sk = nodes.getInt(srow, NHEADERS[0]);
            Assert.assertEquals(sk, edges.getInt(erow, EHEADERS[0]));
            
            Node t = edge.getTargetNode();
            int trow = t.getRow();
            Assert.assertEquals(trow, graph.getTargetNode(erow));
            int tk = nodes.getInt(trow, NHEADERS[0]);
            Assert.assertEquals(tk, edges.getInt(erow, EHEADERS[1]));
            
            Assert.assertEquals(srow, edge.getAdjacentNode(t).getRow());
            Assert.assertEquals(trow, edge.getAdjacentNode(s).getRow());
            Assert.assertEquals(srow, graph.getAdjacentNode(erow, trow));
            Assert.assertEquals(trow, graph.getAdjacentNode(erow, srow));
        }
    }
    
    @Test
    public void testRemoveNode() {
        int cliqueSize = 5;
        Graph g = GraphLib.getClique(cliqueSize);
        Edge[] edges = new Edge[4];
        
        Node rem = (Node)g.nodes().next();
        Iterator it = rem.edges();
        for ( int i=0; it.hasNext(); ++i ) {
            edges[i] = (Edge)it.next();
        }
        
        Assert.assertEquals(true, g.removeNode(rem));
        Assert.assertEquals(false, rem.isValid());
        
        Iterator nodes = g.nodes();
        while ( nodes.hasNext() ) {
            Assert.assertEquals(cliqueSize - 2, ((Node) nodes.next()).getDegree());
        }
        for ( int i=0; i<edges.length; ++i ) {
            Assert.assertEquals(false, edges[i].isValid());
        }
    }
}
