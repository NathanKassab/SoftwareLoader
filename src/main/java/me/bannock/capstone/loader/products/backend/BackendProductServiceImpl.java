package me.bannock.capstone.loader.products.backend;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import me.bannock.capstone.loader.products.ProductDTO;
import me.bannock.capstone.loader.products.ProductService;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BackendProductServiceImpl implements ProductService {

    private final Logger logger = LogManager.getLogger();
    private final CloseableHttpClient client = HttpClientBuilder.create().disableCookieManagement().build();

//    private final String apiKey = "BNOK_%%API_KEY%%";
//    private final String serverIp = "BNOK_%%IP%%";
//    private final String protocol = "BNOK_%%PROTOCOL%%";

    private final String apiKey = "5bde0aa5-e477-4f6a-ab6e-277888f16504";
    private final String serverIp = "localhost:8080";
    private final String protocol = "http";

    @Override
    public List<ProductDTO> getOwnedProducts() throws RuntimeException {
        HttpGet getOwnedProducts = new HttpGet(String.format("%s://%s/api/products/1/getOwnedProducts", protocol, serverIp));
        addAuthHeaderInfo(getOwnedProducts);
        getOwnedProducts.addHeader("Accept", "application/json");

        try(CloseableHttpResponse loginResponse = client.execute(getOwnedProducts)) {
            String content = EntityUtils.toString(loginResponse.getEntity());

            if (loginResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK)
                throw new RuntimeException(content);

            Type productDtoListType = new TypeToken<ArrayList<ProductDTO>>() {}.getType();
            List<ProductDTO> products = new Gson().fromJson(content, productDtoListType);
            logger.info("Fetched user's products: {}", products);
            return products;
        } catch (IOException e) {
            logger.warn("Something went wrong while fetching the user's product list", e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public File downloadProduct() throws RuntimeException {
        return null;
    }

    /**
     * Adds auth header information so the server accepts the request
     * @param request The request to add the header to
     */
    private void addAuthHeaderInfo(HttpRequestBase request){
        request.addHeader("Authorization", String.format("Bearer %s", apiKey));
    }

}
