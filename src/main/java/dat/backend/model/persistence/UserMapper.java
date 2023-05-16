package dat.backend.model.persistence;
import dat.backend.model.entities.User;
import dat.backend.model.exceptions.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

class UserMapper
{
    static User login(String email, String password, ConnectionPool connectionPool) throws DatabaseException
    {
        Logger.getLogger("web").log(Level.INFO, "");

        User user;

        String sql = "SELECT * FROM user WHERE `e-mail` = ? AND password = ?";

        try (Connection connection = connectionPool.getConnection())
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

                    user = new User(id, email, password, role, address, city, phone);
                    return user;
                }
            }
        }
        catch (SQLException ex)
        {
            throw new DatabaseException(ex, "Error logging in. Something went wrong with the database");
        }
        return null;
    }

    public static User createUser(String email, String password, String address, String city, int phoneNumber, String role, ConnectionPool connectionPool) throws DatabaseException, IllegalArgumentException
    {
        Logger.getLogger("web").log(Level.INFO, "trying to create new user...");

        if(password.length() <= 4)
        {
            throw new IllegalArgumentException("Kodeord skal være mindst 5 tegn");
        }

        if(email.contains(" "))
        {
            throw new IllegalArgumentException("Email adressen må ikke indeholde mellemrum");
        }

        User user = getUserByEmail(email, connectionPool);

        if (user != null)
        {
            throw new IllegalArgumentException("Der eksisterer allerede en bruger med den indtastede email adresse");
        }

        String sql = "insert into user (`e-mail`, password, role, address, city, phone) values (?, ?, ?, ?, ?, ?)";

        try(Connection connection = connectionPool.getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(sql))
            {
                ps.setString(1, email);
                ps.setString(2, password);
                ps.setString(3, role);
                ps.setString(4, address);
                ps.setString(5, city);
                ps.setInt(6, phoneNumber);

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
                            user = new User(idUser, email, password, role, address, city, phoneNumber);
                        }
                        else
                        {
                            throw new DatabaseException("No ID was found!");
                        }
                    }
                }
                else
                {
                    throw new DatabaseException("This user " + email + ", could not be insert in to the database");
                }
            }
        }
        catch (SQLException e)
        {
            throw new DatabaseException(e.getMessage());
        }
        return user;
    }

    public static User getUserByEmail(String email, ConnectionPool connectionPool) throws DatabaseException {
        Logger.getLogger("web").log(Level.INFO, "checking if the user exists in the database");
        String sql = "SELECT * FROM user where `e-mail` = ?";

        try(Connection connection = connectionPool.getConnection())
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

                return new User(idUser, email, password, role, address, city, phone);
            }
        }
        catch (SQLException e)
        {
            throw new DatabaseException(e.getMessage());
        }
        return null;
    }

    public static List<User> getAllUsers(ConnectionPool connectionPool) throws DatabaseException
    {
        Logger.getLogger("web").log(Level.INFO, "");
        String sql = "SELECT * FROM user";

        List<User> userList = new ArrayList<>();

        try(Connection connection = connectionPool.getConnection())
        {
            try(PreparedStatement pr = connection.prepareStatement(sql))
            {
                ResultSet rs = pr.executeQuery();
                while(rs.next())
                {
                    int idUser = rs.getInt("idUser");
                    String email = rs.getString("e-mail");
                    String password = rs.getString("password");
                    String role = rs.getString("role");
                    String address = rs.getString("address");
                    String city = rs.getString("city");
                    int phone = rs.getInt("phone");

                    userList.add(new User(idUser, email, password, role, address, city, phone));
                }
            }
            return userList;
        }
        catch (SQLException e)
        {
            throw new DatabaseException(e.getMessage());
        }
    }
}
