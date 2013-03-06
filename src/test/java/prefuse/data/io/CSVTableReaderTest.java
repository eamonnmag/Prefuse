package prefuse.data.io;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;
import prefuse.data.Table;
import prefuse.TestConfig;
import prefuse.data.TableTestData;

public class CSVTableReaderTest implements TableTestData {

    /*
     * Test method for 'edu.berkeley.guir.prefuse.data.io.CSVTableReader.readTable(InputStream)'
     */
    @Test
    public void testReadTableInputStream() {
        // prepare data
        byte[] data = CSV_DATA.getBytes();
        InputStream is = new ByteArrayInputStream(data);
        
        // parse data
        CSVTableReader ctr = new CSVTableReader();
        Table t = null;
        try {
            t = ctr.readTable(is);
        } catch ( DataIOException e ) {
            e.printStackTrace();
            Assert.fail("Data Read Exception");
        }
        
        boolean verbose = TestConfig.verbose();
        
        // text-dump
        if (verbose) System.out.println("-- Data Types -------------");
        for (int c = 0, idx = -1; c < t.getColumnCount(); ++c) {
            String name = t.getColumnType(c).getName();
            if ( (idx=name.lastIndexOf('.')) >= 0 )
                name = name.substring(idx+1);
            Assert.assertEquals(t.getColumnType(c), TYPES[c]);
            if (verbose) System.out.print(name + "\t");
        }
        if (verbose) System.out.println();
        
        if (verbose) System.out.println();
        
        if (verbose) System.out.println("-- Table Data -------------");
        for (int c = 0; c < t.getColumnCount(); ++c) {
            if (verbose) System.out.print(t.getColumnName(c) + "\t");
            Assert.assertEquals(t.getColumnName(c), HEADERS[c]);
        }
        if (verbose) System.out.println();
        for (int r = 0; r < t.getRowCount(); ++r) {
            for (int c = 0; c < t.getColumnCount(); ++c) {
                Object o = t.get(r, c);
                if (verbose) System.out.print(o + "\t");
                Assert.assertEquals(TABLE[c][r], o);
            }
            if (verbose) System.out.println();
        }
        if (verbose) System.out.println();

    }

}
