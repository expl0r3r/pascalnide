/*
 *  Copyright 2017 Tran Le Duy
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

package com.duy.pascal.interperter.builtin_libraries.runtime_exceptions;

import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;

/**
 * Can occur if you try to calculate the square root or logarithm of a negative number.
 * <p>
 * Created by Duy on 08-Apr-17.
 */

public class InvalidFloatingPointOperation extends RuntimePascalException {
    private Object object;

    public InvalidFloatingPointOperation(Object d) {
        this.object = d;
    }
}
