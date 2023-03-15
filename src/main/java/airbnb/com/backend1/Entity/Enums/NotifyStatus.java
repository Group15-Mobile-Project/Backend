package airbnb.com.backend1.Entity.Enums;

public enum NotifyStatus {
    //host
    PENDING_BOOKING("PENDING_BOOKING"),
    //host
    UPDATE_BOOKING("UPDATE_BOOKING"),
      //host
      CANCEL_BOOKING("CANCEL_BOOKING"),
    //tenant
    ACCECPTED_BOOKING("ACCECPTED_BOOKING"),
    //tenant
    UNACCEPTED_BOOKING("UNACCEPTED_BOOKING"),
    //host
    HOME_REVIEW("HOME_REVIEW"),
    //tenant
    TENANT_REVIEW("TENANT_REVIEW");
    

    private String name;

    NotifyStatus(String name) {
        this.name = name;
    }
    public String getName() {
        return this.name;
    }
}
