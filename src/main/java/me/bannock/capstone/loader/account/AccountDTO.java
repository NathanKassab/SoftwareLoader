package me.bannock.capstone.loader.account;

public class AccountDTO {

    public AccountDTO(long uid, String email, String username, String hwid) {
        this.uid = uid;
        this.email = email;
        this.username = username;
        this.hwid = hwid;
    }

    public AccountDTO(){

    }

    private long uid;
    private String email;
    private String username;
    private String hwid;

    public long getUid() {
        return uid;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }


    public String getHwid() {
        return hwid;
    }

    @Override
    public String toString() {
        return "AccountDTO{" +
                "uid=" + uid +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", hwid='" + hwid + '\'' +
                '}';
    }

}
