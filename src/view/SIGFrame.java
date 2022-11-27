package view;

import controller.Controller;
import model.InvoiceHeader;
import model.InvoiceLine;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * A class that represents the main frame of the SIG application
 *
 * @author Kamal Hamza
 * @version 1.0
 *
 */

public class SIGFrame extends JFrame implements ActionListener {

    // Menus, Submenus, and Menu Items
    private JMenuBar menuBar;

    // Left Panel
    private JPanel leftPanel;
    private JPanel leftTableSubPanel;
    private JPanel leftButtonsSubPanel;
    private JPanel leftTableLabelPanel;

    private static final String[] INVOICES_TABLE_COLS = {"No.", "Date", "Customer", "Total"};
    private static final String[] INVOICE_ITEMS_TABLE_COLS = {"No.", "Item Name", "Item Price", "Count", "Item Total"};

    JScrollPane invoicesScrollPane;

    private DefaultTableModel invoicesTableModel;

    private JTable invoices;


    // Right Panel
    private JPanel rightPanel;
    private JPanel rightTableSubPanel;

    private static ArrayList<InvoiceLine> tempInvoiceLines;
    private JPanel rightTableButtonsSubPanel;

    private JPanel rightButtonsSubPanel;
    private JPanel invoiceDataSubPanel;

    private JTextField invNumberField;

    private JTextField invDateField;
    private boolean isDateFieldEdited = false;
    private String updatedDate;
    private static boolean updatedInvoiceLine;

    private JTextField invCustomerNameField;
    private boolean isCustomerNameFieldEdited = false;
    private String updatedCustomerName;

    private JTextField invTotalField;

    JScrollPane invoiceItemsScrollPane;
    private DefaultTableModel invoiceItemsTableModel;

    private JTable invoiceItems;

    public SIGFrame(){
        super("Sales Invoice Generator (SIG)");

        setWindowProperties();
        renderMenuBar();
        renderRightPanel(this);
        renderLeftPanel();

        // Add components to the frame
        renderFrame();
        loadInvoices();

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE); // Do nothing at close

