////////////////////////////////////////////////////////////////////
// DAVIDE MILAN 1216733
// MARCO VOLPATO 1224826
////////////////////////////////////////////////////////////////////

package it.unipd.mtss.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EItemTest {
    EItem testItem;

    @Before
    public void setUp() {
        testItem = new EItem(
                EItemType.TASTIERA,
                "Logitecca MegaTastiera",
                59.90
        );
    }

    @Test
    public void testGetItemType() {
        assertEquals(EItemType.TASTIERA, testItem.getItemType());
    }

    @Test
    public void testGetName() {
        assertEquals("Logitecca MegaTastiera", testItem.getName());
    }

    @Test
    public void testGetPrice() {
        assertEquals(59.90, testItem.getPrice(), 0.0);
    }
}
