import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PaymentView extends JFrame implements ActionListener {
    private Database db;
    private JTextField idField, dateTimeField, supplierIDField, totalPriceField;
    private JButton addButton, viewButton, updateButton, deleteButton;

    public PaymentView(Database db) {
        this.db = db;

        // Set up the JFrame
        setTitle("Payment View");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(6, 2));

        // Create input fields
        idField = new JTextField();
        dateTimeField = new JTextField();
        supplierIDField = new JTextField();
        totalPriceField = new JTextField();

        // Create buttons
        addButton = new JButton("Add Payment");
        viewButton = new JButton("View Payment");
        updateButton = new JButton("Update Payment");
        deleteButton = new JButton("Delete Payment");

        // Add action listeners to buttons
        addButton.addActionListener(this);
        viewButton.addActionListener(this);
        updateButton.addActionListener(this);
        deleteButton.addActionListener(this);

        // Add components to the JFrame
        add(new JLabel("Payment ID:"));
        add(idField);
        add(new JLabel("Date Time:"));
        add(dateTimeField);
        add(new JLabel("Supplier ID:"));
        add(supplierIDField);
        add(new JLabel("Total Price:"));
        add(totalPriceField);
        add(addButton);
        add(viewButton);
        add(updateButton);
        add(deleteButton);

        // Display the JFrame
        setVisible(true);
    }

    // ActionPerformed method for button clicks
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            addPayment();
        } else if (e.getSource() == viewButton) {
            viewPayment();
        } else if (e.getSource() == updateButton) {
            updatePayment();
        } else if (e.getSource() == deleteButton) {
            deletePayment();
        }
    }

    // Method to add a payment
    private void addPayment() {
        PaymentModel payment = new PaymentModel();
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
        payment.dateTime = currentDateTime.format(formatter);
        payment.supplierID = Integer.parseInt(supplierIDField.getText());
        payment.totalPrice = 0;
        db.createPayment(payment);
        JOptionPane.showMessageDialog(this, "Payment added successfully.");
    }

    // Method to view a payment
    private void viewPayment() {
        int paymentID = Integer.parseInt(idField.getText());
        PaymentModel payment = db.readPayment(paymentID);
        if (payment != null) {
            idField.setText(String.valueOf(payment.paymentID));
            dateTimeField.setText(payment.dateTime);
            supplierIDField.setText(String.valueOf(payment.supplierID));
            totalPriceField.setText(String.valueOf(payment.totalPrice));
            new PaymentHistoryView(db, paymentID);
        } else {
            JOptionPane.showMessageDialog(this, "Payment not found.");
        }
    }

    // Method to update a payment
    private void updatePayment() {
        int paymentID = Integer.parseInt(idField.getText());
        String dateTime = dateTimeField.getText();
        int supplierID = Integer.parseInt(supplierIDField.getText());
        float totalPrice = Float.parseFloat(totalPriceField.getText());
        PaymentModel payment = new PaymentModel();
        payment.paymentID = paymentID;
        payment.dateTime = dateTime;
        payment.supplierID = supplierID;
        payment.totalPrice = totalPrice;
        db.updatePayment(payment);
        JOptionPane.showMessageDialog(this, "Payment updated successfully.");
    }

    // Method to delete a payment
    private void deletePayment() {
        int paymentID = Integer.parseInt(idField.getText());
        db.deletePayment(paymentID);
        JOptionPane.showMessageDialog(this, "Payment deleted successfully.");
    }
}
