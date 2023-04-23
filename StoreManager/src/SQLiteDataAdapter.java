import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class SQLiteDataAdapter implements DataAccess {
    Connection conn = null;
    @Override
    public void connect(String url) {
        try {
            url = "jdbc:sqlite:store.db";
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(url);
            if (conn == null)
                System.out.println("Cannot make the connection!!!");
            else
                System.out.println("The connection object is " + conn);

            System.out.println("Connection to SQLite has been established.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    @Override
    public void saveProduct(ProductModel product) {
        try {
            Statement stmt = conn.createStatement();
            if (loadProduct(product.productID) == null) {           // this is a new product!
                stmt.execute("INSERT INTO Product(productID, name, price, quantity) VALUES ("
                        + product.productID + ","
                        + '\'' + product.name + '\'' + ","
                        + product.price + ","
                        + product.quantity + ")"
                );
            }
            else {
                stmt.executeUpdate("UPDATE Product SET "
                        + "productID = " + product.productID + ","
                        + "name = " + '\'' + product.name + '\'' + ","
                        + "price = " + product.price + ","
                        + "quantity = " + product.quantity +
                        " WHERE productID = " + product.productID
                );
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    @Override
    public ProductModel loadProduct(int productID) {
        ProductModel product = null;
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Product WHERE ProductID = " + productID);
            if (rs.next()) {
                product = new ProductModel();
                product.productID = rs.getInt(1);
                product.name = rs.getString(2);
                product.price = rs.getDouble(3);
                product.quantity = rs.getDouble(4);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return product;
    }
    @Override
    public UserModel loadUser(String username, String password) {
        UserModel user = null;
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM User WHERE UserName = ? AND Password = ?");
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                user = new UserModel();
                user.userID = rs.getInt("UserID");
                user.username = rs.getString("UserName");
                user.password = rs.getString("Password");
                user.displayName = rs.getString("DisplayName");
                user.isSeller = rs.getBoolean("IsSeller");
                user.isBuyer = rs.getBoolean("IsBuyer");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return user;
    }
    @Override
    public void saveOrder(Order order) {
        try {
            PreparedStatement statement = conn.prepareStatement("INSERT INTO Orders (OrderDate, Customer, TotalCost, TotalTax) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            statement.setDate(1, new Date(order.date.getTime()));
            statement.setString(2, order.customerName);
            statement.setDouble(3, order.totalCost);
            statement.setDouble(4, order.totalTax);
            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            int orderID = 0;
            if (generatedKeys.next()) {
                orderID = generatedKeys.getInt(1);
            }
            generatedKeys.close();
            statement.close();
            statement = conn.prepareStatement("INSERT INTO OrderLine (OrderID, ProductID, Quantity, Cost) VALUES (?, ?, ?, ?)");
            for (OrderLine line : order.lines) {
                statement.setInt(1, orderID);
                statement.setInt(2, line.productID);
                statement.setDouble(3, line.quantity);
                statement.setDouble(4, line.cost);
                statement.executeUpdate();
            }
            statement.close();
        } catch (Exception e) {
            System.out.println("Database access error!");
            e.printStackTrace();
        }
    }
    @Override
    public List<ProductModel> loadAllProducts() {
        List<ProductModel> list = new ArrayList<>();
        ProductModel product = null;
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Product ");
            while (rs.next()) {
                product = new ProductModel();
                product.productID = rs.getInt(1);
                product.name = rs.getString(2);
                product.price = rs.getDouble(3);
                product.quantity = rs.getDouble(4);
                list.add(product);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }
    @Override
    public UserModel loadUser(String username) {
        UserModel user = null;
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM User WHERE UserName = ?");
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                user = new UserModel();
                user.userID = rs.getInt("UserID");
                user.username = rs.getString("UserName");
                user.password = rs.getString("Password");
                user.displayName = rs.getString("DisplayName");
                user.isSeller = rs.getBoolean("IsSeller");
                user.isBuyer = rs.getBoolean("IsBuyer");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return user;
    }
    @Override
    public Order loadOrder(int orderID) {
        Order order = null;
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Orders WHERE OrderID = ?");
            stmt.setInt(1, orderID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                order = new Order();
                order.setOrderID(rs.getInt("OrderID"));
                order.setDate(rs.getDate("OrderDate"));
                order.setCustomerName(rs.getString("Customer"));
                order.setTotalCost(rs.getDouble("TotalCost"));
                order.setTotalTax(rs.getDouble("TotalTax"));

                PreparedStatement stmt2 = conn.prepareStatement("SELECT * FROM OrderLine WHERE OrderID = ?");
                stmt2.setInt(1, orderID);
                ResultSet rs2 = stmt2.executeQuery();
                while (rs2.next()) {
                    OrderLine line = new OrderLine();
                    line.orderID = rs2.getInt("OrderID");
                    line.productID = rs2.getInt("ProductID");
                    line.quantity = rs2.getInt("Quantity");
                    line.cost = rs2.getDouble("Cost");
                    order.addLine(line);
                }
                rs2.close();
                stmt2.close();
            }
            rs.close();
            stmt.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return order;
    }
    @Override
    public List<Order> loadAllOrders() {
        List<Order> orders = new ArrayList<>();
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Orders");
            while (rs.next()) {
                Order order = new Order();
                order.setOrderID(rs.getInt("OrderID"));
                order.setDate(rs.getDate("OrderDate"));
                order.setCustomerName(rs.getString("Customer"));
                order.setTotalCost(rs.getDouble("TotalCost"));
                order.setTotalTax(rs.getDouble("TotalTax"));

                PreparedStatement stmt2 = conn.prepareStatement("SELECT * FROM OrderLine WHERE OrderID = ?");
                stmt2.setInt(1, order.getOrderID());
                ResultSet rs2 = stmt2.executeQuery();
                while (rs2.next()) {
                    OrderLine line = new OrderLine();
                    line.orderID = rs2.getInt("OrderID");
                    line.productID = rs2.getInt("ProductID");
                    line.quantity = rs2.getInt("Quantity");
                    line.cost = rs2.getDouble("Cost");
                    order.addLine(line);
                }
                rs2.close();
                stmt2.close();

                orders.add(order);
            }
            rs.close();
            stmt.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return orders;
    }
    @Override
    public List<UserModel> loadAllUsers() {
        List<UserModel> users = new ArrayList<>();
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM User");
            while (rs.next()) {
                UserModel user = new UserModel();
                user.userID = rs.getInt("UserID");
                user.username = rs.getString("UserName");
                user.password = rs.getString("Password");
                user.displayName = rs.getString("DisplayName");
                user.isSeller = rs.getBoolean("IsSeller");
                user.isBuyer = rs.getBoolean("IsBuyer");
                users.add(user);
            }
            rs.close();
            stmt.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return users;
    }
}