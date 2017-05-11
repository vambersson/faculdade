package br.com.vambersson.projetofaculdade.activity.logincadastro;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import br.com.vambersson.projetofaculdade.R;

public class ActivityCadLogin extends AppCompatActivity {

    private static final int TIRAR_FOTO = 100;


    private ImageView login_cad_IdimageView;
    private FloatingActionButton login_Cad_IdCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cad_login);

        login_cad_IdimageView = (ImageView) findViewById(R.id.login_cad_IdimageView);

        login_Cad_IdCamera = (FloatingActionButton) findViewById(R.id.login_Cad_IdCamera);
        login_Cad_IdCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chamarCamera();
            }
        });

    }


    private void chamarCamera(){
        Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (it.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(it, TIRAR_FOTO);
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TIRAR_FOTO) {
            if (resultCode == RESULT_OK) {
                if(data != null) {
                    Bundle bundle = data.getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");
                    login_cad_IdimageView.setImageBitmap(bitmap);

                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(getBaseContext(), "A captura foi cancelada",
                            Toast.LENGTH_SHORT);
                } else {
                    Toast.makeText(getBaseContext(), "A câmera foi fechada",
                            Toast.LENGTH_SHORT);
                }
            }
        }

    }
}
