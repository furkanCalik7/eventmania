package com.database.eventmania.backend.service;

import com.database.eventmania.backend.entity.Wallet;
import com.database.eventmania.backend.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class WalletService {
    private final WalletRepository walletRepository;

    @Autowired
    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }


    public Wallet getWalletById(String walletId) throws SQLException{
        return walletRepository.getWalletById(Integer.valueOf(walletId));
    }

    public boolean createWallet(Integer balance) throws SQLException {
        return walletRepository.createWallet(balance);
    }

    public boolean deleteWallet(Integer walletId) throws SQLException{
        return walletRepository.deleteWallet(walletId);
    }

    public boolean updateWallet(Integer walletId, Integer balance) throws SQLException {
        return walletRepository.updateWallet(walletId, balance);
    }
    public boolean addMoney(Integer walletId, Integer amount) throws SQLException {
        return walletRepository.addMoney(walletId, amount);
    }
    public boolean withdrawMoney(Integer walletId, Integer amount) throws SQLException {
        return walletRepository.withdrawMoney(walletId, amount);
    }
    public boolean buyTicket(Integer buyerWalletId, Integer sellerWalletId, Integer amount) throws SQLException {
        return walletRepository.buyTicket(buyerWalletId, sellerWalletId, amount);
    }
}
