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
        if (itemsOrdered.isEmpty()) {
            throw new BillException();
        }

        double total = itemsOrdered.stream().mapToDouble(EItem::getPrice).sum();

        // check if there are at least 10 mice
        long miceCount = itemsOrdered
                .stream()
                .filter(item -> item.getItemType() == EItemType.MOUSE)
                .count();
        if (miceCount >= 10) {
            // find the cheapest and subtract from total
            EItem cheapest = itemsOrdered
                    .stream()
                    .filter(item -> item.getItemType() == EItemType.MOUSE)
                    .reduce((a, b) -> a.getPrice() < b.getPrice() ? a : b)
                    .get();
            total -= cheapest.getPrice();
        }
        double discountCPU = getCPUDiscount(itemsOrdered);
        total -= discountCPU;

        if (total >= 1000.0) {
            total -= total * 0.1;
        }

        return total;
    }

}