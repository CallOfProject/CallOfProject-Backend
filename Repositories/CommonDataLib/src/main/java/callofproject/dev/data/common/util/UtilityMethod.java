package callofproject.dev.data.common.util;

import java.text.Normalizer;
import java.util.Locale;

public final class UtilityMethod
{
    private UtilityMethod()
    {
    }

    //@Deprecated
    /*public static String convert(String str)
    {
        str = Normalizer.normalize(str, Normalizer.Form.NFD);
        return str.replaceAll("[^\\p{ASCII}]", "").trim().toUpperCase().replaceAll("\\s+", "_");
    }*/

    public static String convert(String str)
    {
        String normalized = Normalizer.normalize(str, Normalizer.Form.NFD);
        String ascii = normalized.replaceAll("[^\\p{ASCII}]", "");
        String upperCase = ascii.toUpperCase(Locale.ENGLISH);
        return upperCase.replaceAll("\\s+", "_");
    }
}
