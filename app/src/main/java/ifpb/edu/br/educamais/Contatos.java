package ifpb.edu.br.educamais;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import javax.annotation.Nullable;

public class Contatos extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contatos);

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
                    Log.d("usuario", usuario.getUsername());
                }
            }
        });
    }
}
