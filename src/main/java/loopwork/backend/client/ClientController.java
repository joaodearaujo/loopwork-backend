package loopwork.backend.client;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/professionals/{professionalId}/clients")
public class ClientController {

    private final ClientService service;

    public ClientController(ClientService service) {
        this.service = service;
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
    public ResponseEntity<List<ClientResponse>> list(@PathVariable String professionalId) {
        List<ClientResponse> clients = service.getClientsOfProfessional(professionalId);
        return ResponseEntity.ok(clients);
    }
}