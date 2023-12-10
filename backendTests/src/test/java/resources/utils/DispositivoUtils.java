package resources.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class DispositivoUtils {

    public static JsonObject criarPostDispositivo(){
        JsonObject dispositivo = new JsonObject();

        dispositivo.addProperty("name", "Geladeira");
        dispositivo.addProperty("description", "A beautiful dispositivo");
        dispositivo.addProperty("group", "Eletrodomestivo");
        dispositivo.addProperty("topics", 1234);
        dispositivo.addProperty("type", 1);
        dispositivo.addProperty("attributes",criarAttributes().toString());


        return dispositivo;
    }

    static JsonArray criarAttributes(){
        JsonArray attributesArray = new JsonArray();
        JsonObject attributeObject = new JsonObject();

        JsonObject keyObject = new JsonObject();

        keyObject.addProperty("type", "string");
        keyObject.addProperty("value", "Categoria");

        JsonObject valueObject = new JsonObject();
        valueObject.addProperty("type", "string");
        valueObject.addProperty("value", "Cozinha");

        attributeObject.addProperty("key", keyObject.toString());
        attributeObject.addProperty("value", valueObject.toString());

        attributesArray.add(attributeObject);
        return attributesArray;
    }
    public static JsonObject criarPostDispositivoComConflito(){
        JsonObject dispositivo = new JsonObject();

        dispositivo.addProperty("name", "Geladeira");
        dispositivo.addProperty("description", "A beautiful dispositivo");
        dispositivo.addProperty("group", "Eletrodomestivo");
        dispositivo.addProperty("topics", 1234);
        dispositivo.addProperty("type", 1);
        dispositivo.addProperty("attributes",criarAttributes().toString());


        return dispositivo;
    }
    public static JsonObject criarPostDispositivoAtualizado(){
        JsonObject dispositivo = new JsonObject();

        dispositivo.addProperty("name", "Geladeira");
        dispositivo.addProperty("description", "Um dispositivo bonito");
        dispositivo.addProperty("group", "Eletrodomestivo");
        dispositivo.addProperty("topics", 1234);
        dispositivo.addProperty("type", 1);
        dispositivo.addProperty("attributes",criarAttributes().toString());


        return dispositivo;
    }

    public static JsonObject criarPostDispositivoComProblemaDeValidacao(){
        JsonObject dispositivo = new JsonObject();

        dispositivo.addProperty("name", "");
        dispositivo.addProperty("description", "");
        dispositivo.addProperty("group", "");


        return dispositivo;
    }
}

