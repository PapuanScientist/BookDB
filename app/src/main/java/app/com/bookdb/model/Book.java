package app.com.bookdb.model;

public class Book {

    private String createdAt;
    private String createdByFullname;
    private String createdById;
    private String createdByUsername;

    private String deletedAt;
    private String description;
    private String id;
    private String name;

    private String modifiedAt;
    private String modifiedByFullname;
    private String modifiedById;
    private String modifiedByUsername;

    public Book(){}
    public Book(String createdAt, String createdByFullname, String createdById, String createdByUsername, String deletedAt, String description, String id, String name, String modifiedAt, String modifiedByFullname, String modifiedById, String modifiedByUsername) {
        this.createdAt = createdAt;
        this.createdByFullname = createdByFullname;
        this.createdById = createdById;
        this.createdByUsername = createdByUsername;
        this.deletedAt = deletedAt;
        this.description = description;
        this.id = id;
        this.name = name;
        this.modifiedAt = modifiedAt;
        this.modifiedByFullname = modifiedByFullname;
        this.modifiedById = modifiedById;
        this.modifiedByUsername = modifiedByUsername;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedByFullname() {
        return createdByFullname;
    }

    public void setCreatedByFullname(String createdByFullname) {
        this.createdByFullname = createdByFullname;
    }

    public String getCreatedById() {
        return createdById;
    }

    public void setCreatedById(String createdById) {
        this.createdById = createdById;
    }

    public String getCreatedByUsername() {
        return createdByUsername;
    }

    public void setCreatedByUsername(String createdByUsername) {
        this.createdByUsername = createdByUsername;
    }

    public String getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(String deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(String modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public String getModifiedByFullname() {
        return modifiedByFullname;
    }

    public void setModifiedByFullname(String modifiedByFullname) {
        this.modifiedByFullname = modifiedByFullname;
    }

    public String getModifiedById() {
        return modifiedById;
    }

    public void setModifiedById(String modifiedById) {
        this.modifiedById = modifiedById;
    }

    public String getModifiedByUsername() {
        return modifiedByUsername;
    }

    public void setModifiedByUsername(String modifiedByUsername) {
        this.modifiedByUsername = modifiedByUsername;
    }
}
