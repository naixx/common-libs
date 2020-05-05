import AnalyticsTest.Bar.Companion.firebase
import common.analytics.AnalyticsCall
import common.analytics.Param
import common.analytics.createAdapter
import common.analytics.createAggregate
import org.junit.Test

class AnalyticsTest {

    data class Image(val name: String)

    interface BaseInterface {

        fun simpleCall()
        fun base(@Param("the para") param: String)

        fun baseWithParam(@Param("history") hist: Int)
        fun simpleCallOverriden()

        fun defaultInterface() {
            println("Analytics default interface")
        }

        fun defaultInterfaceWithPara(@Param("the para") param: String) {
            println("defaultInterfaceWithPara() $param")
        }

        fun imageShown(@Param("img") image: Image)

        fun imageStatic(@Param("img") image: Image) {
            println(image)
        }

        fun init()
    }

    interface Foo : BaseInterface {

        override fun defaultInterface() {
            println("defaultInterface() overriden")
        }

        override fun simpleCallOverriden() {
            println("simpleCallOverriden() in derived")
        }

        override fun base(param: String) {
            println("Foo base( param: String)")
        }
    }

    class TestInnterLateinit {
        fun test() { println("from test")}
    }

    interface Bar : BaseInterface {

        companion object : Bar by createAdapter({ name: String, parameterNames: List<String>, args: List<Any> ->
            println("AnalyticsCall stub $name")
            firebase.test()
        }) {
            private lateinit var firebase: TestInnterLateinit
        }

        override fun init() {
            println("Bar init")
            firebase = TestInnterLateinit()
        }

        override fun simpleCallOverriden() {
            println("simpleCallOverriden() in bar")
        }
    }

    @Test
    fun analytics() {

        val derived = createAdapter(Foo::class.java, object : AnalyticsCall {
            override fun invoke(name: String, parameterNames: List<String>, args: List<Any>) {
                println("$name ($parameterNames $args)")
            }
        })

        derived.defaultInterface()
        derived.defaultInterfaceWithPara("para")
        derived.simpleCall()
        derived.simpleCallOverriden()
        derived.baseWithParam(4)
    }

    @Test
    fun analyticsAggregate() {
        val f = createAdapter(Foo::class.java, object : AnalyticsCall {
            override fun invoke(name: String, parameterNames: List<String>, args: List<Any>) {
                println("$name ($parameterNames $args)")
            }
        })
        val b = createAdapter<Bar> { name: String, parameterNames: List<String>, args: List<Any> ->
            println("bar $name ($parameterNames $args)")
        }
        val a = createAggregate(f, b)

        a.defaultInterface()
        a.defaultInterfaceWithPara("para")
        a.simpleCall()
        a.simpleCallOverriden()
        a.base("derived")
        a.baseWithParam(4)
    }

    @Test
    fun analyticsTest() {
        val f = createAdapter(Foo::class.java, object : AnalyticsCall {
            override fun invoke(name: String, parameterNames: List<String>, args: List<Any>) {
                println("$name ($parameterNames $args)")
            }
        })
        val a = createAggregate(f, converter = {
            when (it) {
                is Image -> it.name
                else     -> it
            }
        })

        a.imageShown(Image("hello"))
        a.imageStatic(Image("hello static"))
    }

    @Test
    fun `test inner lateinit property or companion`() {
        val f = createAdapter(Foo::class.java, object : AnalyticsCall {
            override fun invoke(name: String, parameterNames: List<String>, args: List<Any>) {
                println("$name ($parameterNames $args)")
            }
        })
        val a = createAggregate<BaseInterface>(f, Bar, converter = {
            when (it) {
                is Image -> it.name
                else     -> it
            }
        })
        a.init()
        a.imageShown(Image("hello"))
        a.imageStatic(Image("hello static"))
    }
}
