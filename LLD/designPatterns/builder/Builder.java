package builder;

/* 
 Builder class is used , when there are multiple parameters in the class and we want to build the object with some of the parameters.

*/

class Car{

  public String engine;
  public int wheels;
  public int seats;
  public String colour;
  public boolean sunRoof;
  public boolean navigationSystem;

  public Car(CarBuilder carBuilder){

    this.engine=carBuilder.engine;
    this.wheels = carBuilder.wheels;
    this.seats = carBuilder.seats;
    this.colour = carBuilder.color;
    this.sunRoof = carBuilder.sunRoof;
    this.navigationSystem = carBuilder.navigationSystem;

  }

  public String getEngine() {
    return engine;
  }

  public int getWheels() {
    return wheels;
  }
  public int getSeats() {
    return seats;
  }
  public String getColour() {
    return colour;
  }
  public boolean isSunRoof() {
    return sunRoof;
  }
  public boolean isNavigationSystem() {
    return navigationSystem;
  }

  static class CarBuilder{

  public String engine = "";
  public int wheels = 0;
  public int seats = 0;
  public String color = "";
  public boolean sunRoof = false;
  public boolean navigationSystem = false;

  public CarBuilder engine(String engine){
    this.engine = engine;
    return this;
  }

  public CarBuilder wheels(int wheels){
    this.wheels = wheels;
    return this;
  }

  public CarBuilder seats(int seats){
    this.seats = seats;
    return this;
  }

  public CarBuilder color(String color){
    this.color = color;
    return this;
  }

  public CarBuilder sunRoof(boolean sunRoof){
    this.sunRoof=sunRoof;
    return this;  
  }

  public CarBuilder navigationSystem(boolean navigationSystem){
    this.navigationSystem = navigationSystem;
    return this;
  }

  public Car build(){
    return new Car(this);
  }

}



}



public class Builder {

  public static void main(String[] args) {
      Car.CarBuilder builder = new Car.CarBuilder();

  Car car = builder.engine(null)
    .seats(5)
    .wheels(4)
    .color("Blue")
    .sunRoof(false)
    .navigationSystem(true)
    .build();

    System.out.println(car.colour);
  }
  
}
