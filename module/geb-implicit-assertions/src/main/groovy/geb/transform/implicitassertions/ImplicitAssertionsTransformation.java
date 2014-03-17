package geb.transform.implicitassertions;

import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.control.CompilePhase;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.transform.ASTTransformation;
import org.codehaus.groovy.transform.GroovyASTTransformation;

@GroovyASTTransformation(phase = CompilePhase.CANONICALIZATION)
public class ImplicitAssertionsTransformation implements ASTTransformation {
    public void visit(ASTNode[] astNodes, SourceUnit sourceUnit) {
		for (ClassNode classNode : sourceUnit.getAST().getClasses()) {
            // Only alter classes that directly or indirectly extend or implement a geb superclass or interface.
            if (isGebClass(classNode)) {
                classNode.visitContents(new ImplicitAssertionsTransformationVisitor(sourceUnit));
            }
		}
    }
    
    boolean isGebClass(ClassNode classNode) {
        if (classNode == null) {
            return false;
        }
        if (classNode.getPackageName() != null && classNode.getPackageName().startsWith("geb.")) {
            return true;
        }
        ClassNode[] interfaces = classNode.getInterfaces();
        for (ClassNode anInterface : interfaces) {
            if (isGebClass(anInterface)) {
                return true;
            }
        }
        return isGebClass(classNode.getSuperClass());
    }
}