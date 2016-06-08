package Sever;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import javax.swing.JOptionPane;

import dao.MemberDAO;
import dao.RelationshipDAO;
import pojo.Member;

public class Sever {
	
	public final static String Success = "Success!";
	public final static String ConnectUnsuccess = "Connection unsuccess!";
	public final static String AccountIncorrect = "Username or password is incorrect!";
	public final static String AccountExisted = "Username already exists!";
	public final static String AccountNonExisted = "Username does not exist!";
	public final static String AddFriendFail = "Can not add this username!";
	
	public final static String Blank = "#000";
	public final static String SignIn = "#001";
	public final static String SignUp = "#002";
	public final static String AddFriend = "#003";
	public final static String GetListFriends = "#004";
	public final static String UpdateStatus = "#005";
	public final static String Logout = "#010";

	public static void main(String[] args) throws IOException {
		ServerSocket listener = null;
		int clientNumber = 0;

		try {
			listener = new ServerSocket(3200);
		} 
		catch (IOException e) {
			System.out.println("Error!");
	    }
	 
	    try {
	    	while (true) {
	    	Socket socketOfServer = listener.accept();
	        new ServiceThread(socketOfServer, clientNumber++).start();
	    	}
	    } 
	    finally {
	           listener.close();
	    }
	}
}