        /*
         * Add a listener such that if it is the close button is closed, we should be able to do the pre-exit check
         * similar to the Quit button from the menu.
         */
        WindowListener exitListener = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                executeExitCheck();
            }
        };
        addWindowListener(exitListener);
    }

    private void renderFrame() {
        setJMenuBar(menuBar);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // Master Panel
        JPanel masterPanel = new JPanel(new BorderLayout());
        masterPanel.setLayout(new GridLayout(1,2,10,10));
        masterPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));

        // Adding sub-panels to the main left panel
        leftPanel.add(leftTableLabelPanel);
        leftPanel.add(leftTableSubPanel);

        // Adding sub-panels to the main Right Panel
        rightPanel.add(invoiceDataSubPanel);
        rightPanel.add(rightTableSubPanel);

        masterPanel.add(leftPanel);
        masterPanel.add(rightPanel);

        JPanel buttonsPanel = new JPanel(new BorderLayout());
        buttonsPanel.setLayout(new GridLayout(1,2,10,10));

        buttonsPanel.add(leftButtonsSubPanel);
        buttonsPanel.add(rightButtonsSubPanel);

        mainPanel.add(masterPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(5, 50)));
        mainPanel.add(buttonsPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(5, 50)));
        add(mainPanel);

        setVisible(true);
    }

    private void renderRightPanel(JFrame frame) {
        rightPanel = new JPanel(new BorderLayout());
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        renderInvoiceDataSubPanel();
        renderRightTableSubPanel(frame);
        renderRightButtonSubPanel();
    }

    private void renderLeftPanel() {
        leftPanel = new JPanel(new BorderLayout());
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        renderLeftTableLabelSubPanel();
        renderLeftTableSubPanel();
        renderLeftButtonSubPanel();
    }

    private void setWindowProperties() {
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Initially maximize the window
        setResizable(true); // Allow it to be re-sizeable
        setMinimumSize(new Dimension(850, 600)); // The minimum dimensions which guarantee that all buttons
        // will have at least word such as "Create", "Delete", "Save", and "Cancel"
    }

    private void renderMenuBar() {
        menuBar = new JMenuBar();

        JMenuItem loadMenuItem = new JMenuItem("Load", 'L');
        loadMenuItem.setAccelerator(KeyStroke.getKeyStroke('L', KeyEvent.CTRL_DOWN_MASK));

        JMenuItem saveMenuItem = new JMenuItem("Save", 'S');
        saveMenuItem.setAccelerator(KeyStroke.getKeyStroke('S', KeyEvent.CTRL_DOWN_MASK));

        JMenuItem quitMenuItem = new JMenuItem("Quit");
        quitMenuItem.setAccelerator(KeyStroke.getKeyStroke('Q', KeyEvent.CTRL_DOWN_MASK));

        JMenu fileMenu = new JMenu("File");
        fileMenu.add(loadMenuItem);
        loadMenuItem.setActionCommand("load");
        loadMenuItem.addActionListener(this);

        fileMenu.add(saveMenuItem);
        saveMenuItem.setActionCommand("save");
        saveMenuItem.addActionListener(this);

        fileMenu.addSeparator();

        // Quit menu item
        fileMenu.add(quitMenuItem);
        quitMenuItem.setActionCommand("quit");
        quitMenuItem.addActionListener(this);

        menuBar.add(fileMenu);
    }

    private void renderInvoiceDataSubPanel(){
        invoiceDataSubPanel = new JPanel(new BorderLayout());
        invoiceDataSubPanel.setLayout(new GridLayout(5,1));

        JPanel invNumber = new JPanel(new BorderLayout());
        JLabel invNumberLabel = new JLabel("Invoice Number   ");
        invNumberField = new JTextField("");
        invNumberField.setEditable(false);
        invNumberField.setBorder(null);
        invNumber.add(invNumberLabel,BorderLayout.WEST);
        invNumber.add(invNumberField,BorderLayout.CENTER);

        JPanel invDate = new JPanel(new BorderLayout());
        JLabel invDateLabel = new JLabel("Invoice Date        ");
        invDateField = new JTextField("");
        invDateLabel.setBorder(null);
        invDate.add(invDateLabel,BorderLayout.WEST);
        invDate.add(invDateField,BorderLayout.CENTER);

        // A listener to check if the customer name field has been changed.
        invDateField.addActionListener(
                new ActionListener() {
                    public void actionPerformed( ActionEvent e ) {
                        updatedDate = invDateField.getText().trim();

                        /*
                         * We want to check if the user just clicked the box but did not edit the box. So we get the
                         * original value from the file and use it for comparison. To do so, we need to get the invoiceID.
                         *
                         */
                        int invoiceID = invoices.getSelectedRow();

                        if (updatedDate.isEmpty() || updatedDate.isBlank()){ // If the name became empty or spaces, we show an error message and reload the currently selected row.
                            JOptionPane.showMessageDialog(null, "The invoice date can not be empty.", "Missing invoice date", JOptionPane.ERROR_MESSAGE);
                            updatedDate = null; // Are you sure?
                            reloadSelectedInvoiceData("cancel");
                        } else if (!Controller.getInvoicesArrayList().get(invoiceID).getInvoiceDate().equals(updatedDate)){
                            // We have a new String that we need to check if it is valid.
                            if (InvoiceHeader.isValidDate(updatedDate)){
                                isDateFieldEdited = true;
                            } else {
                                JOptionPane.showMessageDialog(null, "Invalid Date. Use format dd-mm-yyyy", "Wrong date format", JOptionPane.ERROR_MESSAGE);
                                updatedDate = null;
                                reloadSelectedInvoiceData("cancel");
                            }
                        } else { // It was clicked, but the String never changed, we reset the updatedDate to indicate no change at exit
                            updatedDate = null;
                        }
                    }
                } );

        // A FocusListener to allow for the field to edit when the user clicks away without needing to press enter
        invDateField.addFocusListener(
                new FocusListener() {
                    @Override
                    public void focusGained(FocusEvent e) {

                    }

                    @Override
                    public void focusLost(FocusEvent e) {
                        invDateField.postActionEvent();
                    }
                }
        );

        JPanel invCustomerName = new JPanel(new BorderLayout());
        JLabel invCustomerNameLabel = new JLabel("Customer Name  ");
        invCustomerNameField = new JTextField("");
        invCustomerNameLabel.setBorder(null);
        invCustomerName.add(invCustomerNameLabel,BorderLayout.WEST);
        invCustomerName.add(invCustomerNameField,BorderLayout.CENTER);

        // A listener to check if the customer name field has been changed.
        invCustomerNameField.addActionListener(
                new ActionListener()
                {
                    public void actionPerformed( ActionEvent e )
                    {
                        updatedCustomerName = invCustomerNameField.getText().trim();

                        /*
                         * We want to check if the user just clicked the box but did not edit the box. So we get the
                         * original value from the file and use it for comparison. To do so, we need to get the invoiceID.
                         *
                         */
                        int invoiceID = invoices.getSelectedRow();

                        if (updatedCustomerName.isEmpty() || updatedCustomerName.isBlank()){ // If the name became empty or spaces, we show an error message and reload the currently selected row.
                            JOptionPane.showMessageDialog(null, "The customer name can not be empty.", "Missing customer name", JOptionPane.ERROR_MESSAGE);
                            updatedCustomerName = null; // Are you sure?
                            reloadSelectedInvoiceData("cancel");
                        } else if (!Controller.getInvoicesArrayList().get(invoiceID).getCustomerName().equals(updatedCustomerName)){
                            // We need to set the flag that it has changed to be true, so we can write it back and save.
                            isCustomerNameFieldEdited = true;
                        } else { // It was clicked, but the String never changed, we reset the updateCustomerName to indicate no change at exit
                            updatedCustomerName = null;
                        }
                    }
                } );

        // A FocusListener to allow for the field to edit when the user clicks away without needing to press enter
        invCustomerNameField.addFocusListener(
                new FocusListener() {
                    @Override
                    public void focusGained(FocusEvent e) {

                    }

                    @Override
                    public void focusLost(FocusEvent e) {
                        invCustomerNameField.postActionEvent();
                    }
                }
        );

        JPanel invTotal = new JPanel(new BorderLayout());
        JLabel invTotalLabel = new JLabel("Invoice Total        ");
        invTotalField = new JTextField("");
        invTotalField.setEditable(false);
        invTotalField.setBorder(null);
        invTotal.add(invTotalLabel,BorderLayout.WEST);
        invTotal.add(invTotalField,BorderLayout.CENTER);

        JPanel invItems = new JPanel(new BorderLayout());

        JLabel invItemsLabel = new JLabel("Invoice Items");
        invItems.add(invItemsLabel,BorderLayout.WEST);

        invoiceDataSubPanel.add(invNumber);
        invoiceDataSubPanel.add(invDate);
        invoiceDataSubPanel.add(invCustomerName);
        invoiceDataSubPanel.add(invTotal);
        invoiceDataSubPanel.add(invItems);

        resetInvoiceDataDisplay();
    }

    private void renderRightTableSubPanel(JFrame frame){
        // Invoice Items
        rightTableSubPanel = new JPanel(new BorderLayout());

        rightTableSubPanel.setLayout(new BoxLayout(rightTableSubPanel, BoxLayout.Y_AXIS));


        rightTableButtonsSubPanel = new JPanel(new BorderLayout());
        rightTableButtonsSubPanel.setLayout(new GridLayout(1,3));
        JButton addItem= new JButton("Create Item");
        addItem.setFont(new Font("Arial", Font.PLAIN, 15));
        addItem.setActionCommand("newItem");
        addItem.addActionListener(this);
        addItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog nII = new NewInvoiceItemForm(frame);
                nII.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
                reloadSelectedInvoiceData("refresh");
            }
        });

        JButton deleteItem = new JButton("Delete Item");
        deleteItem.setFont(new Font("Arial", Font.PLAIN, 15));
        deleteItem.setActionCommand("deleteItem");
        deleteItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });


        // Distributing the two buttons over a row of five columns.
        rightTableButtonsSubPanel.add(addItem);
        rightTableButtonsSubPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        rightTableButtonsSubPanel.add(deleteItem);
        rightTableSubPanel.add(rightTableButtonsSubPanel);

        invoiceItemsTableModel = new DefaultTableModel();

        for (String col : INVOICE_ITEMS_TABLE_COLS){
            invoiceItemsTableModel.addColumn(col);
        }

        invoiceItems = new JTable();
        invoiceItems.setModel(invoiceItemsTableModel);
        invoiceItemsScrollPane = new JScrollPane(invoiceItems);
        rightTableSubPanel.add(invoiceItemsScrollPane);
    }

    private void renderRightButtonSubPanel(){
        rightButtonsSubPanel = new JPanel(new BorderLayout());
        rightButtonsSubPanel.setLayout(new GridLayout(1,5));

        JButton saveBtn = new JButton("Save");
        JButton cancelBtn = new JButton("Cancel");
        rightButtonsSubPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        rightButtonsSubPanel.add(saveBtn);
        saveBtn.setActionCommand("save");
        saveBtn.addActionListener(this);
        rightButtonsSubPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        rightButtonsSubPanel.add(cancelBtn);
        cancelBtn.setActionCommand("cancel");
        cancelBtn.addActionListener(this);
        rightButtonsSubPanel.add(Box.createRigidArea(new Dimension(5, 0)));
    }

    private void renderLeftTableLabelSubPanel(){
        leftTableLabelPanel = new JPanel(new BorderLayout());
        leftTableLabelPanel.setLayout(new GridLayout(2,1));
        JLabel leftTableLabel = new JLabel("Invoices");
        leftTableLabel.setFont(new Font("Verdana", Font.PLAIN, 15));
        leftTableLabelPanel.add(leftTableLabel,BorderLayout.WEST);
    }

    private void renderLeftTableSubPanel(){
        leftTableSubPanel = new JPanel(new BorderLayout());
        invoicesTableModel = new DefaultTableModel();

        for (String col : INVOICES_TABLE_COLS){
            invoicesTableModel.addColumn(col);
        }

        invoices = new JTable();
        invoicesScrollPane = new JScrollPane(invoices);
        invoices.setModel(invoicesTableModel);

        // This part was obtained from here:
        // https://stackoverflow.com/questions/10128064/jtable-selected-row-click-event
        invoices.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting() && invoices.getSelectedRow() != -1){
                    // Condition to prevent calling the event twice for each selection
                    reloadSelectedInvoiceData("cancel");
                }
            }
        });

        leftTableSubPanel.add(invoicesScrollPane);
    }

    private void renderLeftButtonSubPanel(){
        leftButtonsSubPanel = new JPanel(new BorderLayout());
        leftButtonsSubPanel.setLayout(new GridLayout(1,5));

        JButton newInvoiceBTN = new JButton("Create New Invoice");
        newInvoiceBTN.setActionCommand("new");
        newInvoiceBTN.addActionListener(this);

        JButton deleteInvoiceBTN = new JButton("Delete Invoice");
        deleteInvoiceBTN.setActionCommand("delete");
        deleteInvoiceBTN.addActionListener(this);

        // Distributing the two buttons over a row of five columns.
        leftButtonsSubPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        leftButtonsSubPanel.add(newInvoiceBTN);
        leftButtonsSubPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        leftButtonsSubPanel.add(deleteInvoiceBTN);
        leftButtonsSubPanel.add(Box.createRigidArea(new Dimension(5, 0)));
    }

    /**
     * A method to handle the multiple actions from different buttons in the application.
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        switch(e.getActionCommand()){
            case "quit": // Perform the pre-exit check before quitting.
                executeExitCheck();
                break;
            case "load": // Load invoices and reset the right panel view
                loadInvoices();
                resetInvoiceDataDisplay();
                break;
            case "save": // Save changes in the invoice date and customer name.
                if (invoices.getSelectedRow() == -1) { // No invoice is selected
                    JOptionPane.showMessageDialog(null, "You have to select an invoice first to edit and save.", "No Invoice Selected", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    if (!isDateFieldEdited && !isCustomerNameFieldEdited){  // If no changes detected, then inform the user that no changes to save.
                        JOptionPane.showMessageDialog(null, "No changes to save!", "Unchanged Data", JOptionPane.PLAIN_MESSAGE);
                    } else { // At least one of the two data has been changed, we need to save it.

                        int invoiceID = invoices.getSelectedRow(); // Get the row selected to edit that row.

                        if (isDateFieldEdited){ // Apply change if the date field was edited
                            Controller.getInvoicesArrayList().get(invoiceID).setInvoiceDate(updatedDate);
                        }

                        if (isCustomerNameFieldEdited){ // Apply change if the customer name field was edited
                            Controller.getInvoicesArrayList().get(invoiceID).setCustomerName(updatedCustomerName);
                        }

                        // TODO Maybe we can separate the update file and update view this way we can save operation on updating the view.
                        updateFilesAndView(); // Update the file and view by saving the updated table, then reload the data to update the view.
                        invoices.setRowSelectionInterval(invoiceID, invoiceID); // After the update of view, go back and re-select the invoice the user has edited.

                        // Inform the user that the invoice has been saved.
                        JOptionPane.showMessageDialog(null, "Invoice Updated Successfully." , "Successful Update", JOptionPane.INFORMATION_MESSAGE);

                        // Reset the temp holders of changed data and the flags.
                        updatedDate = null;
                        updatedCustomerName = null;
                        isDateFieldEdited = false;
                        isCustomerNameFieldEdited = false;
                    }
                }
                break;
            case "new":
                if (Controller.getHeaderFileExist() && Controller.getLinesFileExist()){ // Allow saving only if proper files are selected.
                    // Reset any edits before heading to new invoice form.
                    if (isDateFieldEdited || isCustomerNameFieldEdited) {
                        resetChangedFields();
                    }

                    JDialog nI = new NewInvoiceForm(this);
                    nI.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
                    updateFilesAndView();

                    // After the new invoice dialog, we select the last item in the table
                    int tableSize = invoicesTableModel.getRowCount();
                    if (tableSize >= 1){
                        invoices.setRowSelectionInterval(tableSize-1, tableSize-1); // After the update of view, go back and re-select the invoice the user has edited.
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "You have to select files first before creating and saving new invoices..", "Files are not Selected", JOptionPane.INFORMATION_MESSAGE);
                }

                break;
            case "delete":
                if (invoices.getSelectedRow() == -1){ // This means no line from the main table is selected, print a message
                    JOptionPane.showMessageDialog(null, "You have to select an invoice first to delete.", "No Invoice Selected", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    int invoiceID = Integer.valueOf(invoices.getValueAt(invoices.getSelectedRow(), 0).toString());
                    int a = JOptionPane.showConfirmDialog( this,"Are you sure you want to delete this invoice?", "Confirm Invoice Deletion", JOptionPane.YES_NO_OPTION );

                    if (a == 0){ // Selected Yes --> Implement Deletion
                        // Handle deletion at two points: UI and FileOperations to save the data without the deleted invoice.
                        // Instead of deleting the row, we will delete the invoice from the file and then reload the updated file.

                        for (int i = 0 ; i < Controller.getInvoicesArrayList().size() ; i++){
                            if (Controller.getInvoicesArrayList().get(i).getInvoiceNumber() == invoiceID){
                                Controller.getInvoicesArrayList().remove(i);
                                break;
                            }
                        }
                        updateFilesAndView();
                        JOptionPane.showMessageDialog(null, "Invoice Deleted Successfully." , "Deletion Confirmation", JOptionPane.INFORMATION_MESSAGE);
                    }
                    // Otherwise, do nothing.
                }

                // After delete, if the table is not empty, select the first row in the table
                if (invoicesTableModel.getRowCount() >= 1){
                    invoices.setRowSelectionInterval(0, 0); // After the update of view, go back and re-select the invoice the user has edited.
                }

                break;
            case "cancel":
                if (invoices.getSelectedRow() != -1 && (updatedCustomerName != null || updatedDate != null || updatedInvoiceLine )){
                    // This means a line from the main table is selected and some edits have been performed
                    // Cancel will just reload the invoice again to overwrite any changes that may have been done to the invoice
//                    reloadSelectedInvoiceData("cancel");
                    resetChangedFields();
                }
        }
    }

    /**
     * A method to load the invoices to the view. It utilizes the <code>load</code> and <code>getInvoiceArray</code>
     * methods from the {@link Controller} class.
     */
    private void loadInvoices(){
        // Loading the invoices from the files and getting the array of invoices.
        Controller.load("prompt");
        displayInvoices();
    }

    /**
     * User to reload the view in case save, edit, or delete have been done without a new prompt to the user
     */
    private void reLoadInvoices(){
        // Loading the invoices from the files and getting the array of invoices.
        Controller.load("reload");
        displayInvoices();
    }

    /**
     * A method used to save the changes to the file and then re-load the data to update the view.
     *
     * This method is used after save, delete, new.
     */
    private void updateFilesAndView(){
        Controller.save(); // Call the Controller's method "save"
        reLoadInvoices();
        displayInvoices(); // Load the invoices to the view.
        resetInvoiceDataDisplay(); // Reset the selected invoice and the right panel.
    }

    private void resetTotalView(){
        // Resetting the table model of the invoices table in the left panel.
        resetInvoiceDataDisplay();
        invoicesTableModel.setRowCount(0);

    }

    private void displayInvoices(){

        resetTotalView(); // Reset view to prepare for loading the new view

        if (!Controller.getHeaderFileExist()){ // Invoices file does not exit, show error message to place the file.
            JOptionPane.showMessageDialog(null, "To get started, you will need to load InvoiceHeader.csv.\n\nTo load the file:\n1- Select File --> Load, or \n2- Press ctrl + L" , "Header File is not loaded", JOptionPane.WARNING_MESSAGE);
            resetTotalView();
        } else { // The invoices file exists, load the data and check for the invoiceItems file.
            if (Controller.getHeaderFileMalformed()){ // Invoices file has formatting problem
                JOptionPane.showMessageDialog(null, "InvoiceHeader.csv is malformed! Fix the format error and then load." , "Header File Malformed", JOptionPane.ERROR_MESSAGE);
                resetTotalView();
            } else {

                /*
                 * Check for the invoice lines file, we are not going to load anything, but it gives the user a heads-up
                 * about the partial data and what to expect.
                 */

                if (!Controller.getLinesFileExist()){
                    JOptionPane.showMessageDialog(null, "You will need to load InvoiceLine.csv after loading the InvoiceHeader.csv file.\n\nTo load the files:\n1- Select File --> Load, or \n2- Press ctrl + L" , "Header File is not loaded", JOptionPane.WARNING_MESSAGE);
                    resetTotalView();
                } else {

                    String[][] invoicesArray = Controller.getInvoicesArray();

                    // Adding the fetched invoices to the table
                    for (String[] invoice : invoicesArray){
                        if (!Controller.getLinesFileMalformed()){ // Present normal row if the invoice line is not malformed
                            invoicesTableModel.addRow(new Object[] {invoice[0], invoice[1], invoice[2], invoice[3]});
                        } else { // If the Invoice Lines file is malformed,
                            JOptionPane.showMessageDialog(null, "InvoiceLine.csv is malformed! Fix the format error and then load." , "Invoice Lines File Malformed", JOptionPane.ERROR_MESSAGE);
                            resetTotalView();
                            break;
                        }
                    }

                    // Setting the table model to the fetched data.
                    invoices.setModel(invoicesTableModel);
                }

            }
        }
    }


    /**
     * Given the <code>invoiceID</code>, the method will load the data of the selected invoice into the right panel showing
     * the ID, date, customer name, and invoice items.
     *
     * @param invoiceID an <code>int</code> that represents the id of the invoice selected in the table of invoices.
     */
    private void loadSelectedInvoiceData(int invoiceID, String mode){

        if (Controller.getLinesFileMalformed()){
            resetInvoiceDataDisplay();
        } else {
            // We have to find index of the invoice corresponding to the invoiceID
            String[][] tempArray = Controller.getInvoicesArray();

            int idx = 0;

            // We need to find the index corresponding to the invoiceID
            for (int i = 0 ; i < tempArray.length ; i++){
                if (Integer.valueOf(tempArray[i][0]) == invoiceID){
                    idx = i;
                    break;
                }
            }

            /*
             * Once the data of the invoice is loaded, we can now set the Date and Customer name fields to be editable.
             * Notice that they were set to be uneditable before when no invoice is loaded to prevent edits while no invoice
             * is selected.
             */

            invDateField.setEditable(true);
            invCustomerNameField.setEditable(true);

            // Setting the text boxes with the corresponding data.
            invNumberField.setText(String.valueOf(invoiceID));
            invDateField.setText(tempArray[idx][1]);
            invCustomerNameField.setText(tempArray[idx][2]);
            invTotalField.setText(tempArray[idx][3]);

            String[][] invoiceItemsArray = new String[0][];
            tempInvoiceLines = Controller.getInvoiceLineArrayList(invoiceID);

            if (mode.equals("cancel")){
                invoiceItemsArray = Controller.getInvoiceListArray(invoiceID);
            } else if (mode.equals("refresh")) {
                invoiceItemsArray = Controller.invoiceLinesArrayListToArray(invoiceID, tempInvoiceLines);
            }

            // Resetting the invoice items table
            invoiceItemsTableModel.setRowCount(0);

            // Adding the invoice items rows to the table.
            for (String[] invoiceItem : invoiceItemsArray){
                invoiceItemsTableModel.addRow(new Object[] {invoiceItem[0], invoiceItem[1], invoiceItem[2], invoiceItem[3], invoiceItem[4]});
            }

            // Setting the table model to the created table.
            invoiceItems.setModel(invoiceItemsTableModel);

        }
    }

    /**
     * A method used to re-populate the right panel with the selected invoice data in case an update and refresh of the
     * view were performed.
     *
     * It utilizes the <code>LoadSelectedInvoiceData</code> method by passing to it the current selected invoice from
     * the table.
     */
    private void reloadSelectedInvoiceData(String mode){
        loadSelectedInvoiceData(Integer.valueOf(invoices.getValueAt(invoices.getSelectedRow(), 0).toString()), mode);
    }

    /**
     * A method used to reset the display of a selected invoice in case a re-load was done
     */
    private void resetInvoiceDataDisplay(){
        invNumberField.setText("");

        invDateField.setText("");
        invDateField.setEditable(false);

        invCustomerNameField.setText("");
        invCustomerNameField.setEditable(false);

        invTotalField.setText("");

        if (invoiceItems != null){
            invoiceItems.setModel(invoiceItemsTableModel);
            invoiceItemsTableModel.setRowCount(0);
        }
    }

    /**
     * A method that is used to reset the changed date and customer name fields.
     */
    private void resetChangedFields(){
        reloadSelectedInvoiceData("cancel");
        updatedCustomerName = null;
        updatedDate = null;
        isDateFieldEdited = false;
        isCustomerNameFieldEdited = false;
        resetUpdatedInvoiceLine();
    }

    /**
     * A method that performs pre-exit check on any changes that may have been done to the Date and Customer Name fields.
     */
    private void executeExitCheck(){
        if (updatedDate == null && updatedCustomerName == null){ // No changes detected, exit.
            dispose();
        } else { // Changes detected, confirm that the user does not want to save the changes.
            int a = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit without saving your data?", "Confirm Exit Without Saving", JOptionPane.YES_NO_OPTION);
            if (a == 0){ // Selected Yes --> exit
                dispose();
            }
            // Otherwise, get back to the app.
        }
    }


    public static void setUpdatedInvoiceLine(){
        updatedInvoiceLine = true;
    }

    public static void resetUpdatedInvoiceLine(){
        updatedInvoiceLine = false;
    }

    public static ArrayList<InvoiceLine> getTempInvoiceLines() {
        return tempInvoiceLines;
    }

}
