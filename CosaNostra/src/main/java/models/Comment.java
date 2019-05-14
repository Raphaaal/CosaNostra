package models;

import java.sql.Date;

public class Comment {

	private int userId;
	private String userName;
	private Date creationDate;
	private String artworkId;
	private String comment;

	public Comment(int userId, String userName, Date creationDate, String artworkId, String comment) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.creationDate = creationDate;
		this.artworkId = artworkId;
		this.comment = comment;
	}

	/**
	 * @return the userId
	 */
	public int getUserId() {
		return userId;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @return the creationDate
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * @return the artwork
	 */
	public String getArtwork() {
		return artworkId;
	}

	/**
	 * @return the commentary
	 */
	public String getCommentary() {
		return comment;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @param creationDate the creationDate to set
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * @param artwork the artwork to set
	 */
	public void setArtwork(String artworkId) {
		this.artworkId = artworkId;
	}

	/**
	 * @param commentary the commentary to set
	 */
	public void setCommentary(String comment) {
		this.comment	=comment;
	}

}
