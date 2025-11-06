import java.io.*;
import java.util.*;

public class BankingSystem {
    private static Scanner scanner = new Scanner(System.in);
    private static final String FILENAME = "record.txt";
    private static final String TEMP_FILENAME = "new.txt";
    private static final String PASSWORD = "cutmteam";
    
    // Date class
    static class Date {
        int month, day, year;
        
        Date() {}
        
        Date(int month, int day, int year) {
            this.month = month;
            this.day = day;
            this.year = year;
        }
        
        @Override
        public String toString() {
            return month + "/" + day + "/" + year;
        }
    }
    
    // Account class
    static class Account {
        String name = "";
        int accNo, age;
        String address = "";
        String citizenship = "";
        double phone;
        String accType = "";
        float amt;
        Date dob = new Date();
        Date deposit = new Date();
        Date withdrawl = new Date();
        
        // Method to parse account from file line
        public static Account parseFromLine(String line) {
            Account acc = new Account();
            String[] parts = line.split("\\s+");
            if (parts.length >= 13) {
                acc.accNo = Integer.parseInt(parts[0]);
                acc.name = parts[1];
                String[] dobParts = parts[2].split("/");
                acc.dob.month = Integer.parseInt(dobParts[0]);
                acc.dob.day = Integer.parseInt(dobParts[1]);
                acc.dob.year = Integer.parseInt(dobParts[2]);
                acc.age = Integer.parseInt(parts[3]);
                acc.address = parts[4];
                acc.citizenship = parts[5];
                acc.phone = Double.parseDouble(parts[6]);
                acc.accType = parts[7];
                acc.amt = Float.parseFloat(parts[8]);
                String[] depParts = parts[9].split("/");
                acc.deposit.month = Integer.parseInt(depParts[0]);
                acc.deposit.day = Integer.parseInt(depParts[1]);
                acc.deposit.year = Integer.parseInt(depParts[2]);
            }
            return acc;
        }
        
        // Method to convert account to file line
        public String toFileString() {
            return String.format("%d %s %d/%d/%d %d %s %s %.0f %s %.2f %d/%d/%d",
                accNo, name, dob.month, dob.day, dob.year, age, address, citizenship,
                phone, accType, amt, deposit.month, deposit.day, deposit.year);
        }
    }
    
    // Calculate interest
    public static float interest(float t, float amount, int rate) {
        return (rate * t * amount) / 100.0f;
    }
    
    // Delay function
    public static void forDelay(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    // Clear screen simulation
    public static void clearScreen() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }
    
