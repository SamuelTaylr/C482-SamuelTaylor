package model;

/**
 * Overrides abstract Part class for "InHouse" parts.
 *
 * @author Samuel Taylor
 */
public class InHouse extends Part
{
    /**
     Declares int to hold machine Id variable
     */
    private int machineId;

    /**
     * Constructs an InHouse instance of a Part object
     * @param id the part id number
     * @param name the name of the part
     * @param price the price of the part
     * @param stock the inventory level of the part
     * @param min the minimum inventory level of the part
     * @param max the maximum inventory level for the part
     * @param machineId the variable for the in house part machine id
     */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId)
    {
        super(id, name, price, stock, min, max);
        setMachineId(machineId);
    }

    /**
     * Accessor for machineId variable
     * @return machineId
     * */
    public int getMachineId()
    {
        return machineId;
    }

    /**
     * Mutator for the machineId variable
     * @param machineId in house part machine id number.
     */
    public void setMachineId(int machineId)
    {
        this.machineId = machineId;
    }
}
