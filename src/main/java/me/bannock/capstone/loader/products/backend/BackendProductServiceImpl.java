package me.bannock.capstone.loader.products.backend;

import me.bannock.capstone.loader.products.ProductDTO;
import me.bannock.capstone.loader.products.ProductService;

import java.io.File;
import java.util.List;

public class BackendProductServiceImpl implements ProductService {

    private final String apiKey = "BNOK_%%API_KEY%%";
    private final String authServerIp = "BNOK_%%AUTH_IP%%";

    @Override
    public List<ProductDTO> getOwnedProducts() throws RuntimeException {
        return null;
    }

    @Override
    public File downloadProduct() throws RuntimeException {
        return null;
    }

}
