package top.hendrixshen.replacetoken;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.CacheableTask;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.TaskAction;
import top.hendrixshen.replacetoken.asm.ReplaceTokenTransform;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;

public class ReplaceTokenTask extends DefaultTask {
    private static final String CLASS_SUFFIX = ".class";
    private SourceSet sourceSet;

    void setSourceSet(SourceSet sourceSet) {
        this.sourceSet = sourceSet;
    }

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
        String inputDir = extension.getInputDir().getOrElse("");
        Path baseDir = Objects.requireNonNull(this.sourceSet.getJava().getClassesDirectory().getOrNull())
                .getAsFile().toPath();
        Files.walkFileTree(baseDir.resolve(inputDir), new ReplaceTokenFileVisitor(extension, baseDir));
    }

    private static class ReplaceTokenFileVisitor implements FileVisitor<Path> {
        private final ReplaceTokenExtension extension;
        private final Path baseDir;

        public ReplaceTokenFileVisitor(ReplaceTokenExtension extension, Path baseDir) {
            this.extension = extension;
            this.baseDir = baseDir;
        }

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
            if (file.toFile().getName().endsWith(ReplaceTokenTask.CLASS_SUFFIX)) {
                new ReplaceTokenTransform(this.extension, file, baseDir).transform();
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
