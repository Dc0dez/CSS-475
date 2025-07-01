package final_project;
import java.sql.*;
import java.util.*;

public class MetroAPI {
    //API Driver
    public static void main(String[] args) {

        try {
            Connection connection = MetroDB.getConnection();
            Scanner scanner = new Scanner(System.in);
            String input;

            do {
                System.out.print("To see a full list of commands, enter list\n Enter a command: ");

                //string input
                input = scanner.nextLine();

                switch(input) {
                    case "c1":
                        MetroDB.ConfirmCreateBus(scanner, connection);
                        break;
                    case "r1":
                        MetroDB.ConfirmGetMinMaxPayingRoute(scanner, connection);
                        break;
                    case "r2":
                        MetroDB.ConfirmGetRouteRevenue(scanner, connection);
                        break;
                    case "r3":
                        MetroDB.ConfirmPM(scanner, connection);
                        break;
                    case "r4":
                        MetroDB.ConfirmGetAllPayments(scanner, connection);
                        break;
                    case "r5":
                        MetroDB.ConfirmGetPaymentsForRoute(scanner, connection);
                        break;
                    case "r6":
                        MetroDB.ConfirmGetBus(scanner, connection);
                        break;
                    case "r7":
                        MetroDB.ConfirmRouteInfo(scanner, connection);
                        break;
                    case "r8":
                        MetroDB.ConfirmAllStops(scanner, connection);
                        break;
                    case "r9":
                        MetroDB.ConfirmAllRoutes(scanner, connection);
                        break;
                    case "r10":
                        MetroDB.ConfirmGetUnusedBuses(scanner, connection);
                        break;
                    case "u1":
                        MetroDB.ConfirmAddBusToRoute(scanner, connection);
                        break;
                    case "a1":
                        MetroDB.ConfirmArchiveStop(scanner, connection);
                        break;
                    //payment
                    case "list":
                        System.out.println("\nc1: Add a new bus.");
                        System.out.println("r1: Get lowest or highest paying route." );
                        System.out.println("r2: Get the revenue for a route." );
                        System.out.println("r3: Get the amount of times different payment methods were used.");
                        System.out.println("r4: Retrieves the total volume of payments made.");
                        System.out.println("r5: Retrieves the total volume of payments made at a particular route.");
                        System.out.println("r6: Lists the buses assigned to a particular route.");
                        System.out.println("r7: Lists all the stops that are in a particular route.");
                        System.out.println("r8: Lists every stop with its information in the system.");
                        System.out.println("r9: Lists every route in the systems and their activity.");
                        System.out.println("r10: Returns a list of active buses assigned to no routes.");
                        System.out.println("u1: Assigns a bus to a route.");
                        System.out.println("a1: Archives a specified stop.");
                        System.out.println("Exit: Quit program\n");
                        break;
                }

            } while (!input.equalsIgnoreCase("exit"));

            System.out.println("Exiting");
            scanner.close();

            connection.close();
            System.out.println("Connection closed.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}