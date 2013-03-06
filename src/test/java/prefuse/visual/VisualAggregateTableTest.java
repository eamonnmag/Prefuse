package prefuse.visual;

import java.util.Iterator;

import junit.framework.TestCase;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import prefuse.Visualization;
import prefuse.visual.AggregateTable;
import prefuse.visual.VisualItem;
import prefuse.visual.VisualTable;
import prefuse.data.TableTest;

public class VisualAggregateTableTest {

    private AggregateTable m_agg;
    private VisualTable m_items;
    
    @Before
    public void setUp() throws Exception {
        Visualization v = new Visualization();
        m_items = v.addTable("items", TableTest.getTestCaseTable());
        
        m_agg = v.addAggregates("aggregates", VisualItem.SCHEMA);
        m_agg.addRow();
        m_agg.addRow();
        
        Iterator iter = m_items.tuples();
        for ( int i=0, count=m_items.getRowCount(); iter.hasNext(); ++i ) {
            VisualItem item = (VisualItem)iter.next();
            int j = i<count/2 ? 0 : 1;
            m_agg.addToAggregate(j, item);
        }
    }

    @After
    public void tearDown() throws Exception {
        m_items = null;
        m_agg = null;
    }

    /*
     * Test method for 'prefuse.data.tuple.AggregateTable.getAggregateSize(int)'
     */
    @Test
    public void testGetAggregateSize() {
        int cc = m_items.getRowCount();
        int s1 = cc/2, s2 = cc-s1;
        Assert.assertEquals(s1, m_agg.getAggregateSize(0));
        Assert.assertEquals(s2, m_agg.getAggregateSize(1));
    }

    /*
     * Test method for 'prefuse.data.tuple.AggregateTable.addToAggregate(int, Tuple)'
     */
    @Test
    public void testAddToAggregate() {
        VisualItem t = m_items.getItem(0);
        int size = m_agg.getAggregateSize(1);
        Assert.assertFalse(m_agg.aggregateContains(1, t));
        m_agg.addToAggregate(1, t);
        Assert.assertTrue(m_agg.aggregateContains(1, t));
        Assert.assertEquals(size + 1, m_agg.getAggregateSize(1));
    }

    /*
     * Test method for 'prefuse.data.tuple.AggregateTable.removeFromAggregate(int, Tuple)'
     */
    @Test
    public void testRemoveFromAggregate() {
        int s = m_agg.getAggregateSize(0);
        
        Assert.assertTrue(m_agg.aggregateContains(0, m_items.getItem(0)));
        m_agg.removeFromAggregate(0, m_items.getItem(0));
        Assert.assertFalse(m_agg.aggregateContains(0, m_items.getItem(0)));
        Assert.assertEquals(--s, m_agg.getAggregateSize(0));
        
        Assert.assertTrue(m_agg.aggregateContains(0, m_items.getItem(1)));
        m_agg.removeFromAggregate(0, m_items.getItem(1));
        Assert.assertFalse(m_agg.aggregateContains(0, m_items.getItem(1)));
        Assert.assertEquals(--s, m_agg.getAggregateSize(0));
    }

    @Test
    public void testRemoveFromAggregateUnderIteration() {
        int s = m_agg.getAggregateSize(0);
        Iterator iter = m_agg.aggregatedTuples(0);
        while ( iter.hasNext() ) {
            VisualItem t = (VisualItem)iter.next();
            Assert.assertTrue(m_agg.aggregateContains(0, t));
            m_agg.removeFromAggregate(0, t);
            Assert.assertEquals(--s, m_agg.getAggregateSize(0));
            Assert.assertFalse(m_agg.aggregateContains(0, t));
        }
    }
    
    /*
     * Test method for 'prefuse.data.tuple.AggregateTable.removeAllFromAggregate(int)'
     */
    @Test
    public void testRemoveAllFromAggregate() {
        m_agg.removeAllFromAggregate(0);
        m_agg.removeAllFromAggregate(1);
        Assert.assertEquals(0, m_agg.getAggregateSize(0));
        Assert.assertEquals(0, m_agg.getAggregateSize(1));
    }

    /*
     * Test method for 'prefuse.data.tuple.AggregateTable.aggregateContains(int, Tuple)'
     */
    @Test
    public void testAggregateContains() {
        VisualItem vi0 = m_items.getItem(0);
        VisualItem vi1 = m_items.getItem(m_items.getRowCount()-1);
        Assert.assertTrue(m_agg.aggregateContains(0, vi0));
        Assert.assertTrue(m_agg.aggregateContains(1, vi1));
    }

    /*
     * Test method for 'prefuse.data.tuple.AggregateTable.aggregatedTuples(int)'
     */
    @Test
    public void testAggregatedTuples() {
        int s = m_agg.getAggregateSize(0);
        Iterator iter = m_agg.aggregatedTuples(0);
        int count = 0;
        for ( ; iter.hasNext(); ++count ) {
            VisualItem t = (VisualItem)iter.next();
            Assert.assertTrue(m_agg.aggregateContains(0, t));
        }
        Assert.assertEquals(s, count);
    }
    
    /*
     * Test method for 'prefuse.data.tuple.AggregateTable.getAggregates(Tuple)'
     */
    @Test
    public void testGetAggregates() {
        for ( int i=0; i<2; ++i ) {
            Iterator iter = m_agg.aggregatedTuples(0);
            while ( iter.hasNext() ) {
                VisualItem t = (VisualItem)iter.next();
                Iterator aggr = m_agg.getAggregates(t);
                Assert.assertEquals(m_agg.getTuple(0), aggr.next());
            }
        }
    }

}
