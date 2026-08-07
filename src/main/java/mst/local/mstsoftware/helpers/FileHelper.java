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

    private static final List<String> ALLOWED_EXTENSIONS = List.of(".jpg", ".jpeg", ".png", ".webp");
    private static final int MIN_FILES = 1, MAX_FILES = 3;

    @SneakyThrows
    public ArrayList<String> uploadFile(List<MultipartFile> files, String source) {
        validateFiles(files);

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
            String relativePath = filePath.replaceFirst("^/" + uploadDir, "");
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

    private void validateFiles(List<MultipartFile> files) {
        if (files.size() < MIN_FILES) {
            throw new IllegalArgumentException("Tối thiểu " + MIN_FILES + " ảnh");
        }
        if (files.size() > MAX_FILES) {
            throw new IllegalArgumentException("Tối đa " + MAX_FILES + " ảnh");
        }
        for (var file : files) {
            String ext = getExtension(file.getOriginalFilename());
            if (!ALLOWED_EXTENSIONS.contains(ext)) {
                throw new IllegalArgumentException(
                        "Định dạng không hợp lệ: " + ext + ". Chỉ chấp nhận: " + ALLOWED_EXTENSIONS
                );
            }
        }

    }
}
