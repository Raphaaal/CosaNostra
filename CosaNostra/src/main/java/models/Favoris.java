package models;

public class Favoris {


	private String page_id;
	private String user_id;
	private  int id;

	public Favoris(int id, String user_id, String page_id) {
		this.id=id;
		this.page_id = page_id;
		this.user_id = user_id;
	}

	public Favoris() {}


	public String getPage_id() {
		return page_id;
	}

	public void setPage_id(String page_id) {
		this.page_id = page_id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Favoris{" +
				"page_id='" + page_id + '\'' +
				", user_id='" + user_id + '\'' +
				", id=" + id +
				'}';
	}
}
