package org.tzi.use.uml.mm.types;

import java.util.HashSet;
import java.util.Set;

/**
 * The OCL Integer with uncertainty.
 *
 * @author Víctor M. Ortiz
 */
public class UIntegerType extends UncertainType {

    UIntegerType() {
        super("UInteger");
    }

    @Override
    public boolean isKindOfNumber(VoidHandling h) {
        return true;
    }

    @Override
    public boolean isTypeOfUInteger() {
        return true;
    }

    @Override
    public boolean isKindOfUReal(VoidHandling h) {
        return true;
    }

    @Override
    public boolean isKindOfUInteger(VoidHandling h) {
        return true;
    }

    @Override
    public Set<? extends Type> allSupertypes() {
        Set<Type> res = new HashSet<>(3);
        res.add(TypeFactory.mkOclAny());
        res.add(TypeFactory.mkUReal());
        res.add(this);
        return res;
    }

    @Override
    public boolean conformsTo(Type other) {
        return other.isTypeOfUInteger() ||
                other.isTypeOfUReal() ||
                other.isTypeOfOclAny();
    }
}
