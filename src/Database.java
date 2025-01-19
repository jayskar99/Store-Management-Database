import java.sql.*;
import java.util.Vector;

public class Database {
    public Connection conn = connect();

    public Connection connect() {
        Connection newConnection = null;
        try {
            String url = "jdbc:sqlite:project1.db";
            newConnection = DriverManager.getConnection(url);
            System.out.print("connected!\n");
        } catch (SQLException sqle) {
            System.out.print("not connected!\n");
            System.err.println(sqle);
            System.exit(1);
        }
        return newConnection;
    }

    public void disconnect() {
        try {
            conn.close();
            System.out.print("disconnected!\n");
        } catch (SQLException sqle) {
            System.out.print("not disconnected!\n");
            System.err.println(sqle);
            System.exit(1);
        }
    }


    // ---------------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------------
    // CRUD for +CustomerPhones
    public void createCustomerPhone(CustomerPhoneModel customerPhone) {
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO CustomerPhones(number, customerID) VALUES (?, ?)");
            stmt.setString(1, customerPhone.number);
            stmt.setInt(2, customerPhone.customerID);
            stmt.executeUpdate();
            System.out.println("Customer phone created");
        } catch (SQLException e) {
            System.out.println("Failed to create customer phone");
            e.printStackTrace();
        }
    }

    public Vector<String> readCustomerPhone(int customerID) {
        Vector<String> phoneNumbers = new Vector<>();
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT number FROM CustomerPhones WHERE customerID = ?");
            stmt.setInt(1, customerID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                phoneNumbers.add(rs.getString("number"));
            }
            System.out.println("Customer phones read");
        } catch (SQLException e) {
            System.out.println("Failed to read customer phones");
            e.printStackTrace();
        }
        return phoneNumbers;
    }

    public void updateCustomerPhone(CustomerPhoneModel customerPhone, String oldNumber) {
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE CustomerPhones SET number = ? WHERE customerID = ? AND number = ?");
            stmt.setString(1, customerPhone.number);
            stmt.setInt(2, customerPhone.customerID);
            stmt.setString(3, oldNumber);
            stmt.executeUpdate();
            System.out.println("Customer phone updated");
        } catch (SQLException e) {
            System.out.println("Failed to update customer phone");
            e.printStackTrace();
        }
    }

    public void deleteCustomerPhone(String number) {
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM CustomerPhones WHERE number = ?");
            stmt.setString(1, number);
            stmt.executeUpdate();
            System.out.println("Customer phone deleted");
        } catch (SQLException e) {
            System.out.println("Failed to delete customer phone");
            e.printStackTrace();
        }
    }

    public void fixCustomerPhone(int customerID) {
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM CustomerPhones WHERE customerID = ?");
            stmt.setInt(1, customerID);
            stmt.executeUpdate();
            System.out.println("Customer phone fixed");
        } catch (SQLException e) {
            System.out.println("Failed to fix customer phone");
            e.printStackTrace();
        }
    }


    // ---------------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------------
    // CRUD for +Customers
    public void createCustomer(CustomerModel customer) {
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO Customers(name, email, address) VALUES (?, ?, ?)");
            stmt.setString(1, customer.name);
            stmt.setString(2, customer.email);
            stmt.setString(3, customer.address);
            stmt.executeUpdate();
            System.out.println("Customer created");
        } catch (SQLException e) {
            System.out.println("Failed to create customer");
            e.printStackTrace();
        }
    }

    public CustomerModel readCustomer(int customerID) {
        CustomerModel customer = new CustomerModel();
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM Customers WHERE customerID = ?");
            stmt.setInt(1, customerID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                customer.customerID = rs.getInt("customerID");
                customer.name = rs.getString("name");
                customer.email = rs.getString("email");
                customer.address = rs.getString("address");
            }
            customer.numbers = readCustomerPhone(customerID);
            System.out.println("Customer read");
        } catch (SQLException e) {
            System.out.println("Failed to read customer");
            e.printStackTrace();
        }
        return customer;
    }

    public void updateCustomer(CustomerModel customer) {
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE Customers SET name = ?, email = ?, address = ? WHERE customerID = ?");
            stmt.setString(1, customer.name);
            stmt.setString(2, customer.email);
            stmt.setString(3, customer.address);
            stmt.setInt(4, customer.customerID);
            stmt.executeUpdate();
            System.out.println("Customer updated");
        } catch (SQLException e) {
            System.out.println("Failed to update customer");
            e.printStackTrace();
        }
    }

    public void deleteCustomer(int customerID) {
        try {
            fixCustomerPhone(customerID);
            PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM Customers WHERE customerID = ?");
            stmt.setInt(1, customerID);
            stmt.executeUpdate();
            System.out.println("Customer deleted");
        } catch (SQLException e) {
            System.out.println("Failed to delete customer");
            e.printStackTrace();
        }
    }


    // ---------------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------------
    // CRUD for +OrderHistory
    public void createOrderHistory(OrderHistoryModel orderHistory) {
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO OrderHistory(orderID, quantity, productID) VALUES (?, ?, ?)");
            stmt.setInt(1, orderHistory.orderID);
            stmt.setInt(2, orderHistory.quantity);
            stmt.setInt(3, orderHistory.productID);
            stmt.executeUpdate();
            updateOrderTotalPrice(orderHistory.orderID);
            System.out.println("Order history created");
        } catch (SQLException e) {
            System.out.println("Failed to create order history");
            e.printStackTrace();
        }
    }

    public OrderHistoryModel readOrderHistory(int orderHistoryID) {
        OrderHistoryModel orderHistory = new OrderHistoryModel();
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM OrderHistory WHERE orderHistoryID = ?");
            stmt.setInt(1, orderHistoryID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                orderHistory.orderHistoryID = rs.getInt("orderHistoryID");
                orderHistory.orderID = rs.getInt("orderID");
                orderHistory.quantity = rs.getInt("quantity");
                orderHistory.productID = rs.getInt("productID");
                System.out.println("Order history read");
            }
        } catch (SQLException e) {
            System.out.println("Failed to read order history");
            e.printStackTrace();
        }
        return orderHistory;
    }

    public void updateOrderHistory(OrderHistoryModel orderHistory) {
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE OrderHistory SET orderID = ?, quantity = ?, productID = ? WHERE orderHistoryID = ?");
            stmt.setInt(1, orderHistory.orderID);
            stmt.setInt(2, orderHistory.quantity);
            stmt.setInt(3, orderHistory.productID);
            stmt.setInt(4, orderHistory.orderHistoryID);
            stmt.executeUpdate();
            updateOrderTotalPrice(orderHistory.orderID);
            System.out.println("Order history updated");
        } catch (SQLException e) {
            System.out.println("Failed to update order history");
            e.printStackTrace();
        }
    }

    public void deleteOrderHistory(int orderHistoryID) {
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM OrderHistory WHERE orderHistoryID = ?");
            stmt.setInt(1, orderHistoryID);
            stmt.executeUpdate();
            OrderHistoryModel orderHistory = readOrderHistory(orderHistoryID);
            updateOrderTotalPrice(orderHistory.orderID);
            System.out.println("Order history deleted");
        } catch (SQLException e) {
            System.out.println("Failed to delete order history");
            e.printStackTrace();
        }
    }

    public void fixOrderHistory(int orderID) {
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM OrderHistory WHERE orderID = ?");
            stmt.setInt(1, orderID);
            stmt.executeUpdate();
            System.out.println("Order history fixed");
        } catch (SQLException e) {
            System.out.println("Failed to fix order history");
            e.printStackTrace();
        }
    }

    public Vector<OrderHistoryModel> getOrderHistory(int orderID) {
        Vector<OrderHistoryModel> orderHistoryVector = new Vector<>();
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(
                "SELECT * FROM OrderHistory WHERE orderID = ?"
            );
            preparedStatement.setInt(1, orderID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                OrderHistoryModel orderHistory = new OrderHistoryModel();
                orderHistory.orderHistoryID = resultSet.getInt("orderHistoryID");
                orderHistory.orderID = resultSet.getInt("orderID");
                orderHistory.quantity = resultSet.getInt("quantity");
                orderHistory.productID = resultSet.getInt("productID");
                orderHistoryVector.add(orderHistory);
            }
            System.out.println("Order history got");
        } catch (SQLException e) {
            System.out.println("Failed to get order history");
            e.printStackTrace();
        }
        return orderHistoryVector;
    }


    // ---------------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------------
    // CRUD for +Orders
    public void createOrder(OrderModel order) {
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO Orders(dateTime, customerID, totalPrice) VALUES (?, ?, ?)");
            stmt.setString(1, order.dateTime);
            stmt.setInt(2, order.customerID);
            stmt.setFloat(3, order.totalPrice);
            stmt.executeUpdate();
            System.out.println("Order created");
        } catch (SQLException e) {
            System.out.println("Failed to create order");
            e.printStackTrace();
        }
    }

    public OrderModel readOrder(int orderID) {
        OrderModel order = new OrderModel();
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM Orders WHERE orderID = ?");
            stmt.setInt(1, orderID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                order.orderID = rs.getInt("orderID");
                order.dateTime = rs.getString("dateTime");
                order.customerID = rs.getInt("customerID");
                order.totalPrice = rs.getFloat("totalPrice");
                System.out.println("Order read");
            }
        } catch (SQLException e) {
            System.out.println("Failed to read order");
            e.printStackTrace();
        }
        return order;
    }

    public void updateOrder(OrderModel order) {
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE Orders SET dateTime = ?, customerID = ?, totalPrice = ? WHERE orderID = ?");
            stmt.setString(1, order.dateTime);
            stmt.setInt(2, order.customerID);
            stmt.setFloat(3, order.totalPrice);
            stmt.setInt(4, order.orderID);
            stmt.executeUpdate();
            System.out.println("Order updated");
        } catch (SQLException e) {
            System.out.println("Failed to update order");
            e.printStackTrace();
        }
    }

    public void deleteOrder(int orderID) {
        try {
            fixOrderHistory(orderID);
            PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM Orders WHERE orderID = ?");
            stmt.setInt(1, orderID);
            stmt.executeUpdate();
            System.out.println("Order deleted");
        } catch (SQLException e) {
            System.out.println("Failed to delete order");
            e.printStackTrace();
        }
    }

    public void updateOrderTotalPrice(int orderID) {
        try {
            String sql = "UPDATE Orders " +
                         "SET totalPrice = ( " +
                         "    SELECT SUM(oh.quantity * p.price) " +
                         "    FROM OrderHistory oh " +
                         "    INNER JOIN Products p ON oh.productID = p.productID " +
                         "    WHERE oh.orderID = Orders.orderID " +
                         ") " +
                         "WHERE orderID = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, orderID);
            stmt.executeUpdate();
            System.out.println("Order totalPrice updated");
        } catch (SQLException e) {
            System.out.println("Failed to update order totalPrice");
            e.printStackTrace();
        }
    }


    // ---------------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------------
    // CRUD for +PaymentHistory
    public void createPaymentHistory(PaymentHistoryModel paymentHistory) {
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO PaymentHistory(paymentID, quantity, productID) VALUES (?, ?, ?)");
            stmt.setInt(1, paymentHistory.paymentID);
            stmt.setInt(2, paymentHistory.quantity);
            stmt.setInt(3, paymentHistory.productID);
            stmt.executeUpdate();
            updatePaymentTotalPrice(paymentHistory.paymentID);
            System.out.println("Payment history created");
        } catch (SQLException e) {
            System.out.println("Failed to create payment history");
            e.printStackTrace();
        }
    }

    public PaymentHistoryModel readPaymentHistory(int paymentHistoryID) {
        PaymentHistoryModel paymentHistory = new PaymentHistoryModel();
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM PaymentHistory WHERE paymentHistoryID = ?");
            stmt.setInt(1, paymentHistoryID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                paymentHistory.paymentHistoryID = rs.getInt("paymentHistoryID");
                paymentHistory.paymentID = rs.getInt("paymentID");
                paymentHistory.quantity = rs.getInt("quantity");
                paymentHistory.productID = rs.getInt("productID");
                System.out.println("Payment history read");
            }
        } catch (SQLException e) {
            System.out.println("Failed to read payment history");
            e.printStackTrace();
        }
        return paymentHistory;
    }

    public void updatePaymentHistory(PaymentHistoryModel paymentHistory) {
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE PaymentHistory SET paymentID = ?, quantity = ?, productID = ? WHERE paymentHistoryID = ?");
            stmt.setInt(1, paymentHistory.paymentID);
            stmt.setInt(2, paymentHistory.quantity);
            stmt.setInt(3, paymentHistory.productID);
            stmt.setInt(4, paymentHistory.paymentHistoryID);
            stmt.executeUpdate();
            updatePaymentTotalPrice(paymentHistory.paymentID);
            System.out.println("Payment history updated");
        } catch (SQLException e) {
            System.out.println("Failed to update payment history");
            e.printStackTrace();
        }
    }

    public void deletePaymentHistory(int paymentHistoryID) {
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM PaymentHistory WHERE paymentHistoryID = ?");
            stmt.setInt(1, paymentHistoryID);
            stmt.executeUpdate();
            PaymentHistoryModel paymentHistory = readPaymentHistory(paymentHistoryID);
            updatePaymentTotalPrice(paymentHistory.paymentID);
            System.out.println("Payment history deleted");
        } catch (SQLException e) {
            System.out.println("Failed to delete payment history");
            e.printStackTrace();
        }
    }

    public void fixPaymentHistory(int paymentID) {
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM PaymentHistory WHERE paymentID = ?");
            stmt.setInt(1, paymentID);
            stmt.executeUpdate();
            System.out.println("Payment history fixed");
        } catch (SQLException e) {
            System.out.println("Failed to fix payment history");
            e.printStackTrace();
        }
    }

    public Vector<PaymentHistoryModel> getPaymentHistory(int paymentID) {
        Vector<PaymentHistoryModel> paymentHistoryList = new Vector<>();
        try {
            PreparedStatement statement = conn.prepareStatement(
                "SELECT * FROM PaymentHistory WHERE paymentID = ?"
            );
            statement.setInt(1, paymentID);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                PaymentHistoryModel paymentHistory = new PaymentHistoryModel();
                paymentHistory.paymentHistoryID = resultSet.getInt("paymentHistoryID");
                paymentHistory.paymentID = resultSet.getInt("paymentID");
                paymentHistory.quantity = resultSet.getInt("quantity");
                paymentHistory.productID = resultSet.getInt("productID");
                paymentHistoryList.add(paymentHistory);
            }
            System.out.println("Payment history got");
        } catch (SQLException e) {
            System.out.println("Failed to get payment history");
            e.printStackTrace();
        }
        return paymentHistoryList;
    }




    // ---------------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------------
    // CRUD for +Payments
    public void createPayment(PaymentModel payment) {
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO Payments(dateTime, supplierID, totalPrice) VALUES (?, ?, ?)");
            stmt.setString(1, payment.dateTime);
            stmt.setInt(2, payment.supplierID);
            stmt.setFloat(3, 0f);
            stmt.executeUpdate();
            System.out.println("Payment created");
        } catch (SQLException e) {
            System.out.println("Failed to create payment");
            e.printStackTrace();
        }
    }

    public PaymentModel readPayment(int paymentID) {
        PaymentModel payment = new PaymentModel();
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM Payments WHERE paymentID = ?");
            stmt.setInt(1, paymentID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                payment.paymentID = rs.getInt("paymentID");
                payment.dateTime = rs.getString("dateTime");
                payment.supplierID = rs.getInt("supplierID");
                payment.totalPrice = rs.getFloat("totalPrice");
                System.out.println("Payment read");
            }
        } catch (SQLException e) {
            System.out.println("Failed to read payment");
            e.printStackTrace();
        }
        return payment;
    }

    public void updatePayment(PaymentModel payment) {
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE Payments SET dateTime = ?, supplierID = ?, totalPrice = ? WHERE paymentID = ?");
            stmt.setString(1, payment.dateTime);
            stmt.setInt(2, payment.supplierID);
            stmt.setFloat(3, payment.totalPrice);
            stmt.setInt(4, payment.paymentID);
            stmt.executeUpdate();
            System.out.println("Payment updated");
        } catch (SQLException e) {
            System.out.println("Failed to update payment");
            e.printStackTrace();
        }
    }

    public void deletePayment(int paymentID) {
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM Payments WHERE paymentID = ?");
            stmt.setInt(1, paymentID);
            stmt.executeUpdate();
            updatePaymentTotalPrice(paymentID);
            System.out.println("Payment deleted");
        } catch (SQLException e) {
            System.out.println("Failed to delete payment");
            e.printStackTrace();
        }
    }
    
    public void updatePaymentTotalPrice(int paymentID) {
        try {
            String sql = "UPDATE Payments " +
                         "SET totalPrice = ( " +
                         "    SELECT SUM(ph.quantity * p.price) " +
                         "    FROM PaymentHistory ph " +
                         "    INNER JOIN Products p ON ph.productID = p.productID " +
                         "    WHERE ph.paymentID = Payments.paymentID " +
                         ") " +
                         "WHERE paymentID = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, paymentID);
            stmt.executeUpdate();
            System.out.println("Payment totalPrice updated");
        } catch (SQLException e) {
            System.out.println("Failed to update payment totalPrice");
            e.printStackTrace();
        }
    }


    // ---------------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------------
    // CRUD for +Product
    public void createProduct(ProductModel product) {
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO Products(name, price, quantity, supplierID) VALUES (?, ?, ?, ?)");
            stmt.setString(1, product.name);
            stmt.setFloat(2, product.price);
            stmt.setInt(3, product.quantity);
            stmt.setInt(4, product.supplierID);
            stmt.executeUpdate();
            System.out.println("Product created");
        } catch (SQLException e) {
            System.out.println("Failed to create product");
            e.printStackTrace();
        }
    }

    public ProductModel readProduct(int productID) {
        ProductModel product = new ProductModel();
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM Products WHERE productID = ?");
            stmt.setInt(1, productID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                product.productID = rs.getInt("productID");
                product.name = rs.getString("name");
                product.price = rs.getFloat("price");
                product.quantity = rs.getInt("quantity");
                product.supplierID = rs.getInt("supplierID");
                System.out.println("Product read");
            }
        } catch (SQLException e) {
            System.out.println("Failed to read product");
            e.printStackTrace();
        }
        return product;
    }

    public void updateProduct(ProductModel product) {
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE Products SET name = ?, price = ?, quantity = ?, supplierID = ? WHERE productID = ?");
            stmt.setString(1, product.name);
            stmt.setFloat(2, product.price);
            stmt.setInt(3, product.quantity);
            stmt.setInt(4, product.supplierID);
            stmt.setInt(5, product.productID);
            stmt.executeUpdate();
            System.out.println("Product updated");
        } catch (SQLException e) {
            System.out.println("Failed to update product");
            e.printStackTrace();
        }
    }

    public void deleteProduct(int productID) {
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM Products WHERE productID = ?");
            stmt.setInt(1, productID);
            stmt.executeUpdate();
            System.out.println("Product deleted");
        } catch (SQLException e) {
            System.out.println("Failed to delete product");
            e.printStackTrace();
        }
    }


    // ---------------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------------
    // CRUD for +SupplierPhones
    public void createSupplierPhone(SupplierPhoneModel supplierPhone) {
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO SupplierPhones(number, supplierID) VALUES (?, ?)");
            stmt.setString(1, supplierPhone.number);
            stmt.setInt(2, supplierPhone.supplierID);
            stmt.executeUpdate();
            System.out.println("Supplier phone created");
        } catch (SQLException e) {
            System.out.println("Failed to create supplier phone");
            e.printStackTrace();
        }
    }

    public Vector<String> readSupplierPhone(int supplierID) {
        Vector<String> phoneNumbers = new Vector<>();
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT number FROM SupplierPhones WHERE supplierID = ?");
            stmt.setInt(1, supplierID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                phoneNumbers.add(rs.getString("number"));
            }
            System.out.println("Supplier phones read");
        } catch (SQLException e) {
            System.out.println("Failed to read supplier phones");
            e.printStackTrace();
        }
        return phoneNumbers;
    }

    public void updateSupplierPhone(SupplierPhoneModel supplierPhone, String oldPhone) {
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE SupplierPhones SET number = ? WHERE supplierID = ? AND number = ?");
            stmt.setString(1, supplierPhone.number);
            stmt.setInt(2, supplierPhone.supplierID);
            stmt.setString(3, oldPhone);
            stmt.executeUpdate();
            System.out.println("Supplier phone updated");
        } catch (SQLException e) {
            System.out.println("Failed to update supplier phone");
            e.printStackTrace();
        }
    }

    public void deleteSupplierPhone(String number) {
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM SupplierPhones WHERE number = ?");
            stmt.setString(1, number);
            stmt.executeUpdate();
            System.out.println("Supplier phone deleted");
        } catch (SQLException e) {
            System.out.println("Failed to delete supplier phone");
            e.printStackTrace();
        }
    }

    public void fixSupplierPhone(int supplierID) {
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM SupplierPhones WHERE supplierID = ?");
            stmt.setInt(1, supplierID);
            stmt.executeUpdate();
            System.out.println("Supplier phone fixed");
        } catch (SQLException e) {
            System.out.println("Failed to fix supplier phone");
            e.printStackTrace();
        }
    }


    // ---------------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------------
    // CRUD for +Suppliers
    public void createSupplier(SupplierModel supplier) {
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO Suppliers(name, email) VALUES (?, ?)");
            stmt.setString(1, supplier.name);
            stmt.setString(2, supplier.email);
            stmt.executeUpdate();
            System.out.println("Supplier created");
        } catch (SQLException e) {
            System.out.println("Failed to create supplier");
            e.printStackTrace();
        }
    }

    public SupplierModel readSupplier(int supplierID) {
        SupplierModel supplier = new SupplierModel();
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM Suppliers WHERE supplierID = ?");
            stmt.setInt(1, supplierID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                supplier.supplierID = rs.getInt("supplierID");
                supplier.name = rs.getString("name");
                supplier.email = rs.getString("email");
            }
            supplier.numbers = readSupplierPhone(supplierID);
            System.out.println("Supplier read");
        } catch (SQLException e) {
            System.out.println("Failed to read supplier");
            e.printStackTrace();
        }
        return supplier;
    }

    public void updateSupplier(SupplierModel supplier) {
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE Suppliers SET name = ?, email = ? WHERE supplierID = ?");
            stmt.setString(1, supplier.name);
            stmt.setString(2, supplier.email);
            stmt.setInt(3, supplier.supplierID);
            stmt.executeUpdate();
            System.out.println("Supplier updated");
        } catch (SQLException e) {
            System.out.println("Failed to update supplier");
            e.printStackTrace();
        }
    }

    public void deleteSupplier(int supplierID) {
        try {
            fixSupplierPhone(supplierID);
            PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM Suppliers WHERE supplierID = ?");
            stmt.setInt(1, supplierID);
            stmt.executeUpdate();
            System.out.println("Supplier deleted");
        } catch (SQLException e) {
            System.out.println("Failed to delete supplier");
            e.printStackTrace();
        }
    }


    // ---------------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------------
    // Reports
    public String generateTotalSalePerMonthReport(String startDate, String endDate, boolean descending, int numEntries) {
        StringBuilder reportBuilder = new StringBuilder();
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT strftime('%Y-%m', Orders.dateTime) AS month_year, SUM(Orders.totalPrice) AS total_sale " +
                            "FROM Orders " +
                            "WHERE Orders.dateTime BETWEEN ? AND ? " +
                            "GROUP BY strftime('%Y-%m', Orders.dateTime) " +
                            "ORDER BY total_sale " + (descending ? "DESC" : "ASC") +
                            " LIMIT ?");
            stmt.setString(1, startDate);
            stmt.setString(2, endDate);
            stmt.setInt(3, numEntries);
            ResultSet rs = stmt.executeQuery();

            reportBuilder.append("Total Sale Per Month Report\n");
            reportBuilder.append("Period: ").append(startDate).append(" to ").append(endDate).append("\n\n");
            reportBuilder.append("Month\t\tTotal Sale\n");

            while (rs.next()) {
                String monthYear = rs.getString("month_year");
                float totalSale = rs.getFloat("total_sale");
                reportBuilder.append(monthYear).append("\t\t").append(totalSale).append("\n");
            }
            System.out.println("Per month report generated");
        } catch (SQLException e) {
            System.out.println("Failed to generate per month report");
            e.printStackTrace();
        }
        return reportBuilder.toString();
    }

    public String generateTotalSalePerProductReport(String startDate, String endDate, boolean descending, int numEntries) {
        StringBuilder reportBuilder = new StringBuilder();
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT OrderHistory.productID, SUM(Orders.totalPrice) AS total_sale " +
                            "FROM Orders " +
                            "JOIN OrderHistory ON Orders.orderID = OrderHistory.orderID " +
                            "WHERE Orders.dateTime BETWEEN ? AND ? " +
                            "GROUP BY OrderHistory.productID " +
                            "ORDER BY total_sale " + (descending ? "DESC" : "ASC") +
                            " LIMIT ?");
            stmt.setString(1, startDate);
            stmt.setString(2, endDate);
            stmt.setInt(3, numEntries);
            ResultSet rs = stmt.executeQuery();

            reportBuilder.append("Total Sale Per Product Report\n");
            reportBuilder.append("Period: ").append(startDate).append(" to ").append(endDate).append("\n\n");
            reportBuilder.append("Product ID\t\tTotal Sale\n");

            while (rs.next()) {
                int productID = rs.getInt("productID");
                float totalSale = rs.getFloat("total_sale");
                reportBuilder.append(productID).append("\t\t").append(totalSale).append("\n");
            }
            System.out.println("Per product report generated");
        } catch (SQLException e) {
            System.out.println("Failed to generate per product report");
            e.printStackTrace();
        }
        return reportBuilder.toString();
    }

    public String generateTotalSalePerCustomerReport(String startDate, String endDate, boolean descending, int numEntries) {
        StringBuilder reportBuilder = new StringBuilder();
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT Orders.customerID, SUM(Orders.totalPrice) AS total_sale " +
                            "FROM Orders " +
                            "WHERE Orders.dateTime BETWEEN ? AND ? " +
                            "GROUP BY Orders.customerID " +
                            "ORDER BY total_sale " + (descending ? "DESC" : "ASC") +
                            " LIMIT ?");
            stmt.setString(1, startDate);
            stmt.setString(2, endDate);
            stmt.setInt(3, numEntries);
            ResultSet rs = stmt.executeQuery();

            reportBuilder.append("Total Sale Per Customer Report\n");
            reportBuilder.append("Period: ").append(startDate).append(" to ").append(endDate).append("\n\n");
            reportBuilder.append("Customer ID\t\tTotal Sale\n");

            while (rs.next()) {
                int customerID = rs.getInt("customerID");
                float totalSale = rs.getFloat("total_sale");
                reportBuilder.append(customerID).append("\t\t").append(totalSale).append("\n");
            }
            System.out.println("Per customer report generated");
        } catch (SQLException e) {
            System.out.println("Failed to generate per customer report");
            e.printStackTrace();
        }
        return reportBuilder.toString();
    }
}
