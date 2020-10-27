package com.zakharovakr.blogCMSMastery.dtos;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Objects;

public class User {
    private int userId;

    @NotBlank(message = "Please enter username")
    @Size(max = 30, message= "Invalid username: Please enter user between 1-70 characters")
    private String username;

    @NotBlank(message = "Please enter password")
    @Size(max = 100, message= "Invalid password: Please enter password between 1-30 characters")
    private String password;

    @NotBlank(message = "Please enter first name")
    @Size(max = 30, message= "Invalid first name: Please enter first name between 1-30 characters")
    private String firstName;

    @NotBlank(message = "Please enter last name")
    @Size(max = 30, message= "Invalid last name: Please enter last name between 1-30 characters")
    private String lastName;

    @NotBlank(message = "Please enter Email")
    @Size(max = 30, message= "Invalid Email: Please enter Email between 1-30 characters")
    private String email;

    private boolean enable;
    private List<Role> roles;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return getUserId() == user.getUserId() &&
                isEnable() == user.isEnable() &&
                Objects.equals(getUsername(), user.getUsername()) &&
                Objects.equals(getPassword(), user.getPassword()) &&
                Objects.equals(getFirstName(), user.getFirstName()) &&
                Objects.equals(getLastName(), user.getLastName()) &&
                Objects.equals(getEmail(), user.getEmail()) &&
                Objects.equals(getRoles(), user.getRoles());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getUsername(), getPassword(), getFirstName(), getLastName(), getEmail(), isEnable(), getRoles());
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", enable=" + enable +
                ", roles=" + roles +
                '}';
    }
}
