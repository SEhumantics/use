package org.tzi.use.uml.mm.expr;

import org.tzi.use.uml.mm.types.Type;
import org.tzi.use.uml.mm.types.TypeFactory;
import org.tzi.use.uml.mm.values.StringValue;
import org.tzi.use.uml.mm.values.UStringValue;
import org.tzi.use.uml.mm.values.UndefinedValue;
import org.tzi.use.uml.mm.values.Value;

public class ExpConstUString extends Expression {

    private Expression eValue;
    private Expression eConf;

    public ExpConstUString(Expression eValue, Expression eConf) throws ExpInvalidException {
        super(TypeFactory.mkUString());

        if (!eConf.type().isKindOfReal(Type.VoidHandling.EXCLUDE_VOID))
            throw new ExpInvalidException("UString : confidance need to be kind of Real");

        if (!eValue.type().isTypeOfString())
            throw new ExpInvalidException("UString : value must be type of String");

        this.eValue = eValue;
        this.eConf = eConf;
    }

    public Expression valueExpression() {
        return eValue;
    }

    public Expression confidenceExpression() {
        return eConf;
    }

    @Override
    public Value eval(EvalContext ctx) {
        Value ustring;
        StringValue value;
        double confidenceValue;
        Value confidence;

        ctx.enter(this);
        value = (StringValue) eValue.eval(ctx);
        confidence = eConf.eval(ctx);

        // confidance must be between 0 and 1
        confidenceValue = Double.valueOf(confidence.toString());
        if (confidenceValue < 0 || confidenceValue > 1)
            ustring = UndefinedValue.instance;
        else
            ustring = new UStringValue(value.value(), confidenceValue);

        ctx.exit(this, ustring);

        return ustring;
    }

    @Override
    protected boolean childExpressionRequiresPreState() {
        return false;
    }

    @Override
    public StringBuilder toString(StringBuilder sb) {
        return sb.append("UString(")
                .append(eValue.toString()).append(",")
                .append(eConf.toString()).append(")");
    }

    @Override
    public void processWithVisitor(ExpressionVisitor visitor) {
        visitor.visitConstUString(this);
    }
}
