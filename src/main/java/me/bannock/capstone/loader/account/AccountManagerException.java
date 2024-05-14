package me.bannock.capstone.loader.account;

public class AccountManagerException extends RuntimeException {

    /**
     * @param authObject The object used to authenticate with whatever is used to manage users
     * @param uid The user's uid
     * @param message The error message that is shown to the user
     */
    public AccountManagerException(String message, String uid, Object authObject){
        super(String.format("%s, auth=%s, uid=%s", message, authObject, uid));
        this.errorMessage = message;
    }

    private final String errorMessage;

    /**
     * @return The raw error message to show to the user
     */
    public String getErrorMessage() {
        return errorMessage;
    }

}
