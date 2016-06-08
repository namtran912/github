// default package
// Generated May 27, 2016 5:11:05 PM by Hibernate Tools 4.3.1.Final
package pojo;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * GroupChat generated by hbm2java
 */
public class GroupChat implements java.io.Serializable {

	private String id;
	private String name;
	private String message;
	private Set members = new HashSet(0);

	public GroupChat() {
	}

	public GroupChat(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public GroupChat(String id, String name, String message, Set members) {
		this.id = id;
		this.name = name;
		this.message = message;
		this.members = members;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Set getMembers() {
		return this.members;
	}

	public void setMembers(Set members) {
		this.members = members;
	}

}
