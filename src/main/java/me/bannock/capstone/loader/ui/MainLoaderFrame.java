package me.bannock.capstone.loader.ui;

import com.google.inject.Inject;
import com.google.inject.Injector;
import me.bannock.capstone.loader.products.ProductService;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MainLoaderFrame extends JFrame {

    @Inject
    public MainLoaderFrame(ProductService productService, Injector injector){
        this.productService = productService;
        this.injector = injector;
    }

    private final ProductService productService;
    private final Injector injector;

    /**
     * Populates this frame with the needed components. Does not clear or remove old components
     */
    public void populateFrame(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(550, 425);
        setTitle("Ultra-cool 'n secure loader #1");
        setJMenuBar(createMenuBar());
        productService.getOwnedProducts();
    }

    private JMenuBar createMenuBar(){
        JMenuBar menuBar = new JMenuBar();

        JMenu helpMenu = new JMenu("Help");
        menuBar.add(helpMenu);

        JMenuItem myAccount = new JMenuItem("My account");
        myAccount.addActionListener(evt -> injector.getInstance(AboutAccountFrame.class).showAndCreateWithParent(this));
        helpMenu.add(myAccount);

        return menuBar;
    }

}
