package com.redhat.qe.sikuli.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Base64;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.Files;

/**
 * @author Jeeva Kandasamy (jkandasa)
 */

public class RemoteFile {
    private static final Logger _logger = LoggerFactory.getLogger(RemoteFile.class.getName());

    public String read(String filePath) throws IOException {
        try (InputStream inputStream = new FileInputStream(filePath);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            IOUtils.copy(inputStream, outputStream);
            return Base64.getEncoder().encodeToString(outputStream.toByteArray());
        }
    }

    public String save(String base64Data, String extension) throws IOException {
        File tempFile = File.createTempFile("upload", extension);
        byte[] data = Base64.getDecoder().decode(base64Data);
        try (InputStream inputStream = new ByteArrayInputStream(data);
                OutputStream outputStream = new FileOutputStream(tempFile)) {
            IOUtils.copy(inputStream, outputStream);
        }
        _logger.debug("TempFile path:{}", tempFile.getAbsolutePath());
        return tempFile.getAbsolutePath();
    }

    public String saveZip(String base64Zip) throws IOException {
        byte[] zipBytes = DatatypeConverter.parseBase64Binary(base64Zip);
        File outputFolder = Files.createTempDir();
        unZipIt(new ByteArrayInputStream(zipBytes), outputFolder);
        return outputFolder.getAbsolutePath();
    }

    private static void unZipIt(InputStream inputStream, File outputFolder) throws IOException {
        try (ZipInputStream zipInputStream = new ZipInputStream(inputStream)) {
            ZipEntry zipEntry;
            while (null != (zipEntry = zipInputStream.getNextEntry())) {
                String fileName = zipEntry.getName();
                File outFile = new File(outputFolder, fileName);
                if (zipEntry.isDirectory()) {
                    outFile.mkdir();
                } else {
                    outFile.getParentFile().mkdirs();
                    try (FileOutputStream fileOutputStream = new FileOutputStream(outFile)) {
                        IOUtils.copy(zipInputStream, fileOutputStream);
                    }
                }
            }
        }
    }
}