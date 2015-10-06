package it.linux.vicenza.mikinacucchi.demowebviewnotification;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

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
        //Verifica se oggi è Martedì
        if(isTuesday()) {

            buildNotification(context);//Visualizza la notifica

        }
        else{
            //Imposta un "promemoria" per riverificare se sia diventato Martedì
            AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

            //Richiama questo Receiver
            Intent i = new Intent(context, BootEventReceiver.class);
            PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);

            //Verifica tra 5 ore
            am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 3600 * 5 , pi);
        }
        /*  =========   */
    }

    /*  Demo Code */
    private boolean isTuesday() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK); //Restituisce il giorno attuale

        return (day == Calendar.TUESDAY);//Vero se è Martedì
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

        //Componente per notificare al SO che vogliamo lanciare una notifica
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, n.build());
    }
    /*  =========   */
}
