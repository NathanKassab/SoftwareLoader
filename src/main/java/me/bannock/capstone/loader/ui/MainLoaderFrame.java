package me.bannock.capstone.loader.ui;

import com.google.inject.Inject;
import me.bannock.capstone.loader.products.ProductService;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MainLoaderFrame extends JFrame {

    @Inject
    public MainLoaderFrame(ProductService productService){
        this.productService = productService;
    }

    private final static int WINDOW_WIDTH = 400, WINDOW_HEIGHT = 350;
    private final static String WINDOW_TITLE = "Ultra-cool 'n secure loader #1";

    private final ProductService productService;

    public void populateFrame(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setTitle(WINDOW_TITLE);
        setJMenuBar(createMenuBar());
        productService.getOwnedProducts();
    }

    private JMenuBar createMenuBar(){
        JMenuBar menuBar = new JMenuBar();

        JMenu helpMenu = new JMenu("Help");
        menuBar.add(helpMenu);
        JMenuItem kill = new JMenuItem("Close loader");
        kill.addActionListener((evt) -> System.exit(0));
        helpMenu.add(kill);

        return menuBar;
    }

}
