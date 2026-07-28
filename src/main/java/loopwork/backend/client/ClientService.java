package loopwork.backend.client;

import loopwork.backend.professional.Professional;
import loopwork.backend.professional.ProfessionalNotFoundException;
import loopwork.backend.professional.ProfessionalRepository;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

    private final ClientRepository repository;
    private final ProfessionalRepository professionalRepository;

    public ClientService(ClientRepository repository, ProfessionalRepository professionalRepository) {
        this.repository = repository;
        this.professionalRepository = professionalRepository;
    }

    public ClientResponse create(ClientRequest clientRequest, String professionalId) {
        Professional professional = professionalRepository.findById(professionalId)
                .orElseThrow( () -> new ProfessionalNotFoundException(professionalId));

        if (repository.existsByProfessionalIdAndEmail(professionalId, clientRequest.email())) {
            throw new ClientEmailAlreadyInUseException(clientRequest.email(), professionalId);
        }

        Client client = new Client(
                clientRequest.name(),
                clientRequest.email(),
                professional
        );

        Client savedClient = repository.save(client);

        return ClientResponse.fromEntity(savedClient);
    }
}
