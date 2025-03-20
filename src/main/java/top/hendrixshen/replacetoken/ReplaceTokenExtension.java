package top.hendrixshen.replacetoken;

import org.gradle.api.Project;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.SourceSet;

import java.util.*;

public abstract class ReplaceTokenExtension {
    private final transient Project project;
    private final Map<String, Object> globalTokens = new LinkedHashMap<>();
    private final Map<String, Map<String, Object>> localTokens = new LinkedHashMap<>();
    private final Set<String> globalClasses = new LinkedHashSet<>();

    static ReplaceTokenExtension getExtension(Project project) {
        return project.getExtensions().getByType(ReplaceTokenExtension.class);
    }

    public ReplaceTokenExtension(Project project) {
        this.project = project;
    }

    public abstract Property<Collection<SourceSet>> getTargetSourceSets();

    public abstract Property<String> getInputDir();

    public abstract Property<String> getOutputDir();

    public void replace(Object token, Object replacement) {
        this.globalTokens.put(token.toString(), replacement);
    }

    public void replace(Object token, Map<Object, Object> replacementMap) {
        for (Map.Entry<Object, Object> entry: replacementMap.entrySet()) {
            this.replace(entry.getKey(), entry.getValue());
        }
    }

    public void replace(Object token, Object replacement, String clazz) {
        this.localTokens.computeIfAbsent(clazz, k -> new LinkedHashMap<>()).put(token.toString(), replacement);
    }

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
