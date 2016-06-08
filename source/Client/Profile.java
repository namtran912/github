package Client;
import java.awt.EventQueue;

import javax.swing.AbstractListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import Sever.Sever;
import pojo.Member;
import pojo.Relationship;

import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class Profile {

	private JList listFriend;
	private static String username;
	private JFrame frmProfile;
	private List<Chat> windowChat = new ArrayList<Chat>();
	private static Socket socket;
	private static BufferedReader reader;
	private static BufferedWriter writer;

	/**
	 * Launch the application.
	 */
	public static void main(Object[] objects) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					username = (String)objects[0];
					socket = (Socket)objects[1];
					
					reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
					
					Profile window = new Profile();
					window.frmProfile.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Profile() {
		initialize();
	}

	private DefaultListModel GetListFriend() {
		DefaultListModel model = new DefaultListModel();
		try
		{
			writer.write(Sever.GetListFriends + username + Sever.Blank);
			writer.newLine();
			writer.flush();
				
			String message = reader.readLine();
			int i = 0;
			if (!message.equals(""))
				while (true) {
					model.add(i++, message.substring(0, message.indexOf(Sever.Blank)));
					if (message.indexOf(Sever.Blank) == message.lastIndexOf(Sever.Blank))
						break;
					message = message.substring(message.indexOf(Sever.Blank) + 4, message.length());
				}
			
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		return model;
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmProfile = new JFrame();
		frmProfile.setTitle(username);
		frmProfile.setResizable(false);
		frmProfile.setBounds(100, 100, 266, 426);
		frmProfile.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmProfile.getContentPane().setLayout(null);
		
		JLabel lblUserName = new JLabel("Username:");
		lblUserName.setBounds(10, 15, 99, 14);
		frmProfile.getContentPane().add(lblUserName);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 107, 240, 241);
		frmProfile.getContentPane().add(scrollPane);
		
		listFriend = new JList(GetListFriend());
		scrollPane.setViewportView(listFriend);
		
		JLabel lblFriends = new JLabel("Friends:");
		lblFriends.setBounds(10, 82, 46, 14);
		frmProfile.getContentPane().add(lblFriends);
		
		JLabel lblStatus = new JLabel("Status:");
		lblStatus.setBounds(10, 48, 46, 14);
		frmProfile.getContentPane().add(lblStatus);
		
		JButton btnChat = new JButton("Chat");
		btnChat.setBounds(161, 78, 89, 23);
		frmProfile.getContentPane().add(btnChat);
		
		JButton btnAddFriend = new JButton("Add friend");
		btnAddFriend.setBounds(151, 359, 99, 23);
		frmProfile.getContentPane().add(btnAddFriend);
		
		JTextField txtUsername = new JTextField();
		txtUsername.setBounds(13, 359, 128, 25);
		frmProfile.getContentPane().add(txtUsername);
		txtUsername.setColumns(10);
		
		JButton btnLogOut = new JButton("Log out");
		btnLogOut.setBounds(161, 11, 89, 23);
		frmProfile.getContentPane().add(btnLogOut);
		
		JLabel lblName = new JLabel("");
		lblName.setText(username);
		lblName.setBounds(80, 15, 77, 14);
		frmProfile.getContentPane().add(lblName);
		
		JComboBox cbbStatus = new JComboBox();
		cbbStatus.setModel(new DefaultComboBoxModel(new String[] {"Online", "Away", "Invisible"}));
		cbbStatus.setBounds(63, 45, 88, 20);
		frmProfile.getContentPane().add(cbbStatus);
		
		btnLogOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LogOut();
			}
		});
		
		btnChat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Chat.main(new Object[]{username, listFriend.getSelectedValuesList().get(0).toString(), socket});
			}
		});
		
		btnAddFriend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AddFriend(txtUsername.getText());
			}
		});
		
		cbbStatus.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				UpdateStatus(e.getItem().toString());
			}
		});
		
		listFriend.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				
			}
		});
	}

	private void UpdateStatus(String status) {
		try
		{
			writer.write(Sever.UpdateStatus + username + Sever.Blank + status);
			writer.newLine();
			writer.flush();
				
			String message = reader.readLine();
			if (message.equals(Sever.Success)) {
			}
			else 
				JOptionPane.showMessageDialog(frmProfile, message);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}	
	}

	private void AddFriend(String username) {
		if (username.equals("")){
			JOptionPane.showMessageDialog(frmProfile, "Enter username!");
			return;
		}
		
		try
		{
			writer.write(Sever.AddFriend + this.username + Sever.Blank + username);
			writer.newLine();
			writer.flush();
				
			String message = reader.readLine();
			if (message.equals(Sever.Success)) {
				((DefaultListModel) listFriend.getModel()).addElement(username);
			}
			else 
				JOptionPane.showMessageDialog(frmProfile, message);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
	}

	private void LogOut() {
		try
		{
			writer.write(Sever.UpdateStatus + username + Sever.Blank + "Offline");
			writer.newLine();
			writer.flush();
			
			String message = reader.readLine();
			
			writer.write(Sever.Logout);
			writer.newLine();
			writer.flush();
				
			message = reader.readLine();
			
			for (Chat chat : windowChat)
				chat.Exit();
			frmProfile.setVisible(false);	
			LogIn.main(null);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}	
	}
}
