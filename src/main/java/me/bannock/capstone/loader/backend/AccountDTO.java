package me.bannock.capstone.loader.backend;

public class AccountDTO {

    public AccountDTO(long uid, String username, boolean enabled){
        this.uid = uid;
        this.username = username;
        this.enabled = enabled;
    }

    private final long uid;
    private final String username;
    private final boolean enabled;

    public long getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public String toString() {
        return "AccountDTO{" +
                "uid=" + uid +
                ", username='" + username + '\'' +
                ", enabled=" + enabled +
                '}';
    }

}
