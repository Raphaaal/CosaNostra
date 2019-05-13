package queries;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

import connect.ConnectionUtils;
import models.User;

public class UserQuery {

	/**
	 * simple test
	 * 
	 * @param args
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		System.out.println(getUser("victorfritz.ici@gmail.com", "azerty"));
	}

	/**
	 * Create a new user
	 * 
	 * @param name
	 * @param password
	 * @param email
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static boolean insertNewUser(String name, String password, String email)
			throws ClassNotFoundException, SQLException {
		// Get Connection
		try (Connection connection = (Connection) ConnectionUtils.getMyConnection();) {
			// Create statement
			Statement statement = (Statement) connection.createStatement();

			String insert = "INSERT INTO users (name,password,email) VALUES ('" + name + "','" + password + "','"
					+ email + "');";
			statement.executeUpdate(insert);

		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
			return false;
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
			return false;
		}
		return true;

	}

	public static User getUser(String email, String password) throws ClassNotFoundException, SQLException {
		// Get Connection
		try (Connection connection = (Connection) ConnectionUtils.getMyConnection();) {

			// Create statement
			Statement statement = (Statement) connection.createStatement();

			String sql = "Select ID, name, password,email from users where email='" + email + "'and password = '"+ password + "';";
			// Execute SQL statement returns a ResultSet object.

			ResultSet rs = statement.executeQuery(sql);
			if(rs.next()) {
				System.out.println(rs.getString("id"));
				System.out.println(rs.getString("password"));
				System.out.println(rs.getString("name"));

				String name=rs.getString("name");
				int id=Integer.parseInt(rs.getString("id"));
				return new User(id,email,name,password);
			}

			return null;

		}

	}

	public static User getUser(int id) throws ClassNotFoundException, SQLException {
		System.out.println(id);
		// Get Connection
		try (Connection connection = (Connection) ConnectionUtils.getMyConnection();) {

			// Create statement
			Statement statement = (Statement) connection.createStatement();

			String sql = "Select ID, name ,email from users where id='" + id + "';";
			// Execute SQL statement returns a ResultSet object.

			System.out.println(sql);

			ResultSet rs = statement.executeQuery(sql);
			if(rs.next()) {
				String email=rs.getString("email");
				String name=rs.getString("name");
				return new User(id,email,name, null);
			}

			return null;

		}

	}

}