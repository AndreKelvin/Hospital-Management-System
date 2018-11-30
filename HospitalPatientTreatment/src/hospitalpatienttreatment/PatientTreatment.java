/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospitalpatienttreatment;

/**
 *
 * @author Andre Kelvin
 */
public class PatientTreatment {
    
    private String patientName;
    private String diagnosis;
    
    public PatientTreatment(String patientName, String diagnosis){
        this.patientName=patientName;
        this.diagnosis=diagnosis;
    }
    
    public String getPatientName(){
        return patientName;
    }
    
    public String getDiagnosis(){
        return diagnosis;
    }
    
}
