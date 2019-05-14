package queries;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

import connect.ConnectionUtils;
import models.User;

public class CommentQuery {

	public static void main(String[] args) {
		insertNewComment(1, "bonjour", "ok","ce truc est nul");
	}
	/**
	 * 
	 * @param userId
	 * @param passw
	 * @param email
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static boolean insertNewComment(int userId, String userName, String artworkId,
			String comment) {

		// Get Connection
		try (Connection connection = (Connection) ConnectionUtils.getMyConnection();) {
			// Create statement
			Statement statement = (Statement) connection.createStatement();

			String insert = "INSERT INTO comments (user_id,user_name,artwork_id,comment) VALUES ('" + userId + "','"
					+ userName+"','" + artworkId + "','" + comment + "');";
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
			if (rs.next()) {
				String email = rs.getString("email");
				String name = rs.getString("name");
				return new User(id, email, name, null);
			}

			return null;

		}

	}

}