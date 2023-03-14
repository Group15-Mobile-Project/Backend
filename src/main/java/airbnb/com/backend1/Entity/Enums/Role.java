package airbnb.com.backend1.Entity.Enums;

public enum Role {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER"),
    HOST("ROLE_HOST");

    private String name;

    Role(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}