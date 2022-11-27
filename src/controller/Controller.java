package controller;

import model.FileOperations;
import model.InvoiceHeader;
import model.InvoiceLine;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * A utility class to manage the updates in the view and the files.
 *
 * @author Kamal Hamza
 * @version 1.0
 */

public final class Controller {

    private static String[][] invoicesArray; // 2D array representing invoices
    private static ArrayList<InvoiceHeader> invoicesArrayList; // ArrayList representing invoices
    private static int latestInvoiceNumber; // The highest invoice number after loading. Used to give a new number for the next new invoice.
    private static HashMap<Integer, String[][]> invoiceLinesMap; // HashMap representing invoice lines with Integer-String[][] paris
    private static HashMap<Integer, ArrayList<InvoiceLine>> invoiceLinesArrayListMap;// HashMap representing invoice lines with Integer-ArrayList paris

    private static boolean headerFileExist;
    private static boolean headerFileMalformed;
    private static boolean linesFileExist;
    private static boolean linesFileMalformed;

    /**
     * This method will load the data by calling the @readFile from {@link FileOperations} class. The returned {@link ArrayList}
     * then needs to be manipulated to be prepared to be rendered as a <code>JTable</code> in the view. This involves a number of
     * operations.
     * 1- Get the data
     * 2- Form an array of Strings listing all the invoices after adding a column at the end that represents the total
     * cost on that invoice. This will be stored in the invoices String array.
     * 3- Form a HashMap of the invoice items listed in an array with a new column at the end listing the total cost of
     * each invoice item. The use of HashMap will be helpful to obtain the invoice items from the Controller to be listed
     * as the user select different invoices on the invoices table.
     */
    public static void load(String loadingType){

        ArrayList<InvoiceHeader> tempInvoiceArrayList = FileOperations.readFile(loadingType);

        if (tempInvoiceArrayList != null){ // Reading successful, populate
            invoicesArrayList =  tempInvoiceArrayList;
            int invoicesCount = invoicesArrayList.size();
            invoicesArray = new String[invoicesCount][4];
            invoiceLinesMap = new HashMap<>();
            invoiceLinesArrayListMap = new HashMap<>();


            ArrayList<InvoiceLine> invoiceLineArrayList;

            for (int i = 0 ; i < invoicesCount ; i++){

                // Get the ArrayList of the invoice lines.

                InvoiceHeader iH = invoicesArrayList.get(i);
                int invNum = iH.getInvoiceNumber();
                invoicesArray[i][0] = String.valueOf(invNum);
                invoicesArray[i][1] = iH.getInvoiceDate();
                invoicesArray[i][2] = iH.getCustomerName();

                invoiceLineArrayList = iH.getInvoiceLines();

                // Store the ArrayList in the HashMap of ArrayLists for fast retreival
                invoiceLinesArrayListMap.put(invNum, invoiceLineArrayList);
                // Convert them to String[][] and store them in the HashMap for fast retreival
                invoiceLinesMap.put(invNum, invoiceLinesArrayListToArray(invNum, invoiceLineArrayList));
                invoicesArray[i][3] = String.valueOf(calculateInvoiceTotal(invoiceLineArrayList));

                latestInvoiceNumber = invNum + 1; // Adding one after getting the highest invoice number loaded from the file.
            }
        } else {
            if (invoicesArrayList != null){
                // Loading is unsuccessful, the user selected file, so we commit to resetting view and showing error messages
                invoicesArrayList.clear();
            }
        }


    }

    public static String[][] invoiceLinesArrayListToArray(int invoiceNumber, ArrayList<InvoiceLine> arrayList){
        int linesCount = arrayList.size();
        String[][]  tempInvoiceLinesArray = new String[linesCount][5];
        for (int i = 0 ; i < linesCount ; i++){
            InvoiceLine iL = arrayList.get(i);
            tempInvoiceLinesArray[i][0] = String.valueOf(invoiceNumber);
            tempInvoiceLinesArray[i][1] = iL.getItemName();
            tempInvoiceLinesArray[i][2] = String.valueOf(iL.getItemPrice());
            tempInvoiceLinesArray[i][3] = String.valueOf(iL.getCount());
            tempInvoiceLinesArray[i][4] = String.valueOf(iL.getItemPrice() * iL.getCount());
        }
        return tempInvoiceLinesArray;
    }

    public static double calculateInvoiceTotal(ArrayList<InvoiceLine> arrayList){
        double invoiceTotal = 0;
        for (InvoiceLine iL : arrayList){
            invoiceTotal += (iL.getItemPrice() * iL.getCount());
        }
        return invoiceTotal;
    }

    /**
     * Calls the <code>saveFile</code> method from the {@link FileOperations} class and passes to it the latest model of
     * the table.
     */
    public static void save(){
        FileOperations.saveFile(invoicesArrayList);
    }


    public static ArrayList<InvoiceHeader> getInvoicesArrayList(){
        return invoicesArrayList;
    }

    public static String[][] getInvoicesArray(){
        return invoicesArray;
    }

    public static String[][] getInvoiceListArray(int invoiceNum){
        return invoiceLinesMap.get(invoiceNum);
    }

    public static ArrayList<InvoiceLine> getInvoiceLineArrayList(int invoiceNum) {
        return invoiceLinesArrayListMap.get(invoiceNum);
    }

    public static int getLatestInvoiceNumber(){
        return latestInvoiceNumber;
    }

    public static boolean getHeaderFileExist() {
        return headerFileExist;
    }

    public static void setHeaderFileExist(boolean headerFileExist) {
        Controller.headerFileExist = headerFileExist;
    }

    public static boolean getLinesFileExist() {
        return linesFileExist;
    }

    public static void setLinesFileExist(boolean linesFileExist) {
        Controller.linesFileExist = linesFileExist;
    }

    public static boolean getHeaderFileMalformed() {
        return headerFileMalformed;
    }

    public static void setHeaderFileMalformed(boolean headerFileMalformed) {
        Controller.headerFileMalformed = headerFileMalformed;
    }

    public static boolean getLinesFileMalformed() {
        return linesFileMalformed;
    }

    public static void setLinesFileMalformed(boolean linesFileMalformed) {
        Controller.linesFileMalformed = linesFileMalformed;
    }

}
