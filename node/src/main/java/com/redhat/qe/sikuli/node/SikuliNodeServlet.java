package com.redhat.qe.sikuli.node;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openqa.grid.internal.GridRegistry;
import org.openqa.grid.web.servlet.RegistryBasedServlet;

import com.google.gson.Gson;
import com.redhat.qe.sikuli.common.RemoteFile;
import com.redhat.qe.sikuli.common.RemoteKeyboard;
import com.redhat.qe.sikuli.common.RemoteMouse;
import com.redhat.qe.sikuli.common.RemoteScreen;

import io.sterodium.rmi.protocol.MethodInvocationDto;
import io.sterodium.rmi.protocol.MethodInvocationResultDto;
import io.sterodium.rmi.protocol.server.RmiFacade;

/**
 * @author Jeeva Kandasamy (jkandasa)
 */

public class SikuliNodeServlet extends RegistryBasedServlet {
    /**  */
    private static final long serialVersionUID = -1613993657693189067L;
    private static final Gson GSON = new Gson();
    private final RmiFacade rmiFacade;

    public SikuliNodeServlet() {
        this(null);
    }

    public SikuliNodeServlet(GridRegistry registry) {
        super(registry);
        rmiFacade = new RmiFacade();
        rmiFacade.add("screen", new RemoteScreen());
        rmiFacade.add("keyboard", new RemoteKeyboard());
        rmiFacade.add("mouse", new RemoteMouse());
        rmiFacade.add("file", new RemoteFile());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        String objectId = getObjectId(request);
        if (objectId == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Can't find object ID in URL string");
            return;
        }
        MethodInvocationDto method = GSON.fromJson(request.getReader(), MethodInvocationDto.class);
        MethodInvocationResultDto result = rmiFacade.invoke(objectId, method);
        response.getWriter().write(GSON.toJson(result));
    }

    private String getObjectId(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        Pattern pattern = Pattern.compile(".+/([^/]+)");
        Matcher matcher = pattern.matcher(requestURI);
        if (!matcher.matches()) {
            return null;
        }
        return matcher.group(1);
    }

}
