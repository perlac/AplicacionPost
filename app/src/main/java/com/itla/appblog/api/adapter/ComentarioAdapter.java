package com.itla.appblog.api.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hootsuite.nachos.NachoTextView;
import com.hootsuite.nachos.terminator.ChipTerminatorHandler;
import com.itla.appblog.R;
import com.itla.appblog.api.Funciones;
import com.itla.appblog.api.modelos.Comment;
import com.itla.appblog.api.modelos.Post;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ComentarioAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Object> listaCometarios;

    public ComentarioAdapter(List<Object> listaCometarios) {
        this.listaCometarios = listaCometarios;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 1)
            return new RecyclerView.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_post_visualizar, parent, false)) {
            };
        else
            return new RecyclerView.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_comentario, parent, false)) {
            };
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (listaCometarios.get(position) instanceof Post) {
            Post post = (Post) listaCometarios.get(position);
            final TextView tvTitulo, tvCuerpo, tvCantLike, tvCantVista, tvNomUsuario, tvComentarios, tvFecha, tvEmail;
            NachoTextView etTags = holder.itemView.findViewById(R.id.ettags_post_full);

            tvTitulo = holder.itemView.findViewById(R.id.tvtitulo);
            tvCuerpo = holder.itemView.findViewById(R.id.tvcuerpo);
            tvCantLike = holder.itemView.findViewById(R.id.tvcantlike);
            tvCantVista = holder.itemView.findViewById(R.id.tvcantvista);
            tvNomUsuario = holder.itemView.findViewById(R.id.tvnomusuario);
            tvComentarios = holder.itemView.findViewById(R.id.tvcant_comentarios_post);
            tvEmail = holder.itemView.findViewById(R.id.tvemail_usuario_post);
            tvFecha = holder.itemView.findViewById(R.id.tvfecha_post);

            tvTitulo.setText(post.getTitle());
            tvCuerpo.setText(post.getBody());
            tvCantLike.setText(post.getLikes() + "");
            tvCantVista.setText(post.getViews() + "");
            tvNomUsuario.setText(post.getUserName());
            tvComentarios.setText(post.getComments());
            tvFecha.setText(Funciones.FormatoFecha(new Date(post.getCreatedAt())));
            tvEmail.setText(post.getUserEmail());

            List<String> tagList = new ArrayList<String>(Arrays.asList(post.getTags()));
            if (tagList.size() >= 5)
                etTags.setMinLines(3);

            etTags.setText(tagList);
            etTags.addChipTerminator('\n', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_ALL);
            etTags.setEnabled(false);
        } else {
            Comment cometario = (Comment) listaCometarios.get(position);

            TextView tvusuario = holder.itemView.findViewById(R.id.tvusuario_comentario);
            TextView tvbody = holder.itemView.findViewById(R.id.tvbody_coometario);
            TextView tvfecha = holder.itemView.findViewById(R.id.tvfecha_comentario);
            TextView tvEmail = holder.itemView.findViewById(R.id.tvemail_comentario);

            tvusuario.setText(cometario.getUserName());
            tvbody.setText(cometario.getBody());
            tvfecha.setText(Funciones.FormatoFecha(new Date(cometario.getCreatedAt())));
            tvEmail.setText(cometario.getUserEmail());
        }
    }

    @Override
    public int getItemCount() {
        return listaCometarios.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (listaCometarios.get(position) instanceof Post) {
            return 1;
        } else {
            return 2;
        }
    }
}
