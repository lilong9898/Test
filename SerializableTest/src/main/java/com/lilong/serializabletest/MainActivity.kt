package com.lilong.serializabletest

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.onClick
import java.io.*

/**
 * serializable和parcelable的区别：
 *
 * serializable:
 * (1) java库中的功能
 * (2) 使用简单，代码少，只要让类实现Serializable接口即可，几乎全自动
 * (3) 序列化过程中通过反射来解析，速度比parcelable慢几乎十倍
 * (4) 占用内存较多，会生成大量临时对象，内存抖动厉害
 * (5) 可以通过ObjectOutputStream将序列化的数据写入磁盘/通过网络传输
 *
 * parcelable:
 * (1) android独有的功能
 * (2) 使用复杂，代码多，需要指定对象与Parcel之间互相转换的过程（需要实现writeToParcel和CREATOR中的createFromParcel)，几乎全手动
 * (3) 序列化过程会依照(2)中用户指定的过程来进行，速度很快，效率高
 * (4) 占内存少
 * (5) 虽然技术上可以，但不应该将parcel的数据（通过parcel.marshall方法可获得其字节数据）写入磁盘或通过网络传输，因为parcel是根据机器和环境深度优化过的，没有通用性，换机器后很可能无法读取
 * */
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
