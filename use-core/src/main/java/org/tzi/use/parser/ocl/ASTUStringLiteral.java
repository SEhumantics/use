package org.tzi.use.parser.ocl;

import java.util.Set;

import org.tzi.use.parser.Context;
import org.tzi.use.util.SemanticException;
import org.tzi.use.uml.mm.expr.ExpConstUString;
import org.tzi.use.uml.mm.expr.ExpInvalidException;
import org.tzi.use.uml.mm.expr.Expression;

public class ASTUStringLiteral extends ASTExpression {

    private ASTExpression eValue;
    private ASTExpression eConf;

    public ASTUStringLiteral(ASTExpression eValue, ASTExpression eConf) {
        this.eValue = eValue;
        this.eConf = eConf;
    }

    @Override
    public Expression gen(Context ctx) throws SemanticException {
        ExpConstUString result;

        try {
            result = new ExpConstUString(eValue.gen(ctx), eConf.gen(ctx));
        }
        catch (ExpInvalidException ex) {
            throw new SemanticException(ex.getMessage());
        }

        return result;
    }

    @Override
    public void getFreeVariables(Set<String> freeVars) {
        eValue.getFreeVariables(freeVars);
        eConf.getFreeVariables(freeVars);
    }

    @Override
    public String toString() {
        return "UString(" + eValue.toString() + ", " + eConf.toString() + ")";
    }
}
