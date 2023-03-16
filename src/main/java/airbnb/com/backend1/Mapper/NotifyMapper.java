package airbnb.com.backend1.Mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import airbnb.com.backend1.Entity.Notify;
import airbnb.com.backend1.Entity.Response.HostResponse;
import airbnb.com.backend1.Entity.Response.NotifyResponse;
import airbnb.com.backend1.Entity.Response.UserResponse;

@Component
public class NotifyMapper {
    @Autowired
    UserMapper userMapper;
    @Autowired
    HostMapper hostMapper;

    public NotifyResponse mapNotifyToResponse(Notify notify) {
        UserResponse tenant = userMapper.mapUserToResponse(notify.getTenant());
        HostResponse host = hostMapper.mapHostToResponse(notify.getHost());

        NotifyResponse res = new NotifyResponse(notify.getId(), notify.getStatus(), notify.getIsRead(), notify.getDateCreated(), notify.getDateUpdated(), tenant, host, notify.getHome().getId());

        
        if(notify.getBooking() != null) {
            res.setBookingId(notify.getBooking().getId());
            res.setBookingCode(notify.getBooking().getBookingCode());
        }
        
        return res;
    }
}
