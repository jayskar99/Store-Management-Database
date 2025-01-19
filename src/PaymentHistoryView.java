import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

public class PaymentHistoryView extends JFrame implements ActionListener {
    private Database db;
    private int paymentID;
    private JList<String> paymentList;
    private DefaultListModel<String> paymentListModel;
    private JButton addButton, updateButton, deleteButton;

    public PaymentHistoryView(Database db, int paymentID) {
        this.db = db;
        this.paymentID = paymentID;

        setTitle("Payment History View");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        paymentListModel = new DefaultListModel<>();
        paymentList = new JList<>(paymentListModel);
        paymentList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION); // Set selection mode
        JScrollPane paymentScrollPane = new JScrollPane(paymentList);
        add(paymentScrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        addButton = new JButton("Add Payment");
        updateButton = new JButton("Update Payment");
        deleteButton = new JButton("Delete Payment");
        addButton.addActionListener(this);
        updateButton.addActionListener(this);
        deleteButton.addActionListener(this);
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);

        loadPaymentHistory();
        setVisible(true);
    }

    private void loadPaymentHistory() {
        paymentListModel.clear();
        paymentListModel.addElement("paymentHistoryID, paymentID, quantity, productID");
        Vector<PaymentHistoryModel> paymentEntries = db.getPaymentHistory(paymentID);
        for (PaymentHistoryModel entry : paymentEntries) {
            paymentListModel.addElement(
                String.valueOf(entry.paymentHistoryID) + ", " +
                String.valueOf(entry.paymentID) + ", " +
                String.valueOf(entry.quantity) + ", " +
                String.valueOf(entry.productID)
            );
        }
    }

    // Method to add a payment
    private void addPayment(int paymentID, int quantity, int productID) {
        PaymentHistoryModel paymentHistory = new PaymentHistoryModel();
        paymentHistory.paymentHistoryID = 0;
        paymentHistory.paymentID = paymentID;
        paymentHistory.quantity = quantity;
        paymentHistory.productID = productID;
        db.createPaymentHistory(paymentHistory);
        loadPaymentHistory();
    }

    // Method to update a payment
    private void updatePayment(String selectedPayment, int newQuantity, int newProductID) {
        // Extract old values
        String[] parts = selectedPayment.split(", ");
        int oldPaymentHistoryID = Integer.parseInt(parts[0].trim());
        int oldPaymentID = Integer.parseInt(parts[1].trim());

        // Update in database
        PaymentHistoryModel newPaymentHistory = new PaymentHistoryModel();
        newPaymentHistory.paymentHistoryID = oldPaymentHistoryID;
        newPaymentHistory.paymentID = oldPaymentID;
        newPaymentHistory.quantity = newQuantity;
        newPaymentHistory.productID = newProductID;
        db.updatePaymentHistory(newPaymentHistory);
        loadPaymentHistory();
    }

    // Method to delete a payment
    private void deletePayment(String selectedPayment) {
        // Extract values
        String[] parts = selectedPayment.split(", ");
        int paymentHistoryID = Integer.parseInt(parts[0].trim());

        // Delete from database
        db.deletePaymentHistory(paymentHistoryID);
        loadPaymentHistory();
    }

    // Implement actionPerformed method to handle button clicks
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            // Create a pop-up dialog for adding payment
            JTextField quantityField = new JTextField(10);
            JTextField productIDField = new JTextField(10);

            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(2, 2));
            panel.add(new JLabel("Quantity:"));
            panel.add(quantityField);
            panel.add(new JLabel("ProductID:"));
            panel.add(productIDField);

            int result = JOptionPane.showConfirmDialog(null, panel, "Add Payment", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    int quantity = Integer.parseInt(quantityField.getText());
                    int productID = Integer.parseInt(productIDField.getText());
                    addPayment(paymentID, quantity, productID);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid input. Please enter valid numbers.");
                }
            }
        } else if (e.getSource() == updateButton) {
            // Update selected payment
            String selectedPayment = paymentList.getSelectedValue();
            if (selectedPayment != null) {
                String[] parts = selectedPayment.split(", ");
                int oldAmount = Integer.parseInt(parts[2].trim());
                int oldDate = Integer.parseInt(parts[3].trim());

                // Create a pop-up dialog for updating payment
                JTextField quantityField = new JTextField(String.valueOf(oldAmount), 10);
                JTextField productIDField = new JTextField(String.valueOf(oldDate), 10);

                JPanel panel = new JPanel();
                panel.setLayout(new GridLayout(2, 2));
                panel.add(new JLabel("Quantity:"));
                panel.add(quantityField);
                panel.add(new JLabel("ProductID:"));
                panel.add(productIDField);

                int result = JOptionPane.showConfirmDialog(null, panel, "Update Payment", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    try {
                        int newQuantity = Integer.parseInt(quantityField.getText());
                        int newProductID = Integer.parseInt(productIDField.getText());
                        updatePayment(selectedPayment, newQuantity, newProductID);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Invalid input. Please enter valid numbers.");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a payment to update.");
            }
        } else if (e.getSource() == deleteButton) {
            // Delete selected payment
            String selectedPayment = paymentList.getSelectedValue();
            if (selectedPayment != null) {
                int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete selected payment?");
                if (confirm == JOptionPane.YES_OPTION) {
                    deletePayment(selectedPayment);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a payment to delete.");
            }
        }
    }
}

