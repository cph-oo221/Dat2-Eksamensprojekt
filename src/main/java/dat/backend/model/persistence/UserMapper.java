package dat.backend.model.persistence;

import dat.backend.model.config.ApplicationStart;
import dat.backend.model.entities.User;
import dat.backend.model.exceptions.DatabaseException;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

class UserMapper
{
    static User login(String email, String password) throws DatabaseException
    {
        Logger.getLogger("web").log(Level.INFO, "");

        User user = null;

        String sql = "SELECT * FROM user WHERE email = ? AND password = ?";

        try (Connection connection = ApplicationStart.getConnectionPool().getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(sql))
            {
                ps.setString(1, email);
                ps.setString(2, password);
                ResultSet rs = ps.executeQuery();
                if (rs.next())
                {
                    String role = rs.getString("role");
                    int id = rs.getInt("idUser");
                    String address = rs.getString("address");
                    String city = rs.getString("city");
                    int phone = rs.getInt("phone");
                    int zip = rs.getInt("zip");

                    user = new User(id, email, password, role, address, city, phone, zip);
                } else
                {
                    throw new DatabaseException("Wrong username or password");
                }
            }
        } catch (SQLException ex)
        {
            throw new DatabaseException(ex, "Error logging in. Something went wrong with the database");
        }
        return user;
    }

    public static User createUser(String email, String password, String address, String city, int zipCode, int phoneNumber, String role) throws DatabaseException
    {
        Logger.getLogger("web").log(Level.INFO, "");
        User user = getUserByEmail(email);

        if (user != null)
        {
            throw new DatabaseException("Can't make a new user that already exists");
        }

        String sql = "insert into user (`e-mail`, password, role, address, city, phone, `zip-code`) values (?, ?, ?, ?, ?, ?, ?)";

        try(Connection connection = ApplicationStart.getConnectionPool().getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(sql))
            {
                ps.setString(1, email);
                ps.setString(2, password);
                ps.setString(3, role);
                ps.setString(4, address);
                ps.setString(5, city);
                ps.setInt(6, zipCode);
                ps.setInt(7, phoneNumber);

                int rowsAffected = ps.executeUpdate();

                if(rowsAffected == 1)
                {
                    ps.close();
                    sql = "SELECT LAST_INSERT_ID()";
                    try(PreparedStatement ps2 = connection.prepareStatement(sql))
                    {
                        ResultSet rs = ps2.executeQuery();
                        if(rs.next())
                        {
                            int idUser = rs.getInt("LAST_INSERT_ID()");

                            user = new User(idUser, email, password, role, address, city, phoneNumber, zipCode);
                        }
                        else
                        {
                            throw new DatabaseException("No ID was found");
                        }
                    }
                }
                else
                {
                    throw new DatabaseException("This user information could not be insert in to the database");
                }
            }
        }
        catch (SQLException e)
        {
            throw new DatabaseException(e.getMessage());
        }
        return user;
    }

    
    private static User getUserByEmail(String email) throws DatabaseException {
        Logger.getLogger("web").log(Level.INFO, "checking if the user exists in the database");
        String sql = "SELECT * FROM user where `e-mail` = ?";

        try(Connection connection = ApplicationStart.getConnectionPool().getConnection())
        {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();
            if (rs.next())
            {
                int idUser = rs.getInt("iduser");
                String password = rs.getString("password");
                String role = rs.getString("role");
                String address = rs.getString("address");
                String city = rs.getString("city");
                int phone = rs.getInt("phone");
                int zip = rs.getInt("zip-code");

                return new User(idUser, email, password, role, address, city, phone, zip);
            }
        }
        catch (SQLException e)
        {
            throw new DatabaseException(e.getMessage());
        }
        return null;
    }

    /*static User createUser(String username, String password, String role, ConnectionPool connectionPool) throws DatabaseException
    {
        Logger.getLogger("web").log(Level.INFO, "");
        User user;
        String sql = "insert into user (username, password, role) values (?,?,?)";
        try (Connection connection = connectionPool.getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(sql))
            {
                ps.setString(1, username);
                ps.setString(2, password);
                ps.setString(3, role);
                int rowsAffected = ps.executeUpdate();
                if (rowsAffected == 1)
                {
                    user = new User(username, password, role);
                } else
                {
                    throw new DatabaseException("The user with username = " + username + " could not be inserted into the database");
                }
            }
        }
        catch (SQLException ex)
        {
            throw new DatabaseException(ex, "Could not insert username into database");
        }
        return user;
    }

     */


}
