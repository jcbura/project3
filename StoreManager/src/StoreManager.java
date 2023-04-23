public class StoreManager {
    private static StoreManager instance = null;
    private RemoteDataAdapter dao;
    private ProductView productView = null;
    private LoginScreen loginScreen = null;
    private CheckoutScreen checkoutScreen = null;
    public ProductView getProductView() {
        return productView;
    }
    public LoginScreen getLoginScreen() {
        return loginScreen;
    }
    public CheckoutScreen getCheckoutScreen() {
        return checkoutScreen;
    }
    private ProductController productController = null;
    private LoginController loginController = null;
    private CheckoutController checkoutController = null;
    public static StoreManager getInstance() {
        if (instance == null)
            instance = new StoreManager("SQLite");
        return instance;
    }
    public RemoteDataAdapter getDataAccess() {
        return dao;
    }
    private StoreManager(String db) {
        dao = new RemoteDataAdapter();
        dao.connect("");
        productView = new ProductView();
        productController = new ProductController(productView, dao);
        loginController = new LoginController(null, dao); // Temporarily pass null as the LoginScreen is not created yet
        loginScreen = new LoginScreen(loginController); // Pass the loginController while initializing the LoginScreen
        loginController.myScreen = loginScreen;
        checkoutController = new CheckoutController(null, dao); // Temporarily pass null as the CheckoutScreen is not created yet
        checkoutScreen = new CheckoutScreen(checkoutController); // Pass the checkoutController while initializing the CheckoutScreen
        checkoutController.myScreen = checkoutScreen;
    }
}