package model;

/**
 * Overrides abstract Part class for "Outsourced" parts.
 *
 * @author Samuel Taylor
 */
public class Outsourced extends Part
{
    /**
     * Declares String to hold companyName variable
     */
    private String companyName;

    /**
     * Constructs an Outsourced instance of a Part object
     * @param id the part id number
     * @param name the name of the part
     * @param price the price of the part
     * @param stock the inventory level of the part
     * @param min the minimum inventory level of the part
     * @param max the maximum inventory level for the part
     * @param companyName the variable for the outsourced part company name
     */
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName)
    {
        super(id, name, price, stock, min, max);
        setCompanyName(companyName);
    }

    /**
     * Accessor for machineId variable
     * @return companyName the name of the company for the outsourced part
     * */
    public String getCompanyName()
    {
        return this.companyName;
    }

    /**
     * Mutator for the machineId variable
     * @param companyName the name of the company for the outsourced part
     */
    public void setCompanyName(String companyName)
    {
        this.companyName = companyName;
    }
}
