/*
 *  Copyright (c) 2017 Tran Le Duy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.duy.pascal.interperter.systemfunction.io;


import android.support.annotation.NonNull;

import com.duy.pascal.interperter.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.interperter.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.declaration.Name;
import com.duy.pascal.interperter.systemfunction.builtin.IMethodDeclaration;
import com.duy.pascal.interperter.ast.instructions.Executable;
import com.duy.pascal.interperter.ast.variablecontext.VariableContext;
import com.duy.pascal.interperter.ast.runtime_value.references.PascalReference;
import com.duy.pascal.interperter.ast.runtime_value.value.FunctionCall;
import com.duy.pascal.interperter.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.interperter.builtin_libraries.file.FileLib;
import com.duy.pascal.interperter.linenumber.LineInfo;
import com.duy.pascal.interperter.exceptions.parsing.ParsingException;
import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;
import com.duy.pascal.interperter.declaration.lang.types.ArgumentType;
import com.duy.pascal.interperter.declaration.lang.types.BasicType;
import com.duy.pascal.interperter.declaration.lang.types.Type;
import com.duy.pascal.interperter.declaration.lang.types.RuntimeType;
import com.duy.pascal.interperter.declaration.lang.types.VarargsType;
import com.duy.pascal.frontend.debug.CallStack;

import java.io.File;

/**
 * Casts an object to the class or the interface represented
 */
public class ReadlnFileFunction implements IMethodDeclaration {

    private ArgumentType[] argumentTypes =
            {new RuntimeType(BasicType.Text, true),
                    new VarargsType(new RuntimeType(BasicType.create(Object.class), true))};

    @Override
    public Name getName() {
        return Name.create("ReadLn");
    }

    @Override
    public FunctionCall generateCall(LineInfo line, RuntimeValue[] arguments,
                                     ExpressionContext f) throws Exception {
        return new ReadLineFileCall(arguments[0], arguments[1], line);
    }

    @Override
    public FunctionCall generatePerfectFitCall(LineInfo line, RuntimeValue[] values, ExpressionContext f) throws Exception {
        return generateCall(line, values, f);
    }

    @Override
    public ArgumentType[] argumentTypes() {
        return argumentTypes;
    }

    @Override
    public Type returnType() {
        return null;
    }

    @Override
    public String description() {
        return null;
    }

    private class ReadLineFileCall extends FunctionCall {
        private RuntimeValue args;
        private LineInfo line;
        private RuntimeValue filePreference;

        ReadLineFileCall(RuntimeValue filePreferences, RuntimeValue args, LineInfo line) {
            this.filePreference = filePreferences;
            this.args = args;
            this.line = line;
        }

        @Override
        public RuntimeType getRuntimeType(ExpressionContext f) throws Exception {
            return null;
        }

        @NonNull
        @Override
        public LineInfo getLineNumber() {
            return line;
        }

        @Override
        public void setLineNumber(LineInfo lineNumber) {

        }

        @Override
        public Object compileTimeValue(CompileTimeContext context) {
            return null;
        }

        @Override
        public RuntimeValue compileTimeExpressionFold(CompileTimeContext context)
                throws Exception {
            return new ReadLineFileCall(filePreference, args, line);
        }

        @Override
        public Executable compileTimeConstantTransform(CompileTimeContext c)
                throws Exception {
            return new ReadLineFileCall(filePreference, args, line);
        }

        @Override
        protected Name getFunctionName() {
            return Name.create("ReadLn");

        }

        @Override
        @SuppressWarnings("unchecked")
        public Object getValueImpl(@NonNull VariableContext f, @NonNull RuntimeExecutableCodeUnit<?> main)
                throws RuntimePascalException {
            FileLib fileLib = main.getDeclaration().getContext().getFileHandler();

            PascalReference[] values = (PascalReference[]) args.getValue(f, main);
            PascalReference<File> file = (PascalReference<File>) filePreference.getValue(f, main);
            fileLib.readlnz(file.get(), values);
            if (main.isDebug()) main.getDebugListener().onVariableChange(new CallStack(f));

            return null;
        }

    }
}
