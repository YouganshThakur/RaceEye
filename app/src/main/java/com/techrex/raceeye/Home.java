package com.techrex.raceeye;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String address;
    WebView camera[];
    Context ctx;
    String mediaServerAddress[];
    int width[];
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ctx=this;
        progressDialog=new ProgressDialog(this);
        camera=new WebView[4];
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView titletext=(TextView)findViewById(R.id.title_textview);
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/NeoSansStd-Medium.ttf");
        titletext.setTypeface(custom_font);
        titletext.setText("RACE EYE");
        //setSupportActionBar(toolbar);
//        this.getActionBar().setDisplayShowTitleEnabled(false);
        SharedPreferences prefs = getSharedPreferences("CONNECTION", MODE_PRIVATE);
        address=prefs.getString("ADDRESS","http://");


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);
        camera[0]=(WebView)findViewById(R.id.cam1_image);
        camera[1]=(WebView)findViewById(R.id.cam2_image);
        camera[2]=(WebView)findViewById(R.id.cam3_image);
        camera[3]=(WebView)findViewById(R.id.cam4_image);
        for(int i=0;i<4;i++)
        {
            camera[i].setVisibility(View.INVISIBLE);
            camera[i].getSettings().setJavaScriptEnabled(true);
            camera[i].getSettings().setUseWideViewPort(false);
            camera[i].setPadding(0, 0, 0, 0);
            camera[i].setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    view.loadUrl("javascript:var a=document.getElementById('scaleControl').style.display='none';" +
                            "var a=document.getElementById('monitorName').style.display='none';" +
                            "var a=document.getElementById('disableAlarmsLink').style.display='none';" +
                            "var a=document.getElementById('replayStatus').style.display='none';" +
                            "var a=document.getElementById('monitorState').style.display='none';" +
                            "var a=document.getElementsByTagName('tr')[0].style.display='none';" +
                            "var a=document.body.style.backgroundColor = 'black';" +
                            "var a=document.getElementById('dvrControls').style.display='none';" +
                            "var a=document.getElementsByTagName('a')[0].style.display='none';" +
                            "var a=document.getElementById('forceAlarmLink').style.display='none';");
                    view.setVisibility(View.VISIBLE);
                }
            });
        }

        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Connecting...");
        progressDialog.show();
        new Get_Camera_Details().execute();



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        }else if(id==R.id.nav_server)
        {
            startActivity(new Intent(this,Wizard.class));
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
    public void camera(View view)
    {
       if(mediaServerAddress==null)return;
        Intent i=new Intent(this,FullScreen.class);
        switch(view.getId())
        {
            case R.id.cam1_button:   if(!(mediaServerAddress.length>0))return;
                i.putExtra("ADDRESS",mediaServerAddress[0]);
                                    i.putExtra("WIDTH",width[0]);
                            break;
            case R.id.cam2_button:   if(!(mediaServerAddress.length>1))return;
                i.putExtra("ADDRESS",mediaServerAddress[1]);
                i.putExtra("WIDTH",width[1]);
                break;
            case R.id.cam3_button:   if(!(mediaServerAddress.length>2))return;
                i.putExtra("ADDRESS",mediaServerAddress[2]);
                i.putExtra("WIDTH",width[2]);
                break;
            case R.id.cam4_button:   if(!(mediaServerAddress.length>3))return;
                i.putExtra("ADDRESS",mediaServerAddress[3]);
                i.putExtra("WIDTH",width[3]);
                break;
        }


        startActivity(i);

    }
    class Get_Camera_Details extends AsyncTask<Void,Void,String>
    {

        String fullurl=address+"/app/camera_details.php";
        @Override
        protected String doInBackground(Void... params) {
            try {
                HttpURLConnection con= (HttpURLConnection) new URL(fullurl).openConnection();
                con.setConnectTimeout(10000);
                InputStream in=con.getInputStream();
                Reader reader=new InputStreamReader(in,"UTF-8");
                // char[] buffer=new char[];
                BufferedReader br = new BufferedReader(reader);
                try {
                    StringBuilder sb = new StringBuilder();
                    String line = br.readLine();

                    while (line != null) {
                        sb.append(line);
                        sb.append("\n");
                        line = br.readLine();
                    }
                    return sb.toString();
                } finally {
                    br.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return "A101";
        }

        @Override
        protected void onPostExecute(String s) {


            if(!((Activity) ctx).isFinishing()) {
                if (s.contains("A101")) {
                    new AlertDialog.Builder(ctx)
                            .setTitle("Unable to connect with server")
                            .setMessage("Please check the server configuration")
                            .setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {

                                        }
                                    }
                            )
                            .show();
                }
                else {
                    int count=0;
                    try {
                        JSONObject data=new JSONObject(s);
                        count=data.getInt("count");
                        mediaServerAddress=new String[count];
                        width=new int[count];
                        for(int i=0;i<count;i++)
                        {
                            JSONObject jsonObject=(data.getJSONArray("details")).getJSONObject(i);
                            mediaServerAddress[i]=jsonObject.getString("address");
                            width[i]=jsonObject.getInt("width");
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    for(int i=0;i<count;i++)
                    {
                        camera[i].setInitialScale(getScale(i));

                        camera[i].loadUrl(mediaServerAddress[i]);
                    }




                }

            }
            progressDialog.dismiss();

        }
    }
    private int getScale(int i){
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point point=new Point();
        display.getSize(point);
        int w = point.x;
        Double val = new Double(w)/new Double(width[i]);
        val = val * 100d;
        return val.intValue();
    }

}
