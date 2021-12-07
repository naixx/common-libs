/*
 * Copyright 2021 Rostislav Chekan
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 */

package common.extras

import androidx.fragment.app.Fragment
import java.io.Serializable

class SS : Serializable

class Arg : Fragment() {
    val s by boolean()
    val l by long()
    val i by int()
    val serializableData by serializable<SerializableData>()

    val ia by intArray()

    init {
        arguments {
            it::l += 41
            it::i += 41
            it::serializableData *= SerializableData2()
            it::ia += intArrayOf()
        }
    }
}

class FragmentArgTest {
}