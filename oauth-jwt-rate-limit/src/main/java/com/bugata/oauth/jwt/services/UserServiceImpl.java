package com.bugata.oauth.jwt.services;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.bugata.oauth.jwt.dtos.UserRequest;
import com.bugata.oauth.jwt.dtos.UserResponse;
import com.bugata.oauth.jwt.models.UserInfo;
import com.bugata.oauth.jwt.models.UserRole;
import com.bugata.oauth.jwt.repositories.RoleRepository;
import com.bugata.oauth.jwt.repositories.UserRepository;
/**
 * @author Bugata
 * Created By Krishna on 01-07-2024
 * @project oauth-jwt
 */
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;
	
    @Autowired
    RoleRepository roleRepository;

    ModelMapper modelMapper = new ModelMapper();



    @Override
    public UserResponse saveUser(UserRequest userRequest) {
        if(userRequest.getUsername() == null){
            throw new RuntimeException("Parameter username is not found in request..!!");
        } else if(userRequest.getPassword() == null){
            throw new RuntimeException("Parameter password is not found in request..!!");
        }


//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        UserDetails userDetail = (UserDetails) authentication.getPrincipal();
//        String usernameFromAccessToken = userDetail.getUsername();
//
//        UserInfo currentUser = userRepository.findByUsername(usernameFromAccessToken);

        UserInfo savedUser = null;

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = userRequest.getPassword();
        String encodedPassword = encoder.encode(rawPassword);

        UserInfo user = modelMapper.map(userRequest, UserInfo.class);
        user.setPassword(encodedPassword);
        if(userRequest.getId() != null){
            UserInfo oldUser = userRepository.findFirstById(userRequest.getId());
            System.out.println(oldUser);
            if(oldUser != null){
              //  oldUser.setId(user.getId());
                oldUser.setPassword(user.getPassword());
                oldUser.setUsername(user.getUsername());
                oldUser.setRoles(user.getRoles());

                savedUser = userRepository.save(oldUser);
              //  roleRepository.saveAll(oldUser.getRoles());
                userRepository.refresh(savedUser);
            } else {
                throw new RuntimeException("Can't find record with identifier: " + userRequest.getId());
            }
        } else {
        	
        	System.out.println("after "+user);
//            user.setCreatedBy(currentUser);
        	
        	//roleRepository.saveAll(user.getRoles());
            savedUser = userRepository.save(user);
           Set<UserRole> rolesSet= user.getRoles();
           for(UserRole role:rolesSet) {
           // UserRole roles=new UserRole();
           // roles.setName(role.getName());
        	   role.setUserInfo(user);
        	roleRepository.save(role);
           }

           ;
        }
        userRepository.refresh(savedUser);
        userRepository.flush();
        UserResponse userResponse = modelMapper.map(savedUser, UserResponse.class);
        System.out.println(userResponse);
        
        return userResponse;
    }

    @Override
    public UserResponse getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetail = (UserDetails) authentication.getPrincipal();
        String usernameFromAccessToken = userDetail.getUsername();
        UserInfo user = userRepository.findByUsername(usernameFromAccessToken);
        UserResponse userResponse = modelMapper.map(user, UserResponse.class);
        return userResponse;
    }

    @Override
    public List<UserResponse> getAllUser() {
        List<UserInfo> users = (List<UserInfo>) userRepository.findAll();
        Type setOfDTOsType = new TypeToken<List<UserResponse>>(){}.getType();
        List<UserResponse> userResponses = modelMapper.map(users, setOfDTOsType);
        return userResponses;
    }


}
