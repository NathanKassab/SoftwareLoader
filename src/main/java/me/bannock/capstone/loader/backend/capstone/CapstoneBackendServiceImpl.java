package me.bannock.capstone.loader.backend.capstone;

import com.google.inject.Inject;
import me.bannock.capstone.loader.backend.AccountDTO;
import me.bannock.capstone.loader.backend.BackendService;
import me.bannock.capstone.loader.backend.ProductDTO;

import java.io.File;
import java.util.List;

public class CapstoneBackendServiceImpl implements BackendService {

    private final String apiKey = "BNOK_%%API_KEY%%";
    private final String uid = "BNOK_%%UID%%";
    private final String authServerIp = "BNOK_%%AUTH_IP%%";

    @Override
    public AccountDTO login(String hwid) throws RuntimeException {
        return null;
    }

    @Override
    public List<ProductDTO> getOwnedProducts() throws RuntimeException {
        return null;
    }

    @Override
    public File downloadProduct() throws RuntimeException {
        return null;
    }

}
