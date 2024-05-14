package me.bannock.capstone.loader.account.backend;

import me.bannock.capstone.loader.account.AccountDTO;
import me.bannock.capstone.loader.account.AccountService;

public class BackendAccountServiceImpl implements AccountService {

    private final String apiKey = "BNOK_%%API_KEY%%";
    private final String uid = "BNOK_%%UID%%";
    private final String authServerIp = "BNOK_%%AUTH_IP%%";

    @Override
    public AccountDTO login(String hwid) throws RuntimeException {
        return null;
    }

}
