package me.bannock.capstone.loader.ui;

import com.google.inject.Inject;
import me.bannock.capstone.loader.products.ProductService;

import javax.swing.JFrame;

public class MainLoaderFrame extends JFrame {

    @Inject
    public MainLoaderFrame(ProductService productService){
        this.productService = productService;
    }

    private final ProductService productService;

}
