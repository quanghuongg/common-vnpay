package vn.vnpay.utils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;

public class MarshallerUtil {
    public static <T> String objectToXMLString(Class<T> tClass, Object data) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(tClass);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            StringWriter sw = new StringWriter();
            jaxbMarshaller.marshal(data, sw);
            return sw.toString();
        } catch (Exception e) {
            return e.getMessage();
        }

    }

    public static <T> T XMLStringToObject(Class<T> tClass, String xmlString) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(tClass);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            Object obj = jaxbUnmarshaller.unmarshal(new StringReader(xmlString));
            if (obj != null)
                return (T) obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}


