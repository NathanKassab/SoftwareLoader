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
     * @return The file that the product was written to
     * @throws RuntimeException If something goes wrong while downloading the product
     */
    File downloadProduct() throws RuntimeException;

}
