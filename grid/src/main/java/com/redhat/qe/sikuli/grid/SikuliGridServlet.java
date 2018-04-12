package com.redhat.qe.sikuli.grid;

import java.io.IOException;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openqa.grid.internal.GridRegistry;
import org.openqa.grid.web.servlet.RegistryBasedServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jeeva Kandasamy (jkandasa)
 */

public class SikuliGridServlet extends RegistryBasedServlet {

    /**  */
    private static final long serialVersionUID = 5243963014485014138L;
    private static final Logger _logger = LoggerFactory.getLogger(SikuliGridServlet.class.getName());

    public SikuliGridServlet() {
        this(null);
    }

    public SikuliGridServlet(GridRegistry registry) {
        super(registry);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        process(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        process(request, response);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        process(request, response);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        process(request, response);
    }

    private void process(HttpServletRequest request, HttpServletResponse response) throws IOException {
        SikuliRequestProcessingClient sikuliRequestProcessingClient;
        try {
            sikuliRequestProcessingClient = createExtensionClient(request.getPathInfo());
        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
            return;
        }

        try {
            sikuliRequestProcessingClient.processRequest(request, response);
        } catch (IOException ex) {
            _logger.error("Exception during request forwarding", ex);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
        }
    }

    private SikuliRequestProcessingClient createExtensionClient(String path) {
        _logger.info("Forwarding request with path: " + path);
        String sessionId = SikuliGridHelper.getSessionIdFromPath(path);
        _logger.info("Retrieving remote host for session: " + sessionId);

        SikuliGridHelper.refreshTimeout(getRegistry(), sessionId);

        URL remoteHost = SikuliGridHelper.getRemoteHostForSession(getRegistry(), sessionId);
        String host = remoteHost.getHost();
        int port = remoteHost.getPort();
        _logger.info("Remote host retrieved: " + host + ":" + port);
        return new SikuliRequestProcessingClient(host, port);
    }
}
