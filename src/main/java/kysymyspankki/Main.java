package kysymyspankki;

import java.sql.*;
import java.util.HashMap;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Spark;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

public class Main {

    public static void main(String[] args) throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:sqlite:kysymyspankki.db");

        Spark.get("*", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("teksti", "Hei maailma!");

            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());
    }

}
