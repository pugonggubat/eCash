import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PaymentValidator {
    private double amount,balance,newBalance;
    private String method,transtype,acctnumber;
    private int methodID;
    private String ref_id;

    // Constructor to initialize payment details
    public PaymentValidator(double amount, String method, int methodID, String acctnumber, String transtype, double balance) {
        this.amount = amount;
        this.method = method;
        this.transtype = transtype;
        this.balance = balance;
        this.methodID = methodID;
        this.acctnumber = acctnumber;
    }

    // Validation method with exception handling
    public void validate() throws IllegalArgumentException {
        switch (transtype){
            case "Cash Out":{
                if (amount <= 0) {
                    JOptionPane.showMessageDialog(null,"Please input acceptable amount");
                    return;
                }
                // Check if balance will go negative
                else if (balance - amount < 0) {
                    JOptionPane.showMessageDialog(null,
                            "Insufficient balance. This transaction would result in a negative balance.",
                            "Insufficient Balance",
                            JOptionPane.WARNING_MESSAGE);

                    return;
                }
                else{
                    newBalance=balance-amount;
                }
// Auto-generate reference ID
//                ref_id = "TXN"+ System.currentTimeMillis();
//
//                try (Connection conn = DBConnect.getConnection()) {
//                    String sql = "INSERT INTO transactions (ref_id, user_id, type, amount, merchant_id,acct, balance, date) " +
//                            "VALUES (?, ?, ?, ?, ?, ?,?, NOW())";
//
//                    PreparedStatement stmt = conn.prepareStatement(sql);
//                    stmt.setString(1, ref_id);
//                    stmt.setInt(2,LoginForm.Session.userId);
//                    stmt.setString(3, transtype);
//                    stmt.setDouble(4, amount);
//                    stmt.setInt(5, methodID);
//                    stmt.setString(6, acctnumber);
//                    stmt.setDouble(7, newBalance);
//
//                    int rows = stmt.executeUpdate();
//                    if (rows > 0) {
//
//                        JOptionPane.showMessageDialog(null, "Sending money successful!\nRef: " + ref_id);
//
//                    } else {
//                        JOptionPane.showMessageDialog(null, "Failed to save transaction.");
//                    }
//
//                    stmt.close();
//                } catch (SQLException e) {
//                    JOptionPane.showMessageDialog(null, "Database Error: " + e.getMessage());
//                    e.printStackTrace();
//                }


                break;
            }

            case "Cash In":{
                if (amount <= 0) {
                    JOptionPane.showMessageDialog(null,"Please input acceptable amount");
                    return;
                }

                else{
                    newBalance=balance+amount;
                }

                break;
            }

        }

        // Auto-generate reference ID
        ref_id = "TXN"+ System.currentTimeMillis();

        try (Connection conn = DBConnect.getConnection()) {
            String sql = "INSERT INTO transactions (ref_id, user_id, type, amount, merchant_id,acct, balance, date) " +
                    "VALUES (?, ?, ?, ?, ?, ?,?, NOW())";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, ref_id);
            stmt.setInt(2,LoginForm.Session.userId);
            stmt.setString(3, transtype);
            stmt.setDouble(4, amount);
            stmt.setInt(5, methodID);
            stmt.setString(6, acctnumber);
            stmt.setDouble(7, newBalance);

            int rows = stmt.executeUpdate();
            if (rows > 0) {

                JOptionPane.showMessageDialog(null, "Transaction successful!\nRef: " + ref_id);

            } else {
                JOptionPane.showMessageDialog(null, "Failed to save transaction.");
            }

            stmt.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database Error: " + e.getMessage());
            e.printStackTrace();
        }

    }

//    // Optional helper method
//    public String getPaymentSummary() {
//        return (transtype=="CashOut"?"Sending":"Top up")+" of ₱" + amount + (transtype=="CashOut"?" to ":" via ") + method.toUpperCase() + " successful! \n " + ref_id;
//    }

}
