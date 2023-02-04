package main;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class App {

    private Controller controller;

    //    --------------------------------------------------------------------

    private void mainMenu(Scanner sc) {
        while (true){
            printMenu();
            System.out.println("Your move?");
            String option = sc.nextLine().toLowerCase();

            switch (option) {
                case "0" -> System.exit(0);
                case "1" -> addMenu(sc);
                case "2" -> removeMenu(sc);
                case "3" -> searchMenu(sc);
                case "4" -> modifyMenu(sc);
                default -> System.out.println("Unknown option...");
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
            System.out.println("Your move?");
            String option = sc.nextLine().toLowerCase();

            switch (option) {
                case "0" -> mainMenu(sc);
                case "1" -> controller.createTables();
                case "2" -> addBookMenu(sc);
                case "3" -> addNewAuthorMenu(sc);
                case "4" -> addNewStoreMenu(sc);
                case "5" -> addBooksToStoreMenu(sc);
                default -> System.out.println("Unknown option...");
            }
        }
    }

    private void printAddMenu(){
        System.out.println("=".repeat(30));
        System.out.println("\t0 - Back to Main Menu...");
        System.out.println("\t1 - Create Tables");
        System.out.println("\t2 - Add New Book");
        System.out.println("\t3 - Add New Author");
        System.out.println("\t4 - Add New Store");
        System.out.println("\t5 - Add Books to Store");
        System.out.println("=".repeat(30));
    }

    private void addBookMenu(Scanner sc) {
        System.out.println("Do you want to add a new author? y/n");
        String author = sc.nextLine().toLowerCase();
        System.out.println("Do you want to add it to an existing store? y/n");
        String store = sc.nextLine().toLowerCase();

        Object[] book = addNewBook(sc, author, store);
        controller.addNewBook(book);

        System.out.println("Done!");

    }

    private Object[] addNewAuthor(Scanner sc){
        Object[] author = new Object[3];
        System.out.println("Author name?");
        author[0] = sc.nextLine();
        System.out.println("Author Dade of Birth? format: yyyy-mm-dd");
        author[1] = sc.nextLine();
        System.out.println("Author Gender? male/female");
        author[2] = sc.nextLine();

        return author;
    }

    private void addNewAuthorMenu(Scanner sc){
        Object[] author = addNewAuthor(sc);
        controller.addNewAuthor(author);
        System.out.println("Done!");
    }

    private Object[] addNewBook(Scanner sc, String author,String store){
        Object[] book = new Object[7];
        System.out.println("Book Title?");
        book[0] = sc.nextLine();
        System.out.println("Book ISBN?");
        book[1] = sc.nextLine();
        System.out.println("Book Edition?");
        book[2] = sc.nextInt(); sc.nextLine();
        System.out.println("Book publish date? format: yyyy-mm-dd");
        book[3] = sc.nextLine();

        if (author.equals("n")){
            controller.printAuthorsWithId();
            System.out.println("\nAuthor ID?");
            book[4] = sc.nextInt(); sc.nextLine();
        } else {
            book[4] = addNewAuthor(sc);
        }

        if (store.equals("y")){
            controller.printStoresWithId();
            System.out.println("Store ID?");
            book[5] = sc.nextInt(); sc.nextLine();
            System.out.println("Amount of Book to send to the Store?");
            book[6] = sc.nextInt(); sc.nextLine();
        }

        return book;
    }

    private Object[] addNewStore(Scanner sc){
        Object[] store = new Object[4];
        System.out.println("Store name?");
        store[0] = sc.nextLine();
        System.out.println("Store address?");
        store[1] = sc.nextLine();
        System.out.println("Store Owner?");
        store[2] = sc.nextLine();
        System.out.println("Want to make the store active? y/n");
        store[3] = sc.nextLine().toLowerCase();

        return store;
    }

    private void addNewStoreMenu(Scanner sc){
        Object[] store = addNewStore(sc);

        controller.addNewStore(store);

        System.out.println("Done!");
    }

    private void addBooksToStoreMenu(Scanner sc){
        controller.printStoresWithId();
        System.out.println("Which Store you want to add?\nPlease Write Store ID:");
        int sid = sc.nextInt(); sc.nextLine();

        if (controller.getStoreById(sid).equals("No such ID")){
            System.out.println("No such ID!\n\nReturning to Add menu...");
            return;
        } else {
            System.out.println(controller.getStoreById(sid));
        }

        System.out.println("How many book you want to add to the store?");
        int amount = sc.nextInt(); sc.nextLine();
        Map<Integer,Integer> bookIds = new HashMap<>();
        controller.printBooksWithId();
        System.out.println("Which books you want to add?\nPlease write Book ID-s");
        for (int i = 0; i < amount; i++) {
            int bid = Integer.parseInt(sc.nextLine());
            System.out.println("Amount to send: ");
            int bidAmount = Integer.parseInt(sc.nextLine());
            bookIds.put(bid,bidAmount);
        }
        controller.addBooksToStore(sid,bookIds);
        System.out.println("Done!");

    }

    //    --------------------------------------------------------------------

    private void removeMenu(Scanner sc){
        while (true){
            printRemoveMenu();
            System.out.println("Your move?");
            String option = sc.nextLine().toLowerCase();

            switch (option) {
                case "0" -> mainMenu(sc);
                case "1" -> removeAuthorMenu(sc);
                case "2" -> removeStoreMenu(sc);
                case "3" -> removeBookFromStoreMenu(sc);
//                case "4" ->
                default -> System.out.println("Unknown option...");
            }
        }
    }
    private void printRemoveMenu(){
        System.out.println("=".repeat(30));
        System.out.println("\t0 - Back to Main Menu...");
        System.out.println("\t1 - Remove Author");
        System.out.println("\t2 - Remove Store");
        System.out.println("\t3 - Remove Book from Store");
        System.out.println("=".repeat(30));
    }

    private void removeAuthorMenu(Scanner sc){
        controller.printAuthorsWithId();
        System.out.println("\n\nWhich Author you want to remove?\nPlease write the Author id:");
        int aid = sc.nextInt(); sc.nextLine();

        if (controller.getAuthorById(aid).equals("No such ID")){
            System.out.println("No such ID!\n\nReturning to remove menu...");
            return;
        } else {
            System.out.println(controller.getAuthorById(aid));
        }

        System.out.println("Are you sure you want to remove this Author? y/n");
        String option = sc.nextLine().toLowerCase();

        if (option.equals("y")){
            controller.removeAuthor(aid);
            System.out.println("Removed!");
        }
    }

    private void removeStoreMenu(Scanner sc){
        controller.printStoresWithId();
        System.out.println("\n\nWhich Store you want to remove?\nPlease write the Store ID:");
        int sid = sc.nextInt(); sc.nextLine();

        if (controller.getStoreById(sid).equals("No such ID")){
            System.out.println("No such ID!\n\nReturning to remove menu...");
            return;
        } else {
            System.out.println(controller.getStoreById(sid));
        }

        System.out.println("Are you sure you want to remove this Store? y/n");
        String option = sc.nextLine().toLowerCase();

        if (option.equals("y")){
            controller.removeStore(sid);
            System.out.println("Removed!");
        }
    }

    private void removeBookFromStoreMenu(Scanner sc){
        controller.printStoresWithId();
        System.out.println("\n\nWhich Store you want to remove books from?\nPlease write the Store ID:");
        int sid = sc.nextInt(); sc.nextLine();

        if (controller.getStoreById(sid).equals("No such ID")){
            System.out.println("No such ID!\n\nReturning to remove menu...");
            return;
        } else {
            System.out.println(controller.getStoreById(sid));
        }

        System.out.println("How many book you want to remove from the store?");
        int amount = sc.nextInt(); sc.nextLine();
        int[] bookIds = new int[amount];
        controller.printBooksWithId();
        System.out.println("Which books you want to remove?\nPlease write Book ID-s");
        for (int i = 0; i < bookIds.length; i++) {
            bookIds[i] = Integer.parseInt(sc.nextLine());
        }
        controller.removeBooksFromStore(sid,bookIds);
        System.out.println("Removed!");

    }

    //    --------------------------------------------------------------------

    private void searchMenu(Scanner sc){
        while (true){
            printSearchMenu();
            System.out.println("Your move?");
            String option = sc.nextLine().toLowerCase();

            switch (option) {
                case "0" -> mainMenu(sc);
                case "1" -> searchBookMenu(sc);
                case "2" -> searchAuthorMenu(sc);
                case "3" -> searchStoreMenu(sc);
                case "4" -> controller.print3StoreWithBooksBellow10();
                case "5" -> controller.printStoreBookAmount();
                default -> System.out.println("Unknown option...");
            }
        }
    }

    private void printSearchMenu(){
        System.out.println("=".repeat(30));
        System.out.println("\t0 - Back to Main Menu...");
        System.out.println("\t1 - Search Book");
        System.out.println("\t2 - Search Author");
        System.out.println("\t3 - Search Store");
        System.out.println("\t4 - Show Stores with low books");
        System.out.println("\t5 - Show Stores with book amounts");
        System.out.println("=".repeat(30));
    }

    private void searchBookMenu(Scanner sc){
        System.out.println("What do you want to search by?" +
                "\n\t1 - ISBN" +
                "\n\t2 - Title" +
                "\n\t3 - Author");
        int option = sc.nextInt(); sc.nextLine();
        switch (option) {
            case 1 -> {
                System.out.println("Write ISBN:");
                String isbn = sc.nextLine();
                System.out.println(controller.getBookByISBN(isbn));
            } case 2 -> {
                System.out.println("Write Title:");
                String title = sc.nextLine();
                System.out.println(controller.getBookByTittle(title));
            } case 3 -> {
                controller.printAuthorsWithId();
                System.out.println("\nPlease write the Author ID:");
                int aid = sc.nextInt(); sc.nextLine();
                System.out.println(controller.getBookByAuthor(aid));
            }default -> System.out.println("No such option\nReturning to search menu...");
        }
    }

    private void searchAuthorMenu(Scanner sc){
        System.out.println("What do you want to search by?" +
                "\n\t1 - Name" +
                "\n\t2 - Date of Birth" +
                "\n\t3 - Gender");
        int option = sc.nextInt(); sc.nextLine();
        switch (option) {
            case 1 -> {
                System.out.println("Write Name:");
                String name = sc.nextLine();
                System.out.println(controller.getAuthorByName(name));
            } case 2 -> {
                System.out.println("Write Date of Birth, format: yyyy-mm-dd");
                String dob = sc.nextLine();
                System.out.println(controller.getAuthorByDob(dob));
            } case 3 -> {
                System.out.println("Write the Gender, m/f :");
                String gender = sc.nextLine();
                System.out.println(controller.getAuthorByGender(gender));
            }default -> System.out.println("No such option\nReturning to search menu...");
        }
    }

    private void searchStoreMenu(Scanner sc){
        System.out.println("What do you want to search by?" +
                "\n\t1 - Address" +
                "\n\t2 - Owner" +
                "\n\t3 - Book");
        int option = sc.nextInt(); sc.nextLine();
        switch (option) {
            case 1 -> {
                System.out.println("Write Address:");
                String address = sc.nextLine();
                System.out.println(controller.getStoreByAddress(address));
            } case 2 -> {
                System.out.println("Write Owner's name:");
                String owner = sc.nextLine();
                System.out.println(controller.getStoreByOwner(owner));
            } case 3 -> {
                controller.printBooksWithId();
                System.out.println("\n\nWhich book you want to search by?\nPlease write the book ID:");
                int bid = sc.nextInt(); sc.nextLine();

                if (controller.getBookById(bid).equals("No such ID")){
                    System.out.println("No such ID!\n\nReturning to search menu...");
                    return;
                } else {
                    System.out.println(controller.getStoreByBookId(bid));
                }
            }default -> System.out.println("No such option\nReturning to search menu...");
        }
    }

    //    --------------------------------------------------------------------

    private void modifyMenu(Scanner sc){
        while (true){
            printModifyMenu();
            System.out.println("Your move?");
            String option = sc.nextLine().toLowerCase();

            switch (option) {
                case "0" -> mainMenu(sc);
                case "1" -> modifyBookMenu(sc);
                case "2" -> modifyAuthorMenu(sc);
                case "3" -> modifyActiveBookMenu(sc);
                case "4" -> modifyStoreMenu(sc);
                case "5" -> modifyActiveStoreMenu(sc);
                default -> System.out.println("Unknown option...");
            }
        }
    }

    private void printModifyMenu(){
        System.out.println("=".repeat(30));
        System.out.println("\t0 - Back to Main Menu...");
        System.out.println("\t1 - Modify Book");
        System.out.println("\t2 - Modify Author");
        System.out.println("\t3 - Modify Book activeness");
        System.out.println("\t4 - Modify Store");
        System.out.println("\t5 - Modify Store activeness");
        System.out.println("=".repeat(30));
    }

    private void modifyBookMenu(Scanner sc){
        controller.printBooksWithId();
        System.out.println("\n\nWhich book you want to modify?\nPlease write the book id:");
        int bid = sc.nextInt(); sc.nextLine();

        if (controller.getBookById(bid).equals("No such ID")){
            System.out.println("No such ID!\n\nReturning to modify menu...");
            return;
        } else {
            System.out.println(controller.getBookById(bid));
        }

        System.out.println("Please Write onto the correct place what do you want to modify\n" +
                "if you dont want to change it, press enter.");
        Object[] book = new Object[5];
        System.out.println("\n\t ISBN?");
        book[0] = sc.nextLine();
        System.out.println("\n\tDate of Publish? format: yyyy-mm-dd");
        book[1] = sc.nextLine();
        System.out.println("\n\tEdition?");
        book[2] = sc.nextInt(); sc.nextLine();
        System.out.println("\n\tTitle?");
        book[3] = sc.nextLine();

        System.out.println("You want to change the Author? y/n");
        String option = sc.nextLine().toLowerCase();

        if (option.equals("y")) {
            System.out.println("Existing Author? y/n");
            String option2 = sc.nextLine().toLowerCase();
            if (option2.equals("y")) {
                controller.printAuthorsWithId();
                System.out.println("\nWhich Author ID?");
                book[4] = sc.nextInt(); sc.nextLine();
            } else {
                book[4] = addNewAuthor(sc);
            }
        }
        controller.modifyBook(bid,book);
        System.out.println("Done!");
    }

    private void modifyActiveBookMenu(Scanner sc){
        System.out.println("1 - Change ACTIVE book to Inactive" +
                            "\n2 - Change INACTIVE book to Active");
        int option = sc.nextInt(); sc.nextLine();
        if (option == 1){
            controller.printActiveBookWithId();
        } else if (option == 2) {
            controller.printInactiveBookWithId();
        } else {
            System.out.println("No such option.\n\nReturning to modify menu...");
            return;
        }

        System.out.println("Please write Book ID:");
        int bid = sc.nextInt(); sc.nextLine();

        if (controller.getBookById(bid).equals("No such ID")){
            System.out.println("No such ID!\n\nReturning to modify menu...");
            return;
        } else {
            System.out.println(controller.getBookById(bid));
        }
        System.out.println("You sure you want to change the activeness? y/n");
        String option2 = sc.nextLine().toLowerCase();

        if (option2.equals("y")){
            controller.modifyBookActiveness(bid);
        }
        System.out.println(controller.getBookById(bid));
        System.out.println("Done!");
    }

    private void modifyActiveStoreMenu(Scanner sc){
        System.out.println("1 - Change ACTIVE Store to Inactive" +
                "\n2 - Change INACTIVE Store to Active");
        int option = sc.nextInt(); sc.nextLine();
        if (option == 1){
            controller.printActiveStoreWithId();
        } else if (option == 2) {
            controller.printInactiveStoreWithId();
        } else {
            System.out.println("No such option.\n\nReturning to modify menu...");
            return;
        }

        System.out.println("Please write Store ID:");
        int bid = sc.nextInt(); sc.nextLine();

        if (controller.getStoreById(bid).equals("No such ID")){
            System.out.println("No such ID!\n\nReturning to modify menu...");
            return;
        } else {
            System.out.println(controller.getStoreById(bid));
        }
        System.out.println("You sure you want to change the activeness? y/n");
        String option2 = sc.nextLine().toLowerCase();

        if (option2.equals("y")){
            controller.modifyStoreActiveness(bid);
        }
        System.out.println(controller.getStoreById(bid));
        System.out.println("Done!");
    }

    private void modifyAuthorMenu(Scanner sc){
        controller.printAuthorsWithId();
        System.out.println("\n\nWhich Author you want to modify?\nPlease write the Author id:");
        int aid = sc.nextInt(); sc.nextLine();

        if (controller.getAuthorById(aid).equals("No such ID")){
            System.out.println("No such ID!\n\nReturning to modify menu...");
            return;
        } else {
            System.out.println(controller.getAuthorById(aid));
        }
        System.out.println("Please Write onto the correct place what do you want to modify\n" +
                "if you dont want to change it, press enter.");
        Object[] author = new Object[3];
        System.out.println("\n\t Name?");
        author[0] = sc.nextLine();
        System.out.println("\n\tDate of Birth? format: yyyy-mm-dd");
        author[1] = sc.nextLine();
        System.out.println("\n\tGender? male/female");
        author[2] = sc.nextLine();

        controller.modifyAuthor(aid,author);
        System.out.println("Done!");

    }

    private void modifyStoreMenu(Scanner sc){
        controller.printStoresWithId();
        System.out.println("\n\nWhich Store you want to modify?\nPlease write the Store id:");
        int sid = sc.nextInt(); sc.nextLine();

        if (controller.getStoreById(sid).equals("No such ID")){
            System.out.println("No such ID!\n\nReturning to modify menu...");
            return;
        } else {
            System.out.println(controller.getStoreById(sid));
        }
        System.out.println("Please Write onto the correct place what do you want to modify\n" +
                "if you dont want to change it, press enter." +
                "\n\nto remove book, go to remove menu." +
                "\nto add book, go to add menu.");
        Object[] store = new Object[3];
        System.out.println("\n\tName?");
        store[0] = sc.nextLine();
        System.out.println("\n\tAddress?");
        store[1] = sc.nextLine();
        System.out.println("\n\tOwner?");
        store[2] = sc.nextLine();

        controller.modifyStore(sid,store);
        System.out.println("Done!");

    }

//    --------------------------------------------------------------------

    public static void main(String[] args) throws Exception {
        App m = new App();

        try (
                Scanner sc = new Scanner(System.in);
                Controller c = new Controller()
        ) {
//            m.controller.print3StoreWithBooksBellow10();
            m.controller = c;
            m.mainMenu(sc);
        }
    }

}