    // Create new account
    public static void newAccount() {
        Account add = new Account();
        Account check = new Account();
        int choice;
        
        accountNo:
        while (true) {
            clearScreen();
            System.out.println("\t\t\t▓▓▓ ADD RECORD ▓▓▓▓");
            while (true) {
                try {
                    System.out.print("\n\n\nEnter today's date(mm/dd/yyyy): ");
                    String dateInput = scanner.next();
                    String[] dateParts = dateInput.split("/");
                    if (dateParts.length != 3) {
                        System.out.println("Invalid format! Please use mm/dd/yyyy format.");
                        continue;
                    }
                    add.deposit.month = Integer.parseInt(dateParts[0]);
                    add.deposit.day = Integer.parseInt(dateParts[1]);
                    add.deposit.year = Integer.parseInt(dateParts[2]);
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input! Please enter numbers in mm/dd/yyyy format.");
                }
            }
            
            System.out.print("\nEnter the account number: ");
            check.accNo = scanner.nextInt();
            
            // Check if account already exists
            try (BufferedReader br = new BufferedReader(new FileReader(FILENAME))) {
                String line;
                boolean exists = false;
                while ((line = br.readLine()) != null) {
                    Account existing = Account.parseFromLine(line);
                    if (existing.accNo == check.accNo) {
                        System.out.println("Account no. already in use!");
                        forDelay(2000);
                        continue accountNo;
                    }
                }
            } catch (IOException e) {
                // File doesn't exist, continue
            }
            break;
        }
        
        add.accNo = check.accNo;
        System.out.print("\nEnter the name: ");
        add.name = scanner.next();
        
        while (true) {
            try {
                System.out.print("\nEnter the date of birth(mm/dd/yyyy): ");
                String dobInput = scanner.next();
                String[] dobParts = dobInput.split("/");
                if (dobParts.length != 3) {
                    System.out.println("Invalid format! Please use mm/dd/yyyy format.");
                    continue;
                }
                add.dob.month = Integer.parseInt(dobParts[0]);
                add.dob.day = Integer.parseInt(dobParts[1]);
                add.dob.year = Integer.parseInt(dobParts[2]);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter numbers in mm/dd/yyyy format.");
            }
        }
        
        System.out.print("\nEnter the age: ");
        add.age = scanner.nextInt();
        System.out.print("\nEnter the address: ");
        add.address = scanner.next();
        System.out.print("\nEnter the citizenship number: ");
        add.citizenship = scanner.next();
        
        while (true) {
            try {
                System.out.print("\nEnter the phone number: ");
                add.phone = scanner.nextDouble();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a valid phone number.");
                scanner.next(); // Clear invalid input
            }
        }
        System.out.print("\nEnter the amount to deposit: ₹");
        add.amt = scanner.nextFloat();
        System.out.println("\nType of account:\n\t#Saving\n\t#Current\n\t#Fixed1(for 1 year)\n\t#Fixed2(for 2 years)\n\t#Fixed3(for 3 years)");
        System.out.print("\n\tEnter your choice: ");
        add.accType = scanner.next();
        
        // Write to file
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILENAME, true))) {
            pw.println(add.toFileString());
        } catch (IOException e) {
            System.out.println("Error writing to file!");
            return;
        }
        
        System.out.println("\nAccount created successfully!");
        
        addInvalid:
        while (true) {
            System.out.print("\n\n\n\t\tEnter 1 to go to the main menu and 0 to exit: ");
            int mainExit = scanner.nextInt();
            clearScreen();
            if (mainExit == 1) {
                menu();
                return;
            } else if (mainExit == 0) {
                close();
                return;
            } else {
                System.out.println("\nInvalid!");
            }
        }
    }
    
    // View customer list
    public static void viewList() {
        int test = 0;
        clearScreen();
        System.out.println("\nACC. NO.\tNAME\t\t\tADDRESS\t\t\tPHONE");
        
        try (BufferedReader br = new BufferedReader(new FileReader(FILENAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                Account acc = Account.parseFromLine(line);
                System.out.printf("\n%6d\t %10s\t\t\t%10s\t\t%.0f", 
                    acc.accNo, acc.name, acc.address, acc.phone);
                test++;
            }
        } catch (IOException e) {
            // File doesn't exist or error reading
        }
        
        if (test == 0) {
            clearScreen();
            System.out.println("\nNO RECORDS!!");
        }
        
        while (true) {
            System.out.print("\n\nEnter 1 to go to the main menu and 0 to exit: ");
            int mainExit = scanner.nextInt();
            clearScreen();
            if (mainExit == 1) {
                menu();
                return;
            } else if (mainExit == 0) {
                close();
                return;
            } else {
                System.out.println("\nInvalid!");
            }
        }
    }
    
    // Edit account information
    public static void edit() {
        Account upd = new Account();
        int choice, test = 0;
        
        System.out.print("\nEnter the account no. of the customer whose info you want to change: ");
        upd.accNo = scanner.nextInt();
        
        List<Account> accounts = new ArrayList<>();
        boolean found = false;
        
        // Read all accounts
        try (BufferedReader br = new BufferedReader(new FileReader(FILENAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                Account acc = Account.parseFromLine(line);
                if (acc.accNo == upd.accNo) {
                    found = true;
                    test = 1;
                    System.out.println("\nWhich information do you want to change?\n1.Address\n2.Phone");
                    System.out.print("\nEnter your choice(1 for address and 2 for phone): ");
                    choice = scanner.nextInt();
                    clearScreen();
                    
                    if (choice == 1) {
                        System.out.print("Enter the new address: ");
                        acc.address = scanner.next();
                        clearScreen();
                        System.out.println("Changes saved!");
                    } else if (choice == 2) {
                        System.out.print("Enter the new phone number: ");
                        acc.phone = scanner.nextDouble();
                        clearScreen();
                        System.out.println("Changes saved!");
                    }
                }
                accounts.add(acc);
            }
        } catch (IOException e) {
            System.out.println("Error reading file!");
            return;
        }
        
        // Write back all accounts
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILENAME))) {
            for (Account acc : accounts) {
                pw.println(acc.toFileString());
            }
        } catch (IOException e) {
            System.out.println("Error writing to file!");
            return;
        }
        
        if (test != 1) {
            clearScreen();
            System.out.println("\nRecord not found!!");
            while (true) {
                System.out.print("\nEnter 0 to try again, 1 to return to main menu and 2 to exit: ");
                int mainExit = scanner.nextInt();
                clearScreen();
                if (mainExit == 1) {
                    menu();
                    return;
                } else if (mainExit == 2) {
                    close();
                    return;
                } else if (mainExit == 0) {
                    edit();
                    return;
                } else {
                    System.out.println("\nInvalid!");
                }
            }
        } else {
            System.out.print("\n\n\nEnter 1 to go to the main menu and 0 to exit: ");
            int mainExit = scanner.nextInt();
            clearScreen();
            if (mainExit == 1) {
                menu();
            } else {
                close();
            }
        }
    }
    
    // Transaction (deposit/withdraw)
    public static void transact() {
        Account transaction = new Account();
        int choice, test = 0;
        
        System.out.print("Enter the account no. of the customer: ");
        transaction.accNo = scanner.nextInt();
        
        List<Account> accounts = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(FILENAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                Account acc = Account.parseFromLine(line);
                if (acc.accNo == transaction.accNo) {
                    test = 1;
                    if (acc.accType.equalsIgnoreCase("fixed1") || 
                        acc.accType.equalsIgnoreCase("fixed2") || 
                        acc.accType.equalsIgnoreCase("fixed3")) {
                        System.out.println("\n\nYOU CANNOT DEPOSIT OR WITHDRAW CASH IN FIXED ACCOUNTS!!!!!");
                        forDelay(2000);
                        clearScreen();
                        menu();
                        return;
                    }
                    
                    System.out.println("\n\nDo you want to\n1.Deposit\n2.Withdraw?");
                    System.out.print("\nEnter your choice(1 for deposit and 2 for withdraw): ");
                    choice = scanner.nextInt();
                    
                    if (choice == 1) {
                        System.out.print("Enter the amount you want to deposit: ₹");
                        transaction.amt = scanner.nextFloat();
                        acc.amt += transaction.amt;
                        System.out.println("\n\nDeposited successfully!");
                    } else {
                        System.out.print("Enter the amount you want to withdraw: ₹");
                        transaction.amt = scanner.nextFloat();
                        if (transaction.amt > acc.amt) {
                            System.out.println("\n\nInsufficient balance!");
                        } else {
                            acc.amt -= transaction.amt;
                            System.out.println("\n\nWithdrawn successfully!");
                        }
                    }
                }
                accounts.add(acc);
            }
        } catch (IOException e) {
            System.out.println("Error reading file!");
            return;
        }
        
        // Write back all accounts
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILENAME))) {
            for (Account acc : accounts) {
                pw.println(acc.toFileString());
            }
        } catch (IOException e) {
            System.out.println("Error writing to file!");
            return;
        }
        
        if (test != 1) {
            System.out.println("\n\nRecord not found!!");
            while (true) {
                System.out.print("\n\n\nEnter 0 to try again, 1 to return to main menu and 2 to exit: ");
                int mainExit = scanner.nextInt();
                clearScreen();
                if (mainExit == 0) {
                    transact();
                    return;
                } else if (mainExit == 1) {
                    menu();
                    return;
                } else if (mainExit == 2) {
                    close();
                    return;
                } else {
                    System.out.println("\nInvalid!");
                }
            }
        } else {
            System.out.print("\nEnter 1 to go to the main menu and 0 to exit: ");
            int mainExit = scanner.nextInt();
            clearScreen();
            if (mainExit == 1) {
                menu();
            } else {
                close();
            }
        }
    }
    
    // Delete account
    public static void erase() {
        Account rem = new Account();
        int test = 0;
        
        System.out.print("Enter the account no. of the customer you want to delete: ");
        rem.accNo = scanner.nextInt();
        
        List<Account> accounts = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(FILENAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                Account acc = Account.parseFromLine(line);
                if (acc.accNo != rem.accNo) {
                    accounts.add(acc);
                } else {
                    test++;
                    System.out.println("\nRecord deleted successfully!");
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file!");
            return;
        }
        
        // Write back remaining accounts
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILENAME))) {
            for (Account acc : accounts) {
                pw.println(acc.toFileString());
            }
        } catch (IOException e) {
            System.out.println("Error writing to file!");
            return;
        }
        
        if (test == 0) {
            System.out.println("\nRecord not found!!");
            while (true) {
                System.out.print("\nEnter 0 to try again, 1 to return to main menu and 2 to exit: ");
                int mainExit = scanner.nextInt();
                if (mainExit == 1) {
                    menu();
                    return;
                } else if (mainExit == 2) {
                    close();
                    return;
                } else if (mainExit == 0) {
                    erase();
                    return;
                } else {
                    System.out.println("\nInvalid!");
                }
            }
        } else {
            System.out.print("\nEnter 1 to go to the main menu and 0 to exit: ");
            int mainExit = scanner.nextInt();
            clearScreen();
            if (mainExit == 1) {
                menu();
            } else {
                close();
            }
        }
    }
    
    // View account details
    public static void see() {
        Account check = new Account();
        int test = 0, rate;
        int choice;
        float time, intrst;
        
        System.out.print("Do you want to check by\n1.Account no\n2.Name\nEnter your choice: ");
        choice = scanner.nextInt();
        
        try (BufferedReader br = new BufferedReader(new FileReader(FILENAME))) {
            String line;
            if (choice == 1) {
                System.out.print("Enter the account number: ");
                check.accNo = scanner.nextInt();
                
                while ((line = br.readLine()) != null) {
                    Account acc = Account.parseFromLine(line);
                    if (acc.accNo == check.accNo) {
                        clearScreen();
                        test = 1;
                        
                        System.out.printf("\nAccount NO.: %d\nName: %s\nDOB: %d/%d/%d\nAge: %d\nAddress: %s\nCitizenship No: %s\nPhone number: %.0f\nType Of Account: %s\nAmount deposited: ₹%.2f\nDate Of Deposit: %d/%d/%d\n\n",
                            acc.accNo, acc.name, acc.dob.month, acc.dob.day, acc.dob.year, acc.age, acc.address, acc.citizenship, acc.phone, acc.accType, acc.amt, acc.deposit.month, acc.deposit.day, acc.deposit.year);
                        
                        calculateInterest(acc);
                    }
                }
            } else if (choice == 2) {
                System.out.print("Enter the name: ");
                check.name = scanner.next();
                
                while ((line = br.readLine()) != null) {
                    Account acc = Account.parseFromLine(line);
                    if (acc.name.equalsIgnoreCase(check.name)) {
                        clearScreen();
                        test = 1;
                        
                        System.out.printf("\nAccount No.: %d\nName: %s\nDOB: %d/%d/%d\nAge: %d\nAddress: %s\nCitizenship No: %s\nPhone number: %.0f\nType Of Account: %s\nAmount deposited: ₹%.2f\nDate Of Deposit: %d/%d/%d\n\n",
                            acc.accNo, acc.name, acc.dob.month, acc.dob.day, acc.dob.year, acc.age, acc.address, acc.citizenship, acc.phone, acc.accType, acc.amt, acc.deposit.month, acc.deposit.day, acc.deposit.year);
                        
                        calculateInterest(acc);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file!");
            return;
        }
        
        if (test != 1) {
            clearScreen();
            System.out.println("\nRecord not found!!");
            while (true) {
                System.out.print("\nEnter 0 to try again, 1 to return to main menu and 2 to exit: ");
                int mainExit = scanner.nextInt();
                clearScreen();
                if (mainExit == 1) {
                    menu();
                    return;
                } else if (mainExit == 2) {
                    close();
                    return;
                } else if (mainExit == 0) {
                    see();
                    return;
                } else {
                    clearScreen();
                    System.out.println("\nInvalid!");
                }
            }
        } else {
            System.out.print("\nEnter 1 to go to the main menu and 0 to exit: ");
            int mainExit = scanner.nextInt();
            if (mainExit == 1) {
                clearScreen();
                menu();
            } else {
                clearScreen();
                close();
            }
        }
    }
    
    // Calculate and display interest
    private static void calculateInterest(Account acc) {
        float time, intrst;
        int rate;
        
        if (acc.accType.equalsIgnoreCase("fixed1")) {
            time = 1.0f;
            rate = 9;
            intrst = interest(time, acc.amt, rate);
            System.out.printf("\n\nYou will get ₹%.2f as interest on %d/%d/%d", intrst, acc.deposit.month, acc.deposit.day, acc.deposit.year + 1);
        } else if (acc.accType.equalsIgnoreCase("fixed2")) {
            time = 2.0f;
            rate = 11;
            intrst = interest(time, acc.amt, rate);
            System.out.printf("\n\nYou will get ₹%.2f as interest on %d/%d/%d", intrst, acc.deposit.month, acc.deposit.day, acc.deposit.year + 2);
        } else if (acc.accType.equalsIgnoreCase("fixed3")) {
            time = 3.0f;
            rate = 13;
            intrst = interest(time, acc.amt, rate);
            System.out.printf("\n\nYou will get ₹%.2f as interest on %d/%d/%d", intrst, acc.deposit.month, acc.deposit.day, acc.deposit.year + 3);
        } else if (acc.accType.equalsIgnoreCase("saving")) {
            time = (1.0f / 12.0f);
            rate = 8;
            intrst = interest(time, acc.amt, rate);
            System.out.printf("\n\nYou will get ₹%.2f as interest on %d of every month", intrst, acc.deposit.day);
        } else if (acc.accType.equalsIgnoreCase("current")) {
            System.out.println("\n\nYou will get no interest");
        }
    }
    
    // Close/Exit message
    public static void close() {
        System.out.println("\n\n\n\nThis Java Project is developed by KIRGS TEAM CUTM!");
    }
    
    // Main menu
    public static void menu() {
        int choice;
        clearScreen();
        System.out.println("\n\n\t\t\tCUSTOMER ACCOUNT BANKING MANAGEMENT SYSTEM");
        System.out.println("\n\n\n\t\t\t▓▓▓▓▓▓▓ WELCOME TO MY BANKING SYSTEM (KIRGS TEAM CUTM ) ▓▓▓▓▓▓▓");
        System.out.println("\n\n\t\t1. Create new account");
        System.out.println("\t\t2. Update information of existing account");
        System.out.println("\t\t3. For transactions");
        System.out.println("\t\t4. Check the details of existing account");
        System.out.println("\t\t5. Removing existing account");
        System.out.println("\t\t6. View customer's list");
        System.out.println("\t\t7. Exit");
        System.out.print("\n\n\n\n\n\t\t Enter your choice: ");
        choice = scanner.nextInt();
        
        clearScreen();
        switch (choice) {
            case 1:
                newAccount();
                break;
            case 2:
                edit();
                break;
            case 3:
                transact();
                break;
            case 4:
                see();
                break;
            case 5:
                erase();
                break;
            case 6:
                viewList();
                break;
            case 7:
                close();
                break;
            default:
                System.out.println("Invalid choice!");
                menu();
        }
    }
    
    // Main method
    public static void main(String[] args) {
        String pass, password = "cutmteam";
        
        System.out.print("\n\n\t\tEnter the password to login: ");
        pass = scanner.next();
        
        if (pass.equals(password)) {
            System.out.print("\n\nPassword Match!\nLOADING");
            for (int i = 0; i <= 6; i++) {
                forDelay(500);
                System.out.print(".");
            }
            clearScreen();
            menu();
        } else {
            System.out.println("\n\nWrong password!!");
            while (true) {
                System.out.print("\nEnter 1 to try again and 0 to exit: ");
                int mainExit = scanner.nextInt();
                if (mainExit == 1) {
                    clearScreen();
                    main(args);
                    break;
                } else if (mainExit == 0) {
                    clearScreen();
                    close();
                    break;
                } else {
                    System.out.println("\nInvalid!");
                    forDelay(1000);
                    clearScreen();
                }
            }
        }
        scanner.close();
    }
}