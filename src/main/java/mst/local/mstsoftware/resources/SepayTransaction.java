package mst.local.mstsoftware.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Optional;

@Data
public class SepayTransaction {
    private String id;
    @JsonProperty("bank_brand_name")
    private String bankBrandName;

    @JsonProperty("account_number")
    private String accountNumber;

    @JsonProperty("transaction_date")
    private String transactionDate;

    @JsonProperty("amount_out")
    private String amountOut;

    @JsonProperty("amount_in")
    private String amountIn;

    private String accumulated;

    @JsonProperty("transaction_content")
    private String transactionContent;

    @JsonProperty("reference_number")
    private String referenceNumber;

    private Optional<String> code;

    @JsonProperty("sub_account")
    private Optional<String> subAccount;

    @JsonProperty("bank_account_id")
    private String bankAccountId;

}