package resources.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class TipoDeDispositivoUtils {

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

    public static JsonObject criarPostTipoDeDispositivoComSucesso(){

        JsonObject tipoDeDispositivo = new JsonObject();

        tipoDeDispositivo.addProperty("name", "Eletrodomestico");
        tipoDeDispositivo.addProperty("description", "para tarefas de casa");
        tipoDeDispositivo.addProperty("attributes",criarAttributes().toString());

        return tipoDeDispositivo;
    }


    public static JsonObject criarPostTipoDeDispositivoVazio(){

        JsonObject tipoDeDispositivo = new JsonObject();

        tipoDeDispositivo.addProperty("name", "");
        tipoDeDispositivo.addProperty("description", "");


        return tipoDeDispositivo;
    }


    public static JsonObject criarPostTipoDeDispositivo2(){

        JsonObject tipoDeDispositivo = new JsonObject();

        tipoDeDispositivo.addProperty("name", "Decoração");
        tipoDeDispositivo.addProperty("description", "para decorar");
        tipoDeDispositivo.addProperty("attributes",criarAttributes().toString());

        return tipoDeDispositivo;
    }

}
