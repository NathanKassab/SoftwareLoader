package me.bannock.capstone.loader.account;

public interface AccountService {

    /**
     * Checks to make sure the hwid matches and grabs the user's details
     * @param hwid The user's hwid
     * @return The user's account
     * @throws RuntimeException If something goes wrong while logging in
     */
    AccountDTO login(String hwid) throws RuntimeException;

}
