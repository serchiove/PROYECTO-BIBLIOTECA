package com.biblioteca.multimedia;

import com.google.gson.*;
import java.lang.reflect.Type;

public class MultimediaAdapter implements JsonDeserializer<Multimedia>, JsonSerializer<Multimedia> {
    @Override
    public Multimedia deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        String tipo = obj.get("tipo").getAsString();
        switch (tipo) {
            case "LibroDigital":
                return context.deserialize(json, LibroDigital.class);
            // Agrega aquí los demás tipos si los tienes: Video, Audio, etc.
            default:
                throw new JsonParseException("Tipo de recurso desconocido: " + tipo);
        }
    }

    @Override
    public JsonElement serialize(Multimedia src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObj = (JsonObject) context.serialize(src, src.getClass());
        jsonObj.addProperty("tipo", src.getClass().getSimpleName());
        return jsonObj;
    }
}

