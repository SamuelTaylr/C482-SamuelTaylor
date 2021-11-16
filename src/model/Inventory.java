package model;

import java.util.Iterator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Inventory {
    private static int partId = 0;
    private static int productId = 0;
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    public Inventory() {
    }

    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }

    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }

    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }

    public static int getNewPartId() {
        return ++partId;
    }

    public static int getNewProductId() {
        return ++productId;
    }

    public static Part lookupPart(int partId) {
        Part partFound = null;
        Iterator var2 = allParts.iterator();

        while(var2.hasNext()) {
            Part part = (Part)var2.next();
            if (part.getId() == partId) {
                partFound = part;
            }
        }

        return partFound;
    }

    public static ObservableList<Part> lookupPart(String partName) {
        ObservableList<Part> partsFound = FXCollections.observableArrayList();
        Iterator var2 = allParts.iterator();

        while(var2.hasNext()) {
            Part part = (Part)var2.next();
            if (part.getName().equals(partName)) {
                partsFound.add(part);
            }
        }

        return partsFound;
    }

    public static Product lookupProduct(int productId) {
        Product productFound = null;
        Iterator var2 = allProducts.iterator();

        while(var2.hasNext()) {
            Product product = (Product)var2.next();
            if (product.getId() == productId) {
                productFound = product;
            }
        }

        return productFound;
    }

    public static ObservableList<Product> lookupProduct(String productName) {
        ObservableList<Product> productsFound = FXCollections.observableArrayList();
        Iterator var2 = allProducts.iterator();

        while(var2.hasNext()) {
            Product product = (Product)var2.next();
            if (product.getName().equals(productName)) {
                productsFound.add(product);
            }
        }

        return productsFound;
    }

    public static void updatePart(int index, Part selectedPart) {
        allParts.set(index, selectedPart);
    }

    public static void updateProduct(int index, Product selectedProduct) {
        allProducts.set(index, selectedProduct);
    }

    public static boolean deletePart(Part selectedPart) {
        if (allParts.contains(selectedPart)) {
            allParts.remove(selectedPart);
            return true;
        } else {
            return false;
        }
    }

    public static boolean deleteProduct(Product selectedProduct) {
        if (allProducts.contains(selectedProduct)) {
            allProducts.remove(selectedProduct);
            return true;
        } else {
            return false;
        }
    }
}
