package ifpb.edu.br.educamais;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class Menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        ImageButton imageButtonsala = (ImageButton) findViewById(R.id.imageButtonSala);
        ImageButton imageButtonconteudo = (ImageButton) findViewById(R.id.imageButtonConteudo);

        imageButtonsala.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Sala.class);
                startActivity(i);
            }
        });

        imageButtonconteudo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), OrganizadorConteudo.class);
                startActivity(i);
            }
        });


    }
}
