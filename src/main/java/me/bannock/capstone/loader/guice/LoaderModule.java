package me.bannock.capstone.loader.guice;

import com.google.inject.AbstractModule;
import me.bannock.capstone.loader.backend.BackendService;
import me.bannock.capstone.loader.backend.capstone.CapstoneBackendServiceImpl;

public class LoaderModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(BackendService.class).to(CapstoneBackendServiceImpl.class).asEagerSingleton();
    }
}
