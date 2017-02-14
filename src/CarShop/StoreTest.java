package CarShop;

import jdk.nashorn.internal.ir.annotations.Ignore;
import models.Car;
import models.Order;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by Mordr on 13.02.2017.
 */
class StoreTest {
    static Store store;

    @BeforeAll
    public static void InitStore() {
        store = new Store();
        assertNotNull(store);
    }

    @org.junit.jupiter.api.Test
    void createCar() {
        Store store = new Store();

        Car car = new Car(100, "Lada", "ABC");
        store.createCar(100, "Lada", "ABC");

        assertNotNull(store.getFreeCars());
        assertTrue(store.getFreeCars().size() > 0);

        store.getFreeCars().stream().forEach((car1) -> {
                assertEquals(100, car1.getPrice());
                assertEquals("Lada", car1.getModel());
                assertEquals("ABC", car1.getRegNum());
            }
        );
    }

    @org.junit.jupiter.api.Test
    void save() {

    }

    @org.junit.jupiter.api.Test
    void recover() {

    }

    @org.junit.jupiter.api.Test
    @Ignore
    void getFirstOrder() {
        Store store = new Store();
        store.getOrders();
    }

    @org.junit.jupiter.api.Test
    void sellCar() throws CarNotFoundException {
        Store store = new Store();
        assertThrows(
            CarNotFoundException.class,
            ()->store.sellCar("GAZ", "kopa", "epa", "123456789")
        );
        store.createCar(100, "Lada", "ABC");
        store.sellCar("Lada", "Epa", "Kopa", "123456789");

        assertTrue(store.getFreeCars().size() == 0);

        /*Car car = new Car(100, "Lada", "ABC");
        Order order = new Order(car, 1000, 123, (short)1);
        order.setCar(car);
        order.setOrderNumber(123);
        assertTrue(store.getOrders().contains());*/
        assertTrue(
                store.getOrders().stream().filter(
                (order) -> order.getCar().getModel().equals("Lada")
                        && order.getCar().getPrice() == 100
                        && order.getCar().getRegNum().equals("ABC")
                ).count() > 0
        );
        assertTrue(store.getContractList().size() == 1);
        assertTrue(
                store.getContractList().values().stream().filter(
                        (client) -> client.getFirstName().equals("Epa")
                        && client.getLastName().equals("Kopa")
                        && client.getLastName().equals("123456789")
        ).count() == 1);
    }

    @org.junit.jupiter.api.Test
    void getContractList() {
        Store store = new Store();
        assertNotNull(store.getContractList());
        assertTrue(store.getContractList().size() == 0);
    }

    @org.junit.jupiter.api.Test
    void getOrders() {

    }

    @org.junit.jupiter.api.Test
    void getFreeCars() {

    }

}