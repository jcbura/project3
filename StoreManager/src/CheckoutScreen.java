import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
public class CheckoutScreen extends JFrame {
    public JButton btnAdd = new JButton("Add a new item");
    public JButton btnPay = new JButton("Finish and Pay");
    public DefaultTableModel items = new DefaultTableModel();
    public JTable tblItems = new JTable(items);
    public JLabel subTotal = new JLabel("Sub Total: ");
    public JLabel tax = new JLabel("Tax: ");
    public JLabel total = new JLabel("Total: ");
    public CheckoutScreen(CheckoutController controller) {
        this.setTitle("Checkout");
        this.setLayout(new BorderLayout());
        this.setSize(500, 600);
        this.setLocationRelativeTo(null); // Center the window on the screen
        this.setResizable(false);
        items.addColumn("Product ID");
        items.addColumn("Name");
        items.addColumn("Price");
        items.addColumn("Quantity");
        items.addColumn("Cost");
        tblItems.setFillsViewportHeight(true);
        tblItems.getTableHeader().setBackground(new Color(100, 100, 100));
        tblItems.getTableHeader().setForeground(Color.white);
        tblItems.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        JScrollPane scrollPane = new JScrollPane(tblItems);
        JPanel panelInfo = new JPanel(new GridLayout(3, 1, 5, 5));
        panelInfo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelInfo.add(subTotal);
        panelInfo.add(tax);
        panelInfo.add(total);
        JPanel panelOrder = new JPanel(new BorderLayout(10, 10));
        panelOrder.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelOrder.add(scrollPane, BorderLayout.CENTER);
        panelOrder.add(panelInfo, BorderLayout.EAST);
        this.getContentPane().add(panelOrder, BorderLayout.CENTER);
        JPanel panelButton = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panelButton.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 10));
        btnAdd.setPreferredSize(new Dimension(150, 30));
        btnPay.setPreferredSize(new Dimension(150, 30));
        panelButton.add(btnAdd);
        panelButton.add(btnPay);
        this.getContentPane().add(panelButton, BorderLayout.SOUTH);
        btnAdd.addActionListener(controller);
        btnPay.addActionListener(controller);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    public void clear() {
        items.setRowCount(0);
        subTotal.setText("Sub Total: ");
        tax.setText("Tax: ");
        total.setText("Total: ");
    }
    public void addRow(Object[] row) {
        items.addRow(row);
    }
}