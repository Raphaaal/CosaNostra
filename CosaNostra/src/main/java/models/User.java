package models;

public class User {

	
	private String email;
	private String name;
	private String password;
	private  int id;
	
	public User(int id,String email, String name, String password) {
		this.id=id;
		this.email = email;
		this.name = name;
		this.password = password;
	}
	
	public User() {}
	
	
	@Override
	public String toString() {
		return "User [email=" + email + ", name=" + name + ", password=" + password + "]";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
