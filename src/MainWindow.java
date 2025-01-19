import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainWindow extends JFrame implements ActionListener {
    private Database db;

    public MainWindow(Database db) {
        this.db = db;

        // Set up the JFrame
        setTitle("Main Window");
        setSize(200, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(6,1));

        // Create buttons
        JButton customerButton = new JButton("Customer View");
        JButton orderButton = new JButton("Order View");
        JButton productButton = new JButton("Product View");
        JButton paymentButton = new JButton("Payment View");
        JButton supplierButton = new JButton("Supplier View");
        JButton reportsButton = new JButton("Reports View");

        // Add action listeners to buttons
        customerButton.addActionListener(this);
        orderButton.addActionListener(this);
        productButton.addActionListener(this);
        paymentButton.addActionListener(this);
        supplierButton.addActionListener(this);
        reportsButton.addActionListener(this);

        // Add buttons to the JFrame
        add(customerButton);
        add(orderButton);
        add(productButton);
        add(paymentButton);
        add(supplierButton);
        add(reportsButton);

        // Display the JFrame
        setVisible(true);
    }

    // ActionPerformed method for button clicks
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Customer View")) {
            new CustomerView(db);
        } else if (e.getActionCommand().equals("Order View")) {
            new OrderView(db);
        } else if (e.getActionCommand().equals("Product View")) {
            new ProductView(db);
        } else if (e.getActionCommand().equals("Payment View")) {
            new PaymentView(db);
        } else if (e.getActionCommand().equals("Supplier View")) {
            new SupplierView(db);
        } else if (e.getActionCommand().equals("Reports View")) {
            new ReportsView(db);
        }
    }
}
