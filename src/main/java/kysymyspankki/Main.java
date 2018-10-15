package kysymyspankki;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import kysymyspankki.dao.KysymysDao;
import kysymyspankki.dao.VastausDao;
import kysymyspankki.database.Database;
import kysymyspankki.domain.Kysymys;
import kysymyspankki.domain.Vastaus;
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
        
        Spark.get("/kysymykset/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            int kysymysId = Integer.parseInt(req.params("id"));
            
            map.put("kysymys", kysymysDao.findOne(kysymysId));
            map.put("vastaukset", vastausDao.findAllByKysymysId(kysymysId));
            return new ModelAndView(map, "kysymys");
        }, new ThymeleafTemplateEngine());
        
        Spark.post("/kysymykset/:id", (req, res) -> {
            String vastausteksti = req.queryParams("vastausteksti");
            String checkboxValue = req.queryParams("oikein");
            
            System.out.println("This is the checkbox value: " + checkboxValue);
            
            int kysymysId = Integer.parseInt(req.params("id"));
            
            Boolean oikein = null;
            
            if (checkboxValue.equals("on")) {
                oikein = true;
            } else {
                oikein = false;
            }
            
            Kysymys kysymys = kysymysDao.findOne(Integer.parseInt(req.params("id")));
            
            vastausDao.saveOrUpdate(new Vastaus(null, kysymys, vastausteksti, oikein));
            res.redirect("/kysymykset/" + kysymysId);
            return "";
        });
        
        Spark.post("/poista/:id", (req, res) -> {
            kysymysDao.delete(Integer.parseInt(req.params("id")));
            res.redirect("/");
            return "";
        });
    }
}