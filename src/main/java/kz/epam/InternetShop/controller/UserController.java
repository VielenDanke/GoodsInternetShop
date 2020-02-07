package kz.epam.InternetShop.controller;

import kz.epam.InternetShop.model.User;
import kz.epam.InternetShop.payload.UpdateRequest;
import kz.epam.InternetShop.security.UserPrincipal;
import kz.epam.InternetShop.service.annotation.*;
import kz.epam.InternetShop.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@IsApprovedPerson
public class UserController {

    private UserService userService;

    @GetMapping("/all")
    @IsAdmin
    public Iterable<User> getAllUsers() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    @IsAdmin
    public User getUserById(@PathVariable Long id) {
        return userService.findById(id);
    }

    @DeleteMapping("/{id}")
    @IsAdmin
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        User user = userService.findById(id);

        userService.delete(user);

        return new ResponseEntity<String>("User deleted successfully", HttpStatus.OK);
    }

    @PutMapping("/update")
    @IsUser
    public User updateUser(@CurrentUser UserPrincipal userPrincipal, @RequestBody UpdateRequest updateRequest) {
        User userFromDatabaseById = userService.findById(userPrincipal.getId());
        if (!StringUtils.isEmpty(updateRequest.getAddress())) {
            userFromDatabaseById.setAddress(updateRequest.getAddress());
        }
        if (!StringUtils.isEmpty(updateRequest.getFullName())) {
            userFromDatabaseById.setFullName(updateRequest.getFullName());
        }
        return userService.save(userFromDatabaseById);
    }

    @GetMapping("/me")
    @isAuthorized
    public User getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        return userService.findById(userPrincipal.getId());
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
