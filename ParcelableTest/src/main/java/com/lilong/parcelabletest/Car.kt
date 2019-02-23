package com.lilong.parcelabletest

import android.os.Parcel
import android.os.Parcelable

data class Car(var model: String, var price: Int) : Parcelable {

    override fun toString(): String {
        return "Car model = " + model + ", price = " + price
    }

    override fun describeContents(): Int {
        return 0
    }

    /**
     * parcelable接口要求必须实现的writeToParcel方法，这是用来指定如何将对象的数据写入parcel的
     * */
    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(model)
        dest?.writeInt(price)
    }

    /**
     * parcelable接口要求必须实现的CREATOR属性，这是用来指定如何从parcel读取数据，重建对象的
     * */
    companion object CREATOR : Parcelable.Creator<Car> {
        override fun createFromParcel(parcel: Parcel): Car {
            var model: String = parcel.readString()
            var price: Int = parcel.readInt()
            return Car(model, price)
        }

        override fun newArray(size: Int): Array<Car?> {
            return arrayOfNulls(size)
        }
    }

}