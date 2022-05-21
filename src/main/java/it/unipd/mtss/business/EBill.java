////////////////////////////////////////////////////////////////////
// DAVIDE MILAN 1216733
// MARCO VOLPATO 1224826
////////////////////////////////////////////////////////////////////

package it.unipd.mtss.business;

import java.util.List;
import it.unipd.mtss.business.exception.BillException;
import it.unipd.mtss.model.EItem;
import it.unipd.mtss.model.EItemType;
import it.unipd.mtss.model.User;

public class EBill implements Bill {

    /*
    ======================
    ====== ISSUE #2 ======
    ======================
    */
    public double getCPUDiscount(List<EItem> itemsOrdered){
        double discountCPU = 0;
        long cpuCount = itemsOrdered
                .stream()
                .filter(item -> item.getItemType() == EItemType.PROCESSORE)
                .count();

        if(cpuCount > 5){
            EItem cheapest = itemsOrdered
                    .stream()
                    .filter(item -> item.getItemType() == EItemType.PROCESSORE)
                    .reduce((a, b) -> a.getPrice() < b.getPrice() ? a : b)
                    .get();
            discountCPU = (cheapest.getPrice() / 2);
            System.out.println(discountCPU);
        }
        return discountCPU;
    }


    /*
        ======================
        ==== COSTO TOTALE ====
        ======================
    */

    @Override
    public double getOrderPrice(List<EItem> itemsOrdered, User user)
            throws BillException {
        double total = itemsOrdered.stream().mapToDouble(EItem::getPrice).sum();
        double discountCPU = getCPUDiscount(itemsOrdered);
        return total - discountCPU;
    }

}