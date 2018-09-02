package app.victor.sentinela.foto;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import app.victor.sentinela.R;
import app.victor.sentinela.tabactivity.DetalhesTerrenoActivity;

public class FotoActivity extends Activity {

    // variables
    private static final int REQUEST_PICTURE = 1000;
    File picsDir;
    File picFile;
    Calendar c;
    SimpleDateFormat dateTime;
    String formatedDate;

    /**
     * View onde a foto tirada sera exibida
     */
    
    private ImageView imageView;

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fotoactivity_layout);
        this.imageView = (ImageView) findViewById(R.id.imagem);
        /**/

        String extPath = Environment.getExternalStorageDirectory().toString();
        picsDir = new File(extPath + "/sentinelaMobile/fotos");
        boolean success = false;
        if (!picsDir.exists()) {
            success = picsDir.mkdirs();
        }
        if (!success) {
            Log.d("DEBUG", "Erro na criacao da pasta " + picsDir);
        } else {
            Log.d("DEBUG", "Pasta " + picsDir + " criada com sucesso!");
        }

        /**/
        c = Calendar.getInstance();
        dateTime = new SimpleDateFormat("dd/MM/yyyy_HH:mm");
        formatedDate = dateTime.format(c.getTime());

        String picName = "obra_" + formatedDate + ".jpg";
        picFile = new File(picsDir, picName);

        Log.d("DEBUG", "foto_path: " + picFile.getAbsolutePath());
        Log.d("DEBUG", "foto_file: " + picFile.getAbsoluteFile());
        takePicture(this.imageView);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void takePicture(View v) {

        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(picFile));
        // Abre a aplicação de câmera
        startActivityForResult(i, REQUEST_PICTURE);
    }

    /**
     * Método chamado quando a aplicação nativa da câmera é finalizada
     */
    @SuppressLint("NewApi")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*
         * Verifica o código de requisição e se o resultado é OK (outro
         * resultado indica que// o usuário cancelou a tirada da foto)
         */

        if (requestCode == REQUEST_PICTURE) {
            if (resultCode == RESULT_OK) {
                FileInputStream fis = null;
                try {
                    try {
                        // Cria um FileInputStream para ler a foto tirada pela
                        // câmera
                        fis = new FileInputStream(picFile);
                        // Converte a stream em um objeto Bitmap
                        Bitmap picture = BitmapFactory.decodeStream(fis);
                        // Mostra o arquivo bitmap na view, para que o usuário
                        // veja a foto tirada
                        imageView.setRotation(180);
                        imageView.setImageBitmap(picture);
                                
                        /* salvar o bitmap */
                        FileOutputStream out = new FileOutputStream(picFile);
                        picture.compress(Bitmap.CompressFormat.JPEG, 90, out);
                        out.flush();
                        out.close();
                        Toast.makeText(FotoActivity.this, "Procedimento de fotografia concluido com sucesso.", Toast.LENGTH_SHORT).show();
                        
                        
//                        Log.d("DEBUG", "fotoPath " + infracao.get_fotoPath());
//                        // Intent intent1 = new Intent(this,
//                        // MainActivity.class);
//                        Intent intent1 = new Intent(this, RegistraLocalizacao.class);
//                        startActivity(intent1);
                    } finally {
                        if (fis != null) {
                            fis.close();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else if (resultCode == RESULT_CANCELED) {
                // User cancelled the image capture
                Toast.makeText(FotoActivity.this, "Interfaceamento com a camera cancelado.", Toast.LENGTH_SHORT).show();
                Intent goDetalhesTerreno = new Intent(this, DetalhesTerrenoActivity.class);
                startActivity(goDetalhesTerreno);

            } else {
                // Image capture failed, advise user
                Toast.makeText(FotoActivity.this, "Ocorreu um erro na interface com a camera do dispositivo.", Toast.LENGTH_SHORT).show();
                Intent goDetalhesTerreno = new Intent(this, DetalhesTerrenoActivity.class);
                startActivity(goDetalhesTerreno);
            }
        }
    }
}
