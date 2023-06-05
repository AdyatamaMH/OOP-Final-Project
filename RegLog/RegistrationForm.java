package RegLog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class RegistrationForm extends JDialog implements ActionListener{
    private JTextField tfName;         // Text field for name
    private JTextField tfEmail;        // Text field for email
    private JTextField tfPhone;        // Text field for phone number
    private JTextField tfAddress;      // Text field for address
    private JPasswordField pfPassword; // Password field for password
    private JButton btnRegister;       // Button to register user
    private JButton btnCancel;          // Button to cancel registration
    private JPanel registerPanel;      // Panel to hold the registration form

    public RegistrationForm(JFrame parent) {
        super(parent);
        setTitle("Create a new account");
        setContentPane(registerPanel);
        setMinimumSize(new Dimension(450, 474));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // ActionListener for the register button
        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser(); // Call the registerUser() method when the button is clicked
            }
        });

        // ActionListener for the cancel button
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the registration form
            }
        });

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    private void registerUser() {
        String name = tfName.getText();            // Retrieve the entered name
        String email = tfEmail.getText();          // Retrieve the entered email
        String phone = tfPhone.getText();          // Retrieve the entered phone number
        String address = tfAddress.getText();      // Retrieve the entered address
        String password = String.valueOf(pfPassword.getPassword()); // Retrieve the entered password

        // Check if any of the fields are empty
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter all fields",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return; // Stop further execution if any field is empty
        }

        // Try to add the user to the database
        user = addUserToDatabase(name, email, phone, address, password);
        if (user != null) {
            dispose(); // Close the registration form if user registration is successful
        } else {
            JOptionPane.showMessageDialog(this,
                    "Failed to register new user",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public User user;
    private User addUserToDatabase(String name, String email, String phone, String address, String password) {
        User user = null;
        final String DB_URL = "jdbc:mysql://localhost:3306/MyRestaurant";
        final String USERNAME = "root";
        final String PASSWORD = "";

        try {
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            // Connected to the database successfully

            Statement stmt = conn.createStatement();
            String sql = "INSERT INTO users (name, email, phone, address, password) " +
                    "VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, phone);
            preparedStatement.setString(4, address);
            preparedStatement.setString(5, password);

            // Insert row into the table
            int addedRows = preparedStatement.executeUpdate();
            if (addedRows > 0) {
                user = new User();
                user.name = name;
                user.email = email;
                user.phone = phone;
                user.address = address;
                user.password = password;
            }

            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }

    public static void main(String[] args) {
        RegistrationForm myForm = new RegistrationForm(null);
        User user = myForm.user;
        if (user != null) {
            System.out.println("Successful registration of: " + user.name);
        } else {
            System.out.println("Registration canceled");
        }
    }

}
