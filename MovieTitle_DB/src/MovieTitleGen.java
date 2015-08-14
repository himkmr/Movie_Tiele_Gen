import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Scanner;

public class MovieTitleGen {
	static Connection conn = null;

	public static void main(String[] args) throws Exception {

		Scanner sc = new Scanner(System.in);
		try {

			// URL of Oracle database server
			String url = "jdbc:oracle:thin:testuser/password@localhost";

			// properties for creating connection to Oracle database
			Properties props = new Properties();
			props.setProperty("user", "testdb");
			props.setProperty("password", "password");

			// creating connection to Oracle database using JDBC
			conn = DriverManager.getConnection(url, props);

			String get_noun = "SELECT * FROM (SELECT * FROM nouns ORDER BY DBMS_RANDOM.RANDOM) WHERE rownum =1";

			PreparedStatement preStatement = conn.prepareStatement(get_noun);
			ResultSet result1 = preStatement.executeQuery();
			result1.next();
			String get_adjective = "SELECT * FROM (SELECT * FROM adjectives ORDER BY DBMS_RANDOM.RANDOM) WHERE rownum =1";

			// creating PreparedStatement object to execute query
			PreparedStatement preStatement2 = conn
					.prepareStatement(get_adjective);
			ResultSet result2 = preStatement2.executeQuery();
			result2.next();
			String noun = result1.getString("NAME");
			String adjective = result2.getString("NAME");
			System.out.println("Your Movie: " + noun + "  " + adjective);
			System.out.println("Enter description: ");
			String desc = sc.nextLine();
			String insert_query = "insert into movie_titles values('" + noun
					+ " " + adjective + "','" + desc + "')";
		//	System.out.println(insert_query);
			PreparedStatement preStatement3 = conn
					.prepareStatement(insert_query);
			preStatement3.executeQuery();

		} catch (SQLException e) {

			e.printStackTrace();
		}

		sc.close();
		conn.close();
	}

	/**
	 * @param url
	 *            - the URL to read words from
	 * @return An array of words, initialized from the given URL
	 */
	public static String[] arrayFromUrl(String url) throws Exception {
		Scanner fin = new Scanner((new URL(url)).openStream());
		int count = fin.nextInt();

		String[] words = new String[count];

		for (int i = 0; i < words.length; i++) {
			words[i] = fin.next();
		}
		fin.close();

		return words;
	}

}