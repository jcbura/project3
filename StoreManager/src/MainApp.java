import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.JFrame;
import javax.swing.UIManager;
public class MainApp {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize theme. Using fallback.");
        }
        StoreManager.getInstance().getLoginScreen().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        StoreManager.getInstance().getLoginScreen().setVisible(true); // Show the ProductView!
    }
}