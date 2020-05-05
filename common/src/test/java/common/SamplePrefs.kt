package common

enum class TestEnum {
    ONE,
    TWO
}

class SamplePrefs : PrefsDsl() {
    var field by enum(TestEnum.ONE)
}
