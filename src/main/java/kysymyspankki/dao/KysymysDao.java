package kysymyspankki.dao;

import java.util.*;
import java.sql.*;
import kysymyspankki.database.Database;
import kysymyspankki.domain.Kysymys;

public class KysymysDao implements Dao<Kysymys, Integer> {

    private Database database;

    public KysymysDao(Database database) {
        this.database = database;
    }

    @Override
    public Kysymys findOne(Integer key) throws SQLException {
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Kysymys WHERE id=?");
        stmt.setInt(1, key);

        ResultSet rs = stmt.executeQuery();
        if (!rs.next()) {
            return null;
        }

        Kysymys k = new Kysymys(rs.getInt("id"), rs.getString("kurssi"), rs.getString("aihe"), rs.getString("kysymysteksti"));

        stmt.close();
        rs.close();
        conn.close();

        return k;
    }

    @Override
    public List<Kysymys> findAll() throws SQLException {
        List<Kysymys> kysymykset;

        try (Connection connection = database.getConnection()) {

            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Kysymys");

            ResultSet rs = stmt.executeQuery();

            kysymykset = new ArrayList<>();

            while (rs.next()) {

                Kysymys kysymys = new Kysymys(rs.getInt("id"), rs.getString("kurssi"),
                        rs.getString("aihe"), rs.getString("kysymysteksti"));

                kysymykset.add(kysymys);

            }

            stmt.close();

            rs.close();

        }

        return kysymykset;
    }

    @Override
    public Kysymys saveOrUpdate(Kysymys object) throws SQLException {
        if (object.getId() == null) {
            return save(object);

        } else {

            return update(object);

        }
    }

    @Override
    public void delete(Integer key) throws SQLException {
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM Kysymys WHERE id = ?");

        stmt.setInt(1, key);
        stmt.executeUpdate();

        stmt.close();
        conn.close();
    }

    private Kysymys save(Kysymys kysymys) throws SQLException {

        Connection conn = database.getConnection();

        PreparedStatement stmt = conn.prepareStatement("INSERT INTO Kysymys"
                + " (kurssi, aihe, kysymysteksti)"
                + " VALUES (?, ?, ?)");

        stmt.setString(1, kysymys.getKurssi());

        stmt.setString(2, kysymys.getAihe());

        stmt.setString(3, kysymys.getKysymysteksti());

        stmt.executeUpdate();

        stmt.close();

        stmt = conn.prepareStatement("SELECT * FROM Kysymys"
                + " WHERE kurssi = ? AND aihe = ? AND kysymysteksti = ?");

        stmt.setString(1, kysymys.getKurssi());
        stmt.setString(2, kysymys.getAihe());
        stmt.setString(3, kysymys.getKysymysteksti());

        ResultSet rs = stmt.executeQuery();

        rs.next();

        Kysymys k = new Kysymys(rs.getInt("id"), rs.getString("kurssi"),
                rs.getString("aihe"), rs.getString("kysymysteksti"));

        stmt.close();

        rs.close();

        conn.close();

        return k;

    }

    private Kysymys update(Kysymys kysymys) throws SQLException {

        Connection conn = database.getConnection();

        PreparedStatement stmt = conn.prepareStatement("UPDATE Kysymys SET"
                + " kurssi = ?, aihe = ?, kysymysteksti = ? WHERE id = ?");

        stmt.setString(1, kysymys.getKurssi());

        stmt.setString(2, kysymys.getAihe());

        stmt.setString(3, kysymys.getKysymysteksti());

        stmt.setInt(4, kysymys.getId());

        stmt.executeUpdate();

        stmt.close();

        conn.close();

        return kysymys;

    }
}
