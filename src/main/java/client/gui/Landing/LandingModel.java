package client.gui.Landing;

/**
 * As part of the MVP-design pattern, this class represents the Model for the Landing
 * i.e., the screen that is shown to the user when they are logging in.
 */
public class LandingModel {
    /**
     * Username that the user has entered
     */
    private String usernameInput;
    /**
     * Password that the user has entered
     */
    private String passwordInput;

    //region Getters and Setters
    public String getUsernameInput() {
        return usernameInput;
    }

    public void setUsernameInput(final String usernameInput) {
        this.usernameInput = usernameInput;
    }

    public String getPasswordInput() {
        return passwordInput;
    }

    public void setPasswordInput(final String passwordInput) {
        this.passwordInput = passwordInput;
    }
    //endregion
}
