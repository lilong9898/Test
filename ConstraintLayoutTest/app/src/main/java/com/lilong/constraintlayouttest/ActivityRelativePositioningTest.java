package com.lilong.constraintlayouttest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

/** constraint layout中的relative positioning*/
public class ActivityRelativePositioningTest extends Activity {

    private MenuItem menuItemCircularPositioningTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relative_positioning_test);
        setTitle("RelativePositioning");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cur_menu, menu);
        menuItemCircularPositioningTest = menu.findItem(R.id.circularPositioningTest);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item == menuItemCircularPositioningTest){
            Intent intent = new Intent(this, ActivityCircularPositioningTest.class);
            startActivity(intent);
        }
        return true;
    }
}
