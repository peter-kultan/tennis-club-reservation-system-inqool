package cz.repositories.impl;

import cz.exceptions.DataAccessException;
import cz.models.Surface;
import cz.repositories.SurfaceRepository;
import org.springframework.beans.factory.annotation.Value;
    import org.springframework.stereotype.Repository;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Repository
public class SurfaceRepositoryImpl implements SurfaceRepository {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Override
    public Surface findSurfaceById(int id) {
        try (var conn = DriverManager.getConnection(url, username, password);
        var st = conn.prepareStatement("SELECT id, name FROM surface WHERE id = ?")) {
            st.setInt(1, id);
            try (var rs = st.executeQuery()) {
                if (rs.next()) {
                    var surface = new Surface(rs.getString("name"));
                    surface.setId(rs.getInt("id"));
                    if (rs.next()) {
                        throw new DataAccessException("Multiple surfaces with id: " + id + " found");
                    }
                    return surface;
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to load surface with id: " + id);
        }
        return null;
    }

    @Override
    public Collection<Surface> findAllSurfaces() {
        try (var conn = DriverManager.getConnection(url, username, password);
        var st = conn.prepareStatement("SELECT id, name FROM surface")) {
            List<Surface> surfaces = new ArrayList<>();
            try (var rs = st.executeQuery()) {
                while (rs.next()) {
                    var surface = new Surface(rs.getString("name"));
                    surface.setId(rs.getInt("id"));
                    surfaces.add(surface);
                }
                return surfaces;
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to load all surfaces", ex);
        }
    }

    @Override
    public void createSurface(Surface surface) {
        if (surface.getId() != null) {
            throw new IllegalArgumentException("Surface already has ID: " + surface);
        }
        try (var conn = DriverManager.getConnection(url, username, password);
        var st = conn.prepareStatement("INSERT INTO surface(name) VALUES (?)",
                Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, surface.getName());
            st.executeUpdate();
            try (var rs = st.getGeneratedKeys()) {
                if (rs.getMetaData().getColumnCount() != 1) {
                    throw new DataAccessException("Failed to fetch generated key: compound ke returned for surface: " + surface);
                }
                if (rs.next()) {
                    surface.setId(rs.getInt(1));
                } else {
                    throw new DataAccessException("Failed to fetch generated key: no key returned for surface: " + surface);
                }
                if (rs.next()) {
                    throw new DataAccessException("Failed to fetch generated key: multiple keys returned for surface: " + surface);
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to store surface: " + surface);
        }
    }

    @Override
    public void updateSurface(Surface surface) {
        if (surface.getId() == null)
        {
            throw new IllegalArgumentException("surface has null ID: " + surface);
        }
        try (var conn = DriverManager.getConnection(url, username, password);
        var st = conn.prepareStatement("UPDATE surface set name = ? where id = ?")) {
            st.setString(1, surface.getName());
            st.setInt(2, surface.getId());
            int rowUpdated = st.executeUpdate();
            if (rowUpdated == 0) {
                throw new DataAccessException("Failed to update non-existing surface: " + surface);
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to update surface", ex);
        }
    }

    @Override
    public void deleteSurface(int id) {
        var surface = findSurfaceById(id);
        if (surface == null) {
            throw new IllegalStateException("surface with id: " + id + " does not exist");
        }
        try (var conn = DriverManager.getConnection(url, username, password);
        var st = conn.prepareStatement("DELETE FROM surface WHERE id = ?")) {
            st.setInt(1, id);
            int rowDeleted = st.executeUpdate();
            if (rowDeleted == 0) {
                throw new DataAccessException("Failed to delete non-existing surface: " + surface);
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to delete surface: " + surface, ex);
        }
    }
}
