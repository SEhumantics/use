package org.tzi.use.uml.mm.expr;

import org.tzi.use.uml.mm.types.TypeFactory;
import org.tzi.use.uml.mm.values.UBooleanValue;
import org.tzi.use.uml.mm.values.UndefinedValue;
import org.tzi.use.uml.mm.values.Value;

public class ExpConstUBoolean extends Expression {
    private Expression eValue;
    private Expression eProbability;

    public ExpConstUBoolean(Expression eValue, Expression eProbability)
    throws ExpInvalidException
    {
        super(TypeFactory.mkUBoolean());

        if (!eValue.type().isTypeOfBoolean())
            throw new ExpInvalidException("Value must be Boolean");

        if (!(eProbability.type().isTypeOfInteger() || eProbability.type().isTypeOfReal()))
            throw new ExpInvalidException("Probability must be a Integer or Real");

        this.eValue = eValue;
        this.eProbability = eProbability;
    }

    public String value() {
        return eValue.toString();
    }

    public String probability() {
        return eProbability.toString();
    }

    @Override
    public Value eval(EvalContext ctx) {
        Value res = null;
        Value value, probability;

        ctx.enter(this);
        value = eValue.eval(ctx);
        probability = eProbability.eval(ctx);

        if (probability.isUndefined())
            res = UndefinedValue.instance;
        else try {
            res = UBooleanValue.valueOf(Boolean.valueOf(value.toString()), Double.valueOf(probability.toString()));
        }
        catch (RuntimeException ex) {
            res = UndefinedValue.instance;
        }

        ctx.exit(this, res);

        return res;
    }

    @Override
    protected boolean childExpressionRequiresPreState() {
        return false;
    }

    @Override
    public StringBuilder toString(StringBuilder sb) {
        sb.append("UBoolean(")
                .append(eValue.toString())
                .append(",")
                .append(eProbability.toString())
                .append(")");
        return sb;
    }

    @Override
    public void processWithVisitor(ExpressionVisitor visitor) {
        visitor.visitConstUBoolean(this);
    }

}
