package mst.local.mstsoftware.resources;

import lombok.Data;

@Data
public class SepayResource<T> {
    private int status;
    private String error;
    private SepayMessage messages;
    private T transactions;
}

@Data
class SepayMessage {
    private boolean success;
}
