package sample;

import javafx.beans.InvalidationListener;
import javafx.collections.ArrayChangeListener;
import javafx.collections.ObservableArray;

public class Item {
    public String id;
    public String itemName;
    public boolean inStock;
    public double price;

    public Item(String id, String itemName, boolean inStock, float price) {
        this.id = id;
        this.itemName = itemName;
        this.inStock = inStock;
        this.price = price;
    }

    // At most 10 items per order
}
