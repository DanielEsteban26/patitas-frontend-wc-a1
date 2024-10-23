package pe.edu.cibertec.patitas_frontend_wc_a.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.cibertec.patitas_frontend_wc_a.dto.LogoutRequestDTO;
import pe.edu.cibertec.patitas_frontend_wc_a.dto.LogoutResponseDTO;
import pe.edu.cibertec.patitas_frontend_wc_a.client.FeingCliente;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/login")
@CrossOrigin(origins = "http://localhost:5173")
public class LogoutControllerFeingCliente {

    @Autowired
    FeingCliente autentificacionFeingClient;

    @PostMapping("/logout-feign")
    public ResponseEntity<LogoutResponseDTO> logout(@RequestBody LogoutRequestDTO logoutRequestDTO) {

        System.out.println("Cerrar sesión con FeignClient");

        try {
            ResponseEntity<LogoutResponseDTO> responseEntity = Mono.fromCallable(() -> autentificacionFeingClient.logout(logoutRequestDTO))
                    .block();

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                LogoutResponseDTO logoutResponseDTO = responseEntity.getBody();
                if (logoutResponseDTO != null && logoutResponseDTO.codigo().equals("00")) {
                    return ResponseEntity.ok(logoutResponseDTO);
                } else {
                    return ResponseEntity.status(401).body(new LogoutResponseDTO("02", "Error: No se pudo cerrar la sesión."));
                }
            } else {
                return ResponseEntity.status(500).body(new LogoutResponseDTO("99", "Error: Ocurrió un problema en el cierre de sesión."));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(500).body(new LogoutResponseDTO("99", "Error: Ocurrió un problema en el cierre de sesión."));
        }
    }
}