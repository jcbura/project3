import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class LoginController implements ActionListener {
    LoginScreen myScreen;
    DataAccess myDAO;
    public LoginController(LoginScreen screen, DataAccess dao) {
        myScreen = screen;
        myDAO = dao;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        String username = myScreen.txtUserName.getText().trim();
        String password = myScreen.txtPassword.getText().trim();
        UserModel user = myDAO.loadUser(username, password);
        if (user == null) {
            JOptionPane.showMessageDialog(null, "This user does not exist!");
            return;
        }
        if (e.getSource() == myScreen.btnLoginSeller) {
            if (!username.equals("seller")) {
                JOptionPane.showMessageDialog(null, "This login is for sellers!");
            } else {
                myScreen.setVisible(false);
                StoreManager.getInstance().getProductView().setVisible(true);
            }
        }
        if (e.getSource() == myScreen.btnLoginBuyer) {
            if (!username.equals("buyer")) {
                JOptionPane.showMessageDialog(null, "This login is for buyers!");
            } else {
                myScreen.setVisible(false);
                StoreManager.getInstance().getCheckoutScreen().setVisible(true);
            }
        }
    }
}