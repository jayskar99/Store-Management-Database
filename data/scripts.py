import csv
from faker import Faker
import random
import os
import re

fake = Faker()

# Function to generate random email
def generate_email():
    return fake.email()

# Generate random data for Suppliers table
suppliers_data = [["supplierID", "name", "email"]]
for i in range(1, 51):
    name = fake.company()
    email = generate_email()
    suppliers_data.append([i, name, email])

# Generate random data for SupplierPhones table
supplier_phones_data = [["number", "supplierID"]]
for i in range(1, 101):
    number = fake.unique.phone_number()
    supplier_id = random.randint(1,50)
    supplier_phones_data.append([number, supplier_id])

# Generate random data for Customers table
customers_data = [["customerID", "name", "email", "address"]]
for i in range(1, 101):
    name = fake.name()
    email = generate_email()
    address = fake.address()
    customers_data.append([i, name, email, address])

# Generate random data for CustomerPhones table
customer_phones_data = [["number", "customerID"]]
for i in range(1, 151):
    number = fake.unique.phone_number()
    customer_id = random.randint(1,100)
    customer_phones_data.append([number, i])

# Generate random data for Products table
products_data = [["productID", "name", "price", "quantity", "supplierID"]]
for i in range(1, 101):
    name = fake.word()
    price = round(random.uniform(1, 1000), 2)
    quantity = random.randint(1, 100)
    supplier_id = random.randint(1, 50)
    products_data.append([i, name, price, quantity, supplier_id])

# Generate random data for Orders table
orders_data = [["orderID", "dateTime", "customerID", "totalPrice"]]
for i in range(1, 1001):
    date_time = fake.date_time_this_year()
    customer_id = random.randint(1, 100)
    total_price = round(random.uniform(1, 1000), 2)
    orders_data.append([i, date_time, customer_id, total_price])

# Generate random data for OrderHistory table
order_history_data = [["orderHistoryID", "orderID", "quantity", "productID"]]
for i in range(1, 5001):
    order_id = random.randint(1, 1000)
    quantity = random.randint(1, 10)
    product_id = random.randint(1, 100)
    order_history_data.append([i, order_id, quantity, product_id])

# Generate random data for Payments table
payments_data = [["paymentID", "dateTime", "supplierID", "totalPrice"]]
for i in range(1, 101):
    date_time = fake.date_time_this_year()
    supplier_id = random.randint(1, 50)
    total_price = round(random.uniform(1, 1000), 2)
    payments_data.append([i, date_time, supplier_id, total_price])

# Generate random data for PaymentHistory table
payment_history_data = [["paymentHistoryID", "paymentID", "quantity", "productID"]]
for i in range(1, 501):
    payment_id = random.randint(1, 100)
    quantity = random.randint(1, 200)
    product_id = random.randint(1, 100)
    payment_history_data.append([i, payment_id, quantity, product_id])

# Write data to CSV files
def write_to_csv(filename, data):
    with open(filename, 'w', newline='') as csvfile:
        writer = csv.writer(csvfile)
        writer.writerows(data)

# Define output directory
output_dir = '/Users/jaydencox/projects/CSCE310/project1/data/'

# Write data to CSV files
write_to_csv(os.path.join(output_dir, 'suppliers.csv'), suppliers_data)
write_to_csv(os.path.join(output_dir, 'supplier_phones.csv'), supplier_phones_data)
write_to_csv(os.path.join(output_dir, 'customers.csv'), customers_data)
write_to_csv(os.path.join(output_dir, 'customer_phones.csv'), customer_phones_data)
write_to_csv(os.path.join(output_dir, 'products.csv'), products_data)
write_to_csv(os.path.join(output_dir, 'orders.csv'), orders_data)
write_to_csv(os.path.join(output_dir, 'order_history.csv'), order_history_data)
write_to_csv(os.path.join(output_dir, 'payments.csv'), payments_data)
write_to_csv(os.path.join(output_dir, 'payment_history.csv'), payment_history_data)
