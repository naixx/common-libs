package test.java.common

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import common.SamplePrefs
import common.TestEnum
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class PrefsUnitTest {

    lateinit var prefs: SamplePrefs

    @Before
    fun before() {
        prefs = SamplePrefs().apply { init(ApplicationProvider.getApplicationContext()) }
    }

    @Test
    fun `default value`() {
        assertEquals(prefs.field, TestEnum.ONE)
        prefs.field = TestEnum.TWO
        assertNotEquals(prefs.field, TestEnum.ONE)
    }
}
