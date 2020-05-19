package patterns.behavioral.command;

import com.google.common.collect.Lists;

import java.util.List;

enum MoneyAction {
  WITHDRAW,
  DEPOSIT
}

interface Command {
  void call();

  void undo();
}

public class Demo {
  public static void main(String[] args) {
    BankAccount bankAccount = new BankAccount();
    System.out.println(bankAccount);

    List<BankAccountCommand> bankAccountCommands =
        List.of(
            new BankAccountCommand(bankAccount, MoneyAction.DEPOSIT, 200),
            new BankAccountCommand(bankAccount, MoneyAction.WITHDRAW, 2000));
    bankAccountCommands.forEach(Command::call);
    System.out.println(bankAccount);

    List<BankAccountCommand> reverse = Lists.reverse(bankAccountCommands);
    reverse.forEach(Command::undo);
    System.out.println(bankAccount);
  }
}

class BankAccount {
  private int balance;
  private int overdraftLimit = -500;

  @Override
  public String toString() {
    return "BankAccount{" + "balance=" + balance + ", overdraftLimit=" + overdraftLimit + '}';
  }

  public boolean deposit(int amount) {
    System.out.println("Depositing " + amount + " USD to current balance");
    balance += amount;
    System.out.println("Balance is now " + balance);
    return true;
  }

  public boolean withdraw(int amount) {
    if (balance - amount < overdraftLimit) {
      System.out.println(
          "Cannot withdraw "
              + amount
              + " USD. Current balance is "
              + balance
              + ". Overdraft Limit is "
              + overdraftLimit);
      return false;
    } else {
      System.out.println("Withdrawing " + amount + "USD.");
      balance -= amount;
      return true;
    }
  }
}

class BankAccountCommand implements Command {
  private BankAccount account;
  private MoneyAction action;
  private boolean succeeded;
  private int amount;

  public BankAccountCommand(BankAccount account, MoneyAction action, int amount) {
    this.account = account;
    this.action = action;
    this.amount = amount;
  }

  @Override
  public void call() {
    switch (action) {
      case DEPOSIT:
        succeeded = account.deposit(amount);
        break;
      case WITHDRAW:
        succeeded = account.withdraw(amount);
        break;
    }
  }

  @Override
  public void undo() {
    if (!succeeded) return;
    switch (action) {
      case DEPOSIT:
        account.withdraw(amount);
        break;
      case WITHDRAW:
        account.deposit(amount);
        break;
    }
  }
}
