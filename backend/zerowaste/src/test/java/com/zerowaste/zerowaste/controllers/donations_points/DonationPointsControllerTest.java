package com.zerowaste.zerowaste.controllers.donations_points;

import com.zerowaste.controllers.donation_points.DonationPointsController;
import com.zerowaste.dtos.donation_points.CreateDonationPointDTO;
import com.zerowaste.dtos.donation_points.DonationPointsDTO;
import com.zerowaste.dtos.donation_points.UpdateDonationPointDTO;
import com.zerowaste.services.donation_points.CreateDonationPointService;
import com.zerowaste.services.donation_points.DeleteDonationPointService;
import com.zerowaste.services.donation_points.GetDonationPointByIdService;
import com.zerowaste.services.donation_points.GetDonationPointsService;
import com.zerowaste.services.donation_points.UpdateDonationPointService;
import com.zerowaste.services.donation_points.exceptions.DonationPointNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalTime;
import java.util.Arrays;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class DonationPointsControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CreateDonationPointService createDonationPointService;

    @Mock
    private GetDonationPointsService getDonationPointsService;

    @Mock
    private GetDonationPointByIdService getDonationPointByIdService;

    @Mock
    private UpdateDonationPointService updateDonationPointService;

    @Mock
    private DeleteDonationPointService deleteDonationPointService;

    @InjectMocks
    private DonationPointsController donationPointsController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(donationPointsController).build();
    }

    @Test
    void testCreateDonationPoint_Success() throws Exception {
        // DTO para criação do ponto de doação
        CreateDonationPointDTO dto = new CreateDonationPointDTO(
                "Point 1",
                LocalTime.of(8, 0),
                LocalTime.of(18, 0),
                "email@example.com",
                "Street 123",
                10,
                "City 1"
        );

        // Simula a execução do serviço sem erros
        doNothing().when(createDonationPointService).execute(dto);

        // Realiza a requisição para criar o ponto de doação
        mockMvc.perform(post("/donation-points/")
                .contentType("application/json")
                .content("{\"name\": \"Point 1\", \"openingTime\": \"08:00\", \"closingTime\": \"18:00\", \"email\": \"email@example.com\", \"street\": \"Street 123\", \"number\": 10, \"city\": \"City 1\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Ponto de doação criado com sucesso!"));

        // Verifica se o serviço foi chamado uma vez para executar a criação do ponto de doação
        verify(createDonationPointService, times(1)).execute(dto);
    }
   @Test
    void testGetDonationPoints_Success() throws Exception {
        // Criando os pontos de doação DTO
        DonationPointsDTO donationPoint1 = new DonationPointsDTO(
            1L, // ID
            "Point 1", 
            LocalTime.of(8, 0), 
            LocalTime.of(18, 0), 
            "email1@example.com", 
            "Street 123", 
            10, 
            "City 1"
        );

        DonationPointsDTO donationPoint2 = new DonationPointsDTO(
            2L, // ID
            "Point 2", 
            LocalTime.of(9, 0), 
            LocalTime.of(17, 0), 
            "email2@example.com", 
            "Street 456", 
            15, 
            "City 2"
            );

        // Mocking o comportamento do serviço para retornar os pontos de doação
        when(getDonationPointsService.execute()).thenReturn(Arrays.asList(donationPoint1, donationPoint2));

        // Realizando a requisição para listar todos os pontos de doação
        mockMvc.perform(get("/donation-points/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.donation_points").exists()) // Verifica se a lista de pontos de doação está presente
                .andExpect(jsonPath("$.donation_points[0].name").value("Point 1")) // Verifica o nome do primeiro ponto de doação
                .andExpect(jsonPath("$.donation_points[1].name").value("Point 2")); // Verifica o nome do segundo ponto de doação

        // Verificando que o serviço foi chamado uma vez
        verify(getDonationPointsService, times(1)).execute();
    }

    @Test
    void testGetDonationPointById_NotFound() throws Exception {
        Long id = 1L;

        // Mocking para lançar DonationPointNotFoundException quando não encontrar o ponto de doação
        when(getDonationPointByIdService.execute(id)).thenThrow(new DonationPointNotFoundException("Ponto de doação não encontrado"));

        // Realizando a requisição para buscar um ponto de doação com id específico
        mockMvc.perform(get("/donation-points/{id}", id))
            .andExpect(status().isNotFound()) // Espera o status de não encontrado (404)
            .andExpect(jsonPath("$.message").value("Ponto de doação não encontrado")); // Verifica a mensagem de erro

        // Verificando que o serviço foi chamado para buscar o ponto de doação pelo id
        verify(getDonationPointByIdService, times(1)).execute(id);
}


    
    @Test
    void testUpdateDonationPoint_Success() throws Exception {
        Long id = 1L;
    
        // Criando um objeto DTO com dados válidos
        UpdateDonationPointDTO dto = new UpdateDonationPointDTO(
                "Updated Point", 
                LocalTime.of(8, 0), // openingTime
                LocalTime.of(18, 0), // closingTime
                "updated@example.com", 
                "Updated Street", 
                123, 
                "Updated City"
        );
    
        // Mocking o comportamento do serviço de atualização
        doNothing().when(updateDonationPointService).execute(id, dto);
    
        // Realizando a requisição de atualização do ponto de doação
        mockMvc.perform(put("/donation-points/" + id)
                .contentType("application/json")
                .content("{"
                        + "\"name\": \"Updated Point\","
                        + "\"openingTime\": \"08:00\","
                        + "\"closingTime\": \"18:00\","
                        + "\"email\": \"updated@example.com\","
                        + "\"street\": \"Updated Street\","
                        + "\"number\": 123,"
                        + "\"city\": \"Updated City\""
                        + "}"))  // JSON correspondente ao DTO
                .andExpect(status().isOk())  // Verificando se o status é OK (200)
                .andExpect(jsonPath("$.message").value("Ponto de doação atualizado com sucesso!"));  // Verificando a mensagem de sucesso
    
        // Verificando que o serviço de atualização foi chamado corretamente
        verify(updateDonationPointService, times(1)).execute(id, dto);
    }
    
    @Test
    void testDeleteDonationPoint_Success() throws Exception {
        Long id = 1L;
    
        // Mocking o comportamento do serviço de exclusão
        doNothing().when(deleteDonationPointService).execute(id);
    
        // Realizando a requisição de exclusão do ponto de doação
        mockMvc.perform(delete("/donation-points/" + id))
                .andExpect(status().isOk())  // Verificando se o status é OK (200)
                .andExpect(jsonPath("$.message").value("Ponto de doação deletado com sucesso!"));  // Verificando a mensagem de sucesso
    
        // Verificando que o serviço de exclusão foi chamado corretamente
        verify(deleteDonationPointService, times(1)).execute(id);
    }
}
