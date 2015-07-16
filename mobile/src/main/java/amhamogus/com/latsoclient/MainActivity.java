package amhamogus.com.latsoclient;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.tech.NfcF;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import amhamogus.com.latsoclient.activities.Product;
import amhamogus.com.latsoclient.adapters.LatsoPageAdapter;
import amhamogus.com.latsoclient.fragments.ProductDetailFragment;
import amhamogus.com.latsoclient.fragments.ScanProductFragment;

/**
 * Main entry point into the Lotso client.
 *
 * @author Amha Mogus (amha.mogus@gmail.com)
 */
public class MainActivity extends AppCompatActivity
        implements ScanProductFragment.OnFragmentInteractionListener {

    NfcAdapter mNfcAdapter;
    // Data received from NFC
    private String mPayload;
    // Message for Social Share
    protected String share_text = "";
    // Represents the details of a product.
    ProductDetailFragment productDetailFragment;
    // Screen that allows the user to tap/scan a product.
    // TODO: Need to redo the interaction model.
    ScanProductFragment scanProductFragment;

    LatsoPageAdapter pageAdapter;
    PendingIntent pendingIntent;
    IntentFilter[] intentFiltersArray;
    String[][] techListsArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get NFC System Adapter
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Log.d("AMHA-OUT", "Extras = " + extras.toString());

            Parcelable[] rawMsgs = getIntent()
                    .getParcelableArrayExtra(mNfcAdapter.EXTRA_NDEF_MESSAGES);

            if (rawMsgs != null) {
                NdefMessage[] msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                    Log.d("AMHA-OUT", "msg [i] = " + msgs[i].toString());
                }
                NdefRecord[] records = msgs[0].getRecords();
                mPayload = new String(records[0].getPayload());

                Log.d("AMHA-OUT", "payload = " + mPayload);

                Intent newintent = new Intent(getApplicationContext(), Product.class);

                Bundle args = new Bundle();
                args.putString("ID", mPayload);
                newintent.putExtras(args);
                startActivity(newintent);
            }
        }

        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pageAdapter = new LatsoPageAdapter(getSupportFragmentManager());
        pager.setAdapter(pageAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(pager);

        pendingIntent = PendingIntent.getActivity(
                this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndef.addDataType("*/*");
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("fail", e);
        }
        intentFiltersArray = new IntentFilter[]{ndef,};
        techListsArray = new String[][]{new String[]{NfcF.class.getName()}};
    }


    @Override
    public void onResume() {
        super.onResume();
        mNfcAdapter.enableForegroundDispatch(this,
                pendingIntent,
                intentFiltersArray,
                techListsArray);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (NfcAdapter.getDefaultAdapter(this) != null)
            NfcAdapter.getDefaultAdapter(this).disableForegroundDispatch(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.menu_item_share) {
            //TODO: build out the interaction model.
            // Share the current product
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, share_text);
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onNewIntent(Intent intent) {
        String payload = "";

        Parcelable[] rawMsgs = intent
                .getParcelableArrayExtra(mNfcAdapter.EXTRA_NDEF_MESSAGES);

        if (rawMsgs != null) {
            NdefMessage[] msgs = new NdefMessage[rawMsgs.length];
            for (int i = 0; i < rawMsgs.length; i++) {
                msgs[i] = (NdefMessage) rawMsgs[i];
            }
            NdefRecord[] records = msgs[0].getRecords();
            payload = new String(records[0].getPayload());

            Bundle args = new Bundle();
            args.putString("ID", payload);

            Intent newintent = new Intent(getApplicationContext(), Product.class);
            newintent.putExtras(args);
            startActivity(newintent);

        } else {
            Log.d("AMHA-OUT", "Intent is empty");
        }
    }

    public void onFragmentInteraction(Uri uri) {
        //TODO: build out the interaction model.
    }

    public void onScanFragmentInteraction(Uri uri) {
        //TODO: build out the interaction model.
    }

    /**
     * Setter method that saves text to be shared.
     *
     * @param text Product text for social share.
     */
    public void setShare(String text) {
        this.share_text = text;
    }
}
