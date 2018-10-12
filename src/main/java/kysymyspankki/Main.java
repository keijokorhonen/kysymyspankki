package kysymyspankki;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Spark;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

public class Main {

    public static void main(String[] args) throws SQLException, Exception {
        if (System.getenv("PORT") != null) {
            Spark.port(Integer.valueOf(System.getenv("PORT")));
        }
        
        Connection connection = getConnection();
        
        PreparedStatement statement = connection.prepareStatement("SELECT kurssi FROM Kysymys");
        ResultSet result = statement.executeQuery();
        
        List<String> kurssit = new ArrayList<>();
        
        while (result.next()) {
            String kurssi = result.getString("kurssi");
            kurssit.add(kurssi);
        }

        Spark.get("*", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("teksti", "Hei maailma!");

            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());
        
        connection.close();
    }

    public static Connection getConnection() throws Exception {
        String dbUrl = System.getenv("JDBC_DATABASE_URL");
        if (dbUrl != null && dbUrl.length() > 0) {
            return DriverManager.getConnection(dbUrl);
        }
        
        return DriverManager.getConnection("jdbc:sqlite:kysymyspankki.db");
    }
}
