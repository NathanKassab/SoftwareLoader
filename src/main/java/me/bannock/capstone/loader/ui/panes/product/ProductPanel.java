package me.bannock.capstone.loader.ui.panes.product;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import me.bannock.capstone.loader.products.ProductDTO;
import me.bannock.capstone.loader.products.ProductService;
import me.bannock.capstone.loader.ui.UiUtils;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;

public class ProductPanel extends JPanel {

    @AssistedInject
    public ProductPanel(@Assisted ProductDTO product, ProductService productService){
        this.product = product;
        this.productService = productService;

        create();
    }

    private final ProductDTO product;
    private final ProductService productService;

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
            JButton launchButton = new JButton("Launch");
            launchButton.addActionListener(evt -> {
                try{
                    productService.launchProduct(product.getId());
                }catch (RuntimeException e){
                    refreshOptionPane();
                    UiUtils.displayErrorDialog("Unable to launch product", e.getMessage());
                }
            });
            productOptionPane.add(launchButton);
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
        JPanel productDisplayPane = new JPanel();
        productDisplayPane.setLayout(new BoxLayout(productDisplayPane, BoxLayout.Y_AXIS));
        productDisplayPane.setBorder(new EmptyBorder(0, 8, 0, 0));

        String displayNameHtml = String.format("<html><h3>%s</h3><html>", product.getName());
        productDisplayPane.add(new JLabel(displayNameHtml));

        productDisplayPane.add(new JLabel("$" + product.getPrice()));
        productDisplayPane.add(new JLabel(product.getDescription()));

        return productDisplayPane;
    }

}
