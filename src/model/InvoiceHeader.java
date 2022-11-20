package model;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A class that represents an {@link InvoiceHeader} object used to describe an invoice which can contain one or
 * more {@link InvoiceLine}.
 *
 * An invoice represented by the {@link InvoiceHeader} will be represented by the <code>invoiceNum</code>, the date
 * of the invoice <code>invoiceDate</code>, name of the customer <code>customerName</code>, and the group of items
 * represented as an {@link ArrayList} of {@link InvoiceLine}
 *
 * @author Kamal Hamza
 * @version 1.0
 */
public class InvoiceHeader {

    private int invoiceNum;
    private String invoiceDate;
    private String customerName;
    private ArrayList<InvoiceLine> invoiceLines;


    // Constructors

    /**
     * A parameterized constructor of the {@link InvoiceHeader} class with a single argument.
     *
     * The constructor take the <code>headerString</code> of type {@link String} as an input. The {@link String}
     * <code>headerString</code> is in the format <code>invoiceNum,invoiceDate,customerName</code>. It
     * then splits the <code>headerString</code> using the {@link String} method <code>split</code> with the comma as a
     * delimiter. It then assigns the parts of the resulting {@link String} array to the fields of this
     * {@link InvoiceHeader} object.
     *
     * @param headerString a {@link String} that represents an invoice header containing invoice data in the format of:
     *                     <code>invoiceNum,invoiceDate,customerName</code>
     */
    public InvoiceHeader(String headerString) {
        String[] headerFields = headerString.split(",");

        invoiceNum = Integer.parseInt(headerFields[0]);
        invoiceDate = headerFields[1];
        customerName = headerFields[2];
        invoiceLines = new ArrayList<>();
    }


    // Methods

    // Getters and Setters

    /**
     *
     * @return an <code>int</code> that represents the id of the invoice.
     */
    public int getInvoiceNumber() {
        return invoiceNum;
    }

    /**
     *
     * @return a {@link String} representing the date on which the invoice was created. The date is in the format of
     * dd-mm-yyyy
     */
    public String getInvoiceDate(){
        return invoiceDate;
    }

    /**
     *
     * @param invoiceDate a {@link String} representing the date on which the invoice was created. The date is in the
     * format of dd-mm-yyyy
     */
    public void setInvoiceDate(String invoiceDate){
        this.invoiceDate = invoiceDate;
    }

    /**
     *
     * @return a {@link String} representing the name of the customer on the invoice.
     */

    public String getCustomerName(){
        return customerName;
    }

    /**
     *
     * @param customerName A {@link String} representing the name of the customer
     */
    public void setCustomerName(String customerName){
        this.customerName = customerName;
    }

    /**
     *
     * @return an {@link ArrayList} of {@link InvoiceLine} representing the items listed under this invoice.
     */
    public ArrayList<InvoiceLine> getInvoiceLines() {
        return invoiceLines;
    }


    /**
     * A method that converts this {@link InvoiceHeader} object into a {@link StringBuilder} object in the format used
     * to save in the <code>InvoiceHeader.csv</code>.
     *
     * @return {@link StringBuilder} object representing this {@link InvoiceHeader} object.
     */
    public StringBuilder headerToString(){
        StringBuilder headerString = new StringBuilder();
        headerString.append(getInvoiceNumber());
        headerString.append(",");
        headerString.append(getInvoiceDate());
        headerString.append(",");
        headerString.append(getCustomerName());

        return headerString;
    }

    /**
     *
     * @return {@link String} representing the <code>InvoiceHeader</code> as specified in the requirements.
     */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder(invoiceNum + "\n{\n" + invoiceDate + ", " + customerName + "\n");

        for (InvoiceLine line : invoiceLines) {
            str.append(line);
            str.append("\n");
        }

        str.append("}\n");

        return str.toString();
    }


    /**
     * Indicates weather an invoice header is a valid invoice.
     *
     * @param invoiceHeader A {@link String} array that represents a split header line
     * @return <code>true</code> or <code>false</code> indicating wheather an invoice header is a valid header line.
     */
    public static boolean isValidHeader(String[] invoiceHeader){
        if (invoiceHeader.length != 3){
            return false;
        }

        try{
            int x = Integer.parseInt(invoiceHeader[0]);
        } catch(NumberFormatException e){
            System.out.println("Invoice Header Malformed: Invoice Number must be an Integer.");
            return false;
        }

        if (!isValidDate(invoiceHeader[1])){
            System.out.println(Arrays.toString(invoiceHeader));
            System.out.println("The date must be in the format \"dd-mm-yyy\"");
            return false;
        }

        return true;
    }

    /**
     * A method used to validate the date input by the user to match the format dd-mm-yyyy.
     *
     * @param date A {@link String} input representing the date entered by the user and to be verfieid.
     */
    public static boolean isValidDate(String date){
        // TODO - The regex does not check for months with 30 or 31 days and the leap years.
        String regex = "^([0-2][0-9]||3[0-1])-(0[0-9]||1[0-2])-([0-9][0-9])?[0-9][0-9]$";

        return date.matches(regex);
    }

}
