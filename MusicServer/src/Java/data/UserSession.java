package data;


import model.User;

public class UserSession {
    public static User currentSession;

    public static User getCurrentSession() {
        return currentSession;
    }

    public static void setCurrentSession(final User currentSession) {
        UserSession.currentSession = currentSession;
    }
}
