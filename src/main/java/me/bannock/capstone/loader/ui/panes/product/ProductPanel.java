package me.bannock.capstone.loader.ui.panes.product;

import com.google.gson.Gson;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import me.bannock.capstone.loader.account.AccountDTO;
import me.bannock.capstone.loader.account.AccountManager;
import me.bannock.capstone.loader.products.ProductDTO;
import me.bannock.capstone.loader.products.ProductService;
import me.bannock.capstone.loader.security.SecurityManager;
import me.bannock.capstone.loader.ui.UiUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.Window;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Optional;

public class ProductPanel extends JPanel {

    @AssistedInject
    public ProductPanel(@Assisted ProductDTO product, ProductService productService,
                        AccountManager accountManager, SecurityManager securityManager){
        this.product = product;
        this.productService = productService;
        this.accountManager = accountManager;
        this.securityManager = securityManager;

        create();
    }

    private final Logger logger = LogManager.getLogger();
    private final ProductDTO product;
    private final ProductService productService;
    private final AccountManager accountManager;
    private final SecurityManager securityManager;

    private JPanel productOptionPane;

    /**
     * Styles and populates this pane
     */
    private void create(){
        setDoubleBuffered(true);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(5, 0, 5, 0));

        JPanel productDisplayPane = buildProductDisplayPane();
        add(productDisplayPane, BorderLayout.CENTER);

        productOptionPane = buildProductOptionPane();
        add(productOptionPane, BorderLayout.EAST);
    }

    /**
     * The product option pane is shown next to the product display pane.
     * It hosts all the ways that the user can interact with their product
     * @return The built product option pane
     */
    private JPanel buildProductOptionPane(){
        JPanel productOptionPane = new JPanel();

        if (!product.isApproved()){
            productOptionPane.add(new JLabel("Unavailable"));
        }
        else if (productService.isProductDownloaded(product.getId())){
            productOptionPane.add(createLaunchButton());
        }
        else if (productService.isProductDownloading(product.getId())){
            productOptionPane.add(new JLabel("Downloading..."));
        }
        else{
            JButton downloadButton = new JButton("Download");
            downloadButton.addActionListener(evt -> {
                if (productService.isProductDownloading(product.getId())){
                    refreshOptionPane();
                    UiUtils.displayErrorDialog("Already downloading", "This product is already downloading");
                    return;
                }
                new ProductDownloadWorker(productService, product.getId(), file -> refreshOptionPane()).execute();
                SwingUtilities.invokeLater(this::refreshOptionPane);
            });
            productOptionPane.add(downloadButton);
        }

        return productOptionPane;
    }

    private JButton createLaunchButton(){
        JButton launchButton = new JButton("Launch");
        launchButton.addActionListener(evt -> {
            try{
                Optional<AccountDTO> user = accountManager.getUser();
                if (!user.isPresent()){
                    logger.error("Failed to launch product because user is not logged in");
                    return;
                }

                String userProp = String.format("-DbnokUser=%s", new Gson().toJson(user.get()));
                String disableAttach1 = "-XX:+DisableAttachMechanism";
                String disableAttach2 = "-Dcom.ibm.tools.attach.enable=no";
                String noVerify = "-noverify";
                Process productProcess = productService.launchProduct(
                        product.getId(), userProp, disableAttach1, disableAttach2, noVerify
                );
                securityManager.protectProcess(productProcess);
                Arrays.stream(JFrame.getFrames()).forEach(Window::dispose);
            }catch (RuntimeException e){
                refreshOptionPane();
                logger.error("Something went wrong while launching the product, product={}", product, e);
                UiUtils.displayErrorDialog("Unable to launch product", e.getMessage());
            }
        });
        return launchButton;
    }

    /**
     * Refreshes the product option pane
     */
    private void refreshOptionPane(){
        remove(productOptionPane);
        productOptionPane = buildProductOptionPane();
        add(productOptionPane, BorderLayout.EAST);
        revalidate();
        repaint();
    }

    /**
     * The product display pane is the main part of this panel. It shows
     * information such as the product name, icon, description, etc
     * @return The built product display pane
     */
    private JPanel buildProductDisplayPane(){
        JPanel productDisplayPaneContainer = new JPanel();
        productDisplayPaneContainer.setLayout(new BorderLayout());
        productDisplayPaneContainer.setBorder(new EmptyBorder(0, 8, 0, 0));

        JPanel productDisplayPane = new JPanel();
        productDisplayPane.setLayout(new BoxLayout(productDisplayPane, BoxLayout.Y_AXIS));
        String productNameHtml = String.format("<html><h3>%s</h3><html>", product.getName());
        JLabel productName = new JLabel(productNameHtml);
        try {
            int fontSize = productName.getFont().getSize() * 3;
            Image buttonIcon = UiUtils.createScaledImage(new URL(product.getIconUrl()), fontSize);
            productName.setIcon(new ImageIcon(buttonIcon));
        } catch (IOException e) {
            logger.warn("Unable to load icon for product, iconUrl={}", product.getIconUrl(), e);
        }
        productDisplayPane.add(productName);
        productDisplayPane.add(new JLabel("$" + product.getPrice()));

        JPanel descriptionContainer = new JPanel();
        descriptionContainer.setLayout(new BorderLayout());
        descriptionContainer.add(new JLabel(String.format("<html>%s<html>", product.getDescription())), BorderLayout.NORTH);

        productDisplayPaneContainer.add(productDisplayPane, BorderLayout.NORTH);
        productDisplayPaneContainer.add(descriptionContainer, BorderLayout.CENTER);

        return productDisplayPaneContainer;
    }

}
