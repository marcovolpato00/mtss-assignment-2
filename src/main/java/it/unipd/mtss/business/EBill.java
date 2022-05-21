////////////////////////////////////////////////////////////////////
// DAVIDE MILAN 1216733
// MARCO VOLPATO 1224826
////////////////////////////////////////////////////////////////////

package it.unipd.mtss.business;

import java.util.List;
import java.util.stream.Stream;

import it.unipd.mtss.business.exception.BillException;
import it.unipd.mtss.model.EItem;
import it.unipd.mtss.model.EItemType;
import it.unipd.mtss.model.User;

public class EBill implements Bill {
    public Stream<EItem> filterByItemType(List<EItem> items, EItemType type) {
        return items.stream().filter(item -> item.getItemType() == type);
    }

    public double getCPUDiscount(List<EItem> itemsOrdered) {
        long cpuCount = filterByItemType(
                itemsOrdered,
                EItemType.PROCESSORE
        ).count();

        if (cpuCount > 5) {
            EItem cheapest = filterByItemType(
                    itemsOrdered,
                    EItemType.PROCESSORE
            ).reduce((a, b) -> a.getPrice() < b.getPrice() ? a : b).get();

            return cheapest.getPrice() / 2;
        }

        return 0.0;
    }

    public double getMouseEqualKeyboardsDiscount(List<EItem> itemsOrdered) {
        long miceCount = filterByItemType(
                itemsOrdered,
                EItemType.MOUSE
        ).count();
        long keyboardsCount = filterByItemType(
                itemsOrdered,
                EItemType.TASTIERA
        ).count();

        if (miceCount == keyboardsCount) {
            EItem cheapest = itemsOrdered
                    .stream()
                    .filter(item -> (item.getItemType() == EItemType.MOUSE || item.getItemType() == EItemType.TASTIERA))
                    .reduce((a, b) -> a.getPrice() < b.getPrice() ? a : b)
                    .get();

            return cheapest.getPrice();
        }
        return 0.0;
    }

    public void checkThirtyItemsOrder(List<EItem> itemsOrdered) throws BillException {
        if (itemsOrdered.size() > 30) {
            throw new BillException("Can't process a order with items >30");
        }
    }

    public double getMiceGift(List<EItem> itemsOrdered) {
        // check if there are at least 10 mice
        long miceCount = filterByItemType(
                itemsOrdered,
                EItemType.MOUSE
        ).count();
        if (miceCount >= 10) {
            // find the cheapest and return its price
            EItem cheapest = filterByItemType(
                    itemsOrdered,
                    EItemType.MOUSE
            ).reduce((a, b) -> a.getPrice() < b.getPrice() ? a : b).get();
            return cheapest.getPrice();
        }
        return 0.0;
    }

    public double getBigOrderDiscount(double orderTotal) {
        if (orderTotal >= 1000.0) {
            return orderTotal * 0.1;
        }
        return 0.0;
    }

    @Override
    public double getOrderPrice(List<EItem> itemsOrdered, User user)
            throws BillException {
        if (itemsOrdered.isEmpty()) {
            throw new BillException("Items list can't be empty");
        }

        checkThirtyItemsOrder(itemsOrdered);

        double total = itemsOrdered.stream().mapToDouble(EItem::getPrice).sum();

        double totalDiscount = 0.0;

        totalDiscount += getMiceGift(itemsOrdered);
        totalDiscount += getCPUDiscount(itemsOrdered);
        totalDiscount += getBigOrderDiscount(total);
        totalDiscount += getMouseEqualKeyboardsDiscount(itemsOrdered);

        total -= totalDiscount;

        return total;
    }

}