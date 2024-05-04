package top.hendrixshen.replacetoken;

import org.gradle.api.Project;
import org.gradle.api.tasks.CacheableTask;
import org.gradle.api.tasks.Input;

import java.nio.file.Path;
import java.util.*;

@CacheableTask
public class ReplaceTokenExtension {
    private final transient Project project;
    private Path inputDir;
    private Path outputDir;
    private final Map<String, Object> globalTokens = new LinkedHashMap<>();
    private final Map<String, Map<String, Object>> localTokens = new LinkedHashMap<>();
    private final Set<String> globalClasses = new LinkedHashSet<>();

    public ReplaceTokenExtension(Project project) {
        this.project = project;
        this.inputDir = this.project.getProject().getLayout().getBuildDirectory()
                .dir("classes").get().getAsFile().toPath();
        this.outputDir = this.inputDir;
    }

    public Path getInputDir() {
        return this.inputDir;
    }

    public Path getOutputDir() {
        return this.outputDir;
    }

    @Input
    public void setInputDir(Path inputDir) {
        this.inputDir = inputDir;
    }

    @Input
    public void setOutputDir(Path outputDir) {
        this.outputDir = outputDir;
    }

    @Input
    public void replace(Object token, Object replacement) {
        this.globalTokens.put(token.toString(), replacement);
    }

    @Input
    public void replace(Object token, Map<Object, Object> replacementMap) {
        for (Map.Entry<Object, Object> entry: replacementMap.entrySet()) {
            this.replace(entry.getKey(), entry.getValue());
        }
    }

    @Input
    public void replace(Object token, Object replacement, String clazz) {
        this.localTokens.computeIfAbsent(clazz, k -> new LinkedHashMap<>()).put(token.toString(), replacement);
    }

    @Input
    public void replaceIn(String clazz) {
        this.globalClasses.add(clazz);
    }

    public Set<String> getGlobalClasses() {
        return this.globalClasses;
    }

    public Map<String, Object> getGlobalTokens() {
        return this.globalTokens;
    }

    public Map<String, Map<String, Object>> getLocalTokens() {
        return this.localTokens;
    }

    public Project getProject() {
        return this.project;
    }
}
