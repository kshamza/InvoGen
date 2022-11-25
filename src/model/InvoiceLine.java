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


    // Constructor

    /**
     * A parameterized constructor of the {@link InvoiceLine} class with a single argument.
     *
     * The constructor take the <code>lineString</code> of type {@link String} as an input. The {@link String}
     * <code>lineString</code> is in the format <code>id,itemName,itemPrice,count</code>. It
     * then splits the <code>lineString</code> using the {@link String} method <code>split</code> with the comma as a
     * delimiter. It then assigns the parts of the resulting {@link String} array to the fields of this
     * {@link InvoiceLine} object.
     *
     * @param lineString a {@link String} that represents an invoice item containing item data in the format of:
     *                     <code>id,itemName,itemPrice,count</code>
     */
    public InvoiceLine(String lineString) {
        String[] lineFields = lineString.split(",");

        id = Integer.parseInt(lineFields[0]);
        itemName = lineFields[1];
        itemPrice = Double.parseDouble(lineFields[2]);
        count = Integer.parseInt(lineFields[3]);
    }


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

    /**
     * A method that converts this {@link InvoiceLine} object into a {@link StringBuilder} object in the format used
     * to save in the <code>InvoiceLine.csv</code>.
     *
     * @return {@link StringBuilder} object representing this {@link InvoiceLine} object.
     */

    public StringBuilder lineToString(){
        StringBuilder lineString = new StringBuilder();
        lineString.append(getId());
        lineString.append(",");
        lineString.append(getItemName());
        lineString.append(",");
        lineString.append(getItemPrice());
        lineString.append(",");
        lineString.append(getCount());

        return lineString;
    }

    /**
     *
     * @return a {@link String} representing an invoice item as specified in the requirements.
     */
    @Override
    public String toString() {
        return itemName + ", " + itemPrice + ", " + count;
    }



    /**
     *
     * A method that can help to detect if the line format is valid.
     *
     * @param invoiceLine an invoice line represented as a {@link String} array
     * @return <code>String</code> indicating if the invoice line format is valid or not. If returned "" this means valid line, otherwise, it will be a non-empty String holding the error message related to the invalid field
     */
    public static String isValidLine(String[] invoiceLine){

        if (invoiceLine.length != 4){
            return "Too many fields per item line!";
        }

        try{
            int x = Integer.parseInt(invoiceLine[0]);
        } catch(NumberFormatException e){
            return "Invoice Line Malformed: Invoice Number must be an Integer.";
        }

        try{
            if (Double.parseDouble(invoiceLine[2]) < 0){
                return "Invoice Line Malformed: Item price must be greater than or equal to 0.";
            }
        } catch(NumberFormatException e){
            return "Invoice Line Malformed: Item price must be a double value.";
        }


        try{
            if (Integer.parseInt(invoiceLine[3]) < 0){
                return "Invoice Line Malformed: Item count must be greater than or equal to 0.";
            }
        } catch(NumberFormatException e){
            return "Invoice Line Malformed: Item count must be an Integer.";
        }

        return "";
    }





    /**
     * A method that can help to detect if the line format is valid.
     *
     * @param invoiceLine an invoice line represented as a {@link String}
     * @return <code>String</code> indicating if the invoice line format is valid or not. If returned "" this means valid line, otherwise, it will be a non-empty String holding the error message related to the invalid field
     */

    public static String isValidLine(String invoiceLine) {
        return isValidLine(invoiceLine.split(","));
    }


}
