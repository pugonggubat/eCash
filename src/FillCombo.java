import javax.swing.*;
import java.sql.*;

public class FillCombo {

    public static class ComboItem {
        private int id;
        private String name;

        public ComboItem(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        // This decides what is shown in JComboBox
        @Override
        public String toString() {
            return name;
        }
    }

    public static void fillMerchant(JComboBox<FillCombo.ComboItem> combo){
        combo.removeAllItems();
        String sql="SELECT * FROM merchants";

        try(Connection conn =  DBConnect.getConnection();
        PreparedStatement stmt= conn.prepareStatement(sql);) {
            ResultSet rsMerchant = stmt.executeQuery();
            while (rsMerchant.next()){
                int id = rsMerchant.getInt("id");
                String name = rsMerchant.getString("name");
                combo.addItem(new FillCombo.ComboItem(id, name));

            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void fillLinked(JComboBox<FillCombo.ComboItem> combo, int userID){
        combo.removeAllItems();
        String sql="SELECT * FROM linked_accts where user_id=?";

        try(Connection conn =  DBConnect.getConnection();
            PreparedStatement stmt= conn.prepareStatement(sql);) {
            stmt.setInt(1, userID);
            ResultSet rsLinked = stmt.executeQuery();


            while (rsLinked.next()){
                int id = rsLinked.getInt("id");
                String name = rsLinked.getString("name");
                combo.addItem(new FillCombo.ComboItem(id, name));

            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}


