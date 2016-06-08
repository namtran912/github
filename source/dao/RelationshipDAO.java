package dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import pojo.Member;
import pojo.Relationship;
import pojo.RelationshipId;

public class RelationshipDAO {
	public static boolean addFriend(Member member, Member member2) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = null;
		Relationship relationship = new Relationship(new RelationshipId(member.getUsername(), member2.getUsername()), member, member2);
		if (getRelationship(relationship.getId()) != null)
			return false;
		try {
			transaction = session.beginTransaction();
			session.save(relationship);
			transaction.commit();
		}
		catch (HibernateException ex) {
			transaction.rollback();
			System.err.println(ex);
			return false;
		}
		finally{
			session.close();
		}
		return true;
	}
	
	public static Relationship getRelationship(RelationshipId id){
		Session session = HibernateUtil.getSessionFactory().openSession();
		Relationship rls = null;
		try {
			rls = (Relationship) session.get(Relationship.class, id);
		}
		catch (HibernateException ex) {
			System.err.println(ex);		
		}
		finally{
			session.close();
		}
		return rls;
	}
	
	public static List<Member> getListFriend(Member member) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<Member> friends = null;
		try {
			String hql = "select relationship.memberByUsername2 from Relationship relationship where relationship.memberByUsername1 = :member";
			Query query = session.createQuery(hql)
					.setParameter("member", member);
			friends = query.list();
		}
		catch (HibernateException ex) {
			System.err.println(ex);		
		}
		finally{
			session.close();
		}
		return friends;
	}
}
