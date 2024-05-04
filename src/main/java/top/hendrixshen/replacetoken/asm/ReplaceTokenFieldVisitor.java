package top.hendrixshen.replacetoken.asm;

import org.objectweb.asm.*;

import java.util.Map;

public class ReplaceTokenFieldVisitor extends FieldVisitor {
    private final Map<String, Object> tokens;

    public ReplaceTokenFieldVisitor(int api, FieldVisitor fv, Map<String, Object> tokens) {
        super(api, fv);
        this.tokens = tokens;
    }

    @Override
    public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        return new ReplaceTokenAnnotationVisitor(this.api,
                super.visitTypeAnnotation(typeRef, typePath, descriptor, visible), tokens);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        return new ReplaceTokenAnnotationVisitor(this.api, super.visitAnnotation(descriptor, visible), tokens);
    }
}
