package org.tzi.use.uml.mm.types;

/**
 * Abstract base class for basic uncertainty types (UInteger, UReal, UBoolean, and UString).
 *
 * @author Víctor Manuel Ortiz Guardeño
 */
public abstract class UncertainType extends BasicType {

    protected UncertainType(String t) {
        super(t);
    }

}
