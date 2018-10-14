/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kysymyspankki.dao;

import java.sql.*;
import java.util.*;
import kysymyspankki.database.Database;
import kysymyspankki.domain.Kysymys;
import kysymyspankki.domain.Vastaus;

public class VastausDao implements Dao<Vastaus, Integer> {

    private Database database;
    private Dao<Kysymys, Integer> kysymysDao;

    public VastausDao(Database database, Dao<Kysymys, Integer> kysymysDao) {
        this.database = database;
        this.kysymysDao = kysymysDao;
    }

    @Override
    public Vastaus findOne(Integer key) throws SQLException {
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Vastaus WHERE id=?");
        stmt.setInt(1, key);

        ResultSet rs = stmt.executeQuery();
        if (!rs.next()) {
            return null;
        }

        Kysymys kysymys = this.kysymysDao.findOne(rs.getInt("kysymys_id"));

        Vastaus v = new Vastaus(key, kysymys, rs.getString("vastausteksti"), rs.getBoolean("oikein"));

        stmt.close();
        rs.close();
        conn.close();

        return v;
    }

    @Override
    public List<Vastaus> findAll() throws SQLException {
        List<Vastaus> vastaukset;

        try (Connection connection = database.getConnection()) {

            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Vastaus");

            ResultSet rs = stmt.executeQuery();

            vastaukset = new ArrayList<>();

            while (rs.next()) {

                Kysymys kysymys = kysymysDao.findOne(rs.getInt("kysymys_id"));
                
                Vastaus vastaus = new Vastaus(rs.getInt("id"), kysymys,
                        rs.getString("vastausteksti"), rs.getBoolean("oikein"));

                vastaukset.add(vastaus);

            }

            stmt.close();

            rs.close();

        }

        return vastaukset;
    }

    @Override
    public Vastaus saveOrUpdate(Vastaus object) throws SQLException {
        if (object.getId() == null) {
            return save(object);

        } else {

            return update(object);

        }
    }

    @Override
    public void delete(Integer key) throws SQLException {
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM Vastaus WHERE id = ?");

        stmt.setInt(1, key);
        stmt.executeUpdate();

        stmt.close();
        conn.close();
    }

    private Vastaus save(Vastaus vastaus) throws SQLException {

        Connection conn = database.getConnection();

        PreparedStatement stmt = conn.prepareStatement("INSERT INTO Vastaus"
                + " (kysymys_id, vastausteksti, oikein)"
                + " VALUES (?, ?, ?)");

        stmt.setInt(1, vastaus.getKysymys().getId());

        stmt.setString(2, vastaus.getVastausteksti());

        stmt.setBoolean(3, vastaus.getOikein());

        stmt.executeUpdate();

        stmt.close();

        stmt = conn.prepareStatement("SELECT * FROM Vastaus"
                + " WHERE kysymys_id = ? AND vastausteksti = ? AND oikein = ?");

        stmt.setInt(1, vastaus.getKysymys().getId());
        stmt.setString(2, vastaus.getVastausteksti());
        stmt.setBoolean(3, vastaus.getOikein());

        ResultSet rs = stmt.executeQuery();

        rs.next();

        Kysymys kysymys = kysymysDao.findOne(rs.getInt("kysymys_id"));

        Vastaus v = new Vastaus(rs.getInt("id"), kysymys,
                rs.getString("vastausteksti"), rs.getBoolean("oikein"));

        stmt.close();

        rs.close();

        conn.close();

        return v;

    }

    private Vastaus update(Vastaus vastaus) throws SQLException {

        Connection conn = database.getConnection();

        PreparedStatement stmt = conn.prepareStatement("UPDATE Vastaus SET"
                + " kysymys_id = ?, vastausteksti = ?, oikein = ? WHERE id = ?");

        stmt.setInt(1, vastaus.getKysymys().getId());

        stmt.setString(2, vastaus.getVastausteksti());

        stmt.setBoolean(3, vastaus.getOikein());

        stmt.setInt(4, vastaus.getId());

        stmt.executeUpdate();

        stmt.close();

        conn.close();

        return vastaus;

    }
}
