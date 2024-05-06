package top.hendrixshen.replacetoken.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import top.hendrixshen.replacetoken.ReplaceTokenExtension;
import top.hendrixshen.replacetoken.util.FileUtil;

import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;

public class ReplaceTokenTransform {
    private final ReplaceTokenExtension extension;
    private final Path inFile;
    private final Path outFile;

    public ReplaceTokenTransform(ReplaceTokenExtension extension, Path in, Path baseDir) {
        this.extension = extension;
        this.inFile = in;
        Path relativePath = baseDir.relativize(this.inFile);
        this.outFile = baseDir.resolve(this.extension.getOutputDir().getOrElse(""))
                .resolve(relativePath);
    }

    public void transform() {
        byte[] bytes = FileUtil.readBytes(this.inFile.toString());
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
            FileUtil.writeBytes(this.outFile.toString(), cw.toByteArray());
        }
    }
}
