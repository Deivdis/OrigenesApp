package com.example.origenes;

import android.content.Intent;
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
    private final String publishableKey = "pk_test_51Ovia8P5eeNXgaze8j1kXdfPSySSXeGtjG3KZpufnX5U0jLZ8PEX0wicEJ9QQRRkN8V4xr0W1AKfAdDnLqaNe2ph00yJYK2OGR";
    private final String secretKey = "sk_test_51Ovia8P5eeNXgazemGBIjiGaN5KRilot9Xphnyhm8U566fRf1GtGjndbQRc4A2q4eUquvjX0pFPqtMNbh9haZApa00OnnLcqbg";
    private final double exchangeRate = 4000.00; // Tasa de cambio fija (1 USD = 4000 COP)

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

        carritoAdapter = new CarritoAdapter(productosEnCarrito, db, currentUserId, total -> totalTextView.setText(String.format("Total: $%.0f COP", total)));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(carritoAdapter);

        calcularTotal(productosEnCarrito);
    }

    private void calcularTotal(List<Producto> productosEnCarrito) {
        double total = productosEnCarrito.stream()
                .mapToDouble(producto -> producto.getPrecio() * producto.getCantidad())
                .sum();
        totalTextView.setText(String.format("Total: $%.0f COP", total));
    }

    private void crearPaymentIntent() {
        Log.d(TAG, "Iniciando crearPaymentIntent");
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.stripe.com/v1/payment_intents";

        double totalPesos = calcularTotalProductos();
        int totalDolaresCentavos = (int) Math.round(totalPesos / exchangeRate * 100000);

        Log.d(TAG, "Total en COP: " + totalPesos);
        Log.d(TAG, "Total en USD: " + totalDolaresCentavos / 100.0);
        Log.d(TAG, "Total en Centavos USD: " + totalDolaresCentavos);

        if (totalDolaresCentavos < 50) { // Se requiere un mínimo de 0.50 USD como monto mínimo de pago
            Log.e(TAG, "El monto es demasiado pequeño para procesar un pago.");
            totalTextView.setText("El monto mínimo para el pago es $0.50 USD.");
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d(TAG, "Respuesta de Stripe: " + response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        clientSecret = jsonObject.getString("client_secret");
                        presentarFormularioDePago();
                    } catch (JSONException e) {
                        Log.e(TAG, "Error al parsear respuesta de Stripe: " + e.getMessage());
                    }
                },
                error -> {
                    if (error.networkResponse != null && error.networkResponse.data != null) {
                        String errorMsg = new String(error.networkResponse.data);
                        Log.e(TAG, "Error en la solicitud a Stripe: " + errorMsg);
                    } else {
                        Log.e(TAG, "Error en la solicitud a Stripe: " + error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("amount", String.valueOf(totalDolaresCentavos)); // Monto en centavos
                params.put("currency", "usd");
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + secretKey);
                headers.put("Content-Type", "application/x-www-form-urlencoded");
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
            vaciarCarrito();
        } else if (paymentSheetResult instanceof PaymentSheetResult.Canceled) {
            Log.d(TAG, "Pago cancelado por el usuario");
        } else if (paymentSheetResult instanceof PaymentSheetResult.Failed) {
            PaymentSheetResult.Failed failedResult = (PaymentSheetResult.Failed) paymentSheetResult;
            Log.e(TAG, "Error en el pago: " + failedResult.getError());
        }
    }

    private void vaciarCarrito() {
        SharedPreferences prefs = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        int currentUserId = prefs.getInt("userId", 0);

        if (currentUserId != 0) {
            db.vaciarCarrito(currentUserId);
            Log.d(TAG, "Carrito vaciado para el usuario con ID: " + currentUserId);

            // Actualizar la lista de productos y la vista del RecyclerView
            List<Producto> productosVacios = db.obtenerProductosDelCarrito(currentUserId);
            carritoAdapter = new CarritoAdapter(productosVacios, db, currentUserId, total -> totalTextView.setText(String.format("Total: $%.0f COP", total)));
            recyclerView.setAdapter(carritoAdapter);
            carritoAdapter.notifyDataSetChanged();

            // Recalcular el total
            calcularTotal(productosVacios);
        } else {
            Log.e(TAG, "No se encontró ID de usuario, es posible que el usuario no esté logueado");
        }
    }

    private double calcularTotalProductos() {
        SharedPreferences prefs = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        int currentUserId = prefs.getInt("userId", -1);

        if (currentUserId == -1) {
            Log.e(TAG, "No user ID found, user might not be logged in");
            return 0;
        }

        List<Producto> productosEnCarrito = db.obtenerProductosDelCarrito(currentUserId);
        return productosEnCarrito.stream()
                .mapToDouble(producto -> producto.getPrecio() * producto.getCantidad())
                .sum();
    }
}
