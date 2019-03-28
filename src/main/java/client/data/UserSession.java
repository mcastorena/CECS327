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

    public static User getCurrentSession() {
        return currentSession;
    }

    public static void setCurrentSession(final User currentSession) {
        UserSession.currentSession = currentSession;
    }
}
