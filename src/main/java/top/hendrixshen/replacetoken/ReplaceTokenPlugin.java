package top.hendrixshen.replacetoken;

import org.apache.commons.lang3.StringUtils;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.tasks.SourceSet;

import java.util.Collections;

public class ReplaceTokenPlugin implements Plugin<Project> {
    @Override
    public void apply(Project target) {
        ReplaceTokenExtension extension = target.getExtensions().create("replaceToken", ReplaceTokenExtension.class);

        target.getPlugins().apply("java");

        target.afterEvaluate(t -> {
            for (SourceSet sourceSet : extension.getTargetSourceSets().getOrElse(Collections.emptyList())) {
                String taskName = String.format("replace%sTokens", StringUtils.capitalize(sourceSet.getName()));
                Task compileJava = target.getTasks().getByName(sourceSet.getCompileJavaTaskName());
                compileJava.getOutputs().upToDateWhen(s -> false);
                target.getTasks().register(taskName, ReplaceTokenTask.class, task -> {
                    task.getInputClassesDirectory().set(sourceSet.getJava().getClassesDirectory().get().getAsFile());

                    task.getInputDir().set(extension.getInputDir());
                    task.getOutputDir().set(extension.getOutputDir());
                    task.getGlobalTokens().set(extension.getGlobalTokens());
                    task.getLocalTokens().set(extension.getLocalTokens());
                    task.getGlobalClasses().set(extension.getGlobalClasses());
                });
                compileJava.finalizedBy(taskName);
            }
        });
    }
}
