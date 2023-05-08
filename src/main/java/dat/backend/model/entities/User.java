package dat.backend.model.entities;

import java.util.Objects;

public class User
{
    private int idUser;
    private String email;
    private String password;
    private String role;
    private String address;
    private String city;
    private int phone;

    public User(int idUser, String email, String password, String role,String address,String city,int phone)
    {
        this.idUser = idUser;
        this.email = email;
        this.password = password;
        this.role = role;
        this.address = address;
        this.city = city;
        this.phone = phone;
    }

    public int getIdUser() { return  idUser; }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getRole()
    {
        return role;
    }

    public void setRole(String role)
    {
        this.role = role;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public int getPhone()
    {
        return phone;
    }

    public void setPhone(int phone)
    {
        this.phone = phone;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return phone == user.phone && Objects.equals(email, user.email) && Objects.equals(password, user.password) && Objects.equals(role, user.role) && Objects.equals(address, user.address) && Objects.equals(city, user.city);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(email, password, role, address, city, phone);
    }

    @Override
    public String toString()
    {
        return "User{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", phone=" + phone +
                '}';
    }
}
