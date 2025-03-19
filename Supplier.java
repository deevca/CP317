// Supplier class to store supplier company information
public class Supplier {
    private String supplierName;
    private int supplierid;
    private String address;  // Fixed: removed duplicate "String"
    private String phone;    // Fixed: removed duplicate "String"
    private String email;

    // Constructor to initialize the supplier object with parameters
    public Supplier(String Name, int id, String Address, String Phone, String Email) {
        this.supplierName = Name;
        this.supplierid = id;
        this.address = Address;
        this.phone = Phone;
        this.email = Email;
    }

    // Default constructor
    public Supplier() {
        this.supplierName = "";
        this.supplierid = 0;
        this.address = "";
        this.phone = "";
        this.email = "";
    }

    // Validates Supplier Data
    /** 
     * Validates the supplier data
     * @return true if data is valid, otherwise false
     * @throws Exception if data cannot be validated
     */
    public boolean validate() throws Exception {
        // Validate supplier data
        if (supplierid <= 0) {
            throw new Exception("Supplier ID must be a positive number");
        }
        
        if (supplierName == null || supplierName.trim().isEmpty()) {
            throw new Exception("Supplier name cannot be empty");
        }
        
        // Validate email format
        if (email == null || !email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new Exception("Invalid email format: " + email);
        }
        
        // Validate phone format (allowing various formats)
        if (phone == null || !phone.matches("^[0-9\\-\\+\\.\\s\\(\\)]+$")) {
            throw new Exception("Invalid phone format: " + phone);
        }
        
        return true;
    }

    // Getters and Setters
    public void setSupplierid(int supplierid) {
        this.supplierid = supplierid;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getSupplierid() {
        return supplierid;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }
}
