package me.bannock.capstone.loader.guice;

import com.google.inject.AbstractModule;
import me.bannock.capstone.loader.account.AccountManager;
import me.bannock.capstone.loader.account.backend.BackendAccountManagerImpl;
import me.bannock.capstone.loader.hwid.HwidService;
import me.bannock.capstone.loader.hwid.oshi.OshiHwidService;
import me.bannock.capstone.loader.products.ProductService;
import me.bannock.capstone.loader.products.backend.BackendProductServiceImpl;

public class LoaderModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(AccountManager.class).to(BackendAccountManagerImpl.class).asEagerSingleton();

        bind(ProductService.class).to(BackendProductServiceImpl.class).asEagerSingleton();
        bind(HwidService.class).to(OshiHwidService.class).asEagerSingleton();
    }
}
