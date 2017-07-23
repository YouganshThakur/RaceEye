package com.techrex.raceeye;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.VideoView;

public class FullScreen extends AppCompatActivity {

    WebView camera;
    String status="";
    LinearLayout controls;
    int flag=0;
    boolean isLandscape=false;
    String address;
    int width;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);
        address=getIntent().getStringExtra("ADDRESS");
        width=getIntent().getIntExtra("WIDTH",320);
        controls=(LinearLayout)findViewById(R.id.controls);
        camera=(WebView)findViewById(R.id.fullScreenWebView);
        camera.getSettings().setJavaScriptEnabled(true);
        camera.getSettings().setUseWideViewPort(false);
        camera.getSettings().setSupportZoom(false);
        camera.setPadding(0, 0, 0, 0);
        camera.setInitialScale(getScale());
        camera.setVisibility(View.GONE);
        camera.setWebViewClient(new WebViewClient() {
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
        camera.loadUrl(address);
        status="Playing";

    }
    public void play_pause(View view)
    {
        if(status.contains("Playing"))
        {
            camera.loadUrl("javascript:streamCmdPause( true );");
            ((Button)findViewById(R.id.play_pause)).setBackground(ContextCompat.getDrawable(this,R.drawable.pause));
            status="Paused";
        }
        else if(status.contains("Paused"))
        {
            camera.loadUrl("javascript:streamCmdPlay( true );");
            ((Button)findViewById(R.id.play_pause)).setBackground(ContextCompat.getDrawable(this,R.drawable.play));
            status="Playing";

        }
        ((ImageView)findViewById(R.id.live_logo)).setVisibility(View.GONE);

    }
    public void rewind(View view)
    {
        if(status.contains("Playing"))
        {
            camera.loadUrl("javascript:streamCmdPause( true );streamCmdFastRev( true );");
            ((Button)findViewById(R.id.play_pause)).setBackground(ContextCompat.getDrawable(this,R.drawable.pause));
            status="Paused";
        }
        else {
            camera.loadUrl("javascript:streamCmdFastRev( true );");
        }
        ((ImageView)findViewById(R.id.live_logo)).setVisibility(View.GONE);

    }
    public void forward(View view)
    {
        if(status.contains("Paused"))
        {
            camera.loadUrl("javascript:streamCmdPause( true );streamCmdFastFwd( true );");
            ((Button)findViewById(R.id.play_pause)).setBackground(ContextCompat.getDrawable(this,R.drawable.pause));
            status="Paused";
        }
        else {
            camera.loadUrl("javascript:streamCmdFastFwd( true );");
        }
        ((ImageView)findViewById(R.id.live_logo)).setVisibility(View.GONE);
    }
    public void go_live(View view)
    {
        if(status.contains("Paused"))
        {
            camera.loadUrl("javascript:streamCmdPlay( true );javascript:streamCmdStop( true );");
            status="Playing";
        }
        else {
            camera.loadUrl("javascript:streamCmdStop( true );");

        }
        ((ImageView)findViewById(R.id.live_logo)).setVisibility(View.VISIBLE);

    }
    public void control_box(View view)
    {

            controls.setVisibility(View.VISIBLE);


    }
    public void full_screen(View view)
    {
        if(isLandscape) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            isLandscape=false;
        }
        else{
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            isLandscape=true;
        }

    }

    private int getScale(){
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point point=new Point();
        display.getSize(point);
        int w = point.x;
        Double val = new Double(w)/new Double(width);
        val = val * 100d;
        return val.intValue();
    }

    @Override
    protected void onResume() {
        super.onResume();
        camera.setVisibility(View.GONE);
        camera.loadUrl(address);
        status="Playing";

    }
}
