import javax.swing.*;
import java.awt.*;
public class ProductView extends JFrame {
    public JTextField txtProductID = new JTextField(20);
    public JTextField txtProductName = new JTextField(20);
    public JTextField txtProductPrice = new JTextField(20);
    public JTextField txtProductQuantity = new JTextField(20);
    public JButton btnLoad = new JButton("Load");
    public JButton btnSave = new JButton("Save");
    public ProductView() {
        Font labelFont = new Font("Arial", Font.BOLD, 14);
        Font textFieldFont = new Font("Arial", Font.PLAIN, 14);
        txtProductID.setFont(textFieldFont);
        txtProductName.setFont(textFieldFont);
        txtProductPrice.setFont(textFieldFont);
        txtProductQuantity.setFont(textFieldFont);
        btnLoad.setPreferredSize(new Dimension(100, 30));
        btnSave.setPreferredSize(new Dimension(100, 30));
        this.setTitle("Product View");
        this.setSize(new Dimension(600, 300));
        this.setLocationRelativeTo(null); // Center the window on the screen
        this.setResizable(false);
        this.getContentPane().setLayout(new BorderLayout());
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        JLabel[] labels = {
                new JLabel("Product ID"),
                new JLabel("Product Name"),
                new JLabel("Price"),
                new JLabel("Quantity")
        };
        for (JLabel label : labels) {
            label.setFont(labelFont);
        }
        inputPanel.add(labels[0]);
        inputPanel.add(txtProductID);
        inputPanel.add(labels[1]);
        inputPanel.add(txtProductName);
        inputPanel.add(labels[2]);
        inputPanel.add(txtProductPrice);
        inputPanel.add(labels[3]);
        inputPanel.add(txtProductQuantity);
        this.getContentPane().add(inputPanel, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 10));
        buttonPanel.add(btnLoad);
        buttonPanel.add(btnSave);
        this.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}