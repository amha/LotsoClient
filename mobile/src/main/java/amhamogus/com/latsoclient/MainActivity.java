package amhamogus.com.latsoclient;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;

import amhamogus.com.latsoclient.fragments.ProductDetailFragment;
import amhamogus.com.latsoclient.fragments.ScanProductFragment;

/**
 * Main entry point into the Lotso client.
 *
 * @author Amha Mogus (amha.mogus@gmail.com)
 */
public class MainActivity extends Activity
        implements ProductDetailFragment.OnFragmentInteractionListener,
        ScanProductFragment.OnFragmentInteractionListener {

    NfcAdapter mNfcAdapter;

    // Data received from NFC
    private String mPayload;

    // Message for Social Share
    protected String share_text = "";

    // Represents the details of a product.
    ProductDetailFragment productDetailFragment;

    // Screen that allows the user to tap/scan a product.
    // TODO: Need to redo theinteraction model.
    ScanProductFragment scanProductFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get NFC System Adapter
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        // If a user has tapped a
        Intent mIntent = getIntent();
        Bundle mBundle = mIntent.getExtras();

        Parcelable[] rawMsgs = mIntent
                .getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        if (rawMsgs != null) {
            NdefMessage[] msgs = new NdefMessage[rawMsgs.length];
            for (int i = 0; i < rawMsgs.length; i++) {
                msgs[i] = (NdefMessage) rawMsgs[i];
            }
            NdefRecord[] records = msgs[0].getRecords();
            mPayload = new String(records[0].getPayload());
            // Pass data to Fragment
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(
                    R.id.fragmentLayout,
                    productDetailFragment.newInstance(mPayload));
            transaction.commit();

        } else {
            // App was launched by user, so we prompt them to
            // interact (e.g. bump/scan/tap) with a product.
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(
                    R.id.fragmentLayout,
                    scanProductFragment.newInstance());
            transaction.commit();

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        PendingIntent intent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        NfcAdapter.getDefaultAdapter(this).enableForegroundDispatch(this, intent, null, null);
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
