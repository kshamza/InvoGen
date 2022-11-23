package model;

import controller.Controller;
import exceptions.FileFormatException;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.util.ArrayList;

/**
 * A utility class that performs the reading/writing operations on the CSV files; InvoiceHeader.csv and InvoiceLine.csv
 *
 * @author Kamal Hamza
 * @version 1.0
 */

public class FileOperations {


    // Default path is set to the resources directory of the project. The user can, however, navigate away from it.
    private static final String DEFAULT_PATH = "resources/";

    private static String invoiceHeaderFilePath;
    private static String invoiceLinesFilePath;

    /**
     * A driver method used to test the file operations of loading and saving data.
     *
     * The method loads, prints, saves, and then reloads the data to make sure that the operations are performed
     * properly
     *
     *
     * @param args
     */
/*
    public static void main(String[] args) {

        // Testing the read
        ArrayList<InvoiceHeader> headersArrayList = readFile("prompt");

        for (InvoiceHeader header : headersArrayList) {
            System.out.println(header);
        }

        // Appending the data with an invoice to test the save
        InvoiceHeader tempInvoice = new InvoiceHeader("52,30-11-2022,John Doe");
        InvoiceLine invLine1 = new InvoiceLine("52,item1,1234.0,1");
        InvoiceLine invLine2 = new InvoiceLine("52,item2,5678.0,2");

        tempInvoice.getInvoiceLines().add(invLine1);
        tempInvoice.getInvoiceLines().add(invLine2);

        headersArrayList.add(tempInvoice);

        // Testing the save by saving nad then re-loading the data
        saveFile(headersArrayList);

        headersArrayList = readFile("reload");

        for (InvoiceHeader header : headersArrayList) {
            System.out.println(header);
        }
    }
*/

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
    public static ArrayList<InvoiceHeader> readFile(String loadingType) {
        if (loadingType.equals("prompt")){ // If the loading type is prompt, call the file loader, else, reload with existing file.
            fileLoader();
        }

        if (invoiceHeaderFilePath != null && invoiceLinesFilePath != null){ // If two csv files are selected.
            // An ArrayList of type InvoiceHeader to be returned.
            ArrayList<InvoiceHeader> headersArrayList = new ArrayList<>();

            // Reading Headers
            String[] headersArray = getCSVFileRows(invoiceHeaderFilePath);

            // Creating InvoiceHeader Object for each header element in headers and verify the format of the header file.
            for (String header : headersArray) {
                String trimmedHeader = header.trim();
                if (!trimmedHeader.isBlank()) {
                    try{
                        String[] tempHeader = trimmedHeader.split(",");

                        if (tempHeader.length % 3 == 0 && InvoiceHeader.isValidHeader(tempHeader)){ // Checking if the line consists of three parts
                            headersArrayList.add(new InvoiceHeader(trimmedHeader));
                        } else {
                            throw new FileFormatException();
                        }
                    } catch (FileFormatException e){
                        Controller.setHeaderFileMalformed(true);
                        System.out.println("InvoiceHeader.csv file is malformed. Fix the file and reload!");
                        break; // break out once you find one line that is malformed.
                    }
                }
            }

            // Reading Lines only if the headers file is not malformed
            if (!Controller.getHeaderFileMalformed()){

                String[] linesArray = getCSVFileRows(invoiceLinesFilePath);

                /*
                 * Converting each String into an ArrayList element of type InvoiceLine and
                 * adding it to the corresponding InvoiceHeader
                 */

                for (String line : linesArray) {

                    String trimmedLine = line.trim();

                    try{
                        String[] tempLine = trimmedLine.split(",");
                        if (!trimmedLine.isBlank()) {
                            if (tempLine.length % 4 == 0 && InvoiceLine.isValidLine(tempLine)){
                                InvoiceLine invLine = new InvoiceLine(trimmedLine);

                                // If the id of the read line matches the line of header invoice number, add it
                                // to the ArrayList of lines.
                                for (InvoiceHeader header : headersArrayList) {
                                    if (header.getInvoiceNumber() == invLine.getId()) {
                                        header.getInvoiceLines().add(invLine);
                                    }
                                }
                            } else {
                                throw new FileFormatException();
                            }
                        }
                    } catch (FileFormatException e){
                        Controller.setLinesFileMalformed(true);
                        System.out.println("InvoiceLine.csv file is malformed. Fix the file and reload!");
                        break; // Break out of the loop once you fins one line that is malformed in the lines file.
                    }
                }
            } else {
                headersArrayList.clear();
            }

            return headersArrayList;
        }

        return null;
    }

    private static void resetFileStates() {
        // Resetting the file mar=lformed flags
        Controller.setLinesFileMalformed(false);
        Controller.setHeaderFileMalformed(false);
        Controller.setHeaderFileExist(false);
        Controller.setLinesFileExist(false);
        setInvoiceHeaderFilePath(null);
        setInvoiceLinesFilePath(null);
    }

