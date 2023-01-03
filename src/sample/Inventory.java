package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;

public class Inventory {
    ObservableList<sample.Item> totalItems = FXCollections.observableArrayList();

    public Inventory(File txtFile) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(txtFile));
        String stringFromFile;

        while((stringFromFile = br.readLine()) != null) {
            String[] splitLine = stringFromFile.split(", ");

            totalItems.add(new Item(splitLine[0], splitLine[1], splitLine[2].equals("true"), Float.parseFloat(splitLine[3])));
        }

        for (Item item: totalItems) {
            System.out.println("Item id: " + item.id + "\nItem Name: " + item.itemName + "\nItem in stock: " + (item.inStock ? "True" : "False") + "\nItem price: " + item.price + "\n");
        }
    }
}
