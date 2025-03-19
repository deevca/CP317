// Source code is decompiled from a .class file using FernFlower decompiler.
public class Product {
   private int productid;
   private String productName;
   private String description;
   private double price;
   private int quantity;
   private char status;
   private int supplierid;

   public Product(int var1, String var2, String var3, double var4, int var6, char var7, int var8) {
      this.productid = var1;
      this.productName = var2;
      this.description = var3;
      this.price = var4;
      this.quantity = var6;
      this.status = var7;
      this.supplierid = var8;
   }

   public Product() {
      this.productid = 0;
      this.productName = "";
      this.description = "";
      this.price = 0.0;
      this.quantity = 0;
      this.status = ' ';
      this.supplierid = 0;
   }

   public boolean validate() throws Exception {
      if (this.productid <= 0) {
         throw new Exception("Product ID must be a positive number");
      } else if (this.productName != null && !this.productName.trim().isEmpty()) {
         if (this.price <= 0.0) {
            throw new Exception("Product price must be positive");
         } else if (this.quantity < 0) {
            throw new Exception("Product quantity cannot be negative");
         } else if (this.status != 'A' && this.status != 'B' && this.status != 'C') {
            throw new Exception("Product quality must be A, B, or C");
         } else if (this.supplierid <= 0) {
            throw new Exception("Supplier ID must be a positive number");
         } else {
            return true;
         }
      } else {
         throw new Exception("Product type cannot be empty");
      }
   }

   public int getProductid() {
      return this.productid;
   }

   public void setProductid(int var1) {
      this.productid = var1;
   }

   public String getProductName() {
      return this.productName;
   }

   public void setProductName(String var1) {
      this.productName = var1;
   }

   public String getDescription() {
      return this.description;
   }

   public void setDescription(String var1) {
      this.description = var1;
   }

   public double getPrice() {
      return this.price;
   }

   public void setPrice(double var1) {
      this.price = var1;
   }

   public int getQuantity() {
      return this.quantity;
   }

   public void setQuantity(int var1) {
      this.quantity = var1;
   }

   public char getStatus() {
      return this.status;
   }

   public void setStatus(char var1) {
      this.status = var1;
   }

   public int getSupplierid() {
      return this.supplierid;
   }

   public void setSupplierid(int var1) {
      this.supplierid = var1;
   }
}
