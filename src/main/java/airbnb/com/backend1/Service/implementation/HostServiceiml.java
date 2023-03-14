package airbnb.com.backend1.Service.implementation;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import airbnb.com.backend1.Entity.Host;
import airbnb.com.backend1.Entity.Users;
import airbnb.com.backend1.Entity.Response.HostResponse;
import airbnb.com.backend1.Exception.EntityNotFoundException;
import airbnb.com.backend1.Mapper.HostMapper;
import airbnb.com.backend1.Repository.HostRepos;
import airbnb.com.backend1.Repository.UserRepos;
import airbnb.com.backend1.Service.HostService;


@Service 
public class HostServiceiml implements HostService {
    @Autowired
    HostRepos hostRepos;
    @Autowired
    UserRepos userRepos;
    @Autowired
    HostMapper hostMapper;

    @Override
    public List<HostResponse> getAllHosts() {
       List<Host> hosts = hostRepos.findAll();
        hosts.sort((a, b) -> a.getUser().getUsername().compareTo(b.getUser().getUsername()));
       List<HostResponse> res = hosts.stream().map(hos -> hostMapper.mapHostToResponse(hos)).collect(Collectors.toList());
       return res;
    }

    @Override
    public List<HostResponse> getAllHostsByName(String name) {
        List<Host> hosts = hostRepos.findByUsername(name);
        hosts.sort((a, b) -> a.getUser().getUsername().compareTo(b.getUser().getUsername()));
       List<HostResponse> res = hosts.stream().map(hos -> hostMapper.mapHostToResponse(hos)).collect(Collectors.toList());
       return res;
    }



    @Override
    public HostResponse getHostById(Long id) {
        Optional<Host> entity = hostRepos.findById(id);
        if(entity.isPresent()) {
            return hostMapper.mapHostToResponse(entity.get());
        }
        throw new EntityNotFoundException("the host not found");
    }

    @Override
    public HostResponse getHostByAuthUser() {
       Users authUser = getAuthUser();
       Optional<Host> entity = hostRepos.findByUserId(authUser.getId());
       if(entity.isPresent()) {
           return hostMapper.mapHostToResponse(entity.get());
       }
        throw new EntityNotFoundException("the host not found");
    }
    private Users isCheck(Optional<Users> entity) {
        if(entity.isPresent()) {
            return entity.get();
        }
        throw new EntityNotFoundException("the user not found");
    }
    private Users getAuthUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Users> entity = userRepos.findByUsername(username);
        Users user = isCheck(entity);
        return user;
    }
}
