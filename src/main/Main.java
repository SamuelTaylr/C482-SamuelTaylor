package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import model.Product;

public class Main extends Application {
    public Main() {
    }

    public void start(Stage primaryStage) throws Exception {
        Parent root = (Parent)FXMLLoader.load(this.getClass().getResource("../view/MainScreen.fxml"));
        primaryStage.setTitle("Inventory Management System");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        int partId = Inventory.getNewPartId();
        InHouse tvScreen = new InHouse(partId, "TV Screen", 300.0D, 5, 1, 20, 101);
        partId = Inventory.getNewPartId();
        InHouse tvShell = new InHouse(partId, "TV Shell", 100.0D, 5, 1, 20, 101);
        partId = Inventory.getNewPartId();
        InHouse powerCord = new InHouse(partId, "Power Cord", 2.99D, 5, 1, 20, 101);
        partId = Inventory.getNewPartId();
        Outsourced remote = new Outsourced(partId, "Remote Control", 29.99D, 50, 30, 100, "Panasonic");
        Inventory.addPart(tvScreen);
        Inventory.addPart(tvShell);
        Inventory.addPart(powerCord);
        Inventory.addPart(remote);
        int productId = Inventory.getNewProductId();
        Product television = new Product(productId, "LCD Television", 499.99D, 20, 20, 100);
        television.addAssociatedPart(tvScreen);
        television.addAssociatedPart(tvShell);
        television.addAssociatedPart(powerCord);
        television.addAssociatedPart(remote);
        Inventory.addProduct(television);
        launch(args);
    }
}