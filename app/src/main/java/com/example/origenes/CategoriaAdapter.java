package com.example.origenes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CategoriaAdapter extends RecyclerView.Adapter<CategoriaAdapter.CategoriaViewHolder> {

    private List<Categoria> categorias;
    public static OnCategoriaClickListener onCategoriaClickListener;

    public CategoriaAdapter(List<Categoria> categorias, OnCategoriaClickListener onCategoriaClickListener) {
        this.categorias = categorias;
        this.onCategoriaClickListener = onCategoriaClickListener;
    }

    @Override
    public CategoriaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_categoria, parent, false);
        return new CategoriaViewHolder(view, onCategoriaClickListener);
    }

    @Override
    public void onBindViewHolder(CategoriaViewHolder holder, int position) {
        Categoria categoria = categorias.get(position);
        holder.bind(categoria);
    }

    @Override
    public int getItemCount() {
        return categorias.size();
    }

    public interface OnCategoriaClickListener {
        void onCategoriaClick(Categoria categoria);
    }

    class CategoriaViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombreCategoria;
        ImageView imgCategoria;
        OnCategoriaClickListener onCategoriaClickListener;

        public CategoriaViewHolder(View itemView, OnCategoriaClickListener onCategoriaClickListener) {
            super(itemView);
            txtNombreCategoria = itemView.findViewById(R.id.txtNombreCategoria);
            imgCategoria = itemView.findViewById(R.id.imgCategoria);
            this.onCategoriaClickListener = onCategoriaClickListener;
            //itemView.setOnClickListener(this);
        }

        public void bind(Categoria categoria) {
            txtNombreCategoria.setText(categoria.getNombre());
            imgCategoria.setImageResource(categoria.getImagenRecurso());
            imgCategoria.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CategoriaAdapter.onCategoriaClickListener.onCategoriaClick(categorias.get(getAdapterPosition()));
                }
            });
        }
    }
}
