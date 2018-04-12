package com.redhat.qe.sikuli.common;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.sikuli.api.DesktopScreenRegion;
import org.sikuli.api.ImageTarget;
import org.sikuli.api.ScreenRegion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jeeva Kandasamy (jkandasa)
 */

public class RemoteScreen extends DesktopScreenRegion {
    private static final Logger _logger = LoggerFactory.getLogger(RemoteScreen.class.getName());

    private static final String ERROR_TARGET_NOT_AVAILABLE = "TargetName[{0}] not available in the repository."
            + " Add it before to use.";
    private final HashMap<String, ImageTarget> imagesRepository = new HashMap<>();

    public void addTarget(String targetName, String base64png) {
        addTarget(targetName, base64png, 0.8);
    }

    public void clearAllTargets() {
        _logger.debug("About to clear {} target(s)", imagesRepository.size());
        imagesRepository.clear();
    }

    public List<String> listTargets() {
        ArrayList<String> targets = new ArrayList<String>();
        for (String target : imagesRepository.keySet()) {
            targets.add(target);
        }
        return targets;
    }

    public void addTarget(String targetName, String base64png, double minScore) {
        ImageTarget imageTarget = new ImageTarget(SikuliCommonUtils.imageFromBase64(base64png));
        imageTarget.setMinScore(minScore);
        imagesRepository.put(targetName, imageTarget);
        _logger.debug("New target[{}] added. Number of targets:{}", targetName, imagesRepository.size());
    }

    public String captureBase64() {
        return SikuliCommonUtils.imageToBase64(capture());
    }

    public String captureBase64(ScreenRegion region) {
        return SikuliCommonUtils.imageToBase64(region.capture());
    }

    public ScreenRegion find(String targetName) {
        if (!imagesRepository.containsKey(targetName)) {
            throw new RuntimeException(MessageFormat.format(ERROR_TARGET_NOT_AVAILABLE, targetName));
        }
        try {
            return find(imagesRepository.get(targetName));
        } catch (Exception ex) {
            _logger.error("Exception,", ex);
            throw new RuntimeException(ex.getMessage());
        }
    }

    public List<ScreenRegion> findAll(String targetName) {
        if (!imagesRepository.containsKey(targetName)) {
            throw new RuntimeException(MessageFormat.format(ERROR_TARGET_NOT_AVAILABLE, targetName));
        }
        try {
            return findAll(imagesRepository.get(targetName));
        } catch (Exception ex) {
            _logger.error("Exception,", ex);
            throw new RuntimeException(ex.getMessage());
        }
    }

}
