package ifpb.edu.br.educamais;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.OnItemClickListener;
import com.xwray.groupie.ViewHolder;

import java.util.List;

import javax.annotation.Nullable;

public class Contatos extends AppCompatActivity {

    private GroupAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contatos);

        RecyclerView recyclerView = findViewById(R.id.reciclerListaContatos);

         adapter = new GroupAdapter();
         recyclerView.setAdapter(adapter);
         recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        //criando eventos para a lista dos contatos
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull Item item, @NonNull View view) {
                Intent intent = new Intent(getApplicationContext(), Sala.class);

                //Criando o usuário da mensagem
                UserItem userItem = (UserItem) item;
                intent.putExtra("user", userItem.usuario);


                startActivity(intent);
            }
        });

        //Método que pega os usuários cadastrados no FireBase..
        buscarUsuariosFirebase();
    }

    private void buscarUsuariosFirebase(){
        FirebaseFirestore.getInstance().collection("/users").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.e("Erro firestore", e.getMessage());
                }
                List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot doc: docs) {
                    User usuario = doc.toObject(User.class);

                    adapter.add(new UserItem(usuario));


                }
            }
        });
    }

    private class UserItem extends Item<ViewHolder>{

        private final User usuario;

        private UserItem(User usuario) {
            this.usuario = usuario;
        }


        @Override
        public void bind(@NonNull ViewHolder viewHolder, int position) {
            TextView textViewNomeContato = viewHolder.itemView.findViewById(R.id.textViewNomeContato);
            ImageView imageViewFoto = viewHolder.itemView.findViewById(R.id.imageViewFoto);

            textViewNomeContato.setText(usuario.getUsername());

            Picasso.get().load(usuario.getProfileURL()).into(imageViewFoto);
        }

        @Override
        public int getLayout() {
            return R.layout.item_usuario;
        }
    }
}
