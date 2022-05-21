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

        double total = bill.getOrderPriceNoDiscount(
                items
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
        //double total = bill.getOrderPriceNoDiscount(items);
        double discount = bill.getCPUDiscount(items);

        assertEquals(0, discount, 0.1);
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

        //double total = bill.getOrderPriceNoDiscount(items);
        double discount = bill.getCPUDiscount(items);

        assertEquals(0, discount, 0.1);
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

        //double total = bill.getOrderPriceNoDiscount(items);
        double discount = bill.getCPUDiscount(items);

        assertEquals(0, discount, 0.1);
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

        //double total = bill.getOrderPriceNoDiscount(items);
        double discount = bill.getCPUDiscount(items);

        assertEquals(55.95, discount, 0.1);
    }

    @Test
    public void testEmptyItemsThrows() throws BillException {
        exception.expect(BillException.class);

        EBill bill = new EBill();
        List<EItem> items = new ArrayList<>();
        bill.getOrderPrice(items, testUser);
    }

    @Test
    public void testGiftCheapestOnTenMice() {
        EBill bill = new EBill();

        List<EItem> items = new ArrayList<>(Collections.nCopies(
                8,
                new EItem(EItemType.MOUSE, "Topolino", 20.10)
        ));
        items.add(new EItem(EItemType.MOUSE, "Topolino costoso", 39.90));
        items.add(new EItem(EItemType.MOUSE, "Topolino cheap", 14.90));

        double gift = bill.getMiceGift(items);

        assertEquals(14.90, gift, 0.0);
    }

    @Test
    public void testNotGiftCheapestMouse() {
        EBill bill = new EBill();

        List<EItem> items = new ArrayList<>(Collections.nCopies(
                6,
                new EItem(EItemType.MOUSE, "Topolino", 20.10)
        ));
        items.add(new EItem(EItemType.MOUSE, "Topolino costoso", 39.90));
        items.add(new EItem(EItemType.MOUSE, "Topolino cheap", 14.90));

        double gift = bill.getMiceGift(items);

        assertEquals(0, gift, 0.0);
    }

    @Test
    public void testBigOrderDiscount() throws BillException {
        EBill bill = new EBill();
        List<EItem> items = Arrays.asList(
                new EItem(EItemType.TASTIERA, "Razer SuperKeys 7000", 449.50),
                new EItem(EItemType.TASTIERA, "Razer SuperKeys 7000", 449.50),
                new EItem(EItemType.TASTIERA, "Razer SuperKeys 7000", 449.50)
        );
        double total = bill.getOrderPriceNoDiscount(items);

        double discount = bill.getBigOrderDiscount(total);

        assertEquals(total * 0.1, discount, 0.0);
    }

    @Test
    public void testSmallOrderNotDiscounted() throws BillException {
        EBill bill = new EBill();
        List<EItem> items = Arrays.asList(
                new EItem(EItemType.TASTIERA, "Tastiera El-Cheapo", 15.50),
                new EItem(EItemType.MOUSE, "Mouse El-Cheapo", 9.90)
        );
        double total = bill.getOrderPriceNoDiscount(items);

        double discount = bill.getBigOrderDiscount(total);

        assertEquals(0.0, discount, 0.0);
    }

    /*
    ======================
    ====== ISSUE #5 ======
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

        double discount = bill.getMouseEqualKeyboardsDiscount(items);
        assertEquals(0, discount, 0.1);
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

        double discount = bill.getMouseEqualKeyboardsDiscount(items);
        assertEquals(14.90, discount, 0.1);
    }


    /*
    ======================
    ====== ISSUE #7 ======
    ======================
    */

    @Test
    public void testNumberOfItems_moreThanThirty () throws BillException {
        exception.expect(BillException.class);

        EBill bill = new EBill();
        List<EItem> items = Collections.nCopies(
                35,
                new EItem(EItemType.TASTIERA, "Razer SuperKeys 7000", 449.50)
        );

        bill.getOrderPriceNoDiscount(items);
    }

    @Test
    public void testNumberOfItems_Thirty () throws BillException {
        EBill bill = new EBill();

        List<EItem> items = Collections.nCopies(
                30,
                new EItem(EItemType.TASTIERA, "Razer SuperKeys 7000", 449.50)
        );

        bill.getOrderPriceNoDiscount(items);
    }


    @Test
    public void testNumberOfItems_LessThanThirty () throws BillException {
        EBill bill = new EBill();

        List<EItem> items = Collections.nCopies(
                15,
                new EItem(EItemType.TASTIERA, "Razer SuperKeys 7000", 449.50)
        );

        bill.getOrderPriceNoDiscount(items);
    }

    @Test
    public void testOrderHasCommission() throws BillException {
        EBill bill = new EBill();

        List<EItem> items = Arrays.asList(
                new EItem(EItemType.MOUSE, "Topolino cheap", 9.90)
        );

        double total = bill.getOrderPriceNoDiscount(items);

        assertEquals(9.90 + 2.0, total, 0.0);
    }

    @Test
    public void testOrderHasNotCommission() throws BillException {
        EBill bill = new EBill();

        List<EItem> items = Arrays.asList(
                new EItem(EItemType.MOUSE, "Topolino cheap", 9.90),
                new EItem(EItemType.MOUSE, "Topolino cheap", 9.90)
        );

        double total = bill.getOrderPriceNoDiscount(items);

        assertEquals(19.80, total, 0.0);
    }
}
