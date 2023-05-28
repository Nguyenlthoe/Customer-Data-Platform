package bk.edu.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class UploadService {

    public static final Path CURRENT_FOLDER = Paths.get(System.getProperty("user.dir"));
    public String uploadFile(MultipartFile image) {
        Path staticPath = Paths.get("static");
        Path imagePath = Paths.get("images");
        String imageName = image.getOriginalFilename().replace(" ", "_");
        Path file = CURRENT_FOLDER.resolve(staticPath)
                .resolve(imagePath).resolve(imageName);
        try{
            OutputStream os = Files.newOutputStream(file);
            os.write(image.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return file.toFile().getAbsolutePath();
    }
}
