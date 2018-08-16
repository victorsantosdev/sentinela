package app.victor.sentinela.imbituba.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class ImageHandling {

    /* victor: teste 2:ok app_imagens */
    public static String saveToTerrenosFolder(Context context, Bitmap bitmapImage, String foto_nome) {

        ContextWrapper cw = (ContextWrapper) context;

        // path to /data/data/yourapp/app_data/imageDir
        //o pŕoprio sistema de arquivos do android coloca o app_ na frente do nome da pasta
        File directory = cw.getDir("imagensterrenos", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory, foto_nome);
        Log.v("Salvando imagem", "salvando imagem em : " + mypath);

        FileOutputStream fos = null;
        try {

            fos = new FileOutputStream(mypath);

            // Use the compress method on the BitMap object to write image to
            // the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mypath.getAbsolutePath();
    }

    /* metodo para carregar a imagem do path */
    public static Bitmap loadImageFromTerrenosFolder(Context context, String img_name) {

        String newImageName = img_name.replaceAll("app_imagensterrenos/", "");
        Log.v("Nome da imagem a ser carregada", "nome da imagem a ser carregada: " + newImageName);
        ContextWrapper cw = (ContextWrapper) context;
        Bitmap b = null;
        try {
            // path to /data/data/yourapp/app_data/imageDir //o pŕoprio sistema de arquivos do android coloca o app_ na frente do nome da pasta
            File directory = cw.getDir("imagensterrenos", Context.MODE_PRIVATE);
            // Create imageDir
            File mypath = new File(directory, newImageName);
            Log.v("Carregando imagem", "carregando imagem de : " + mypath);
            InputStream is = new FileInputStream(mypath);
            b = BitmapFactory.decodeStream(new BufferedInputStream(is));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return b;
    }

    /* victor: teste 2:ok app_imagens */
    public static String saveToTempFolder(Context context, Bitmap bitmapImage, String foto_nome) {

        ContextWrapper cw = (ContextWrapper) context;

        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("temp", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory, foto_nome);
        Log.v("Salvando imagem", "salvando imagem em : " + mypath);

        FileOutputStream fos = null;
        try {

            fos = new FileOutputStream(mypath);

            // Use the compress method on the BitMap object to write image to
            // the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mypath.getAbsolutePath();
    }

    /* metodo para carregar a imagem do path */
    public static Bitmap loadImageFromTempFolder(Context context, String img_name) {

        String newImageName = img_name.replaceAll("app_imagensterrenos/", "");
        Log.v("Nome da imagem a ser carregada", "nome da imagem a ser carregada: " + newImageName);
        ContextWrapper cw = (ContextWrapper) context;
        Bitmap b = null;
        try {
            // path to /data/data/yourapp/app_data/imageDir
            File directory = cw.getDir("temp", Context.MODE_PRIVATE);
            // Create imageDir
            File mypath = new File(directory, newImageName);
            Log.v("Carregando imagem", "carregando imagem de : " + mypath);
            InputStream is = new FileInputStream(mypath);
            b = BitmapFactory.decodeStream(new BufferedInputStream(is));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return b;
    }

    public static String saveToInfracaoFolder(Context context, String foto_nome) {
        /* metodo para carregar a imagem do path */
        ContextWrapper cw = (ContextWrapper) context;

        Bitmap infracaoImage = loadImageFromTempFolder(cw, foto_nome);
        File directory = cw.getDir("infracoes", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory, foto_nome);
        Log.v("Salvando imagem", "salvando imagem em : " + mypath);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to
            // the OutputStream
            infracaoImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mypath.getAbsolutePath();
    }

    public static String saveToNotificacaoFolder(Context context, String foto_nome) {
        /* metodo para carregar a imagem do path */
        ContextWrapper cw = (ContextWrapper) context;

        Bitmap infracaoImage = loadImageFromTempFolder(cw, foto_nome);
        File directory = cw.getDir("notificacoes", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory, foto_nome);
        Log.v("Salvando imagem", "salvando imagem em : " + mypath);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to
            // the OutputStream
            infracaoImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mypath.getAbsolutePath();
    }
    

}
