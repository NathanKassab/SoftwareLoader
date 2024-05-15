package me.bannock.capstone.loader.ui.panes.product;

import me.bannock.capstone.loader.products.ProductService;
import me.bannock.capstone.loader.ui.UiUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.SwingWorker;
import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

public class ProductDownloadWorker extends SwingWorker<File, Object> {

    public ProductDownloadWorker(ProductService productService, long productId, Consumer<File> onDownloadComplete){
        this.productService = productService;
        this.productId = productId;
        this.onDownloadComplete = onDownloadComplete;
    }

    private final Logger logger = LogManager.getLogger();
    private final ProductService productService;
    private final long productId;
    private final Consumer<File> onDownloadComplete;

    @Override
    protected File doInBackground() throws Exception {
        return productService.downloadProduct(productId);
    }

    @Override
    protected void done() {
        try {
            onDownloadComplete.accept(get());
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Something went wrong while getting the download file", e);
            UiUtils.displayErrorDialog("Failed to download product", e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
