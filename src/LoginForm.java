import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginForm extends JFrame {
    private JPanel rootPanel;
    private JButton btnLogin;
    private JButton btnClose;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JLabel lblUsername;
    private JLabel lblPass;
    private JPanel panelLeft;
    private JPanel panelRight;
    private JSplitPane splitPanel;


    public LoginForm() {
        setTitle("LoginForm");
        setUndecorated(true);
        setContentPane(rootPanel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setSize(500, 200);
        setLocationRelativeTo(null);
        setVisible(true);

        btnClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                dispose();
            }
        });
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent a) {
                String username = txtUsername.getText();
                String pass = new String(txtPassword.getPassword());

                Connection conn=null;
                PreparedStatement stmt = null;
                ResultSet rs = null;
                try {
                    // Connect to database
                    conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ecash", "elson", "");
                    System.out.println("Connected to database!");

                    // Create a statement
                    String sql = "SELECT id, username FROM users where username=? AND password=?";
                    stmt = conn.prepareStatement(sql);
                    stmt.setString(1,username);
                    stmt.setString(2,pass);

                    // Execute query
                    rs = stmt.executeQuery();

                    // Process result
                    if (rs.next()) {
                        int id = rs.getInt("id");
                        String name = rs.getString("username");
//                        System.out.println(id + " - " + username);

                        Session.userId = id;

                        // open main form directly

                        new MainForm(id);
//                        main.setVisible(true);

                        // close login form
                        SwingUtilities.getWindowAncestor(rootPanel).dispose();
                    }
                    else{
                        JOptionPane.showMessageDialog(null,"Login Failed!");
                        System.out.println("Login Failed");
                    }

                } catch (SQLException e) {
                    // Handle DB errors
                    System.out.println("Database error: " + e.getMessage());
                    e.printStackTrace();
                } finally {
                    // Always close resources in reverse order
                    try {
                        if (rs != null) rs.close();
                        if (stmt != null) stmt.close();
                        if (conn != null) conn.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        btnClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }


    public class Session {
        public static String username;
        public static int userId;
        public static String role;
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginForm().setVisible(true);
        });
    }
}
