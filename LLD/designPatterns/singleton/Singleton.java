package singleton;

class Logger{

  private static Logger instance;

  private Logger(){}

  public static  Logger createLogger(){

    if(instance==null){
      synchronized(Logger.class){
        if(instance==null)
          instance = new Logger();
      }
    }

    return instance;
  }

  public void loggerName(){
    System.out.println("Logger name:"+instance.toString());
  }

}

public class Singleton {
  
  /*
  
  Singleton patterns is used when we want a single instance of an object across the application, which maintains consistency.
  it can be used while creating, Database connection.

  If there is a task which is has to be done by only single object , no multiple objects than we have to use Singleton object.
  So to not allow any one else to craete a new instance and use the same instance, we make the constructor private.

  */

  public static void main(String[] args) {

    Logger logger1 = Logger.createLogger();

    logger1.loggerName();

    Logger logger2 = Logger.createLogger();

    logger2.loggerName();
    
  }
  
}
