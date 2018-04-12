package com.redhat.qe.sikuli.common;

import org.sikuli.api.ScreenRegion;
import org.sikuli.api.robot.desktop.DesktopMouse;

/**
 * @author Jeeva Kandasamy (jkandasa)
 */

public class RemoteMouse extends DesktopMouse {

    public void click(ScreenRegion region) {
        click(region.getCenter());
    }
}
