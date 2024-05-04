package top.hendrixshen.replacetoken.asm;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.TypePath;

import java.util.Map;

public class ReplaceTokenMethodVisitor extends MethodVisitor {
    private final Map<String, Object> tokens;

    public ReplaceTokenMethodVisitor(int api, MethodVisitor mv, Map<String, Object> tokens) {
        super(api, mv);
        this.tokens = tokens;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        return new ReplaceTokenAnnotationVisitor(this.api, super.visitAnnotation(descriptor, visible), this.tokens);
    }

    @Override
    public AnnotationVisitor visitInsnAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        return new ReplaceTokenAnnotationVisitor(this.api,
                super.visitInsnAnnotation(typeRef, typePath, descriptor, visible), this.tokens);
    }

    @Override
    public AnnotationVisitor visitLocalVariableAnnotation(int typeRef, TypePath typePath, Label[] start, Label[] end,
                                                          int[] index, String descriptor, boolean visible) {
        return new ReplaceTokenAnnotationVisitor(this.api, super.visitLocalVariableAnnotation(typeRef, typePath,
                start, end, index, descriptor, visible), this.tokens);
    }

    @Override
    public AnnotationVisitor visitParameterAnnotation(int parameter, String descriptor, boolean visible) {
        return new ReplaceTokenAnnotationVisitor(this.api,
                super.visitParameterAnnotation(parameter, descriptor, visible), this.tokens);
    }

    @Override
    public AnnotationVisitor visitTryCatchAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        return new ReplaceTokenAnnotationVisitor(this.api,
                super.visitTryCatchAnnotation(typeRef, typePath, descriptor, visible), this.tokens);
    }

    @Override
    public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        return new ReplaceTokenAnnotationVisitor(this.api,
                super.visitTypeAnnotation(typeRef, typePath, descriptor, visible), this.tokens);
    }

    @Override
    public void visitLdcInsn(Object value) {
        value = this.tokens.getOrDefault(value.toString(), value);
        super.visitLdcInsn(value);
    }
}
