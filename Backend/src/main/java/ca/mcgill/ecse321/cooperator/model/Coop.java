package ca.mcgill.ecse321.cooperator.model;

import javax.persistence.Entity;
import java.sql.Date;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Id;

@Entity
public class Coop{
   private int year;

public void setYear(int value) {
    this.year = value;
}
public int getYear() {
    return this.year;
}
private Semester semester;

public void setSemester(Semester value) {
    this.semester = value;
}
public Semester getSemester() {
    return this.semester;
}
private Date startDate;

public void setStartDate(Date value) {
    this.startDate = value;
}
public Date getStartDate() {
    return this.startDate;
}
private Date endDate;

public void setEndDate(Date value) {
    this.endDate = value;
}
public Date getEndDate() {
    return this.endDate;
}
private String location;

public void setLocation(String value) {
    this.location = value;
}
public String getLocation() {
    return this.location;
}
private String jobDescription;

public void setJobDescription(String value) {
    this.jobDescription = value;
}
public String getJobDescription() {
    return this.jobDescription;
}
private Boolean needWorkPermit;

public void setNeedWorkPermit(Boolean value) {
    this.needWorkPermit = value;
}
public Boolean getNeedWorkPermit() {
    return this.needWorkPermit;
}
private Boolean employerConfirmation;

public void setEmployerConfirmation(Boolean value) {
    this.employerConfirmation = value;
}
public Boolean getEmployerConfirmation() {
    return this.employerConfirmation;
}
private Student student;

@ManyToOne(optional=false)
public Student getStudent() {
   return this.student;
}

public void setStudent(Student student) {
   this.student = student;
}

private Employer employer;

@ManyToOne(optional=false)
public Employer getEmployer() {
   return this.employer;
}

public void setEmployer(Employer employer) {
   this.employer = employer;
}

private TasksWorkloadReport tasksWorkloadReport;

@OneToOne(mappedBy="coop" )
public TasksWorkloadReport getTasksWorkloadReport() {
   return this.tasksWorkloadReport;
}

public void setTasksWorkloadReport(TasksWorkloadReport tasksWorkloadReport) {
   this.tasksWorkloadReport = tasksWorkloadReport;
}

private TechnicalReport technicalReport;

@OneToOne(mappedBy="coop" )
public TechnicalReport getTechnicalReport() {
   return this.technicalReport;
}

public void setTechnicalReport(TechnicalReport technicalReport) {
   this.technicalReport = technicalReport;
}

private CoopPlacementProof coopPlacementProof;

@OneToOne
public CoopPlacementProof getCoopPlacementProof() {
   return this.coopPlacementProof;
}

public void setCoopPlacementProof(CoopPlacementProof coopPlacementProof) {
   this.coopPlacementProof = coopPlacementProof;
}

private TaxFormInsruction taxFormInsruction;

@OneToOne(optional=false)
public TaxFormInsruction getTaxFormInsruction() {
   return this.taxFormInsruction;
}

public void setTaxFormInsruction(TaxFormInsruction taxFormInsruction) {
   this.taxFormInsruction = taxFormInsruction;
}

private TaxForm taxForm;

@ManyToOne(optional=false)
public TaxForm getTaxForm() {
   return this.taxForm;
}

public void setTaxForm(TaxForm taxForm) {
   this.taxForm = taxForm;
}

private AcceptanceForm acceptanceForm;

@OneToOne
public AcceptanceForm getAcceptanceForm() {
   return this.acceptanceForm;
}

public void setAcceptanceForm(AcceptanceForm acceptanceForm) {
   this.acceptanceForm = acceptanceForm;
}

private EmployerContract employerContract;

@OneToOne
public EmployerContract getEmployerContract() {
   return this.employerContract;
}

public void setEmployerContract(EmployerContract employerContract) {
   this.employerContract = employerContract;
}

private StudentEvaluation studentEvaluation;

@OneToOne
public StudentEvaluation getStudentEvaluation() {
   return this.studentEvaluation;
}

public void setStudentEvaluation(StudentEvaluation studentEvaluation) {
   this.studentEvaluation = studentEvaluation;
}

private int jobID;

public void setJobID(int value) {
    this.jobID = value;
}
@Id
public int getJobID() {
    return this.jobID;
}
}
