import com.hp.gagawa.java.elements.*;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.util.List;
public class WebServer {
    //http://localhost:8500
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8500), 0);
        HttpContext root = server.createContext("/");
        root.setHandler(WebServer::handleRequest);
        HttpContext context = server.createContext("/users");
        context.setHandler(WebServer::handleRequestUser);
        HttpContext product = server.createContext("/products");
        product.setHandler(WebServer::handleRequestProduct);
        HttpContext order = server.createContext("/orders");
        order.setHandler(WebServer::handleRequestOrder);
        HttpContext allusers = server.createContext("/users/all");
        allusers.setHandler(WebServer::handleRequestAllUsers);
        HttpContext allproducts = server.createContext("/products/all");
        allproducts.setHandler(WebServer::handleRequestAllProducts);
        HttpContext allorders = server.createContext("/orders/all");
        allorders.setHandler(WebServer::handleRequestAllOrders);
        server.start();
    }
    private static void handleRequest(HttpExchange exchange) throws IOException {
        Html html = new Html();
        Head head = new Head();
        html.appendChild( head );
        Title title = new Title();
        title.appendChild( new Text("Online shopping web server") );
        head.appendChild( title );
        Body body = new Body();
        H1 h1 = new H1();
        h1.appendChild(new Text("Online Shopping Web Server Page"));
        body.appendChild(h1);
        P timePara = new P();
        timePara.appendText("Server time: " + LocalDateTime.now());
        body.appendChild(timePara);
        P productsPara = new P();
        A productslink = new A("/products/all", "/products/all");
        productslink.appendText("Product list");
        productsPara.appendChild(productslink);
        body.appendChild(productsPara);
        P ordersPara = new P();
        A ordersLink = new A("/orders/all", "/orders/all");
        ordersLink.appendText("Order list");
        ordersPara.appendChild(ordersLink);
        body.appendChild(ordersPara);
        P usersPara = new P();
        A usersLink = new A("/users/all", "/users/all");
        usersLink.appendText("User list");
        usersPara.appendChild(usersLink);
        body.appendChild(usersPara);
        html.appendChild( body );
        String response = html.write();
        exchange.sendResponseHeaders(200, response.getBytes().length);//response code and length
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
    private static void handleRequestAllUsers(HttpExchange exchange) throws IOException {
        String url = "jdbc:sqlite:store.db";
        SQLiteDataAdapter dao = new SQLiteDataAdapter();
        dao.connect(url);
        List<UserModel> list = dao.loadAllUsers();
        Html html = new Html();
        Head head = new Head();
        html.appendChild(head);
        Title title = new Title();
        title.appendChild(new Text("All Users Page Title"));
        head.appendChild(title);
        Body body = new Body();
        html.appendChild(body);
        H1 h1 = new H1();
        h1.appendChild(new Text("All Users Page"));
        body.appendChild(h1);
        P para = new P();
        para.appendChild(new Text("The server time is " + LocalDateTime.now()));
        body.appendChild(para);
        para = new P();
        para.appendChild(new Text("The server has " + list.size() + " users."));
        body.appendChild(para);
        Table table = new Table();
        Tr row = new Tr();
        Th header = new Th();
        header.appendText("UserID");
        row.appendChild(header);
        header = new Th();
        header.appendText("Username");
        row.appendChild(header);
        header = new Th();
        header.appendText("Password");
        row.appendChild(header);
        header = new Th();
        header.appendText("DisplayName");
        row.appendChild(header);
        header = new Th();
        header.appendText("IsSeller");
        row.appendChild(header);
        header = new Th();
        header.appendText("IsBuyer");
        row.appendChild(header);
        table.appendChild(row);
        for (UserModel user : list) {
            row = new Tr();
            Td cell = new Td();
            cell.appendText(String.valueOf(user.userID));
            row.appendChild(cell);
            cell = new Td();
            cell.appendText(user.username);
            row.appendChild(cell);
            cell = new Td();
            cell.appendText(user.password);
            row.appendChild(cell);
            cell = new Td();
            cell.appendText(user.displayName);
            row.appendChild(cell);
            cell = new Td();
            cell.appendText(String.valueOf(user.isSeller));
            row.appendChild(cell);
            cell = new Td();
            cell.appendText(String.valueOf(user.isBuyer));
            row.appendChild(cell);
            table.appendChild(row);
        }
        table.setBorder("1");
        html.appendChild(table);
        String response = html.write();
        System.out.println(response);
        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
    private static void handleRequestUser(HttpExchange exchange) throws IOException {
        String uri =  exchange.getRequestURI().toString();
        String username = uri.substring(uri.lastIndexOf('/')+1);
        System.out.println(uri);
        String url = "jdbc:sqlite:store.db";
        SQLiteDataAdapter dao = new SQLiteDataAdapter();
        dao.connect(url);
        Html html = new Html();
        Head head = new Head();
        html.appendChild( head );
        Title title = new Title();
        title.appendChild( new Text("User Page Title") );
        head.appendChild( title );
        Body body = new Body();
        html.appendChild( body );
        H1 h1 = new H1();
        h1.appendChild( new Text("User Page") );
        body.appendChild( h1 );
        P para = new P();
        para.appendChild( new Text("The server time is " + LocalDateTime.now()) );
        body.appendChild(para);
        UserModel user = dao.loadUser(username);
        if (user != null) {
            para = new P();
            para.appendText("UserID: " + user.userID);
            html.appendChild(para);
            para = new P();
            para.appendText("Username: " + user.username);
            html.appendChild(para);
            para = new P();
            para.appendText("Password: " + user.password);
            html.appendChild(para);
            para = new P();
            para.appendText("DisplayName: " + user.displayName);
            html.appendChild(para);
            para = new P();
            para.appendText("IsSeller: " + user.isSeller);
            html.appendChild(para);
            para = new P();
            para.appendText("IsBuyer: " + user.isBuyer);
            html.appendChild(para);
        }
        else {
            para = new P();
            para.appendText("User not found");
            html.appendChild(para);
        }
        String response = html.write();
        System.out.println(response);
        exchange.sendResponseHeaders(200, response.getBytes().length);//response code and length
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
    private static void handleRequestAllProducts(HttpExchange exchange) throws IOException {
        //String response =  "This simple web server is designed with help from ChatGPT!";
        String url = "jdbc:sqlite:store.db";
        SQLiteDataAdapter dao = new SQLiteDataAdapter();
        dao.connect(url);
        List<ProductModel> list = dao.loadAllProducts();
        Html html = new Html();
        Head head = new Head();
        html.appendChild( head );
        Title title = new Title();
        title.appendChild( new Text("All Products Page Title") );
        head.appendChild( title );
        Body body = new Body();
        html.appendChild( body );
        H1 h1 = new H1();
        h1.appendChild( new Text("All Products Page") );
        body.appendChild( h1 );
        P para = new P();
        para.appendChild( new Text("The server time is " + LocalDateTime.now()) );
        body.appendChild(para);
        para = new P();
        para.appendChild( new Text("The server has " + list.size() + " products." ));
        body.appendChild(para);
        Table table = new Table();
        Tr row = new Tr();
        Th header = new Th(); header.appendText("ProductID"); row.appendChild(header);
        header = new Th(); header.appendText("Product name"); row.appendChild(header);
        header = new Th(); header.appendText("Price"); row.appendChild(header);
        header = new Th(); header.appendText("Quantity"); row.appendChild(header);
        table.appendChild(row);
        for (ProductModel product : list) {
            row = new Tr();
            Td cell = new Td(); cell.appendText(String.valueOf(product.productID)); row.appendChild(cell);
            cell = new Td(); cell.appendText(product.name); row.appendChild(cell);
            cell = new Td(); cell.appendText(String.valueOf(product.price)); row.appendChild(cell);
            cell = new Td(); cell.appendText(String.valueOf(product.quantity)); row.appendChild(cell);
            table.appendChild(row);
        }
        table.setBorder("1");
        html.appendChild(table);
        String response = html.write();
        System.out.println(response);
        exchange.sendResponseHeaders(200, response.getBytes().length);//response code and length
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
    private static void handleRequestProduct(HttpExchange exchange) throws IOException {
        String uri =  exchange.getRequestURI().toString();
        int id = Integer.parseInt(uri.substring(uri.lastIndexOf('/')+1));
        System.out.println(id);
        String url = "jdbc:sqlite:store.db";
        SQLiteDataAdapter dao = new SQLiteDataAdapter();
        dao.connect(url);
        Html html = new Html();
        Head head = new Head();
        html.appendChild( head );
        Title title = new Title();
        title.appendChild( new Text("Product Page Title") );
        head.appendChild( title );
        Body body = new Body();
        html.appendChild( body );
        H1 h1 = new H1();
        h1.appendChild( new Text("Product Page") );
        body.appendChild( h1 );
        P para = new P();
        para.appendChild( new Text("The server time is " + LocalDateTime.now()) );
        body.appendChild(para);
        ProductModel product = dao.loadProduct(id);
        if (product != null) {
            para = new P();
            para.appendText("ProductID: " + product.productID);
            html.appendChild(para);
            para = new P();
            para.appendText("Product name: " + product.name);
            html.appendChild(para);
            para = new P();
            para.appendText("Price: " + product.price);
            html.appendChild(para);
            para = new P();
            para.appendText("Quantity: " + product.quantity);
            html.appendChild(para);
        }
        else {
            para = new P();
            para.appendText("Product not found");
            html.appendChild(para);
        }
        String response = html.write();
        System.out.println(response);
        exchange.sendResponseHeaders(200, response.getBytes().length);//response code and length
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
    private static void handleRequestAllOrders(HttpExchange exchange) throws IOException {
        String url = "jdbc:sqlite:store.db";
        SQLiteDataAdapter dao = new SQLiteDataAdapter();
        dao.connect(url);
        List<Order> list = dao.loadAllOrders();
        Html html = new Html();
        Head head = new Head();
        html.appendChild(head);
        Title title = new Title();
        title.appendChild(new Text("All Orders Page Title"));
        head.appendChild(title);
        Body body = new Body();
        html.appendChild(body);
        H1 h1 = new H1();
        h1.appendChild(new Text("All Orders Page"));
        body.appendChild(h1);
        P para = new P();
        para.appendChild(new Text("The server time is " + LocalDateTime.now()));
        body.appendChild(para);
        para = new P();
        para.appendChild(new Text("The server has " + list.size() + " orders."));
        body.appendChild(para);
        Table table = new Table();
        Tr row = new Tr();
        Th header = new Th(); header.appendText("OrderID"); row.appendChild(header);
        header = new Th(); header.appendText("OrderDate"); row.appendChild(header);
        header = new Th(); header.appendText("Customer"); row.appendChild(header);
        header = new Th(); header.appendText("TotalCost"); row.appendChild(header);
        header = new Th(); header.appendText("TotalTax"); row.appendChild(header);
        table.appendChild(row);
        for (Order order : list) {
            row = new Tr();
            Td cell = new Td(); cell.appendText(String.valueOf(order.getOrderID())); row.appendChild(cell);
            cell = new Td(); cell.appendText(String.valueOf(order.getDate())); row.appendChild(cell);
            cell = new Td(); cell.appendText(order.getCustomerName()); row.appendChild(cell);
            cell = new Td(); cell.appendText(String.valueOf(order.getTotalCost())); row.appendChild(cell);
            cell = new Td(); cell.appendText(String.valueOf(order.getTotalTax())); row.appendChild(cell);
            table.appendChild(row);
        }
        table.setBorder("1");
        html.appendChild(table);
        String response = html.write();
        System.out.println(response);
        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
    private static void handleRequestOrder(HttpExchange exchange) throws IOException {
        String uri = exchange.getRequestURI().toString();
        int orderID = Integer.parseInt(uri.substring(uri.lastIndexOf('/') + 1));
        System.out.println(uri);
        String url = "jdbc:sqlite:store.db";
        SQLiteDataAdapter dao = new SQLiteDataAdapter();
        dao.connect(url);
        Html html = new Html();
        Head head = new Head();
        html.appendChild(head);
        Title title = new Title();
        title.appendChild(new Text("Order Page Title"));
        head.appendChild(title);
        Body body = new Body();
        html.appendChild(body);
        H1 h1 = new H1();
        h1.appendChild(new Text("Order Page"));
        body.appendChild(h1);
        P para = new P();
        para.appendChild(new Text("The server time is " + LocalDateTime.now()));
        body.appendChild(para);
        Order order = dao.loadOrder(orderID);
        if (order != null) {
            para = new P();
            para.appendText("OrderID: " + order.getOrderID());
            html.appendChild(para);
            para = new P();
            para.appendText("OrderDate: " + order.getDate());
            html.appendChild(para);
            para = new P();
            para.appendText("Customer: " + order.getCustomerName());
            html.appendChild(para);
            para = new P();
            para.appendText("TotalCost: " + order.getTotalCost());
            html.appendChild(para);
            para = new P();
            para.appendText("TotalTax: " + order.getTotalTax());
            html.appendChild(para);
            para = new P();
            if (order.getLines().size() == 1) {
                para.appendText("The order has " + order.getLines().size() + " line.");
            } else {
                para.appendText("The order has " + order.getLines().size() + " lines.");
            }
            html.appendChild(para);
            Table table = new Table();
            Tr row = new Tr();
            Th header = new Th(); header.appendText("OrderID"); row.appendChild(header);
            header = new Th(); header.appendText("ProductID"); row.appendChild(header);
            header = new Th(); header.appendText("Quantity"); row.appendChild(header);
            header = new Th(); header.appendText("Cost"); row.appendChild(header);
            table.appendChild(row);
            for (OrderLine line : order.getLines()) {
                row = new Tr();
                Td cell = new Td(); cell.appendText(String.valueOf(line.orderID)); row.appendChild(cell);
                cell = new Td(); cell.appendText(String.valueOf(line.productID)); row.appendChild(cell);
                cell = new Td(); cell.appendText(String.valueOf(line.quantity)); row.appendChild(cell);
                cell = new Td(); cell.appendText(String.valueOf(line.cost)); row.appendChild(cell);
                table.appendChild(row);
            }
            table.setBorder("1");
            html.appendChild(table);
        } else {
            para = new P();
            para.appendText("Order not found");
            html.appendChild(para);
        }
        String response = html.write();
        System.out.println(response);
        exchange.sendResponseHeaders(200, response.getBytes().length); // response code and length
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}