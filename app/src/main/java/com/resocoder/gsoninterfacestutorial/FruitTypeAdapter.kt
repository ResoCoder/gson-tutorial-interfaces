package com.resocoder.gsoninterfacestutorial

import com.google.gson.*
import java.lang.reflect.Type

private const val CLASSNAME = "CLASSNAME"
private const val DATA = "DATA"

class FruitTypeAdapter
    : JsonSerializer<Fruit>, JsonDeserializer<Fruit> {
    override fun serialize(src: Fruit?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return JsonObject().apply {
            addProperty(CLASSNAME, src?.javaClass?.name)
            add(DATA, context?.serialize(src))
        }
    }

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Fruit {
        val jsonObject = json?.asJsonObject
        val className = jsonObject?.get(CLASSNAME)?.asString
        val clazz = getObjectClass(className)
        return context!!.deserialize(jsonObject?.get(DATA), clazz)
    }

    private fun getObjectClass(className: String?): Class<*> {
        try {
            return Class.forName(className)
        } catch (e: ClassNotFoundException) {
            throw JsonParseException(e)
        }
    }
}