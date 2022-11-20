package model;

/**
 * A class that represents an {@link InvoiceLine} object used to describe one item in an invoice.
 *
 *
 * An invoice item represented using the {@link InvoiceLine} object is represented by the <code>id</code>, which is the
 * id of the owning {@link InvoiceHeader} object, the <code>itemName</code> representing the name of the item, item's
 * price <code>itemPrice</code>, and the number of units <code>count</code>.
 *
 * @author Kamal Hamza
 * @version 1.0
 */

public class InvoiceLine {

    private int id; // The id of the containing InvoiceHeader
    private String itemName; // Name of the item
    private double itemPrice; // Price of the item
    private int count; // Number of units of the item

    // Methods

    // Getters & Setters
    /**
     *
     * @return an <code>int</code> that represents the <code>id</code> of the {@link InvoiceLine} which is also
     * the <code>id</code> of the containing {@link InvoiceHeader}.
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @return a {@link String} that is the name of the item.
     */
    public String getItemName(){
        return itemName;
    }

    /**
     *
     * @return a <code>double</code> that is the price of the item.
     */

    public double getItemPrice(){
        return itemPrice;
    }

    /**
     *
     * @return an <code>int</code> that represents the number of units of the item.
     */
    public int getCount(){
        return count;
    }

}

