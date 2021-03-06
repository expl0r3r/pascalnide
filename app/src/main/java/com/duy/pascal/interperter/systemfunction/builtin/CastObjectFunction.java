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

package com.duy.pascal.interperter.systemfunction.builtin;


import android.support.annotation.NonNull;

import com.duy.pascal.interperter.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.interperter.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.ast.instructions.Executable;
import com.duy.pascal.interperter.ast.runtime_value.references.PascalReference;
import com.duy.pascal.interperter.ast.runtime_value.value.FunctionCall;
import com.duy.pascal.interperter.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.interperter.ast.variablecontext.VariableContext;
import com.duy.pascal.interperter.declaration.Name;
import com.duy.pascal.interperter.declaration.lang.types.ArgumentType;
import com.duy.pascal.interperter.declaration.lang.types.JavaClassBasedType;
import com.duy.pascal.interperter.declaration.lang.types.PointerType;
import com.duy.pascal.interperter.declaration.lang.types.RuntimeType;
import com.duy.pascal.interperter.declaration.lang.types.Type;
import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;
import com.duy.pascal.interperter.linenumber.LineInfo;

/**
 * Casts an object to the class or the interface represented
 */
public class CastObjectFunction implements IMethodDeclaration {

    private static final ArgumentType[] ARGUMENT_TYPES =
            {new RuntimeType(new JavaClassBasedType(Object.class), true), //target
                    new RuntimeType(new JavaClassBasedType(Object.class), false)}; //other

    @Override
    public Name getName() {
        return Name.create("Cast");

    }

    @Override
    public FunctionCall generateCall(LineInfo line, RuntimeValue[] arguments,
                                     ExpressionContext f) throws Exception {
        RuntimeValue pointer = arguments[0];
        RuntimeValue value = arguments[1];
        PointerType declType = (PointerType) pointer.getRuntimeType(f).declType;
        Class<?> storageClass = declType.pointedToType.getStorageClass();
        return new InstanceObjectCall(pointer, value, storageClass, line);
    }

    @Override
    public FunctionCall generatePerfectFitCall(LineInfo line, RuntimeValue[] values, ExpressionContext f) throws Exception {
        return generateCall(line, values, f);
    }

    @Override
    public ArgumentType[] argumentTypes() {
        return ARGUMENT_TYPES;
    }

    @Override
    public Type returnType() {
        return null;
    }

    @Override
    public String description() {
        return null;
    }

    private class InstanceObjectCall extends BuiltinFunctionCall {
        private RuntimeValue value;
        private Class<?> storageClass;
        private LineInfo line;
        private RuntimeValue pointer;

        InstanceObjectCall(RuntimeValue pointer, RuntimeValue value, Class<?> storageClass, LineInfo line) {
            this.value = value;
            this.pointer = pointer;
            this.storageClass = storageClass;
            this.line = line;
        }

        @Override
        public RuntimeType getRuntimeType(ExpressionContext f) throws Exception {
            return null;
        }

        @Override
        public Object compileTimeValue(CompileTimeContext context) {
            return null;
        }

        @Override
        public RuntimeValue compileTimeExpressionFold(CompileTimeContext context)
                throws Exception {
            return new InstanceObjectCall(pointer, value, storageClass, line);
        }

        @Override
        public Executable compileTimeConstantTransform(CompileTimeContext c)
                throws Exception {
            return new InstanceObjectCall(pointer, value, storageClass, line);
        }

        @Override
        protected String getFunctionNameImpl() {
            return "Cast";
        }

        @Override
        @SuppressWarnings("unchecked")
        public Object getValueImpl(@NonNull VariableContext f, @NonNull RuntimeExecutableCodeUnit<?> main)
                throws RuntimePascalException {
            //get reference of variable
            PascalReference pointer = (PascalReference) this.pointer.getValue(f, main);

            //get value of arg 2
            Object value = this.value.getValue(f, main);

            //cast object to type of variable
            Object casted = storageClass.cast(value);

            //set value
            pointer.set(casted);
            return null;
        }

    }
}
