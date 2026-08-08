package mst.local.mstsoftware.config;

import mst.local.mstsoftware.resources.SepayResource;
import mst.local.mstsoftware.resources.SepayTransaction;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;

@HttpExchange(contentType = "application/json")
public interface SepayApiClient {
    @GetExchange("/transactions/list")
    SepayResource<List<SepayTransaction>> getBankAccounts(
            @RequestParam("account_number") String accountNumber,
            @RequestParam("transaction_date_min") String fromDate,
            @RequestParam("transaction_date_max") String toDate,
            @RequestParam("limit") int limit
    );

    @GetExchange("/bank_accounts/list")
    SepayResource<List<SepayTransaction>> getBankAccounts();
}
