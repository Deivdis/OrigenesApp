package com.example.origenes;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;
public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder> {
    private List<Producto> productos;
    private OrigenesBD origenBD;
    private Context context;

    public ProductoAdapter(List<Producto> productos, Context context) {
        this.productos = productos;
        this.context = context;
        this.origenBD = new OrigenesBD(context);
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_producto, parent, false);
        return new ProductoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
        Producto producto = productos.get(position);

        holder.nombreTextView.setText(producto.getNombre());
        holder.descripcionTextView.setText(producto.getDescripcion());
        holder.precioTextView.setText(producto.getPrecio());

        // Cargar imagen con Picasso si la URL está disponible
        String imageUrl = obtenerUrlImagen(producto.getId());
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Picasso.get().load(imageUrl).into(holder.imagenProducto);
        }
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    // Función para obtener la URL de la imagen según el ID del producto
    private String obtenerUrlImagen(int productoId) {
        // Aquí consultamos la base de datos para obtener la URL de la imagen del producto
        // Puedes modificar esta parte para que se ajuste a tu implementación de base de datos
        SQLiteDatabase db = origenBD.getReadableDatabase();
        String[] projection = {OrigenesBD.COLUMNA_PRODUCTO_URL_IMAGEN};
        String selection = OrigenesBD.COLUMNA_PRODUCTO_ID + " = ?";
        String[] selectionArgs = {String.valueOf(productoId)};
        Cursor cursor = db.query(OrigenesBD.TABLA_PRODUCTOS, projection, selection, selectionArgs, null, null, null);
        String imageUrl = null;
        if (cursor.moveToFirst()) {
            imageUrl = cursor.getString(cursor.getColumnIndexOrThrow(OrigenesBD.COLUMNA_PRODUCTO_URL_IMAGEN));
        }
        cursor.close();
        return imageUrl;
    }

    static class ProductoViewHolder extends RecyclerView.ViewHolder {
        TextView nombreTextView, descripcionTextView, precioTextView;
        ImageView imagenProducto;

        ProductoViewHolder(View itemView) {
            super(itemView);
            nombreTextView = itemView.findViewById(R.id.nombreTextView);
            descripcionTextView = itemView.findViewById(R.id.descripcionTextView);
            precioTextView = itemView.findViewById(R.id.precioTextView);
            imagenProducto = itemView.findViewById(R.id.imagenImageView);
        }
    }
}