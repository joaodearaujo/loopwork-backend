package loopwork.backend.client;

import loopwork.backend.professional.Professional;
import loopwork.backend.professional.ProfessionalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {

    private static final Logger logger = LoggerFactory.getLogger(ClientService.class);
    private final ClientRepository repository;
    private final ProfessionalService professionalService;

    public ClientService(ClientRepository repository, ProfessionalService professionalService) {
        this.repository = repository;
        this.professionalService = professionalService;
    }

    public ClientResponse create(ClientRequest clientRequest, String professionalId) {
        Professional professional = professionalService.findByIdOrThrow(professionalId);

        if (repository.existsByProfessionalIdAndEmail(professionalId, clientRequest.email())) {
            throw new ClientEmailAlreadyInUseException(clientRequest.email(), professionalId);
        }

        Client client = new Client(
                clientRequest.name(),
                clientRequest.email(),
                professional
        );

        Client savedClient = repository.save(client);

        logger.info("Client created successfully");

        return ClientResponse.fromEntity(savedClient);
    }

    public List<ClientResponse> getClientsOfProfessional(String professionalId) {
        professionalService.findByIdOrThrow(professionalId);

        return repository.findByProfessionalId(professionalId)
                .stream()
                .map(ClientResponse::fromEntity)
                .toList();
    }

    public Client findByIdOrThrow(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException(id));
    }
}
