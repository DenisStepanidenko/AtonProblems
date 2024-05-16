package Problem1FontOffice;

import java.util.concurrent.Semaphore;

/**
 * Класс, который представляет собой персонажа из реплики
 */
public class Character implements Runnable {
    private final Semaphore currentSemaphore;
    private final Semaphore[] nextSemaphores;
    private final String[] words;
    private final String name;
    private int currentReplica;

    public Character(String name, Semaphore currentSemaphore, Semaphore[] nextSemaphores, String[] words) {
        this.name = name;
        this.currentSemaphore = currentSemaphore;
        this.nextSemaphores = nextSemaphores;
        this.words = words;
    }

    @Override
    public void run() {
        while (currentReplica < words.length) {
            try {
                /**
                 * Захватываем ресурс для текущего семафора
                 */
                currentSemaphore.acquire();
                System.out.println(name + ": " + words[currentReplica]);


                if(currentReplica == nextSemaphores.length){
                    /**
                     * Можно заметить, что для каждой реплики есть следующая, кроме последней - после неё ничего нет
                     * Именно этот случай мы обрабатываем здесь, чтобы избежать ArrayIndexOutOfBoundsException и успешно завершить этот поток
                     */
                    break;
                }

                /**
                 * Освобождаем ресурс семафора для следующей реплики
                 */
                nextSemaphores[currentReplica].release();
                currentReplica++;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
