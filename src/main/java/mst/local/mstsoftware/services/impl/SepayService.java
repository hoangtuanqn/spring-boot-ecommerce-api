package mst.local.mstsoftware.services.impl;

import lombok.AllArgsConstructor;
import mst.local.mstsoftware.config.SepayApiClient;
import mst.local.mstsoftware.resources.SepayTransaction;
import mst.local.mstsoftware.services.interfaces.SepayServiceInterface;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SepayService implements SepayServiceInterface {
    private final SepayApiClient sepayApiClient;

    @Override
    public List<SepayTransaction> getTransaction(String accountNumber, String fromtDate, String toDate, int limit) {
        var response = sepayApiClient.getBankAccounts(accountNumber, fromtDate, toDate, limit);
        if (response.getStatus() != 200) {
            throw new RuntimeException(response.getError());
        }
        return response.getTransactions();
    }
}
