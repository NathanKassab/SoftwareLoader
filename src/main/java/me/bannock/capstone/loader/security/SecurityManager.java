package me.bannock.capstone.loader.security;


public interface SecurityManager {

    /**
     * Starts the manager's checks to protect this process
     */
    void startProtecting();

    /**
     * Tells the security manager to protect another process
     * @param process The process to protect
     */
    void protectProcess(Process process);

}
