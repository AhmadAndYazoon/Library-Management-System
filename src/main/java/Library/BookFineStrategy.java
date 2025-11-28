/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Library;

public class BookFineStrategy implements FineStrategy{
    @Override
    public int calculateFine(int overdueDays) {
        return 10*overdueDays;
    }
}
