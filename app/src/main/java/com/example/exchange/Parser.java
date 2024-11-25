package com.example.exchange;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class Parser {
    public static List<CurrencyRate> parseXML(InputStream stream) {
        List<CurrencyRate> rates = new ArrayList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(stream);

            NodeList items = document.getElementsByTagName("item");
            for (int i = 0; i < items.getLength(); i++) {
                Element item = (Element) items.item(i);
                String targetCurrency = getElementText(item, "targetCurrency");
                String targetName = getElementText(item, "targetName");
                double rate = Double.parseDouble(getElementText(item, "exchangeRate"));

                rates.add(new CurrencyRate(targetCurrency, targetName, rate));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rates;
    }

    private static String getElementText(Element parent, String tagName) {
        return parent.getElementsByTagName(tagName).item(0).getTextContent();
    }
}