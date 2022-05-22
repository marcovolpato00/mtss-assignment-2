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
import java.time.LocalDate;
import java.time.LocalTime;

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
                LocalDate.of(1000, 12, 25)
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


    /*
    ======================
    ====== ISSUE #9 ======
    ======================
    */

    @Test
    public void testUnderageGifts_lessThanTenUnderAge_AllInTime(){
        EBill bill = new EBill();

        User user1 = new User( 1,"Jesus", LocalDate.of(2005, 12, 25));
        User user2 = new User( 1,"Davide", LocalDate.of(2010, 3, 16));
        User user3 = new User( 1,"Marco", LocalDate.of(2008, 5, 21));
        User user4 = new User( 1,"Marco", LocalDate.of(2000, 1, 1));
        
        assertEquals(true, bill.giftOrder(user1, 0.99, LocalTime.of(18, 20, 15))); 
        assertEquals(false, bill.giftOrder(user2, 0.1, LocalTime.of(18, 10, 15)));
        assertEquals(false, bill.giftOrder(user3, 0.9, LocalTime.of(18, 20, 15)));
        assertEquals(false, bill.giftOrder(user1, 0.99, LocalTime.of(18, 40, 35)));      
        assertEquals(false, bill.giftOrder(user4, 0.92, LocalTime.of(18, 30, 47)));      
        assertEquals(1, bill.giftedOrders);
    }

    @Test
    public void testUnderageGifts_lessThanTenUnderAge_SomeInTime(){
        EBill bill = new EBill();

        User user1 = new User( 1,"Jesus", LocalDate.of(2005, 12, 25));
        User user2 = new User( 1,"Davide", LocalDate.of(2010, 3, 16));
        User user3 = new User( 1,"Marco", LocalDate.of(2008, 5, 21));
        User user4 = new User( 1,"Marco", LocalDate.of(2000, 1, 1));

        assertEquals(true, bill.giftOrder(user1, 0.99, LocalTime.of(18, 20, 15))); 
        assertEquals(false, bill.giftOrder(user2, 0.1, LocalTime.of(18, 10, 15)));
        assertEquals(false, bill.giftOrder(user3, 0.9, LocalTime.of(18, 20, 15)));
        assertEquals(false, bill.giftOrder(user1, 0.99, LocalTime.of(18, 40, 35)));
        assertEquals(false, bill.giftOrder(user3, 0.95, LocalTime.of(19, 20, 15)));
        assertEquals(false, bill.giftOrder(user4, 0.92, LocalTime.of(18, 30, 47)));  
        assertEquals(1, bill.giftedOrders);
    }


    @Test
    public void testUnderageGifts_lessThanTenUnderAge_NoneInTime(){
        EBill bill = new EBill();

        User user1 = new User( 1,"Jesus", LocalDate.of(2005, 12, 25));
        User user2 = new User( 1,"Davide", LocalDate.of(2010, 3, 16));
        User user3 = new User( 1,"Marco", LocalDate.of(2008, 5, 21));
        User user4 = new User( 1,"Marco", LocalDate.of(2000, 1, 1));
        
        assertEquals(false, bill.giftOrder(user1, 0.99, LocalTime.of(20, 20, 15))); 
        assertEquals(false, bill.giftOrder(user2, 0.1, LocalTime.of(20, 10, 15)));
        assertEquals(false, bill.giftOrder(user3, 0.9, LocalTime.of(20, 20, 15)));
        assertEquals(false, bill.giftOrder(user1, 0.99, LocalTime.of(20, 40, 35)));
        assertEquals(false, bill.giftOrder(user3, 0.95, LocalTime.of(19, 20, 15)));
        assertEquals(false, bill.giftOrder(user4, 0.92, LocalTime.of(20, 30, 47)));  
        assertEquals(0, bill.giftedOrders);
    }

    @Test
    public void testUnderageGifts_lessThanTenNoneUnderAge(){
        EBill bill = new EBill();

        User user1 = new User( 1,"Jesus", LocalDate.of(2000, 12, 25));
        User user2 = new User( 1,"Davide", LocalDate.of(2000, 3, 16));
        User user3 = new User( 1,"Marco", LocalDate.of(2000, 5, 21));
        User user4 = new User( 1,"Marco", LocalDate.of(2000, 1, 1));
        
        assertEquals(false, bill.giftOrder(user1, 0.99, LocalTime.of(20, 20, 15))); 
        assertEquals(false, bill.giftOrder(user2, 0.1, LocalTime.of(20, 10, 15)));
        assertEquals(false, bill.giftOrder(user3, 0.9, LocalTime.of(20, 20, 15)));
        assertEquals(false, bill.giftOrder(user1, 0.99, LocalTime.of(20, 40, 35)));
        assertEquals(false, bill.giftOrder(user3, 0.95, LocalTime.of(19, 20, 15)));
        assertEquals(false, bill.giftOrder(user4, 0.92, LocalTime.of(20, 30, 47)));  
        assertEquals(0, bill.giftedOrders);
    }

    @Test
    public void testUnderageGifts_TenUnderAge_AllInTime(){
        EBill bill = new EBill();

        User user1 = new User( 1,"Jesus", LocalDate.of(2010, 12, 25));
        User user2 = new User( 1,"Davide", LocalDate.of(2010, 3, 16));
        User user3 = new User( 1,"Marco", LocalDate.of(2010, 5, 21));
        User user4 = new User( 1,"Marco", LocalDate.of(2010, 1, 1));
        User user5 = new User( 1,"Davide", LocalDate.of(2010, 3, 16));
        User user6 = new User( 1,"Marco", LocalDate.of(2010, 5, 21));
        User user7 = new User( 1,"Marco", LocalDate.of(2010, 1, 1));
        User user8 = new User( 1,"Davide", LocalDate.of(2010, 3, 16));
        User user9 = new User( 1,"Marco", LocalDate.of(2010, 5, 21));
        User user10 = new User( 1,"Marco", LocalDate.of(2010, 1, 1));
        
        assertEquals(true, bill.giftOrder(user1, 0.99, LocalTime.of(18, 18, 15))); 
        assertEquals(false, bill.giftOrder(user2, 0.1, LocalTime.of(18, 10, 15)));
        assertEquals(false, bill.giftOrder(user3, 0.9, LocalTime.of(18, 18, 15)));            
        assertEquals(true, bill.giftOrder(user4, 0.92, LocalTime.of(18, 30, 47)));  
        assertEquals(true, bill.giftOrder(user5, 0.99, LocalTime.of(18, 40, 35)));
        assertEquals(true, bill.giftOrder(user6, 0.95, LocalTime.of(18, 18, 15)));
        assertEquals(true, bill.giftOrder(user7, 0.95, LocalTime.of(18, 18, 15)));
        assertEquals(true, bill.giftOrder(user8, 0.95, LocalTime.of(18, 18, 15)));
        assertEquals(true, bill.giftOrder(user9, 0.95, LocalTime.of(18, 18, 15)));
        assertEquals(true, bill.giftOrder(user10, 0.95, LocalTime.of(18, 18, 15)));
        assertEquals(8, bill.giftedOrders);
    }

    @Test
    public void testUnderageGifts_TenUnderAge_SomeInTime(){
        EBill bill = new EBill();

        User user1 = new User( 1,"Jesus", LocalDate.of(2010, 12, 25));
        User user2 = new User( 1,"Davide", LocalDate.of(2010, 3, 16));
        User user3 = new User( 1,"Marco", LocalDate.of(2010, 5, 21));
        User user4 = new User( 1,"Marco", LocalDate.of(2010, 1, 1));
        User user5 = new User( 1,"Davide", LocalDate.of(2010, 3, 16));
        User user6 = new User( 1,"Marco", LocalDate.of(2010, 5, 21));
        User user7 = new User( 1,"Marco", LocalDate.of(2010, 1, 1));
        User user8 = new User( 1,"Davide", LocalDate.of(2010, 3, 16));
        User user9 = new User( 1,"Marco", LocalDate.of(2010, 5, 21));
        User user10 = new User( 1,"Marco", LocalDate.of(2010, 1, 1));
        
        assertEquals(true, bill.giftOrder(user1, 0.99, LocalTime.of(18, 18, 15))); 
        assertEquals(false, bill.giftOrder(user2, 0.1, LocalTime.of(18, 10, 15)));
        assertEquals(false, bill.giftOrder(user3, 0.9, LocalTime.of(18, 18, 15)));            
        assertEquals(true, bill.giftOrder(user4, 0.92, LocalTime.of(18, 30, 47)));  
        assertEquals(true, bill.giftOrder(user5, 0.99, LocalTime.of(18, 40, 35)));
        assertEquals(false, bill.giftOrder(user6, 0.95, LocalTime.of(19, 19, 15)));
        assertEquals(false, bill.giftOrder(user7, 0.95, LocalTime.of(19, 19, 15)));
        assertEquals(false, bill.giftOrder(user8, 0.95, LocalTime.of(19, 19, 15)));
        assertEquals(false, bill.giftOrder(user9, 0.95, LocalTime.of(19, 19, 15)));
        assertEquals(false, bill.giftOrder(user10, 0.95, LocalTime.of(19, 19, 15)));
        assertEquals(3, bill.giftedOrders);
    }

    @Test
    public void testUnderageGifts_TenUnderAge_NoneInTime(){
        EBill bill = new EBill();

        User user1 = new User( 1,"Jesus", LocalDate.of(2010, 12, 25));
        User user2 = new User( 1,"Davide", LocalDate.of(2010, 3, 16));
        User user3 = new User( 1,"Marco", LocalDate.of(2010, 5, 21));
        User user4 = new User( 1,"Marco", LocalDate.of(2010, 1, 1));
        User user5 = new User( 1,"Davide", LocalDate.of(2010, 3, 16));
        User user6 = new User( 1,"Marco", LocalDate.of(2010, 5, 21));
        User user7 = new User( 1,"Marco", LocalDate.of(2010, 1, 1));
        User user8 = new User( 1,"Davide", LocalDate.of(2010, 3, 16));
        User user9 = new User( 1,"Marco", LocalDate.of(2010, 5, 21));
        User user10 = new User( 1,"Marco", LocalDate.of(2010, 1, 1));
        
        assertEquals(false, bill.giftOrder(user1, 0.99, LocalTime.of(20, 20, 15))); 
        assertEquals(false, bill.giftOrder(user2, 0.1, LocalTime.of(20, 10, 15)));
        assertEquals(false, bill.giftOrder(user3, 0.9, LocalTime.of(20, 20, 15)));            
        assertEquals(false, bill.giftOrder(user4, 0.92, LocalTime.of(20, 30, 47)));  
        assertEquals(false, bill.giftOrder(user5, 0.99, LocalTime.of(20, 40, 35)));
        assertEquals(false, bill.giftOrder(user6, 0.95, LocalTime.of(20, 20, 15)));
        assertEquals(false, bill.giftOrder(user7, 0.95, LocalTime.of(20, 20, 15)));
        assertEquals(false, bill.giftOrder(user8, 0.95, LocalTime.of(20, 20, 15)));
        assertEquals(false, bill.giftOrder(user9, 0.95, LocalTime.of(20, 20, 15)));
        assertEquals(false, bill.giftOrder(user10, 0.95, LocalTime.of(20, 18, 15)));
        assertEquals(0, bill.giftedOrders);
    }

    @Test
    public void testUnderageGifts_TenNoneUnderAge(){
        EBill bill = new EBill();

        User user1 = new User( 1,"Jesus", LocalDate.of(2000, 12, 25));
        User user2 = new User( 1,"Davide", LocalDate.of(2000, 3, 16));
        User user3 = new User( 1,"Marco", LocalDate.of(2000, 5, 21));
        User user4 = new User( 1,"Marco", LocalDate.of(2000, 1, 1));
        User user5 = new User( 1,"Davide", LocalDate.of(2000, 3, 16));
        User user6 = new User( 1,"Marco", LocalDate.of(2000, 5, 21));
        User user7 = new User( 1,"Marco", LocalDate.of(2000, 1, 1));
        User user8 = new User( 1,"Davide", LocalDate.of(2000, 3, 16));
        User user9 = new User( 1,"Marco", LocalDate.of(2000, 5, 21));
        User user10 = new User( 1,"Marco", LocalDate.of(2000, 1, 1));
        
        assertEquals(false, bill.giftOrder(user1, 0.99, LocalTime.of(18, 18, 15))); 
        assertEquals(false, bill.giftOrder(user2, 0.1, LocalTime.of(18, 10, 15)));
        assertEquals(false, bill.giftOrder(user3, 0.9, LocalTime.of(18, 18, 15)));            
        assertEquals(false, bill.giftOrder(user4, 0.92, LocalTime.of(18, 30, 47)));  
        assertEquals(false, bill.giftOrder(user5, 0.99, LocalTime.of(18, 40, 35)));
        assertEquals(false, bill.giftOrder(user6, 0.95, LocalTime.of(20, 20, 15)));
        assertEquals(false, bill.giftOrder(user7, 0.95, LocalTime.of(20, 20, 15)));
        assertEquals(false, bill.giftOrder(user8, 0.95, LocalTime.of(20, 20, 15)));
        assertEquals(false, bill.giftOrder(user9, 0.95, LocalTime.of(20, 20, 15)));
        assertEquals(false, bill.giftOrder(user10, 0.95, LocalTime.of(20, 18, 15)));
        assertEquals(0, bill.giftedOrders);
    }

    @Test
    public void testUnderageGifts_MoreThanTenUnderAge_AllInTime(){
        EBill bill = new EBill();

        User user1 = new User( 1,"Jesus", LocalDate.of(2010, 12, 25));
        User user2 = new User( 1,"Davide", LocalDate.of(2010, 3, 16));
        User user3 = new User( 1,"Marco", LocalDate.of(2010, 5, 21));
        User user4 = new User( 1,"Marco", LocalDate.of(2010, 1, 1));
        User user5 = new User( 1,"Davide", LocalDate.of(2010, 3, 16));
        User user6 = new User( 1,"Marco", LocalDate.of(2010, 5, 21));
        User user7 = new User( 1,"Marco", LocalDate.of(2010, 1, 1));
        User user8 = new User( 1,"Davide", LocalDate.of(2010, 3, 16));
        User user9 = new User( 1,"Marco", LocalDate.of(2010, 5, 21));
        User user10 = new User( 1,"Marco", LocalDate.of(2010, 1, 1));
        User user11 = new User( 1,"Marco", LocalDate.of(2010, 1, 1));
        
        assertEquals(true, bill.giftOrder(user1, 0.99, LocalTime.of(18, 18, 15))); 
        assertEquals(false, bill.giftOrder(user2, 0.1, LocalTime.of(18, 10, 15)));
        assertEquals(false, bill.giftOrder(user3, 0.9, LocalTime.of(18, 18, 15)));            
        assertEquals(true, bill.giftOrder(user4, 0.92, LocalTime.of(18, 30, 47)));  
        assertEquals(true, bill.giftOrder(user5, 0.99, LocalTime.of(18, 40, 35)));
        assertEquals(true, bill.giftOrder(user6, 0.95, LocalTime.of(18, 18, 15)));
        assertEquals(true, bill.giftOrder(user7, 0.95, LocalTime.of(18, 18, 15)));
        assertEquals(true, bill.giftOrder(user8, 0.95, LocalTime.of(18, 18, 15)));
        assertEquals(true, bill.giftOrder(user9, 0.95, LocalTime.of(18, 18, 15)));
        assertEquals(true, bill.giftOrder(user10, 0.95, LocalTime.of(18, 18, 15)));
        assertEquals(true, bill.giftOrder(user11, 0.95, LocalTime.of(18, 18, 15)));
        assertEquals(9, bill.giftedOrders);
    }

    @Test
    public void testUnderageGifts_MoreThanTenUnderAge_SomeInTime(){
        EBill bill = new EBill();

        User user1 = new User( 1,"Jesus", LocalDate.of(2010, 12, 25));
        User user2 = new User( 1,"Davide", LocalDate.of(2010, 3, 16));
        User user3 = new User( 1,"Marco", LocalDate.of(2010, 5, 21));
        User user4 = new User( 1,"Marco", LocalDate.of(2010, 1, 1));
        User user5 = new User( 1,"Davide", LocalDate.of(2010, 3, 16));
        User user6 = new User( 1,"Marco", LocalDate.of(2010, 5, 21));
        User user7 = new User( 1,"Marco", LocalDate.of(2010, 1, 1));
        User user8 = new User( 1,"Davide", LocalDate.of(2010, 3, 16));
        User user9 = new User( 1,"Marco", LocalDate.of(2010, 5, 21));
        User user10 = new User( 1,"Marco", LocalDate.of(2010, 1, 1));
        User user11 = new User( 1,"Marco", LocalDate.of(2010, 1, 1));

        assertEquals(true, bill.giftOrder(user1, 0.99, LocalTime.of(18, 18, 15))); 
        assertEquals(false, bill.giftOrder(user2, 0.1, LocalTime.of(18, 10, 15)));
        assertEquals(false, bill.giftOrder(user3, 0.9, LocalTime.of(18, 18, 15)));            
        assertEquals(true, bill.giftOrder(user4, 0.92, LocalTime.of(18, 30, 47)));  
        assertEquals(true, bill.giftOrder(user5, 0.99, LocalTime.of(18, 40, 35)));
        assertEquals(false, bill.giftOrder(user6, 0.95, LocalTime.of(19, 19, 15)));
        assertEquals(false, bill.giftOrder(user7, 0.95, LocalTime.of(19, 19, 15)));
        assertEquals(false, bill.giftOrder(user8, 0.95, LocalTime.of(19, 19, 15)));
        assertEquals(false, bill.giftOrder(user9, 0.95, LocalTime.of(19, 19, 15)));
        assertEquals(false, bill.giftOrder(user10, 0.95, LocalTime.of(19, 19, 15)));
        assertEquals(true, bill.giftOrder(user11, 0.99, LocalTime.of(18, 40, 35)));
        assertEquals(4, bill.giftedOrders);
    }

    @Test
    public void testUnderageGifts_MoreThanTenUnderAge_NoneInTime(){
        EBill bill = new EBill();

        User user1 = new User( 1,"Jesus", LocalDate.of(2010, 12, 25));
        User user2 = new User( 1,"Davide", LocalDate.of(2010, 3, 16));
        User user3 = new User( 1,"Marco", LocalDate.of(2010, 5, 21));
        User user4 = new User( 1,"Marco", LocalDate.of(2010, 1, 1));
        User user5 = new User( 1,"Davide", LocalDate.of(2010, 3, 16));
        User user6 = new User( 1,"Marco", LocalDate.of(2010, 5, 21));
        User user7 = new User( 1,"Marco", LocalDate.of(2010, 1, 1));
        User user8 = new User( 1,"Davide", LocalDate.of(2010, 3, 16));
        User user9 = new User( 1,"Marco", LocalDate.of(2010, 5, 21));
        User user10 = new User( 1,"Marco", LocalDate.of(2010, 1, 1));
        User user11 = new User( 1,"Marco", LocalDate.of(2010, 1, 1));
        
        assertEquals(false, bill.giftOrder(user1, 0.99, LocalTime.of(20, 20, 15))); 
        assertEquals(false, bill.giftOrder(user2, 0.1, LocalTime.of(20, 10, 15)));
        assertEquals(false, bill.giftOrder(user3, 0.9, LocalTime.of(20, 20, 15)));            
        assertEquals(false, bill.giftOrder(user4, 0.92, LocalTime.of(20, 30, 47)));  
        assertEquals(false, bill.giftOrder(user5, 0.99, LocalTime.of(20, 40, 35)));
        assertEquals(false, bill.giftOrder(user6, 0.95, LocalTime.of(20, 20, 15)));
        assertEquals(false, bill.giftOrder(user7, 0.95, LocalTime.of(20, 20, 15)));
        assertEquals(false, bill.giftOrder(user8, 0.95, LocalTime.of(20, 20, 15)));
        assertEquals(false, bill.giftOrder(user9, 0.95, LocalTime.of(20, 20, 15)));
        assertEquals(false, bill.giftOrder(user10, 0.95, LocalTime.of(20, 18, 15)));
        assertEquals(false, bill.giftOrder(user11, 0.95, LocalTime.of(20, 18, 15)));
        assertEquals(0, bill.giftedOrders);
    }

    @Test
    public void testUnderageGifts_MoreThanTenNoneUnderAge(){
        EBill bill = new EBill();

        User user1 = new User( 1,"Jesus", LocalDate.of(2000, 12, 25));
        User user2 = new User( 1,"Davide", LocalDate.of(2000, 3, 16));
        User user3 = new User( 1,"Marco", LocalDate.of(2000, 5, 21));
        User user4 = new User( 1,"Marco", LocalDate.of(2000, 1, 1));
        User user5 = new User( 1,"Davide", LocalDate.of(2000, 3, 16));
        User user6 = new User( 1,"Marco", LocalDate.of(2000, 5, 21));
        User user7 = new User( 1,"Marco", LocalDate.of(2000, 1, 1));
        User user8 = new User( 1,"Davide", LocalDate.of(2000, 3, 16));
        User user9 = new User( 1,"Marco", LocalDate.of(2000, 5, 21));
        User user10 = new User( 1,"Marco", LocalDate.of(2000, 1, 1));
        User user11 = new User( 1,"Marco", LocalDate.of(2000, 1, 1));
        
        assertEquals(false, bill.giftOrder(user1, 0.99, LocalTime.of(18, 18, 15))); 
        assertEquals(false, bill.giftOrder(user2, 0.1, LocalTime.of(18, 10, 15)));
        assertEquals(false, bill.giftOrder(user3, 0.9, LocalTime.of(18, 18, 15)));            
        assertEquals(false, bill.giftOrder(user4, 0.92, LocalTime.of(18, 30, 47)));  
        assertEquals(false, bill.giftOrder(user5, 0.99, LocalTime.of(18, 40, 35)));
        assertEquals(false, bill.giftOrder(user6, 0.95, LocalTime.of(20, 20, 15)));
        assertEquals(false, bill.giftOrder(user7, 0.95, LocalTime.of(20, 20, 15)));
        assertEquals(false, bill.giftOrder(user8, 0.95, LocalTime.of(20, 20, 15)));
        assertEquals(false, bill.giftOrder(user9, 0.95, LocalTime.of(20, 20, 15)));
        assertEquals(false, bill.giftOrder(user10, 0.95, LocalTime.of(20, 18, 15)));
        assertEquals(false, bill.giftOrder(user11, 0.99, LocalTime.of(18, 40, 35)));
        assertEquals(0, bill.giftedOrders);
    }

    @Test
    public void testUnderageGifts_MoreThanTenAllUnderAge_AllInTime(){
        EBill bill = new EBill();

        User user1 = new User( 1,"Jesus", LocalDate.of(2010, 12, 25));
        User user2 = new User( 1,"Davide", LocalDate.of(2010, 3, 16));
        User user3 = new User( 1,"Marco", LocalDate.of(2010, 5, 21));
        User user4 = new User( 1,"Marco", LocalDate.of(2010, 1, 1));
        User user5 = new User( 1,"Davide", LocalDate.of(2010, 3, 16));
        User user6 = new User( 1,"Marco", LocalDate.of(2010, 5, 21));
        User user7 = new User( 1,"Marco", LocalDate.of(2010, 1, 1));
        User user8 = new User( 1,"Davide", LocalDate.of(2010, 3, 16));
        User user9 = new User( 1,"Marco", LocalDate.of(2010, 5, 21));
        User user10 = new User( 1,"Marco", LocalDate.of(2010, 1, 1));
        User user11 = new User( 1,"Marco", LocalDate.of(2010, 1, 1));
        
        assertEquals(true, bill.giftOrder(user1, 0.99, LocalTime.of(18, 18, 15))); 
        assertEquals(true, bill.giftOrder(user2, 0.91, LocalTime.of(18, 10, 15)));
        assertEquals(true, bill.giftOrder(user3, 0.92, LocalTime.of(18, 18, 15)));            
        assertEquals(true, bill.giftOrder(user4, 0.92, LocalTime.of(18, 30, 47)));  
        assertEquals(true, bill.giftOrder(user5, 0.99, LocalTime.of(18, 40, 35)));
        assertEquals(true, bill.giftOrder(user6, 0.95, LocalTime.of(18, 18, 15)));
        assertEquals(true, bill.giftOrder(user7, 0.95, LocalTime.of(18, 18, 15)));
        assertEquals(true, bill.giftOrder(user8, 0.95, LocalTime.of(18, 18, 15)));
        assertEquals(true, bill.giftOrder(user9, 0.95, LocalTime.of(18, 18, 15)));
        assertEquals(true, bill.giftOrder(user10, 0.95, LocalTime.of(18, 18, 15)));
        assertEquals(false, bill.giftOrder(user11, 0.99, LocalTime.of(18, 40, 35)));
        assertEquals(10, bill.giftedOrders);    
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
