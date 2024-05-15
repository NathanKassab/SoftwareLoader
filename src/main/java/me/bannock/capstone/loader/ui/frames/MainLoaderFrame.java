package me.bannock.capstone.loader.ui.frames;

import com.google.inject.Inject;
import com.google.inject.Injector;
import me.bannock.capstone.loader.products.ProductDTO;
import me.bannock.capstone.loader.products.ProductService;
import me.bannock.capstone.loader.ui.UiUtils;
import me.bannock.capstone.loader.ui.panes.product.ProductPanel;
import me.bannock.capstone.loader.ui.panes.product.ProductPanelFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;

public class MainLoaderFrame extends JFrame {

    @Inject
    public MainLoaderFrame(ProductService productService, ProductPanelFactory productPanelFactory,
                           Injector injector){
        this.productService = productService;
        this.productPanelFactory = productPanelFactory;
        this.injector = injector;
    }

    private final Logger logger = LogManager.getLogger();
    private final ProductService productService;
    private final ProductPanelFactory productPanelFactory;
    private final Injector injector;
    private JPanel contentPane, productPane;

    /**
     * Populates this frame with the needed components. Does not clear or remove old components
     */
    public void populateFrame(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(550, 425);
        setTitle("Ultra-cool 'n secure loader #1");
        setJMenuBar(createMenuBar());

        // This needs to be double buffered because we repaint it whenever the product pane is swapped
        contentPane = new JPanel(true);
        contentPane.setLayout(new BorderLayout());
        setContentPane(contentPane);

        JPanel sideNavPane = new JPanel();
        sideNavPane.setLayout(new BoxLayout(sideNavPane, BoxLayout.Y_AXIS));
        populateSideNav(sideNavPane);
        JScrollPane sideNavScrollPane = new JScrollPane(sideNavPane);
        sideNavScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        sideNavScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        contentPane.add(sideNavScrollPane, BorderLayout.WEST);

        productPane = createDefaultProductPane();
        contentPane.add(productPane, BorderLayout.CENTER);
    }

    /**
     * This creates the default product pane, which is what is shown on startup.
     * @return The default product pane
     */
    private JPanel createDefaultProductPane(){
        JPanel defaultProductPane = new JPanel();
        defaultProductPane.setLayout(new BoxLayout(defaultProductPane, BoxLayout.Y_AXIS));
        defaultProductPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        defaultProductPane.add(new JLabel("Welcome!"));
        defaultProductPane.add(new JLabel("Select a product from the side to get started"));
        return defaultProductPane;
    }

    /**
     * The side nav contains buttons to open each product on the product pane.
     * This method populates it.
     * @param sideNavPane The pane to populate. Will not be styled
     */
    private void populateSideNav(JPanel sideNavPane){
        for (ProductDTO product : productService.getOwnedProducts()){
            JButton openProduct = new JButton();

            String buttonText = String.format("%s", product.getName());
            openProduct.setText(buttonText);

            try {
                int fontSize = openProduct.getFont().getSize();
                Image buttonIcon = UiUtils.createScaledImage(new URL(product.getIconUrl()), fontSize);
                openProduct.setIcon(new ImageIcon(buttonIcon));
            } catch (IOException e) {
                logger.warn("Unable to load icon for product, iconUrl={}", product.getIconUrl());
            }

            openProduct.addActionListener(evt -> changeProducts(product));

            sideNavPane.add(openProduct);
        }
    }

    /**
     * Creates a pane for a product and shows it to the user
     * @param product The product to show inside the product view
     */
    private void changeProducts(ProductDTO product){
        ProductPanel productPanel = productPanelFactory.create(product);
        contentPane.remove(this.productPane);
        productPane = productPanel;
        contentPane.add(productPane, BorderLayout.CENTER);
        contentPane.revalidate();
        contentPane.repaint();
    }

    /**
     * The menu bar is what's displayed at the top of the window. This
     * method creates, styles, and populates it
     * @return The generated menu bar
     */
    private JMenuBar createMenuBar(){
        JMenuBar menuBar = new JMenuBar();

        JMenu helpMenu = new JMenu("Help");
        menuBar.add(helpMenu);

        JMenuItem myAccount = new JMenuItem("My account");
        myAccount.addActionListener(evt -> injector.getInstance(AboutAccountFrame.class).showAndCreateWithParent(this));
        helpMenu.add(myAccount);

        JMenuItem refreshProducts = new JMenuItem("Refresh products");
        refreshProducts.addActionListener(evt -> {
            populateFrame();
            revalidate();
            repaint();
        });
        helpMenu.add(refreshProducts);

        return menuBar;
    }

}
