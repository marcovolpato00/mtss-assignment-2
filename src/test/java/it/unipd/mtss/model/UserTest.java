////////////////////////////////////////////////////////////////////
// DAVIDE MILAN 1216733
// MARCO VOLPATO 1224826
////////////////////////////////////////////////////////////////////

package it.unipd.mtss.model;

import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.time.LocalDate;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;

public class UserTest {
    private User testUser;

    @Before
    public void setUp() {
        testUser = new User(
                1,
                "Someone",
                LocalDate.of(1000, 12, 25)
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
        LocalDate expected = LocalDate.of(1000, 12, 25);
        assertEquals(expected, testUser.getBirth());
    }
}
