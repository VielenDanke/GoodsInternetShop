package kz.epam.InternetShop.controller;

import kz.epam.InternetShop.model.User;
import kz.epam.InternetShop.security.UserPrincipal;
import kz.epam.InternetShop.service.annotation.CurrentUser;
import kz.epam.InternetShop.service.annotation.IsAdmin;
import kz.epam.InternetShop.service.annotation.IsApprovedPerson;
import kz.epam.InternetShop.service.annotation.IsUser;
import kz.epam.InternetShop.service.interfaces.UserService;
import kz.epam.InternetShop.util.ControllerUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/shop/user")
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
    public User updateUser(@CurrentUser UserPrincipal userPrincipal) {
        User userFromDatabaseById = userService.findById(userPrincipal.getId());
        userFromDatabaseById.setAddress(userPrincipal.getFullAddress());
        userFromDatabaseById.setFullName(userPrincipal.getPersonFullName());
        return userService.save(userFromDatabaseById);
    }

    @GetMapping("/me")
    @IsUser
    public User getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        return userService.findById(userPrincipal.getId());
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
