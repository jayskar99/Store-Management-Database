import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

public class OrderHistoryView extends JFrame implements ActionListener {
    private Database db;
    private int orderID;
    private JList<String> historyList;
    private DefaultListModel<String> historyListModel;
    private JButton addButton, updateButton, deleteButton;

    public OrderHistoryView(Database db, int orderID) {
        this.db = db;
        this.orderID = orderID;

        setTitle("Order History View");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        historyListModel = new DefaultListModel<>();
        historyList = new JList<>(historyListModel);
        historyList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION); // Set selection mode
        JScrollPane historyScrollPane = new JScrollPane(historyList);
        add(historyScrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        addButton = new JButton("Add Entry");
        updateButton = new JButton("Update Entry");
        deleteButton = new JButton("Delete Entry");
        addButton.addActionListener(this);
        updateButton.addActionListener(this);
        deleteButton.addActionListener(this);
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);

        loadOrderHistory();
        setVisible(true);
    }

    private void loadOrderHistory() {
        historyListModel.clear();
        historyListModel.addElement("orderHistoryID, orderId, quantity, productID");
        Vector<OrderHistoryModel> historyEntries = db.getOrderHistory(orderID);
        for (OrderHistoryModel entry : historyEntries) {
            historyListModel.addElement(
                String.valueOf(entry.orderHistoryID)+", "+
                String.valueOf(entry.orderID)+", "+
                String.valueOf(entry.quantity)+", "+
                String.valueOf(entry.productID)
                );
        }
    }

    // Method to add an entry
    private void addEntry(int quantity, int productID) {
        OrderHistoryModel orderHistory = new OrderHistoryModel();
        orderHistory.orderID = orderID;
        orderHistory.quantity = quantity;
        orderHistory.productID = productID;
        db.createOrderHistory(orderHistory);
        loadOrderHistory();
    }

    // Method to update an entry
    private void updateEntry(String selectedEntry, int newQuantity, int newProductID) {
        // Extract old values
        String[] parts = selectedEntry.split(", ");
        int oldOrderHistoryID = Integer.parseInt(parts[0].trim());
        int oldOrderID = Integer.parseInt(parts[1].trim());

        // Update in database
        OrderHistoryModel newOrderHistory = new OrderHistoryModel();
        newOrderHistory.orderHistoryID = oldOrderHistoryID;
        newOrderHistory.orderID = oldOrderID;
        newOrderHistory.quantity = newQuantity;
        newOrderHistory.productID = newProductID;
        db.updateOrderHistory(newOrderHistory);
        loadOrderHistory();
    }

    // Method to delete an entry
    private void deleteEntry(String selectedEntry) {
        // Extract values
        String[] parts = selectedEntry.split(",");
        int orderHistoryID = Integer.parseInt(parts[0].trim());

        // Delete from database
        db.deleteOrderHistory(orderHistoryID);
        loadOrderHistory();
    }

    // Implement actionPerformed method to handle button clicks
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            // Create a pop-up dialog for adding entry
            JTextField quantityField = new JTextField(5);
            JTextField productIDField = new JTextField(5);

            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(2, 2));
            panel.add(new JLabel("Quantity:"));
            panel.add(quantityField);
            panel.add(new JLabel("Product ID:"));
            panel.add(productIDField);

            int result = JOptionPane.showConfirmDialog(null, panel, "Add Entry", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    int quantity = Integer.parseInt(quantityField.getText());
                    int productID = Integer.parseInt(productIDField.getText());
                    addEntry(quantity, productID);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid input. Please enter valid numbers.");
                }
            }
        } else if (e.getSource() == updateButton) {
            // Update selected entry
            String selectedEntry = historyList.getSelectedValue();
            if (selectedEntry != null) {
                String[] parts = selectedEntry.split(",");
                int oldQuantity = Integer.parseInt(parts[2].trim());
                int oldProductID = Integer.parseInt(parts[3].trim());

                // Create a pop-up dialog for updating entry
                JTextField quantityField = new JTextField(String.valueOf(oldQuantity), 5);
                JTextField productIDField = new JTextField(String.valueOf(oldProductID), 5);

                JPanel panel = new JPanel();
                panel.setLayout(new GridLayout(2, 2));
                panel.add(new JLabel("Quantity:"));
                panel.add(quantityField);
                panel.add(new JLabel("Product ID:"));
                panel.add(productIDField);

                int result = JOptionPane.showConfirmDialog(null, panel, "Update Entry", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    try {
                        int newQuantity = Integer.parseInt(quantityField.getText());
                        int newProductID = Integer.parseInt(productIDField.getText());
                        updateEntry(selectedEntry, newQuantity, newProductID);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Invalid input. Please enter valid numbers.");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select an entry to update.");
            }
        } else if (e.getSource() == deleteButton) {
            // Delete selected entry
            String selectedEntry = historyList.getSelectedValue();
            if (selectedEntry != null) {
                int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete selected entry?");
                if (confirm == JOptionPane.YES_OPTION) {
                    deleteEntry(selectedEntry);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select an entry to delete.");
            }
        }
    }
}
