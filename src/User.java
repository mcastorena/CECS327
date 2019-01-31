public class User {
    private String username,
                    email,
                    password;

    public Profile userProfile; // user's profile containing their playlists

    public User() {}

    public User(String username, String email, String password, Profile profile) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.userProfile = profile;
    }

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
}
