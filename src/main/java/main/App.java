package main;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;

public class App {

    private Controller controller;

    //    --------------------------------------------------------------------

    private void mainMenu(Scanner sc) {
        while (true){
            printMenu();
            System.out.println("Mit lepsz?");
            String option = sc.nextLine().toLowerCase();

            switch (option) {
                case "0" -> System.exit(0);
                case "1" -> addMenu(sc);
                case "2" -> removeMenu(sc);
                case "3" -> searchMenu(sc);
                case "4" -> modifyMenu(sc);
                default -> System.out.println("Nem ismert opcio...");
            }
        }
    }

    private void printMenu() {
        System.out.println("=".repeat(30));
        System.out.println("\t0 - Exit");
        System.out.println("\t1 - Add Menu...");
        System.out.println("\t2 - Remove Menu...");
        System.out.println("\t3 - Search Menu...");
        System.out.println("\t4 - Modify Menu...");
        System.out.println("=".repeat(30));
    }

    //    --------------------------------------------------------------------

    private void addMenu(Scanner sc){
        while (true){
            printAddMenu();
            System.out.println("Mit lepsz?");
            String option = sc.nextLine().toLowerCase();

            switch (option) {
                case "0" -> mainMenu(sc);
                case "1" -> controller.createTables();
//                case "2" ->
//                case "3" ->
//                case "4" ->
                default -> System.out.println("Nem ismert opcio...");
            }
        }
    }

    private void printAddMenu(){
        System.out.println("=".repeat(30));
        System.out.println("\t0 - Back to Main Menu...");
        System.out.println("\t1 - Create Tables");
        System.out.println("=".repeat(30));
    }

    //    --------------------------------------------------------------------

    private void removeMenu(Scanner sc){
        while (true){
            printRemoveMenu();
            System.out.println("Mit lepsz?");
            String option = sc.nextLine().toLowerCase();

            switch (option) {
                case "0" -> mainMenu(sc);
//                case "1" ->
//                case "2" ->
//                case "3" ->
//                case "4" ->
                default -> System.out.println("Nem ismert opcio...");
            }
        }
    }
    private void printRemoveMenu(){
        System.out.println("=".repeat(30));
        System.out.println("\t0 - Back to Main Menu...");
        System.out.println("=".repeat(30));
    }

    //    --------------------------------------------------------------------

    private void searchMenu(Scanner sc){
        while (true){
            printSearchMenu();
            System.out.println("Mit lepsz?");
            String option = sc.nextLine().toLowerCase();

            switch (option) {
                case "0" -> mainMenu(sc);
//                case "1" ->
//                case "2" ->
//                case "3" ->
//                case "4" ->
                default -> System.out.println("Nem ismert opcio...");
            }
        }
    }

    private void printSearchMenu(){
        System.out.println("=".repeat(30));
        System.out.println("\t0 - Back to Main Menu...");
        System.out.println("=".repeat(30));
    }

    //    --------------------------------------------------------------------

    private void modifyMenu(Scanner sc){
        while (true){
            printModifyMenu();
            System.out.println("Mit lepsz?");
            String option = sc.nextLine().toLowerCase();

            switch (option) {
                case "0" -> mainMenu(sc);
//                case "1" ->
//                case "2" ->
//                case "3" ->
//                case "4" ->
                default -> System.out.println("Nem ismert opcio...");
            }
        }
    }

    private void printModifyMenu(){
        System.out.println("=".repeat(30));
        System.out.println("\t0 - Back to Main Menu...");
        System.out.println("=".repeat(30));
    }

//    --------------------------------------------------------------------

    public static void main(String[] args) throws Exception {
        App m = new App();

        try (
                Scanner sc = new Scanner(System.in);
                Controller c = new Controller()
        ) {
            m.controller = c;
            m.mainMenu(sc);
        }
    }

}
