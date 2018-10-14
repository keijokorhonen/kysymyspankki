package kysymyspankki;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import kysymyspankki.dao.KysymysDao;
import kysymyspankki.dao.VastausDao;
import kysymyspankki.database.Database;
import kysymyspankki.domain.Kysymys;
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
        
        Database database = new Database("jdbc:sqlite:kysymyspankki.db");
        KysymysDao kysymysDao = new KysymysDao(database);
        VastausDao vastausDao = new VastausDao(database, kysymysDao);
        
        Spark.get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("kysymykset", kysymysDao.findAll());

            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());
        
        Spark.post("/", (req, res) -> {
            String kurssi = req.queryParams("kurssi");
            String aihe = req.queryParams("aihe");
            String kysymysteksti = req.queryParams("kysymysteksti");
            
            kysymysDao.saveOrUpdate(new Kysymys(null, kurssi, aihe, kysymysteksti));
            res.redirect("/");
            return "";
        });
    }
}