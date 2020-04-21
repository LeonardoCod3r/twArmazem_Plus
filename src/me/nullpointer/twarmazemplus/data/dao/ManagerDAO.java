package me.nullpointer.twarmazemplus.data.dao;

import me.nullpointer.twarmazemplus.cache.DropCACHE;
import me.nullpointer.twarmazemplus.cache.PlayerCACHE;
import me.nullpointer.twarmazemplus.data.ConnectionFactory;
import me.nullpointer.twarmazemplus.utils.armazem.Armazem;
import me.nullpointer.twarmazemplus.utils.armazem.DropPlayer;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ManagerDAO implements DAO<String, Armazem> {

    @Override
    public void createTable() {
        try {
            final Connection connection = ConnectionFactory.make();
            assert connection != null : "Conexão não foi estabelecida corretamente.";
            final PreparedStatement st = connection.prepareStatement("CREATE TABLE IF NOT EXISTS Armazem(Owner VARCHAR(16) Primary Key, Limit DOUBLE, Multiplier DOUBLE, Friends VARCHAR(8000), Drops VARCHAR(8000));");
            st.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void load(String owner) {
        try {
            final Connection connection = ConnectionFactory.make();
            assert connection != null : "Conexão não foi estabelecida corretamente.";
            final PreparedStatement st = connection.prepareStatement("SELECT * FROM Armazem where Owner = ?");
            st.setString(1, owner);
            final ResultSet result = st.executeQuery();
            result.next();
            final List<DropPlayer> dropPlayer = new ArrayList<>();
            Arrays.asList(result.getString("Drops").split(",")).forEach(s -> dropPlayer.add(new DropPlayer(s.split(":")[0], Double.parseDouble(s.split(":")[1]))));
            final Armazem armazem = new Armazem(owner, result.getDouble("Limit"), result.getDouble("Multiplier"), new ArrayList<>(), dropPlayer, Arrays.asList(result.getString("Friends").replaceAll(" ", "").replace("]", "").replace("[", "").split(",")));
            PlayerCACHE.put(armazem);
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(Armazem armazem) {
        try {
            final Connection connection = ConnectionFactory.make();
            assert connection != null : "Conexão não foi estabelecida corretamente.";
            final PreparedStatement st = connection.prepareStatement("INSERT INTO Armazem(Owner, Limit, Multiplier, Friends, Drops) VALUES(?,?,?,?,?) ON DUPLICATE KEY UPDATE Armazem SET Owner = ? ,Limit = ? ,Multiplier = ?, Friends = ?, Drops = ?");
            final StringBuilder stringBuilder = new StringBuilder();
            armazem.getDropPlayers().forEach(dropPlayer -> stringBuilder.append(stringBuilder.toString().equals("") ? "" : ",").append(dropPlayer.getKeyDrop()).append(":").append(dropPlayer.getDropAmount()));
            st.setString(1, armazem.getOwner());
            st.setDouble(2, armazem.getLimit());
            st.setDouble(3, armazem.getMultiplier());
            st.setString(4, armazem.getFriends().toString());
            st.setString(5, stringBuilder.toString());
            st.setString(6, armazem.getOwner());
            st.setDouble(7, armazem.getLimit());
            st.setDouble(8, armazem.getMultiplier());
            st.setString(9, armazem.getFriends().toString());
            st.setString(10, stringBuilder.toString());
            st.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveAll() {
        try {
            final Connection connection = ConnectionFactory.make();
            assert connection != null : "Conexão não foi estabelecida corretamente.";
            for (Armazem armazem : PlayerCACHE.armazens) {
                final PreparedStatement st = connection.prepareStatement("INSERT INTO Armazem(Owner, Limit, Multiplier, Friends, Drops) VALUES(?,?,?,?,?) ON DUPLICATE KEY UPDATE Armazem SET Owner = ? ,Limit = ? ,Multiplier = ?, Friends = ?, Drops = ?");
                final StringBuilder stringBuilder = new StringBuilder();
                armazem.getDropPlayers().forEach(dropPlayer -> stringBuilder.append(stringBuilder.toString().equals("") ? "" : ",").append(dropPlayer.getKeyDrop()).append(":").append(dropPlayer.getDropAmount()));
                st.setString(1, armazem.getOwner());
                st.setDouble(2, armazem.getLimit());
                st.setDouble(3, armazem.getMultiplier());
                st.setString(4, armazem.getFriends().toString());
                st.setString(5, stringBuilder.toString());
                st.setString(6, armazem.getOwner());
                st.setDouble(7, armazem.getLimit());
                st.setDouble(8, armazem.getMultiplier());
                st.setString(9, armazem.getFriends().toString());
                st.setString(10, stringBuilder.toString());
                st.executeUpdate();
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadAll() {
        try {
            final Connection connection = ConnectionFactory.make();
            assert connection != null : "Conexão não foi estabelecida corretamente.";
            final PreparedStatement st = connection.prepareStatement("SELECT * FROM Armazem");
            final ResultSet result = st.executeQuery();
            while (result.next()) {
                if (Bukkit.getPlayer(result.getString("Owner")) != null) {
                    final List<DropPlayer> dropPlayer = new ArrayList<>();
                    Arrays.asList(result.getString("Drops").split(",")).forEach(s -> {
                        if (DropCACHE.drops.stream().anyMatch(drop -> drop.getKeyDrop().equalsIgnoreCase(s.split(":")[0]))){
                            dropPlayer.add(new DropPlayer(s.split(":")[0], Double.parseDouble(s.split(":")[1])));
                        }
                    });
                    final Armazem armazem = new Armazem(result.getString("Owner"), result.getDouble("Limit"), result.getDouble("Multiplier"), new ArrayList<>(), dropPlayer, Arrays.asList(result.getString("Friends").replaceAll(" ", "").replace("]", "").replace("[", "").split(",")));
                    PlayerCACHE.put(armazem);
                }
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String owner) {
        try {
            final Connection connection = ConnectionFactory.make();
            assert connection != null : "Conexão não foi estabelecida corretamente.";
            final PreparedStatement st = connection.prepareStatement("delete Armazem where Owner = ?");
            st.setString(1, owner);
            st.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean exists(String owner) {
        try {
            final Connection connection = ConnectionFactory.make();
            assert connection != null : "Conexão não foi estabelecida corretamente.";
            final PreparedStatement st = connection.prepareStatement("SELECT * FROM Armazem where Owner = ?");
            st.setString(1, owner);
            final ResultSet result = st.executeQuery();
            connection.close();
            return result.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}