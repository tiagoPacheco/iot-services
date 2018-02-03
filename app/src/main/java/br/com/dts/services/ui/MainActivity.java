package br.com.dts.services.ui;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import br.com.dts.services.R;
import br.com.dts.services.service.SampleIntentService;
import br.com.dts.services.service.SyncService;

public class MainActivity extends ListActivity {

    private final String options [] = {"Intent Service", "Pooling Service", "MP3 Player", "Download Manager" };

    private static final int SAMPLE_INTENT_SERVICE = 0;
    private static final int SAMPLE_POOLING_SERVICE = 1;
    private static final int SAMPLE_MP3 = 2;
    private static final int SAMPLE_DOWNLOAD_MANAGER =3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_selectable_list_item, options));

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        switch (position) {
            case SAMPLE_INTENT_SERVICE:
                startService(new Intent(this, SampleIntentService.class));
                break;
            case SAMPLE_POOLING_SERVICE:
                Intent service = new Intent(this, SyncService.class);
                service.setAction(SyncService.ACTION_SYNC_DATA);
                startService(service);
                break;
            case SAMPLE_MP3:
                startActivity(new Intent(this, Mp3ListActivity.class));
                break;
            case SAMPLE_DOWNLOAD_MANAGER:
                startActivity(new Intent(this, DownloadDemo.class));
                break;
        }
    }
}
