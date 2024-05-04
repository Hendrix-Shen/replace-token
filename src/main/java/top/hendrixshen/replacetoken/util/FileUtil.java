package top.hendrixshen.replacetoken.util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Reference to <a href="https://github.com/lsieun/learn-java-asm">learn-java-asm</a>
 */
public class FileUtil {
    public static byte[] readBytes(String filepath) {
        File file = new File(filepath);

        if (!file.exists()) {
            throw new IllegalArgumentException("File Not Exist: " + filepath);
        }

        InputStream in = null;

        try {
            in = Files.newInputStream(file.toPath());
            in = new BufferedInputStream(in);
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            IOUtil.copy(in, bao);
            return bao.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtil.closeQuietly(in);
        }

        throw new RuntimeException("Failed to read file: " + filepath);
    }

    public static void writeBytes(String filepath, byte[] bytes) {
        File file = new File(filepath);
        File dirFile = file.getParentFile();
        FileUtil.mkdirs(dirFile);

        try (OutputStream out = Files.newOutputStream(Paths.get(filepath));
             BufferedOutputStream buff = new BufferedOutputStream(out)) {
            buff.write(bytes);
            buff.flush();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void mkdirs(File dirFile) {
        boolean file_exists = dirFile.exists();

        if (file_exists && dirFile.isDirectory()) {
            return;
        }

        if (file_exists && dirFile.isFile()) {
            throw new RuntimeException("Not A Directory: " + dirFile);
        }

        if (!file_exists) {
            dirFile.mkdirs();
        }
    }
}
