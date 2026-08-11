package loopwork.backend.client;

import jakarta.validation.Valid;
import loopwork.backend.authentication.authenticatedUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clients")
public class ClientController {

    private final ClientService service;
    private final authenticatedUser authenticatedUser;

    public ClientController(ClientService service, authenticatedUser authenticatedUser) {
        this.service = service;
        this.authenticatedUser = authenticatedUser;
    }

    @PostMapping
    public ResponseEntity<ClientResponse> create(
            @PathVariable String professionalId,
            @Valid @RequestBody ClientRequest request
    ) {
        ClientResponse response = service.create(request, professionalId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ClientResponse>> list() {
        String professionalId = authenticatedUser.getProfessionalId();
        List<ClientResponse> clients = service.getClientsOfProfessional(professionalId);
        return ResponseEntity.ok(clients);
    }
}