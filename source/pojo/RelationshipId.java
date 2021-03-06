// default package
// Generated May 27, 2016 5:11:05 PM by Hibernate Tools 4.3.1.Final
package pojo;

import java.util.Arrays;

/**
 * RelationshipId generated by hbm2java
 */
public class RelationshipId implements java.io.Serializable {

	private String username1;
	private String username2;

	public RelationshipId() {
	}

	public RelationshipId(String username1, String username2) {
		this.username1 = username1;
		this.username2 = username2;
	}

	public String getUsername1() {
		return this.username1;
	}

	public void setUsername1(String username1) {
		this.username1 = username1;
	}

	public String getUsername2() {
		return this.username2;
	}

	public void setUsername2(String username2) {
		this.username2 = username2;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof RelationshipId))
			return false;
		RelationshipId castOther = (RelationshipId) other;

		return ((this.getUsername1() == castOther.getUsername1()) || (this.getUsername1() != null
				&& castOther.getUsername1() != null && this.getUsername1().equalsIgnoreCase(castOther.getUsername1())))
				&& ((this.getUsername2() == castOther.getUsername2())
						|| (this.getUsername2() != null && castOther.getUsername2() != null
								&& this.getUsername2().equals(castOther.getUsername2())));
	}

	public int hashCode() {
		int result = 17;

		int username1Hashcode = 0;
		String username1Property = this.getUsername1();
		if (username1Property != null) {
			username1Hashcode = 1;
			for (int i = 0; i < username1Property.length(); i++) {
				username1Hashcode = 37 * username1Hashcode + username1Property.charAt(i);
			}
		}

		result = 37 * result + username1Hashcode;

		int username2Hashcode = 0;
		String username2Property = this.getUsername2();
		if (username2Property != null) {
			username2Hashcode = 1;
			for (int i = 0; i < username2Property.length(); i++) {
				username2Hashcode = 37 * username2Hashcode + username2Property.charAt(i);
			}
		}

		result = 37 * result + username2Hashcode;

		return result;
	}

}
