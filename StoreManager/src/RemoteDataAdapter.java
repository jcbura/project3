import com.google.gson.Gson;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.List;
public class RemoteDataAdapter implements DataAccess {
    Gson gson = new Gson();
    Socket s = null;
    DataInputStream dis = null;
    DataOutputStream dos = null;
    @Override
    public void connect(String url) {
        try {
            s = new Socket("localhost", 5056);
            dis = new DataInputStream(s.getInputStream());
            dos = new DataOutputStream(s.getOutputStream());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    @Override
    public void saveProduct(ProductModel product) {
        RequestModel req = new RequestModel();
        req.code = req.SAVE_PRODUCT_REQUEST;
        req.product = product;
        String json = gson.toJson(req);
        try {
            dos.writeUTF(json);
            String received = dis.readUTF();
            System.out.println("Server response:" + received);
            ResponseModel res = gson.fromJson(received, ResponseModel.class);
            if (res.code == ResponseModel.UNKNOWN_REQUEST) {
                System.out.println("The request is not recognized by the Server");
            }
            else         // this is a JSON string for a product information
                if (res.code == ResponseModel.DATA_NOT_FOUND) {
                    System.out.println("The Server could not find a product with that ID!");
                }
                else {
                    ProductModel model = gson.fromJson(res.body, ProductModel.class);
                    System.out.println("Receiving a ProductModel object");
                    System.out.println("ProductID = " + model.productID);
                    System.out.println("Product name = " + model.name);
                }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    @Override
    public ProductModel loadProduct(int productID) {
        RequestModel req = new RequestModel();
        req.code = req.LOAD_PRODUCT_REQUEST;
        req.body = String.valueOf(productID);
        String json = gson.toJson(req);
        try {
            dos.writeUTF(json);
            String received = dis.readUTF();
            System.out.println("Server response:" + received);
            ResponseModel res = gson.fromJson(received, ResponseModel.class);
            if (res.code == ResponseModel.UNKNOWN_REQUEST) {
                System.out.println("The request is not recognized by the Server");
                return null;
            }
            else         // this is a JSON string for a product information
                if (res.code == ResponseModel.DATA_NOT_FOUND) {
                    System.out.println("The Server could not find a product with that ID!");
                    return null;
                }
                else {
                    ProductModel model = gson.fromJson(res.body, ProductModel.class);
                    System.out.println("Receiving a ProductModel object");
                    System.out.println("ProductID = " + model.productID);
                    System.out.println("Product name = " + model.name);
                    return model; // found this product and return!!!
                }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
    @Override
    public UserModel loadUser(String username, String password) {
        RequestModel req = new RequestModel();
        req.code = req.USER_REQUEST;
        UserModel userModel = new UserModel();
        userModel.username = username;
        userModel.password = password;
        req.user = userModel;
        String json = gson.toJson(req);
        try {
            dos.writeUTF(json);
            String received = dis.readUTF();
            System.out.println("Server response:" + received);
            ResponseModel res = gson.fromJson(received, ResponseModel.class);
            if (res.code == ResponseModel.UNKNOWN_REQUEST) {
                System.out.println("The request is not recognized by the Server");
                return null;
            } else if (res.code == ResponseModel.DATA_NOT_FOUND) {
                System.out.println("The Server could not find a user with that username and password!");
                return null;
            } else {
                UserModel user = gson.fromJson(res.body, UserModel.class);
                System.out.println("Receiving a UserModel object");
                System.out.println("UserID = " + user.userID);
                System.out.println("UserName = " + user.username);
                return user;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
    @Override
    public void saveOrder(Order order) {
        RequestModel req = new RequestModel();
        req.code = req.ORDER_REQUEST;
        req.order = order;
        String json = gson.toJson(req);
        try {
            dos.writeUTF(json);
            String received = dis.readUTF();
            System.out.println("Server response:" + received);
            ResponseModel res = gson.fromJson(received, ResponseModel.class);
            if (res.code == ResponseModel.UNKNOWN_REQUEST) {
                System.out.println("The request is not recognized by the Server");
            } else if (res.code == ResponseModel.DATA_NOT_FOUND) {
                System.out.println("The Server could not find the order!");
            } else {
                System.out.println("Order saved successfully.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public List<ProductModel> loadAllProducts() {
        return null;
    }
    public UserModel loadUser(String username) {
        return null;
    }
    public Order loadOrder(int orderID) {
        return null;
    }
    public List<Order> loadAllOrders() {
        return null;
    }
    public List<UserModel> loadAllUsers() {
        return null;
    }
}