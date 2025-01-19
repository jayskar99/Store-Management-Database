import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

public class CustomerPhoneView extends JFrame implements ActionListener {
    private Database db;
    private int customerID;
    private JList<String> phoneList;
    private DefaultListModel<String> phoneListModel;
    private JButton addButton, updateButton, deleteButton;

    public CustomerPhoneView(Database db, int customerID) {
        this.db = db;
        this.customerID = customerID;

        setTitle("Customer Phone View");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        phoneListModel = new DefaultListModel<>();
        phoneList = new JList<>(phoneListModel);
        phoneList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION); // Set selection mode
        JScrollPane phoneScrollPane = new JScrollPane(phoneList);
        add(phoneScrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        addButton = new JButton("Add Phone");
        updateButton = new JButton("Update Phone");
        deleteButton = new JButton("Delete Phone");
        addButton.addActionListener(this);
        updateButton.addActionListener(this);
        deleteButton.addActionListener(this);
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);

        loadCustomerPhones();
        setVisible(true);
    }

    private void loadCustomerPhones() {
        phoneListModel.clear();
        Vector<String> phoneNumbers = db.readCustomerPhone(customerID);
        for (String phoneNumber : phoneNumbers) {
            phoneListModel.addElement(phoneNumber);
        }
    }    

    // Method to add a phone
    private void addPhone(String phoneNumber) {
        CustomerPhoneModel phone = new CustomerPhoneModel();
        phone.customerID = customerID;
        phone.number = phoneNumber;
        db.createCustomerPhone(phone);
        loadCustomerPhones();
    }

    // Method to update a phone
    private void updatePhone(String oldPhoneNumber, String newPhoneNumber) {
        CustomerPhoneModel phone = new CustomerPhoneModel();
        phone.customerID = customerID;
        phone.number = newPhoneNumber;
        db.updateCustomerPhone(phone, oldPhoneNumber);
        loadCustomerPhones();
    }

    // Method to delete a phone
    private void deletePhone(String phoneNumber) {
        db.deleteCustomerPhone(phoneNumber);
        loadCustomerPhones();
    }

    // Implement actionPerformed method to handle button clicks
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            String phoneNumber = JOptionPane.showInputDialog(this, "Enter phone number:");
            if (phoneNumber != null && !phoneNumber.isEmpty()) {
                addPhone(phoneNumber);
            }
        } else if (e.getSource() == updateButton) {
            String[] selectedPhoneNumbers = phoneList.getSelectedValuesList().toArray(new String[0]);
            if (selectedPhoneNumbers.length > 0) {
                String newPhoneNumber = JOptionPane.showInputDialog(this, "Enter new phone number:");
                if (newPhoneNumber != null && !newPhoneNumber.isEmpty()) {
                    for (String selectedPhoneNumber : selectedPhoneNumbers) {
                        updatePhone(selectedPhoneNumber, newPhoneNumber);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select phone number(s) to update.");
            }
        } else if (e.getSource() == deleteButton) {
            String[] selectedPhoneNumbers = phoneList.getSelectedValuesList().toArray(new String[0]);
            if (selectedPhoneNumbers.length > 0) {
                int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete selected phone number(s)?");
                if (confirm == JOptionPane.YES_OPTION) {
                    for (String selectedPhoneNumber : selectedPhoneNumbers) {
                        deletePhone(selectedPhoneNumber);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select phone number(s) to delete.");
            }
        }
    }
}
