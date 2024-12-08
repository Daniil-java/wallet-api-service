package com.review.walletapiservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.review.walletapiservice.dto.OperationType;
import com.review.walletapiservice.dto.WalletOperationRequest;
import com.review.walletapiservice.dto.WalletResponse;
import com.review.walletapiservice.entities.Transaction;
import com.review.walletapiservice.entities.Wallet;
import com.review.walletapiservice.repositories.TransactionRepository;
import com.review.walletapiservice.repositories.WalletRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = WalletApiServiceApplication.class)
@AutoConfigureMockMvc
public class WalletControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WalletRepository walletRepository;
    @MockBean
    private TransactionRepository transactionRepository;


    @Test
    @DisplayName("Тест проверяет корректность создания кошелька")
    public void testCreateWallet() throws Exception {
        ArgumentCaptor<Wallet> walletCaptor = ArgumentCaptor.forClass(Wallet.class);

        mockMvc.perform(post("/api/v1/wallets/create"))
                .andExpect(status().isOk());

        verify(walletRepository, times(1)).save(walletCaptor.capture());
        assertEquals(BigDecimal.ZERO, walletCaptor.getValue().getBalance());
    }

    @Test
    @DisplayName("Тест проверяет корректность получения данных кошелька")
    public void testGetBalance() throws Exception {
        Wallet wallet = new Wallet()
                .setWalletId(UUID.randomUUID())
                .setBalance(BigDecimal.valueOf(100l));

        doReturn(Optional.of(wallet)).when(walletRepository).findById(any());

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/wallets/" + wallet.getWalletId()))
                .andExpect(status().isOk())
                .andReturn();

        WalletResponse walletResponse = new ObjectMapper().readValue(mvcResult.getResponse().getContentAsString(), WalletResponse.class);

        verify(walletRepository, times(1)).findById(wallet.getWalletId());
        assertEquals(wallet.getWalletId(), walletResponse.getWalletId());
        assertEquals(wallet.getBalance(), walletResponse.getBalance());
    }

    @Test
    @DisplayName("Тест проверяет корректность пополнения средств с кошелька")
    public void testHandleOperationDeposit() throws Exception {
        Wallet wallet = new Wallet()
                .setWalletId(UUID.randomUUID())
                .setBalance(BigDecimal.valueOf(100l));

        WalletOperationRequest request = new WalletOperationRequest();
        request.setWalletId(wallet.getWalletId());
        request.setOperationType(OperationType.DEPOSIT);
        request.setAmount(BigDecimal.valueOf(50));

        ArgumentCaptor<Wallet> walletCaptor = ArgumentCaptor.forClass(Wallet.class);
        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);

        doReturn(true).when(walletRepository).existsById(wallet.getWalletId());
        doReturn(Optional.of(wallet)).when(walletRepository).findById(wallet.getWalletId());

        mockMvc.perform(post("/api/v1/wallets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(walletRepository, times(1)).save(walletCaptor.capture());
        verify(transactionRepository, times(1)).save(transactionCaptor.capture());
        assertEquals(BigDecimal.valueOf(150l), walletCaptor.getValue().getBalance());
        assertEquals(request.getAmount(), transactionCaptor.getValue().getAmount());
        assertEquals(request.getOperationType(), transactionCaptor.getValue().getOperationType());
        assertEquals(wallet.getWalletId(), transactionCaptor.getValue().getWallet().getWalletId());
    }

    @Test
    @DisplayName("Тест проверяет корректность снятия средств с кошелька")
    public void testHandleOperationWithdraw() throws Exception {
        Wallet wallet = new Wallet()
                .setWalletId(UUID.randomUUID())
                .setBalance(BigDecimal.valueOf(100l));


        WalletOperationRequest request = new WalletOperationRequest();
        request.setWalletId(wallet.getWalletId());
        request.setOperationType(OperationType.WITHDRAW);
        request.setAmount(BigDecimal.valueOf(50));

        ArgumentCaptor<Wallet> walletCaptor = ArgumentCaptor.forClass(Wallet.class);
        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);

        doReturn(true).when(walletRepository).existsById(wallet.getWalletId());
        doReturn(Optional.of(wallet)).when(walletRepository).findById(wallet.getWalletId());

        mockMvc.perform(post("/api/v1/wallets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(walletRepository, times(1)).save(walletCaptor.capture());
        verify(transactionRepository, times(1)).save(transactionCaptor.capture());
        assertEquals(BigDecimal.valueOf(50l), walletCaptor.getValue().getBalance());
        assertEquals(request.getAmount(), transactionCaptor.getValue().getAmount());
        assertEquals(request.getOperationType(), transactionCaptor.getValue().getOperationType());
        assertEquals(wallet.getWalletId(), transactionCaptor.getValue().getWallet().getWalletId());
    }
}
