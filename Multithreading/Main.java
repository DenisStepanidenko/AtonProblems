package Problem1FontOffice;

import java.util.concurrent.Semaphore;

public class Main {
    public static void main(String[] args) {



        /**
         * Инициализируем семафоры для всех персонажей
         */
        Semaphore semaphoreJoey = new Semaphore(1);
        Semaphore semaphoreChandler = new Semaphore(0);
        Semaphore semaphorePhoebe = new Semaphore(0);
        Semaphore semaphoreMonica = new Semaphore(0);

        /**
         * Инициализируем массив строк со словами всех персонажей
         */
        String[] wordsOfJoey = {"Hey , hey.", "Yes, I am. As of today. I am officially Joey Tribbiani, actor slash model.", "You know those posters for the City Free Clinic?", "No, but I hear lyme disease is open, so... (crosses fingers)", "Thanks."};
        String[] wordsOfChandler = {"Hey.", "And this from the cry-for-help department. Are you wearing makeup?", "That's so funny, cause I was thinking you look more like Joey Tribianni, man slash woman.", " Do you know which one you're gonna be?", "Good luck. man. I hope you get it."};
        String[] wordsOfPhoebe = {"Hey.", "What were you modeling for?", "You know, the asthma guy was really cute.",};
        String[] wordsOfMonica = {"Oh, wow, so you're gonna be one of those \"healthy, healthy, healthy guys\"?"};


        /**
         * Создаём персонажей
         */
        Character joey = new Character("Joey", semaphoreJoey, new Semaphore[]{semaphoreChandler, semaphoreChandler, semaphoreMonica, semaphoreChandler}, wordsOfJoey);
        Character chandler = new Character("Chandler", semaphoreChandler, new Semaphore[]{semaphorePhoebe, semaphoreJoey, semaphorePhoebe, semaphoreJoey, semaphoreJoey}, wordsOfChandler);
        Character phoebe = new Character("Phoebe", semaphorePhoebe, new Semaphore[]{semaphoreChandler, semaphoreJoey, semaphoreChandler}, wordsOfPhoebe);
        Character monica = new Character("Monica", semaphoreMonica, new Semaphore[]{semaphorePhoebe}, wordsOfMonica);


        /**
         * На основе созданных персонажей создаём потоки
         */
        Thread threadJoey = new Thread(joey);
        Thread threadChandler = new Thread(chandler);
        Thread threadPhoebe = new Thread(phoebe);
        Thread threadMonica = new Thread(monica);


        /**
         * Запускаем все потоки
         */
        threadJoey.start();
        threadChandler.start();
        threadMonica.start();
        threadPhoebe.start();
    }
}
