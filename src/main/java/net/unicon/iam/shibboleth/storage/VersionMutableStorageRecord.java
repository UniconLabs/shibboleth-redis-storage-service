package net.unicon.iam.shibboleth.storage;

import org.opensaml.storage.MutableStorageRecord;

import java.io.Serializable;

public class VersionMutableStorageRecord extends MutableStorageRecord implements Serializable {
    public VersionMutableStorageRecord(String value, Long expiration, Long version) {
        super(value, expiration);
        super.setVersion(version);
    }
}
