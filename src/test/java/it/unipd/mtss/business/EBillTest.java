////////////////////////////////////////////////////////////////////
// DAVIDE MILAN 1216733
// MARCO VOLPATO 1224826
////////////////////////////////////////////////////////////////////

package it.unipd.mtss.business;

import it.unipd.mtss.business.exception.BillException;
import it.unipd.mtss.model.EItem;
import it.unipd.mtss.model.EItemType;
import it.unipd.mtss.model.User;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class EBillTest {
    User testUser;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() {
        testUser = new User(
                1,
                "Jesus",
                new GregorianCalendar(1000, Calendar.DECEMBER, 25).getTime()
        );
    }

    @Test
    public void testTotalNoDiscounts() throws BillException {
        EBill bill = new EBill();

        List<EItem> items = Arrays.asList(
                new EItem(EItemType.PROCESSORE, "Intel i5 6666k", 199.90),
                new EItem(EItemType.SCHEDAMADRE, "Mobo 1", 98.50),
                new EItem(EItemType.MOUSE, "Topolino", 20.10),
                new EItem(EItemType.TASTIERA, "Razer SuperKeys 7000", 449.50)
        );

        double total = bill.getOrderPrice(
                items,
                testUser
        );

        assertEquals(199.90 + 98.50 + 20.10 + 449.50, total, 0.0);
    }

    /*
        ======================
        ====== ISSUE #2 ======
        ======================
    */
    @Test
    public void checkCPUDiscount_zeroCPU() throws BillException {

        EBill bill = new EBill();
        List<EItem> items = Arrays.asList(
                new EItem(EItemType.SCHEDAMADRE, "Mobo 1", 98.50),
                new EItem(EItemType.MOUSE, "Topolino", 20.10),
                new EItem(EItemType.TASTIERA, "Razer SuperKeys 7000", 449.50)
        );
        User user = new User(
                1,
                "Jesus",
                new GregorianCalendar(1000, Calendar.DECEMBER, 25).getTime()
        );
        double total = bill.getOrderPrice(
                items,
                user
        );

        assertEquals(98.50 + 20.10 + 449.50, total, 0.1);
    }

    @Test
    public void checkCPUDiscount_lessThanFiveCPU() throws BillException {

        EBill bill = new EBill();
        List<EItem> items = Arrays.asList(
                new EItem(EItemType.PROCESSORE, "Intel i5 1111k", 111.90),
                new EItem(EItemType.SCHEDAMADRE, "Mobo 1", 98.50),
                new EItem(EItemType.PROCESSORE, "Intel i5 2222k", 222.90),
                new EItem(EItemType.PROCESSORE, "Intel i5 3333k", 333.90),                
                new EItem(EItemType.MOUSE, "Topolino", 20.10),
                new EItem(EItemType.PROCESSORE, "Intel i5 4444k", 444.90),
                new EItem(EItemType.TASTIERA, "Razer SuperKeys 7000", 449.50)
        );
        User user = new User(
                1,
                "Jesus",
                new GregorianCalendar(1000, Calendar.DECEMBER, 25).getTime()
        );
        double total = bill.getOrderPrice(
                items,
                user
        );

        assertEquals(111.90 + 222.90 + 333.90 + 444.90 + 98.50 + 20.10 + 449.50, total, 0.1);
    }

    @Test
    public void checkCPUDiscount_FiveCPU() throws BillException {

        EBill bill = new EBill();
        List<EItem> items = Arrays.asList(
                new EItem(EItemType.PROCESSORE, "Intel i5 1111k", 111.90),
                new EItem(EItemType.SCHEDAMADRE, "Mobo 1", 98.50),
                new EItem(EItemType.PROCESSORE, "Intel i5 2222k", 222.90),
                new EItem(EItemType.PROCESSORE, "Intel i5 3333k", 333.90),                
                new EItem(EItemType.MOUSE, "Topolino", 20.10),
                new EItem(EItemType.PROCESSORE, "Intel i5 4444k", 444.90),
                new EItem(EItemType.TASTIERA, "Razer SuperKeys 7000", 449.50),
                new EItem(EItemType.PROCESSORE, "Intel i5 5555k", 555.90)
        );
        User user = new User(
                1,
                "Jesus",
                new GregorianCalendar(1000, Calendar.DECEMBER, 25).getTime()
        );
        double total = bill.getOrderPrice(
                items,
                user
        );        

        assertEquals(111.90 + 222.90 + 333.90 + 444.90 + 555.90 + 98.50 + 20.10 + 449.50, total, 0.1);
    }

    @Test
    public void checkCPUDiscount_moreThanFiveCPU() throws BillException {

        EBill bill = new EBill();
        List<EItem> items = Arrays.asList(
                new EItem(EItemType.PROCESSORE, "Intel i5 1111k", 111.90),
                new EItem(EItemType.SCHEDAMADRE, "Mobo 1", 98.50),
                new EItem(EItemType.PROCESSORE, "Intel i5 2222k", 222.90),
                new EItem(EItemType.PROCESSORE, "Intel i5 3333k", 333.90),                
                new EItem(EItemType.MOUSE, "Topolino", 20.10),
                new EItem(EItemType.PROCESSORE, "Intel i5 4444k", 444.90),
                new EItem(EItemType.TASTIERA, "Razer SuperKeys 7000", 449.50),
                new EItem(EItemType.PROCESSORE, "Intel i5 5555k", 555.90),
                new EItem(EItemType.PROCESSORE, "Intel i5 6666k", 666.90)
        );
        User user = new User(
                1,
                "Jesus",
                new GregorianCalendar(1000, Calendar.DECEMBER, 25).getTime()
        );
        double total = bill.getOrderPrice(
                items,
                user
        );
        double ass = (111.90 / 2) + 222.90 + 333.90 + 444.90 + 555.90 + 666.90 + 98.50 + 20.10 + 449.50;
        assertEquals(ass, total, 0.1);
    }

    @Test
    public void testEmptyItemsThrows() throws BillException {
        exception.expect(BillException.class);

        EBill bill = new EBill();
        List<EItem> items = new ArrayList<>();
        bill.getOrderPrice(items, testUser);
    }

    @Test
    public void testGiftCheapestOnTenMice() throws BillException {
        EBill bill = new EBill();

        List<EItem> items = new ArrayList<>(Collections.nCopies(
                8,
                new EItem(EItemType.MOUSE, "Topolino", 20.10)
        ));
        items.add(new EItem(EItemType.MOUSE, "Topolino costoso", 39.90));
        items.add(new EItem(EItemType.MOUSE, "Topolino cheap", 14.90));

        double total = bill.getOrderPrice(items, testUser);

        assertEquals((20.10 * 8) + 39.90, total, 0.0);
    }

    @Test
    public void testBigOrderDiscount() throws BillException {
        EBill bill = new EBill();

        List<EItem> items = Arrays.asList(
                new EItem(EItemType.TASTIERA, "Razer SuperKeys 7000", 449.50),
                new EItem(EItemType.TASTIERA, "Razer SuperKeys 7000", 449.50),
                new EItem(EItemType.TASTIERA, "Razer SuperKeys 7000", 449.50)
        );

        double total = bill.getOrderPrice(items, testUser);

        double expectedTotal = 449.50 * 3;
        expectedTotal = expectedTotal - (expectedTotal * 0.1);

        assertEquals(expectedTotal, total, 0.0);
    }



    /*
    ======================
    ====== ISSUE #3 ======
    ======================
    */

    @Test
    public void testMouseKeyboard_NotEqual() throws BillException {
        EBill bill = new EBill();

        List<EItem> items = Arrays.asList(
                new EItem(EItemType.TASTIERA, "Razer SuperKeys 7000", 449.50),
                new EItem(EItemType.MOUSE, "Topolino costoso", 39.90),
                new EItem(EItemType.TASTIERA, "Razer SuperKeys 7000", 449.50)
        );

        double total = bill.getOrderPrice(items, testUser);

        double assertion = 449.50 * 2 + 39.90;
        assertEquals(assertion, total, 0.1);
    }
    
    @Test
    public void testMouseKeyboard_Equal() throws BillException {
        EBill bill = new EBill();

        List<EItem> items = Arrays.asList(
                new EItem(EItemType.TASTIERA, "Razer SuperKeys 7000", 449.50),
                new EItem(EItemType.MOUSE, "Topolino costoso", 39.90),
                new EItem(EItemType.TASTIERA, "Razer SuperKeys 7000", 449.50),
                new EItem(EItemType.MOUSE, "Topolino cheap", 14.90)
        );

        double total = bill.getOrderPrice(items, testUser);

        double assertion = 449.50 * 2 + 39.90;
        assertEquals(assertion, total, 0.1);
    }
}
