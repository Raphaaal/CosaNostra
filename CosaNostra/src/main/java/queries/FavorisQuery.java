package queries;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import connect.ConnectionUtils;
import models.Favoris;
import models.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FavorisQuery {

	/**
	 * Create a new favoris
	 * 
	 * @param user_id
	 * @param page_id
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static boolean insertNewFavoris(String user_id, String page_id)
			throws ClassNotFoundException, SQLException {

		System.out.println("USER ID" + user_id);
		System.out.println("PAGE_ID" + page_id);
		// Get Connection
		try (Connection connection = (Connection) ConnectionUtils.getMyConnection();) {
			// Create statement
			Statement statement = (Statement) connection.createStatement();

			String insert = "INSERT INTO favoris (user_id, page_id) VALUES ('" + user_id + "','" + page_id + "');";
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

	public static Favoris getFavoris(String user_id, String page_id) throws ClassNotFoundException, SQLException {
		// Get Connection
		try (Connection connection = (Connection) ConnectionUtils.getMyConnection();) {

			// Create statement
			Statement statement = (Statement) connection.createStatement();

			String sql = "Select ID, user_id, page_id from favoris where user_id='" + user_id + "'and page_id = '"+ page_id + "';";
			// Execute SQL statement returns a ResultSet object.

			ResultSet rs = statement.executeQuery(sql);
			if(rs.next()) {
				int id=Integer.parseInt(rs.getString("id"));
				return new Favoris(id,user_id, page_id);
			}

			return new Favoris();
		}
	}

	public static boolean RemoveFavoris(String user_id, String page_id) throws ClassNotFoundException, SQLException {

		// Get Connection
		try (Connection connection = (Connection) ConnectionUtils.getMyConnection();) {
			// Create statement
			Statement statement = (Statement) connection.createStatement();

			String sql = "DELETE from favoris where user_id='" + user_id + "'and page_id = '"+ page_id + "';";
			statement.executeUpdate(sql);

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