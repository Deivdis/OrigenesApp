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
    private Context context;

    public ProductoAdapter(List<Producto> productos, Context context) {
        this.productos = productos;
        this.context = context;
    }

    @Override
    public ProductoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_producto, parent, false);
        return new ProductoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductoViewHolder holder, int position) {
        Producto producto = productos.get(position);
        holder.bind(producto);
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
        notifyDataSetChanged();
    }

    class ProductoViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombreProducto;
        TextView txtDescripcionProducto;
        TextView txtPrecioProducto;
        ImageView imgProducto;

        public ProductoViewHolder(View itemView) {
            super(itemView);
            txtNombreProducto = itemView.findViewById(R.id.txtNombreProducto);
            txtDescripcionProducto = itemView.findViewById(R.id.txtDescripcionProducto);
            txtPrecioProducto = itemView.findViewById(R.id.txtPrecioProducto);
            imgProducto = itemView.findViewById(R.id.imgProducto);
        }

        public void bind(Producto producto) {
            txtNombreProducto.setText(producto.getNombre());
            txtDescripcionProducto.setText(producto.getDescripcion());
            txtPrecioProducto.setText(producto.getPrecio());
            imgProducto.setImageResource(producto.getImageResourceId()); // Utiliza el nuevo método getImageResourceId()
        }
    }
}
