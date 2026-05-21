package abstractFactory;

/* The issue with Factory Design pattern is that it voilates the Open/Close principle of SOLID.
because we have to update the same Factory class for object creation if there is a new type of class introduced.
so if any thing fails than the entire factory will collapse so avoiid this Abstract Factory solution is introduced.

so in Abstract Factory pattern we create multiple factory which specifically creates a single type of object.

*/

/*
==================== CLASS DIAGRAM ====================

                <<interface>>
                    Car
            ---------------------
            + start() : void
            + stop()  : void
            ---------------------
                    ^
                    |
        ---------------------------
        |                         |
      BMW                       Audi
------------------     ------------------
+ start() : void       + start() : void
+ stop()  : void       + stop()  : void
------------------     ------------------


                <<interface>>
                   Factory
            -------------------------
            + createCar() : Car
            -------------------------
                    ^
                    |
        ---------------------------
        |                         |
   BMWFactory               AudiFactory
------------------     ------------------
+ createCar() : Car   + createCar() : Car
------------------     ------------------
        |                         |
        | creates                 | creates
        v                         v
      BMW                       Audi

=======================================================
*/


interface Car {

    public void start();
    public void stop();
    
}

class BMW implements Car{

    @Override
    public void start() {
        System.out.println("BMW is starting");
    }

    @Override
    public void stop() {
    System.out.println("BMW is stoping");
    }
    
}

class Audi implements Car{

    @Override
    public void start() {
    System.out.println("Audi is starting");
    }

    @Override
    public void stop() {
    System.out.println("Audi is stoping");
    }
    
}

    interface Factory {
        public Car createVehicle();
    }

    class BMWFactory implements Factory{

        public Car createVehicle() {
            return new BMW();
        }

    }

    class AudiFactory implements Factory{

        @Override
        public Car createVehicle() {
            return new Audi();
        }

    }

public class AbstractFactory {

    public static void main(String[] args) {
        System.out.println("Abstract Factory Pattern");

        // creating Audi Factory.
        
        Factory factory = new AudiFactory();
        Car car = factory.createVehicle();

        car.start();
        car.stop();
    }


    
}
