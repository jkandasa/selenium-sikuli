package com.redhat.qe.sikuli.client;

import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.redhat.qe.sikuli.common.RemoteFile;
import com.redhat.qe.sikuli.common.RemoteKeyboard;
import com.redhat.qe.sikuli.common.RemoteMouse;
import com.redhat.qe.sikuli.common.RemoteScreen;

import io.sterodium.rmi.protocol.client.RemoteNavigator;

/**
 * @author Jeeva Kandasamy (jkandasa)
 */

public class SikuliClient {
    private static final Logger _logger = LoggerFactory.getLogger(SikuliClient.class.getName());
    private static final String SIKULI_HUB_PATH = "/grid/admin/SikuliGridServlet/session/{0}/SikuliNodeServlet";
    private static final String SIKULI_NODE_PATH = "/extra/SikuliNodeServlet";
    private static RemoteNavigator remoteNavigator;
    private static RemoteScreen remoteScreen;
    private static RemoteKeyboard remoteKeyboard;
    private static RemoteMouse remoteMouse;

    RemoteFile remoteFile;

    public enum CONNECTION_TYPE {
        GRID,
        NODE;
    }

    public SikuliClient(String host, int port, String sessionId) {
        this(host, port, sessionId, CONNECTION_TYPE.GRID);
    }

    public SikuliClient(String host, int port, String sessionId, CONNECTION_TYPE connectionType) {
        switch (connectionType) {
            case NODE:
                remoteNavigator = new RemoteNavigator(host, port, SIKULI_NODE_PATH);
                break;
            case GRID:
            default:
                remoteNavigator = new RemoteNavigator(host, port, MessageFormat.format(SIKULI_HUB_PATH, sessionId));
                break;
        }
        remoteScreen = remoteNavigator.createProxy(RemoteScreen.class, "screen");
        remoteKeyboard = remoteNavigator.createProxy(RemoteKeyboard.class, "keyboard");
        remoteMouse = remoteNavigator.createProxy(RemoteMouse.class, "mouse");
        remoteFile = remoteNavigator.createProxy(RemoteFile.class, "file");
        _logger.debug("Sikuli remote client initialized. Selenium host:{}:{}, sessionId:{}, ConnectionType:{}",
                host, port, sessionId, connectionType);
    }

    public RemoteScreen screen() {
        return remoteScreen;
    }

    public RemoteFile file() {
        return remoteFile;
    }

    public RemoteKeyboard keyboard() {
        return remoteKeyboard;
    }

    public RemoteMouse mouse() {
        return remoteMouse;
    }
}
