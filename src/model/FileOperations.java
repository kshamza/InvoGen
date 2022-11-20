package model;

import java.util.ArrayList;

/**
 * A utility class that performs the reading/writing operations on the CSV files; InvoiceHeader.csv and InvoiceLine.csv
 *
 * @author Kamal Hamza
 * @version 1.0
 */

public class FileOperations {

    /**
     * A method to read the data from the CSV files storing the invoices and their items.
     *
     * The method reads the invoices from the file located in the directory specified by the <code>static</code> field
     * <code>INVOICE_HEADER_FILE_PATH</code> and the corresponding invoice items stored in the file located in the
     * directory specified by the <code>static</code> field <code>INVOICE_LINES_FILE_PATH</code>
     *
     * @return {@link ArrayList} of {@link InvoiceHeader} objects. Each object represents an invoice including its id,
     *            date, customer name, and an {@link ArrayList} of {@link InvoiceLine} representing the invoice items.
     */
    public static ArrayList<InvoiceHeader> readFile() {

        // An ArrayList of type InvoiceHeader to be returned.
        ArrayList<InvoiceHeader> headersArrayList = new ArrayList<>();



        return headersArrayList;
    }

    /**
     * A method used to save all the invoices to the csv files.
     *
     * @param invoiceHeaders an {@link ArrayList} of invoices that will be processed to extract headers and lines.
     */

    public static void saveFile(ArrayList<InvoiceHeader> invoiceHeaders) {

    }






}
