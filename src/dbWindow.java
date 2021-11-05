import java.awt.EventQueue;
import java.sql.*;


import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

//import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;

import net.proteanit.sql.DbUtils;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class dbWindow {

	private JFrame frame;
	private JTextField txtbname;
	private JTextField txtedition;
	private JTextField txtprice;
	private JTable table;
	private JTable table_1;
	private JTextField txtbid;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					dbWindow window = new dbWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public dbWindow() {
		initialize();
		Connect();
		table_load();
	}
	
	Connection con;
	PreparedStatement pst;
	ResultSet rs;
	
	
	
	public void Connect()
		{
			try {
			 con = DriverManager.getConnection("jdbc:mysql://localhost:8889/demo", "root", "root");
			} 
			catch (SQLException ex)
			{ex.printStackTrace();}
		}
	
	
	// Loads saved database info in to application's table
	public void table_load() {
		try {
			pst = con.prepareStatement("select * from books");
			rs = pst.executeQuery();
			table_1.setModel(DbUtils.resultSetToTableModel(rs));
			
		}
		catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
	

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 900, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Library Book Finder");
		lblNewLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 35));
		lblNewLabel.setBounds(181, 6, 332, 63);
		frame.getContentPane().add(lblNewLabel);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Registration", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setToolTipText("");
		panel.setBounds(26, 81, 368, 152);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblNewLabel_1 = new JLabel("Book Name");
		lblNewLabel_1.setBounds(18, 24, 76, 27);
		panel.add(lblNewLabel_1);
		
		JLabel lblNewLabel_1_1 = new JLabel("Edition");
		lblNewLabel_1_1.setBounds(18, 63, 76, 27);
		panel.add(lblNewLabel_1_1);
		
		JLabel lblNewLabel_1_1_1 = new JLabel("Price");
		lblNewLabel_1_1_1.setBounds(18, 102, 76, 27);
		panel.add(lblNewLabel_1_1_1);
		
		txtbname = new JTextField();
		txtbname.setBounds(106, 24, 240, 26);
		panel.add(txtbname);
		txtbname.setColumns(10);
		
		txtedition = new JTextField();
		txtedition.setColumns(10);
		txtedition.setBounds(106, 63, 240, 26);
		panel.add(txtedition);
		
		txtprice = new JTextField();
		txtprice.setColumns(10);
		txtprice.setBounds(106, 102, 240, 26);
		panel.add(txtprice);
		
		JButton btnClear = new JButton("Clear");
		btnClear.setBounds(269, 245, 117, 29);
		frame.getContentPane().add(btnClear);
		
		JButton btnExit = new JButton("Exit");
		btnExit.setBounds(153, 245, 117, 29);
		frame.getContentPane().add(btnExit);
		
		JButton btnNewButton = new JButton("Save");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String bookName;
				String edition; 
				String price;
				
				bookName = txtbname.getText();
				edition = txtedition.getText();
				price = txtprice.getText();
				
				try {
					pst = con.prepareStatement("insert into books(title, edition, price)values(?,?,?)");
					pst.setString(1, bookName);
					pst.setString(2, edition);
					pst.setString(3, price);
					pst.executeUpdate();
					JOptionPane.showMessageDialog(null, "Record added!");
					txtbname.setText("");
					txtedition.setText("");
					txtprice.setText("");
					txtbname.requestFocus();
					table_load();
				}
				catch (SQLException ex)
				{ex.printStackTrace();}
				
			}
		});
		btnNewButton.setBounds(36, 245, 117, 29);
		frame.getContentPane().add(btnNewButton);
		
		table = new JTable();
		table.setBounds(459, 338, 139, -185);
		frame.getContentPane().add(table);
		
		table_1 = new JTable();
		table_1.setBounds(406, 82, 467, 186);
		frame.getContentPane().add(table_1);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Search", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(26, 286, 368, 80);
		frame.getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		JLabel lblNewLabel_1_1_2 = new JLabel("Book ID");
		lblNewLabel_1_1_2.setBounds(17, 28, 76, 27);
		panel_1.add(lblNewLabel_1_1_2);
		
		txtbid = new JTextField();
		txtbid.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				try {
					String id = txtbid.getText();
					
					pst = con.prepareStatement("select title, edition, price from books where id = ?");
					pst.setString(1,  id);
					ResultSet rs = pst.executeQuery();
					
					if (rs.next() == true) 
					{
						String name = rs.getString(1);
						String edition = rs.getString(2);
						String price = rs.getString(3);
						
						txtbname.setText(name);
						txtedition.setText(edition);
						txtprice.setText(price);
					}
					else 
					{
						txtbname.setText("");
						txtedition.setText("");
						txtprice.setText("");
					}
				}
				catch (SQLException ex)
				{ex.printStackTrace();}
			}
		});
		txtbid.setColumns(10);
		txtbid.setBounds(105, 28, 226, 26);
		panel_1.add(txtbid);
		
		JButton btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String bookName;
				String edition; 
				String price;
				String bookId;
				
				bookName = txtbname.getText();
				edition = txtedition.getText();
				price = txtprice.getText();
				bookId = txtbid.getText();
				
				try {
					pst = con.prepareStatement("update books set title = ?, edition = ?, price = ? where id = ?");
					pst.setString(1, bookName);
					pst.setString(2, edition);
					pst.setString(3, price);
					pst.setString(4, bookId);
					pst.executeUpdate();
					
					JOptionPane.showMessageDialog(null, "Record added!");
					txtbname.setText("");
					txtedition.setText("");
					txtprice.setText("");
					txtbid.setText("");
					txtbname.requestFocus();
					table_load();
				}
				catch (SQLException ex)
				{ex.printStackTrace();}
				
				
				
			}
		});
		btnUpdate.setBounds(391, 311, 70, 29);
		frame.getContentPane().add(btnUpdate);
		
		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String bookId;
				
				bookId = txtbid.getText();
				
				try {
					pst = con.prepareStatement("delete from books where id = ?");
					
					pst.setString(1, bookId);
					pst.executeUpdate();
					
					JOptionPane.showMessageDialog(null, "Record deleted!");
					txtbname.setText("");
					txtedition.setText("");
					txtprice.setText("");
					txtbid.setText("");
					txtbname.requestFocus();
					table_load();
				}
				catch (SQLException ex)
				{ex.printStackTrace();}
			}
		});
		btnDelete.setBounds(391, 338, 70, 29);
		frame.getContentPane().add(btnDelete);
	}
}
