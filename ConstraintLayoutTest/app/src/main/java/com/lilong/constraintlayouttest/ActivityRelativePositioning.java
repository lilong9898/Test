package com.lilong.constraintlayouttest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

/**
 * constraint layout中的relative positioning
 */
public class ActivityRelativePositioning extends Activity {

    private MenuItem menuItemRelativeToGoneWidgetPositioning;
    private MenuItem menuItemCircularPositioning;
    private MenuItem menuItemDimensionConstraint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relative_positioning);
        setTitle("RelativePositioning");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cur_menu, menu);
        menuItemRelativeToGoneWidgetPositioning = menu.findItem(R.id.relativeToGoneWidgetPositioning);
        menuItemCircularPositioning = menu.findItem(R.id.circularPositioning);
        menuItemDimensionConstraint = menu.findItem(R.id.dimensionConstraint);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item == menuItemRelativeToGoneWidgetPositioning) {
            Intent intent = new Intent(this, ActivityRelativeToGoneWidgetPositioning.class);
            startActivity(intent);
        } else if (item == menuItemCircularPositioning) {
            Intent intent = new Intent(this, ActivityCircularPositioning.class);
            startActivity(intent);
        } else if (item == menuItemDimensionConstraint) {
            Intent intent = new Intent(this, ActivityDimensionConstraint.class);
            startActivity(intent);
        }
        return true;
    }
}
