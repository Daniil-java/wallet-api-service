package com.review.walletapiservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.review.walletapiservice.dto.OperationType;
import com.review.walletapiservice.dto.WalletOperationRequest;
import com.review.walletapiservice.entities.Wallet;
import com.review.walletapiservice.repositories.WalletRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = WalletApiServiceApplication.class)
@AutoConfigureMockMvc
@Transactional
public class WalletControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WalletRepository walletRepository;


    @Test
    @Transactional
    public void testCreateWallet() throws Exception {
        ResultActions resultActions = mockMvc.perform(post("/api/v1/wallets/create"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value("0"));

    }

    @Test
    @Transactional
    public void testGetBalance() throws Exception {
        Wallet wallet = walletRepository.save(new Wallet().setBalance(BigDecimal.valueOf(100)));

        mockMvc.perform(get("/api/v1/wallets/" + wallet.getWalletId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value("100"));
    }

    @Test
    @Transactional
    public void testHandleOperationDeposit() throws Exception {
        Wallet wallet = walletRepository.save(new Wallet().setBalance(BigDecimal.valueOf(100)));

        WalletOperationRequest request = new WalletOperationRequest();
        request.setWalletId(wallet.getWalletId());
        request.setOperationType(OperationType.DEPOSIT);
        request.setAmount(BigDecimal.valueOf(50));

        mockMvc.perform(post("/api/v1/wallets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value("150"));
    }

    @Test
    @Transactional
    public void testHandleOperationWithdraw() throws Exception {
        Wallet wallet = walletRepository.save(new Wallet().setBalance(BigDecimal.valueOf(100)));

        WalletOperationRequest request = new WalletOperationRequest();
        request.setWalletId(wallet.getWalletId());
        request.setOperationType(OperationType.WITHDRAW);
        request.setAmount(BigDecimal.valueOf(50));

        mockMvc.perform(post("/api/v1/wallets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value("50"));
    }
}
