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
