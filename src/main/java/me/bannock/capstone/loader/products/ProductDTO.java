package me.bannock.capstone.loader.products;

public class ProductDTO {

    public ProductDTO(long id, String name, String iconUrl, String description, long ownerUid) {
        this.id = id;
        this.name = name;
        this.iconUrl = iconUrl;
        this.description = description;
        this.ownerUid = ownerUid;
    }

    private final long id;
    private final String name, iconUrl, description;
    private final long ownerUid;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public String getDescription() {
        return description;
    }

    public long getOwnerUid() {
        return ownerUid;
    }

    @Override
    public String toString() {
        return "ProductDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                ", description='" + description + '\'' +
                ", ownerUid=" + ownerUid +
                '}';
    }

}
