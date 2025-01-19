import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ProductView extends JFrame implements ActionListener {
    private Database db;
    private JTextField idField, nameField, priceField, quantityField, supplierIDField;
    private JButton addButton, viewButton, updateButton, deleteButton;

    public ProductView(Database db) {
        this.db = db;

        // Set up the JFrame
        setTitle("Product View");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(7, 2));

        // Create input fields
        idField = new JTextField();
        nameField = new JTextField();
        priceField = new JTextField();
        quantityField = new JTextField();
        supplierIDField = new JTextField();

        // Create buttons
        addButton = new JButton("Add Product");
        viewButton = new JButton("View Product");
        updateButton = new JButton("Update Product");
        deleteButton = new JButton("Delete Product");

        // Add action listeners to buttons
        addButton.addActionListener(this);
        viewButton.addActionListener(this);
        updateButton.addActionListener(this);
        deleteButton.addActionListener(this);

        // Add components to the JFrame
        add(new JLabel("Product ID:"));
        add(idField);
        add(new JLabel("Name:"));
        add(nameField);
        add(new JLabel("Price:"));
        add(priceField);
        add(new JLabel("Quantity:"));
        add(quantityField);
        add(new JLabel("Supplier ID:"));
        add(supplierIDField);
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
            addProduct();
        } else if (e.getSource() == viewButton) {
            viewProduct();
        } else if (e.getSource() == updateButton) {
            updateProduct();
        } else if (e.getSource() == deleteButton) {
            deleteProduct();
        }
    }

    // Method to add a product
    private void addProduct() {
        ProductModel product = new ProductModel();
        product.productID = 0;
        product.name = nameField.getText();
        product.price = Float.parseFloat(priceField.getText());
        product.quantity = Integer.parseInt(quantityField.getText());
        product.supplierID = Integer.parseInt(supplierIDField.getText());
        db.createProduct(product);
        JOptionPane.showMessageDialog(this, "Product added successfully.");
    }

    // Method to view a product
    private void viewProduct() {
        int productID = Integer.parseInt(idField.getText());
        ProductModel product = db.readProduct(productID);
        if (product != null) {
            idField.setText(String.valueOf(product.productID));
            nameField.setText(product.name);
            priceField.setText(String.valueOf(product.price));
            quantityField.setText(String.valueOf(product.quantity));
            supplierIDField.setText(String.valueOf(product.supplierID));
        } else {
            JOptionPane.showMessageDialog(this, "Product not found.");
        }
    }

    // Method to update a product
    private void updateProduct() {
        int productID = Integer.parseInt(idField.getText());
        String name = nameField.getText();
        float price = Float.parseFloat(priceField.getText());
        int quantity = Integer.parseInt(quantityField.getText());
        int supplierID = Integer.parseInt(supplierIDField.getText());
        ProductModel product = new ProductModel();
        product.productID = productID;
        product.name = name;
        product.price = price;
        product.quantity = quantity;
        product.supplierID = supplierID;
        db.updateProduct(product);
        JOptionPane.showMessageDialog(this, "Product updated successfully.");
    }
    

    // Method to delete a product
    private void deleteProduct() {
        int productID = Integer.parseInt(idField.getText());
        db.deleteProduct(productID);
        JOptionPane.showMessageDialog(this, "Product deleted successfully.");
    }
}
