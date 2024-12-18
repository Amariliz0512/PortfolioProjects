/*																			
* Author:		Amariliz Gonzalez Piñeiro									
*																																
*				Internship Final Program			
*									
* Service Request Program connects to MySQL Database in which contains a
* view ‘open_request_view’ which tracks open requests items. You will be 
* able to add a new request, select the request, update request, clear all 
* items, delete request and exit the program.
* 
* 
*/
package RequestTracking;

import java.awt.EventQueue;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import net.proteanit.sql.DbUtils;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import java.sql.*;
import javax.swing.border.EtchedBorder;
import java.awt.Color;
import javax.swing.ScrollPaneConstants;
import java.awt.Window.Type;
import java.awt.SystemColor;

public class TrackRequest {

	private JFrame frmRequestTracking;
	private JTextField txtDateofRequest;
	private JTextField txtDescOfReq;
	private JTextField txtTechAssigned;
	private JTextField textDateComp;
	private JTable table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TrackRequest window = new TrackRequest();
					window.frmRequestTracking.setVisible(true);
				} 
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Create the application.
	 */
	public TrackRequest() {
		initialize();
		Connect();
		table_load();
	}
	
	//connect MySQL
	Connection con;
	PreparedStatement pst;
	ResultSet rs;
	private JTextField txtid;
	 
	public void Connect()
	    {
	        try {
	            Class.forName("com.mysql.cj.jdbc.Driver");
	            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/request_tracking", "root","TwinL0ve$");
	        }
	        catch (ClassNotFoundException ex)
	        {
	          ex.printStackTrace();
	        }
	        catch (SQLException ex)
	        {
	            ex.printStackTrace();
	        }
	 
	    }
	
	//runs stored procedure and upload report to get open requests of a view
	public void table_load()
	{
		try {
			// calls to store procedure to only show the open requests
			pst = con.prepareStatement("call request_tracking.open_request_update()");
			pst.execute();
			pst.close();
			//calls to select a view called open_request_view which only shows 'open' requests
			pst = con.prepareStatement("select * from open_request_view");
			rs = pst.executeQuery();
			table.setModel(DbUtils.resultSetToTableModel(rs));
			}
		catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Unable to connect load the Open Rquests. Please contact Tech Support!");
			e.printStackTrace();
			}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmRequestTracking = new JFrame(); // creates frame
		frmRequestTracking.setBackground(SystemColor.activeCaption);
		frmRequestTracking.setType(Type.POPUP);
		frmRequestTracking.setTitle("Service Request");
		frmRequestTracking.setBounds(100, 100, 1248, 765);
		frmRequestTracking.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmRequestTracking.getContentPane().setLayout(null);
		
		ImageIcon image = new ImageIcon("icons.support.png"); //creates an image icon
		frmRequestTracking.setIconImage(image.getImage());
		//label of Title
		JLabel lbTitle = new JLabel("Service Request");
		lbTitle.setFont(new Font("Tahoma", Font.BOLD, 30));
		lbTitle.setBounds(467, 11, 307, 37);
		frmRequestTracking.getContentPane().add(lbTitle);
		//panel updates with labels inside of panel
		JPanel panelUpdateReq = new JPanel();
		panelUpdateReq.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Update Request", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelUpdateReq.setBounds(74, 46, 1093, 254);
		frmRequestTracking.getContentPane().add(panelUpdateReq);
		panelUpdateReq.setLayout(null);
		
		JLabel lbDateReq = new JLabel("Date of Request");
		lbDateReq.setBounds(211, 58, 97, 14);
		lbDateReq.setFont(new Font("Tahoma", Font.PLAIN, 12));
		panelUpdateReq.add(lbDateReq);
		
		JLabel lbDescReq = new JLabel("Description");
		lbDescReq.setBounds(211, 96, 113, 14);
		lbDescReq.setFont(new Font("Tahoma", Font.PLAIN, 12));
		panelUpdateReq.add(lbDescReq);
		
		JLabel lbTechAssng = new JLabel("Technician Assigned");
		lbTechAssng.setBounds(211, 130, 113, 14);
		lbTechAssng.setFont(new Font("Tahoma", Font.PLAIN, 12));
		panelUpdateReq.add(lbTechAssng);
		
		JLabel lbDateComp = new JLabel("Date of Completion");
		lbDateComp.setBounds(211, 167, 113, 14);
		lbDateComp.setFont(new Font("Tahoma", Font.PLAIN, 12));
		panelUpdateReq.add(lbDateComp);
		
		JLabel lbNotes = new JLabel("Notes");
		lbNotes.setBounds(533, 35, 46, 14);
		lbNotes.setFont(new Font("Tahoma", Font.PLAIN, 12));
		panelUpdateReq.add(lbNotes);
		
		//Text boxes
		txtDateofRequest = new JTextField();
		txtDateofRequest.setBounds(334, 53, 151, 26);
		panelUpdateReq.add(txtDateofRequest);
		txtDateofRequest.setColumns(10);
		
		txtDescOfReq = new JTextField();
		txtDescOfReq.setBounds(334, 91, 151, 26);
		panelUpdateReq.add(txtDescOfReq);
		txtDescOfReq.setColumns(10);
		
		txtTechAssigned = new JTextField();
		txtTechAssigned.setBounds(334, 125, 151, 26);
		panelUpdateReq.add(txtTechAssigned);
		txtTechAssigned.setColumns(10);
		
		textDateComp = new JTextField();
		textDateComp.setBounds(334, 162, 151, 26);
		panelUpdateReq.add(textDateComp);
		textDateComp.setColumns(10);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(533, 53, 351, 153);
		scrollPane_2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane_2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		panelUpdateReq.add(scrollPane_2);
		
		JTextArea txtAreaNotes = new JTextArea();
		txtAreaNotes.setLineWrap(true);
		scrollPane_2.setViewportView(txtAreaNotes);
		txtAreaNotes.setWrapStyleWord(true);
		
		JLabel lblyyyymmdd = new JLabel("(yyyy-mm-dd)");
		lblyyyymmdd.setBounds(211, 181, 113, 14);
		lblyyyymmdd.setFont(new Font("Tahoma", Font.PLAIN, 12));
		panelUpdateReq.add(lblyyyymmdd);
		
		//label for request id
		JLabel lbId = new JLabel("Request ID");
		lbId.setBounds(10, 71, 61, 15);
		panelUpdateReq.add(lbId);
		lbId.setFont(new Font("Tahoma", Font.PLAIN, 12));
				
		txtid = new JTextField();
		txtid.setBounds(81, 66, 39, 26);
		panelUpdateReq.add(txtid);
		//selects row of request to update if completed 
		txtid.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				
				try {
					String id = txtid.getText();
					pst = con.prepareStatement("select Date_of_Req, Desc_of_Req, Name_of_Tech, Notes from request_status where id = ?");
					pst.setString(1, id);
				    ResultSet rs = pst.executeQuery();
				    				         
				   if(rs.next()==true)
				    {
				    	String Date_of_Req = rs.getString(1);
				        String Desc_of_Req = rs.getString(2);
				        String Name_of_Tech = rs.getString(3);
				        String Notes = rs.getString(4);
				                
				        txtDateofRequest.setText(Date_of_Req);
				        txtDescOfReq.setText(Desc_of_Req);
				        txtTechAssigned.setText(Name_of_Tech);
				        txtAreaNotes.setText(Notes);
				        txtid.requestFocus();
				    }  
				    else
				    {
				    	txtDateofRequest.setText("");
				    	txtDescOfReq.setText("");
				    	txtTechAssigned.setText("");
				    	txtAreaNotes.setText("");
				    	txtid.requestFocus();
				    }
				    }
				catch (SQLException ex) {
				          
				        }
			}
		});
		
		txtid.setColumns(10);
				
		//button updates
		JButton btnUpdate = new JButton("Update Request");
		btnUpdate.setBounds(914, 48, 145, 37);
		panelUpdateReq.add(btnUpdate);
				
		//this button deletes a row
		JButton btnDelete_1 = new JButton("Delete Request");
		btnDelete_1.setBounds(914, 169, 145, 37);
		panelUpdateReq.add(btnDelete_1);
				
		JLabel lbInstructions = new JLabel("Please enter Request ID to update."); //label
		lbInstructions.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lbInstructions.setBounds(10, 35, 207, 16);
		panelUpdateReq.add(lbInstructions);
		
		//this button and action even adds a request		
		JButton btnNewRequest = new JButton("Add New Request");
		btnNewRequest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String Date_of_Req, Desc_of_Req, Name_of_Tech, Notes;
				
				Date_of_Req =  txtDateofRequest.getText();
				Desc_of_Req = txtDescOfReq.getText();
				Name_of_Tech = txtTechAssigned.getText();
				Notes = txtAreaNotes.getText();
		         //Asks user if they are sure they want to add
		        int opt = JOptionPane.showConfirmDialog(null, "Are you sure you want to add a new request?", "Add New Request!", JOptionPane.YES_NO_OPTION);
		        if(opt==0) {
		        	try {
		        		//will delete from original table
						pst = con.prepareStatement("insert into requests(Date_of_Req, Desc_of_Req, Name_of_Tech, Notes)values(?,?,?,?)");
						pst.setString(1, Date_of_Req); 
						pst.setString(2, Desc_of_Req);
						pst.setString(3, Name_of_Tech);
						pst.setString(4, Notes);
								            
						pst.executeUpdate();
						JOptionPane.showMessageDialog(null, "Request Added");
						table_load();
								          
						txtDateofRequest.setText("");
						txtDescOfReq.setText("");
						txtTechAssigned.setText("");
						txtAreaNotes.setText("");
						txtid.requestFocus();
						}
		        	catch (SQLException e1) {
		        		JOptionPane.showMessageDialog(null, "Please do not add an ID or Completion Date. This request did not add! Please try again!");
						 e1.printStackTrace();
							}
		        	}	
			}
		});
		btnNewRequest.setBounds(340, 206, 145, 37);
		panelUpdateReq.add(btnNewRequest);
				
		//this buttons clears everything
		JButton btnClear = new JButton("Clear All");
		btnClear.setBounds(914, 107, 145, 37);
		panelUpdateReq.add(btnClear);
				
		JLabel lblyyyymmdd_1 = new JLabel("(yyyy-mm-dd)"); //label
		lblyyyymmdd_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblyyyymmdd_1.setBounds(211, 71, 113, 14);
		panelUpdateReq.add(lblyyyymmdd_1);
		
		//Clears all items
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent e) {
				txtid.setText("");
				txtDateofRequest.setText("");
				txtDescOfReq.setText(""); 
				txtTechAssigned.setText("");
				textDateComp.setText(""); 
				txtAreaNotes.setText("");
				txtid.requestFocus(); 
			}
		});
		
		//this button and action event deletes request selected
		btnDelete_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String btnDelete;
				btnDelete  = txtid.getText();
				//Asks user if they are sure they want to delete
				int opt = JOptionPane.showConfirmDialog(null, "Are you sure you want to Delete Request?", "Delete", JOptionPane.YES_NO_OPTION);
				if(opt==0) {
					try {
						//will delete from original table
						pst = con.prepareStatement("delete from requests where id =?");
						pst.setString(1, btnDelete);
						pst.executeUpdate();
						JOptionPane.showMessageDialog(null, "Request Deleted");
						table_load();
        		   
						txtDateofRequest.setText("");
						txtDescOfReq.setText("");
						txtTechAssigned.setText("");
						txtAreaNotes.setText("");
						txtid.requestFocus();
						}
					catch (SQLException e1) {
						JOptionPane.showMessageDialog(null, "This request did not delete! Please try again!");
						e1.printStackTrace();
							}
                		}	
			}
		});
		
		//this button  and Action even updates
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String Comp_Date, Notes, btId;
				Comp_Date = textDateComp.getText();
				Notes = txtAreaNotes.getText();
				btId = txtid.getText();
				// ask user if they are sure they want to update
				int opt = JOptionPane.showConfirmDialog(null, "Are you sure you want to Complete the request?", "Update Request", JOptionPane.YES_NO_OPTION);
				if(opt==0) {
						try { 
							//once completed request and date added the date will only update the original request table
							pst = con.prepareStatement("update requests set Comp_Date=?, Notes=? where id =?"); 

							pst.setString(1, Comp_Date); 
							pst.setString(2, Notes);
							pst.setString(3, btId);
							
							pst.executeUpdate();
							JOptionPane.showMessageDialog(null, "Updated!");
							table_load();
							
							txtDateofRequest.setText("");
					    	txtDescOfReq.setText("");
					    	txtTechAssigned.setText("");
					    	txtid.requestFocus();
							}
						  catch (SQLException e1) {
							  JOptionPane.showMessageDialog(null, "You can only update Comp_Date and Notes! Please try again!");
							  e1.printStackTrace(); 
						  	}
						}
			}
		});
		
		//this button and action event exits program
		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});	
		btnExit.setBounds(561, 671, 98, 44);
		frmRequestTracking.getContentPane().add(btnExit);
		
		// adds a scrollpane
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 311, 1212, 341);
		frmRequestTracking.getContentPane().add(scrollPane);
		//table from the report
		table = new JTable();
		scrollPane.setViewportView(table);
		
	}
}
