package factory;
/* Factory Design Pattern:

The main reason of this pattern is that the object creation logic should be centralised, instead of manually creating the object
we make a centralised entity which is responsible for creating objects.
The issue might be that everyone would pass different parameters while making object so resolve this issue Factory Design pattern was introduced.


*/


/*
==================== CLASS DIAGRAM ====================

        +----------------------+
        |      Vehicle         |  <<interface>>
        +----------------------+
        | + start()            |
        | + stop()             |
        +----------^-----------+
                   |
     -----------------------------------------
     |                |                     |
+-----------+   +-----------+       +-----------+
|   Car     |   |   Bike    |       |   Truck   |
+-----------+   +-----------+       +-----------+
| + start() |   | + start() |       | + start() |
| + stop()  |   | + stop()  |       | + stop()  |
+-----------+   +-----------+       +-----------+

                   (creates objects)
                        |
                        v
              +----------------------+
              |   VehicleFactory     |
              +----------------------+
              | + createVehicle()    |
              +----------------------+

=======================================================
*/


import java.util.Scanner;

interface Vehicle{
    public void start();
    public void stop();
}

class Car implements Vehicle{

    @Override
    public void start() {
        System.out.println("Car is starting");
    }

    @Override
    public void stop() {
    System.out.println("Car is stoping");
    }
    
}

class Bike implements Vehicle{

    @Override
    public void start() {
    System.out.println("Bike is starting");
    }

    @Override
    public void stop() {
    System.out.println("Bike is stoping");
    }
    
}

class Truck implements Vehicle{

    @Override
    public void start() {
    System.out.println("Truck is starting");
    }

    @Override
    public void stop() {
    System.out.println("Truck is starting");
    }
    
}

class VehicleFactory{ // Centralised Factory which creates Vehicle and returrns them.
    public static Vehicle createVehicle(String vehicle){

        if(vehicle.equals("CAR"))
            return new Car();
        else if(vehicle.equals("TRUCK"))
            return new Truck();
        else if(vehicle.equals("BIKE"))
            return new Bike();
        else
            throw new IllegalArgumentException("Enter valid vehicle type!!");

    }
}

public class Factory{
    public static void main(String[] args) {
        System.out.println("Factory Design Pattern");

        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the type of Vehicle");
        String vehicle = scanner.nextLine();

        Vehicle vehicle2 = VehicleFactory.createVehicle(vehicle);
        vehicle2.start();
        vehicle2.stop();
    }
}