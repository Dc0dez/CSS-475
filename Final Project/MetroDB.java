package final_project;
import java.sql.*;
import java.util.*;
import java.time.*;
import java.time.format.*;

public class MetroDB {

    private static final String URL = "jdbc:postgresql://localhost:5432/dbbus";
    private static final String USER = "postgres";
    private static final String PASSWORD = "";


    //Get connection for database
    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connection established successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Connection failed.");
        }
        return conn;
    }

    // Enter information for GetMinMaxPayingRoute API call
    public static void ConfirmGetMinMaxPayingRoute(Scanner scanner, Connection connection) {
        String start_time = "";
        String end_time = "";

        System.out.print("Highest [h] or lowest [l] paying route: ");
        String order = scanner.nextLine();
        if (order.equals("h")) {
            order = "DESC";
        } else if (order.equals("l")) {
            order = "ASC";
        } else {
            System.out.println("Invalid input code.");
            return;
        }

        System.out.print("Start date [yyyy-mm-dd] (optional):");
        start_time += scanner.nextLine();
        if (start_time.length() != 0) {
            System.out.print("Start time [hh:mm am/pm] (optional):");
            start_time += " " + scanner.nextLine();
        }

        System.out.print("End date [yyyy-mm-dd] (optional):");
        end_time += scanner.nextLine();
        if (end_time.length() != 0) {
            System.out.print("End time [hh:mm am/pm] (optional):");
            end_time += " " + scanner.nextLine();
        }

        if (start_time.length() == 0) {
            start_time = "1900-01-01";
        }
        if (end_time.length() == 0) {
            end_time = "2100-01-01";
        }

        GetMinMaxPayingRoute(order, start_time, end_time, connection);
    }

    // Parameter: order (string), start_time (string) (optional), end_time (string) (optional)
    // Returns: Route Number (string)
    // Implemented by Bryant Shea
    public static void GetMinMaxPayingRoute(String order, String start_time, String end_time, Connection connection) {

        String pay_query = "SELECT Route.number AS route\r\n" + //
            "FROM Route\r\n" + //
            "\tJOIN CustomerTrip ON (Route.ID = CustomerTrip.routeID)\r\n" + //
            "\tJOIN FareType ON (CustomerTrip.faretypeatcreation = FareType.ID)\r\n" + //
            "WHERE (CustomerTrip.onbus >= '" + start_time + "')\r\n" + //
            "\tAND (CustomerTrip.offbus <= '" + end_time + "')\r\n" + //
            "GROUP BY Route.number\r\n" + //
            "ORDER BY SUM(FareType.fare) " + order + "\r\n" + //
            "LIMIT 1;";

        try {
            PreparedStatement payStatement = connection.prepareStatement(pay_query);

            try (ResultSet rs = payStatement.executeQuery()) {
                if (rs.next()) {
                    String route = rs.getString("route");

                    String order_output;
                    if (order.equals("ASC")) {
                        order_output = "lowest";
                    } else {
                        order_output = "highest";
                    }

                    System.out.println("Route with the " + order_output + " revenue: " + route);
                } else {
                    System.out.println("No data found.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Enter information for GetRouteRevenue API call
    public static void ConfirmGetRouteRevenue(Scanner scanner, Connection connection) {
        String start_time = "";
        String end_time = "";

        System.out.print("Select route number: ");
        String s_route = scanner.nextLine();

        System.out.print("Start date [yyyy-mm-dd] (optional):");
        start_time += scanner.nextLine();
        if (start_time.length() != 0) {
            System.out.print("Start time [hh:mm am/pm] (optional):");
            start_time += " " + scanner.nextLine();
        }

        System.out.print("End date [yyyy-mm-dd] (optional):");
        end_time += scanner.nextLine();
        if (end_time.length() != 0) {
            System.out.print("End time [hh:mm am/pm] (optional):");
            end_time += " " + scanner.nextLine();
        }

        if (start_time.length() == 0) {
            start_time = "1900-01-01";
        }
        if (end_time.length() == 0) {
            end_time = "2100-01-01";
        }

        int route = Integer.parseInt(s_route);
        GetRouteRevenue(route, start_time, end_time, connection);
    }

    // Parameters: route number (int), start time (string) (optional), end time (string) (optioanl)
    // Returns: Route Revenue (string)
    // Implemented by Bryant Shea
    public static void GetRouteRevenue(int route_number, String start_time, String end_time, Connection connection) {

        String revenue_query = "SELECT SUM(FareType.fare) AS revenue\r\n" + //
            "FROM Route\r\n" + //
            "\tJOIN CustomerTrip ON (Route.ID = CustomerTrip.routeID)\r\n" + //
            "\tJOIN FareType ON (CustomerTrip.faretypeatcreation = FareType.ID)\r\n" + //
            "WHERE (CustomerTrip.onbus >= '" + start_time +"')\r\n" + //
            "\tAND (CustomerTrip.offbus <= '" + end_time + "')\r\n" + //
            "\tAND (Route.number = '" + route_number + "')\r\n" + //
            "GROUP BY Route.number;";
        
        try {
            PreparedStatement revenueStatement = connection.prepareStatement(revenue_query);

            try (ResultSet rs = revenueStatement.executeQuery()) {
                if (rs.next()) {
                    String revenue = rs.getString("revenue");

                    System.out.println("Revenue for route " + route_number + ": " + revenue);
                } else {
                    System.out.println("No data found.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Enter information for GetGetAllPayments API call
    public static void ConfirmGetAllPayments(Scanner scanner, Connection connection) {
        String start_time = "";
        String end_time = "";

        System.out.print("Start date [yyyy-mm-dd] (optional):");
        start_time += scanner.nextLine();
        if (start_time.length() != 0) {
            System.out.print("Start time [hh:mm am/pm] (optional):");
            start_time += " " + scanner.nextLine();
        }

        System.out.print("End date [yyyy-mm-dd] (optional):");
        end_time += scanner.nextLine();
        if (end_time.length() != 0) {
            System.out.print("End time [hh:mm am/pm] (optional):");
            end_time += " " + scanner.nextLine();
        }

        if (start_time.length() == 0) {
            start_time = "1900-01-01";
        }
        if (end_time.length() == 0) {
            end_time = "2100-01-01";
        }

        GetAllPayments(start_time, end_time, connection);
    }

    // Parameters: start time (string) (optional), end time (string), (optional)
    // Returns: Payment Volume (int)
    // Implemented by Bryant Shea
    public static void GetAllPayments(String start_time, String end_time, Connection connection) {
        
        String volume_query = "SELECT COUNT(*) AS volume\r\n" + //
            "FROM CustomerTrip\r\n" + //
            "WHERE (CustomerTrip.onbus >= '" + start_time + "')\r\n" + //
            "\tAND (CustomerTrip.offbus <= '" + end_time + "');\r\n";
        
        try {
           PreparedStatement volumeStatement = connection.prepareStatement(volume_query);
           
           try (ResultSet rs = volumeStatement.executeQuery()) {
                if (rs.next()) {
                    String volume = rs.getString("volume");

                    System.out.println("Payment volume: " + volume);
                } else {
                    System.out.println("No data found.");
                }
           }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Enter information for GetPaymentsForRoute API call
    public static void ConfirmGetPaymentsForRoute(Scanner scanner, Connection connection) {
        String start_time = "";
        String end_time = "";

        System.out.print("Select route number: ");
        String s_route = scanner.nextLine();

        System.out.print("Start date [yyyy-mm-dd] (optional):");
        start_time += scanner.nextLine();
        if (start_time.length() != 0) {
            System.out.print("Start time [hh:mm am/pm] (optional):");
            start_time += " " + scanner.nextLine();
        }

        System.out.print("End date [yyyy-mm-dd] (optional):");
        end_time += scanner.nextLine();
        if (end_time.length() != 0) {
            System.out.print("End time [hh:mm am/pm] (optional):");
            end_time += " " + scanner.nextLine();
        }

        if (start_time.length() == 0) {
            start_time = "1900-01-01";
        }
        if (end_time.length() == 0) {
            end_time = "2100-01-01";
        }

        int route = Integer.parseInt(s_route);

        GetPaymentsForRoute(route, start_time, end_time, connection);
    }

    // Parameters: route (int), start time (string) (optional), end time (string) (optional)
    // Returns: Payment Volume (int)
    // Implemented by Bryant Shea
    public static void GetPaymentsForRoute(int route, String start_time, String end_time, Connection connection) {
        
        String volume_query = "SELECT COUNT(*) AS volume\r\n" + //
            "FROM CustomerTrip\r\n" + //
            "WHERE (CustomerTrip.onbus >= '" + start_time + "')\r\n" + //
            "\tAND (CustomerTrip.offbus <= '" + end_time + "')\r\n" + //
            "\tAND (CustomerTrip.routeID = '" + route + "');";
        
        try {
            PreparedStatement volumeStatement = connection.prepareStatement(volume_query);

            try (ResultSet rs = volumeStatement.executeQuery()) {
                if (rs.next()) {
                    String volume = rs.getString("volume");

                    System.out.println("Payment volume for route " + route + ": " + volume);
                } else {
                    System.out.println("Payment volume for route " + route + ": 0");
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void ConfirmCreateBus(Scanner scanner, Connection connection) {
        System.out.println("");
        System.out.println("Double Decker - holds 70 passengers");
        System.out.println("Mini - holds 25 passengers");
        System.out.println("Trolley - holds 40 passengers");
        System.out.println("Articulated - holds 60 passengers");
        System.out.print("Select Bus Type: ");
        String bus_type = scanner.nextLine();
        System.out.print("Type in Bus Number: ");
        String bus_number = scanner.nextLine();
        CreateBus(bus_number, bus_type, connection);
    }

    // Implemented by Jonas Morden
    private static void CreateBus(String busNumber, String busType, Connection connection) {
        // List of valid bus types
        String[] validBusTypes = {"Double Decker", "Mini", "Trolley", "Articulated"};

        // Check if the bus type is valid
        boolean isValidBusType = false;
        for (String validType : validBusTypes) {
            if (validType.equalsIgnoreCase(busType)) {
                isValidBusType = true;
                break;
            }
        }

        // If the bus type is invalid, print error and return
        if (!isValidBusType) {
            System.out.println("Error: " + busType + " is an invalid bus type. ");
            System.out.println("Only the following bus types are accepted: Double Decker, Mini, Trolley, Articulated.");
            System.out.println("");
            return;
        }

        String checkBusTypeQuery = "SELECT id FROM bustype WHERE name ILIKE ?";
        // Check for duplicate bus number
        String checkBusNumberQuery = "SELECT id FROM bus WHERE number = ?";
        String insertBusQuery = "INSERT INTO bus (number, isactive, bustypeid) VALUES (?, true, ?)";

        try {
            // Check if bus type exists in the bustype table
            String busTypeId = "-1";
            try (PreparedStatement checkBusTypeStmt = connection.prepareStatement(checkBusTypeQuery)) {
                checkBusTypeStmt.setString(1, busType);
                try (ResultSet rs = checkBusTypeStmt.executeQuery()) {
                    if (rs.next()) {
                        busTypeId = rs.getString("id"); // Bus type exists, get its ID
                    }
                }
            }

            // If bus type doesn't exist, reject input
            if (busTypeId.equals("-1")) {
                System.out.println("Error: " + busTypeId + " is an invalid bus type. ");
                System.out.println("");
                return;
            }

            // Check if the bus number already exists, if it doesn't reject the input
            try (PreparedStatement checkBusNumberStmt = connection.prepareStatement(checkBusNumberQuery)) {
                checkBusNumberStmt.setString(1, busNumber);
                try (ResultSet rs = checkBusNumberStmt.executeQuery()) {
                    if (rs.next()) {
                        System.out.println("Error: Bus number " + busNumber + " already exists. Please enter a different bus number.");
                        System.out.println("");
                        return;
                    }
                }
            }

            // Insert the new bus into the bus table
            try (PreparedStatement insertBusStmt = connection.prepareStatement(insertBusQuery)) {
                insertBusStmt.setString(1, busNumber);
                insertBusStmt.setString(2, busTypeId);
                insertBusStmt.executeUpdate();
            }

            // Confirmation message
            System.out.println("Bus number " + busNumber + " of type " + busType + " has been created.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void ConfirmAddBusToRoute(Scanner scanner, Connection connection) {
        System.out.println("");
        System.out.print("Type in Bus Number: ");
        String bus_number = scanner.nextLine();
        System.out.print("Type in Route Number: ");
        String route_number = scanner.nextLine();
        AddBusToRoute(bus_number, route_number, connection);
    }

    // Implemented by Jonas Morden
    private static void AddBusToRoute(String busNumber, String routeNumber, Connection connection) {
        String checkBusQuery = "SELECT id FROM bus WHERE number = ?";
        String checkRouteQuery = "SELECT id FROM route WHERE number = ?";
        String checkAssignmentQuery = "SELECT * FROM busassignment WHERE busid = ? AND routeid = ?";
        String insertBusAssignmentQuery = "INSERT INTO busassignment (busid, routeid) VALUES (?, ?)";

        try {
            // Check if the bus number exists in the bus table
            int busId = -1;
            try (PreparedStatement checkBusStmt = connection.prepareStatement(checkBusQuery)) {
                checkBusStmt.setString(1, busNumber);
                try (ResultSet rs = checkBusStmt.executeQuery()) {
                    if (rs.next()) {
                        busId = rs.getInt("id");
                    }
                }
            }

            if (busId == -1) {
                System.out.println("Error: Bus with number " + busNumber + " does not exist.");
                return; // Exit method if bus does not exist
            }

            // Check if the route number exists in the route table
            int routeId = -1;
            try (PreparedStatement checkRouteStmt = connection.prepareStatement(checkRouteQuery)) {
                checkRouteStmt.setString(1, routeNumber);
                try (ResultSet rs = checkRouteStmt.executeQuery()) {
                    if (rs.next()) {
                        routeId = rs.getInt("id");
                    }
                }
            }
            // If route does not exist
            if (routeId == -1) {
                System.out.println("Error: Route with number " + routeNumber + " does not exist.");
                return;
            }

            // Check if the bus is already assigned to the route
            try (PreparedStatement checkAssignmentStmt = connection.prepareStatement(checkAssignmentQuery)) {
                checkAssignmentStmt.setInt(1, busId);
                checkAssignmentStmt.setInt(2, routeId);
                try (ResultSet rs = checkAssignmentStmt.executeQuery()) {
                    if (rs.next()) {
                        System.out.println("Error: Bus " + busNumber + " is already assigned to Route " + routeNumber);
                        return; // Exit method if bus is already assigned
                    }
                }
            }

            // insert a record into the busassignment table, assign bus to route
            try (PreparedStatement insertBusAssignmentStmt = connection.prepareStatement(insertBusAssignmentQuery)) {
                insertBusAssignmentStmt.setInt(1, busId);
                insertBusAssignmentStmt.setInt(2, routeId);
                insertBusAssignmentStmt.executeUpdate();
            }

            System.out.println("Bus " + busNumber + " has been successfully added to Route " + routeNumber);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void ConfirmAllStops(Scanner scanner, Connection connection) {// Dylan Nelson
    	System.out.println("Press enter for all stops or specify stop number: ");
    	String stop_number = scanner.nextLine();
    	GetAllStops(stop_number,connection);
    }
    
    // Implemented by Dylan Nelson
    public static void GetAllStops (String stop_number, Connection connection) { //Dylan Nelson
    	//Queries the stop information and associated route
    	String stops_query = "SELECT Stop.number AS stop_number, Stop.address, Stop.city, " + 
    						"Stop.zip, Stop.isactive, Route.number AS route_number " +
    						"FROM Stop " +
    							"JOIN Schedule ON (Stop.ID = Schedule.stopID) " +
    							"JOIN Route ON (Schedule.routeID = Route.ID) " +
    						"WHERE Stop.number LIKE ?";
    	
    	try (PreparedStatement get_stop_info = connection.prepareStatement(stops_query)) {
    		if (stop_number.isEmpty()) { // Returns all stops and associated data
    			stop_number = "%";
    		}
    		
    		get_stop_info.setString(1, stop_number); // Set the parameter in the prepared statement
    		
    		try (ResultSet rs = get_stop_info.executeQuery()) { //Execute query and get results
    			while (rs.next()) { //Iterate through each row of the result
    				//Extract data from the current row
    			String stop = rs.getString("stop_number"); //Get stop number
    			String stop_address = rs.getString("address"); //Get address
    			String stop_zip = rs.getString("zip"); //Get zip
    			boolean is_active = rs.getBoolean("isactive"); //Get active status 
    			String route = rs.getString("route_number"); //Get route number
    			//Display stop information
    			System.out.println("Stop: " + stop + " | Address: " + stop_address + " | Zip: "
    					+ stop_zip + " | Is active?: " + is_active + " | Route: " + route); 
    			}
    		}	//ResultSet automatically closed here
    	} catch (SQLException e) {
    		//Handle any SQL exceptions
    		e.printStackTrace(); //Print stack trace for debugging
    	}
    	System.out.println("");
    }
    
    public static void ConfirmAllRoutes(Scanner scanner, Connection connection) { //Dylan Nelson
    	  System.out.println("Press enter for all routes or specify route number: ");
    	    String route_number = scanner.nextLine();
    	    
    	    if (!route_number.isEmpty()) { //If user specifies stop, gives option for full or partial matching
    	        System.out.println("Do you want exact matching (E) or partial matching (P)? ");
    	        String matchChoice = scanner.nextLine().toUpperCase();
    	        boolean exactMatch = matchChoice.equals("E");
    	        
    	        GetAllRoutes(route_number, exactMatch, connection);
    	    } else {
    	        // Empty input means show all routes
    	        GetAllRoutes("", false, connection);
    	    }
    	}
    
    // Implemented by Dylan Nelson
    public static void GetAllRoutes(String route_number, boolean exactMatch, Connection connection) { //Dylan Nelson
    	String routes_query;
    	
    	if (exactMatch && !route_number.isEmpty()) {
    		//Queries the exact route number and if it is active
    		routes_query = "SELECT Route.number, Route.isactive " + 
					"FROM Route " +
					"WHERE Route.number = ?";
    	}else {
    		//Queries the route number and if it is active
    		routes_query = "SELECT Route.number, Route.isactive " + 
					"FROM Route " +
					"WHERE Route.number LIKE ?";
    	}
    	
    	try (PreparedStatement get_routes = connection.prepareStatement(routes_query)) {
    		if (route_number.isEmpty()) { // Returns all routes
    			route_number = "%";
    		} else if (!exactMatch) {
    			route_number = "%" + route_number + "%"; //Returns complete or partial route
    		}
    		
    		get_routes.setString(1, route_number); // Set the parameter in the prepared statement
    		
    		try(ResultSet rs = get_routes.executeQuery()) { // Execute query and get results
    			while (rs.next()) { // Iterate through each row of the result
    				// Extract data from the current row
    				String retrieved_route = rs.getString("number"); // Get route number
    				boolean is_active = rs.getBoolean("isactive"); // Get active status
    				// Display route information
    				System.out.println("Route: " + retrieved_route + " | is active?: " + is_active);
    			}
    		} // ResultSet automatically closed here
    	} catch (SQLException e) {
    		// Handle any SQL exceptions
    		e.printStackTrace(); // Print stack trace for debugging
    	}
    	System.out.println("");
    }


    public static void ConfirmArchiveStop(Scanner scanner, Connection connection) {
        System.out.print("Select Route Number: ");
        String route_number = scanner.nextLine();
        System.out.print("Select Stop Number: ");
        String stop_number = scanner.nextLine();
        ArchiveStop(route_number, stop_number, connection);
    }
    
    // Implemented by Jonas Morden
    public static void ArchiveStop(String route_number, String stop_number, Connection connection) {
        // Check whether a stop (identified by stop.number) exists on a specific route
        String checkQuery = "SELECT stop.id, schedule.sequence FROM stop " +
                "JOIN schedule ON stop.id = schedule.stopid " +
                "JOIN route ON schedule.routeid = route.id " +
                "WHERE route.number = ? AND stop.number = ?";
        // Marks the stop as inactive
        String archiveQuery = "UPDATE stop SET isactive = FALSE WHERE id = ?";
        // Adjusts the sequence numbers of the remaining stops on the route after a stop is archived
        String shiftSequenceQuery = "UPDATE schedule " +
                "SET sequence = sequence - 1 " +
                "WHERE routeid = (SELECT id FROM route WHERE number = ?) " +
                "AND sequence > ?";
    
        try {
            try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery);
                 PreparedStatement archiveStmt = connection.prepareStatement(archiveQuery);
                 PreparedStatement shiftStmt = connection.prepareStatement(shiftSequenceQuery)) {
    
                checkStmt.setString(1, route_number);
                checkStmt.setString(2, stop_number);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (!rs.next()) {
                        System.out.println("Error: Stop " + stop_number + " is not associated with Route " + route_number);
                        return;
                    }
                    // Get ID and sequence
                    int stopId = rs.getInt("id");
                    int stopSequence = rs.getInt("sequence");
    
                    // Archive the stop
                    archiveStmt.setInt(1, stopId);
                    int rowsUpdated = archiveStmt.executeUpdate();
    
                    if (rowsUpdated > 0) {
                        // Shift sequence numbers of remaining stops accordingly
                        shiftStmt.setString(1, route_number);
                        shiftStmt.setInt(2, stopSequence);
                        shiftStmt.executeUpdate();
    
                        System.out.println("Stop " + stop_number + " for Route " + route_number + " has been archived.");
                    } else {
                        System.out.println("Error: Could not archive stop.");
                    }
                }
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Confirmation for PMUsage
    public static void ConfirmPM(Scanner scanner, Connection connection) {
        String start_timestamp = "", end_timestamp = "", route_number = "", pay_method = "";
        boolean time_requested = false, route_requested = false;


        System.out.print("Do you want to select a route? (Press Y to confirm): ");
        String confirmation = scanner.nextLine();

        if (confirmation.equalsIgnoreCase("Y")) {
            System.out.print("Select Route: ");
            route_number = scanner.nextLine();
            route_requested = true;
        }

        System.out.print("Do you want to select a timeframe? (Press Y to confirm): ");
        confirmation = scanner.nextLine();

        if (confirmation.equalsIgnoreCase("Y")) {
            time_requested = true;
            System.out.print("Select Starting time (HH:MM AM/PM): ");
            String start_time = scanner.nextLine();

            System.out.print("Enter Starting Year (YYYY): ");
            String start_year = scanner.nextLine(); 
            System.out.print("Select Starting Month (MM): ");
            String start_month = scanner.nextLine(); 
            System.out.print("Select Starting Day (DD): ");
            String start_day = scanner.nextLine(); 
            String start_date = start_year + "-" + start_month + "-" + start_day;

            System.out.print("Select End Time (HH:MM AM/PM) : ");
            String end_time = scanner.nextLine();

            System.out.print("Enter End Year (YYYY): ");
            String end_year = scanner.nextLine(); 
            System.out.print("Select End Month (MM): ");
            String end_month = scanner.nextLine(); 
            System.out.print("Select End Day (DD): ");
            String end_day = scanner.nextLine(); 

            String end_date = end_year + "-" + end_month + "-" + end_day;

            //Test for valid dates
            try {
                // Define the formatters for 12-hour, 24-hour, and dates
                DateTimeFormatter date_format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                DateTimeFormatter format12Hour = DateTimeFormatter.ofPattern("h:mm a");
                DateTimeFormatter format24Hour = DateTimeFormatter.ofPattern("HH:mm:ss");


                // Parse/Confirm the input time to LocalTime, then  reformat to 24 hours for database
                LocalTime confirm_time = LocalTime.parse(start_time, format12Hour);
                start_time = confirm_time.format(format24Hour);

                LocalTime confirm_endtime = LocalTime.parse(end_time, format12Hour);
                end_time = confirm_endtime.format(format24Hour);

                LocalDate.parse(start_date, date_format);
                LocalDate.parse(end_date, date_format);

                start_timestamp = start_date + " " + start_time;
                end_timestamp = end_date + " "+ end_time;

                System.out.println(start_timestamp);
                System.out.println(end_timestamp);

            } catch (DateTimeParseException e) {
                System.out.println("Invalid date/time detected" + e.getMessage());
                return;
            }

        }
        //Ask for payment method
        
        if (time_requested || route_requested){
            System.out.print("Select Payment Method (Cash, ORCA Card): ");
            pay_method = scanner.nextLine();
        } 


        PMUsage(route_number, pay_method, start_timestamp, end_timestamp, connection);
    }

    
    
    //route -> payment selection (1, 2)
    //time -> payment selection (1, 2, 3)
    //Route AND time -> Requires payment selection (1, 2, 3, 4)
    //neither route and time -> no seleciton, display all payments
    // Implemented by Brian Huynh

    public static void PMUsage(String route_number, String pay_method, String start, String end, Connection connection) {

        int time_param_one = 2, time_param_two = 3;
        Timestamp startTimestamp = null, endTimestamp = null;

        String pm_query = """
                           SELECT paymentmethod.name AS method, COUNT(paymentmethod.name) AS payment_count\r
                           FROM customertrip\r
                           \tJOIN paymentmethod ON (paymentmethod.id = paymentmethodid)\r
                           \tJOIN faretype ON (faretype.id = faretypeatcreation)\r
                           \tJOIN route ON (route.id = routeid)\r\n""";

                //Complete the query based on parameters
                if (!route_number.isEmpty() || !start.isEmpty() && !end.isEmpty()) {

                    //payment first for either route/time
                    pm_query += "WHERE paymentmethod.name = ?\r\n ";

                    //route requested
                    if (!route_number.isEmpty()) {
                        pm_query += "AND route.number = ? \r\n "; 
                    }

                    //time requested
                    if (!start.isEmpty() && !end.isEmpty()) {

                        if (!route_number.isEmpty()) {
                            time_param_one = 3;
                            time_param_two = 4;
                        } 
                        startTimestamp = Timestamp.valueOf(start);
                        endTimestamp = Timestamp.valueOf(end);

                        pm_query +=  "AND (onbus >= ?  \r\n";
                        pm_query +=  "AND onbus <= ? ) \r\n" ;
                    }
                }

                pm_query += "GROUP BY paymentmethod.name, paymentmethodid"; //

        try {
            PreparedStatement fetchPM = connection.prepareStatement(pm_query);


            //Route or time is passed in
            if (!route_number.isEmpty() || (!start.isEmpty() && !end.isEmpty())) {

                //Payment method first
                fetchPM.setString(1, pay_method);

                //Route Second
                System.out.println("Payments for Route " + route_number);
                if (!route_number.isEmpty()) {
                    fetchPM.setString(2, route_number);
                }

                //Time third
                if (!start.isEmpty() && !end.isEmpty()) { 
                     System.out.println("Between " + startTimestamp + " - " + endTimestamp);
                    fetchPM.setTimestamp(time_param_one, startTimestamp);
                    fetchPM.setTimestamp(time_param_two, endTimestamp);
                }
            }

            try (ResultSet myRs = fetchPM.executeQuery()) {

                while (myRs.next()) {

                    String method_name = myRs.getString("method");
                    int payment = myRs.getInt("payment_count");

                    System.out.println (method_name + ": " + payment);
                }
                myRs.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();
      }
    }

    //Enter information for the bus
    public static void ConfirmGetBus(Scanner scanner, Connection connection) {
        System.out.print("Select Route: ");
        String route_number = scanner.nextLine();

        GetBus(route_number, connection);
    }
    //Parameter: 1 (route.number)
    //Returns:  Route Number (string), Bus Number(string), Bus Type (string), Capacity(integer)
    // Implemented by Dylan Nelson
    public static void GetBus(String route_number, Connection connection) {

        String bus_query = "SELECT route.number AS routenumber, bus.number AS busnumber, bustype.name, capacity\r\n" + //
                "FROM bus\r\n" + //
                "\tJOIN bustype ON (bustype.id = bustypeid)\r\n" + //
                "\tJOIN busassignment ON (busid = bus.id)\r\n" + //
                "\tJOIN route ON (route.id = busassignment.routeid) WHERE route.number = ?";

        try {
            PreparedStatement fetchBus = connection.prepareStatement(bus_query);
            fetchBus.setString(1, route_number);

            try (ResultSet myRs = fetchBus.executeQuery()) {
                System.out.println("Buses for Route: " + route_number);

                while (myRs.next()) {
                    String route = myRs.getString("routenumber");
                    String bus = myRs.getString("busnumber");
                    String type = myRs.getString("name");
                    int cap = myRs.getInt("capacity");

                    System.out.println (route + " " + bus + " " + type + " " + cap);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("");
    }

    public static void ConfirmRouteInfo(Scanner scanner, Connection connection) {
        System.out.print("Select Route: ");
        String route_number = scanner.nextLine();

        GetRouteInfo(route_number, connection);
    }

    //Parameter: 1 (route.number)
    //Returns:  Stop number (string), Address (string), city (string), zip(string), sequence (integer)
    // Implemented by Brian Huynh
    public static void GetRouteInfo(String route_number, Connection connection) {

        String route_query = "SELECT stop.number AS stop, address, city, zip, sequence\r\n" + //
                "FROM route\r\n" + //
                "\tJOIN schedule ON (routeid = route.id)\r\n" + //
                "\tJOIN stop ON (stop.id = stopid)\r\n" + //
                "WHERE route.number = ? AND Stop.IsActive = true";
        try {
            PreparedStatement fetchRoute = connection.prepareStatement(route_query);

            fetchRoute.setString(1, route_number);

            try (ResultSet myRs = fetchRoute.executeQuery()) {
                System.out.println("");
                System.out.println("Route " + route_number + " information:");
                System.out.printf("| %-8s | %-20s | %-20s | %-10s | %-12s |\n", "Stop #", "Address", "City", "Zip", "Sequence #");
                System.out.println("|----------|----------------------|----------------------|------------|--------------|");
                while (myRs.next()) {
                    String stop = myRs.getString("stop");
                    String address = myRs.getString("address");
                    String city = myRs.getString("city");
                    String zip = myRs.getString("zip");
                    int index = myRs.getInt("sequence");
                    System.out.printf("| %-8s | %-20s | %-20s | %-10s | %-12d |\n", stop, address, city, zip, index);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("");
    }

    // Enter information for GetUnusedBuses API call
    public static void ConfirmGetUnusedBuses(Scanner scanner, Connection connection) {
        System.out.println("Order buses by their capacity ascending [a] or descending [d]: ");

        String order = scanner.nextLine();
        if (order.equals("d")) {
            order = "DESC";
        } else if (order.equals("a")) {
            order = "ASC";
        } else {
            System.out.println("Invalid input code.");
            return;
        }

        GetUnusedBuses(order, connection);
    }

    // Parameters: ordering (string)
    // Returns: bus number (int), bus name (string), bus capacity (int)
    // Implemented by Bryant Shea
    public static void GetUnusedBuses(String order, Connection connection) {
        String bus_query = "SELECT Bus.number, BusType.name, BusType.capacity\r\n" + //
            "FROM BusType\r\n" + //
            "\tJOIN Bus ON (BusType.ID = Bus.bustypeID)\r\n" + //
            "\tLEFT JOIN BusAssignment ON (Bus.ID = BusAssignment.busID)\r\n" + //
            "WHERE BusAssignment.routeID IS NULL\r\n" + //
            "\tAND Bus.isactive = 'True'\r\n" + //
            "ORDER BY BusType.capacity " + order;
        
        try {
            PreparedStatement busStatement = connection.prepareStatement(bus_query);

            try (ResultSet rs = busStatement.executeQuery()) {
                System.out.println("Active buses not currently assigned to any route: ");
                System.out.printf("| %-5s | %-20s | %-5s |\n", "Bus #", "Bus Name", "Bus Capacity");
                System.out.println("|-------|----------------------|--------------|");

                while (rs.next()) {
                    String number = rs.getString("number");
                    String name = rs.getString("name");
                    String capacity = rs.getString("capacity");

                    System.out.printf("| %-5s | %-20s | %-12s |\n", number, name, capacity);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}