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

package com.duy.pascal.interperter.exceptions.parsing.index

import com.duy.pascal.interperter.linenumber.LineInfo
import com.duy.pascal.interperter.exceptions.parsing.ParsingException
import com.duy.pascal.interperter.declaration.lang.types.Type

class NonArrayIndexed(line: LineInfo, var t: Type) : ParsingException(line) {

    override val message: String?
        get() = "Tried to do indexed access on something which wasn't an array or a string. It was a " + t.toString()

}
