import java.util.List;
public interface DataAccess {
    void connect(String url);
    void saveProduct(ProductModel product);
    ProductModel loadProduct(int productID);
    UserModel loadUser(String username, String password);
    void saveOrder(Order order);
    List<ProductModel> loadAllProducts();
    UserModel loadUser(String username);
    Order loadOrder(int orderID);
    List<Order> loadAllOrders();
    List<UserModel> loadAllUsers();
}