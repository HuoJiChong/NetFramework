package com.derek.velly.Interface;

public interface IDataListener<M> {

    void onSuccess(M m);
    void onFail();
}
