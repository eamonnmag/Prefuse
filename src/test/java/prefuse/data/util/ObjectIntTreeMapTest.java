package prefuse.data.util;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

import junit.framework.TestCase;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import prefuse.util.collections.IntIterator;
import prefuse.util.collections.LiteralIterator;
import prefuse.util.collections.ObjectIntTreeMap;

public class ObjectIntTreeMapTest {
    
    ObjectIntTreeMap map = new ObjectIntTreeMap(true);
    int[] keys = { 1, 2, 5, 3, 4, 5, 10 };
    int[] sort;
    
    public ObjectIntTreeMapTest() {
        sort = (int[])keys.clone();
        Arrays.sort(sort);
    }
    
    @Before
    public void setUp() throws Exception {
        for ( int i=0; i<keys.length; ++i ) {
            map.put(new Integer(keys[i]),keys[i]);
        }
    }

    @After
    public void tearDown() throws Exception {
        map.clear();
    }

    /*
     * Test method for 'edu.berkeley.guir.prefuse.data.util.IntIntTreeMap.clear()'
     */
    @Test
    public void testClear() {
        map.clear();
        Assert.assertTrue(map.isEmpty());
        try {
            map.keyIterator().next();
            Assert.fail("Iterator should be empty");
        } catch ( NoSuchElementException success ) {
        }
        Assert.assertEquals(map.get(new Integer(1)), Integer.MIN_VALUE);
    }

    /*
     * Test method for 'edu.berkeley.guir.prefuse.data.util.IntIntTreeMap.get(int)'
     */
    @Test
    public void testGet() {
        for ( int i=0; i<map.size(); ++i ) {
            Assert.assertEquals(map.get(new Integer(keys[i])), keys[i]);
        }
    }

    /*
     * Test method for 'edu.berkeley.guir.prefuse.data.util.IntIntTreeMap.put(int, int)'
     */
    @Test
    public void testPut() {
        map.clear();
        int size = 0;
        for ( int i=0; i<keys.length; ++i ) {
            map.put(new Integer(keys[i]),keys[i]);
            Assert.assertEquals(++size, map.size());
            Assert.assertEquals(map.get(new Integer(keys[i])), keys[i]);
        }
    }

    /*
     * Test method for 'edu.berkeley.guir.prefuse.data.util.IntIntTreeMap.remove(int)'
     */
    @Test
    public void testRemoveInt() {
        int size = map.size();
        for ( int i=0; i<keys.length; ++i ) {
            int val = map.remove(new Integer(keys[i]));
            Assert.assertEquals(keys[i], val);
            Assert.assertEquals(--size, map.size());
        }
        for ( int i=0; i<keys.length; ++i ) {
            Assert.assertEquals(map.get(new Integer(keys[i])), Integer.MIN_VALUE);
        }
    }

    /*
     * Test method for 'edu.berkeley.guir.prefuse.data.util.IntIntTreeMap.firstKey()'
     */
    @Test
    public void testFirstKey() {
        Assert.assertEquals(((Integer) map.firstKey()).intValue(), sort[0]);
    }

    /*
     * Test method for 'edu.berkeley.guir.prefuse.data.util.IntIntTreeMap.lastKey()'
     */
    @Test
    public void testLastKey() {
        Assert.assertEquals(((Integer) map.lastKey()).intValue(), sort[sort.length - 1]);
    }

    /*
     * Test method for 'edu.berkeley.guir.prefuse.data.util.IntIntTreeMap.keyIterator()'
     */
    @Test
    public void testKeyIterator() {
        Iterator iter = map.keyIterator();
        for ( int i=0; iter.hasNext(); ++i ) {
            Integer key = (Integer)iter.next();
            Assert.assertEquals(sort[i], key.intValue());
        }
    }

    /*
     * Test method for 'edu.berkeley.guir.prefuse.data.util.IntIntTreeMap.subMap(int, int)'
     */
    @Test
    public void testSubMap() {
        int k1, i1, i2, i, k, len = sort.length-1;
        for ( i=0, k=sort[0]; k==sort[0]; ++i, k=sort[i] );
        k1 = k; i1 = i;
        for ( i=len, k=sort[len]; i>=0 && k==sort[len]; --i, k=sort[i] );
        i2 = i;
        
        Object loKey = new Integer(k1);
        Object hiKey = new Integer(sort[len]);
        
        Iterator iter = map.keyRangeIterator(loKey, true, hiKey, false);
        for ( i=i1; iter.hasNext() && i <= i2; ++i ) {
            Assert.assertEquals(((Integer) iter.next()).intValue(), sort[i]);
        }
        Assert.assertTrue(!iter.hasNext() && i == i2 + 1);
        
        IntIterator liter = map.valueRangeIterator(loKey, true, hiKey, false);
        for ( i=i1; liter.hasNext() && i <= i2; ++i ) {
            Assert.assertEquals(liter.nextInt(), sort[i]);
        }
        Assert.assertTrue(!iter.hasNext() && i == i2 + 1);
    }

    /*
     * Test method for 'edu.berkeley.guir.prefuse.data.util.AbstractTreeMap.size()'
     */
    @Test
    public void testSize() {
        Assert.assertEquals(map.size(), keys.length);
    }

    /*
     * Test method for 'edu.berkeley.guir.prefuse.data.util.AbstractTreeMap.isEmpty()'
     */
    @Test
    public void testIsEmpty() {
        Assert.assertFalse(map.isEmpty());
    }

    /*
     * Test method for 'edu.berkeley.guir.prefuse.data.util.AbstractTreeMap.valueIterator()'
     */
    @Test
    public void testValueIterator() {
        LiteralIterator iter = map.valueIterator(true);
        for ( int i=0; iter.hasNext(); ++i ) {
            int val = iter.nextInt();
            Assert.assertEquals(sort[i], val);
        }
    }

}
