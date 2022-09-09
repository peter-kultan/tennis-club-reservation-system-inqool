package cz.repositories.impl;

import cz.exceptions.DataAccessException;
import cz.models.User;
import cz.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {


    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Override
    public User findUserById(int id) {
        try (var conn = DriverManager.getConnection(url, username, password);
                         var st = conn.prepareStatement("SELECT id, name FROM users WHERE id = ?")) {
            st.setInt(1, id);
            try (var rs = st.executeQuery()) {
                if (rs.next()) {
                    var user = new User(rs.getString("phone_number"),
                            rs.getString("name"));
                    user.setId(rs.getInt("id"));
                    if (rs.next()) {
                        throw new DataAccessException("Multiple users with id: " + id + " found");
                    }
                    return user;
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to load user with id: " + id, ex);
        }
        return null;
    }

    @Override
    public User findUserByPhoneNumber(String phoneNumber) {
        try (var conn = DriverManager.getConnection(url, username, password);
             var st = conn.prepareStatement("SELECT id, name FROM users WHERE phone_number = ?")) {
            st.setString(1, phoneNumber);
            try (var rs = st.executeQuery()) {
                if (rs.next()) {
                    var user = new User(phoneNumber,
                            rs.getString("name"));
                    user.setId(rs.getInt("id"));
                    if (rs.next()) {
                        throw new DataAccessException("Multiple users with phone number: " + phoneNumber + " found");
                    }
                    return user;
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to load user with phone number: " + phoneNumber, ex);
        }
        return null;
    }

    @Override
    public Collection<User> findAllUsers() {
        try (var conn = DriverManager.getConnection(url, username, password);
        var st = conn.prepareStatement("SELECT id, phone_number, name FROM users")) {
            List<User> users = new ArrayList<>();
            try (var rs = st.executeQuery()) {
                while (rs.next()) {
                    var user = new User(rs.getString("phone_number"),
                            rs.getString("name"));
                    user.setId(rs.getInt("id"));
                }
                return users;
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to load all users", ex);
        }
    }

    @Override
    public void createUser(User user) {
        if (user.getId() != null) {
            throw new IllegalStateException("User already has ID: " + user);
        }
        try (var conn = DriverManager.getConnection(url, username, password);
        var st = conn.prepareStatement("INSERT INTO users(phone_number, name) VALUES (?, ?)",
                Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, user.getPhoneNumber());
            st.setString(2, user.getName());
            st.executeUpdate();
            try (var rs = st.getGeneratedKeys()) {
                if (rs.getMetaData().getColumnCount() != 1) {
                    throw new DataAccessException("Failed to fetch generated key: compound key returned for user: " + user);
                }
                if (rs.next()) {
                    user.setId(rs.getInt(1));
                } else {
                    throw new DataAccessException("Failed to fetch generated key: no key returned for user: " + user);
                }
                if (rs.next()) {
                    throw new DataAccessException("Failed to fetch generated key: multiple keys returned for user: " + user);
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to store user: " + user);
        }
    }

    @Override
    public void updateUser(User user) {
        if (user.getId() == null) {
            throw new IllegalStateException("User has null ID: " + user);
        }
        try (var conn = DriverManager.getConnection(url, username, password);
             var st = conn.prepareStatement("UPDATE users SET phone_number = ?, name = ? WHERE id = ?")) {
            st.setString(1, user.getPhoneNumber());
            st.setString(2, user.getName());
            st.setInt(3, user.getId());
            int rowUpdated = st.executeUpdate();
            if (rowUpdated == 0) {
                throw new DataAccessException("Failed to update non-existing user: " + user);
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to update user", ex);
        }
    }

    @Override
    public void deleteUser(int id) {
        var user = findUserById(id);
        if (user == null) {
            throw new IllegalStateException("user with id: " + id + " does not exist");
        }
        try (var conn = DriverManager.getConnection(url, username, password);
             var st = conn.prepareStatement("DELETE FROM users WHERE id = ?")) {
            st.setInt(1, id);
            int rowDeleted = st.executeUpdate();
            if (rowDeleted == 0) {
                throw new DataAccessException("Failed to delete non-existing user: " + user);
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to delete user: " + user, ex);
        }
    }
}
