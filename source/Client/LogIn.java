package Client;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.AbstractListModel;
import javax.swing.JButton;
import javax.swing.JTextField;

import pojo.Member;
import Sever.Sever;

import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class LogIn {

	private JFrame frmLogin;
	private JPasswordField txtPassword;
	private Socket socket;
	private BufferedReader reader;
	private BufferedWriter writer;
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LogIn window = new LogIn();
					window.frmLogin.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public LogIn() {
		String lookAndFeel = "Nimbus";
		try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if (lookAndFeel.equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(LogIn.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LogIn.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LogIn.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LogIn.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
		
		initialize();
		if (!ConnectToSever()) {
			JOptionPane.showMessageDialog(frmLogin, Sever.ConnectUnsuccess);
			System.exit(0);
		}		
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmLogin = new JFrame();
		frmLogin.setTitle("Log in");
		frmLogin.setResizable(false);
		frmLogin.setBounds(100, 100, 345, 300);
		frmLogin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmLogin.getContentPane().setLayout(null);
		
		JTextField txtUserName = new JTextField();
		txtUserName.setBounds(160, 97, 130, 25);
		frmLogin.getContentPane().add(txtUserName);
		txtUserName.setColumns(10);
		
		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setBounds(58, 100, 81, 14);
		frmLogin.getContentPane().add(lblUsername);
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setBounds(58, 141, 81, 14);
		frmLogin.getContentPane().add(lblPassword);
		
		JButton btnSignIn = new JButton();
		btnSignIn.setText("Sign In");
		btnSignIn.setBounds(58, 189, 89, 23);
		frmLogin.getContentPane().add(btnSignIn);
		
		JButton btnSignUp = new JButton("Sign Up");
		btnSignUp.setBounds(204, 189, 89, 23);
		frmLogin.getContentPane().add(btnSignUp);
		
		txtPassword = new JPasswordField();
		txtPassword.setBounds(160, 138, 130, 25);
		frmLogin.getContentPane().add(txtPassword);
		
		Runtime.getRuntime().addShutdownHook(new Thread()
		{
		    @Override
		    public void run()
		    {
		    	try {
		    		writer.write(Sever.Logout);
					writer.newLine();
					writer.flush();
					
					writer.close();
					reader.close();
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		    }
		});
		
		txtPassword.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					btnSignIn.doClick();
			}
		});
		
		txtUserName.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					if (txtPassword.getText().equals(""))
						txtPassword.requestFocus();
					else
						btnSignIn.doClick();
			}
		});
		
		btnSignUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SignUp(txtUserName.getText(), txtPassword.getText());
			}
		});	
		
		btnSignIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SignIn(txtUserName.getText(), txtPassword.getText());
			}
		});
	}

	private void SignUp(String username, String password) {
		if (username.equals("") || password.equals("")){
			JOptionPane.showMessageDialog(frmLogin, "Username and password must not be empty!");
			return;
		}
		
		try
		{
			writer.write(Sever.SignUp + username + Sever.Blank + password);
			writer.newLine();
			writer.flush();
				
			String message = reader.readLine();
			if (message.equals(Sever.Success)) {
				Profile.main(new Object[]{username, socket});
				frmLogin.setVisible(false);
			}
			else 
				JOptionPane.showMessageDialog(frmLogin, message);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	private void SignIn(String username, String password) {
		if (username.equals("") || password.equals("")){
			JOptionPane.showMessageDialog(frmLogin, "Username and password must not be empty!");
			return;
		}
		
		try
		{
			writer.write(Sever.SignIn + username + Sever.Blank + password);
			writer.newLine();
			writer.flush();
				
			String message = reader.readLine();
			if (message.equals(Sever.Success)) {
				writer.write(Sever.UpdateStatus + username + Sever.Blank + "Online");
				writer.newLine();
				writer.flush();
				
				message = reader.readLine();
				
				Profile.main(new Object[]{username, socket});
				frmLogin.setVisible(false);		
			}
			else 
				JOptionPane.showMessageDialog(frmLogin, message);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private boolean ConnectToSever() {
		try {
			socket = new Socket("localhost",3200);
			
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		} 
		catch (UnknownHostException e) {
			e.printStackTrace();
			return false;
		} 
		catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return socket.isConnected();
	}
}
