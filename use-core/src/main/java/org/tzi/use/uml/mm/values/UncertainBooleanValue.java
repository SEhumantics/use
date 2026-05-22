package org.tzi.use.uml.mm.values;

import org.tzi.use.uml.mm.types.Type;

public abstract class UncertainBooleanValue extends UncertainValue {

    protected UncertainBooleanValue(Type t) {
        super(t);
    }

    public abstract UncertainBooleanValue not();
}
