import java.io.*;
import java.util.*;

/**
 * Fileio class is used to read and write files
 */
public class fileio {
    /**
     * Reads supplier data from txt file
     * @param filename The name of the file to read
     * @return Inventory object containing supplier data
     * @throws Exception if file cannot be read
     */
    public static Inventory readSupplierFile(String filename) throws Exception {
        Inventory inventory = new Inventory();
       
        // Verify file is text file
        if (!filename.toLowerCase().endsWith(".txt")) {
            throw new Exception("Unsupported file type. Must be a text file (.txt).");
        }

        // Check if file exists 
        File file = new File(filename);
        if (!file.exists()) {
            throw new Exception("Supplier file not found: " + filename);
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            int lineNumber = 0;
            int suppliersAdded = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;      
                // Skip empty lines
                if (line.trim().isEmpty()) {
                    continue;
                }

                try {
                    // Split by comma and trim each part
                    String[] parts = line.split(",");

                    if (parts.length < 5) {
                        System.out.println("Warning: Skipping invalid supplier at line " + lineNumber);
                        continue;
                    }

                    // Trim each part to remove extra spaces
                    String idStr = parts[0].trim();
                    String name = parts[1].trim();
                    String address = parts[2].trim();
                    String phone = parts[3].trim();
                    String email = parts[4].trim();

                    // Validate ID format
                    try {
                        int id = Integer.parseInt(idStr);
                        Supplier supplier = new Supplier(name, id, address, phone, email);

                        // Validate supplier data
                        try {
                            supplier.validate();
                            inventory.addSupplier(supplier);
                            suppliersAdded++;
                        } catch (Exception e) {
                            System.out.println("Warning: Skipping line. Invalid supplier data at line " + lineNumber + ": " + e.getMessage());
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Warning: Skipping line. Invalid supplier ID at line " + lineNumber + ": " + idStr + " - " + e.getMessage()); 
                    }              
                } catch (Exception e) {
                    // Catch any exception extra and continue
                    System.out.println("Warning: Skipping line. Error processing supplier at line " + lineNumber + ": " + e.getMessage()); 
                }
            }

            System.out.println("Read " + suppliersAdded + " suppliers from " + filename);

        } catch (IOException e) {
            throw new Exception("Error reading supplier file: " + e.getMessage());
        }

        return inventory;
    }

    /**
     * Reads product data from txt file
     * @param filename the name of product file to read
     * @param inventory the inventory to add products to
     * @throws Exception if file there is an error reading file
     */
    public static void readProductFile(String filename, Inventory inventory) throws Exception {
        // Verify file is text file
        if (!filename.toLowerCase().endsWith(".txt")) {
            throw new Exception("Unsupported file type. Must be a text file (.txt).");
        }

        // Check if file exists
        File file = new File(filename);
        if (!file.exists()) {
            throw new Exception("Product file not found: " + filename);
        }

        int productsAdded = 0;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;

                // Skip empty lines
                if (line.trim().isEmpty()) {
                    continue;
                }

                try {
                    // Split by comma
                    String[] parts = line.split(",", 7);  

                    // Validate number all 7 fields of product data
                    if (parts.length < 7) {
                        System.out.println("Warning: Skipping invalid line at line " + lineNumber + ". Expected 7 fields, found " + parts.length);
                        continue;
                    }

                    try {
                        // Parse each field, trimming any spaces
                        int id = Integer.parseInt(parts[0].trim());
                        String productName = parts[1].trim();   // (Category)
                        String description = parts[2].trim();   // (Description)   

                        // Handle price with dollar sign
                        String priceStr = parts[3].trim().replace("$", "");
                        double price = Double.parseDouble(priceStr);

                        int quantity = Integer.parseInt(parts[4].trim());
                        char status = parts[5].trim().charAt(0);
                        int supplierid = Integer.parseInt(parts[6].trim());

                        Product product = new Product(id, productName, description, price, quantity, status, supplierid);  

                        // Validate product data
                        try { 
                            product.validate();
                            inventory.addProduct(product);
                            productsAdded++;
                        } catch (Exception e) {
                            System.out.println("Warning: Skipping line. Invalid product data at line " + lineNumber + ": " + e.getMessage());
                        }     
                    } catch (NumberFormatException e) {
                        System.out.println("Warning: Skipping line. Invalid product ID at line " + lineNumber + ": " + e.getMessage());
                    } catch (IndexOutOfBoundsException e) {
                        System.out.println("Warning: Skipping line. Invalid product data at line " + lineNumber + ": " + e.getMessage());
                    }
                } catch (Exception e) {
                    System.out.println("Warning: Skipping line. Error processing product at line " + lineNumber + ": " + e.getMessage());
                }
            }
            
            System.out.println("Product data loaded successfully from " + filename + ". " + productsAdded + " products added.");
            
        } catch (IOException e) {
            throw new Exception("Error reading product file: " + e.getMessage());
        }
    }

    /**
     * Writes the inventory data to a text file in the required format
     * @param filename The name of the file to write
     * @param inventory The inventory containing products and suppliers
     * @throws Exception If there is an error writing the file
     */
    public static void writeInventoryFile(String filename, Inventory inventory) throws Exception {
        // Verify file extension
        if (!filename.toLowerCase().endsWith(".txt")) {
            System.err.println("Error: Unsupported output file format. Only .txt files are supported.");
            filename = filename + ".txt";
            System.out.println("Using modified output filename: " + filename);
        }
        
        try {
            // Get valid products that have existing suppliers
            List<Product> validProducts = inventory.getValidProducts();
            
            // Sort products by ID as required by project spec
            validProducts.sort(Comparator.comparingInt(Product::getProductid));
            
            // Create the writer
            try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
                // Write header row
                writer.printf("%-10s %-15s %-10s %-10s %-10s %-20s\n", 
                             "Product ID", "Product Name", "Quantity", "Price", "Status", "Supplier name");
                writer.println("-------------------------------------------------------------------------");
                
                // Write product data
                for (Product product : validProducts) {
                    // Get the supplier for this product
                    Supplier supplier = inventory.getSupplierById(product.getSupplierid());
                    String supplierName = (supplier != null) ? supplier.getSupplierName() : "Unknown";
                    
                    writer.printf("%-10d %-15s %-10d %-10.1f %-10c %-20s\n", 
                                 product.getProductid(), 
                                 limitString(product.getProductName(), 15), 
                                 product.getQuantity(), 
                                 product.getPrice(), 
                                 product.getStatus(),
                                 limitString(supplierName, 20));
                }
            }
            
            System.out.println("Successfully wrote " + validProducts.size() + " products to " + filename);
            
        } catch (IOException e) {
            throw new Exception("Error writing inventory file: " + e.getMessage());
        }
    }
    
    /**
     * Helper method to limit string length for formatting
     * @param input The string to limit
     * @param maxLength The maximum length of the string
     * @return The limited string
     */
    private static String limitString(String input, int maxLength) {
        if (input == null) {
            return "";
        }
        return input.length() <= maxLength ? input : input.substring(0, maxLength - 3) + "...";
    }   
}
