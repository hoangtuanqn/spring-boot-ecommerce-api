package mst.local.mstsoftware.services.interfaces;

import mst.local.mstsoftware.resources.SepayTransaction;

import java.util.List;

public interface SepayServiceInterface {
    public List<SepayTransaction> getTransaction(String accountNumber, String fromtDate, String toDate, int limit);
}
