import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

import connect.ConnectionUtils;

public class UserQuery {
	
	
	/**
	 * simple test 
	 * @param args
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		
		// Get Connection
		try (Connection connection = (Connection) ConnectionUtils.getMyConnection();) {
			
			// Create statement
			Statement statement = (Statement) connection.createStatement();

			String sql = "Select ID, login, password from user";

			// Execute SQL statement returns a ResultSet object.
			ResultSet rs = statement.executeQuery(sql);

			// Fetch on the ResultSet
			// Move the cursor to the next record.
			while (rs.next()) {
				int id = rs.getInt("ID");
				String login = rs.getString(2);
				String password = rs.getString("password");
				System.out.println("--------------------");
				System.out.println("ID:" + id);
				System.out.println("login:" + login);
				System.out.println("password:" + password);
			}
		} catch (Exception e) {
			System.out.println(e);
		}

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
			
			String insert = "INSERT INTO user (login,password,email) VALUES ('" + login + "','" + password + "','"+email+"');";
			System.out.println(insert);
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
	
	

}