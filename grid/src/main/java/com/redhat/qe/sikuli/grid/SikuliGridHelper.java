package com.redhat.qe.sikuli.grid;

import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.grid.internal.GridRegistry;
import org.openqa.grid.internal.TestSession;

/**
 * @author Jeeva Kandasamy (jkandasa)
 */

public class SikuliGridHelper {
    private static final Pattern SESSION_ID_PATTERN = Pattern.compile("/session/([^/]+).*");

    private SikuliGridHelper() {

    }

    public static String getSessionIdFromPath(String pathInfo) {
        Matcher matcher = SESSION_ID_PATTERN.matcher(pathInfo);
        if (matcher.matches()) {
            return matcher.group(1);
        }
        throw new IllegalArgumentException("Invalid request. Session Id is not present");
    }

    public static String trimSessionPath(String pathInfo) {
        return pathInfo.replaceFirst("/session/" + getSessionIdFromPath(pathInfo), "");
    }

    public static URL getRemoteHostForSession(GridRegistry registry, String sessionId) {
        for (TestSession activeSession : registry.getActiveSessions()) {
            if (sessionId.equals(activeSession.getExternalKey().getKey())) {
                return activeSession.getSlot().getProxy().getRemoteHost();
            }
        }
        throw new IllegalArgumentException("Invalid sessionId. No active session is present for id:" + sessionId);
    }

    public static void refreshTimeout(GridRegistry registry, String sessionId) {
        for (TestSession activeSession : registry.getActiveSessions()) {
            if ((activeSession != null) && (sessionId != null) && (activeSession.getExternalKey() != null)) {
                if (sessionId.equals(activeSession.getExternalKey().getKey())) {
                    refreshTimeout(activeSession);
                }
            }
        }
    }

    private static void refreshTimeout(TestSession activeSession) {
        if (activeSession.getInactivityTime() != 0) {
            activeSession.setIgnoreTimeout(false);
        }
    }
}
