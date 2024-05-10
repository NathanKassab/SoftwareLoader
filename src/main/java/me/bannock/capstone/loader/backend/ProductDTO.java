package me.bannock.capstone.loader.backend;

public class ProductDTO {

    public ProductDTO(long id, String name, String version){
        this.id = id;
        this.name = name;
        this.version = version;
    }

    private final long id;
    private final String name, version;

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return "ProductDTO{" +
                "name='" + name + '\'' +
                ", version='" + version + '\'' +
                '}';
    }

}
