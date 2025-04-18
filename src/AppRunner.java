import enums.ActionLetter;
import model.*;
import util.UniversalArray;
import util.UniversalArrayImpl;

import java.util.Random;
import java.util.Scanner;

public class AppRunner {

    private final UniversalArray<Product> products = new UniversalArrayImpl<>();

    private final CoinAcceptor coinAcceptor;

    private final CardAcceptor cardAcceptor;

    private  Terminal terminal;


    private static boolean isExit = false;

    private AppRunner() {
        products.addAll(new Product[]{
                new Water(ActionLetter.B, 20),
                new CocaCola(ActionLetter.C, 50),
                new Soda(ActionLetter.D, 30),
                new Snickers(ActionLetter.E, 80),
                new Mars(ActionLetter.F, 80),
                new Pistachios(ActionLetter.G, 130)
        });

        coinAcceptor = new CoinAcceptor(100);
        cardAcceptor = new CardAcceptor(1500);
    }

    public static void run() {
        AppRunner app = new AppRunner();
        while (!isExit) {
            app.chosePay();
        }
    }

    private void chosePay(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Выберите способ оплаты");
        System.out.println("1 - монеты");
        System.out.println("2 - карта");
        String chose = sc.nextLine();
        switch (chose){
            case "1":
                terminal = coinAcceptor;
                print("В автомате доступны:");
                showProducts(products);
                coinPay();
                break;
            case  "2":
                terminal = cardAcceptor;
                print("В автомате доступны:");
                showProducts(products);
                cartPay();
                break;
        }
    }

    private void coinPay(){
        print("Монет на сумму: " + coinAcceptor.getSum());
        UniversalArray<Product> allowProducts = new UniversalArrayImpl<>();
        allowProducts.addAll(getAllowedProducts().toArray());
        chooseAction(allowProducts);
    }

    private void cartPay(){
        System.out.println("Вы оплачиваете картой");
        while (true) {
            boolean number = cartNumber();
            boolean password = cartPassword();
            if (!password && !number) {
                System.out.println("Неверные данные");
                return;
            }
            else {
                print("Сумма на карте: " + cardAcceptor.getSum());
                UniversalArray<Product> allowProducts = new UniversalArrayImpl<>();
                allowProducts.addAll(getAllowedProducts().toArray());
                chooseAction(allowProducts);
                break;
            }
        }

    }

    private static boolean cartNumber(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Введите шестизначный номер карты");
        System.out.println("Цифры должны быть в пределах от 100000 до 999999");
        while (true){
            int numberCart = sc.nextInt();
            if(numberCart <= 100000 || numberCart > 999999){
                System.out.println("Неверные данные");
                return false;
            } else {
                System.out.println("Номер карты приемлем");
                return true;
            }
        }
    }

    private static boolean cartPassword() {
        Random rnd = new Random();
        Scanner sc = new Scanner(System.in);
        int randomPassword = rnd.nextInt(1000);
        System.out.printf("Одноразовый пароль %s%n", randomPassword);
        int password = sc.nextInt();
        System.out.println();
        if (password == randomPassword) {
            return true;
        } else {
            return  false;
        }
    }





    private UniversalArray<Product> getAllowedProducts() {
        UniversalArray<Product> allowProducts = new UniversalArrayImpl<>();
        for (int i = 0; i < products.size(); i++) {
            if (terminal.getSum() >= products.get(i).getPrice()) {
                allowProducts.add(products.get(i));
            }
        }
        return allowProducts;
    }

    private void chooseAction(UniversalArray<Product> products) {
        showActions(products);
        print(" h - Выйти");
        String action = fromConsole().substring(0, 1);
        try {
            for (int i = 0; i < products.size(); i++) {
                if (products.get(i).getActionLetter().equals(ActionLetter.valueOf(action.toUpperCase()))) {
                    terminal.setSum(terminal.getSum() - products.get(i).getPrice());
                    print("Вы купили " + products.get(i).getName());
                    break;
                } else if ("h".equalsIgnoreCase(action)) {
                    isExit = true;
                    break;
                }
            }
        } catch (IllegalArgumentException e) {
            print("Недопустимая буква. Попрбуйте еще раз.");
            chooseAction(products);
        }


    }

    private void showActions(UniversalArray<Product> products) {
        for (int i = 0; i < products.size(); i++) {
            print(String.format(" %s - %s", products.get(i).getActionLetter().getValue(), products.get(i).getName()));
        }
    }

    private String fromConsole() {
        return new Scanner(System.in).nextLine();
    }

    private void showProducts(UniversalArray<Product> products) {
        for (int i = 0; i < products.size(); i++) {
            print(products.get(i).toString());
        }
    }

    private void print(String msg) {
        System.out.println(msg);
    }
}
