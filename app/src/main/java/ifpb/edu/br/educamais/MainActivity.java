package ifpb.edu.br.educamais;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;

public class MainActivity extends AppCompatActivity {

    private EditText editEmail;
    private EditText editPassword;
    private Button buttonEntrar;
    private Button buttonCadastrarUsuario;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editEmail = (EditText) findViewById(R.id.editTextEmail);
        editPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonEntrar = (Button) findViewById(R.id.buttonEntrar);
        buttonCadastrarUsuario = (Button) findViewById(R.id.buttonCadastrarUsuario);

        buttonEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = editEmail.getText().toString();
                String password = editPassword.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "E-mail ou Senha devem ser preenchidos!!!", Toast.LENGTH_LONG).show();
                    return;
                }

                try {
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Usuário autenticado!", Toast.LENGTH_LONG).show();
                                Log.i("Autenticando", task.getResult().getUser().getUid());

                                Intent i = new Intent(getApplicationContext(), Menu.class);
                                startActivity(i);
                            }else{
                                String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(getApplicationContext(), "Usuário Não Cdastrado!",Toast.LENGTH_LONG).show();
                        }
                    });
                }catch (Exception e) {

                }

            }
        });

        buttonCadastrarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), CadastrarUsuario.class);
                startActivity(i);
            }
        });

    }
}
