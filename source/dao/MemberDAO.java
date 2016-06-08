package dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import pojo.Member;

public class MemberDAO {
	public static boolean addMember(Member mem) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		if (MemberDAO.getMember(mem.getUsername()) != null)
			return false;
		Transaction transaction = null;
		try {
				
			transaction = session.beginTransaction();
			session.save(mem);
			transaction.commit();
		}
		catch (HibernateException ex) {
			transaction.rollback();
			System.err.println(ex);	
		}
		finally{
			session.close();
		}
		return true;
	}
	
	public static Member getMember(String username){
		Session session = HibernateUtil.getSessionFactory().openSession();
		Member mem = null;
		try {
			mem = (Member) session.get(Member.class, username);
		}
		catch (HibernateException ex) {
			System.err.println(ex);		
		}
		finally{
			session.close();
		}
		return mem;
	}
	
	public static boolean isExist(String username, String password){
		Session session = HibernateUtil.getSessionFactory().openSession();
		Member mem = null;
		try {
			mem = (Member) session.get(Member.class, username);
			if (mem == null) 
				return false;
			if (mem.getPassword().equals(password))
				return true;
			else 
				return false;
		}
		catch (HibernateException ex) {
			System.err.println(ex);		
		}
		finally{
			session.close();
		}
		return false;
	}

	public static void updateStatus(String username, String status) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = null;
		try {
			Member member = MemberDAO.getMember(username);
			transaction = session.beginTransaction();
			session.update(new Member(member.getUsername(), member.getPassword(), status));
			transaction.commit();
		}
		catch (HibernateException ex) {
			transaction.rollback();
			System.err.println(ex);
			
		}
		finally{
			session.close();
		}	
	}
}
