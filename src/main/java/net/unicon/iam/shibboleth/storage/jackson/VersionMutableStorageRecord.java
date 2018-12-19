package net.unicon.iam.shibboleth.storage.jackson;

import org.opensaml.storage.MutableStorageRecord;

public class VersionMutableStorageRecord extends MutableStorageRecord {
    public VersionMutableStorageRecord(String value, Long expiration, Long version) {
        super(value, expiration);
        super.setVersion(version);
    }
}
