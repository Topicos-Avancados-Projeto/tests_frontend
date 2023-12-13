package resources.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class DispositivoUtils {

    public static String criarPostDispositivo(){


        return "{"
                + "\"name\":\"Geladeira\","
                + "\"description\":\"A beautiful dispositivo\","
                + "\"group\":\"ce83d5b7-9c29-4e99-af33-38fe4f538c53\","
                + "\"topics\":\"ce83d5b7-9c29-4e99-af33-38fe4f538c53\","
                + "\"type\":\"ce83d5b7-9c29-4e99-af33-38fe4f538c53\","
                + "\"attributes\":{"
                + "\"key\":\"categoria\","
                + "\"value\":\"eletrodomestico\""
                + "}}";
    }


    public static String criarPostDispositivoComConflito(){

        return "{"
                + "\"name\":\"\","
                + "\"description\":\"\","
                + "\"group\":\"\","
                + "\"topics\":\"ce83d5b7-9c29-4e99-af33-38fe4f538c53\","
                + "\"type\":\"ce83d5b7-9c29-4e99-af33-38fe4f538c53\","
                + "\"attributes\":{"
                + "\"key\":\"categoria\","
                + "\"value\":\"eletrodomestico\""
                + "}}";
    }
    public static String criarPostDispositivoAtualizado(){

        return "{"
                + "\"name\":\"Frigorifico\","
                + "\"description\":\"A beautiful dispositivo\","
                + "\"group\":\"ce83d5b7-9c29-4e99-af33-38fe4f538c53\","
                + "\"topics\":\"ce83d5b7-9c29-4e99-af33-38fe4f538c53\","
                + "\"type\":\"ce83d5b7-9c29-4e99-af33-38fe4f538c53\","
                + "\"attributes\":{"
                + "\"key\":\"categoria\","
                + "\"value\":\"eletrodomestico\""
                + "}}";
    }

    public static JsonObject criarPostDispositivoComProblemaDeValidacao(){
        JsonObject dispositivo = new JsonObject();

        dispositivo.addProperty("name", "");
        dispositivo.addProperty("description", "");
        dispositivo.addProperty("group", "");


        return dispositivo;
    }
}

