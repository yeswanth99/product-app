-- Schema creation for the 'products' table
CREATE TABLE IF NOT EXISTS products (
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    category VARCHAR(100),
    price DOUBLE,
    discount_percentage DOUBLE,
    rating DOUBLE,
    stock INT,
    brand VARCHAR(100),
    sku VARCHAR(100),
    weight DOUBLE,
    width DOUBLE,
    height DOUBLE,
    depth DOUBLE,
    warranty_information TEXT,
    shipping_information TEXT,
    availability_status VARCHAR(100),
    return_policy TEXT,
    minimum_order_quantity INT,
    thumbnail VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    barcode VARCHAR(255),
    qr_code VARCHAR(255),
    UNIQUE (sku, title)
);

-- Create the 'product_reviews' table
CREATE TABLE IF NOT EXISTS product_reviews (
    id INT AUTO_INCREMENT PRIMARY KEY,
    rating INT,
    comment TEXT,
    date TIMESTAMP,
    reviewer_name VARCHAR(255),
    reviewer_email VARCHAR(255),
    product_id INT,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);

-- Schema creation for the 'product_images' table
CREATE TABLE IF NOT EXISTS product_images (
    id INT PRIMARY KEY AUTO_INCREMENT,
    product_id INT,
    image VARCHAR(255),
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);

-- Schema creation for the 'product_tags' table
CREATE TABLE IF NOT EXISTS product_tags (
    product_id INT,
    tags VARCHAR(255),
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);

-- Insert sample data into the 'products' table
INSERT INTO products (title, description, category, price, discount_percentage, rating, stock, brand, sku, weight, width, height, depth, warranty_information, shipping_information, availability_status, return_policy, minimum_order_quantity, thumbnail, barcode, qr_code)
VALUES
('Essence Mascara Lash Princess', 'The Essence Mascara Lash Princess is a popular mascara known for its volumizing and lengthening effects. Achieve dramatic lashes with this long-lasting and cruelty-free formula.', 'beauty', 9.99, 7.17, 4.94, 5, 'Essence', 'RCH45Q1A', 2.0, 23.17, 14.43, 28.01, '1 month warranty', 'Ships in 1 month', 'Low Stock', '30 days return policy', 24, 'thumbnail1.jpg', 'barcode1', 'qrcode1'),
('Eyeshadow Palette with Mirror', 'The Eyeshadow Palette with Mirror offers a versatile range of eyeshadow shades for creating stunning eye looks. With a built-in mirror, it\'s convenient for on-the-go makeup application.', 'beauty', 19.99, 5.5, 3.28, 44, 'Glamour Beauty', 'MVCFH27F', 3.0, 12.42, 8.63, 29.13, '1 year warranty', 'Ships in 2 weeks', 'In Stock', '30 days return policy', 32, 'thumbnail2.jpg', 'barcode2', 'qrcode2');

-- Insert sample data into the 'product_reviews' table
INSERT INTO product_reviews (rating, comment, date, reviewer_name, reviewer_email, product_id)
VALUES
(2, 'Very unhappy with my purchase!', '2024-05-23 08:56:21', 'John Doe', 'john.doe@x.dummyjson.com', 1),
(2, 'Not as described!', '2024-05-23 08:56:21', 'Nolan Gonzalez', 'nolan.gonzalez@x.dummyjson.com', 1),
(5, 'Very satisfied!', '2024-05-23 08:56:21', 'Scarlett Wright', 'scarlett.wright@x.dummyjson.com', 1),
(4, 'Very satisfied!', '2024-05-23 08:56:21', 'Liam Garcia', 'liam.garcia@x.dummyjson.com', 2),
(1, 'Very disappointed!', '2024-05-23 08:56:21', 'Nora Russell', 'nora.russell@x.dummyjson.com', 2),
(5, 'Highly impressed!', '2024-05-23 08:56:21', 'Elena Baker', 'elena.baker@x.dummyjson.com', 2);

-- Insert sample data into the 'product_images' table
INSERT INTO product_images (product_id, image)
VALUES
(1, 'image1.jpg'),
(1, 'image2.jpg'),
(2, 'image3.jpg'),
(2, 'image4.jpg');
