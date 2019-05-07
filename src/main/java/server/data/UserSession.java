package server.data;


import server.model.User;

/**
 * This class represents a User's active session
 */
public class UserSession {

    /**
     * User whose session it i s
     */
    private static User currentSession;

    //region Getters and Setters
    public static User getCurrentSession() {
        return currentSession;
    }

    public static void setCurrentSession(final User currentSession) {
        UserSession.currentSession = currentSession;
    }
    //endregion
}
