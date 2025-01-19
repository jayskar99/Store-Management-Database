import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CustomerView extends JFrame implements ActionListener {
    private Database db;
    private JTextField idField, nameField, emailField, addressField;
    private JButton addButton, viewButton, updateButton, deleteButton;

    public CustomerView(Database db) {
        this.db = db;

        // Set up the JFrame
        setTitle("Customer View");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(6, 2));

        // Create input fields
        idField = new JTextField();
        nameField = new JTextField();
        emailField = new JTextField();
        addressField = new JTextField();

        // Create buttons
        addButton = new JButton("Add Customer");
        viewButton = new JButton("View Customer");
        updateButton = new JButton("Update Customer");
        deleteButton = new JButton("Delete Customer");

        // Add action listeners to buttons
        addButton.addActionListener(this);
        viewButton.addActionListener(this);
        updateButton.addActionListener(this);
        deleteButton.addActionListener(this);

        // Add components to the JFrame
        add(new JLabel("Customer ID:"));
        add(idField);
        add(new JLabel("Name:"));
        add(nameField);
        add(new JLabel("Email:"));
        add(emailField);
        add(new JLabel("Address:"));
        add(addressField);
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
            addCustomer();
        } else if (e.getSource() == viewButton) {
            viewCustomer();
        } else if (e.getSource() == updateButton) {
            updateCustomer();
        } else if (e.getSource() == deleteButton) {
            deleteCustomer();
        }
    }

    // Method to add a customer
    private void addCustomer() {
        CustomerModel customer = new CustomerModel();
        customer.name = nameField.getText();
        customer.email = emailField.getText();
        customer.address = addressField.getText();
        db.createCustomer(customer);
        JOptionPane.showMessageDialog(this, "Customer added successfully.");
    }

    // Method to view a customer
    private void viewCustomer() {
        int customerID = Integer.parseInt(idField.getText());
        CustomerModel customer = db.readCustomer(customerID);
        if (customer != null) {
            idField.setText(String.valueOf(customer.customerID));
            nameField.setText(customer.name);
            emailField.setText(customer.email);
            addressField.setText(customer.address);
            new CustomerPhoneView(db,customerID);
        } else {
            JOptionPane.showMessageDialog(this, "Customer not found.");
        }
    }

    // Method to update a customer
    private void updateCustomer() {
        int customerID = Integer.parseInt(idField.getText());
        if (customerID != 0) {
            CustomerModel customer = new CustomerModel();
            customer.customerID = customerID;
            customer.name = nameField.getText();
            customer.email = emailField.getText();
            customer.address = addressField.getText();
            db.updateCustomer(customer);
            JOptionPane.showMessageDialog(this, "Customer updated successfully.");
        } else {
            JOptionPane.showMessageDialog(this, "Please view a customer first.");
        }
    }

    // Method to delete a customer
    private void deleteCustomer() {
        int customerID = Integer.parseInt(idField.getText());
        if (customerID != 0) {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this customer?");
            if (confirm == JOptionPane.YES_OPTION) {
                db.deleteCustomer(customerID);
                JOptionPane.showMessageDialog(this, "Customer deleted successfully.");
                clearFields();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please view a customer first.");
        }
    }

    // Method to clear input fields
    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        emailField.setText("");
        addressField.setText("");
    }
}
