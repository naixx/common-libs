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

import android.app.Activity
import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import common.extras.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ActivityExtraTest {

    @Test
    fun `simple types`() {
        // GIVEN
        val i = TestActivity.newInstance(
            int = 42,
            long = 64,
            intArra = intArrayOf(42, 64)
        ).intent(ApplicationProvider.getApplicationContext())
        with(ApplicationProvider.getApplicationContext<Context>()) {
            val i: ExtraStarter = TestActivity.newInstance(
                int = 42,
                long = 64,
                intArra = intArrayOf(42, 64)
            )

            val extrasUnsafe: ExtraStarter = extrasUnsafe<TestActivity> { }
            extrasUnsafe.intent()
        }
        val scenario = launchActivity<TestActivity>(i)

        // WHEN
        scenario.moveToState(Lifecycle.State.CREATED)

        // THEN
        scenario.onActivity { activity ->
            assertEquals(42, activity.int)
            assertEquals(64, activity.long)
            assertTrue(intArrayOf(42, 64) contentEquals activity.intArray)
        }
    }

    @Test
    fun `Serializable types`() {
        // GIVEN
        val i = TestActivity.newInstance(
            ser = SerializableData()
        ).intent(ApplicationProvider.getApplicationContext())
        val scenario = launchActivity<TestActivity>(i)

        // WHEN
        scenario.moveToState(Lifecycle.State.CREATED)

        // THEN
        scenario.onActivity { activity ->
            assertEquals(SerializableData(), activity.serialaziable)
        }
    }

    @Test
    fun `Compile time checks`() {

        extras<TestActivity, SerializableData> {
//            TestActivity::serialaziable *= SerializableData2()

        }
        extras<TestActivity, SerializableData> {
            TestActivity::serialaziable += (SerializableData())
            TestActivity::serialaziable += (SerializableData())
        }
        doTest<TestActivity>(extras<TestActivity, SerializableData, SerializableData2> {
            TestActivity::serialaziable += (SerializableData())
//          TestActivity::serialaziable *= (SerializableData2())
//          TestActivity::serialaziable2 *= (SerializableData())
            TestActivity::serialaziable2 += (SerializableData2())
        }) {
            assertEquals(SerializableData(), it.serialaziable)
            assertEquals(SerializableData2(), it.serialaziable2)
        }
    }

    @Test
    fun `Parcelable types`() {
        extrasUnsafe<TestActivity> {
            TestActivity::long += 3
            TestActivity::serialaziable += ParcelableData(12)
            //TestActivity::parcelable *= SerializableData()
        }
        doTest<TestActivity>(extras<TestActivity, SerializableData, ParcelableData> {
            TestActivity::serialaziable += SerializableData()
            TestActivity::long += 3

           // TestActivity::parcelable += ParcelableData(2)
        }) {
            assertEquals(ParcelableData(2), it.parcelable)
            assertEquals(ParcelableData2(2), it.parcelable2)
        }
        doTest<TestActivity>(extras<TestActivity, SerializableData, ParcelableData> {
//            TestActivity::serialaziable *= SerializableData()
//            TestActivity::serialaziable.timesAssign()

        }) {
            assertEquals(ParcelableData(2), it.parcelable)
            assertEquals(ParcelableData2(2), it.parcelable2)
        }
    }

    private inline fun <reified A : Activity> doTest(starter: ExtraStarter, testBlock: ActivityScenario.ActivityAction<A>) {
        val scenario = launchActivity<A>(starter.intent(ApplicationProvider.getApplicationContext()))
        // WHEN
        scenario.moveToState(Lifecycle.State.CREATED)
        // THEN
        scenario.onActivity(testBlock)
    }
}
/*
        val activity = launchExtraActivity {
            putExtra(TestActivity::i.name, 1)
        }
    }
/*
    @Test
    fun getMissing() {
        val activity = launchExtraActivity()
        assertThrows<IllegalArgumentException> { activity.stringExtra }
        assertThrows<IllegalArgumentException> { activity.intExtra }
        assertNull(activity.nullableIntExtra)
        assertEquals(TextExtraActivity.DEFAULT_INT, activity.intDefaultExtra)
        assertEquals(TextExtraActivity.DEFAULT_NULLABLE_INT, activity.nullableIntDefaultExtra)
    }

    @Test
    fun getNull() {
        val activity = launchExtraActivity {
            putExtra(TextExtraActivity.EXTRA_STRING, null as String?)
            putExtra(TextExtraActivity.EXTRA_INT, null as Int?)
            putExtra(TextExtraActivity.EXTRA_NULLABLE_INT, null as Int?)
            putExtra(TextExtraActivity.EXTRA_INT_DEFAULT, null as Int?)
            putExtra(TextExtraActivity.EXTRA_NULLABLE_INT_DEFAULT, null as Int?)
        }
        assertThrows<IllegalArgumentException> { activity.stringExtra }
        assertThrows<IllegalArgumentException> { activity.intExtra }
        assertNull(activity.nullableIntExtra)
        assertEquals(TextExtraActivity.DEFAULT_INT, activity.intDefaultExtra)
        assertEquals(TextExtraActivity.DEFAULT_NULLABLE_INT, activity.nullableIntDefaultExtra)
    }

    @Test
    fun getWrongType() {
        val activity = launchExtraActivity {
            putExtra(TextExtraActivity.EXTRA_STRING, Bundle())
            putExtra(TextExtraActivity.EXTRA_INT, Bundle())
            putExtra(TextExtraActivity.EXTRA_NULLABLE_INT, Bundle())
            putExtra(TextExtraActivity.EXTRA_INT_DEFAULT, Bundle())
            putExtra(TextExtraActivity.EXTRA_NULLABLE_INT_DEFAULT, Bundle())
        }
        assertThrows<IllegalArgumentException> { activity.stringExtra }
        assertThrows<IllegalArgumentException> { activity.intExtra }
        assertThrows<IllegalArgumentException> { activity.nullableIntExtra }
        assertEquals(TextExtraActivity.DEFAULT_INT, activity.intDefaultExtra)
        assertEquals(TextExtraActivity.DEFAULT_NULLABLE_INT, activity.nullableIntDefaultExtra)
    }*/
/*
    private fun launchExtraActivity(configure: Intent.() -> Unit = {}): TestActivity {
        val intent = Intent().apply(configure)
        return rule.launchActivity(intent)
    }*/
}
*/
