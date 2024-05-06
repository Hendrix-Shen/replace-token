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
        ReplaceTokenExtension extension = target.getExtensions().create("replaceToken",
                ReplaceTokenExtension.class, target);
        target.getPlugins().apply("java");

        target.afterEvaluate(t -> {
            for (SourceSet sourceSet : extension.getTargetSourceSets().getOrElse(Collections.emptyList())) {
                String taskName = String.format("replace%sTokens", StringUtils.capitalize(sourceSet.getName()));
                Task compileJava = target.getTasks().getByName(sourceSet.getCompileJavaTaskName());
                compileJava.getOutputs().upToDateWhen(s -> false);
                target.getTasks().create(taskName, ReplaceTokenTask.class, task -> task.setSourceSet(sourceSet));
                compileJava.finalizedBy(taskName);
            }
        });
    }
}
