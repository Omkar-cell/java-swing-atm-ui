/*
 * File: ATMMachine.java
 * Author: Greg Holden
 * Class: CMIS 242
 * Prof: Lana Caulfield
 * Date: April 10, 2016
 */
package atmmachine;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class ATMMachine implements ActionListener {
    
    // FIELDS
    private double amount;
    private final JButton DEPOSIT = new JButton("Deposit");
    private final JButton WITHDRAW = new JButton("Withdraw");
    private final JButton TRANSFER_TO = new JButton("Transfer To");
    private final JButton BALANCE = new JButton("Balance");
    private final JRadioButton CHECKING_RADIO_BTN = new JRadioButton("Checking");
    private final JRadioButton SAVINGS_RADIO_BTN = new JRadioButton("Savings");
    private final JTextField txtField = new JTextField("", 20);
    private JPanel mainPanel = new JPanel();
    private final Account myAccount = new Account(0,0); // account created at program start
    
    // CONSTRUCTOR
    public ATMMachine(){
        JFrame ATMFrame = new JFrame("ATM Machine");//create frame
        setFrame(ATMFrame); //set frame parameters
        mainPanel = buttonPanel(mainPanel);//get content panel
        ATMFrame.add(mainPanel);//add content panel
    } // end constructor
    
    // METHODS
    
    // PLACE BUTTONS AND TXTFIELD ONTO MAIN PANEL
    public JPanel buttonPanel(JPanel mainPanel) {
        this.mainPanel = mainPanel;
        mainPanel.setLayout(new BorderLayout());
        
        // Button Panel
        JPanel buttons = new JPanel();
        buttons.setLayout(new GridLayout(2, 2, 10, 10));
        buttons.setBorder(BorderFactory.createEmptyBorder(10,70,10,70));
        buttons.add(DEPOSIT);
        buttons.add(WITHDRAW);
        buttons.add(TRANSFER_TO);
        buttons.add(BALANCE);
        mainPanel.add(buttons, BorderLayout.NORTH);
        
        //Set Event Listeners
        DEPOSIT.addActionListener(this);
        WITHDRAW.addActionListener(this);
        TRANSFER_TO.addActionListener(this);
        BALANCE.addActionListener(this);
        
        // Radio Buttons and TextField
        JPanel radioButtons = new JPanel();
        radioButtons.setLayout(new FlowLayout(FlowLayout.CENTER));
        ButtonGroup group = new ButtonGroup();
        radioButtons.add(CHECKING_RADIO_BTN);
        radioButtons.add(SAVINGS_RADIO_BTN);
        radioButtons.add(txtField);
        group.add(CHECKING_RADIO_BTN);
        group.add(SAVINGS_RADIO_BTN);
        CHECKING_RADIO_BTN.setSelected(true);//pre-select Checking Account
        mainPanel.add(radioButtons, BorderLayout.CENTER);
        
        // Return mainPanel
        return mainPanel;
    }
    
    // SET JFRAME PARAMETERS
    public void setFrame(JFrame frame){
        frame.setLocationRelativeTo(null); // center frame
        frame.setResizable(false); // frame cannot be resized by user
        frame.setSize(360,210);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    // ACTION EVENTS FROM PRESSING JBUTTONS
    @Override
    public void actionPerformed (ActionEvent e) throws NumberFormatException {
        amount = 0; // reset amount to 0 after each button press
        boolean acct;
        try{
            // Pressing DEPOSIT Button
            if (e.getSource() == DEPOSIT){
                amount = Double.parseDouble(txtField.getText());
                txtField.setText(""); // clear JTextField
                if (amount > 0){
                    acct = getAccount();
                    if (acct == true){
                        myAccount.depositChecking(amount);
                    }
                    else {
                        myAccount.depositSavings(amount);
                    }
                }
                else {
                    JFrame dialogFrame = dialogFrame();
                    JOptionPane.showMessageDialog(dialogFrame, 
                        "Invalid deposit amount");
                    txtField.setText(""); 
                }
            }
            // Pressing WITHDRAW Button
            else if (e.getSource() == WITHDRAW){
                amount = Double.parseDouble(txtField.getText());
                txtField.setText(""); // clear JTextField
                try{
                    if (amount > 0){
                        acct = getAccount();
                        if (acct == true){
                            myAccount.withdrawFromChecking(amount);
                        }
                        else {
                            myAccount.withdrawFromSavings(amount);
                        }
                    }
                    else {
                        JFrame dialogFrame = dialogFrame();
                        JOptionPane.showMessageDialog(dialogFrame, 
                            "Invalid withdraw amount");
                    }
                }
                catch(InsufficientFunds ife){
                    ife.message();
                    txtField.setText("");
                }
            }
            // Pushing TRANSFER_TO Button
            else if (e.getSource() == TRANSFER_TO){
                amount = Double.parseDouble(txtField.getText());
                txtField.setText(""); // clear JTextField
                try{
                    if (amount > 0){
                        acct = getAccount();
                        if (acct == true){
                            myAccount.transfer(amount, true);
                        }
                        else {
                            myAccount.transfer(amount, false);
                        }
                    }
                    else {
                        JFrame dialogFrame = dialogFrame();
                        JOptionPane.showMessageDialog(dialogFrame, 
                            "Invalid transfer amount");
                    }
                }
                catch(InsufficientFunds ife){
                        ife.message();
                        txtField.setText(""); 
                    }
            }
            // Pushing BALANCE Button
            else if (e.getSource() == BALANCE){
                txtField.setText(""); // Clear JTextField
                acct = getAccount();
                if (acct == true){
                    myAccount.getCheckingBal();
                }
                else {
                    myAccount.getSavingsBal();
                }
            }
        }
        catch (NumberFormatException nfe){ // Exception when non-numeric value entered in txtField
            JFrame dialogFrame = dialogFrame();
            JOptionPane.showMessageDialog(dialogFrame, 
              "Input value must be numeric.");
            txtField.setText(""); 
        }
    }

    // CHECK WHICH ACCOUNT IS SELECTED BY RADIO BUTTONS
    public boolean getAccount(){
        return CHECKING_RADIO_BTN.isSelected() == true; // returns true for Checking Account
        // returns "true" for Savings Account
    }
    
    // CREATE JFRAME FOR JOPTIONPANE MESSAGE DISPLAY
    public JFrame dialogFrame(){
        JFrame dialogBox = new JFrame("Message");
        dialogBox.setLocationRelativeTo(null);
        dialogBox.setSize(100,50);
        dialogBox.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        return dialogBox;
    }
    
    // END ATMMACHINE CONSTRUCTION AND METHODS

    // START MAIN PROGRAM
    public static void main(String[] args) {
        ATMMachine myATM = new ATMMachine();
    }
    // END MAIN PROGRAM
    
} // End ATMMachine Class