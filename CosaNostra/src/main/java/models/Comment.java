package models;


import java.sql.Date;

public class Comment {

	private int userId;
	private String userName;
	private Date creationDate;
	private String artworkId;
	private String comment;

	public Comment(){}

	public Comment(int userId, String userName, Date creationDate, String artworkId, String comment) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.creationDate = creationDate;
		this.artworkId = artworkId;
		this.comment = comment;
	}

	public Comment(int userId, String userName, String artworkId, String comment) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.creationDate = null;
		this.artworkId = artworkId;
		this.comment = comment;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getArtworkId() {
		return artworkId;
	}

	public void setArtworkId(String artworkId) {
		this.artworkId = artworkId;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public String toString() {
		return "Comment{" +
				"userId=" + userId +
				", userName='" + userName + '\'' +
				", creationDate=" + creationDate +
				", artworkId='" + artworkId + '\'' +
				", comment='" + comment + '\'' +
				'}';
	}
}
