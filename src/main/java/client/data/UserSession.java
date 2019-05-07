package client.data;


import client.model.User;

/**
 * This class represents an active session for a logged-in User
 */
public class UserSession {
    /**
     * User that is currently logged in
     */
    private static User currentSession;

    /**
     * Get the User who's currently active
     *
     * @return User reference for who is active
     */
    public static User getCurrentSession() {
        return currentSession;
    }

    /**
     * Set the session User to the supplied USer
     *
     * @param currentSession - User being set for a current session
     */
    public static void setCurrentSession(final User currentSession) {
        UserSession.currentSession = currentSession;
    }
}
