package queries;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

import connect.ConnectionUtils;
import models.Comment;
import models.User;

public class CommentQuery {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		System.out.println(getArtworkComment("ok").get(0));
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

	/**
	 * return ampty list if no result
	 * @param artworkId
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static List<Comment> getArtworkComment(String artworkId) throws ClassNotFoundException, SQLException {
		// Get Connection
		try (Connection connection = (Connection) ConnectionUtils.getMyConnection();) {

			// Create statement
			Statement statement = (Statement) connection.createStatement();

			String sql = "Select user_id, user_name ,creation_date,artwork_id,comment from comments where artwork_id ='" + artworkId + "';";
			// Execute SQL statement returns a ResultSet object.
			List<Comment> comments= new ArrayList<>();
			System.out.println(sql);

			ResultSet rs = statement.executeQuery(sql);
			while(rs.next()) {
			
				String userId = rs.getString("user_id");
				
				String userName = rs.getString("user_name");
				System.out.println(rs.getString("creation_date"));
				Timestamp creationDate=Timestamp.valueOf(rs.getString("creation_date"));
				

				
				System.out.println("apres creation");
				String comment=rs.getString("comment");
				
				Comment commentaire=new Comment(Integer.parseInt(userId),userName,creationDate,artworkId,comment);
				comments.add( commentaire);
			
			}

			return comments;

		}

	}
	

}