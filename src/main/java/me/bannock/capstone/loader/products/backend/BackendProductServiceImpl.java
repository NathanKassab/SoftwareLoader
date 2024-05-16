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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArraySet;

public class BackendProductServiceImpl implements ProductService {

    public BackendProductServiceImpl(){
        downloadDir.mkdirs();
        workingDir.mkdirs();

        // We want to delete the downloads every time until product versioning is added
        File[] downloads = downloadDir.listFiles();
        if (downloads != null)
            Arrays.stream(downloads).forEach(File::delete);
    }

    private final Logger logger = LogManager.getLogger();
    private final CloseableHttpClient client = HttpClientBuilder.create().disableCookieManagement().build();
    private final File downloadDir = new File("downloads/");
    private final File workingDir = new File("working/");
    private final CopyOnWriteArraySet<Long> productsBeingDownloaded = new CopyOnWriteArraySet<>();

    private final String apiKey = "BNOK_%%API_KEY%%";
    private final String serverIp = "BNOK_%%IP%%";
    private final String protocol = "BNOK_%%PROTOCOL%%";

//    private final String apiKey = "5bde0aa5-e477-4f6a-ab6e-277888f16504";
//    private final String serverIp = "localhost:8080";
//    private final String protocol = "http";

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
    public File downloadProduct(long productId) throws RuntimeException {
        String downloadEndpoint = String.format("%s://%s/api/products/download/1/%s", protocol, serverIp, productId);
        HttpGet downloadProduct = new HttpGet(downloadEndpoint);
        addAuthHeaderInfo(downloadProduct);
        downloadProduct.addHeader("Accept", "application/octet-stream");

        productsBeingDownloaded.add(productId);
        try(CloseableHttpResponse downloadResponse = client.execute(downloadProduct)) {

            if (downloadResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK){
                String error = EntityUtils.toString(downloadResponse.getEntity());
                throw new RuntimeException(error);
            }

            File downloadLocation = getProductDownloadFile(productId);
            writeInputStreamToFile(downloadLocation, downloadResponse.getEntity().getContent());
            productsBeingDownloaded.remove(productId);
            logger.info("Downloaded product, productId={}, file={}", productId, downloadLocation);
            downloadLocation.deleteOnExit();
            return downloadLocation;
        } catch (IOException e) {
            productsBeingDownloaded.remove(productId);
            logger.warn("Something went wrong while downloading the product, productId={}", productId, e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public boolean isProductDownloaded(long productId) {
        return getProductDownloadFile(productId).exists();
    }

    @Override
    public boolean isProductDownloading(long productId) {
        return productsBeingDownloaded.contains(productId);
    }

    @Override
    public Process launchProduct(long productId, String... sysProperties) throws RuntimeException {
        File downloadFile = getProductDownloadFile(productId);
        if (!downloadFile.exists()){
            logger.error("Attempted to launch product where download file that does not exist, productId={}, file={}",
                    productId, downloadFile);
            throw new RuntimeException("Download file does not exist");
        }

        File productWorkingDir = new File(workingDir, productId + "/");
        if (!productWorkingDir.exists())
            productWorkingDir.mkdirs();

        String javaw = String.format("%s/bin/javaw", System.getProperty("java.home"));
        javaw = new File(javaw).getAbsolutePath();

        ArrayList<String> launchCmds = new ArrayList<>(Arrays.asList(javaw, "-jar", downloadFile.getAbsolutePath()));
        launchCmds.addAll(1, Arrays.asList(sysProperties));

        ProcessBuilder processBuilder = new ProcessBuilder(launchCmds);
        processBuilder.directory(productWorkingDir);
        try {
            Process process = processBuilder.start();
            logger.info("Launched product, productId={}, sysProps={}", productId, sysProperties);

            // We write over the file instead of deleting because the
            // JVM locks the file
            wipeFileLater(downloadFile, 10000);

            return process;
        } catch (IOException e) {
            logger.error("Something went wrong while launching product, productId={}, file={}",
                    productId, downloadFile, e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Waits a specific amount of time and then writes over the file
     * with 0 bytes
     * @param file The file to write over
     */
    private void wipeFileLater(File file, long delay){
        Timer wipeFileTimer = new Timer();
        TimerTask wipeFileTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    writeInputStreamToFile(file, new ByteArrayInputStream(new byte[]{}));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        wipeFileTimer.schedule(wipeFileTask, delay);
    }

    /**
     * Reads bytes from an input stream and writes them to a file
     * @param file The file to write to
     * @param input The input stream to read from
     * @throws IOException If something goes wrong while reading or writing
     */
    private void writeInputStreamToFile(File file, InputStream input) throws IOException {
        try (FileOutputStream downloadOutput = new FileOutputStream(file)){
            byte[] buffer = new byte[2048];
            int readBytes;
            while ((readBytes = input.read(buffer, 0, buffer.length)) != -1){
                downloadOutput.write(buffer, 0, readBytes);
            }
        }
    }

    /**
     * @param productId The product's id
     * @return The product's download file
     */
    private File getProductDownloadFile(long productId){
        return new File(downloadDir, String.format("%s", productId));
    }

    /**
     * Adds auth header information so the server accepts the request
     * @param request The request to add the header to
     */
    private void addAuthHeaderInfo(HttpRequestBase request){
        request.addHeader("Authorization", String.format("Bearer %s", apiKey));
    }

}
