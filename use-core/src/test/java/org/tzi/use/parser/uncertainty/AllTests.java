package org.tzi.use.parser.uncertainty;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

    private AllTests() {
    }

    public static Test suite() {
        final TestSuite test = new TestSuite("All parser uncertainty tests");
        test.addTestSuite(org.tzi.use.parser.uncertainty.USECompilerUncertaintyTest.class);
        return test;
    }
}
