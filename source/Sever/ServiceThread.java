package Sever;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.List;

import dao.MemberDAO;
import dao.RelationshipDAO;
import pojo.Member;

public class ServiceThread extends Thread{
	private int clientNumber;
    private Socket socketOfServer;

    public ServiceThread(Socket socketOfServer, int clientNumber) {
        this.clientNumber = clientNumber;
        this.socketOfServer = socketOfServer;
    }

    @Override
    public void run() {

        try {

        	BufferedReader reader = new BufferedReader(new InputStreamReader(socketOfServer.getInputStream()));
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socketOfServer.getOutputStream()));

            while (true) {
            	String message = reader.readLine();
				if (message == null)
					continue;
				
				String result;
				if ((result = ProcessMessage(message)) == null)
					break;
				
				writer.write(result);
				writer.newLine();
				writer.flush();
            }
            
            writer.close();
			reader.close();

        } catch (IOException e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }
    
    private static String ProcessMessage(String message) {
		String request = message.substring(0, 4);
		String content = message.substring(4, message.length()); 
		String result = Sever.Success;
		
		switch (request) {
			case Sever.SignUp:{
				String username = content.substring(0, content.indexOf(Sever.Blank));
				String password = content.substring(content.indexOf(Sever.Blank) + 4, content.length());
				
				result = CreateAccount(username, password);
				break;
			}
			case Sever.SignIn:{
				String username = content.substring(0, content.indexOf(Sever.Blank));
				String password = content.substring(content.indexOf(Sever.Blank) + 4, content.length());
				
				result = CheckAccountToSignIn(username, password);
				break;
			}
			case Sever.AddFriend: {
				String username = content.substring(0, content.indexOf(Sever.Blank));
				String username2 = content.substring(content.indexOf(Sever.Blank) + 4, content.length());
				
				result = AddFriend(username, username2);
				break;
			}
			case Sever.GetListFriends: {
				String username = content.substring(0, content.indexOf(Sever.Blank));
				
				result = GetListFriends(username);
				break;
			}
			case Sever.UpdateStatus: {
				String username = content.substring(0, content.indexOf(Sever.Blank));
				String status = content.substring(content.indexOf(Sever.Blank) + 4, content.length());
				
				result = UpdateStatus(username, status);
				break;
			}
			case Sever.Logout:
				result = null;
				break;
			default :
				break;
		}
		return result;
	}
	
	private static String GetListFriends(String username) {
		List<Member> listFriends = RelationshipDAO.getListFriend(MemberDAO.getMember(username));
		String list = "";
		for (int i = 0; i < listFriends.size(); i++) {
			String status = listFriends.get(i).getStatus();
			if (status.equals("Invisible"))
				status = "Offline";
			list += listFriends.get(i).getUsername() + " - " + status + Sever.Blank;
		}
		return list;
	}

	private static String AddFriend(String username, String username2) {
		Member member2 = MemberDAO.getMember(username2);
		if (member2 == null)
			return Sever.AccountNonExisted;
		
		Member member = MemberDAO.getMember(username);
		if (RelationshipDAO.addFriend(member, member2))
			return Sever.Success;
		return Sever.AddFriendFail;
	}

	private static String CheckAccountToSignIn(String username, String password) {
		if (MemberDAO.isExist(username, password))
			return Sever.Success;
		return Sever.AccountIncorrect;
		
	}

	private static String CreateAccount(String username, String password) {
		Member mem = new Member(username, password, "Online");
		if (MemberDAO.addMember(mem))
			return Sever.Success;
		return Sever.AccountExisted;
	}
	
	private static String UpdateStatus(String username, String status) {
		MemberDAO.updateStatus(username, status);
		return Sever.Success;
	}
}
