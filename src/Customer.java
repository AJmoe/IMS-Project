import net.proteanit.sql.DbUtils;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class Customer {
    Connection conn;
    PreparedStatement pstmt = null;
    private JTextField tfName;
    private JTextField tfEmail;
    private JTextField tfAddress;
    private JTextField tfNumber;
    private JButton closeButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton saveButton;
    public JPanel CustomerPanel;
    private JTable table1;
    private JTextField tfSEARCH;
    private JButton searchButton;
    private JTextField tfCustomerID;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Customer");
        frame.setContentPane(new Customer().CustomerPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ims=bd", "root", "Password@123");
            System.out.println("connected to DB...");
        } catch (Exception err) {
            System.out.println("Error: " + err);
        }
    }

    void table_load() {
        try {
            pstmt = conn.prepareStatement("select * from Customer");
            ResultSet rs = pstmt.executeQuery();
            table1.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Customer() {
        connect();
        table_load();

        // Add mouse listener to the table
        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = table1.getSelectedRow();
                if (selectedRow != -1) {
                    String CustomerID = table1.getValueAt(selectedRow, 0).toString();
                    String Name = table1.getValueAt(selectedRow, 1).toString();
                    String Email = table1.getValueAt(selectedRow, 2).toString();
                    String Address = table1.getValueAt(selectedRow, 3).toString();
                    String PhNumber = table1.getValueAt(selectedRow, 4).toString();

                    tfCustomerID.setText(CustomerID);
                    tfName.setText(Name);
                    tfEmail.setText(Email);
                    tfAddress.setText(Address);
                    tfNumber.setText(PhNumber);
                }
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String CustomerID, Name, Email, Address, PhNumber;
                CustomerID = tfCustomerID.getText();
                Name = tfName.getText();
                Email = tfEmail.getText();
                Address = tfAddress.getText();
                PhNumber = tfNumber.getText();

                try {
                    pstmt = conn.prepareStatement("insert into Customer(Name, Email, Address, PhNumber) values(?, ?, ?, ?)");
                    pstmt.setString(1, Name);
                    pstmt.setString(2, Email);
                    pstmt.setString(3, Address);
                    pstmt.setString(4, PhNumber);
                    pstmt.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Record added");
                    table_load();
                    tfName.setText("");
                    tfEmail.setText("");
                    tfAddress.setText("");
                    tfNumber.setText("");
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String CustomerID, Name, Email, Address, PhNumber;
                CustomerID = tfCustomerID.getText();
                Name = tfName.getText();
                Email = tfEmail.getText();
                Address = tfAddress.getText();
                PhNumber = tfNumber.getText();
                try {
                    pstmt = conn.prepareStatement("Update Customer set Name = ?, Email = ?, Address = ?, PhNumber = ? WHERE CustomerID = ?");
                    pstmt.setString(1, Name);
                    pstmt.setString(2, Email);
                    pstmt.setString(3, Address);
                    pstmt.setString(4, PhNumber);
                    pstmt.setString(5, CustomerID);
                    pstmt.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Record UPDATED!!!");
                    table_load();
                    tfCustomerID.setText("");
                    tfName.setText("");
                    tfEmail.setText("");
                    tfAddress.setText("");
                    tfNumber.setText("");
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int option = JOptionPane.showConfirmDialog(null, "Do you want to DELETE the selected Record?", "Confirmation", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {

                }
                int selectedRow = table1.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "Please select a record to delete.");
                    return;
                }

                String CustomerID = table1.getValueAt(selectedRow, 0).toString();

                try {
                    pstmt = conn.prepareStatement("DELETE FROM Customer WHERE CustomerID = ?");
                    pstmt.setString(1, CustomerID);
                    pstmt.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Record DELETED Successfully!!");
                    table_load();
                    tfCustomerID.setText("");
                    tfName.setText("");
                    tfEmail.setText("");
                    tfAddress.setText("");
                    tfNumber.setText("");
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });

        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create an instance of the Home class
                Home home = new Home();

                // Get the current JFrame
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(CustomerPanel);

                // Close the current frame
                frame.dispose();

                // Show the Home frame
                home.setVisible(true);
            }
        });


        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String CustomerID = tfSEARCH.getText();
                    pstmt = conn.prepareStatement("SELECT Name, Email, Address, PhNumber FROM Customer WHERE CustomerID = ?");
                    pstmt.setString(1, CustomerID);
                    ResultSet rs = pstmt.executeQuery();

                    if (rs.next()) {
                        String name = rs.getString("Name");
                        String email = rs.getString("Email");
                        String address = rs.getString("Address");
                        String phNumber = rs.getString("PhNumber");
                        tfName.setText(name);
                        tfEmail.setText(email);
                        tfAddress.setText(address);
                        tfNumber.setText(phNumber);
                    } else {
                        tfName.setText("");
                        tfEmail.setText("");
                        tfAddress.setText("");
                        tfNumber.setText("");
                        JOptionPane.showMessageDialog(null, "Invalid Customer ID");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public JPanel getPanel() {
        return CustomerPanel;
    }
}
