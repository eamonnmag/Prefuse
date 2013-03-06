package prefuse.data.expression;

import junit.framework.TestCase;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import prefuse.data.Table;
import prefuse.data.expression.IfExpression;
import prefuse.data.expression.Predicate;
import prefuse.data.expression.parser.ExpressionParser;
import prefuse.util.PredicateChain;
import prefuse.data.TableTest;

public class PredicateChainTest {

    private PredicateChain m_chain;
    private Table m_table;
    
    private Predicate p1, p2, p3;
    
    @Before
    public void setUp() throws Exception {
        m_table = TableTest.getTestCaseTable();
        
        p1 = (Predicate)ExpressionParser.parse("id=3");
        p2 = (Predicate)ExpressionParser.parse("float<2");
        p3 = (Predicate)ExpressionParser.parse("id>3");
        
        m_chain = new PredicateChain();
        m_chain.add(p1, 1);
        m_chain.add(p2, 2);
        m_chain.add(p3, 3);
    }

    @After
    public void tearDown() throws Exception {
        m_chain = null;
        m_table = null;
        p1 = null;
        p2 = null;
        p3 = null;
    }

    /*
     * Test method for 'prefuse.util.PredicateChain.get(Tuple)'
     */
    @Test
    public void testGet() {
        Assert.assertEquals(1, m_chain.get(m_table.getTuple(2)));
        Assert.assertEquals(2, m_chain.get(m_table.getTuple(0)));
        Assert.assertEquals(3, m_chain.get(m_table.getTuple(3)));
        Assert.assertEquals(null, m_chain.get(m_table.getTuple(1)));
    }

    /*
     * Test method for 'prefuse.util.PredicateChain.add(Predicate, Object)'
     */
    @Test
    public void testAdd() {
        Predicate p = (Predicate)ExpressionParser.parse("id=2");
        m_chain.add(p, 4);
        Assert.assertEquals(4, m_chain.get(m_table.getTuple(1)));
    }

    /*
     * Test method for 'prefuse.util.PredicateChain.remove(Predicate)'
     */
    @Test
    public void testRemove() {
        Assert.assertTrue(m_chain.getExpression() instanceof IfExpression);
        Assert.assertTrue(m_chain.remove(p1));
        Assert.assertEquals(2, m_chain.get(m_table.getTuple(2)));
        
        Assert.assertTrue(m_chain.getExpression() instanceof IfExpression);
        Assert.assertTrue(m_chain.remove(p2));
        Assert.assertEquals(null, m_chain.get(m_table.getTuple(0)));
        Assert.assertEquals(null, m_chain.get(m_table.getTuple(1)));
        Assert.assertEquals(null, m_chain.get(m_table.getTuple(2)));
        
        Assert.assertTrue(m_chain.getExpression() instanceof IfExpression);
        Assert.assertTrue(m_chain.remove(p3));
        Assert.assertEquals(null, m_chain.get(m_table.getTuple(3)));
        
        Assert.assertFalse(m_chain.getExpression() instanceof IfExpression);
        Assert.assertFalse(m_chain.remove(p1));
        Assert.assertFalse(m_chain.remove(p2));
        Assert.assertFalse(m_chain.remove(p3));
    }

    /*
     * Test method for 'prefuse.util.PredicateChain.clear()'
     */
    @Test
    public void testClear() {
        Assert.assertTrue(m_chain.getExpression() instanceof IfExpression);
        m_chain.clear();
        Assert.assertEquals(null, m_chain.get(m_table.getTuple(0)));
        Assert.assertEquals(null, m_chain.get(m_table.getTuple(1)));
        Assert.assertEquals(null, m_chain.get(m_table.getTuple(2)));
        Assert.assertEquals(null, m_chain.get(m_table.getTuple(3)));
        Assert.assertFalse(m_chain.getExpression() instanceof IfExpression);
        Assert.assertFalse(m_chain.remove(p1));
        Assert.assertFalse(m_chain.remove(p2));
        Assert.assertFalse(m_chain.remove(p3));
    }

    @Test
    public void testRemove2() {
        PredicateChain pc = new PredicateChain();
        Predicate p1 = (Predicate) ExpressionParser.parse("_fixed");
        Predicate p2 = (Predicate) ExpressionParser.parse("_highlight");
        pc.add(p1, 1);
        pc.add(p2, 2);
        Assert.assertTrue(pc.remove(p2));
    }
    
}
