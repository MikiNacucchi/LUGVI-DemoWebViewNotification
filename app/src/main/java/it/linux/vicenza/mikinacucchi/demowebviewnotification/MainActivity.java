package it.linux.vicenza.mikinacucchi.demowebviewnotification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*  Demo Code   */
        if(isOnline()){
            String link = getString(R.string.url_lugvi_events);

            WebView webView = (WebView)findViewById(R.id.webView);
            //Abilita Javascript
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);

            //Apri pagina Eventi LUG-Vi
            webView.loadUrl(link);
        }
        else{
            String msg_errConnection = getString(R.string.str_noconnection);
            Toast.makeText(this, msg_errConnection, Toast.LENGTH_LONG).show();

            //Termino l'Activity non potendo visualizzare la pagina web
            finish();
        }

        /*  ========    */

    }

    /*  Demo Code   */
    //Richiede permesso ACCESS_NETWORK_STATE
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
    /*  ========    */




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
