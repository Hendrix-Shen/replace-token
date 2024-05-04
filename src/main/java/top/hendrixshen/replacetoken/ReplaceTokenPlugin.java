package top.hendrixshen.replacetoken;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;

public class ReplaceTokenPlugin implements Plugin<Project> {
    @Override
    public void apply(Project target) {
        target.getExtensions().create("replaceToken",
                ReplaceTokenExtension.class, target);
        target.getPlugins().apply("java");

        target.afterEvaluate(t -> {
            Task classes = target.getTasks().getByName("classes");
            Task compileJava = target.getTasks().getByName("compileJava");
            compileJava.getOutputs().upToDateWhen(s -> false);
            target.getTasks().register("replaceTokensInClasses", ReplaceTokenTask.class);
            classes.finalizedBy("replaceTokensInClasses");
        });
    }
}
