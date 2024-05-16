package me.bannock.capstone.loader.guice;

import com.google.inject.AbstractModule;
import me.bannock.capstone.loader.account.AccountManager;
import me.bannock.capstone.loader.account.backend.BackendAccountManagerImpl;
import me.bannock.capstone.loader.hwid.HwidService;
import me.bannock.capstone.loader.hwid.oshi.OshiHwidService;
import me.bannock.capstone.loader.products.ProductService;
import me.bannock.capstone.loader.products.backend.BackendProductServiceImpl;
import me.bannock.capstone.loader.security.SecurityManager;
import me.bannock.capstone.loader.security.dummy.DummySecurityManager;

public class LoaderModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ProductService.class).to(BackendProductServiceImpl.class).asEagerSingleton();
        bind(HwidService.class).to(OshiHwidService.class).asEagerSingleton();
        bind(SecurityManager.class).to(DummySecurityManager.class).asEagerSingleton();

        bind(AccountManager.class).to(BackendAccountManagerImpl.class).asEagerSingleton();
    }
}
