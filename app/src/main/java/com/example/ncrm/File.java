package com.example.ncrm;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

/**
 * Created by shameer on 2018-03-10.
 */

public class File implements Serializable {
    private String fileName;
    private Long createdOn;
    private Long modifiedOn;
    private String type;
    private String userId;

    @Exclude
    private String id;

    public File(String fileName, Long createdOn, Long modifiedOn, String type, String userId) {
        this.fileName = fileName;
        this.createdOn = createdOn;
        this.modifiedOn = modifiedOn;
        this.type = type;
        this.userId = userId;
    }

    public File() {}

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Long createdOn) {
        this.createdOn = createdOn;
    }

    public Long getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Long modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
