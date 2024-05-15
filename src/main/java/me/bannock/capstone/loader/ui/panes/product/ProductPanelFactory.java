package me.bannock.capstone.loader.ui.panes.product;

import me.bannock.capstone.loader.products.ProductDTO;

public interface ProductPanelFactory {

    /**
     * Creates a new product panel, styled and all
     * @param productDTO The product to build the pane for
     * @return The product panel
     */
    ProductPanel create(ProductDTO productDTO);

}
