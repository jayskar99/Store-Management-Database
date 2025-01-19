import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ReportsView extends JFrame {
    private Database db;
    private JTextField startDateField, endDateField, numEntriesField;
    private JCheckBox descendingCheckBox, saveReportCheckBox;
    private JButton monthReportButton, productReportButton, customerReportButton;

    public ReportsView(Database db) {
        this.db = db;

        setTitle("Generate Reports");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(6, 2));

        JLabel startDateLabel = new JLabel("Start Date (YYYY-MM-DD):");
        startDateField = new JTextField();
        add(startDateLabel);
        add(startDateField);

        JLabel endDateLabel = new JLabel("End Date (YYYY-MM-DD):");
        endDateField = new JTextField();
        add(endDateLabel);
        add(endDateField);

        JLabel numEntriesLabel = new JLabel("Number of Entries:");
        numEntriesField = new JTextField("10");
        add(numEntriesLabel);
        add(numEntriesField);

        descendingCheckBox = new JCheckBox("Descending");
        add(descendingCheckBox);

        saveReportCheckBox = new JCheckBox("Save Report");
        add(saveReportCheckBox);

        monthReportButton = new JButton("Generate Per Month Report");
        productReportButton = new JButton("Generate Per Product Report");
        customerReportButton = new JButton("Generate Per Customer Report");

        add(monthReportButton);
        add(productReportButton);
        add(customerReportButton);

        // Add action listeners to buttons
        monthReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateTotalSalePerMonthReport();
            }
        });

        productReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateTotalSalePerProductReport();
            }
        });

        customerReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateTotalSalePerCustomerReport();
            }
        });

        setVisible(true);
    }

    private void generateTotalSalePerMonthReport() {
        String startDate = startDateField.getText();
        String endDate = endDateField.getText();
        boolean descending = descendingCheckBox.isSelected();
        int numEntries = Integer.parseInt(numEntriesField.getText());
        boolean saveReport = saveReportCheckBox.isSelected();

        String report = db.generateTotalSalePerMonthReport(startDate, endDate, descending, numEntries);
        if (saveReport) {
            // Save report to a file
            saveReportToFile(report);
            JOptionPane.showMessageDialog(this, "Report saved to file.", "Report Saved", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, report, "Total Sale Per Month Report", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void generateTotalSalePerProductReport() {
        String startDate = startDateField.getText();
        String endDate = endDateField.getText();
        boolean descending = descendingCheckBox.isSelected();
        int numEntries = Integer.parseInt(numEntriesField.getText());
        boolean saveReport = saveReportCheckBox.isSelected();

        String report = db.generateTotalSalePerProductReport(startDate, endDate, descending, numEntries);
        if (saveReport) {
            // Save report to a file
            saveReportToFile(report);
            JOptionPane.showMessageDialog(this, "Report saved to file.", "Report Saved", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, report, "Total Sale Per Product Report", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void generateTotalSalePerCustomerReport() {
        String startDate = startDateField.getText();
        String endDate = endDateField.getText();
        boolean descending = descendingCheckBox.isSelected();
        int numEntries = Integer.parseInt(numEntriesField.getText());
        boolean saveReport = saveReportCheckBox.isSelected();

        String report = db.generateTotalSalePerCustomerReport(startDate, endDate, descending, numEntries);
        if (saveReport) {
            // Save report to a file
            saveReportToFile(report);
            JOptionPane.showMessageDialog(this, "Report saved to file.", "Report Saved", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, report, "Total Sale Per Customer Report", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void saveReportToFile(String report) {
        try {
            // Specify the file path where you want to save the report
            String filePath = "/Users/jaydencox/Downloads/report.txt";
            
            // Create a FileWriter object with the specified file path
            FileWriter fileWriter = new FileWriter(filePath);
            
            // Create a BufferedWriter object to write to the file
            BufferedWriter writer = new BufferedWriter(fileWriter);
            
            // Write the report content to the file
            writer.write(report);
            
            // Close the BufferedWriter
            writer.close();
            
            // Show a message dialog indicating that the report has been saved
            JOptionPane.showMessageDialog(this, "Report saved to file: " + filePath, "Report Saved", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle IOException
            JOptionPane.showMessageDialog(this, "Error occurred while saving report.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
