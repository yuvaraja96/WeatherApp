package com.example.weatherapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import dmax.dialog.SpotsDialog;


public class ItemDetail extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final int REQUEST_CODE = 1234;
    final String API_GET_TOKEN = "http://172.16.8.247/braintree/main.php";
    final String API_CHECK_OUT = "http://172.16.8.247/braintree/checkout.php";
    DatabaseReference reff;
    History history;
    TextView mTitle, mDes, mAmount;
    ImageView mImage;
    String token, amount;
    HashMap<String, String> paramsHash;
    Button btnPay;
    private FirebaseUser user;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        ActionBar actionBar = getSupportActionBar();

        mTitle = findViewById(R.id.titleTv2);
        mDes = findViewById(R.id.descriptionTv2);
        mImage = findViewById(R.id.imageTv2);
        mAmount = findViewById(R.id.amountTv);
        reff = FirebaseDatabase.getInstance().getReference().child("History");
        history = new History();

        Intent intent = getIntent();

        String mTitle2 = intent.getStringExtra("iTitle");
        String mDescription = intent.getStringExtra("iDesc");

        byte[] mBytes = getIntent().getByteArrayExtra("iImage");

        Bitmap bitmap = BitmapFactory.decodeByteArray(mBytes, 0, mBytes.length);

        actionBar.setTitle(mTitle2);

        mTitle.setText(mTitle2);
        mDes.setText(mDescription);
        mImage.setImageBitmap(bitmap);

        String[] arraySpinner = new String[]{
                "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"
        };

        Spinner s = findViewById(R.id.qtySpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, arraySpinner);
        adapter.setDropDownViewResource(R.layout.spinner_layout);
        s.setAdapter(adapter);
        s.setOnItemSelectedListener(this);

        btnPay = findViewById(R.id.btnPay);
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPayment();


            }
        });


        loadToken();
    }

    private void loadToken() {

        final AlertDialog waitingDialog = new SpotsDialog.Builder().setContext(this).build();

        waitingDialog.show();
        waitingDialog.setMessage("Please wait...");

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(API_GET_TOKEN, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                waitingDialog.dismiss();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                waitingDialog.dismiss();
                token = responseString;
            }
        });
    }

    private void submitPayment() {
        DropInRequest dropInRequest = new DropInRequest().clientToken(token);
        startActivityForResult(dropInRequest.getIntent(this), REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                PaymentMethodNonce nonce = result.getPaymentMethodNonce();
                String strNonce = nonce.getNonce();

                if (!mAmount.getText().toString().isEmpty()) {
                    amount = mAmount.getText().toString();
                    paramsHash = new HashMap<>();
                    paramsHash.put("amount", amount);
                    paramsHash.put("nonce", strNonce);
                    sendPayments2();

                } else {
                    Toast.makeText(this, "Please enter valid amount", Toast.LENGTH_SHORT).show();
                }
            } else if (resultCode == RESULT_CANCELED)
                Toast.makeText(this, "User Cancel", Toast.LENGTH_SHORT).show();
            else {
                Exception error = (Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
                Log.d("ERROR", error.toString());
            }
        }
    }

    /*private void sendPayments() {
        RequestQueue queue = Volley.newRequestQueue(ItemDetail.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, API_CHECK_OUT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("Successful"))
                    Toast.makeText(ItemDetail.this, "Transaction successful!", Toast.LENGTH_SHORT).show();
                else {
                    Toast.makeText(ItemDetail.this, "Transaction failed!", Toast.LENGTH_SHORT).show();
                }
                Log.d("ERROR", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                if (paramsHash == null)
                    return null;
                Map<String, String> params = new HashMap<>();
                for (String key : paramsHash.keySet()) {
                    params.put(key, paramsHash.get(key));
                }
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        queue.add(stringRequest);
    }*/

    private void sendPayments2() {
        AsyncHttpClient client = new AsyncHttpClient();
        Map<String, String> paramMap = new HashMap<>();
        for (String key : paramsHash.keySet()) {
            paramMap.put(key, paramsHash.get(key));
        }
        RequestParams params = new RequestParams(paramMap);
        client.post(API_CHECK_OUT, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Toast.makeText(ItemDetail.this, "Transaction successful!", Toast.LENGTH_SHORT).show();

                Spinner s = findViewById(R.id.qtySpinner);
                String qty = s.getSelectedItem().toString();

                user = FirebaseAuth.getInstance().getCurrentUser();
                String tool = mTitle.getText().toString();
                String amount = mAmount.getText().toString();

                history.setUser(user);
                history.setTool(tool);
                history.setQuantity(qty);
                history.setPrice(amount);

                reff.push().setValue(history);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(ItemDetail.this, "Transaction failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        int quantity = Integer.parseInt(adapterView.getItemAtPosition(i).toString());
        int amt = quantity * 10;

        String totalAmount = amt + "";

        mAmount.setText(totalAmount);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
