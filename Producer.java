public class Producer implements Runnable {

    private final Queue queue; // Объект очереди

    private int currentValue; // Текущий значение для поставки в очередь

    private final static int MaxDelay = 70;

    Producer(Queue queue) {

        this.queue = queue;

        this.currentValue = 0;

// запускаем новый поток, где запускаем метод run (имплементация Runnable)

        new Thread(this, "Prod ").start();

    }

    public void run() {

        try {

            while (true) {

// Если текущее значение уже было взято потребителем, создаём новое значение

                if (queue.getProductTaken()) {

                    currentValue++;

                }

                queue.put(currentValue); // Кладём значение в очередь

//Thread.sleep((int)(Math.random() * MaxDelay)); // Останвливаем выполнение потока на некоторый промежуток

            }

        } catch (InterruptedException e) {

        }

    }

}
