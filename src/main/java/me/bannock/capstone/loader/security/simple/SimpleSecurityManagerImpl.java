package me.bannock.capstone.loader.security.simple;

import me.bannock.capstone.loader.security.SecurityManager;

public class SimpleSecurityManagerImpl implements SecurityManager {

    @Override
    public void startProtecting() {

    }

    @Override
    public void protectProcess(Process process) {
        Thread protectThread = new Thread(() -> {
            synchronized (process){
                while(process.isAlive()){ // TODO: This loop does not end when a java program closes
                    try {
                        Thread.sleep(300);
                    }catch (Exception ignored){}
                }
            }
        });
        protectThread.setDaemon(false);
        protectThread.start();
        Runtime.getRuntime().addShutdownHook(new Thread(process::destroyForcibly));
    }

}
