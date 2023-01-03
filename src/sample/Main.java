package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class Main extends Application {
    File inventoryFile = new File("src/sample/inventory.txt");
    public Inventory inventory;
    public Cart cart;

    {
        try {
            inventory = new Inventory(inventoryFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("NileDotCom");
        primaryStage.setScene(new Scene(root, 800, 275));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    // My logic
    public Item getItems(String id) {
        for (Item item: inventory.totalItems) {
            if (id.equals(item.id)) {
                return item;
            }
        }
        return null;
    }

    public void createCart() {
        cart = new Cart();
    }

    public void addToCart(String itemString) {
        DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("ddMMyyyyhhmm");
        DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("dd/MM/yy hh:mm:ss a z");
        ZonedDateTime now = ZonedDateTime.now();
        String invoiceString;
        String[] splitItemString = itemString.split(" ");
        double discountedPrice = Float.parseFloat(splitItemString[splitItemString.length - 1].replaceAll("[$]?", ""));

        cart.numberOfLines++;
        cart.orderSubtotal += discountedPrice;
        Item item = getItems(splitItemString[0]);
        cart.cartItemsString.add(itemString);
        cart.itemsInCart.add(item);
        invoiceString = dtf1.format(now) + ", " + itemString + ", " + dtf2.format(now); // Need to fix formatting on string
        cart.finalInvoiceString.add(invoiceString);
    }

    public void createInvoice() throws IOException {
        FileWriter fw = new FileWriter(new File("src/sample/transactions.txt"));
        for (String invoiceItem: cart.finalInvoiceString) {
            fw.write(invoiceItem);
            fw.write("\n");
        }
        fw.close();
    }
}
