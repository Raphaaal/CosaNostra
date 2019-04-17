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
		System.out.println(getUser("youb"));
	}

	/**
	 * Create a new user
	 * 
	 * @param login
	 * @param password
	 * @param mail
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static boolean insertNewUser(String login, String password, String email)
			throws ClassNotFoundException, SQLException {
		// Get Connection
		try (Connection connection = (Connection) ConnectionUtils.getMyConnection();) {
			// Create statement
			Statement statement = (Statement) connection.createStatement();

			String insert = "INSERT INTO user (login,password,email) VALUES ('" + login + "','" + password + "','"
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

	public static User getUser(String login) throws ClassNotFoundException, SQLException {
		// Get Connection
		try (Connection connection = (Connection) ConnectionUtils.getMyConnection();) {

			// Create statement
			Statement statement = (Statement) connection.createStatement();

			String sql = "Select ID, login, password,email from user where login='" + login + "';";
			// Execute SQL statement returns a ResultSet object.
			System.out.println(sql);
			ResultSet rs = statement.executeQuery(sql);
			if(rs.next()) {
				System.out.println(rs.getString("id"));
				System.out.println(rs.getString("password"));
				System.out.println(rs.getString("login"));
				
				
				String email=rs.getString("email");
				String password=rs.getString("password");
				int id=Integer.parseInt(rs.getString("id"));
				return new User(id,email,login,password);
			}

			return null;

		}

	}

}