package ua.skillsup.weather;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.*;


public class App {

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(6);
        Integer[] degrees = new Integer[5];
        String[] sites = {"gismeteo.ua", "sinoptik.ua", "yandex.ua", "bbc.co.uk", "weather.com"};

        for (int i = 0; i <= 4; i++) {
            executorService.execute(new Walker(i, degrees, sites));
        }

        executorService.execute(new Runnable() {
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(5 * 1000);
                        double sum = 0;
                        System.out.println();
                        System.out.println("On time " + LocalDateTime.now());
                        for (int i = 0; i <= 4; i++) {
                            System.out.println("Temperature on " + sites[i] + " is " + degrees[i]);
                            sum += degrees[i];
                        }
                        double avg = sum / 5;
                        System.out.println("Average temperature in Dnipropetrovsk is " + avg + " C" + "\n");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        return;
                    }
                }
            }
        });

        Thread.sleep(3 * 10 * 1000);
        executorService.shutdownNow();

    }

    private static class Walker implements Runnable {

        private int id;
        private Integer[] degrees;
        private String[] sites;

        public Walker(int id, Integer[] degrees, String[] sites) {
            this.id = id;
            this.degrees = degrees;
            this.sites = sites;
        }

        public void run() {
            int min = 1;
            int max = 5;
            int value;
            Random random = new Random();
            while (true) {
                try {
                    Thread.sleep(100 * random.nextInt(30));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }
                value = ThreadLocalRandom.current().nextInt(min, max + 1);
                degrees[id] = value;
                System.out.println("Temperature on " + sites[id] + " update to " + value);
            }
        }
    }
}