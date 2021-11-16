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

/*
author: Samuel Taylor
FUTURE ENHANCEMENT:
A possible future enhancement that could be added to the program would be the ability
to use the part/product data in the program to perform calculations to find new metrics
like inventory trends, price trends most/least used part or product, etc.  It would be fun
to return to this project and try to adapt it to work in this manner.  Another possible enhancement
could be to add the functionality to export the data to another program like an accounting
or excel spreadsheet.
*/

/*
JavaDoc folder is located in the C482-SamuelTaylor file.
EXCEPTION paragraph can be found in the Inventory model class description
 */




/**
 An Inventory Management Program for viewing, adding and removing parts and products.
 Also includes the ability to associate 1 or more parts with a product while
 also being able to add or remove these associated parts.
 */
public class Main extends Application
{
    /**
     * The start method which is called to create the stage and load the first scene.
     *
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        Parent root = (Parent)FXMLLoader.load(getClass().getResource("../view/MainScreen.fxml"));
        primaryStage.setTitle("Inventory Management System");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
    /**
     * The main method which creates sample data for the program and launches the program.
     *
     * @param args
     *
     */

    public static void main(String[] args) {
        int partId = Inventory.getNewPartId();
        int productId = Inventory.getNewProductId();

        /*
        Creates sample data for the part method
         */
        InHouse muffler = new InHouse(partId, "OEM Muffler", 180.18,
                12, 1, 35, 1002);
        partId = Inventory.getNewPartId();
        InHouse radiator = new InHouse(partId, "OEM Radiator", 99.99,
                6, 1, 35, 1002);
        partId = Inventory.getNewPartId();
        InHouse transmission = new InHouse(partId, "OEM Transmission", 354.03,
                2, 1, 10, 1002);
        partId = Inventory.getNewPartId();
        Outsourced oilPan = new Outsourced(partId, "ODM Oil Pan", 34.10,
                8, 1, 15, "Cheap Parts LLC");
        partId = Inventory.getNewPartId();
        Outsourced alternator = new Outsourced(partId, "ODM Alternator", 15.99,
                65, 1, 100, "Actual Junk Inc.");

        /*
         Passes created parts to the addPart method to be added to the allParts list
         */
        Inventory.addPart(muffler);
        Inventory.addPart(radiator);
        Inventory.addPart(transmission);
        Inventory.addPart(oilPan);
        Inventory.addPart(alternator);

        /*
         Creates sample data for Product and associates parts with the new product
         then adds the product to the allProducts list.
         */
        Product cheapCarOne = new Product(productId, "Junker Deluxe 2", 3600.99, 3,
                1, 10);
        cheapCarOne.addAssociatedPart(muffler);
        cheapCarOne.addAssociatedPart(radiator);
        cheapCarOne.addAssociatedPart(oilPan);
        cheapCarOne.addAssociatedPart(alternator);
        Inventory.addProduct(cheapCarOne);

        productId = Inventory.getNewProductId();
        Product cheapCarAllOEM = new Product(productId, "Junker Original", 1999.99, 8,
                1, 100);
        cheapCarAllOEM.addAssociatedPart(muffler);
        cheapCarAllOEM.addAssociatedPart(radiator);
        cheapCarAllOEM.addAssociatedPart(transmission);
        Inventory.addProduct(cheapCarAllOEM);

        launch(args);
    }

}















    /**public static void main(String[] args)
    {
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
}*/