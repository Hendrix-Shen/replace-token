package top.hendrixshen.replacetoken;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
import top.hendrixshen.replacetoken.asm.ReplaceTokenTransform;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class ReplaceTokenTask extends DefaultTask {
    private static final String CLASS_SUFFIX = ".class";

    @TaskAction
    public void runTask() throws IOException {
        try {
            this.runTaskImpl();
        } catch (Exception e) {
            this.getLogger().error("Failed to execute replacement task.", e);
            throw e;
        }
    }

    private void runTaskImpl() throws IOException {
        ReplaceTokenExtension extension = this.getProject().getExtensions().getByType(ReplaceTokenExtension.class);
        Files.walkFileTree(extension.getInputDir(), new ReplaceTokenFileVisitor(extension));

    }

    private static class ReplaceTokenFileVisitor implements FileVisitor<Path> {
        private final ReplaceTokenExtension extension;

        public ReplaceTokenFileVisitor(ReplaceTokenExtension extension) {
            this.extension = extension;
        }

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
            if (file.toFile().getName().endsWith(ReplaceTokenTask.CLASS_SUFFIX)) {
                new ReplaceTokenTransform(this.extension, file).transform();
            }

            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) {
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
            return FileVisitResult.CONTINUE;
        }
    }
}
