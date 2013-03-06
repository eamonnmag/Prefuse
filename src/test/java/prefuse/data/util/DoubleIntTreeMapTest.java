package prefuse.data.util;

import java.util.Arrays;
import java.util.NoSuchElementException;

import junit.framework.TestCase;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import prefuse.util.collections.DoubleIntTreeMap;
import prefuse.util.collections.LiteralIterator;

public class DoubleIntTreeMapTest {
    
    DoubleIntTreeMap map = new DoubleIntTreeMap(true);
    int[] keys = { 1, 2, 5, 3, 4, 5, 10 };
    int[] sort;
    
    public DoubleIntTreeMapTest() {
        sort = keys.clone();
        Arrays.sort(sort);
    }
    
    @Before
    public void setUp() throws Exception {
        for (int key : keys) {
            map.put(key, key);
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
        Assert.assertEquals(map.get(1), Integer.MIN_VALUE);
    }

    /*
     * Test method for 'edu.berkeley.guir.prefuse.data.util.IntIntTreeMap.get(int)'
     */
    @Test
    public void testGet() {
        for ( int i=0; i<map.size(); ++i ) {
            Assert.assertEquals(map.get(keys[i]), keys[i]);
        }
    }

    /*
     * Test method for 'edu.berkeley.guir.prefuse.data.util.IntIntTreeMap.put(int, int)'
     */
    @Test
    public void testPut() {
        map.clear();
        int size = 0;
        for (int key : keys) {
            map.put(key, key);
            Assert.assertEquals(++size, map.size());
            Assert.assertEquals(map.get(key), key);
        }
    }

    /*
     * Test method for 'edu.berkeley.guir.prefuse.data.util.IntIntTreeMap.remove(int)'
     */
    @Test
    public void testRemoveInt() {
        int size = map.size();
        for (int key : keys) {
            int val = map.remove(key);
            Assert.assertEquals(key, val);
            Assert.assertEquals(--size, map.size());
        }
        for (int key : keys) {
            Assert.assertEquals(map.get(key), Integer.MIN_VALUE);
        }
    }

    /*
     * Test method for 'edu.berkeley.guir.prefuse.data.util.IntIntTreeMap.firstKey()'
     */
    @Test
    public void testFirstKey() {
        Assert.assertEquals((int) map.firstKey(), sort[0]);
    }

    /*
     * Test method for 'edu.berkeley.guir.prefuse.data.util.IntIntTreeMap.lastKey()'
     */
    @Test
    public void testLastKey() {
        Assert.assertEquals((int) map.lastKey(), sort[sort.length - 1]);
    }

    /*
     * Test method for 'edu.berkeley.guir.prefuse.data.util.IntIntTreeMap.keyIterator()'
     */
    @Test
    public void testKeyIterator() {
        LiteralIterator iter = map.keyIterator();
        for ( int i=0; iter.hasNext(); ++i ) {
            double key = iter.nextDouble();
            Assert.assertEquals(sort[i], (int) key);
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
        
        LiteralIterator iter = map.keyRangeIterator(k1, true, sort[len], false);
        for ( i=i1; iter.hasNext() && i <= i2; ++i ) {
            Assert.assertEquals((int) iter.nextDouble(), sort[i]);
        }
        Assert.assertTrue(!iter.hasNext() && i == i2 + 1);
        
        iter = map.valueRangeIterator(k1, true, sort[len], false);
        for ( i=i1; iter.hasNext() && i <= i2; ++i ) {
            Assert.assertEquals(iter.nextInt(), sort[i]);
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
