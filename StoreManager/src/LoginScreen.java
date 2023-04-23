import javax.swing.*;
import java.awt.*;
public class LoginScreen extends JFrame {
    public JTextField txtUserName = new JTextField(20);
    public JPasswordField txtPassword = new JPasswordField(20);
    public JButton btnLoginSeller = new JButton("Seller Login");
    public JButton btnLoginBuyer = new JButton("Buyer Login");
    public LoginScreen(LoginController controller) {
        Font labelFont = new Font("Arial", Font.BOLD, 14);
        this.setTitle("Login Screen");
        this.setSize(new Dimension(400, 250));
        this.setLocationRelativeTo(null); // Center the window on the screen
        this.setResizable(false);
        this.getContentPane().setLayout(new BorderLayout());
        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        JLabel[] labels = {
                new JLabel("Username"),
                new JLabel("Password")
        };
        for (JLabel label : labels) {
            label.setFont(labelFont);
        }
        inputPanel.add(labels[0]);
        inputPanel.add(txtUserName);
        inputPanel.add(labels[1]);
        inputPanel.add(txtPassword);
        this.getContentPane().add(inputPanel, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 10));
        btnLoginSeller.setPreferredSize(new Dimension(120, 30));
        btnLoginBuyer.setPreferredSize(new Dimension(120, 30));
        buttonPanel.add(btnLoginSeller);
        buttonPanel.add(btnLoginBuyer);
        this.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        btnLoginSeller.addActionListener(controller);
        btnLoginBuyer.addActionListener(controller);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}