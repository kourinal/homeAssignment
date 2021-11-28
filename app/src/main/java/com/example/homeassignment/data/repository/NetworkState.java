package com.example.homeassignment.data.repository;



public class NetworkState {

    public enum Status{
        RUNNING,
        SUCCESS,
        FAILED
    }
    public static final NetworkState LOADED = new NetworkState(Status.SUCCESS, "Success");
    public static final NetworkState LOADING = new NetworkState(Status.RUNNING, "Running");
    public static final NetworkState FAILED = new NetworkState(Status.FAILED, "Failed");


    private Status status;
    private String message;

    public NetworkState(Status status, String msg){
        this.status = status;
        this.message = msg;
    }

    public Status getStatus(){
        return status;
    }

    public String getMessage(){
        return message;
    }

}
