package listeners;

/**
 * Created by gandy on 07.04.15.
 *
 */
public interface ListenerSupport<T> {

    public void addListener(T listener);
    public void removeListener(T listener);

}
