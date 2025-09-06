package top.hendrixshen.replacetoken.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import top.hendrixshen.replacetoken.ReplaceTokenTask;
import top.hendrixshen.replacetoken.util.FileUtil;

import java.nio.file.Path;
import java.util.Map;
import java.util.Set;

public class ReplaceTokenTransform {
    private final ReplaceTokenTask task;
    private final Path inFile;
    private final Path outFile;

    public ReplaceTokenTransform(ReplaceTokenTask task, Path in, Path baseDir) {
        this.task = task;
        this.inFile = in;
        Path relativePath = baseDir.relativize(this.inFile);
        this.outFile = baseDir.resolve(this.task.getOutputDir().getOrElse(""))
                .resolve(relativePath);
    }

    public void transform() {
        byte[] bytes = FileUtil.readBytes(this.inFile.toString());
        ClassReader cr = new ClassReader(bytes);
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        ClassVisitor cv = null;
        String className = cr.getClassName();
        boolean modified = false;

        Set<String> globalClasses = this.task.getGlobalClasses().get();
        Map<String, Object> globalTokens = this.task.getGlobalTokens().get();
        Map<String, Map<String, Object>> localTokens = this.task.getLocalTokens().get();

        if (globalClasses.isEmpty() || globalClasses.contains(className)) {
            cv = new ReplaceTokenClassVisitor(Opcodes.ASM9, cw, globalTokens);
            modified = true;
        }

        if (localTokens.containsKey(className)) {
            cv = new ReplaceTokenClassVisitor(Opcodes.ASM9, cv == null ? cw : cv, localTokens.get(className));
            modified = true;
        }

        if (modified) {
            cr.accept(cv, ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
            FileUtil.writeBytes(this.outFile.toString(), cw.toByteArray());
        }
    }
}
