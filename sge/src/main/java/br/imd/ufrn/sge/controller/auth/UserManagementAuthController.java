package br.imd.ufrn.sge.controller.auth;


import br.imd.ufrn.sge.dto.UserRegisterDTO;
import br.imd.ufrn.sge.dto.UserReqResponseDTO;
import br.imd.ufrn.sge.exceptions.AuthException;
import br.imd.ufrn.sge.models.auth.Usuario;
import br.imd.ufrn.sge.service.auth.UserService;
import br.imd.ufrn.sge.service.auth.UsuarioManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class UserManagementAuthController {

    @Autowired
    private UsuarioManagementService usuarioManagementService;

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserReqResponseDTO> register(@RequestBody UserRegisterDTO userReqResponseDTO){
        try {
            UserReqResponseDTO userReqResponseDTO1 = usuarioManagementService.register(userReqResponseDTO);
            return ResponseEntity.ok().body(userReqResponseDTO1);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(400).body(null);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<UserReqResponseDTO> login(@RequestBody UserReqResponseDTO userReqResponseDTO){
        try {
            UserReqResponseDTO userReqResponseDTO1 = usuarioManagementService.login(userReqResponseDTO);
            return ResponseEntity.ok().body(userReqResponseDTO1);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(400).body(null);
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<UserReqResponseDTO> refresh(@RequestBody UserReqResponseDTO userReqResponseDTO){
        try {
            UserReqResponseDTO userReqResponseDTO1 = usuarioManagementService.refresh(userReqResponseDTO);
            return ResponseEntity.ok().body(userReqResponseDTO1);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(400).body(null);
        }
    }

//    @PostMapping("/logout")
//    public ResponseEntity<UserReqResponseDTO> logout(@RequestBody UserReqResponseDTO userReqResponseDTO){
//        try {
//            UserReqResponseDTO userReqResponseDTO1 = usuarioManagementService.logout(userReqResponseDTO);
//            return ResponseEntity.ok().body(userReqResponseDTO1);
//        }catch (Exception e){
//            e.printStackTrace();
//            return ResponseEntity.status(400).body(null);
//        }
//    }

    @GetMapping("/check-role/{id}")
    public ResponseEntity<?> checkUserRole(@PathVariable Long id) throws AuthException {
        Optional<Usuario> user = userService.findUserByIdDadosPessoais(id);
        if (user.isPresent()) {
            return ResponseEntity.ok().body(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }



}
