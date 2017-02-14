import CarShop.CarNotFoundException;
import CarShop.Store;
import models.Order;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import java.io.*;

/**
 * Created by sa on 08.02.17.
 */
public class Main {
    private static final Logger logger = Logger.getLogger(Main.class);
    static  {
        DOMConfigurator.configure("src/resources/log4j.xml");
    }
    public static void main(String[] args) {
        Store store = new Store();
        logger.trace("store created");
        store.createCar(500000, "kia-rio","B146AA");
        /*try {
            store.sellCar("kia-rio",
                    "Jhon",
                    "Konner" ,
                    "+79126241898");
        } catch (CarNotFoundException e) {
            e.printStackTrace();
        }
        */
        store.save();
    }
}
