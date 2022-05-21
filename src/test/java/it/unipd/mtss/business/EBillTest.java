////////////////////////////////////////////////////////////////////
// DAVIDE MILAN 1216733
// MARCO VOLPATO 1224826
////////////////////////////////////////////////////////////////////

package it.unipd.mtss.business;

import it.unipd.mtss.business.exception.BillException;
import it.unipd.mtss.model.EItem;
import it.unipd.mtss.model.EItemType;
import it.unipd.mtss.model.User;
import org.junit.Test;

import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class EBillTest {
    @Test
    public void checkTotalNoDiscounts() throws BillException {
        EBill bill = new EBill();
        List<EItem> items = Arrays.asList(
                new EItem(EItemType.PROCESSORE, "Intel i5 6666k", 199.90),
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

        assertEquals(199.90 + 98.50 + 20.10 + 449.50, total, 0.1);
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
        assertEquals(ass, total, 0.0);
    }
}
