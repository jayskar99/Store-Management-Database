import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class SupplierView extends JFrame implements ActionListener {
    private Database db;
    private JTextField idField, nameField, emailField;
    private JButton addButton, viewButton, updateButton, deleteButton;

    public SupplierView(Database db) {
        this.db = db;

        // Set up the JFrame
        setTitle("Supplier View");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(5, 2));

        // Create input fields
        idField = new JTextField();
        nameField = new JTextField();
        emailField = new JTextField();

        // Create buttons
        addButton = new JButton("Add Supplier");
        viewButton = new JButton("View Supplier");
        updateButton = new JButton("Update Supplier");
        deleteButton = new JButton("Delete Supplier");

        // Add action listeners to buttons
        addButton.addActionListener(this);
        viewButton.addActionListener(this);
        updateButton.addActionListener(this);
        deleteButton.addActionListener(this);

        // Add components to the JFrame
        add(new JLabel("SupplierID:"));
        add(idField);
        add(new JLabel("Name:"));
        add(nameField);
        add(new JLabel("Email:"));
        add(emailField);
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
            addSupplier();
        } else if (e.getSource() == viewButton) {
            viewSupplier();
        } else if (e.getSource() == updateButton) {
            updateSupplier();
        } else if (e.getSource() == deleteButton) {
            deleteSupplier();
        }
    }

    // Method to add a supplier
    private void addSupplier() {
        SupplierModel supplier = new SupplierModel();
        supplier.supplierID = 0;
        supplier.name = nameField.getText();
        supplier.email = emailField.getText();
        db.createSupplier(supplier);
        JOptionPane.showMessageDialog(this, "Supplier added successfully.");
    }

    // Method to view a supplier
    private void viewSupplier() {
        int supplierID = Integer.parseInt(idField.getText());
        SupplierModel supplier = db.readSupplier(supplierID);
        if (supplier != null) {
            idField.setText(String.valueOf(supplier.supplierID));
            nameField.setText(supplier.name);
            emailField.setText(supplier.email);
            new SupplierPhoneView(db,supplierID);
        } else {
            JOptionPane.showMessageDialog(this, "Supplier not found.");
        }
    }

    // Method to update a supplier
    private void updateSupplier() {
        int supplierID = Integer.parseInt(idField.getText());
        String name = nameField.getText();
        String email = emailField.getText();
        SupplierModel supplier = new SupplierModel();
        supplier.supplierID = supplierID;
        supplier.name = name;
        supplier.email = email;
        db.updateSupplier(supplier);
        JOptionPane.showMessageDialog(this, "Supplier updated successfully.");
    }

    // Method to delete a supplier
    private void deleteSupplier() {
        int supplierID = Integer.parseInt(idField.getText());
        db.deleteSupplier(supplierID);
        JOptionPane.showMessageDialog(this, "Supplier deleted successfully.");
    }
}

