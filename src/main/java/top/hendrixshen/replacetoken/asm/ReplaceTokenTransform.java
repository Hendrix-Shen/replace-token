package top.hendrixshen.replacetoken.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import top.hendrixshen.replacetoken.ReplaceTokenExtension;
import top.hendrixshen.replacetoken.util.FileUtil;

import java.nio.file.Path;
import java.util.Map;

public class ReplaceTokenTransform {
    private final ReplaceTokenExtension extension;
    private final Path path;

    public ReplaceTokenTransform(ReplaceTokenExtension extension, Path file) {
        this.extension = extension;
        this.path = file;
    }

    public void transform() {
        byte[] bytes = FileUtil.readBytes(this.path.toString());
        ClassReader cr = new ClassReader(bytes);
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        ClassVisitor cv = null;
        String className = cr.getClassName();
        boolean modified = false;

        if (this.extension.getGlobalClasses().isEmpty() || this.extension.getGlobalClasses().contains(className)) {
            cv = new ReplaceTokenClassVisitor(Opcodes.ASM9, cw, this.extension.getGlobalTokens());
            modified = true;
        }

        if (this.extension.getLocalTokens().containsKey(className)) {
            cv = new ReplaceTokenClassVisitor(Opcodes.ASM9, cv == null ? cw : cv, this.extension.getLocalTokens().get(className));
            modified = true;
        }

        if (modified) {
            cr.accept(cv, ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
            Path relativePath = this.extension.getInputDir().relativize(path);
            FileUtil.writeBytes(this.extension.getOutputDir().resolve(relativePath).toString(), cw.toByteArray());
        }
    }
}
