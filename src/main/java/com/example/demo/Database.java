package com.example.demo;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.sql.*;

public class Database {
    private static final String DB_DIR = ".MyQRCodeApp";
    private static final String DB_NAME = "appdb";
    private static Database instance;
    private Connection conn;

    private Database() {
        openConnection();
        initTables();
    }

    public static synchronized Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            if (conn == null || conn.isClosed()) {
                openConnection(); // opnieuw openen als gesloten
            }
        } catch (SQLException e) {
            throw new RuntimeException("Fout bij controleren/heropenen van databaseconnectie", e);
        }
        return conn;
    }


    private void openConnection() {
        try {
            Path dbDir = Paths.get(System.getProperty("user.home"), DB_DIR);
            Files.createDirectories(dbDir);
            String url = "jdbc:h2:file:" + dbDir.resolve(DB_NAME);
            conn = DriverManager.getConnection(url, "sa", "");
        } catch (Exception e) {
            throw new RuntimeException("Kon databaseverbinding niet openen", e);
        }
    }

    private void initTables() {
        try (Statement stmt = getConnection().createStatement()) {
            stmt.execute("""
              CREATE TABLE IF NOT EXISTS users (
                userid IDENTITY PRIMARY KEY,
                username VARCHAR(255) UNIQUE NOT NULL,
                password_hash VARCHAR(255) NOT NULL
              )
            """);
            stmt.execute("""
              CREATE TABLE IF NOT EXISTS administrators (
                adminid IDENTITY PRIMARY KEY,
                username VARCHAR(255) UNIQUE NOT NULL,
                password_hash VARCHAR(255) NOT NULL
              )
            """);
        } catch (SQLException e) {
            throw new RuntimeException("Kon tabellen niet initialiseren", e);
        }
    }

    public boolean registerUser(String username, String plainPassword) {
        return insert("users", username, plainPassword);
    }

    public boolean registerAdmin(String username, String plainPassword) {
        return insert("administrators", username, plainPassword);
    }

    public boolean validateUser(String username, String plainPassword) {
        return validate("users", username, plainPassword);
    }

    public boolean validateAdmin(String username, String plainPassword) {
        return validate("administrators", username, plainPassword);
    }

    private boolean insert(String table, String username, String pwd) {
        String sql = "INSERT INTO " + table + "(username,password_hash) VALUES(?,?)";
        try (PreparedStatement st = getConnection().prepareStatement(sql)) {
            st.setString(1, username);
            st.setString(2, hash(pwd));
            st.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("SQL fout bij insert:");
            e.printStackTrace();
            return false;
        }
    }

    private boolean validate(String table, String username, String plainPassword) {
        String sql = "SELECT password_hash FROM " + table + " WHERE username=?";
        try (PreparedStatement st = getConnection().prepareStatement(sql)) {
            st.setString(1, username);
            try (ResultSet rs = st.executeQuery()) {
                if (!rs.next()) return false;
                return rs.getString(1).equals(hash(plainPassword));
            }
        } catch (SQLException e) {
            return false;
        }
    }

    private String hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Hash-fout", e);
        }
    }
}
