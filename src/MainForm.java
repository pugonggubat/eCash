import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainForm  extends JFrame{
    private JButton btnSend;
    private JPanel rootPanel;
    private JLabel lblecash;
    private JTabbedPane TabPanel;
    private JPanel tabSend;
    private JTextField txtSend;
    private JTextField txtAcctnum;
    private JComboBox<FillCombo.ComboItem> cboMerchant;
    private JLabel lblhello;
    private JPanel tabMain;
    private JLabel lblDateTime;
    private JComboBox<FillCombo.ComboItem> cboLinked;
    private JLabel lblBalance;
    private JTable tblTansactions;
    private JPanel tabCashIn;
    private JPanel tabTrans;
    private JScrollPane scrollPane;
    private JPanel tabProfile;
    private JLabel lblUserProfile;
    private JLabel lblFullname;
    private JLabel lblEmail;
    private JButton btnLoad;
    private JTextField txtLoad;
    private JTextField txtLoadAcct;

    private String fname,lname,middle,email,acct;
    public final int userID;
    private Double balance=0.0;

    public MainForm(int userID) {
        this.userID=userID;
        iniComponents();
        getUser(userID);
        getBalance(userID);

        FillCombo.fillMerchant(cboMerchant);
        FillCombo.fillLinked(cboLinked,userID);




        btnSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FillCombo.ComboItem selected = (FillCombo.ComboItem) cboMerchant.getSelectedItem();
                int methodId = selected.getId();
                String methodName = selected.getName();
                Double amount;
                try {
                    amount = Double.parseDouble(txtSend.getText().trim());
                } catch (NumberFormatException ea) {
                    // Rethrow with a clearer message
                    JOptionPane.showMessageDialog(null,
                            "Invalid amount. Please enter numbers only.",
                            "Input Error",
                            JOptionPane.ERROR_MESSAGE);
                    txtSend.selectAll();
                    return; // stop action here
                }
                try {
                    String acctnumber = txtAcctnum.getText();
                    PaymentValidator validator = new PaymentValidator(amount,methodName,methodId,acctnumber,"Cash Out",balance);
                    validator.validate();
                    getBalance(userID);

                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(null,"Validation failed: " + ex.getMessage());
                }
            }
        });

        btnLoad.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FillCombo.ComboItem selected = (FillCombo.ComboItem) cboLinked.getSelectedItem();
                int methodId = selected.getId();
                String methodName = selected.getName();
                Double amount;
                try {
                    amount = Double.parseDouble(txtLoad.getText().trim());
                } catch (NumberFormatException ea) {
                    // Rethrow with a clearer message
                    JOptionPane.showMessageDialog(null,
                            "Invalid amount. Please enter numbers only.",
                            "Input Error",
                            JOptionPane.ERROR_MESSAGE);
                    txtLoad.selectAll();
                    return; // stop action here
                }
                try {
                    String acctnumber = txtLoadAcct.getText();
                    PaymentValidator validator = new PaymentValidator(amount,methodName,methodId,acctnumber,"Cash In",balance);
                    validator.validate();
                    getBalance(userID);

                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(null,"Validation failed: " + ex.getMessage());
                }
            }
        });
    }


    private void updateDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy hh:mm:ss a");
        lblDateTime.setText(sdf.format(new Date()));
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    private void iniComponents(){
        setTitle("eCash Digital Wallet");
        setContentPane(rootPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
//      setSize(350, 550);
        setVisible(true);
        setLocationRelativeTo(getParent());

        Timer timer = new Timer(1000, e -> updateDateTime());
        timer.start();


    }

    public void getUser(int userID){

        String sql = "SELECT * from users where id=?";
        //this type of connection will clause automatically, those inside try(), no need finally and close
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userID);
            ResultSet rsUser = stmt.executeQuery();

            while (rsUser.next()){
                lblhello.setText("Hello " + rsUser.getString("fname") + "!");
                fname=rsUser.getString("fname");
                lname=rsUser.getString("lname");
                middle=rsUser.getString("middle");
                acct=rsUser.getString("acct_id");
                email=rsUser.getString("email");

                lblFullname.setText(fname+ " " + middle + " " + lname);
                lblEmail.setText(email);
            }


        } catch (SQLException ex) {
//            JOptionPane.showMessageDialog(null,"Database error: " + ex.getMessage());
            JOptionPane.showMessageDialog(null,ex.getMessage(), "Error",JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    public void getBalance(int userID){

        txtSend.setText("");
        txtAcctnum.setText("");
        cboMerchant.setSelectedIndex(-1);
        txtLoad.setText("");
        txtLoadAcct.setText("");
        cboLinked.setSelectedIndex(-1);


        String sql = "SELECT t.*, m.name as merchant_name FROM transactions t JOIN merchants m" +
                " ON t.merchant_id = m.id " +
                "WHERE t.user_id = ? ORDER BY t.date DESC;";
        //this type of connection will clause automatically, those inside try(), no need finally and close
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,
                     ResultSet.CONCUR_READ_ONLY)) {

            stmt.setInt(1, userID);

            ResultSet rsBalance = stmt.executeQuery();


            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("REF #");
            model.addColumn("TRANSACTION");
            model.addColumn("AMOUNT");
            model.addColumn("MERCHANT");
            model.addColumn("ACCT #");
            model.addColumn("BALANCE");
            model.addColumn("DATE");


            if(rsBalance.next()) {
                balance = rsBalance.getDouble("balance");
                lblBalance.setText(String.format("%.2f", balance));
            }
                //  Add rows to the model
                rsBalance.beforeFirst();
                while (rsBalance.next()) {
                    model.addRow(new Object[]{
                            rsBalance.getString("ref_id"),
                            rsBalance.getString("type"),
                            rsBalance.getDouble("amount"),
                            rsBalance.getString("merchant_name"),
                            rsBalance.getString("acct"),
                            rsBalance.getDouble("balance"),
                            rsBalance.getString("date")
                    });

                }

            tblTansactions.setModel(model);
            tblTansactions.revalidate();
            tblTansactions.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);





        } catch (SQLException ex) {
//            System.out.println("Database error: " + ex.getMessage());
            JOptionPane.showMessageDialog(null,ex.getMessage(), "Error",JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void createUIComponents() {
        //
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainForm(LoginForm.Session.userId);
        });

    }
}
