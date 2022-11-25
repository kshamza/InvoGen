package view;

import controller.Controller;
import model.InvoiceHeader;
import model.InvoiceLine;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * A dialog to take the data for a new invoice from the user.
 * To save time this class was taken from @see https://www.geeksforgeeks.org/java-swing-simple-user-registration-form/
 *  The form has been changed to match my objective which is to take an input of a new invoice.
 *
 * @author Kamal Hamza
 * @version 1.0
 */



public class NewInvoiceForm extends JDialog implements ActionListener {
    private JTextField tInvNumber;
    private JComboBox date;
    private JComboBox month;
    private JComboBox year;
    private JTextField tname;

    private static InvoiceHeader newInvoice;
    private static ArrayList<InvoiceLine> tempInvoiceLines;
    private static final String[] INVOICE_ITEMS_TABLE_COLS = {"No.", "Item Name", "Item Price", "Count", "Item Total"};
    private JScrollPane invoiceItemsScrollPane;
    private DefaultTableModel invoiceItemsTableModel;
    private JTable invoiceItems;

    private JButton addNewItem;

    private JButton insert;
    private JButton reset;
    private JButton cancel;

    public static void main(String[] args) {
        new NewInvoiceForm(null);
    }

    /**
     *
     * A constructor of the new invoice form.
     *
     * @param parentFrame the frame from which the dialog is called.
     */
    public NewInvoiceForm(JFrame parentFrame){
        super(parentFrame, "New Invoice", true);
        setFormWindowProperties();
        renderFormUI();
    }

    public static ArrayList<InvoiceLine> getTempInvoiceLines() {
        return tempInvoiceLines;
    }

    private void renderFormUI() {
        // Components of the Form
        Container c = getContentPane();
        c.setLayout(null);

        JLabel title = new JLabel("New Invoice Form");
        title.setFont(new Font("Arial", Font.PLAIN, 30));
        title.setSize(300, 30);
        title.setLocation(200, 30);
        c.add(title);

        // Invoice Number
        JLabel invNumber = new JLabel("Invoice Number");
        invNumber.setFont(new Font("Arial", Font.PLAIN, 20));

        int labelWidth = 200;
        invNumber.setSize(labelWidth, 20);
        invNumber.setLocation(100, 100);
        c.add(invNumber);

        tempInvoiceLines = new ArrayList<>();

        tInvNumber = new JTextField();
        tInvNumber.setText(String.valueOf(Controller.getLatestInvoiceNumber()));
        tInvNumber.setEditable(false);
        tInvNumber.setFont(new Font("Arial", Font.PLAIN, 15));

        int FIELD_WIDTH = 250;
        tInvNumber.setSize(FIELD_WIDTH, 20);
        tInvNumber.setLocation(300, 100);
        c.add(tInvNumber);

        // Invoice Date

        JLabel invDate = new JLabel("Invoice Date");
        invDate.setFont(new Font("Arial", Font.PLAIN, 20));
        invDate.setSize(labelWidth, 20);
        invDate.setLocation(100, 150);
        c.add(invDate);

        String[] dates = {"01", "02", "03", "04", "05",
                "06", "07", "08", "09", "10",
                "11", "12", "13", "14", "15",
                "16", "17", "18", "19", "20",
                "21", "22", "23", "24", "25",
                "26", "27", "28", "29", "30",
                "31"};

        date = new JComboBox<>(dates);
        date.setFont(new Font("Arial", Font.PLAIN, 15));
        date.setSize(50, 20);
        int dateStartLocation = 300;
        date.setLocation(dateStartLocation, 150);
        c.add(date);

        String[] months = {"01", "02", "03", "04",
                "05", "06", "07", "08",
                "09", "10", "11", "12"};

        month = new JComboBox<>(months);
        month.setFont(new Font("Arial", Font.PLAIN, 15));
        month.setSize(60, 20);
        month.setLocation(60+ dateStartLocation, 150);
        c.add(month);

        String[] years = {"1995", "1996", "1997", "1998",
                "1999", "2000", "2001", "2002",
                "2003", "2004", "2005", "2006",
                "2007", "2008", "2009", "2010",
                "2011", "2012", "2013", "2014",
                "2015", "2016", "2017", "2018",
                "2019", "2020", "2021", "2022",
                "2023"};

        year = new JComboBox<>(years);
        year.setFont(new Font("Arial", Font.PLAIN, 15));
        year.setSize(60, 20);
        year.setLocation(130 + dateStartLocation, 150);
        c.add(year);

        // Customer Name

        JLabel name = new JLabel("Customer Name");
        name.setFont(new Font("Arial", Font.PLAIN, 20));
        name.setSize(labelWidth, 20);
        name.setLocation(100, 200);
        c.add(name);

        tname = new JTextField();
        tname.setFont(new Font("Arial", Font.PLAIN, 15));
        tname.setSize(FIELD_WIDTH, 20);
        tname.setLocation(300, 200);
        c.add(tname);

        // Invoice Items - Will take them as a bulk text to simplify the processing as opposed to adding them in a different form or a table

        JLabel items = new JLabel("Invoice Items");
        items.setFont(new Font("Arial", Font.PLAIN, 20));
        items.setSize(labelWidth, 20);
        items.setLocation(100, 250);
        c.add(items);

        invoiceItemsTableModel = new DefaultTableModel();

        for (String col : INVOICE_ITEMS_TABLE_COLS){
            invoiceItemsTableModel.addColumn(col);
        }

        invoiceItems = new JTable();
        invoiceItems.setModel(invoiceItemsTableModel);
        invoiceItemsScrollPane = new JScrollPane(invoiceItems);
        invoiceItemsScrollPane.setSize(FIELD_WIDTH + labelWidth, 150);
        invoiceItemsScrollPane.setLocation(100, 300);
        c.add(invoiceItemsScrollPane);

        // Buttons

        addNewItem = new JButton("New Item");
        addNewItem.setFont(new Font("Arial", Font.PLAIN, 15));
        addNewItem.setSize(120, 20);
        addNewItem.setLocation(300, 250);
        addNewItem.addActionListener(this);
        c.add(addNewItem);


        insert = new JButton("Add");
        insert.setFont(new Font("Arial", Font.PLAIN, 15));
        insert.setSize(100, 20);
        insert.setLocation(150, 500);
        insert.addActionListener(this);
        c.add(insert);

        reset = new JButton("Reset");
        reset.setFont(new Font("Arial", Font.PLAIN, 15));
        reset.setSize(100, 20);
        reset.setLocation(270, 500);
        reset.addActionListener(this);
        c.add(reset);

        cancel = new JButton("Cancel");
        cancel.setFont(new Font("Arial", Font.PLAIN, 15));
        cancel.setSize(100, 20);
        cancel.setLocation(390, 500);
        cancel.addActionListener(this);
        c.add(cancel);

        // Dispose the new invoice form when esc is pressed.
        // Obtained from: https://gist.github.com/smamran/0a33dfabb339590aab7e
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "Cancel");
        getRootPane().getActionMap().put("Cancel", new AbstractAction()
        {
            public void actionPerformed(ActionEvent e)
            {
                dispose();
            }
        });

