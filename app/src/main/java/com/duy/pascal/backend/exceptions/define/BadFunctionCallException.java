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

package com.duy.pascal.backend.exceptions.define;


import com.duy.pascal.backend.linenumber.LineInfo;

public class BadFunctionCallException extends com.duy.pascal.backend.exceptions.ParsingException {
    public String functionName;
    public boolean functionExists;
    public boolean numargsMatch;

    public BadFunctionCallException(LineInfo line, String functionName,
                                    boolean functionExists, boolean numargsMatch) {
        super(line);
        this.functionName = functionName;
        this.functionExists = functionExists;
        this.numargsMatch = numargsMatch;
    }

    @Override
    public String getMessage() {
        if (functionExists) {
            if (numargsMatch) {
                return ("One or more arguments has an incorrect operator when calling function \"" + functionName + "\".");
            } else {
                return ("Either too few or two many arguments are being passed to function \"" + functionName + "\".");
            }
        } else {
            return ("Can not call function or procedure \"" + functionName + "\", which is not defined.");
        }
    }

}