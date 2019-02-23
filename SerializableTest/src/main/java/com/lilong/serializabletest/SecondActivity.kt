package com.lilong.serializabletest

import android.app.Activity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.onClick
import java.io.*

/**
 * 这个activity被配置在另外的进程里，但也可正常使用Serialize/DeSerialize序列化功能
 * 在一个进程中Serialize，也可在另外的进程中DeSerialize
 * (1) 同一个应用中设置的不同进程，有相同uid，所以对应用私有数据有相同的读写权限
 * (2) 可以跨进程传递数据，但这种能力跟Serializable接口无关，本质上还是相同uid的进程有相同权限所致
 * */
class SecondActivity : Activity() {

    val objFile: File = File(MainApplication.Companion.getInstance()?.cacheDir?.absolutePath + File.separator + "haha")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        setTitle("SecondActivity")
        var student: Student = Student("xiaoming", 12)
        tvBeforeSerialize.text = student.toString()
        btnSerialize.onClick {
            var objectOutputStream: ObjectOutputStream = ObjectOutputStream(FileOutputStream(objFile))
            objectOutputStream.writeObject(student)
            objectOutputStream.close()
        }

        /**
         * Serializable接口的实例，可以实现序列化和反序列化，通过ObjectInputStream的readObject和ObjectOutputStream的writeObject方法
         * 可以将
         * */
        btnDeSerialize.onClick {
            var objectInputStream: ObjectInputStream = ObjectInputStream(FileInputStream(objFile))
            var student: Student = objectInputStream.readObject() as Student
            objectInputStream.close()
            tvAfterDeSerialize.text = student.toString()
        }
    }
}
