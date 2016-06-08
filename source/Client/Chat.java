package Client;
import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import javax.swing.SwingConstants;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Chat {

	private JFrame frmChat;
	private static String username;
	private static String username2;
	private static String status;
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
					username2 = (String)objects[1];
					status = username2.substring(username2.indexOf(" - ") + 3, username2.length());
					username2 = username2.substring(0, username2.indexOf(" - "));
					socket = (Socket)objects[2];
					
					reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
					
					Chat window = new Chat();
					window.frmChat.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Chat() {
		initialize();
	}
	
	public void Exit() {
		frmChat.setVisible(false);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmChat = new JFrame();
		frmChat.setResizable(false);
		frmChat.setTitle(username2);
		frmChat.setBounds(100, 100, 450, 449);
		frmChat.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmChat.getContentPane().setLayout(null);
		
		JTextArea txtChat = new JTextArea();
		txtChat.setBounds(10, 335, 304, 64);
		frmChat.getContentPane().add(txtChat);
		
		JButton btnSend = new JButton("Send");
		btnSend.setBounds(324, 336, 100, 63);
		frmChat.getContentPane().add(btnSend);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 66, 414, 231);
		frmChat.getContentPane().add(scrollPane);
		
		JList listMessage = new JList();
		scrollPane.setViewportView(listMessage);
		
		JLabel lblStatus = new JLabel(status);
		lblStatus.setBounds(378, 41, 46, 14);
		frmChat.getContentPane().add(lblStatus);
		
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
	}
}
