package w3t4.unterricht.w3t5_multimedia_menu_arzu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;
import android.widget.ViewFlipper;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity
{
    private ViewFlipper vf;
    private Button btnPlay, btnStop, btnPause;
    private MediaPlayer mediaPlayer;
    private TextView lbl;
    private ZeitThread zt;
    private Handler handler;
    private MenuItem miVideo, miSound;

    // ==============================================================
    private class ZeitThread extends Thread
    {
        @Override
        public void run()
        {
            while (true)
            {
                int maxSek, aktSek;

                if (mediaPlayer != null)
                {
                    maxSek = mediaPlayer.getDuration() / 1000;
                    aktSek = mediaPlayer.getCurrentPosition() / 1000;
                } else
                {
                    maxSek = 0;
                    aktSek = 0;
                }

                String text = String.format("akt = %d s, max = %d s", aktSek, maxSek);
                handler.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        lbl.setText(text);
                    }
                });

                try
                {
                    Thread.sleep(1000);
                } catch (InterruptedException ex)
                {
                    Log.e("FEHLER", Arrays.toString(ex.getStackTrace()));
                }
            }
        }
    }

    // ==============================================================
    private class MyOCL implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            if ((v == btnPlay) && (!mediaPlayer.isPlaying()))
                mediaPlayer.start();
            if ((v == btnPause) && (mediaPlayer.isPlaying()))
                mediaPlayer.pause();
            if ((v == btnStop) && (mediaPlayer.isPlaying()))
            {
                mediaPlayer.stop();
                sound();
            }
        }
    }

    // ==============================================================

    /**
     * Diese Methode f??gt dem Optionsmenu zwei Men??punkte hinzu. Das
     * f??hrt dazu, da?? die ber??hmten drei Punkte f??r das ??ffnen des
     * Kontextmen??s Men?? angezeigt werden.
     *
     * @param menu das Kontextmen??
     * @return boolean-Wert (egal^^)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        miVideo = menu.add("Video");
        miSound = menu.add("Sound");
        return true;
    }

    // --------------------------------------------------------------

    /**
     * Wird aus dem Optionsmen?? ein Men??punkt ausgew??hlt, landet der
     * Programmablauf in dieser Methode. Sie entspricht dem Listener
     * f??r die Men??auswahl.
     *
     * @param item Ausgew??hlter Men??punkt
     * @return boolean
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if (item == miSound)
            sound();
        else if (item == miVideo)
            video();

        return super.onOptionsItemSelected(item);
    }

    // --------------------------------------------------------------
    private void video()
    {
        vf.setDisplayedChild(0);

        VideoView film = (VideoView) findViewById(R.id.vwFilm);

        // der MediaController ist eine eingebettete Steuerleiste f??r die Videowiedergabe
        MediaController controller = new MediaController(AppGlobal.getContext());
        // was soll mit dem Controller gesteuert werden?
        controller.setAnchorView(film);
        // In welcher VideoView soll der MediaController angezeigt werden?
        film.setMediaController(controller);

        MyOCL ocl = new MyOCL();

        controller.setPrevNextListeners(ocl, ocl);

        String quelle = "res"; // "web"
        String url = null;

        // Wiedergabe aus den Ressourcen
        if (quelle.equals("res"))
        {
            url = "android.resource://" + getPackageName() + "/" + R.raw.android_video;
        } else if (quelle.equals("web"))
        {
            url = "https://grosskopf-software.homelinux.com/film.mp4";
        }

        film.setVideoURI(Uri.parse(url));
        film.start();
    }

    // --------------------------------------------------------------
    private void sound()
    {
        vf.setDisplayedChild(1);

        zt = null;
        zt = new ZeitThread();
        zt.start();
        mediaPlayer = MediaPlayer.create(AppGlobal.getContext(), R.raw.dst_assembly);
    }

    // --------------------------------------------------------------
    private void initComponents()
    {
        vf = (ViewFlipper) findViewById(R.id.vf);

        MyOCL ocl = new MyOCL();
        btnPlay = AppGlobal.getButton(this, R.id.btnPlay, ocl);
        btnPause = AppGlobal.getButton(this, R.id.btnPause, ocl);
        btnStop = AppGlobal.getButton(this, R.id.btnStop, ocl);

        lbl = (TextView) findViewById(R.id.lbl);
        handler = new Handler();
    }

    // --------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppGlobal.permissions(this,
                Manifest.permission.MANAGE_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.INTERNET);

        AppGlobal.setContext(this);

        initComponents();
    }
}