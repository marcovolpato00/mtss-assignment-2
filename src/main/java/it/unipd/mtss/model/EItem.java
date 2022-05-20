////////////////////////////////////////////////////////////////////
// DAVIDE MILAN 1216733
// MARCO VOLPATO 1224826
////////////////////////////////////////////////////////////////////

package it.unipd.mtss.model;

public class EItem {
    private final EItemType itemType;
    private final String name;
    private final double price;

    public EItem(EItemType itemType, String name, double price) {
        this.itemType = itemType;
        this.name = name;
        this.price = price;
    }

    public EItemType getItemType() {
        return this.itemType;
    }

    public String getName() {
        return this.name;
    }

    public double getPrice() {
        return this.price;
    }
}