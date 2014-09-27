package de.switajski.priebes.flexibleorders.application;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.beanutils.PropertyUtilsBean;

public class BeanUtil {

    /**
     * from http://stackoverflow.com/questions/6099040/find-out-the-differences-
     * between-two-java-beans-for-version-tracking
     * 
     * @param oldObject
     * @param newObject
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     */
    public static List<String> getDifferencesOfObjects(Object oldObject, Object newObject)
            throws IllegalAccessException,
            InvocationTargetException,
            NoSuchMethodException {
        List<String> differences = new ArrayList<String>();

        BeanMap map = new BeanMap(oldObject);

        PropertyUtilsBean propUtils = new PropertyUtilsBean();

        for (Object propNameObject : map.keySet()) {
            String propertyName = (String) propNameObject;
            Object property1 = propUtils.getProperty(oldObject, propertyName);
            Object property2 = propUtils.getProperty(newObject, propertyName);

            String intro = propertyName + " (";
            if (property1 == null || property2 == null) {
                if (property1 == null && property2 == null) continue;
                else {
                    differences.add(property1 == null ?
                            intro + "null != \"" + property2 + "\")":
                            intro + "\"" + property2 + "\" != null)");
                }
            }

            if (!property1.equals(property2)) {
                differences.add(intro + "\"" + property1 + "\" != \"" + property2 + "\")");
            }
        }

        return differences;

    }
}
