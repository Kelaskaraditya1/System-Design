package stratergy;

/* Stratergy pattern is used when there are multiple ways of doing a single thing than we have to use the Stratergy pattern.
to avoid multiple if else statements.

bad practise : 

class public ProcessPayment{

public void makePayment(String paymentMethod){

if(paymentMethod=="Credit"){
      ...implementation
    }

    if(paymentMethod=="Debit"){
      ...implementation
    }

  }
    
}

we have to edit the same block , which violates the Open Close principle.

Good practise: 

1) craete a interface for payment method and create multiple concrete class for each payment method and implement the PaymentMethod interface.

  interface PaymentMethod{

    public void processPayment();

  }

  class Debit implements PaymentMethod { ... }

  class Credit implements PaymentMethod { ... }

2) create an instance of the interface where we have to use it, and use the functionality.

  class ProcessPayment{

  public PaymentMethod paymentMethod;

    public void processPayment(){

    paymentMethod.processPayment(); // instead of writing multiple if-else statements.

    }

  }

*/

interface PaymentMethod{

  public void processPayment();

}

class Debit implements PaymentMethod{

  @Override
  public void processPayment() {
    System.out.println("Payment with Debit completed");
  }
  
}


class Credit implements PaymentMethod{

  @Override
  public void processPayment() {
    System.out.println("Payment with Credit completed");
  }
  
}

class Cash implements PaymentMethod{

  @Override
  public void processPayment() {
    System.out.println("Payment with Cash completed");
  }

}

class Upi implements PaymentMethod{

  @Override
  public void processPayment() {
    System.out.println("Payment with Upi completed");
  }
  
}

class Payment{

  public PaymentMethod paymentMethod;

  public Payment(PaymentMethod paymentMethod){
    this.paymentMethod=paymentMethod;
  }

  public void processPayment(){
    paymentMethod.processPayment();
  }

}

public class Stratergy {

  public static void main(String[] args) {

    Upi upi = new Upi();

    Payment payment = new Payment(upi);
    payment.processPayment();
    
  }
  
}
