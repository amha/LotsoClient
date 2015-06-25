package amhamogus.com.latsoclient.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import amhamogus.com.latsoclient.MainActivity;
import amhamogus.com.latsoclient.R;
import amhamogus.com.latsoclient.net.YaaSService;

/**
 * Create an instance of a Lotso product.
 *
 * @author Amha Mogus
 */
public class ProductDetailFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // Product object and assicated data.
    private JSONObject productInformation;
    private TextView productTitle;
    private TextView productDescription;
    private ImageView productImage;
    private TextView productCondition;

    // Product ID that is passed into this fragment.
    private String mParam1;

    // TODO
    private OnFragmentInteractionListener mListener;

    // View that will be updated when a product history
    // has been returned from the Lotso product repository.
    private LinearLayout mHistoryCard;

    /**
     * Create an instance of a Lotso product.
     *
     * @param productID Parameter 1.
     * @return A new instance of fragment ProductDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductDetailFragment newInstance(String productID) {

        // Extract product code (a 4 digit number for demo purposes)
        String code = productID.substring(productID.length() - 4, productID.length());

        // Pass product code as a parameter.
        ProductDetailFragment fragment = new ProductDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, code);
        fragment.setArguments(args);

        return fragment;
    }

    public ProductDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentLayout =
                inflater.inflate(R.layout.fragment_product_detail, container, false);

        // Retrieve UI elements that will be updated onPostExecute.
        productTitle = (TextView) fragmentLayout.findViewById(R.id.product_title);
        productDescription = (TextView) fragmentLayout.findViewById(R.id.product_description);
        productImage = (ImageView) fragmentLayout.findViewById(R.id.damn_image);
        productCondition = (TextView) fragmentLayout.findViewById(R.id.product_condition);
        mHistoryCard = (LinearLayout) fragmentLayout.findViewById(R.id.card_list);

        // Send request data request in the backgound.
        new ProductWorker().execute(mParam1);
        return fragmentLayout;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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
        public void onFragmentInteraction(Uri uri);
    }

    private class ProductWorker extends AsyncTask<String, String, JSONObject> {

        protected JSONObject doInBackground(String... strings) {
            YaaSService service = new YaaSService();
            JSONObject returnedObject = service.getProduct(strings[0]);
            return returnedObject;
        }

        @Override
        protected void onPostExecute(JSONObject object) {

            Log.d("LOTSO", "JSON = " + object.toString());

            try {
                // Extract Product data from JSON Objects from the server response.
                JSONObject productInfo = object.getJSONObject("product");
                JSONArray productMedia = object.getJSONArray("media");

                // Get product history and verification.
                JSONObject productInstance = object.getJSONObject("instance");
                JSONArray instanceHistory = productInstance.getJSONArray("history");

                // For each date, create a text view and insert into UI.
                TextView myTextView;
                for (int i = 0; i < instanceHistory.length(); i++) {
                    myTextView = new TextView(getActivity());
                    myTextView.setText(instanceHistory.getJSONObject(i).getString("date"));
                    myTextView.setTextAppearance(getActivity(), R.style.productDate);
                    mHistoryCard.addView(myTextView);
                }

                // Populate product title.
                productTitle.setText(productInfo.getString("name"));

                // Populate product description.
                productDescription.setText(productInfo.getString("description"));

                // Download product image.
                loadImage(productMedia.getJSONObject(0).getString("url"));

                // Populate product condition.
                productCondition.setText(productInstance.getString("condition"));

                // Save product info for social share.
                ((MainActivity) getActivity()).setShare("Hey, I just got "
                        + productInfo.getString("name")
                        + "" + " from #lotso. #hybris #yaashackathon #isobar #nyc");

            } catch (JSONException e) {
                //TODO:
                Log.d("LOTSO", e.toString());
            }
        }
    }

    public void loadImage(String url) {
        Glide.with(getActivity().getApplicationContext())
                .load(url)
                .into(productImage);
    }
}
