package ifpb.edu.br.educamais;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.net.URI;
import java.util.UUID;

public class CadastrarUsuario extends AppCompatActivity {

    private EditText editNome;
    private EditText editEmail;
    private EditText editPassword;
    private Button buttonCadastrar;
    private ImageButton imageButtonFoto;
    private Uri uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_usuario);

        editNome = (EditText) findViewById(R.id.editTextName);
        editEmail = (EditText) findViewById(R.id.editTextEmail);
        editPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonCadastrar = (Button) findViewById(R.id.buttonCadastrar);
        imageButtonFoto = (ImageButton) findViewById(R.id.imageButtonFoto);

        buttonCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                criarUsuario();
            }
        });

        imageButtonFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selecionarFoto();
            }
        });
    }


    private void criarUsuario() {

        String nome = editNome.getText().toString();
        final String email = editEmail.getText().toString();
        String password = editPassword.getText().toString();

        if (nome.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Seu Nome, E-mail ou Senha devem ser preenchidos!!!", Toast.LENGTH_LONG).show();
        } else {

            //Chamada para cadastro do usuário de autenticação...
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(CadastrarUsuario.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //Testa a Task
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Usuário Criado com Sucesso", Toast.LENGTH_LONG).show();
                                buttonCadastrar.setEnabled(false);
                                buttonCadastrar.setBackgroundColor(R.drawable.botao_arredondado_vermelho);
                                //Salvar o usuário no firestore
                                salvarUsuario();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(email, "onComplete: Failed=" + e.getMessage());
                    Toast.makeText(getApplicationContext(), "Falha na criação do usuário (Verificar E-mail Válido) ou (Senha Menor que 6 Caracteres)", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void selecionarFoto() {
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        startActivityForResult(i, 0);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 0) {
            uri = data.getData();

            Bitmap bitmap = null;

            try{
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                imageButtonFoto.setImageDrawable(new BitmapDrawable(bitmap));
            }catch (IOException e){

            }
        }
    }

    //Grava a foto no fireStore
    private void salvarUsuario(){
        String arquivoID = UUID.randomUUID().toString();
        final StorageReference storageReference = FirebaseStorage.getInstance().getReference("/images" + arquivoID);
        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.i("URL pública da foto", uri.toString());

                        //Criando o usuário para salvar no banco de dados CloudFireStore - Coleção de Usuário

                        String uudi = FirebaseAuth.getInstance().getUid();
                        String username = editNome.getText().toString();
                        String profileURL = uri.toString();
                        User user = new User(uudi, username, profileURL);

                        FirebaseFirestore.getInstance().collection("users").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.i("Cadastrado Firecloud", documentReference.getId());
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.i("Erro Cadastro Firecloud", e.getMessage());
                            }
                        });

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
               Log.e("Erro ao gravar usuário", e.getMessage(), e);
            }
        });
    }
}
