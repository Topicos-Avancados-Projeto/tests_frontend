package resources.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class GruposDeDispositivoUtils {



    public static String criarPostGrupoDeDispositivoComSucesso(){

        return "{"
                + "\"name\":\"Eletrodomestico\","
                + "\"description\":\"para tarefas de casa\","
                + "\"devices\":[\"1\",\"2\",\"3\"],"
                + "\"attributes\":{"
                + "\"key\":\"categoria\","
                + "\"value\":\"eletrodomestico\""
                + "}}";
    }


    public static String criarPostGrupoDeDispositivoAtualizado(){



        return "{"
                + "\"name\":\"Eletrodomesticos\","
                + "\"description\":\"para tarefas de casa e de empresas\","
                + "\"devices\":[\"1\",\"2\",\"3\"],"
                + "\"attributes\":{"
                + "\"key\":\"categoria\","
                + "\"value\":\"eletrodomestico\""
                + "}}";
    }
    public static String criarPostTipoDeDispositivoVazio(){

        return "{"
                + "\"name\":\"\","
                + "\"description\":\"\","
                + "\"attributes\":{"
                + "\"key\":\"categoria\","
                + "\"value\":\"eletrodomestico\""
                + "}}";
    }
}
