package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/*
* EXCEPTION
* Rather than describe a specific exception I encountered, I wanted to talk about logic errors
* and unnecessary code I was having trouble with.  For the inventory model and for the program in general,
* I thought I needed different methods to perform each task, especially when it came to searching
* the lists for a part id number or part name. I spent around two days experimenting with different
* implementation methods until I saw a post about using the .contains function with a list and that
* it could be made to work for both an integer and a string.  Over the time I spent working on this
* project i've been able to trim so many unnecessary things from it and learned how to do more with less.  */

/**
 * Model responsible for assigning Part and Product id's.
 *
 * This class returns an incremented id number for parts and products,
 * also has Accessors and Mutators for the allParts and allProducts lists.
 *
 * @author Samuel Taylor
 */
public class Inventory
{

    private static int partId = 0; // primitive that is incremented for part Id's
    private static int productId = 0; // primitive that is incremented for product Id's
    private static ObservableList<Part> allParts = FXCollections.observableArrayList(); // allParts list to contain all Parts
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList(); // allProducts list to contain all Products

    /**
     * Accessor for allParts list
     * @return allParts
     */
    public static ObservableList<Part> getAllParts()
    {
        return allParts;
    }

    /**
     * Accessor for allProducts list
     * @return allProducts
     */
    public static ObservableList<Product> getAllProducts()
    {
        return allProducts;
    }

    /**
     * Adds a new part to the allParts list
     * @param newPart the part to be added
     */
    public static void addPart(Part newPart)
    {
        allParts.add(newPart);
    }

    /**
     * Adds a new product to the allProducts list
     * @param newProduct the product to be added
     */
    public static void addProduct(Product newProduct)
    {
        allProducts.add(newProduct);
    }

    /**
     * Returns an incremented partId
     * @return partId
     */
    public static int getNewPartId()
    {
        return ++partId;
    }

    /**
     * Returns an incremented productId
     * @return productId
     */
    public static int getNewProductId()
    {
        return ++productId;
    }

    /**
     * Method that removes a part from allParts list
     * @param selectedPart the part to be removed
     */
    public static void deletePart(Part selectedPart)
    {
        if (allParts.contains(selectedPart))
        {
            allParts.remove(selectedPart);
        }
    }

    /**
     * Method that removes a product from allProducts list
     * @param selectedProduct The product to be removed
     */
    public static void deleteProduct(Product selectedProduct)
    {
        if (allProducts.contains(selectedProduct))
        {
            allProducts.remove(selectedProduct);
        }
    }
}
