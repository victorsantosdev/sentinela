package app.victor.sentinela.imbituba.json;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import com.google.gson.Gson;
import android.util.Log;
import app.victor.sentinela.imbituba.bdclasses.CodigoLei;

public class JsonParsingCodigoLei {
    private static final String JPCL_TAG = JsonParsingCodigoLei.class.getSimpleName();

    public static final String CODIGOSLEI_HEADER = "codigoslei";

    // CodigoMulta elements
    public static final String CODIGOLEI_ID = "codigolei_id";
    public static final String CODIGOLEI_CODIGO = "codigo";
    public static final String CODIGOLEI_DESCRICAO = "descricao";
    public static final String CODIGOLEI_VALORINFRACAO = "valorinfracao";    
    
    
    // Native method for parsing json  multas file
       public static List<CodigoLei> NativeJsonParseMethodForCodigosInfracoes(String jsonString) {
           JSONObject jsonResponse;
           try {
               // Creating list object for storing the parsed students
               // padrão indicador do objeto multa é prefixo m_
               List<CodigoLei> ci_List = new ArrayList<CodigoLei>();

               jsonResponse = new JSONObject(jsonString);

               /*****
                * Returns the value mapped by name if it exists and is a JSONArray.
                ***/
               /******* Returns null otherwise. *******/
               JSONArray jsonMainNode = jsonResponse.optJSONArray(CODIGOSLEI_HEADER);

               /*********** Process each JSON Node ************/
               int lengthJsonArr = jsonMainNode.length();

               for (int i = 0; i < lengthJsonArr; i++) {
                   /****** Get Object for each JSON node. ***********/
                   JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                   addCurrentCodigoLeiToCodigoLeiListUsingGson(jsonChildNode, ci_List);
               }
               /****************** End Parse Response JSON Data *************/
               return ci_List;

               // *************
           } catch (Exception e) {
               Log.e("EXC", "Error: No Json string to parse", e);
               Log.d(JPCL_TAG, "ERRO JSON PARSE CODIGOS LEI");
               return null;
           }
       }

       private static void addCurrentCodigoLeiToCodigoLeiListUsingGson(JSONObject currentCodigoLei, List<CodigoLei> cl_List) {
           Gson gson = new Gson();
           CodigoLei codigoInfracao = gson.fromJson(currentCodigoLei.toString(), CodigoLei.class);
           cl_List.add(codigoInfracao);
       }
    
}
