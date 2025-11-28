/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Library;

/**
 *
 * @author rahhal
 */
public class CDFineStrategy implements FineStrategy {
    @Override
    public int calculateFine(int overdueDays) {
        return 20*overdueDays;
    }
}
