package com.lilong.archlifecycletest

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.FragmentActivity

class MainActivity : FragmentActivity() {

    private var menuItemJumpToSecondActivity: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = "FirstActivity"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        menuItemJumpToSecondActivity = menu?.findItem(R.id.menuItemSecondActivity)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item == menuItemJumpToSecondActivity) {
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
        }
        return true
    }

    companion object {
        const val TAG = "LTest"
    }
}
