package mst.local.mstsoftware.helpers;

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
public class UploadHelper {
    @Value("${app.upload.dir}")
    private String uploadDir;

    public ArrayList<String> uploadFile(List<MultipartFile> files, String source) throws IOException {
        var paths = new ArrayList<String>();
        final String normalizedPath = (source.charAt(0) == '/' ? source : "/" + source).toLowerCase();

        Path dir = Paths.get(uploadDir + normalizedPath);
        if (!Files.exists(dir)) Files.createDirectories(dir);
        for (var file : files) {
            String ext = getExtension(file.getOriginalFilename());
            String fileName = UUID.randomUUID() + ext;
            Files.copy(file.getInputStream(), dir.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);

            paths.add("/uploads" + normalizedPath + "/" + fileName);
        }
        return paths;
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
    }
}
