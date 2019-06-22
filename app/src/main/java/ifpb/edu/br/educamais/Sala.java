package ifpb.edu.br.educamais;

import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.xwray.groupie.Group;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.ViewHolder;

import java.util.List;

import javax.annotation.Nullable;

public class Sala extends AppCompatActivity {

    private GroupAdapter adapter;
    private User user;
    private User me;
    private EditText editTextMemsagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sala);

        //Pegando o usuário
        user = (User) getIntent().getExtras().getParcelable("user");
        //passando o nome do usuário
        getSupportActionBar().setTitle(user.getUsername());

        RecyclerView rv = findViewById(R.id.recyclerChat);
        editTextMemsagem = findViewById(R.id.editTextChat);
        Button botaoChat = findViewById(R.id.buttonChat);

        botaoChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessenger();
            }
        });

        adapter = new GroupAdapter();
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        FirebaseFirestore.getInstance().collection("/users").document(FirebaseAuth.getInstance().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                         me = documentSnapshot.toObject(User.class);

                         exibirMensagemBalao();
                         
                    }
                });

    }

    private void exibirMensagemBalao() {
        if(me != null){
            String fromID = me.getUuid();
            String toId = user.getUuid();

            FirebaseFirestore.getInstance().collection("/conversas")
                    .document(fromID)
                    .collection(toId)
                    .orderBy("timestamp", Query.Direction.ASCENDING)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            List<DocumentChange> documentChanges = queryDocumentSnapshots.getDocumentChanges();

                            if(documentChanges != null){
                                for (DocumentChange doc: documentChanges) {

                                    if(doc.getType() == DocumentChange.Type.ADDED){
                                        Mensagem mensagem = doc.getDocument().toObject(Mensagem.class);
                                        adapter.add(new MessengerItem(mensagem));
                                    }
                                    
                                }
                            }
                        }
                    });
        }
    }

    private void sendMessenger() {

        String mensagem = editTextMemsagem.getText().toString();

        editTextMemsagem.setText(null);

        String fromId = FirebaseAuth.getInstance().getUid();
        String toId = user.getUuid();
        long timestamp = System.currentTimeMillis();

        Mensagem objmensagem = new Mensagem();
        objmensagem.setFromId(fromId);
        objmensagem.setToId(toId);
        objmensagem.setTimestamp(timestamp);
        objmensagem.setText(mensagem);

        if(!mensagem.isEmpty()){
            //enviando
            FirebaseFirestore.getInstance().collection("/conversas").document(fromId).collection(toId).add(objmensagem).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Log.d("teste", documentReference.getId());
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("teste", e.getMessage(), e);
                }
            });

            //recebendo
            FirebaseFirestore.getInstance().collection("/conversas").document(toId).collection(fromId).add(objmensagem).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Log.d("teste", documentReference.getId());
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("teste", e.getMessage(), e);
                }
            });
        }

    }

    private class MessengerItem extends Item<ViewHolder>{


        private final Mensagem mensagem;

        private MessengerItem(Mensagem mensagem) {
            this.mensagem = mensagem;
        }


        @Override
        public void bind(@NonNull ViewHolder viewHolder, int position) {
            TextView textView = viewHolder.itemView.findViewById(R.id.textViewMessenger);
            ImageView imageViewmessenger = viewHolder.itemView.findViewById(R.id.imageViewMessegerUser);

            textView.setText(mensagem.getText());
            Picasso.get().load(user.getProfileURL()).into(imageViewmessenger);
        }

        @Override
        public int getLayout() {
            return mensagem.getFromId().equals(FirebaseAuth.getInstance().getUid())
                    ? R.layout.item_from_messenger
                    : R.layout.item_to_messenger;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menucontato, menu);
        return true;
    }

    @Override
    //Escuta os eventos que ocorrem nos menus
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.contatos:
                Intent intent = new Intent(getApplicationContext(), Contatos.class);
                startActivity(intent);
                break;
            case R.id.sair:
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}
