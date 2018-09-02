package app.victor.sentinela.json;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import com.google.gson.Gson;
import android.util.Log;
import app.victor.sentinela.bdclasses.Usuario;

public class JsonParsingUsuario {
    public static final String USUARIOS_HEADER = "usuarios";
    private static final String JPU_TAG = JsonParsingUsuario.class.getSimpleName();

    // usuario elements
    public static final String USUARIO_ID = "usuario_id";
    public static final String USUARIO_NOME = "nome";
    public static final String USUARIO_SENHA = "senha";
    
 // Native method for parsing json  usuarios file
    public static List<Usuario> NativeJsonParseMethodForUsuarios(String jsonString) {
        JSONObject jsonResponse;
        try {
            // Creating list object for storing the parsed students
            // padrão indicador do objeto multa é prefixo m_
            List<Usuario> u_List = new ArrayList<Usuario>();

            jsonResponse = new JSONObject(jsonString);

            /*****
             * Returns the value mapped by name if it exists and is a JSONArray.
             ***/
            /******* Returns null otherwise. *******/
            JSONArray jsonMainNode = jsonResponse.optJSONArray(USUARIOS_HEADER);

            /*********** Process each JSON Node ************/
            int lengthJsonArr = jsonMainNode.length();

            for (int i = 0; i < lengthJsonArr; i++) {
                /****** Get Object for each JSON node. ***********/
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                addCurrentUsuarioToUsuariosListUsingGson(jsonChildNode, u_List);
            }
            /****************** End Parse Response JSON Data *************/
            return u_List;

            // *************
        } catch (Exception e) {
            Log.e("EXC", "Error: No Json string to parse", e);
            Log.d(JPU_TAG, "ERRO JSON PARSE USUARIO");
            return null;
        }
    }

    private static void addCurrentUsuarioToUsuariosListUsingGson(JSONObject currentUsuario, List<Usuario> u_List) {
        Gson gson = new Gson();
        Usuario usuario = gson.fromJson(currentUsuario.toString(), Usuario.class);
        u_List.add(usuario);
    }
    
}
