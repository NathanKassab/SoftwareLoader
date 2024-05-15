package me.bannock.capstone.loader.products;

import java.io.File;
import java.util.List;

public interface ProductService {

    /**
     * Grabs a list of all the products that the user currently owns
     * @return The list of owned products
     * @throws RuntimeException If something goes wrong while grabbing the products
     */
    List<ProductDTO> getOwnedProducts() throws RuntimeException;

    /**
     * Downloads the product file
     * @param productId The id of the product to download
     * @return The file that the product was written to
     * @throws RuntimeException If something goes wrong while downloading the product
     */
    File downloadProduct(long productId) throws RuntimeException;

    /**
     * @param productId The product's id
     * @return Whether the product's file is downloaded
     */
    boolean isProductDownloaded(long productId);

    /**
     * @param productId The product's id
     * @return Whether the product is currently being downloaded
     */
    boolean isProductDownloading(long productId);

    /**
     * Launches a product
     * @param productId The product's id
     * @throws RuntimeException If something goes wrong while launching the product
     */
    void launchProduct(long productId) throws RuntimeException;

}
