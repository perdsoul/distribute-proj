package node;

import java.util.Scanner;

import static node.NodeContext.*;

public class Main {
    public static void printMenu() {
        System.out.println("-----MENU-------");
        System.out.println("1. upload");
        System.out.println("2. download");
        System.out.println("3. search file");
    }

    /**
     * get choice
     *
     * @param low
     * @param high
     * @return
     */
    public static int getchoice(Scanner sc, int low, int high) {
        int choice = 0;
        while (true) {
            System.out.println("please input your choice : ");
            try {
                choice = Integer.valueOf(sc.nextLine());
                if (choice < low || choice > high) {
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                continue;
            }
        }
        return choice;
    }

    public static void main(String[] args) throws InterruptedException {
        NodeServer.start(NodeContext.LOCAL_IP);
        NodeClient.start(NodeContext.START_IP, NodeContext.SERVER_POST);
        buildTopology();
        System.out.println(neighbors);

        Scanner sc = new Scanner(System.in);
        while (true) {
            printMenu();
            // 获取输入选项
            int choice = getchoice(sc, 1, 3);
            if (choice == 1) {
                System.out.println("please input your filename : ");
                String filename = sc.nextLine();
                uploadFile(filename);
            } else if (choice == 2) {
                System.out.println("please input filename : ");
                String filename = sc.nextLine();
                System.out.println("please input ip : ");
                String ip = sc.nextLine();
                downloadFile(filename, ip);
            } else if (choice == 3){
                System.out.println("please input search key : ");
                String key = sc.nextLine();
                System.out.println(searchFile(key));
            }
        }
    }
}
