package com.redhat.qe.sikuli.common;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jeeva Kandasamy (jkandasa)
 */

public class SikuliCommonUtils {
    private static final Logger _logger = LoggerFactory.getLogger(SikuliCommonUtils.class.getName());

    private SikuliCommonUtils() {

    }

    public static String imageToBase64(BufferedImage bufferedImage) {
        return imageToBase64(bufferedImage, "png");
    }

    public static String imageToBase64(BufferedImage bufferedImage, String type) {
        try (final ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ImageIO.write(bufferedImage, type.toLowerCase(), outputStream);
            return Base64.getEncoder().encodeToString(outputStream.toByteArray());
        } catch (IOException ex) {
            _logger.error("Exception,", ex);
            throw new RuntimeException(ex);
        }
    }

    public static BufferedImage imageFromBase64(String base64png) {
        byte[] imageBytes = Base64.getDecoder().decode(base64png);
        try (InputStream inputStream = new ByteArrayInputStream(imageBytes)) {
            return ImageIO.read(inputStream);
        } catch (IOException ex) {
            _logger.error("Exception,", ex);
            throw new RuntimeException(ex);
        }
    }

    public static String base64(String path, String... more) {
        try {
            return Base64.getEncoder().encodeToString(Files.readAllBytes(Paths.get(path, more)));
        } catch (IOException ex) {
            _logger.error("Exception,", ex);
            throw new RuntimeException(ex);
        }
    }

    public static void saveToDisk(String base64Data, String path, String... more) {
        byte[] data = Base64.getDecoder().decode(base64Data);
        Path actualPath = Paths.get(path, more);
        try {
            Files.write(actualPath, data);
        } catch (IOException ex) {
            _logger.error("Exception,", ex);
            throw new RuntimeException(ex);
        }
    }
}
