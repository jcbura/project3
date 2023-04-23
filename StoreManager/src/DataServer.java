import java.io.*;
import java.net.*;
import com.google.gson.Gson;
public class DataServer
{
    public static void main(String[] args) throws IOException
    {
        ServerSocket ss = new ServerSocket(5056);
        System.out.println("Starting server program!!!");
        int nClients = 0;
        while (true)
        {
            Socket s = null;
            try
            {
                s = ss.accept();
                nClients++;
                System.out.println("A new client is connected : " + s + " client number: " + nClients);
                DataInputStream dis = new DataInputStream(s.getInputStream());
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                System.out.println("Assigning new thread for this client");
                Thread t = new ClientHandler(s, dis, dos);
                t.start();
            }
            catch (Exception e){
                s.close();
                e.printStackTrace();
            }
        }
    }
}
class ClientHandler extends Thread
{
    final DataInputStream dis;
    final DataOutputStream dos;
    final Socket s;
    Gson gson = new Gson();
    DataAccess dao = new SQLiteDataAdapter();
    public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos)
    {
        this.s = s;
        this.dis = dis;
        this.dos = dos;
        dao.connect("");
    }
    @Override
    public void run()
    {
        String received;
        while (true) {
            try {
                received = dis.readUTF();
                System.out.println("Message from client " + received);
                RequestModel req = gson.fromJson(received, RequestModel.class);
                if (req.code == RequestModel.EXIT_REQUEST) {
                    System.out.println("Client " + this.s + " sends exit...");
                    System.out.println("Closing this connection.");
                    this.s.close();
                    System.out.println("Connection closed");
                    break;
                }
                ResponseModel res = new ResponseModel();
                if (req.code == RequestModel.LOAD_PRODUCT_REQUEST) {
                    int id = Integer.parseInt(req.body);
                    System.out.println("The Client asks for a product with ID = " + id);
                    ProductModel model = dao.loadProduct(id);
                    if (model != null) {
                        res.code = ResponseModel.OK;
                        res.body = gson.toJson(model);
                    }
                    else {
                        res.code = ResponseModel.DATA_NOT_FOUND;
                        res.body = "";
                    }
                }
                else if (req.code == RequestModel.SAVE_PRODUCT_REQUEST) {
                    ProductModel model = req.product;
                    dao.saveProduct(model);
                    if (model != null) {
                        res.code = ResponseModel.OK;
                        res.body = gson.toJson(model);
                    }
                    else {
                        res.code = ResponseModel.DATA_NOT_FOUND;
                        res.body = "";
                    }
                }
                else if (req.code == RequestModel.USER_REQUEST) {
                    String username = req.user.username;
                    String password = req.user.password;
                    System.out.println("The Client asks for a user with username = " + username + " and password = " + password);
                    UserModel user = dao.loadUser(username, password);
                    if (user != null) {
                        res.code = ResponseModel.OK;
                        res.body = gson.toJson(user);
                    } else {
                        res.code = ResponseModel.DATA_NOT_FOUND;
                        res.body = "";
                    }
                }
                else if (req.code == RequestModel.ORDER_REQUEST) {
                    Order order = req.order;
                    dao.saveOrder(order);
                    res.code = ResponseModel.OK;
                    res.body = "";
                }
                else {
                    res.code = ResponseModel.UNKNOWN_REQUEST;
                    res.body = "";
                }
                String json = gson.toJson(res);
                System.out.println("JSON object of ResponseModel: " + json);
                dos.writeUTF(json);
                dos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try
        {
            this.dis.close();
            this.dos.close();

        } catch(IOException e){
            e.printStackTrace();
        }
    }
}