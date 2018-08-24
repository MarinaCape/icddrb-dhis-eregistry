package org.icddrb.dhis.android.sdk.utils.support.math;

import java.util.Stack;
import org.nfunk.jep.ParseException;
import org.nfunk.jep.function.PostfixMathCommand;

public abstract class UnaryDoubleFunction extends PostfixMathCommand {
    public abstract Double eval(double d);

    public UnaryDoubleFunction() {
        this.numberOfParameters = 1;
    }

    public void run(Stack inStack) throws ParseException {
        checkStack(inStack);
        Object param = inStack.pop();
        if (param == null || !(param instanceof Double)) {
            throw new ParseException("Invalid parameter type, must be double: " + param);
        }
        inStack.push(eval(((Double) param).doubleValue()));
    }
}
