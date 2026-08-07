package mst.local.mstsoftware.helpers;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class FileHelper {
    @Value("${app.upload.dir}")
    private String uploadDir;

    @SneakyThrows
    public ArrayList<String> uploadFile(List<MultipartFile> files, String source) {
        var paths = new ArrayList<String>();
        final String normalizedPath = (source.charAt(0) == '/' ? source : "/" + source).toLowerCase();

        Path dir = Paths.get(uploadDir + normalizedPath);
        if (!Files.exists(dir)) Files.createDirectories(dir);
        for (var file : files) {
            String ext = getExtension(file.getOriginalFilename());
            String fileName = UUID.randomUUID() + ext;
            Files.copy(file.getInputStream(), dir.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);

            paths.add("/" + uploadDir + normalizedPath + "/" + fileName);
        }
        return paths;
    }

    public boolean removeFile(String filePath) {
        try {
            String relativePath = filePath.replaceFirst("^/upload/", "");
            Path file = Paths.get(uploadDir + relativePath);
            return Files.deleteIfExists(file);
        } catch (IOException e) {
            return false;
        }
    }

    @SneakyThrows
    public void removeFiles(List<String> filePaths) {
        filePaths.forEach(this::removeFile);
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
    }
}
