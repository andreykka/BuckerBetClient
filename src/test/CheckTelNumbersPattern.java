package test;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by gandy on 05.05.15.
 *
 */
public class CheckTelNumbersPattern {

    private static Pattern uaTelPat = Pattern.compile("^((8|\\+38?)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$");


    public static void main(String[] args) {
        List<String> strings = Arrays.asList("+380501724062",
                                            "0501724062", "+38050-17-24-062", "050-17-24-062", "+38(050)-17-240-62",
                                            "+38(050) 17 24 062", "+38(050)1724062", "+39(050)1724062", "+38(050)172     4062");
        System.out.println(" NOT MATCH ");
        strings.stream().filter(s -> !uaTelPat.matcher(s).matches()).forEach(System.out::println);
        System.out.println(" MATCHED ");
        strings.stream().filter(s -> uaTelPat.matcher(s).matches()).forEach(System.out::println);

    }
}
