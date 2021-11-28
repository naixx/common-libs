
A simple DSL to create and start activities and fragments in a nice way

### Creating Activity starters
For activity you can have

```kotlin
data class ParcelableData(val s: Int) : Parcelable

class TestActivity : Activity() {
    val p: ParcelableData by extraParcelable()
    val l by extraLong()
}
```

To start `TestActivity` you can use two options:

* Simply use
```kotlin


class TestActivity : Activity() {
    ...
    companion object{
        fun newInstance() = 
            extras<TestActivity, ParcelableData>{
                TestActivity::p += ParcelableData(2)
                TestActivity::l += 3
            }
    }
}
```
In that case you can't pass any other object to the property `::p` that is not of type `ParcelableData`



* Or

```kotlin
extrasUnsafe<TestActivity>{
    TestActivity::p += ParcelableData(2)
    TestActivity::l += 3
}.start(context)
```
Here you will not have any type safety during compile time, so you can pass any object to `::p`

### Launching intents in an easy way

Imagine you want to start activity from some `Context`. With these  builders you can start it in the following way:

```kotlin
import common.extras.*

class AnotherActivity : Activity() {
   fun onCreate(){
        TestActivity.newInstance().start()
        TestActivity.newInstance().intent()
        TestActivity.newInstance().startForResult(2)
   }
}

```
You don't have to pass context here as it is captured by property `start`, `intent`, `startForResult`

### Fragments

TODO

```kotlin

class TestFragment : Fragment() {
    val s by boolean()
    val l by long()
    val i by int()
    val serializableData by serializable<SerializableData>()

    val ia by intArray()

    companion object {
        fun newInstance() = TestFragment().arguments {
            it::l += 41
            it::i += 41
            it::serializableData *= SerializableData2()
            it::ia += intArrayOf()
        }
    }
}
```
