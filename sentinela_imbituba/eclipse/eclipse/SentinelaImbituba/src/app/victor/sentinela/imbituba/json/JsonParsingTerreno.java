package app.victor.sentinela.imbituba.json;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;
import app.victor.sentinela.imbituba.bdclasses.Terreno;
import com.google.gson.Gson;

public class JsonParsingTerreno {
    private static final String JPT_TAG = JsonParsingTerreno.class.getSimpleName();

    // static final String for parsed elements
    // Class elements
    public static final String TERRENOS_HEADER = "terrenos";
    
    // Terreno elements
    public static final String TERRENO_ID = "terreno_id";
    public static final String TERRENO_PROPRIETARIO = "proprietario";
    public static final String TERRENO_ENDERECO = "endereco";
    public static final String TERRENO_NUMERO = "numero";
    public static final String TERRENO_BAIRRO = "bairro";
    public static final String TERRENO_CIDADE = "cidade";
    public static final String TERRENO_ESTADO = "estado";
    public static final String TERRENO_TOPOGRAFIA = "topografia";
    public static final String TERRENO_AREA = "area";
    public static final String TERRENO_CONFIGURACAO = "configuracao";
    public static final String TERRENO_SITUACAO_CADASTRAL = "situacao_cadastral";
    public static final String TERRENO_FOTOPATH = "foto_path";

 // Native method for parsing json  terrenos file
    public static List<Terreno> NativeJsonParseMethodForTerrenos(String jsonString) {
        JSONObject jsonResponse;
        try {
            // Creating list object for storing the parsed students
            // padrão indicador do objeto terreno é prefixo t_
            List<Terreno> t_List = new ArrayList<Terreno>();

            jsonResponse = new JSONObject(jsonString);

            /*****
             * Returns the value mapped by name if it exists and is a JSONArray.
             ***/
            /******* Returns null otherwise. *******/
            JSONArray jsonMainNode = jsonResponse.optJSONArray(TERRENOS_HEADER);

            /*********** Process each JSON Node ************/
            int lengthJsonArr = jsonMainNode.length();

            for (int i = 0; i < lengthJsonArr; i++) {
                /****** Get Object for each JSON node. ***********/
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                addCurrentTerrenoToTerrenosListUsingGson(jsonChildNode, t_List);
            }
            /****************** End Parse Response JSON Data *************/
            return t_List;

            // *************
        } catch (Exception e) {
            Log.e("EXC", "Error: No Json string to parse", e);
            Log.d(JPT_TAG, "ERRO JSON PARSE TERRENO");
            return null;
        }
    }

    private static void addCurrentTerrenoToTerrenosListUsingGson(JSONObject currentTerreno, List<Terreno> t_List) {
        Gson gson = new Gson();
        Terreno terreno = gson.fromJson(currentTerreno.toString(), Terreno.class);
        t_List.add(terreno);
    }
}
