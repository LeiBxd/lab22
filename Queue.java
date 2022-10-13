import java.util.concurrent.locks.*;

public class Queue {

    private final int[] queueItems = new int[10]; // Элементы очереди

    private int begin = 0; // Начало очереди

    private int end = queueItems.length - 1; // Конец очереди

    private final ReentrantLock factoryLock = new ReentrantLock(); // Блокировщик

    private static ReentrantLock lock = new ReentrantLock(); // Блокировщик для print

    private final Condition conditionFull = factoryLock.newCondition(); // Условие для блокировки (заполнена ли очередь полностью)

    private final Condition conditionEmpty = factoryLock.newCondition(); // Условие для блокировки (пустая ли очередь)

    private boolean productTaken = false; // Был ли последний элемент очереди взят потребителем

// Добавить элемент в очередь

    public void put(int i) throws InterruptedException {

        

// Блокируем очередь

        factoryLock.lock();

// Ставим статус, что значение не было принято потребителем
        setProductTaken(false);

        try {

// Если очередь заполнена, то ждём сигнала, пока в очереди освободиться место

            while (full()) {

                conditionFull.await();

            }

// Смещаем индекс последнего элемента и присваиваем значение

            end = next(end);

            queueItems[end] = i;

// Вывод очереди на экран

            System.out.print(Thread.currentThread().getName() + " [put] (" + i + ") ");

            print();

// Оповещаем, что очередь не пуста

            conditionEmpty.signalAll();

        } finally {

// Разблокируем очередь

            factoryLock.unlock();

        }

    }

// Взять элемент из очереди

    public int get() throws InterruptedException {

// Блокируем очередь

        factoryLock.lock();

        try {

// Если очередь не заполнена, то ждём сигнала, пока очередь в очередь не поступит новый элемент

            while (empty()) {

                conditionEmpty.await();

            }

// Ставим статус, что значение было принято потребителем

            setProductTaken(true);

// Сохраняем элемент

            int result = queueItems[begin];

// Смещаем индекс первого элемента

            begin = next(begin);

// Вывод очереди на экран

            System.out.print(Thread.currentThread().getName() + " [get] (" + result + ") ");

            print();

// Оповещаем, что очередь пуста

            conditionFull.signalAll();

// Возвращаем элемент

            return result;

        } finally {

// Разблокируем очередь

            factoryLock.unlock();

        }

    }

//Сеттер для productTaken

    private void setProductTaken(boolean status) {

        productTaken = status;

    }

// Геттер для productTaken

    public boolean getProductTaken() {

        return productTaken;

    }

// Заполнена ли полностью очередь

    public boolean full() {

        return next(next(end)) == begin;

    }

// Пустая ли очередь

    public boolean empty() {

        return next(end) == begin;

    }

// Следующий индекс в очереди после N

    private int next(int n) {
        return (n + 1) % queueItems.length;

    }

// Вывод на экран элементов очереди

    private void print() {

        lock.lock();

        for (int i = begin; i != next(end); i = next(i)) {

            System.out.print(queueItems[i] + " ");

        }

        System.out.println();

        lock.unlock();

    }

}
