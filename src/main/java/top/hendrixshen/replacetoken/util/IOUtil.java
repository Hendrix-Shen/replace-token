package top.hendrixshen.replacetoken.util;

import java.io.*;

/**
 * Reference to <a href="https://github.com/lsieun/learn-java-asm">learn-java-asm</a>
 */
public class IOUtil {
    public static final int EOF = -1;

    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

    public static int copy(InputStream input, OutputStream output) throws IOException {
        long count = IOUtil.copyLarge(input, output);

        if (count > Integer.MAX_VALUE) {
            return -1;
        }

        return (int) count;
    }

    public static long copyLarge(InputStream input, OutputStream output) throws IOException {
        return IOUtil.copy(input, output, IOUtil.DEFAULT_BUFFER_SIZE);
    }

    public static long copy(InputStream input, OutputStream output, int bufferSize) throws IOException {
        return IOUtil.copyLarge(input, output, new byte[bufferSize]);
    }

    public static long copyLarge(InputStream input, OutputStream output, byte[] buffer) throws IOException {
        long count = 0;
        int n;

        while (IOUtil.EOF != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }

        return count;
    }

    public static void closeQuietly(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (final IOException ignore) {
        }
    }
}
