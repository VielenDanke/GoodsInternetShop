package kz.epam.InternetShop.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kz.epam.InternetShop.model.User;
import kz.epam.InternetShop.payload.UpdateRequest;
import kz.epam.InternetShop.security.UserPrincipal;
import kz.epam.InternetShop.service.annotation.CurrentUser;
import kz.epam.InternetShop.service.annotation.IsAdmin;
import kz.epam.InternetShop.service.annotation.IsApprovedPerson;
import kz.epam.InternetShop.service.annotation.IsUser;
import kz.epam.InternetShop.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@IsApprovedPerson
@Api(value = "User management system")
public class UserController {

    private UserService userService;

    @IsAdmin
    @GetMapping("/all")
    @ApiOperation(value = "Get all users", response = List.class, httpMethod = "GET")
    public Iterable<User> getAllUsers() {
        return userService.findAll();
    }

    @IsAdmin
    @GetMapping("/{id}")
    @ApiOperation(value = "Get user by ID", response = User.class, httpMethod = "GET")
    public User getUserById(@PathVariable Long id) {
        return userService.findById(id);
    }

    @IsAdmin
    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete user by ID", response = ResponseEntity.class, httpMethod = "DELETE")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        User user = userService.findById(id);

        userService.delete(user);

        return new ResponseEntity<String>("User deleted successfully", HttpStatus.OK);
    }

    @IsUser
    @PutMapping("/update")
    @ApiOperation(value = "Update user with user principal", response = User.class, httpMethod = "PUT")
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

    @IsUser
    @GetMapping("/me")
    @ApiOperation(value = "Get user by user principal for profile", response = User.class, httpMethod = "GET")
    public User getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        return userService.findById(userPrincipal.getId());
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
