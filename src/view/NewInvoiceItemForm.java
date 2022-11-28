package view;

import controller.Controller;
import model.InvoiceLine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class NewInvoiceItemForm extends JDialog implements ActionListener  {

    private JTextField tItemName;
    private JTextField tItemPrice;
    private JTextField tItemCount;
    private JButton insert;
    private JButton reset;
    private JButton cancel;

    /**
     * An overloaded invoice item constructor that takes a JDialog as an input.
     *
     * @param parentDialog
     */
    public NewInvoiceItemForm(JDialog parentDialog){
        super(parentDialog, "New Invoice Item", true);
        setFormWindowProperties();
        renderFormUI(parentDialog);
    }

    /**
     * An overloaded invoice item constructor that takes a JFrame as an input.
     *
     * @param parentDialog
     */
    public NewInvoiceItemForm(JFrame parentDialog){
        super(parentDialog, "New Invoice Item", true);
        setFormWindowProperties();
        renderFormUI(parentDialog);
    }


    private void setFormWindowProperties() {
        setTitle("New Invoice Item");

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double screenWidth = screenSize.getWidth();
        double screenHeight = screenSize.getHeight();

        int newInvoiceHeight = 350;
        int newInvoiceWidth = 600;

        /*
         * To center the new invoice window, we can calculate the difference between half the screen size and the
         * new invoice form's width.
         */
        setBounds((int) ((screenWidth-newInvoiceHeight)/2), (int)((screenHeight-newInvoiceWidth)/2), newInvoiceWidth, newInvoiceHeight);
        setUndecorated(true);
        getRootPane().setBorder( BorderFactory.createLineBorder(Color.MAGENTA) );

        getRootPane().setWindowDecorationStyle(JRootPane.NONE);
        setResizable(false);
    }

    // I will use the source of the call to the NewInvoiceItemForm.java to decide which ArrayList of invoices should I edit, the new one or the loaded one in the main view.
    private void renderFormUI(RootPaneContainer parentDialog) {
        // Components of the Form
        Container c = getContentPane();
        c.setLayout(null);

        JLabel title = new JLabel("New Invoice Item");
        title.setFont(new Font("Arial", Font.PLAIN, 30));
        title.setSize(300, 30);
        title.setLocation(200, 30);
        c.add(title);

        // Item Name
        JLabel itemName = new JLabel("Item Name");
        itemName.setFont(new Font("Arial", Font.PLAIN, 20));

        int labelWidth = 200;
        itemName.setSize(labelWidth, 20);
        itemName.setLocation(100, 100);
        c.add(itemName);

        tItemName = new JTextField();
        tItemName.setFont(new Font("Arial", Font.PLAIN, 15));

        int FIELD_WIDTH = 250;
        tItemName.setSize(FIELD_WIDTH, 20);
        tItemName.setLocation(300, 100);
        c.add(tItemName);

        // Item Price
        JLabel price = new JLabel("Item Price");
        price.setFont(new Font("Arial", Font.PLAIN, 20));
        price.setSize(labelWidth, 20);
        price.setLocation(100, 150);
        c.add(price);

        tItemPrice = new JTextField();
        tItemPrice.setFont(new Font("Arial", Font.PLAIN, 15));
        tItemPrice.setSize(FIELD_WIDTH, 20);
        tItemPrice.setLocation(300, 150);
        c.add(tItemPrice);

        // Item Count
        JLabel count = new JLabel("Item Count");
        count.setFont(new Font("Arial", Font.PLAIN, 20));
        count.setSize(labelWidth, 20);
        count.setLocation(100, 200);
        c.add(count);

        tItemCount = new JTextField();
        tItemCount.setFont(new Font("Arial", Font.PLAIN, 15));
        tItemCount.setSize(FIELD_WIDTH, 20);
        tItemCount.setLocation(300, 200);
        c.add(tItemCount);

        // Buttons
        insert = new JButton("Add Item");
        insert.setFont(new Font("Arial", Font.PLAIN, 15));
        insert.setSize(100, 20);
        insert.setLocation(150, 270);

        insert.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (tItemName.getText().isBlank() || tItemName.getText().isEmpty()){
                    JOptionPane.showMessageDialog(null, "Missing Item Name!", "Missing Item Name", JOptionPane.WARNING_MESSAGE);
                } else if (tItemPrice.getText().isBlank() || tItemPrice.getText().isEmpty()){
                    JOptionPane.showMessageDialog(null, "Missing Item Price!", "Missing Item Price", JOptionPane.WARNING_MESSAGE);
                } else if (tItemCount.getText().isBlank() || tItemCount.getText().isEmpty()){
                    JOptionPane.showMessageDialog(null, "Missing Item Count!", "Missing Item Count", JOptionPane.WARNING_MESSAGE);
                } else { // All data are entered, we need to validate the input.
                    StringBuilder invoiceItem = new StringBuilder();

                    if (parentDialog instanceof JDialog){
                        invoiceItem .append(Controller.getLatestInvoiceNumber());
                    } else if (parentDialog instanceof SIGFrame) {
//                        invoiceItem .append(SIGFrame.getTempInvoiceLines().get(0).getId()); // Causes a problem when we have no invoice items in an invoice.
                        invoiceItem.append(SIGFrame.getSelectedInvoiceID()); // Rely on the number of the invoice itself.
                    }

                    invoiceItem.append(",");
                    invoiceItem.append(tItemName.getText());
                    invoiceItem.append(",");
                    invoiceItem.append(tItemPrice.getText());
                    invoiceItem.append(",");
                    invoiceItem.append(tItemCount.getText());
                    String lineValidityConfirmation = InvoiceLine.isValidLine(invoiceItem.toString());
                    if (!lineValidityConfirmation.equals("")){
                        JOptionPane.showMessageDialog(null, lineValidityConfirmation, "Invalid Item", JOptionPane.WARNING_MESSAGE);
                    } else { // Valid line, no returned error message.
                        if (parentDialog instanceof JDialog){
                            NewInvoiceForm.getTempInvoiceLines().add(new InvoiceLine(invoiceItem.toString()));
                        } else if (parentDialog instanceof SIGFrame) {
                            SIGFrame.getTempInvoiceLines().add(new InvoiceLine(invoiceItem.toString()));
                            SIGFrame.setUpdatedInvoiceLine();
                            JOptionPane.showMessageDialog(null, "Invoice total is inaccurate until you save changes!", "Inaccurate Invoice Total", JOptionPane.WARNING_MESSAGE);
                        }
                        dispose();
                    }
                }
            }
        });

        c.add(insert);

        reset = new JButton("Reset");
        reset.setFont(new Font("Arial", Font.PLAIN, 15));
        reset.setSize(100, 20);
        reset.setLocation(270, 270);
        reset.addActionListener(this);
        c.add(reset);

        cancel = new JButton("Cancel");
        cancel.setFont(new Font("Arial", Font.PLAIN, 15));
        cancel.setSize(100, 20);
        cancel.setLocation(390, 270);
        cancel.addActionListener(this);
        c.add(cancel);

        // Dispose the new invoice form when esc is pressed.
        // Obtained from: https://gist.github.com/smamran/0a33dfabb339590aab7e
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "cancel");
        getRootPane().getActionMap().put("cancel", new AbstractAction()
        {
            public void actionPerformed(ActionEvent e)
            {
                dispose();
            }
        });

        setVisible(true);
        toFront();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == reset) {
            String def = "";
            tItemName.setText(def);
            tItemPrice.setText(def);
            tItemCount.setText(def);

        } else if (e.getSource() == cancel){
            dispose();
        }
    }

}
