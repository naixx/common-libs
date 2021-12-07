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
import android.os.Parcel
import android.os.Parcelable
import androidx.fragment.app.Fragment
import java.io.Serializable

data class SerializableData(val s: Int = 2) : Serializable
data class SerializableData2(val s: Int = 2) : Serializable

data class ParcelableData(val i: Int) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.readInt())

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(i)
    }

    companion object CREATOR : Parcelable.Creator<ParcelableData> {
        override fun createFromParcel(parcel: Parcel): ParcelableData {
            return ParcelableData(parcel)
        }

        override fun newArray(size: Int): Array<ParcelableData?> {
            return arrayOfNulls(size)
        }
    }
}

data class ParcelableData2(val i: Int) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.readInt())

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(i)
    }

    companion object CREATOR : Parcelable.Creator<ParcelableData> {
        override fun createFromParcel(parcel: Parcel): ParcelableData {
            return ParcelableData(parcel)
        }

        override fun newArray(size: Int): Array<ParcelableData?> {
            return arrayOfNulls(size)
        }
    }
}

class TestFragment : Fragment() {

    init {
        extrasUnsafe<TestActivity>().start()
        extrasUnsafe<TestActivity>().intent()
        extrasUnsafe<TestActivity>().startForResult(2)
    }
}

class TestActivity : Activity() {
    val s by extraBoolean()
    val long by extraLong()
    val int by extraInt()
    val parcelable by extraParcelable<ParcelableData>()
    val parcelable2 by extraParcelable<ParcelableData2?>()
    val serialaziable: SerializableData by extraSerializable()
    val serialaziable2: SerializableData2 by extraSerializable()
    val intArray by extraIntArray()

    init {
        extrasUnsafe<TestActivity> { }.intent()
    }

    companion object {
        fun newInstance(
            int: Int = 0,
            long: Long = 0,
            p: Parcelable? = null,
            ser: SerializableData? = null,
            intArra: IntArray = intArrayOf()
        ): ExtraStarter {

            return extras<TestActivity, SerializableData?, Parcelable?> {
                TestActivity::long += long
                TestActivity::int += int

                TestActivity::parcelable += p

                TestActivity::intArray += intArra
                TestActivity::serialaziable += ser

            }
        }
    }
}

