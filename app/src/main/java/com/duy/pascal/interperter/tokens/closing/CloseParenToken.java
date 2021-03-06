package com.duy.pascal.interperter.tokens.closing;

import com.duy.pascal.interperter.exceptions.parsing.grouping.GroupingException;
import com.duy.pascal.interperter.exceptions.parsing.grouping.GroupingException.Type;
import com.duy.pascal.interperter.linenumber.LineInfo;
import com.duy.pascal.interperter.tokens.grouping.GrouperToken;
import com.duy.pascal.interperter.tokens.grouping.ParenthesizedToken;

public class CloseParenToken extends ClosingToken {

    public CloseParenToken(LineInfo line) {
        super(line);
    }

    @Override
    public GroupingException getClosingException(GrouperToken t) {
        return t instanceof ParenthesizedToken //(
                ? null
                : new GroupingException(getLineNumber(), Type.MISMATCHED_PARENTHESES);
    }

    public String toCode() {
        return ")";
    }

    @Override
    public String toString() {
        return ")";
    }
}
