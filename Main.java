public class Main {

    public static void main(String[] args){

// Создаём объект очереди

        Queue q = new Queue();

// Создаём производителя и двух потребителей

        new Producer(q);

        new Consumer(q,"Cons1");

        new Consumer(q,"Cons2");

    }

}
