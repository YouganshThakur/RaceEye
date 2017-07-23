package com.techrex.raceeye;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class Wizard extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {

    String server_address;
    Context ctx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wizard);
        ctx=this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView titletext=(TextView)findViewById(R.id.title_textview);
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/NeoSansStd-Medium.ttf");
        titletext.setTypeface(custom_font);
        titletext.setText("RACE EYE");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(1).setChecked(true);
        String temp="";
        SharedPreferences prefs = getSharedPreferences("CONNECTION", MODE_PRIVATE);
        temp=prefs.getString("ADDRESS","");
        ((TextView)findViewById(R.id.heading3)).setText("Current Server : "+temp);
        ((EditText)findViewById(R.id.addressEditText)).setText(temp.length()==0?"http://":temp);

    }
    public void continue_button(View view)
    {
        server_address=((EditText)findViewById(R.id.addressEditText)).getText().toString();
        SharedPreferences prefs = getSharedPreferences("CONNECTION", MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor= prefs.edit();
        prefsEditor.putString("ADDRESS",server_address);
        prefsEditor.commit();
        startActivity(new Intent(Wizard.this, Home.class));
        finish();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            startActivity(new Intent(this,Home.class));
        }else if(id==R.id.nav_server)
        {
            //startActivity(new Intent(this,Wizard.class));
        }else if(id==R.id.nav_help)
        {
            new AlertDialog.Builder(ctx)
                    .setTitle("Help")
                    .setMessage("The app required to connect with the HTTP server to fetch the live stream. Go to the server configuration section of the app and enter the address of the server.\n" +
                            "for eg: http://192.168.0.103 or http://192.168.0.103/RaceEye\n" +
                            "\n" +
                            "If you still do not see the stream then login to the control panel of the race eye and check the media streaming server address.\n" +
                            "If the problem still persists see the submitted document.")
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {

                                }
                            }
                    )
                    .show();
        }else if(id==R.id.nav_about)
        {
            new AlertDialog.Builder(ctx)
                    .setTitle("About")
                    .setMessage("Race-Eye allows you to access video feeds from the different cars or any action on the race track of Formula E beyond your view from the grand stand.\n" +
                            "Developed By: \n" +
                            "Yougansh Thakur\n" +
                            "Harsh Vardhan\n" +
                            "Team Name: TechRex\n" +
                            "App submitted for #UNITEDBYHCL Hackathon\n" +
                            "Challenge: Second screen viewing at trackside – For Formula E")
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {

                                }
                            }
                    )
                    .show();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