        setVisible(true);
        toFront();
    }

    private void setFormWindowProperties() {
        setTitle("New Invoice Form");

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double screenWidth = screenSize.getWidth();
        double screenHeight = screenSize.getHeight();

        int newInvoiceHeight = 550;
        int newInvoiceWidth = 650;

        /*
         * To center the new invoice window, we can calculate the difference between half the screen size and the
         * new invoice form's width.
         */
        setBounds((int) ((screenWidth-newInvoiceHeight)/2), (int)((screenHeight-newInvoiceWidth)/2), newInvoiceWidth, newInvoiceHeight);
        setUndecorated(true);
        getRootPane().setBorder( BorderFactory.createLineBorder(Color.BLUE) );

        getRootPane().setWindowDecorationStyle(JRootPane.NONE);
        setResizable(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == insert) {
            if (tname.getText().equals("")){
                JOptionPane.showMessageDialog(null, "Missing Customer Name!", "Missing Customer Name", JOptionPane.WARNING_MESSAGE);
                System.out.println(invoiceItemsTableModel.getRowCount());
            } else if (invoiceItemsTableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(null, "Missing Invoice Items!", "Missing Invoice Items", JOptionPane.WARNING_MESSAGE);
            } else {
                StringBuilder invoiceHeader = new StringBuilder(tInvNumber.getText());
                invoiceHeader.append(",");
                invoiceHeader.append((String)date.getSelectedItem());
                invoiceHeader.append("-");
                invoiceHeader.append((String)month.getSelectedItem());
                invoiceHeader.append("-");
                invoiceHeader.append((String)year.getSelectedItem());
                invoiceHeader.append(",");
                invoiceHeader.append(tname.getText());

                newInvoice = new InvoiceHeader(invoiceHeader.toString());
                for (InvoiceLine line : tempInvoiceLines){
                    newInvoice.getInvoiceLines().add(line);
                }

                Controller.getInvoicesArrayList().add(newInvoice);
                dispose();
            }
        } else if (e.getSource() == addNewItem){
            JDialog nII = new NewInvoiceItemForm(this);
            nII.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
            invoiceItemsTableModel.setRowCount(0);

            for (InvoiceLine line : tempInvoiceLines){
                invoiceItemsTableModel.addRow(new Object[]{line.getId(), line.getItemName(), line.getItemPrice(), line.getCount(), line.getItemPrice()*line.getCount()});
            }
        } else if (e.getSource() == reset) {
            String def = "";
            tname.setText(def);
            date.setSelectedIndex(0);
            month.setSelectedIndex(0);
            year.setSelectedIndex(0);

            // Resetting the invoice items table
            invoiceItemsTableModel.setRowCount(0);
        } else if (e.getSource() == cancel){
            dispose();
        }
    }
}

