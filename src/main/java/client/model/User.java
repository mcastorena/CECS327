package client.model;

import com.google.gson.annotations.SerializedName;

/**
 * This class represents the User
 */
public class User {

    @SerializedName("username")
    private String username;

    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    transient public Profile userProfile; // user's profile containing their playlists

    /**
     * Empty constructor
     */
    public User() {
    }

    /**
     * Constructs a User given a username, email, and password
     *
     * @param username - User's username
     * @param email    - User's email
     * @param password - User's password
     */
    public User(final String username, final String email, final String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    /**
     * Constructs a User given a username, email, password, and Profile
     *
     * @param username - User's username
     * @param email    - User's email
     * @param password - User's password
     * @param profile  - User's Profile
     */
    public User(String username, String email, String password, Profile profile) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.userProfile = profile;
    }

    //region Getters and setters
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
