import java.sql.Connection;

public class DBTest {

    public static void main(String[] args) {

        Connection connection = DatabaseConnection.getConnection();

        if (connection != null) {
            System.out.println("Java is connected to MySQL!");
        }
    }
}