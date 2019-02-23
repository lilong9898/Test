package com.lilong.parcelabletest

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcel
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.onClick

/**
 * parcel[不应该]被转换成任何存储在磁盘上的数据，也[不应该]通过网被传输
 * 因为其实际的字节是根据当前机器做过优化的，不适用于别的机器
 *
 * 需要存储在磁盘上或者被网络传输的，要用Serializable
 * */
class MainActivity : Activity() {

    var menuItemJumpToActivityInAnotherProcess: MenuItem? = null;

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option, menu)
        menuItemJumpToActivityInAnotherProcess = menu?.findItem(R.id.jumpToActivityInAnotherProcess)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item == menuItemJumpToActivityInAnotherProcess) {
            var intent: Intent = Intent(this, SecondActivity::class.java)
            var car:Car = Car("BMW", 30)
            intent.putExtra("car", car)
            startActivity(intent)
        }
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setTitle("MainActivity")

        var parcelledCar: Car = Car("audi", 39)
        tvBeforeParcel.text = parcelledCar.toString()

        var parcel: Parcel = Parcel.obtain()
        // 注意，读写parcel之前都要setDataPosition(0)以便将游标调整到最开始
        parcel.setDataPosition(0)

        btnParcel.onClick {
            parcelledCar.writeToParcel(parcel, 0)
        }

        btnDeParcel.onClick {
            parcel.setDataPosition(0)
            // 注意，读写parcel之前都要setDataPosition(0)以便将游标调整到最开始
            var unparcelledCar: Car = Car.CREATOR.createFromParcel(parcel)
            tvAfterDeParcel.text = unparcelledCar.toString()
        }
    }
}
