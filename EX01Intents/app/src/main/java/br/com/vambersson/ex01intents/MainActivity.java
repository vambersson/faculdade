package br.com.vambersson.ex01intents;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.net.URL;

public class MainActivity extends ListActivity {


    private static final String[] OPCOES = {
            "Broser",
            "Realizando uma chamada",
            "Mapa",
            "Tocar música",
            "SMS",
            "Compartinhar",
            "Minha acção 1",
            "Minha acção 2",
            "Sair"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,OPCOES);
        setListAdapter(adapter);

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Uri uri = null;
        Intent it = null;

        switch(position){
            case 0:
                //Abre uma URL
                uri = Uri.parse("https://www.google.com.br/");
                it = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(it);
                break;
            case 1:
                //Realiza chamada
                uri = uri.parse("tel:997139524");
                it = new Intent(Intent.ACTION_DIAL,uri);
                startActivity(it);
                break;
            case 2:
                //Pesquisa uma posição no mapa
                //Seu ADV deve esta usando Google APIs!
                uri = Uri.parse("geo:0,0?q=Rua+Amelia,Recife");
                it = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(it);
                break;
            case 3:
                //Executa uma música do SDcard
                uri = Uri.parse("file:///mnt/sdcard/musica.mp3");
                it = new Intent();
                it.setAction(Intent.ACTION_VIEW);
                it.setDataAndType(uri,"audio/mp3");
                startActivity(it);
                break;
            case 4:
                //Abre o editor do SMS
                uri = Uri.parse("sms:12345");
                it = new Intent(Intent.ACTION_VIEW,uri);
                it.putExtra("sms_body","Corpo do SMS");
                startActivity(it);
                break;
            case 5:
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT,"Compartilhado via Intent");
                intent.setType("text/plain");
                startActivity(intent);
                break;
            case 6:
                it = new Intent("dominando.android.ACAO_CUSTOMIZADA");
                startActivity(it);
                break;
            case 7:
                uri = Uri.parse("produto://Notebook/Slim");
                it = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(it);
                break;
            default:
                finish();

        }



    }
}
