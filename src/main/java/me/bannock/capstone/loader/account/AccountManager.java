package me.bannock.capstone.loader.account;

import java.util.Optional;

public interface AccountManager {

    /**
     * Checks to make sure the hwid matches and grabs the user's details
     * @param hwid The user's hwid
     * @throws AccountManagerException If something goes wrong while logging in
     */
    void login(String hwid) throws AccountManagerException;

    /**
     * Gets the currently logged-in user
     * @return The user object, if they have logged in
     */
    Optional<AccountDTO> getUser();

    /**
     * @return True if the user has been logged in
     */
    default boolean isLoggedIn(){
        return getUser().isPresent();
    }

}
