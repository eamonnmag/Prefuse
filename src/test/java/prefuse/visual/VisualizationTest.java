package prefuse.visual;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import prefuse.Visualization;
import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.data.Table;
import prefuse.data.Tuple;
import prefuse.data.tuple.TupleSet;
import prefuse.visual.NodeItem;
import prefuse.visual.VisualGraph;
import prefuse.visual.VisualItem;
import prefuse.visual.VisualTable;
import prefuse.data.GraphTest;
import prefuse.data.TableTest;

public class VisualizationTest {

    private Graph m_g;
    private Table m_t;
    private Visualization m_vis;
    private Tuple m_t0;
    private Node m_n0;
    private VisualItem m_vt0;
    private NodeItem m_vn0;
    
    @Before
    public void setUp() {
        m_vis = new Visualization();
        m_t = TableTest.getTestCaseTable();
        m_g = GraphTest.getTestCaseGraph();
        
        m_t0 = m_t.getTuple(0);
        m_n0 = m_g.getNode(0);
        
        VisualTable vt = (VisualTable)m_vis.add("t", m_t);
        VisualGraph vg = (VisualGraph)m_vis.add("g", m_g);
        
        m_vt0 = vt.getItem(0);
        m_vn0 = (NodeItem)vg.getNode(0);
        
        TupleSet ts = m_vis.getFocusGroup(Visualization.FOCUS_ITEMS);
        ts.addTuple(m_vt0);
        ts.addTuple(m_vn0);
    }
    
    @Test
    public void testRepeatGroup() {
    	Table t = new Table();
    	Graph g = new Graph();
    	try {
    		m_vis.add("t", t);
    		Assert.fail("Should not allow duplicate groups");
    	} catch ( Exception e ) {
    	}
    	try {
    		m_vis.addFocusGroup(Visualization.FOCUS_ITEMS, t);
    		Assert.fail("Should not allow duplicate groups");
    	} catch ( Exception e ) {
    	}
    	try {
    		m_vis.add("g", g);
    		Assert.fail("Should not allow duplicate groups");
    	} catch ( Exception e ) {
    	}
    	m_vis.removeGroup("t");
    	try {
    		m_vis.add("t", t);
    	} catch ( Exception e ) {
    		Assert.fail("Should be able to re-use group name after removal");
    	}
    	m_vis.removeGroup("g");
    	try {
    		m_vis.add("g", t);
    	} catch ( Exception e ) {
    		Assert.fail("Should be able to re-use group name after removal");
    	}
    }
    
    /*
     * Test method for 'prefuse.Visualization.removeGroup(String)'
     */
    @Test
    public void testRemoveGroup() {
        m_vis.removeGroup("g");
        Assert.assertEquals(null, m_vis.getGroup("g"));
        Assert.assertEquals(null, m_vis.getGroup("g.nodes"));
        Assert.assertEquals(null, m_vis.getGroup("g.edges"));
        Assert.assertEquals(null, m_vis.getSourceData("g"));
        Assert.assertEquals(null, m_vis.getSourceData("g.nodes"));
        Assert.assertEquals(null, m_vis.getSourceData("g.edges"));
        Assert.assertEquals(false, m_vis.getFocusGroup("_focus_").containsTuple(m_n0));
    }

    /*
     * Test method for 'prefuse.Visualization.reset()'
     */
    @Test
    public void testReset() {
        m_vis.reset();
        
        Assert.assertEquals(null, m_vis.getGroup("t"));
        Assert.assertEquals(null, m_vis.getSourceData("t"));
        
        Assert.assertEquals(null, m_vis.getGroup("g"));
        Assert.assertEquals(null, m_vis.getGroup("g.nodes"));
        Assert.assertEquals(null, m_vis.getGroup("g.edges"));
        Assert.assertEquals(null, m_vis.getSourceData("g"));
        Assert.assertEquals(null, m_vis.getSourceData("g.nodes"));
        Assert.assertEquals(null, m_vis.getSourceData("g.edges"));
        
        Assert.assertEquals(0, m_vis.size("t"));
        Assert.assertEquals(0, m_vis.size("g"));
        Assert.assertEquals(0, m_vis.size("g.nodes"));
        Assert.assertEquals(0, m_vis.size("g.edges"));
        Assert.assertEquals(0, m_vis.getFocusGroup("_focus_").getTupleCount());
        Assert.assertEquals(false, m_vis.items().hasNext());
    }

    /*
     * Test method for 'prefuse.Visualization.getSourceData(String)'
     */
    @Test
    public void testGetSourceData() {
        Assert.assertEquals(m_t, m_vis.getSourceData("t"));
        Assert.assertEquals(m_t, m_vt0.getSourceData());
        
        Assert.assertEquals(m_g, m_vis.getSourceData("g"));
        Assert.assertEquals(m_g.getNodeTable(), m_vis.getSourceData("g.nodes"));
        Assert.assertEquals(m_g.getEdgeTable(), m_vis.getSourceData("g.edges"));
        Assert.assertEquals(m_g.getNodeTable(), m_vn0.getSourceData());
    }

    /*
     * Test method for 'prefuse.Visualization.getSourceTuple(VisualItem)'
     */
    @Test
    public void testGetSourceTuple() {
        Assert.assertEquals(m_t0, m_vis.getSourceTuple(m_vt0));
        Assert.assertEquals(m_n0, m_vis.getSourceTuple(m_vn0));
        Assert.assertEquals(m_t0, m_vt0.getSourceTuple());
        Assert.assertEquals(m_n0, m_vn0.getSourceTuple());
    }
    
    /*
     * Test method for 'prefuse.Visualization.getVisualItem(String,Tuple)'
     */
    @Test
    public void testGetVisualItem() {
        Assert.assertEquals(m_vt0, m_vis.getVisualItem("t", m_t0));
        Assert.assertEquals(m_vn0, m_vis.getVisualItem("g", m_n0));
        Assert.assertEquals(m_vn0, m_vis.getVisualItem("g.nodes", m_n0));
    }

}
