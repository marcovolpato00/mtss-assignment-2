////////////////////////////////////////////////////////////////////
// DAVIDE MILAN 1216733
// MARCO VOLPATO 1224826
////////////////////////////////////////////////////////////////////

package it.unipd.mtss.model;

import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;

public class UserTest {
    private User testUser;

    @Before
    public void setUp() {
        testUser = new User(
                1,
                "Someone",
                new GregorianCalendar(2022, Calendar.MAY, 20).getTime()
        );
    }

    @Test
    public void testGetId() {
        assertEquals(1, testUser.getId());
    }

    @Test
    public void testGetName() {
        assertEquals("Someone", testUser.getName());
    }

    @Test
    public void testGetBirth() {
        Date expected = new GregorianCalendar(
                2022,
                Calendar.MAY,
                20
        ).getTime();
        assertEquals(expected, testUser.getBirth());
    }
}
