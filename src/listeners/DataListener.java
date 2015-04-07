package listeners;

import pojo.OutputData;

import java.util.List;

/**
 * Created by gandy on 07.04.15.
 *
 */

public interface DataListener {

    public void dataObtained(List<OutputData> data);

}
