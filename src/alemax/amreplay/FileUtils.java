package alemax.amreplay;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class FileUtils {

    public static void copyDirectory(Path source, Path destination) throws IOException {
        Files.walk(source)
                .forEach(src -> {
                    try {
                        Files.copy(src, destination.resolve(source.relativize(src)),
                                StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

}
