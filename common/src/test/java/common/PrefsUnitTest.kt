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
