package org.tzi.use.parser.uncertainty;

import junit.framework.TestCase;
import org.tzi.use.config.Options;
import org.tzi.use.parser.ocl.OCLCompiler;
import org.tzi.use.uml.mm.MModel;
import org.tzi.use.uml.mm.ModelFactory;
import org.tzi.use.uml.mm.expr.Evaluator;
import org.tzi.use.uml.mm.expr.Expression;
import org.tzi.use.uml.mm.values.Value;
import org.tzi.use.uml.mm.values.VarBindings;
import org.tzi.use.uml.sys.MSystem;
import org.tzi.use.uml.sys.MSystemState;
import org.tzi.use.util.SuffixFileFilter;

import java.io.*;
import java.net.URISyntaxException;

public class USECompilerUncertaintyTest extends TestCase {

    private static boolean VERBOSE = true;

    private static File TEST_PATH;

    static {
        try {
            TEST_PATH = new File(ClassLoader.getSystemResource("org/tzi/use/parser/uncertainty").toURI());
        } catch (NullPointerException | URISyntaxException e) {
            TEST_PATH = null;
            fail("Folders including tests are missing!");
        }
    }

    private class ExpressionTest {
        String expression;
        String expected;
    }

    private class StringOutputStream extends OutputStream {
        private StringBuilder fBuffer;

        public StringOutputStream() {
            fBuffer = new StringBuilder();
        }

        @Override
        public void write(int b) throws IOException {
            fBuffer.append((char) b);
        }

        public void reset() {
            fBuffer = new StringBuilder();
        }

        @Override
        public String toString() {
            return fBuffer.toString();
        }
    }

    public void testUncertaintyExpression() {

        MModel model = new ModelFactory().createModel("Test");
        File dir = TEST_PATH;
        File[] files = dir.listFiles(new SuffixFileFilter(".in"));
        StringOutputStream sos = new StringOutputStream();
        PrintWriter pw = new PrintWriter(sos);

        Options.explicitVariableDeclarations = false;

        assertNotNull(files);

        System.out.println("-----------------------------------------------------------------");
        System.out.println("It's going to be executed " + files.length + " test files.");

        for (File testFile : files) {
            String testFileName = testFile.getName();
            System.out.println("-----------------------------------------------------------------");
            System.out.println("File : " + testFileName);

            try (BufferedReader in = new BufferedReader(new FileReader(testFile))) {

                ExpressionTest expTest = readExpressionLine(in);
                while (expTest != null) {

                    if (VERBOSE) {
                        System.out.println("\tExpression : \n" +
                                "\t\t" + expTest.expression + "\n" +
                                "\t\t-> " + expTest.expected);
                        System.out.println();
                    }

                    Value result = executeExpression(model, pw, expTest);

                    if (result == null) {
                        String errMessage = "";
                        String[] lines = sos.toString().split("\\r?\\n");
                        for (int i = lines.length - 1; i >= 0; i--) {
                            if (!lines[i].trim().isEmpty()) {
                                errMessage = lines[i].trim();
                                break;
                            }
                        }
                        assertEquals("evaluate : " + expTest.expression, expTest.expected, errMessage);
                        sos.reset();
                    }
                    else
                        assertEquals("evaluate : " + expTest.expression, expTest.expected, result.toStringWithType());

                    expTest = readExpressionLine(in);
                }
            }
            catch (IOException ex) {
                throw new RuntimeException("Couldn't open file " + testFileName);
            }

        }

    }

    private ExpressionTest readExpressionLine(BufferedReader in) throws IOException {
        ExpressionTest expTest = new ExpressionTest();
        String line;
        StringBuilder expressionBuilder = new StringBuilder();

        line = in.readLine();
        while (line != null && (expTest.expression == null || expTest.expected == null)) {
            line = line.trim();

            if (line.length() != 0 && !line.startsWith("#") ) {

                if (expTest.expression == null) {

                    if (line.startsWith("->"))
                        throw new RuntimeException("missing expression");

                    if (!line.endsWith("\\")) {
                        expressionBuilder.append(line);
                        expTest.expression = expressionBuilder.toString().replace("\t", " ");
                    }
                    else
                        expressionBuilder.append(line.substring(0, line.length()-2) + "\n");

                } else {

                    if (!line.startsWith("->"))
                        throw new RuntimeException("missing expected result line");

                    expTest.expected = line.substring(3);
                }

            }

            if (expTest.expected == null)
                line = in.readLine();
        }

        if (expTest.expected == null || expTest.expression == null )
            expTest = null; // End of file

        return expTest;
    }

    private Value executeExpression(MModel model, PrintWriter pwErr, ExpressionTest expressionTest) {
        InputStream stream = new ByteArrayInputStream(expressionTest.expression.getBytes());
        Value result = null;
        Expression expr =
                OCLCompiler.compileExpression(
                        model,
                        stream,
                        TEST_PATH.getAbsolutePath(),
                        pwErr,
                        new VarBindings()
                );

        if (expr != null) {
            MSystemState systemState = new MSystem(model).state();
            result = new Evaluator().eval(expr, systemState);
        }

        return result;
    }
}
