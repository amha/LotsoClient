package amhamogus.com.latsoclient.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import amhamogus.com.latsoclient.R;

/**
 * Placeholder screen for demo purposes that asks the user
 * to interact with a product be scanning or tapping.
 *
 * @author Amha Mogus
 */
public class ScanProductFragment extends Fragment {

    private String title = "TEMP TITLE";
    private int page = 0;

    private OnFragmentInteractionListener mListener;

    /**
     * Screen that prompts the user to scan/interact
     * with a product. Note, this is merely a placeholder
     * screen for the demo.
     * @return A new instance of fragment ScanProductFragment.
     */
    public static ScanProductFragment newInstance() {
        return new ScanProductFragment();
    }

    public ScanProductFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scan_product, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onScanFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onScanFragmentInteraction(Uri uri);
    }

}