    /**
     * A method that supports the <code>readFile</code> method which performs the process of reading the content of the
     * file located at the given <code>filePath</code>.
     *
     * The method reads the file as a byte-stream and then parses it into Strings
     *
     *
     * @param filePath a {@link String} of the path to the file that needs to be read.
     *
     * @return a {@link String} array that contains {@link String} elements each representing one of the lines of the
     * read file
     */
    private static String[] getCSVFileRows(String filePath) {
        String[] rows = new String[0];
        FileInputStream fis = null;

        try {
            fis = new FileInputStream(filePath);
            byte[] b = fis.readAllBytes();
            String str = new String(b);
            rows = str.split("\r");

            if (filePath.endsWith("Header.csv")){
                Controller.setHeaderFileExist(true);
            } else {
                Controller.setLinesFileExist(true);
            }

        } catch (FileNotFoundException e) {
            System.out.print("Reading Error! Can't find Invoice ");
            if (filePath.endsWith("Header.csv")) {
                System.out.print("Headers file (InvoiceHeader.csv)");
                Controller.setHeaderFileExist(false);
            } else {
                System.out.print("Lines file (InvoiceLine.csv)");
                Controller.setLinesFileExist(false);
            }
            System.out.println(" in the resources directory.");
        } catch (IOException e) {
            System.out.print("IO Exception Reading Invoice ");
            if (filePath.endsWith("Headers.csv")) {
                System.out.println("Headers");
            } else {
                System.out.println("Lines");
            }
        } finally {
            try {
                if (fis != null){
                    fis.close();
                }
            } catch (IOException e) {
            }
        }
        return rows;
    }


    /**
     * A method used to save all the invoices to the csv files.
     *
     * @param invoiceHeaders an {@link ArrayList} of invoices that will be processed to extract headers and lines.
     */

    public static void saveFile(ArrayList<InvoiceHeader> invoiceHeaders) {

        StringBuilder headersString = new StringBuilder();
        StringBuilder linesString = new StringBuilder();

        for (int i = 0; i < invoiceHeaders.size(); i++) {
            /*
             * Forming the StringBuilder object headersString representing the invoices headers to prepare it to be
             * written to the InvoiceHeader.csv
             */
            headersString.append(invoiceHeaders.get(i).headerToString());

            if (i != invoiceHeaders.size() - 1) {
                headersString.append("\r");
            }

            // A temporary ArrayList used to store the invoice lines, so we can iterate over them and convert to Strings.
            ArrayList<InvoiceLine> lines = invoiceHeaders.get(i).getInvoiceLines();

            // Forming the StringBuilder object of the linesString to be written to the InvoiceLine.csv
            for (int j = 0; j < lines.size(); j++) {
                linesString.append(lines.get(j).lineToString());

                // Adding a \r at the end of each line
                if (j != lines.size() - 1 || i != invoiceHeaders.size() - 1) {
                    linesString.append("\r");
                }
            }
        }

        // Writing to the files
        writeCSVFileRows(invoiceHeaderFilePath, headersString); // Pass the headerString to the header file to be written
        writeCSVFileRows(invoiceLinesFilePath, linesString); // Pass linesString to the lines file to be written
    }


    /**
     * A method that supports the <code>saveFile</code> method which performs the process of writing the content of the
     * file located at the given <code>filePath</code>.
     *
     * The method writes the file as a byte-stream
     *
     *
     * @param filePath a {@link String} of the path to the file that needs to be read.
     *
     * @param rows a {@link StringBuilder} object that represents all the rows that need to be written to the file at
     *             given path <code>filePath</code>
     */
    private static void writeCSVFileRows(String filePath, StringBuilder rows) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(filePath);
            fos.write(rows.toString().getBytes());
        } catch (FileNotFoundException e) {
            System.out.print("Writing Error! Can not find Invoice ");
            if (filePath.endsWith("Header.csv")) {
                System.out.print("Headers file (InvoiceHeader.csv)");
            } else {
                System.out.print("Lines file (InvoiceLine.csv)");
            }
            System.out.println(" in the resources directory.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (fos != null){ // Trying to close only if we have an actual object
                    fos.close();
                }
            } catch (IOException e) {
            }
        }
    }


    private static void setInvoiceHeaderFilePath(String invoiceHeaderFilePath) {
        FileOperations.invoiceHeaderFilePath = invoiceHeaderFilePath;
    }

    private static void setInvoiceLinesFilePath(String invoiceLinesFilePath) {
        FileOperations.invoiceLinesFilePath = invoiceLinesFilePath;
    }


    private static void fileLoader() {
        JFileChooser fileChooser = new JFileChooser(DEFAULT_PATH);
        fileChooser.setSelectedFile(null);
        FileNameExtensionFilter fileFilter = new FileNameExtensionFilter("CSV Files", "csv");
        fileChooser.setFileFilter(fileFilter);

        File f = new File(DEFAULT_PATH + "InvoiceHeader.csv");
        fileChooser.setSelectedFile(f);

        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION ) {

            if (fileChooser.getSelectedFile().getPath().endsWith("InvoiceHeader.csv")){
                resetFileStates(); // Reset the file states and re-populate only if the user takes the first serious step into loading new files.

                setInvoiceHeaderFilePath(fileChooser.getSelectedFile().getPath());
                Controller.setHeaderFileExist(true);
            } else {
                setInvoiceHeaderFilePath(null);
                Controller.setHeaderFileExist(false);
            }
        } else {
            setInvoiceHeaderFilePath(invoiceHeaderFilePath);
        }

        f = new File(DEFAULT_PATH + "InvoiceLine.csv");
        fileChooser.setSelectedFile(f);

        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION ) {
            if (fileChooser.getSelectedFile().getPath().endsWith("InvoiceLine.csv")){
                setInvoiceLinesFilePath(fileChooser.getSelectedFile().getPath());
                Controller.setLinesFileExist(true);
            } else {
                setInvoiceLinesFilePath(null);
            }
        } else {
            setInvoiceLinesFilePath(invoiceLinesFilePath);
        }
    }
}
