package org.tzi.use.parser.ocl;

import java.util.Set;

import org.tzi.use.parser.Context;
import org.tzi.use.util.SemanticException;
import org.tzi.use.uml.mm.expr.ExpConstUInteger;
import org.tzi.use.uml.mm.expr.ExpInvalidException;
import org.tzi.use.uml.mm.expr.Expression;

public class ASTUIntegerLiteral extends ASTExpression {

    private ASTExpression eValue;
    private ASTExpression eUncertainty;

    public ASTUIntegerLiteral(ASTExpression eValue, ASTExpression eUncertainty) {
        this.eValue = eValue;
        this.eUncertainty = eUncertainty;
    }

    @Override
    public Expression gen(Context ctx) throws SemanticException {
        Expression result = null;
        Expression value = eValue.gen(ctx);
        Expression uncertainty = eUncertainty.gen(ctx);

        try {
            result = new ExpConstUInteger(value, uncertainty);
        }
        catch (ExpInvalidException ex) {
            throw new SemanticException(ex.getMessage());
        }

        return result;
    }

    @Override
    public void getFreeVariables(Set<String> freeVars) {
        eValue.getFreeVariables(freeVars);
        eUncertainty.getFreeVariables(freeVars);
    }

    @Override
    public String toString() {
        return "UInteger(" + eValue.toString() + ", " + eUncertainty.toString() + ")";
    }
}
