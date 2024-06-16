import net.proteanit.sql.DbUtils;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class Category extends JFrame {

    Connection conn;
    PreparedStatement pstmt = null;
    private JTextField tfcategoryName;
    private JButton saveButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton closeButton;
    private JTable table1;
    public JPanel categorypanel;
    private JTextField tfCategoryID;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Category");
        frame.setContentPane(new Category().categorypanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set the frame to full screen
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setSize(java.awt.Toolkit.getDefaultToolkit().getScreenSize());

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
            pstmt = conn.prepareStatement("select * from Category");
            ResultSet rs = pstmt.executeQuery();
            table1.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Category() {
        connect();
        table_load();

        // Add mouse listener to the table
        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = table1.getSelectedRow();
                if (selectedRow != -1) {
                    String CategoryID = table1.getValueAt(selectedRow, 0).toString();
                    String Name = table1.getValueAt(selectedRow, 1).toString();
                    tfCategoryID.setText(CategoryID);
                    tfcategoryName.setText(Name);
                }
            }
        });

        // Save button action listener
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String CategoryID = tfCategoryID.getText();
                String Name = tfcategoryName.getText();
                try {
                    pstmt = conn.prepareStatement("INSERT INTO Category(CategoryID, Name) VALUES(?, ?)");
                    pstmt.setString(1, CategoryID);
                    pstmt.setString(2, Name);
                    pstmt.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Record added");
                    table_load();
                    tfCategoryID.setText("");
                    tfcategoryName.setText("");
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });

        // Update button action listener
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String CategoryID = tfCategoryID.getText();
                String Name = tfcategoryName.getText();
                try {
                    pstmt = conn.prepareStatement("UPDATE Category SET Name = ? WHERE CategoryID = ?");
                    pstmt.setString(1, Name);
                    pstmt.setString(2, CategoryID);
                    pstmt.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Record UPDATED!!!");
                    table_load();
                    tfCategoryID.setText("");
                    tfcategoryName.setText("");
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });

        // Delete button action listener
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table1.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "Please select a record to delete.");
                    return;
                }
                String ProductID = table1.getValueAt(selectedRow, 0).toString();
                try {
                    pstmt = conn.prepareStatement("DELETE FROM Category WHERE CategoryID = ?");
                    pstmt.setString(1, ProductID);
                    pstmt.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Record DELETED Successfully!!");
                    table_load();
                    tfCategoryID.setText("");
                    tfcategoryName.setText("");
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });

        // Close button action listener
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create an instance of the Home class
                Home home = new Home();
                // Get the current JFrame
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(categorypanel);
                // Close the current frame
                frame.dispose();
                // Show the Home frame
                home.setVisible(true);
            }
        });
    }

    public JPanel getPanel() {
        return categorypanel;
    }
}
