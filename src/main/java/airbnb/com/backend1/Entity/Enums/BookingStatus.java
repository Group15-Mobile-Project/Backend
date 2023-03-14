package airbnb.com.backend1.Entity.Enums;

public enum BookingStatus {
    PENDING("PENDING"),
    ACCECPTED("ACCEPTED"),
    UNACCEPTED("UNACCEPTED");
    

    private String name;

    BookingStatus(String name) {
        this.name = name;
    }
    public String getName() {
        return this.name;
    }
    
}
