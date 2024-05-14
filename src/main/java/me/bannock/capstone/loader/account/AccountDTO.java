package me.bannock.capstone.loader.account;

public class AccountDTO {

    public AccountDTO(long uid, String email, String username, String apiKey, String hwid) {
        this.uid = uid;
        this.email = email;
        this.username = username;
        this.apiKey = apiKey;
        this.hwid = hwid;
    }

    private final long uid;
    private final String email;
    private final String username;
    private final String apiKey;
    private final String hwid;

    public long getUid() {
        return uid;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getHwid() {
        return hwid;
    }

}
