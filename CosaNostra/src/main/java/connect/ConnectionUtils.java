package connect;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionUtils {

	public static Connection getMyConnection() throws SQLException, ClassNotFoundException {

		return MySQLConnUtils.getMySQLConnection();
	}

	//
	// Test Connection ...
	//
	public static void main(String[] args) throws SQLException, ClassNotFoundException {

		System.out.println("Get connection ... ");

		// Get a Connection object
		try (Connection conn = ConnectionUtils.getMyConnection();) {

			System.out.println("Get connection " + conn);

			System.out.println("Done!");

		}
	}

}
