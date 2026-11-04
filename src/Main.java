import javax.swing.SwingUtilities;

// Main class that starts the GUI application.
public class Main {

    public static void main(String[] args) {

        // Starts the GUI on the Swing Event Dispatch Thread.
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {

                // Opens the Dashboard window.
                new Dashboard();
            }
        });
    }
}