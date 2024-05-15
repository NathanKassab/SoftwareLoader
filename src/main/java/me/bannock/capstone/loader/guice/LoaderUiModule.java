package me.bannock.capstone.loader.guice;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import me.bannock.capstone.loader.ui.panes.product.ProductPanel;
import me.bannock.capstone.loader.ui.panes.product.ProductPanelFactory;

public class LoaderUiModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder().implement(ProductPanel.class, ProductPanel.class).build(ProductPanelFactory.class));
    }

}
