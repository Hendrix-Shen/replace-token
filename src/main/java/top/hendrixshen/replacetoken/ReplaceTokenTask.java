package top.hendrixshen.replacetoken;

import org.gradle.api.DefaultTask;
import org.gradle.api.provider.MapProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.provider.SetProperty;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.Optional;
import org.gradle.api.tasks.TaskAction;
import top.hendrixshen.replacetoken.asm.ReplaceTokenTransform;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Map;

public abstract class ReplaceTokenTask extends DefaultTask {
    private static final String CLASS_SUFFIX = ".class";

    @Input
    public abstract Property<File> getInputClassesDirectory();

    @Optional
    @Input
    public abstract Property<String> getInputDir();

    @Optional
    @Input
    public abstract Property<String> getOutputDir();

    @Optional
    @Input
    public abstract MapProperty<String, Object> getGlobalTokens();

    @Optional
    @Input
    public abstract MapProperty<String, Map<String, Object>> getLocalTokens();

    @Optional
    @Input
    public abstract SetProperty<String> getGlobalClasses();

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
        String inputDir = this.getInputDir().getOrElse("");
        Path baseDir = this.getInputClassesDirectory().get().toPath();
        Files.walkFileTree(baseDir.resolve(inputDir), new ReplaceTokenFileVisitor(this, baseDir));
    }

    private static class ReplaceTokenFileVisitor implements FileVisitor<Path> {
        private final ReplaceTokenTask task;
        private final Path baseDir;

        public ReplaceTokenFileVisitor(ReplaceTokenTask task, Path baseDir) {
            this.task = task;
            this.baseDir = baseDir;
        }

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
            if (file.toFile().getName().endsWith(ReplaceTokenTask.CLASS_SUFFIX)) {
                new ReplaceTokenTransform(this.task, file, baseDir).transform();
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
