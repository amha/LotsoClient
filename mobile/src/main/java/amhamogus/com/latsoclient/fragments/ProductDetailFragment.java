package amhamogus.com.latsoclient.fragments;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import amhamogus.com.latsoclient.R;
import amhamogus.com.latsoclient.net.YaaSService;

/**
 * Create an instance of a Lotso product.
 *
 * @author Amha Mogus
 */
public class ProductDetailFragment extends Fragment {

    private String TAG = "LOCAL-LOTSO";
    private static final String ARG_PARAM1 = "param1";

    private String[] product1 = {"Coach Classic Leather Penny Shoulder Purse", "Coach Classic Leather Shoulder Purse 17994. Display Bag ~ New This genuine Coach Shoulder Bag shows only the slightest hint of handling with a couple of extremely faint surface marks but nothing obvious or significant (see close-up photos). Leather remains firm and intact with no shelf wear. Hardware remains bright and shiny. Interior is in new condition with no issues. Note that the bag was missing the shoulder strap which has been replaced with a genuine leather (generic) shoulder strap which is not removable. Comes with original Coach care card.", "Used"};
    private String[] product2 = {"Marc Jacobs: New Nudes Sheer Lip Gel", "Sheer, chic and habit-forming, this covetable lip collection breaks the mold of traditional nude lipstick with its innovative gellified formula that glides effortlessly over lips, laying down subtle shades with a sleek shine finish. Velvet-like Monoi butter, derived from the tropical Tiaré flower, delivers supreme moisture while nourishing myrrh extract and antioxidant vitamin e reduce signs of aging, including vertical fine lines.", "New"};

    // Product object and associated data.
    private JSONObject productInformation;
    private TextView productTitle;
    private TextView productDescription;
    private ImageView productImage;
    private TextView productCondition;
    private ScrollView scrollView;
    private ProgressBar progressBar;

    // Product ID that is passed into this fragment.
    private String mParam1;

    // View that will be updated when a product history
    // has been returned from the Lotso product repository.
    private LinearLayout mHistoryCard;

    /**
     * Create an instance of a Lotso product.
     *
     * @param productID Product ID to send to YaaS.
     * @return A new instance of fragment ProductDetailFragment.
     */
    public static ProductDetailFragment newInstance(String productID) {

        // Pass product code as a parameter.
        ProductDetailFragment fragment = new ProductDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, productID);
//        Log.d(TAG, "PRODUCT ID = "+ productID);
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
        productDescription.setLineSpacing(2, 1.1f);
        productImage = (ImageView) fragmentLayout.findViewById(R.id.damn_image);
        productCondition = (TextView) fragmentLayout.findViewById(R.id.product_condition);
        //mHistoryCard = (LinearLayout) fragmentLayout.findViewById(R.id.card_list);

        scrollView = (ScrollView) fragmentLayout.findViewById(R.id.scrollView);
//
//        if(mParam1 == "en001"){
//            productTitle.setText(product1[0]);
//            productDescription.setText(product1[1]);
//            productCondition.setText(product1[2]);
//            productImage.setImageResource(R.drawable.product_bag);
//        }
//        else{
//            productTitle.setText(product2[0]);
//            productDescription.setText(product2[1]);
//            productCondition.setText(product2[2]);
//            productImage.setImageResource(R.drawable.product_lip_stick);
//        }
        //Removing call to YaaS for the local version of the app.
        // Send request data request in the backgound.
        new ProductWorker().execute(mParam1);
        return fragmentLayout;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    private class ProductWorker extends AsyncTask<String, String, JSONObject> {

        protected JSONObject doInBackground(String... strings) {
            YaaSService service = new YaaSService();
            return service.getProduct(strings[0]);
        }

        @Override
        protected void onPostExecute(JSONObject object) {

            try {
                // Extract Product data from JSON Objects from the server response.
                JSONObject productInfo = object.getJSONObject("product");
                JSONArray productMedia = object.getJSONArray("media");

                // Get product history and verification.
                JSONObject productInstance = object.getJSONObject("instance");
                Log.d("OUTPUT", productInstance.toString());
                JSONArray instanceHistory = productInstance.getJSONArray("history");

//                progressBar.setVisibility(View.INVISIBLE);
                scrollView.setVisibility(View.VISIBLE);

                // For each date, create a text view and insert into UI.
                TextView myTextView;
                for (int i = 0; i < instanceHistory.length(); i++) {
                    myTextView = new TextView(getActivity());
                    myTextView.setText(instanceHistory.getJSONObject(i).getString("date"));
                    myTextView.setTextAppearance(getActivity(), R.style.productDate);
                    //mHistoryCard.addView(myTextView);
                }

                // Populate product title.
                productTitle.setText(productInfo.getString("name"));

                // Populate product description.
                productDescription.setText(productInfo.getString("description"));

                // Download product image.
                loadImage(productMedia.getJSONObject(0).getString("url"));

                // Populate product condition.
                productCondition.setText(productInstance.getString("condition"));

//                // Save product info for social share.
//                ((MainActivity) getActivity()).setShare("Hey, I just got "
//                        + productInfo.getString("name")
//                        + "" + " from #lotso. #hybris #yaashackathon #isobar #nyc");


            } catch (JSONException e) {
                //TODO:
                Log.d("LOTSO", e.toString());
            }
        }
    }

    public void loadImage(String url) {
        Log.d("URLTEST", url);
        Glide.with(getActivity().getApplicationContext())
                .load(url)
                .into(productImage);
    }
}
