package com.example.origenes;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CarritoActivity extends AppCompatActivity {

    private OrigenesBD db;
    private RecyclerView recyclerView;
    private CarritoAdapter carritoAdapter;
    private TextView totalTextView;
    private static final String TAG = "CarritoActivity";
    private Button pagarButton;
    private PaymentSheet paymentSheet;
    private String clientSecret;
    private String publishableKey = "pk_test_51Ovia8P5eeNXgaze8j1kXdfPSySSXeGtjG3KZpufnX5U0jLZ8PEX0wicEJ9QQRRkN8V4xr0W1AKfAdDnLqaNe2ph00yJYK2OGR";
    private String secretKey = "sk_test_51Ovia8P5eeNXgazemGBIjiGaN5KRilot9Xphnyhm8U566fRf1GtGjndbQRc4A2q4eUquvjX0pFPqtMNbh9haZApa00OnnLcqbg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        PaymentConfiguration.init(getApplicationContext(), publishableKey);
        paymentSheet = new PaymentSheet(this, this::onPaymentSheetResult);

        db = new OrigenesBD(this);
        recyclerView = findViewById(R.id.recyclerViewCarrito);
        totalTextView = findViewById(R.id.totalTextView);
        pagarButton = findViewById(R.id.Pagarbutton);

        obtenerProductos();

        pagarButton.setOnClickListener(v -> {
            Log.d(TAG, "Pagarbutton clicked");
            crearPaymentIntent();
        });
    }

    private void obtenerProductos() {
        SharedPreferences prefs = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        int currentUserId = prefs.getInt("userId", -1);

        if (currentUserId == -1) {
            Log.e(TAG, "No user ID found, user might not be logged in");
            return;
        }

        List<Producto> productosEnCarrito = db.obtenerProductosDelCarrito(currentUserId);

        imprimirProductos(productosEnCarrito);
        carritoAdapter = new CarritoAdapter(productosEnCarrito, db, currentUserId, total -> totalTextView.setText(String.format("Total: $%.2f", total)));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(carritoAdapter);

        calcularTotal(productosEnCarrito);
    }

    private void imprimirProductos(List<Producto> productosEnCarrito) {
        for (Producto producto : productosEnCarrito) {
            Log.d(TAG, "Producto ID: " + producto.getId());
            Log.d(TAG, "Nombre: " + producto.getNombre());
            Log.d(TAG, "Descripción: " + producto.getDescripcion());
            Log.d(TAG, "Precio: " + producto.getPrecio());
            Log.d(TAG, "Cantidad: " + producto.getCantidad());
            Log.d(TAG, "Image Resource ID: " + producto.getImageResourceId());
        }
    }

    private void calcularTotal(List<Producto> productosEnCarrito) {
        double total = 0;
        for (Producto producto : productosEnCarrito) {
            total += producto.getPrecio() * producto.getCantidad();
        }
        totalTextView.setText(String.format("Total: $%.2f", total));
    }

    private void crearPaymentIntent() {
        Log.d(TAG, "Iniciando crearPaymentIntent");
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.stripe.com/v1/payment_intents";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Respuesta de Stripe: " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            clientSecret = jsonObject.getString("client_secret");
                            presentarFormularioDePago();
                        } catch (JSONException e) {
                            Log.e(TAG, "Error al parsear respuesta de Stripe: " + e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse != null && error.networkResponse.data != null) {
                    String errorMsg = new String(error.networkResponse.data);
                    Log.e(TAG, "Error en la solicitud a Stripe: " + errorMsg);
                } else {
                    Log.e(TAG, "Error en la solicitud a Stripe: " + error.getMessage());
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("amount", "1000"); // Monto en centavos
                params.put("currency", "usd");
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + secretKey);
                headers.put("Content-Type", "application/x-www-form-urlencoded"); // Asegúrate de que el tipo de contenido sea correcto
                return headers;
            }
        };

        queue.add(stringRequest);
    }

    private void presentarFormularioDePago() {
        Log.d(TAG, "Presentando formulario de pago con clientSecret: " + clientSecret);
        paymentSheet.presentWithPaymentIntent(clientSecret, new PaymentSheet.Configuration("Origenes Inc."));
    }

    private void onPaymentSheetResult(PaymentSheetResult paymentSheetResult) {
        if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            Log.d(TAG, "Pago completado exitosamente");
        } else if (paymentSheetResult instanceof PaymentSheetResult.Canceled) {
            Log.d(TAG, "Pago cancelado por el usuario");
        } else if (paymentSheetResult instanceof PaymentSheetResult.Failed) {
            PaymentSheetResult.Failed failedResult = (PaymentSheetResult.Failed) paymentSheetResult;
            Log.e(TAG, "Error en el pago: " + failedResult.getError());
        }
    }
}
