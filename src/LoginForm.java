import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginForm extends JDialog{

    private JTextField tfEmail;
    private JPasswordField tfPassword;
    private JButton btnOK;
    private JButton btnCancel;
    private JPanel loginPanel;

    public LoginForm(JFrame parent){
        // Call the constructor of the JDialog class
        super(parent);
        // Set a title
        setTitle("Login");
        // Display the panel
        setContentPane(loginPanel);
        // Provide a minimum size
        setMinimumSize(new Dimension(450, 474));
        setModal(true);
        // Display the panel in the middle of the frame
        setLocationRelativeTo(parent);
        // Close the dialog when we click on the close button
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        btnOK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = tfEmail.getText();
                String password = String.valueOf(tfPassword.getPassword());

                user = getAuthenticateUser(email, password);

                if (user != null){
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(LoginForm.this,
                            "Email or Password Invalid",
                            "Try again",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        // The dialog is visible
        setVisible(true);
    }

    public User user;
    private User getAuthenticateUser(String email, String password) {
        User user = null;

        final String DB_URL = "jdbc:mysql://localhost/mystore?serverTimezone=UTC";
        final String USERNAME = "root";
        final String PASSWORD = "";

        try {
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            // Connected to database successfuly !

            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM users WHERE email=? AND password=?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                user = new User();
                user.name = resultSet.getString("name");
                user.email = resultSet.getString("email");
                user.password = resultSet.getString("password");
            }

            stmt.close();
            conn.close();

        }catch(Exception e){
            e.printStackTrace();
        }


        return user;
    }

    public static void main(String[] args) {
        LoginForm loginForm = new LoginForm(null);
        User user = loginForm.user;

        if (user != null){
            System.out.println("Successful Authentication of :" + user.name);
            System.out.println("        Email: " + user.email);
        } else {
            System.out.println("Authentication canceled");
        }
    }


}
