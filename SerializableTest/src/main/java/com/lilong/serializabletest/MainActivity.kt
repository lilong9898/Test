package com.lilong.serializabletest

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.onClick
import java.io.*

class MainActivity : Activity() {

    val objFile: File = File(MainApplication.Companion.getInstance()?.cacheDir?.absolutePath + File.separator + "haha")
    var menuItemJumpToActivityInAnotherProcess: MenuItem? = null;

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option, menu)
        menuItemJumpToActivityInAnotherProcess = menu?.findItem(R.id.jumpToActivityInAnotherProcess)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item == menuItemJumpToActivityInAnotherProcess) {
            var intent: Intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
        }
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setTitle("MainActivity")
        var student: Student = Student("xiaoming", 12)
        tvBeforeSerialize.text = student.toString()
        btnSerialize.onClick {
            var objectOutputStream: ObjectOutputStream = ObjectOutputStream(FileOutputStream(objFile))
            objectOutputStream.writeObject(student)
            objectOutputStream.close()
        }

        /**
         * Serializable接口的实例，可以实现序列化和反序列化，通过ObjectInputStream的readObject和ObjectOutputStream的writeObject方法
         * 可以将对象的数据（属性的值）都存储和还原
         * */
        btnDeSerialize.onClick {
            try {
                var objectInputStream: ObjectInputStream = ObjectInputStream(FileInputStream(objFile))
                var student: Student = objectInputStream.readObject() as Student
                objectInputStream.close()
                tvAfterDeSerialize.text = student.toString()
            } catch (e: Exception) {
            }
        }
    }
}
