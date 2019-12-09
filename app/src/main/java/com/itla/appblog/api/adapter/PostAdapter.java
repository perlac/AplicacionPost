package com.itla.appblog.api.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itla.appblog.ComentariosActivity;
import com.itla.appblog.R;
import com.itla.appblog.api.Funciones;
import com.itla.appblog.api.ManejadorSesion;
import com.itla.appblog.api.ServicioApi;
import com.itla.appblog.api.interfaces.PostInterface;
import com.itla.appblog.api.modelos.Post;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Post> postLista;

    public PostAdapter(List<Post> myDataset) {
        postLista = myDataset;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_post, parent, false);
        return new RecyclerView.ViewHolder(view) {
        };
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        final Post itemLista = postLista.get(holder.getAdapterPosition());

        final TextView tvTitulo, tvCuerpo, tvCantLike, tvCantVista, tvNomUsuario, tvComentarios, tvFecha, tvEmail;
        final ImageButton ibLike;

        tvTitulo = holder.itemView.findViewById(R.id.tvtitulo);
        tvCuerpo = holder.itemView.findViewById(R.id.tvcuerpo);
        tvCantLike = holder.itemView.findViewById(R.id.tvcantlike);
        tvCantVista = holder.itemView.findViewById(R.id.tvcantvista);
        tvNomUsuario = holder.itemView.findViewById(R.id.tvnomusuario);
        tvComentarios = holder.itemView.findViewById(R.id.tvcant_comentarios_post);
        tvEmail = holder.itemView.findViewById(R.id.tvemail_usuario_post);
        tvFecha = holder.itemView.findViewById(R.id.tvfecha_post);
        ibLike = holder.itemView.findViewById(R.id.iblike);

        tvTitulo.setText(itemLista.getTitle());
        tvCuerpo.setText(itemLista.getBody());
        tvCantLike.setText(itemLista.getLikes() + "");
        tvCantVista.setText(itemLista.getViews() + "");
        tvNomUsuario.setText(itemLista.getUserName());
        tvComentarios.setText(itemLista.getComments());
        tvFecha.setText(Funciones.FormatoFecha(new Date(itemLista.getCreatedAt())));
        tvEmail.setText(itemLista.getUserEmail());

        if (itemLista.isLiked())
            ibLike.setImageResource(R.drawable.like);
        else
            ibLike.setImageResource(R.drawable.like2);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ComentariosActivity.class);
                intent.putExtra("idPost", itemLista.getId());
                v.getContext().startActivity(intent);
            }
        });

        ibLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Retrofit retrofit = ServicioApi.getServicoApi();
                PostInterface postService = retrofit.create(PostInterface.class);
                Call<Void> call;
                if (itemLista.isLiked())
                    call = postService.quitarLike(ManejadorSesion.getToken(), itemLista.getId());
                else
                    call = postService.darLike(ManejadorSesion.getToken(), itemLista.getId());

                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.code() == 200)
                            if (itemLista.isLiked()) {
                                ibLike.setImageResource(R.drawable.like2);
                                itemLista.setLiked(false);
                                itemLista.setLikes(itemLista.getLikes() - 1);
                            } else {
                                ibLike.setImageResource(R.drawable.like);
                                itemLista.setLiked(true);
                                itemLista.setLikes(itemLista.getLikes() + 1);
                            }
                        notifyItemChanged(position);
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(holder.itemView.getContext(), "No se pudo conectar al servidor", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return postLista.size();
    }
}
