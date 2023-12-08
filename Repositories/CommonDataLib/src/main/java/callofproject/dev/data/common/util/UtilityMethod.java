package callofproject.dev.data.common.util;

import java.text.Normalizer;

public final class UtilityMethod
{
    private UtilityMethod()
    {
    }

    public static String convert(String str)
    {
        str = Normalizer.normalize(str, Normalizer.Form.NFD);
        return str.replaceAll("[^\\p{ASCII}]", "").trim().toUpperCase().replaceAll("\\s+", "_");
    }
}
