package loopwork.backend.client;

import loopwork.backend.professional.Professional;
import loopwork.backend.professional.ProfessionalService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ProfessionalService professionalService;

    @InjectMocks
    private ClientService clientService;

    @Test
    void shouldCreateClientWhenEmailIsNotInUse() {
        Professional professional = new Professional("professional", "professional@example.com", "password");
        ClientRequest request = new ClientRequest("client", "client@example.com");

        when(professionalService.findByIdOrThrow("prof-1")).thenReturn(professional);
        when(clientRepository.existsByProfessionalIdAndEmail("prof-1", "client@example.com")).thenReturn(false);
        when(clientRepository.save(any(Client.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ClientResponse response = clientService.create(request, "prof-1");

        assertEquals("client", response.name());
        assertEquals("client@example.com", response.email());
    }

    @Test
    void shouldThrowWhenEmailAlreadyInUseForProfessional() {
        Professional professional = new Professional("professional", "professional@example.com", "password");
        ClientRequest request = new ClientRequest("client", "client@example.com");

        when(professionalService.findByIdOrThrow("prof-1")).thenReturn(professional);
        when(clientRepository.existsByProfessionalIdAndEmail("prof-1", "client@example.com")).thenReturn(true);

        assertThrows(ClientEmailAlreadyInUseException.class, () -> clientService.create(request, "prof-1"));
    }

    @Test
    void shouldReturnListOfClientsWhenProfessionalExists() {
        Professional professional = new Professional("professional", "professional@example.com", "password");

        Client client = new Client(
                "Client",
                "client@example.com",
                professional
        );

        Client client2 = new Client(
                "Client2",
                "client2@example.com",
                professional
        );

        when(professionalService.findByIdOrThrow("prof-1")).thenReturn(professional);
        when(clientRepository.findByProfessionalId("prof-1")).thenReturn(List.of(client, client2));

        List<ClientResponse> clientResponseList = clientService.getClientsOfProfessional("prof-1");

        assertEquals(2, clientResponseList.size());
        assertEquals("Client", clientResponseList.get(0).name());
        assertEquals("client@example.com", clientResponseList.get(0).email());

        assertEquals("Client2", clientResponseList.get(1).name());
        assertEquals("client2@example.com", clientResponseList.get(1).email());
    }
}