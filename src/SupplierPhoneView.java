import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

public class SupplierPhoneView extends JFrame implements ActionListener {
    private Database db;
    private int supplierID;
    private JList<String> phoneList;
    private DefaultListModel<String> phoneListModel;
    private JButton addButton, updateButton, deleteButton;

    public SupplierPhoneView(Database db, int supplierID) {
        this.db = db;
        this.supplierID = supplierID;

        setTitle("Supplier Phone View");
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

        loadSupplierPhones();
        setVisible(true);
    }

    private void loadSupplierPhones() {
        phoneListModel.clear();
        Vector<String> phoneNumbers = db.readSupplierPhone(supplierID);
        for (String phoneNumber : phoneNumbers) {
            phoneListModel.addElement(phoneNumber);
        }
    }    

    // Method to add a phone
    private void addPhone(String phoneNumber) {
        SupplierPhoneModel phone = new SupplierPhoneModel();
        phone.supplierID = supplierID;
        phone.number = phoneNumber;
        db.createSupplierPhone(phone);
        loadSupplierPhones();
    }

    // Method to update a phone
    private void updatePhone(String oldPhoneNumber, String newPhoneNumber) {
        SupplierPhoneModel phone = new SupplierPhoneModel();
        phone.supplierID = supplierID;
        phone.number = newPhoneNumber;
        db.updateSupplierPhone(phone, oldPhoneNumber);
        loadSupplierPhones();
    }

    // Method to delete a phone
    private void deletePhone(String phoneNumber) {
        db.deleteSupplierPhone(phoneNumber);
        loadSupplierPhones();
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
