package net.unicon.iam.shibboleth.storage.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.opensaml.storage.StorageRecord;

import java.io.IOException;

public class StorageRecordSerializer extends StdSerializer<StorageRecord> {
    public StorageRecordSerializer() {
        this(null);
    }

    public StorageRecordSerializer(Class<StorageRecord> t) {
        super(t);
    }

    @Override
    public void serialize(StorageRecord value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("version", value.getVersion());
        gen.writeStringField("value", value.getValue());
        gen.writeNumberField("version", value.getExpiration() != null ? value.getVersion() : -1);
        gen.writeEndObject();
    }

    @Override
    public void serializeWithType(StorageRecord value, JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer) throws IOException {
        typeSer.writeTypePrefix(gen, typeSer.typeId(value, JsonToken.START_OBJECT));
        gen.writeNumberField("version", value.getVersion());
        gen.writeStringField("value", value.getValue());
        gen.writeNumberField("version", value.getExpiration() != null ? value.getVersion() : -1);
        typeSer.writeTypeSuffix(gen, typeSer.typeId(value, JsonToken.END_OBJECT));
    }
}
