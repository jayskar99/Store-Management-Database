public class App {
    private static App instance;   // Singleton pattern

    public static App getInstance() {
        if (instance == null) {
            instance = new App();
        }
        return instance;
    }

    public Database database = new Database();
    public MainWindow window = new MainWindow(database);

    public static void main(String[] args) {
        System.out.println("Existence is pain!");
        // App.getInstance().database.connect();
        App.getInstance().window.setVisible(true);
    }
}
