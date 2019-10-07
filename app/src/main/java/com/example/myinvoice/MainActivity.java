package com.example.myinvoice;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private SectionsStatepageAdapter sectionAdapter;
    private ViewPager viewpager;
    public static ArrayList<Invoice> invoices;
    public static int index=0;
    SQLiteHandler db;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        db=new SQLiteHandler(getApplicationContext());
        sectionAdapter=new SectionsStatepageAdapter(getSupportFragmentManager());
        viewpager=(ViewPager) findViewById(R.id.container);
        setupViewPager(viewpager);
    }
    private void setupViewPager(ViewPager pager){
        SectionsStatepageAdapter adapter=new SectionsStatepageAdapter(getSupportFragmentManager());
        adapter.addFragment(new Main());
        adapter.addFragment(new DataEntry());
        adapter.addFragment(new Help());
        adapter.addFragment(new IndividualInvoice());
        pager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menuHome) {
            this.setViewPager(0);
            return true;
        } else if (id == R.id.menuNew) {
            this.setViewPager(1);
            return true;
        }else if (id == R.id.menuHelp) {
            this.setViewPager(2);
            return true;
        }else if (id == R.id.menuQuit) {
            new AlertDialog.Builder(this)
                    .setTitle("Are you sure? This will exit the app")
                    .setMessage("Quit App")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            finish();
                        }})
                    .setNegativeButton(android.R.string.no, null).show();
            return true;
        }



        return super.onOptionsItemSelected(item);
    }
    public void setViewPager(int fragment){
        viewpager.setCurrentItem(fragment);
    }
}
