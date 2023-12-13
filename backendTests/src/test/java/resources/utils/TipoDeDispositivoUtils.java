package resources.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class TipoDeDispositivoUtils {



    public static String criarPostTipoDeDispositivoComSucesso(){

       return "{"
                + "\"name\":\"Eletrodomestico\","
                + "\"description\":\"para tarefas de casa\","
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
                + "\"key\":\"\","
                + "\"value\":\"\""
                + "}}";
    }


    public static String criarPostTipoDeDispositivo2(){

        return "{"
                + "\"name\":\"Decoração\","
                + "\"description\":\"para decorar\","
                + "\"attributes\":{"
                + "\"key\":\"categoria\","
                + "\"value\":\"eletrodomestico\""
                + "}}";
    }

}
