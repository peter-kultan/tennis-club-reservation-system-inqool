package cz.repositories.impl;

import cz.exceptions.DataAccessException;
import cz.models.Court;
import cz.repositories.CourtRepository;
import cz.repositories.SurfaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Repository
public class CourtRepositoryImpl implements CourtRepository {

    @Autowired
    private SurfaceRepository surfaceRepository;

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Override
    public Court findCourtById(int id) {
        try (var conn = DriverManager.getConnection(url, username, password); var st = conn.prepareStatement("SELECT id, surface_id, hour_price from court where id = ?")) {
            st.setLong(1, id);
            try (var rs = st.executeQuery()) {
                if (rs.next()) {
                    var court = new Court(surfaceRepository.findSurfaceById(rs.getInt("surface_id")), rs.getDouble("hour_price"));
                    court.setId(id);
                    if (rs.next()) {
                        return null;
                    }
                    return court;
                }
            }
        } catch (SQLException ex) {
            return null;
        }
        return null;
    }

    @Override
    public Collection<Court> findAllCourts() {
        try (var conn = DriverManager.getConnection(url, username, password); var st = conn.prepareStatement("SELECT id, surface_id, hour_price FROM court")) {
            List<Court> courts = new ArrayList<>();
            try (var rs = st.executeQuery()) {
                while (rs.next()) {
                    var court = new Court(surfaceRepository.findSurfaceById(rs.getInt("surface_id")), rs.getDouble("hour_price"));
                    court.setId(rs.getInt("id"));
                    courts.add(court);
                }
                return courts;
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to load all courts", ex);
        }
    }

    @Override
    public void createCourt(Court court) {
        if (court.getId() != null) {
            throw new IllegalArgumentException("Court has already ID: " + court.getId());
        }
        try (var conn = DriverManager.getConnection(url, username, password); var st = conn.prepareStatement("INSERT INTO court(surface_id, hour_price) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            st.setInt(1, court.getSurface().getId());
            st.setDouble(2, court.getHourPrice());
            st.executeUpdate();
            try (var rs = st.getGeneratedKeys()) {
                if (rs.getMetaData().getColumnCount() != 1) {
                    throw new DataAccessException("Failed to fetch generated key: compound key returned for court: " + court);
                }
                if (rs.next()) {
                    court.setId(rs.getInt(1));
                } else {
                    throw new DataAccessException("Failed to fetch generated key: no key returned for court: " + court);
                }
                if (rs.next()) {
                    throw new DataAccessException("Failed to fetch generated key: multiple keys returned for court: " + court);
                }
            }

        } catch (SQLException ex) {
            throw new DataAccessException("Failed to store court: " + court, ex);
        }
    }

    @Override
    public void updateCourt(Court court) {
        if (court.getId() == null) {
            throw new IllegalArgumentException("court has null ID: " + court);
        }
        try (var conn = DriverManager.getConnection(url, username, password); var st = conn.prepareStatement("UPDATE court SET surface_id = ?, hour_price = ? WHERE id = ?")) {
            st.setInt(1, court.getSurface().getId());
            st.setDouble(2, court.getHourPrice());
            st.setInt(3, court.getId());
            int rowUpdated = st.executeUpdate();
            if (rowUpdated == 0) {
                throw new DataAccessException("Failed to update non-existing court: " + court);
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to update court", ex);
        }
    }

    @Override
    public void deleteCourt(int id) {
        var court = findCourtById(id);
        if (court == null) {
            throw new IllegalStateException("Court with id: " + id + " does not exist");
        }
        try (var conn = DriverManager.getConnection(url, username, password); var st = conn.prepareStatement("DELETE FROM court WHERE id = ?")) {
            st.setInt(1, id);
            int rowDeleted = st.executeUpdate();
            if (rowDeleted == 0) {
                throw new DataAccessException("Failed to delete non-existing court: " + court);
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to delete court: " + court, ex);
        }
    }
}
