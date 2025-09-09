package top.hendrixshen.replacetoken;

import org.gradle.api.provider.Property;
import org.gradle.api.tasks.SourceSet;

import java.util.*;

public abstract class ReplaceTokenExtension {
    private final Map<String, Object> globalTokens = new LinkedHashMap<>();
    private final Map<String, Map<String, Object>> localTokens = new LinkedHashMap<>();
    private final Set<String> globalClasses = new LinkedHashSet<>();

    /**
     * The target source sets to process for token replacement.
     */
    public abstract Property<Collection<SourceSet>> getTargetSourceSets();

    /**
     * The input directory path containing class files to process.
     */
    public abstract Property<String> getInputDir();

    /**
     * The output directory path for processed class files.
     */
    public abstract Property<String> getOutputDir();

    /**
     * Adds a global token replacement rule that applies to all target classes.
     * The token will be replaced with the specified replacement value in all
     * classes that are processed.
     *
     * @param token       the token string to be replaced (cannot be null)
     * @param replacement the replacement value (cannot be null)
     */
    public void replace(Object token, Object replacement) {
        Objects.requireNonNull(token, "Token cannot be null");
        Objects.requireNonNull(replacement, "Replacement cannot be null");

        this.globalTokens.put(token.toString(), replacement);
    }

    /**
     * Adds multiple global token replacement rules from a map.
     * All token-replacement pairs in the map will be applied globally to all target classes.
     *
     * @param replacementMap map containing token-replacement pairs where keys are tokens
     *                       to be replaced and values are replacement objects (cannot be null or empty)
     */
    public void replace(Map<Object, Object> replacementMap) {
        Objects.requireNonNull(replacementMap, "Replacement map cannot be null");
        if (replacementMap.isEmpty()) {
            throw new IllegalArgumentException("Replacement map cannot be empty");
        }

        for (Map.Entry<Object, Object> entry : replacementMap.entrySet()) {
            this.replace(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Adds a local token replacement rule for a specific class.
     * The token will only be replaced in the specified class, not globally.
     *
     * @param token       the token string to be replaced (cannot be null)
     * @param replacement the replacement value (cannot be null)
     * @param clazz       the fully qualified name of the target class (cannot be null, blank, or empty)
     */
    public void replace(Object token, Object replacement, String clazz) {
        Objects.requireNonNull(token, "Token cannot be null");
        Objects.requireNonNull(replacement, "Replacement cannot be null");
        Objects.requireNonNull(clazz, "Class name cannot be null");
        if (clazz.trim().isEmpty()) {
            throw new IllegalArgumentException("Class name cannot be blank or empty");
        }

        this.localTokens.computeIfAbsent(clazz, k -> new LinkedHashMap<>()).put(token.toString(), replacement);
    }

    /**
     * Specifies that global token replacements should be applied to the given class.
     * This method adds the class to the set of classes that will receive global token replacements.
     *
     * @param clazz the fully qualified name of the target class (cannot be null, blank, or empty)
     */
    public void replaceIn(String clazz) {
        Objects.requireNonNull(clazz, "Class name cannot be null");
        if (clazz.trim().isEmpty()) {
            throw new IllegalArgumentException("Class name cannot be blank or empty");
        }

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
}
