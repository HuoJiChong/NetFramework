package velly.Interface;

/**
 * 数据监听，返回给调用层
 *
 * @param <M>
 */
public interface IDataListener<M> {

    void onSuccess(M m);

    void onFail();
}
