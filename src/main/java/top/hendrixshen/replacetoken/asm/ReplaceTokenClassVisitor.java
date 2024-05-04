package top.hendrixshen.replacetoken.asm;

import org.objectweb.asm.*;

import java.util.Map;

public class ReplaceTokenClassVisitor extends ClassVisitor {
    private final Map<String, Object> tokens;

    public ReplaceTokenClassVisitor(int api, ClassVisitor cv, Map<String, Object> tokens) {
        super(api, cv);
        this.tokens = tokens;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        return new ReplaceTokenMethodVisitor(this.api,
                super.visitMethod(access, name, descriptor, signature, exceptions), this.tokens);
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        if (value instanceof String) {
            value = this.tokens.getOrDefault(value, value);
        }

        return new ReplaceTokenFieldVisitor(this.api, super.visitField(access, name, descriptor, signature, value),
                this.tokens);
    }

    @Override
    public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        return new ReplaceTokenAnnotationVisitor(this.api,
                super.visitTypeAnnotation(typeRef, typePath, descriptor, visible), this.tokens);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        return new ReplaceTokenAnnotationVisitor(this.api,
                super.visitAnnotation(descriptor, visible), this.tokens);
    }
}
