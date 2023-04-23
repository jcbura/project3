import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.util.List;
import java.util.ArrayList;
import java.text.DecimalFormat;
public class CheckoutController implements ActionListener {
    CheckoutScreen myScreen;
    DataAccess myDAO;
    Order order = null;
    public CheckoutController(CheckoutScreen screen, DataAccess dao) {
        myScreen = screen;
        myDAO = dao;
        resetOrder();
    }
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == myScreen.btnAdd) {
            addProduct();
        }
        else if (e.getSource() == myScreen.btnPay) {
            saveOrder();
        }
    }
    private void saveOrder() {
        Date orderDate;
        String customerName = "";
        double totalCost;
        double totalTax;
        totalCost = order.getTotalCost();
        if (totalCost == 0) {
            JOptionPane.showMessageDialog(null, "Order cannot be empty!");
        }
        else {
            orderDate = new java.sql.Date(System.currentTimeMillis());
            customerName = JOptionPane.showInputDialog(null, "Enter your name: ");
            while (customerName.isEmpty()) {
                customerName = JOptionPane.showInputDialog(null, "Name cannot be empty! Enter your name: ");
            }
            totalTax = order.getTotalTax();
            List<OrderLine> tempLines = new ArrayList<>();
            for (OrderLine line : order.getLines()) {
                tempLines.add(line);
            }
            order.getLines().clear();
            for (OrderLine line : tempLines) {
                order.addLine(line);
            }
            order.setDate(orderDate);
            order.setCustomerName(customerName);
            order.setTotalCost(totalCost);
            order.setTotalTax(totalTax);
            myDAO.saveOrder(order);
            JOptionPane.showMessageDialog(null, "Order placed successfully!");
            myScreen.clear();
            resetOrder();
        }
    }
    private void addProduct() {
        DecimalFormat df = new DecimalFormat("$###,###,##0.00");
        String id = JOptionPane.showInputDialog("Enter ProductID: ");
        while (id.isEmpty()) {
            id = JOptionPane.showInputDialog("ProductID cannot be empty! Enter ProductID: ");
        }
        ProductModel product = myDAO.loadProduct(Integer.parseInt(id));
        if (product == null) {
            JOptionPane.showMessageDialog(null, "This product does not exist!");
            return;
        }
        double quantity = Double.parseDouble(JOptionPane.showInputDialog(null,"Enter quantity: "));
        if (quantity <= 0 || quantity > product.quantity) {
            JOptionPane.showMessageDialog(null, "This quantity is not valid!");
            return;
        }
        OrderLine line = new OrderLine();
        line.setProductID(product.productID);
        line.setQuantity(quantity);
        line.setCost(quantity * product.price);
        product.quantity = (product.quantity - quantity); // update new quantity!!
        myDAO.saveProduct(product); // and store this product back right away!!!
        order.getLines().add(line);
        order.setTotalCost(order.getTotalCost() + line.getCost());
        Object[] row = new Object[5];
        row[0] = line.getProductID();
        row[1] = product.name;
        row[2] = product.price;
        row[3] = line.getQuantity();
        row[4] = line.getCost();
        this.myScreen.addRow(row);
        String formattedSubTotal = df.format(order.getTotalCost() - order.getTotalTax());
        this.myScreen.subTotal.setText("Sub Total: " + formattedSubTotal);
        String formattedTax = df.format(order.getTotalTax());
        this.myScreen.tax.setText("Tax: " + formattedTax);
        String formatTotal = df.format(order.getTotalCost());
        this.myScreen.total.setText("Total: " + formatTotal);
        this.myScreen.invalidate();
    }
    private void resetOrder() {
        order = new Order(); // create a new instance of Order
    }
}