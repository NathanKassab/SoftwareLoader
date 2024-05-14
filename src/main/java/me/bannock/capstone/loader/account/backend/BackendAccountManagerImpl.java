package me.bannock.capstone.loader.account.backend;

import com.google.gson.Gson;
import me.bannock.capstone.loader.account.AccountDTO;
import me.bannock.capstone.loader.account.AccountManager;
import me.bannock.capstone.loader.account.AccountManagerException;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Optional;

public class BackendAccountManagerImpl implements AccountManager {

    private final Logger logger = LogManager.getLogger();
    private final CloseableHttpClient client = HttpClientBuilder.create().disableCookieManagement().build();

//    private final String apiKey = "BNOK_%%API_KEY%%";
//    private final String uid = "BNOK_%%UID%%";
//    private final String serverIp = "BNOK_%%IP%%";
//    private final String protocol = "BNOK_%%PROTOCOL%%";

    private final String apiKey = "5bde0aa5-e477-4f6a-ab6e-277888f16504";
    private final String uid = "0";
    private final String serverIp = "localhost:8080";
    private final String protocol = "http";

    private AccountDTO user = null;

    @Override
    public void login(String hwid) throws AccountManagerException {
        HttpGet login = new HttpGet(String.format("%s://%s/api/accounts/1/login?hwid=%s", protocol, serverIp, hwid));
        addAuthHeaderInfo(login);
        login.addHeader("Accept", "application/json");

        try(CloseableHttpResponse loginResponse = client.execute(login)) {
            String content = EntityUtils.toString(loginResponse.getEntity());

            if (loginResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK)
                throw new AccountManagerException(content, uid, apiKey);

            // We now map the json response to an account dto
            this.user = new Gson().fromJson(content, AccountDTO.class);
            logger.info("Logged user in, user={}", user);

        } catch (IOException e) {
            logger.warn("Something went wrong while logging user into their account", e);
            throw new AccountManagerException(e.getMessage(), uid, apiKey);
        }
    }

    @Override
    public Optional<AccountDTO> getUser() {
        return Optional.of(user);
    }

    /**
     * Adds auth header information so the server accepts the request
     * @param request The request to add the header to
     */
    private void addAuthHeaderInfo(HttpRequestBase request){
        request.addHeader("Authorization", String.format("Bearer %s", apiKey));
    }

}
