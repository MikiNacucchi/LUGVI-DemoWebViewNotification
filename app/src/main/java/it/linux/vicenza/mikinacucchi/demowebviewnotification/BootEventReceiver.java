package it.linux.vicenza.mikinacucchi.demowebviewnotification;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Calendar;

public class BootEventReceiver extends BroadcastReceiver {
    public BootEventReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        //throw new UnsupportedOperationException("Not yet implemented");


        /*  Demo Code*/

        //"Gestore dei promemoria" per riverificare se sia diventato Martedì
        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        Intent i = new Intent(context, BootEventReceiver.class);//Richiama questo Receiver
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);//<-- PendingIntent!



        //Verifica se oggi è Martedì
        if(isTuesday()) {
            Log.i("BootEventReceiver","BootEventReceiver|isTuesday");

            am.cancel(pi);//Cancella allarmi pendendti per evitare notifiche multiple, sarà riattivato alla prossima riaccensione

            buildNotification(context);//Visualizza la notifica
        }
        else{
            Log.i("BootEventReceiver","BootEventReceiver|else");

            //Verifica ogni ora
            am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000  * 3600, pi);
        }
        /*  =========   */
    }

    /*  Demo Code */
    private boolean isTuesday() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK); //Restituisce il giorno attuale

        //return (day == Calendar.TUESDAY);//Vero se è Martedì
        return (day == Calendar.SATURDAY);//Per linuxDay
    }

    private void buildNotification(Context context) {
        //Azione da Eseguire alla pressione della Notifica
        Intent i=new Intent(context,MainActivity.class);//Avvia la nostra Activity principale con la pagina eventi
        PendingIntent pi= PendingIntent.getActivity(context, 0, i, 0); //PendingIntent perchè non sarà eseguita ora

        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);//Suono di notifica di default

        String msgAppname = context.getString(R.string.app_name); //Titolo Notifica
        String msgNotification = context.getString(R.string.str_notificationText); //Testo Notifica

        NotificationCompat.Builder n  = new NotificationCompat.Builder(context) //Crea un oggetto Notifica
                .setContentTitle(msgAppname)
                .setContentText(msgNotification)
                .setSound(sound)
                .setSmallIcon(android.R.drawable.ic_menu_agenda) //Icona notifica
                .setContentIntent(pi) //cosa eseguire
                .setAutoCancel(true); //cancella la notifica alla pressione



        ////////////////////    Estendiamo la notifica per la visualizzazione su Smartwatch //////////////////////
        String location = "45.520258, 11.616224";//Coordinate LugVi

        //Creaiamo l'azione "Raggiungi LugVi" per avviare la navigazione verso
        Intent mapIntent = new Intent(Intent.ACTION_VIEW);
        Uri geoUri = Uri.parse("geo:0,0?q=" + Uri.encode(location));
        mapIntent.setData(geoUri);
        PendingIntent mapPendingIntent = PendingIntent.getActivity(context, 0, mapIntent, 0);

        NotificationCompat.Action action = new NotificationCompat.Action.Builder(
                android.R.drawable.ic_menu_directions,
                context.getString(R.string.str_action_map), mapPendingIntent).build();


        //Creiamo la Notifica per lo Smartwatch
        NotificationCompat.WearableExtender wearableExtender = new NotificationCompat.WearableExtender()
                .setBackground(BitmapFactory.decodeResource(
                        context.getResources(), R.mipmap.ic_launcher))
                .addAction(action);//aggiungiamo l'azione precedentemente creata

        //Estendiamo la notifica(su smartphone) della funzionalità wearable(verrà visualizzata anche su smartwatch)
        n.extend(wearableExtender);
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////


        //Componente per notificare al SO che vogliamo lanciare una notifica
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, n.build());
    }
    /*  =========   */
}
