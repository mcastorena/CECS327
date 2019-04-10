package server.model;

import com.google.gson.annotations.SerializedName;

/**
 * This class represents a User for our Music App
 */
public class User {
    /**
     * User's username
     */
    @SerializedName("username")
    private String username;

    /**
     * User's email address
     */
    @SerializedName("email")
    private String email;

    /**
     * User's password
     */
    @SerializedName("password")
    private String password;

    /**
     * User's profile that contains their Playlists
     */
    transient public Profile userProfile;

    /**
     * Empty default constructor
     */
    public User() {
    }

    /**
     * Constructor #2
     *
     * @param username Username
     * @param email    Email address
     * @param password Password
     */
    public User(final String username, final String email, final String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    /**
     * Constructor #3
     *
     * @param username Username
     * @param email    Email address
     * @param password Password
     * @param profile  Profile
     */
    public User(String username, String email, String password, Profile profile) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.userProfile = profile;
    }

    //region Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public Profile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(final Profile userProfile) {
        this.userProfile = userProfile;
    }
    //endregion
}
