package net.unicon.iam.shibboleth.storage.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.opensaml.storage.StorageRecord;

import java.io.IOException;

public class StorageRecordDeserializer extends StdDeserializer<StorageRecord> {
    public StorageRecordDeserializer() {
        this(null);
    }

    public StorageRecordDeserializer(Class<StorageRecord> t) {
        super(t);
    }

    @Override
    public StorageRecord deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = p.getCodec().readTree(p);
        long version = node.get("version").asLong();
        String value = node.get("value").asText();
        Long expiration = node.get("expiration").asLong();
        if (expiration == -1) {
            expiration = null;
        }
        VersionMutableStorageRecord storageRecord = new VersionMutableStorageRecord(value, expiration, version);
        return storageRecord;
    }
}
