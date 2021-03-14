package pt.up.fc.progmovel.socialapp.shared;

public class User {

    private final String hwdId;
    private final String name;

    public User(String hwdId, String name) {
        this.hwdId = hwdId;
        this.name = name;
    }

    public String getHwdId() {
        return hwdId;
    }

    public String getName() {
        return name;
    }

}
