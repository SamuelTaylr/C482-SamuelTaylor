package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Class that creates a Product object that includes
 * a list to contain associated parts
 *
 * @author Samuel Taylor
 *
 */
public class Product
{
    private int id; // primitive for product Id number
    private String name; // product name variable
    private double price; // product price variable
    private int stock; // product inventory level variable
    private int min; // product minimum inv. level variable
    private int max; // product maximum inv. level variable
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList(); // a list of associated parts for product object

    /**
     * Constructor a new instance of the Product object
     *
     * @param id holds product id
     * @param name holds product name
     * @param price holds product price
     * @param stock holds inventory level for product
     * @param min holds minimum inventory level for product
     * @param max holds maximum inventory level for product
     */
    public Product(int id, String name, double price, int stock, int min, int max)
    {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /**
     * Accessor for product Id
     * @return the id for the product
     */
    public int getId()
    {
        return this.id;
    }

    /**
     * Mutator for product Id
     * @param id the product id for this product
     */
    public void setId(int id)
    {
        this.id = id;
    }

    /**
     * Accessor for product Name
     * @return the product name
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Mutator for product Name
     * @param name the name of this product
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Accessor for product Price
     * @return the price the product
     */
    public double getPrice()
    {
        return this.price;
    }

    /**
     * Mutator for product Price
     * @param price the price for this product
     */
    public void setPrice(double price)
    {
        this.price = price;
    }

    /**
     * Accessor for product Inventory level
     * @return the inventory level for the product
     */
    public int getStock()
    {
        return this.stock;
    }

    /**
     * Mutator for product Inventory level
     * @param stock the inventory level for this product
     */
    public void setStock(int stock)
    {
        this.stock = stock;
    }

    /**
     * Accessor for product Minimum inventory
     * @return the minimum inventory level for the product
     */
    public int getMin()
    {
        return this.min;
    }

    /**
     * Mutator for product Minimum inventory
     * @param min the minimum inventory for this product
     */
    public void setMin(int min)
    {
        this.min = min;
    }

    /**
     * Accessor for product Maximum inventory
     * @return the maximum inventory for the product
     */
    public int getMax()
    {
        return this.max;
    }

    /**
     * Mutator for product Maximum inventory
     * @param max the maximum inventory for this product
     */
    public void setMax(int max)
    {
        this.max = max;
    }

    /**
     * Adds a part to list of associated parts
     * @param part the part to be added to list
     */
    public void addAssociatedPart(Part part)
    {
        this.associatedParts.add(part);
    }

    /*
     * Previously removed a part from the list of associated parts
     * but I kept having issues getting it work as intended and instead
     * found it easier to use the built in functions of the list

     public boolean deleteAssociatedPart(Part selectedAssociatedPart)
     {
        if (this.associatedParts.contains(selectedAssociatedPart))
        {
            this.associatedParts.remove(selectedAssociatedPart);
            return true;
        }
        else
        {
            return false;
        }
    }
    */

    /**
     * Accessor for the list of associated parts for this product
     * @return associatedParts the list of associated parts
     */
    public ObservableList<Part> getAllAssociatedParts()
    {
        return this.associatedParts;
    }
}
