package me.bannock.capstone.loader.products;

public class ProductDTO {

    public ProductDTO(long id, String name,
                      String iconUrl, String description,
                      long ownerUid, boolean approved,
                      double price) {
        this.id = id;
        this.name = name;
        this.iconUrl = iconUrl;
        this.description = description;
        this.ownerUid = ownerUid;
        this.approved = approved;
        this.price = price;
    }

    public ProductDTO(){

    }

    private long id;
    private String name, iconUrl, description;
    private long ownerUid;
    private boolean approved;
    private double price;

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

    public boolean isApproved() {
        return approved;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "ProductDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                ", description='" + description + '\'' +
                ", ownerUid=" + ownerUid +
                ", approved=" + approved +
                ", price=" + price +
                '}';
    }

}
