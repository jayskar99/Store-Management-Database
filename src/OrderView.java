import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class OrderView extends JFrame implements ActionListener {
    private Database db;
    private JTextField idField, dateTimeField, customerIDField, totalPriceField;
    private JButton addButton, viewButton, updateButton, deleteButton;

    public OrderView(Database db) {
        this.db = db;

        // Set up the JFrame
        setTitle("Order View");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(6, 2));

        // Create input fields
        idField = new JTextField();
        dateTimeField = new JTextField();
        customerIDField = new JTextField();
        totalPriceField = new JTextField();

        // Create buttons
        addButton = new JButton("Add Order");
        viewButton = new JButton("View Order");
        updateButton = new JButton("Update Order");
        deleteButton = new JButton("Delete Order");

        // Add action listeners to buttons
        addButton.addActionListener(this);
        viewButton.addActionListener(this);
        updateButton.addActionListener(this);
        deleteButton.addActionListener(this);

        // Add components to the JFrame
        add(new JLabel("Order ID:"));
        add(idField);
        add(new JLabel("Date Time:"));
        add(dateTimeField);
        add(new JLabel("Customer ID:"));
        add(customerIDField);
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
            addOrder();
        } else if (e.getSource() == viewButton) {
            viewOrder();
        } else if (e.getSource() == updateButton) {
            updateOrder();
        } else if (e.getSource() == deleteButton) {
            deleteOrder();
        }
    }

    // Method to add an order
    private void addOrder() {
        OrderModel order = new OrderModel();
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
        order.dateTime = currentDateTime.format(formatter);
        order.customerID = Integer.parseInt(customerIDField.getText());
        order.totalPrice = 0;
        db.createOrder(order);
        JOptionPane.showMessageDialog(this, "Order added successfully.");
    }

    // Method to view an order
    private void viewOrder() {
        int orderID = Integer.parseInt(idField.getText());
        OrderModel order = db.readOrder(orderID);
        if (order != null) {
            idField.setText(String.valueOf(order.orderID));
            dateTimeField.setText(order.dateTime);
            customerIDField.setText(String.valueOf(order.customerID));
            totalPriceField.setText(String.valueOf(order.totalPrice));
            new OrderHistoryView(db,orderID);
        } else {
            JOptionPane.showMessageDialog(this, "Order not found.");
        }
    }

    // Method to update an order
    private void updateOrder() {
        int orderID = Integer.parseInt(idField.getText());
        int customerID = Integer.parseInt(customerIDField.getText());
        float totalPrice = Float.parseFloat(totalPriceField.getText());
        OrderModel order = new OrderModel();
        order.orderID = orderID;
        order.customerID = customerID;
        order.totalPrice = totalPrice;
        db.updateOrder(order);
        JOptionPane.showMessageDialog(this, "Order updated successfully.");
    }

    // Method to delete an order
    private void deleteOrder() {
        int orderID = Integer.parseInt(idField.getText());
        db.deleteOrder(orderID);
        JOptionPane.showMessageDialog(this, "Order deleted successfully.");
    }
}
