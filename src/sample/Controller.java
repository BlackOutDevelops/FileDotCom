package sample;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javafx.event.ActionEvent;

import javax.swing.text.View;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Controller {

    Main main = new Main();
    int count = 1;
    @FXML
    public Button ProcessItem;
    @FXML
    public Button ConfirmItem;
    @FXML
    public Button ViewOrder;
    @FXML
    public Button FinishOrder;
    @FXML
    public Button NewOrder;
    @FXML
    public Button Exit;
    @FXML
    public Label ItemID;
    @FXML
    public Label Quantity;
    @FXML
    public Label Details;
    @FXML
    public Label OrderSubtotal;
    @FXML
    public TextField UserItemID;
    @FXML
    public TextField UserQuantity;
    @FXML
    public TextField UserDetails;
    @FXML
    public TextField UserSubtotal;

    @FXML
    private void increment(ActionEvent e) {
        count++;

        ProcessItem.setText("Process Item #" + count);
        ConfirmItem.setText("Confirm Item #" + count);
        ItemID.setText("Enter item ID for Item #" + count + ":");
        Quantity.setText("Enter quantity for Item #" + count + ":");
        Details.setText("Details for Item #" + (count-1) + ":");
        OrderSubtotal.setText("Order subtotal for " + (count-1) + " item(s):");
    }

    @FXML
    private void resetCount(ActionEvent e) {
        count = 1;

        ProcessItem.setText("Process Item #" + count);
        ConfirmItem.setText("Confirm Item #" + count);
        ItemID.setText("Enter item ID for Item #" + count + ":");
        Quantity.setText("Enter quantity for Item #" + count + ":");
        Details.setText("Details for Item #" + count + ":");
        OrderSubtotal.setText("Order subtotal for " + count + " item(s):");
    }

    @FXML
    private void process(ActionEvent e) {
        Item itemInInventory = main.getItems(UserItemID.getText().trim());
        double percentage = 0, discountedPrice;
        int quantity;

        if (itemInInventory != null && !(UserQuantity.getText().trim().equals(""))) {
            quantity = Integer.parseInt(UserQuantity.getText().trim());
            if (quantity > 0 && quantity < 5) {
                percentage = 0;
            } else if (quantity >= 5 && quantity < 10) {
                percentage = 10;
            } else if (quantity >= 10 && quantity < 15) {
                percentage = 15;
            } else if (quantity >= 15) {
                percentage = 20;
            }

            discountedPrice = (itemInInventory.price * (1 - (percentage / 100))) * quantity;

            Details.setText("Details for Item #" + count + ":");

            if (itemInInventory.inStock) {
                UserDetails.setText(itemInInventory.id + " " + itemInInventory.itemName + " $" +
                        String.format("%.2f", itemInInventory.price) + " " + UserQuantity.getText().trim() + " " + String.format("%.0f", percentage) + "% $" + String.format("%.2f", discountedPrice));
            }
        }
        else {
        }

        if (UserItemID.getText().trim().equals("") && UserQuantity.getText().trim().equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Nile Dot Com - ERROR");
            alert.setContentText("Please enter an Item ID and quantity!");
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    System.out.println("Pressed Ok.");
                }
            });
        }
        else if (UserItemID.getText().trim().equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Nile Dot Com - ERROR");
            alert.setContentText("Please enter an Item ID!");
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    System.out.println("Pressed Ok.");
                }
            });
        }
        else if (UserQuantity.getText().trim().equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Nile Dot Com - ERROR");
            alert.setContentText("Please enter a quantity!");
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    System.out.println("Pressed Ok.");
                }
            });
        }
        else if (itemInInventory == null) {
            ProcessItem.setDisable(true);
            ConfirmItem.setDisable(false);
            UserItemID.setEditable(false);
            UserQuantity.setEditable(false);
        }
        else if (!(itemInInventory.inStock)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Nile Dot Com - ERROR");
            alert.setContentText("Sorry... that item is out of stock, please try another item");
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    System.out.println("Pressed OK.");
                }
            });
        }
        else {
            ProcessItem.setDisable(true);
            ConfirmItem.setDisable(false);
            UserItemID.setEditable(false);
            UserQuantity.setEditable(false);
        }
    }

    @FXML
    private void confirm(ActionEvent e) {
        Item itemInDetails = main.getItems(UserItemID.getText().trim());

        if (UserDetails.getText().equals("") || itemInDetails == null || !itemInDetails.inStock) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Nile Dot Com - ERROR");
            alert.setContentText("Item ID " + UserItemID.getText() + " not in file");
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    System.out.println("Pressed OK1.");
                    UserItemID.clear();
                    UserQuantity.clear();
                    ProcessItem.setDisable(false);
                    ConfirmItem.setDisable(true);
                    UserItemID.setEditable(true);
                    UserQuantity.setEditable(true);
                }
            });
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Nile Dot Com - Item Confirmed");
            alert.setContentText("Item #" + count + " accepted. Added to your cart.");
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    System.out.println("Pressed OK2.");

                    if (count == 1) {
                        main.createCart();
                    }

                    main.addToCart(UserDetails.getText());
                    UserItemID.clear();
                    UserQuantity.clear();
                    ProcessItem.setDisable(false);
                    ConfirmItem.setDisable(true);
                    ViewOrder.setDisable(false);
                    FinishOrder.setDisable(false);
                    UserItemID.setEditable(true);
                    UserQuantity.setEditable(true);
                    UserSubtotal.setText("$" + String.format("%.2f", main.cart.orderSubtotal));
                    increment(e);
                }
                if (rs == ButtonType.CANCEL) {
                    System.out.println("Pressed Cancel.");
                }
            });
        }
    }

    @FXML
    private void viewOrder(ActionEvent e) {
        int count = 1;
        String buffer = "";
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Nile Dot Com - Current Shopping Cart Status");
        for (String itemString: main.cart.cartItemsString) {
            buffer += count + ". " + itemString + "\n";
            count++;
        }
        alert.setContentText(buffer);
        alert.getDialogPane().getButtonTypes().add(ButtonType.OK);
        alert.show();
    }

    @FXML
    private void finishOrder(ActionEvent e) throws IOException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yy hh:mm:ss a z");
        ZonedDateTime now = ZonedDateTime.now();
        int count = 1;
        double taxAmount;
        String buffer = "Date: " + dtf.format(now) + "\n\nNumber of line items: " + main.cart.numberOfLines + "\n\n";
        buffer += "Item# / ID / Title / Price / Qty / Disc % / Subtotal:\n\n";
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Nile Dot Com - Final Invoice");
        for (String itemString: main.cart.cartItemsString) {
            buffer += count + ". " + itemString + "\n";
            count++;
        }
        taxAmount = (main.cart.taxRate / 100) * main.cart.orderSubtotal;
        buffer += "\n\n\n" + "Order subtotal:   $" + String.format("%.2f" ,main.cart.orderSubtotal);
        buffer += "\n\n" + "Tax rate:          " + String.format("%.0f" ,main.cart.taxRate) + "%";
        buffer += "\n\n" + "Tax amount:       $" + String.format("%.2f", taxAmount);
        buffer += "\n\n" + "Order total:      $" + String.format("%.2f", taxAmount + main.cart.orderSubtotal);
        buffer += "\n\nThanks for shopping at Nile Dot Com!";
        alert.setContentText(buffer);
        alert.getDialogPane().getButtonTypes().add(ButtonType.OK);
        alert.show();
        main.createInvoice();
        ProcessItem.setDisable(true);
        FinishOrder.setDisable(true);
        UserItemID.setEditable(false);
        UserQuantity.setEditable(false);
    }

    @FXML
    private void newOrder(ActionEvent e) {
        if (main.cart != null) {
            if (main.cart.itemsInCart != null) {
                main.cart.itemsInCart.removeAll(main.cart.itemsInCart);
            }
            if (main.cart.cartItemsString != null) {
                main.cart.cartItemsString.removeAll(main.cart.cartItemsString);
            }
            if (main.cart.finalInvoiceString != null) {
                main.cart.finalInvoiceString.removeAll(main.cart.finalInvoiceString);
            }
            main.cart.date = "";
            main.cart.numberOfLines = 0;
            main.cart.taxRate = 6;
            main.cart.orderSubtotal = 0;
        }
        UserItemID.clear();
        UserQuantity.clear();
        UserDetails.clear();
        UserSubtotal.clear();
        ProcessItem.setDisable(false);
        NewOrder.setDisable(false);
        ConfirmItem.setDisable(true);
        ViewOrder.setDisable(true);
        FinishOrder.setDisable(true);
        UserItemID.setEditable(true);
        UserQuantity.setEditable(true);
        resetCount(e);
    }

    @FXML
    private void exit(ActionEvent e) {
        Platform.exit();
    }
}
