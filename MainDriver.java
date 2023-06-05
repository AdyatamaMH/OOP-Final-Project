import RegLog.RegistrationForm;
import RegLog.LoginForm;
import RegLog.User;
import RegLog.SimplePOS;

import javax.swing.*;

public class MainDriver {
    public static void main(String[] args) {
        // Display prompt dialog
        int choice = JOptionPane.showOptionDialog(null, "Choose an option:",
                "Restaurant Ordering System", JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE, null, new String[]{"Register", "Login"}, null);

        if (choice == JOptionPane.YES_OPTION) {
            // Create and display the registration form
            RegistrationForm registrationForm = new RegistrationForm(null);
            User user = registrationForm.user;
            if (user != null) {
                int option = JOptionPane.showOptionDialog(null, "Registered as: " + user.name +
                                "\nProceed to SimplePOS?", "Confirmation", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, null, null);
                if (option == JOptionPane.YES_OPTION) {
                    openSimplePOS(user);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Registration canceled");
            }
        } else if (choice == JOptionPane.NO_OPTION) {
            // Create and display the login form
            LoginForm loginForm = new LoginForm(null);
            User user = loginForm.user;
            if (user != null) {
                int option = JOptionPane.showOptionDialog(null, "Logged in as: " + user.name +
                                "\nProceed to SimplePOS?", "Confirmation", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, null, null);
                if (option == JOptionPane.YES_OPTION) {
                    openSimplePOS(user);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Authentication canceled");
            }
        }
    }

    private static void openSimplePOS(User user) {
        // Open the SimplePOS program or perform necessary actions
        SwingUtilities.invokeLater(() -> {
            SimplePOS simplePOS = new SimplePOS();
            simplePOS.setVisible(true);
            simplePOS.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        });
    }
}
