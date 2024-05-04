package top.hendrixshen.replacetoken.asm;

import org.objectweb.asm.AnnotationVisitor;

import java.util.Map;

public class
ReplaceTokenAnnotationVisitor extends AnnotationVisitor {
    private final Map<String, Object> tokens;

    protected ReplaceTokenAnnotationVisitor(int api, AnnotationVisitor av, Map<String, Object> tokens) {
        super(api, av);
        this.tokens = tokens;
    }

    @Override
    public void visit(String name, Object value) {
        value = this.replace(value);
        super.visit(name, value);
    }

    @Override
    public void visitEnum(String name, String descriptor, String value) {
        value = this.replace(value).toString();
        super.visitEnum(name, descriptor, value);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String name, String descriptor) {
        return new ReplaceTokenAnnotationVisitor(this.api, super.visitAnnotation(name, descriptor), this.tokens);
    }

    @Override
    public AnnotationVisitor visitArray(String name) {
        return new ReplaceTokenAnnotationVisitor(this.api, super.visitArray(name), this.tokens);
    }

    private Object replace(Object value) {
        if (value instanceof String && this.tokens.containsKey(value)) {
            value = this.tokens.get(value);
        }

        return value;
    }
}
