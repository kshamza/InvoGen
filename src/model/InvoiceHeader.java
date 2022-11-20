package model;

import java.util.ArrayList;

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

}
