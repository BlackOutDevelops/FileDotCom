package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.w3c.dom.DOMStringList;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class Cart {
    ObservableList<Item> itemsInCart = FXCollections.observableArrayList();
    String date;
    ArrayList<String> cartItemsString, finalInvoiceString;
    int numberOfLines;
    double taxRate, orderSubtotal;

    public Cart() {
        this.itemsInCart = FXCollections.observableArrayList();
        this.cartItemsString = new ArrayList<String>();
        this.finalInvoiceString = new ArrayList<String>();
        this.date = "";
        this.numberOfLines = 0;
        this.taxRate = 6;
        this.orderSubtotal = 0;
    }
}
