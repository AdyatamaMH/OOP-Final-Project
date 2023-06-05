package RegLog;

// Importing necessary classes and libraries
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginForm extends JDialog implements ActionListener{
    private JTextField tfEmail;
    private JPasswordField pfPassword;
    private JButton btnOK;
    private JButton btnCancel;
    private JPanel loginPanel;

    public LoginForm(JFrame parent) {
        super(parent);
        setTitle("Login");
        setContentPane(loginPanel);
        setMinimumSize(new Dimension(450, 474));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // ActionListener for the OK button
        btnOK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Retrieve entered email and password
                String email = tfEmail.getText();
                String password = String.valueOf(pfPassword.getPassword());

                // Call the getAuthenticatedUser method to authenticate the user
                user = getAuthenticatedUser(email, password);

                if (user != null) {
                    // If user is authenticated successfully, close the login form
                    dispose();
                }
                else {
                    // If authentication fails, show an error message
                    JOptionPane.showMessageDialog(LoginForm.this,
                            "Email or Password Invalid",
                            "Try again",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // ActionListener for the Cancel button
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Close the login form when cancel button is clicked
                dispose();
            }
        });

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
    public User user;

    // Method to authenticate the user using the provided email and password
    private User getAuthenticatedUser(String email, String password) {
        User user = null;

        // Database connection details
        final String DB_URL = "jdbc:mysql://localhost:3306/MyRestaurant";
        final String USERNAME = "root";
        final String PASSWORD = "";

        try {
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            // Connected to the database successfully...

            Statement stmt = conn.createStatement();
            String sql = "SELECT email, password FROM users WHERE email=? AND password=?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                // If a matching user is found in the database, create a User object with retrieved details
                user = new User();
                user.name = resultSet.getString("name");
                user.email = resultSet.getString("email");
                user.phone = resultSet.getString("phone");
                user.address = resultSet.getString("address");
                user.password = resultSet.getString("password");
            }

            stmt.close();
            conn.close();

        } catch(Exception e) {
            e.printStackTrace();
        }

        return user;
    }

    public static void main(String[] args) {
        // Create an instance of the LoginForm
        LoginForm loginForm = new LoginForm(null);
        User user = loginForm.user;
        if (user != null) {
            // If a user is authenticated, print the user details
            System.out.println("Successful Authentication of: " + user.name);
            System.out.println("          Email: " + user.email);
            System.out.println("          Phone: " + user.phone);
            System.out.println("          Address: " + user.address);
        }
        else {
            // If authentication is canceled, print a message
            System.out.println("Authentication canceled");
        }
    }

}
