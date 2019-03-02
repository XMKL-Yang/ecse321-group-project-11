package ca.mcgill.ecse321.cooperator.controller;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import ca.mcgill.ecse321.cooperator.dto.AdministratorDto;
import ca.mcgill.ecse321.cooperator.dto.CoopDto;
import ca.mcgill.ecse321.cooperator.dto.EmployerDto;
import ca.mcgill.ecse321.cooperator.dto.FormDto;
import ca.mcgill.ecse321.cooperator.dto.ReminderDto;
import ca.mcgill.ecse321.cooperator.dto.StudentDto;
import ca.mcgill.ecse321.cooperator.model.AcceptanceForm;
import ca.mcgill.ecse321.cooperator.model.Administrator;
import ca.mcgill.ecse321.cooperator.model.Coop;
import ca.mcgill.ecse321.cooperator.model.CoopEvaluation;
import ca.mcgill.ecse321.cooperator.model.Employer;
import ca.mcgill.ecse321.cooperator.model.Faculty;
import ca.mcgill.ecse321.cooperator.model.Form;
import ca.mcgill.ecse321.cooperator.model.PDF;
import ca.mcgill.ecse321.cooperator.model.Reminder;
import ca.mcgill.ecse321.cooperator.model.Semester;
import ca.mcgill.ecse321.cooperator.model.Student;
import ca.mcgill.ecse321.cooperator.model.StudentEvaluation;
import ca.mcgill.ecse321.cooperator.model.TasksWorkloadReport;
import ca.mcgill.ecse321.cooperator.service.CooperatorService;

@CrossOrigin(origins = "*")
@RestController
public class CooperatorRestController {

	@Autowired
	private CooperatorService service;

	// Student
	@PostMapping(value = {
			"/student/{phone}/{firstName}/{lastName}/{email}/{password}/{userId}/{id}/{academicYear}/{major}/{minor}",
			"/student/{phone}/{firstName}/{lastName}/{email}/{password}/{userId}/{id}/{academicYear}/{major}/{minor}/" })
	public StudentDto createStudent(@PathVariable("phone") long phone, @PathVariable("firstName") String firstName,
			@PathVariable("lastName") String lastName, @PathVariable("email") String email,
			@PathVariable("password") String password, @PathVariable("userId") int userId, @PathVariable("id") int id,
			@PathVariable("academicYear") String academicYear, @PathVariable("major") String major,
			@PathVariable("minor") String minor) throws IllegalArgumentException {
		// @formatter:on
		Student student = service.createStudent(userId, phone, email, firstName, lastName, password,
				Faculty.Engineering, id, major, minor, academicYear, null);
		return convertToDto(student);
	}

	// Student
	@PostMapping(value = { "/s", "/s/" })
	public StudentDto createStudent2(@RequestParam long phone, @RequestParam String firstName,
			@RequestParam String lastName, @RequestParam String email, @RequestParam String password,
			@RequestParam int userId, @RequestParam int id, @RequestParam String academicYear,
			@RequestParam String major, @RequestParam String minor) throws IllegalArgumentException {
		// @formatter:on
		Student student = service.createStudent(userId, phone, email, firstName, lastName, password,
				Faculty.Engineering, id, major, minor, academicYear, null);
		return convertToDto(student);
	}

	@GetMapping(value = { "/students", "/students/" })
	public List<StudentDto> getAllStudents() {
		List<StudentDto> studentDtos = new ArrayList<>();
		for (Student student : service.getAllStudents()) {
			studentDtos.add(convertToDto(student));
		}
		return studentDtos;
	}

	private StudentDto convertToDto(Student s) {
		ArrayList<Integer> coopIds = new ArrayList<Integer>();
		if (s.getCoop() != null) {
			for (Coop c : s.getCoop()) {
				coopIds.add(c.getCoopId());
			}
		}
		AdministratorDto adminDto = null;
		if (s.getAdministrator() != null) {
			adminDto = convertToDto(s.getAdministrator());
		} else {
			adminDto = new AdministratorDto();
		}
		StudentDto studentDto = new StudentDto(s.getPhone(), s.getFirstName(), s.getLastName(), s.getEmail(),
				s.getPassword(), s.getUserId(), s.getId(), s.getAcademicYear(), s.getMajor(), s.getMinor(), adminDto,
				s.getFaculty(), coopIds);
		return studentDto;
	}

	private AdministratorDto convertToDto(Administrator administrator) {
		// TODO Auto-generated method stub
		return null;
	}

	// Coop
	@PostMapping(value = {
			"/coop/{coopId}/{employerConfirmation}/{endDate}/{jobDescription}/{jobId}/{location}/{needWorkPermit}/{semester}/{startDate}/{studentId}/{employerId}",
			"/coop/{coopId}/{employerConfirmation}/{endDate}/{jobDescription}/{jobId}/{location}/{needWorkPermit}/{semester}/{startDate}/{studentId}/{employerId}/" })
	public CoopDto createCoop(@PathVariable("coopId") int coopId,
			@PathVariable("employerConfirmation") boolean employerConfirmation,
			@PathVariable("endDate") String endDateStr, @PathVariable("jobDescription") String jobDescription,
			@PathVariable("jobId") int jobId, @PathVariable("location") String location,
			@PathVariable("needWorkPermit") boolean needWorkPermit, @PathVariable("semester") String semesterStr,
			@PathVariable("startDate") String startDateStr, @PathVariable("studentId") int studentId,
			@PathVariable("employerId") int employerId) throws IllegalArgumentException {
		// @formatter:on
		Student student = service.getStudent(studentId);
		Employer employer = service.getEmployer(employerId);

		Semester semester = getSemester(semesterStr);

		Date endDate = new Date(createDate(endDateStr));
		Date startDate = new Date(createDate(startDateStr));

		Coop coop = service.createCoop(coopId, employerConfirmation, endDate, jobDescription, jobId, location,
				needWorkPermit, semester, startDate, student, employer);

		return convertToDto(coop);
	}

	@GetMapping(value = { "/coops", "/coops/" })
	public List<CoopDto> getAllCoops() {
		List<CoopDto> coopDtos = new ArrayList<>();
		for (Coop coop : service.getAllCoops()) {
			coopDtos.add(convertToDto(coop));
		}
		return coopDtos;
	}

	private CoopDto convertToDto(Coop c) {
		ArrayList<Integer> PDFIds = new ArrayList<Integer>();
		if (c.getPDF() != null) {
			for (PDF pdf : c.getPDF()) {
				PDFIds.add(pdf.getDocId());
			}
		}

		ArrayList<Integer> formIds = new ArrayList<Integer>();
		if (c.getForm() != null) {
			for (Form f : c.getForm()) {
				formIds.add(f.getFormId());
			}
		}

		ArrayList<Integer> reminderIds = new ArrayList<Integer>();
		if (c.getReminder() != null) {
			for (Reminder r : c.getReminder()) {
				reminderIds.add(r.getReminderId());
			}
		}

		StudentDto studentDto = null;
		if (c.getStudent() != null) {
			studentDto = convertToDto(c.getStudent());
		} else {
			studentDto = new StudentDto();
		}

		EmployerDto employerDto = null;
		if (c.getEmployer() != null) {
			employerDto = convertToDto(c.getEmployer());
		} else {
			employerDto = new EmployerDto();
		}

		CoopDto coopDto = new CoopDto(c.getCoopId(), c.getEmployerConfirmation(), c.getEndDate(), c.getJobDescription(),
				c.getJobId(), c.getLocation(), c.getNeedWorkPermit(), c.getSemester(), c.getStartDate(), studentDto,
				employerDto, PDFIds, formIds, reminderIds);
		return coopDto;
	}

	// Employer
	@PostMapping(value = {
			"/employer/{phone}/{firstName}/{lastName}/{email}/{password}/{userId}/{position}/{company}/{location}",
			"/employer/{phone}/{firstName}/{lastName}/{email}/{password}/{userId}/{position}/{company}/{location}/" })
	public EmployerDto createEmployer(@PathVariable("phone") long phone, @PathVariable("firstName") String firstName,
			@PathVariable("lastName") String lastName, @PathVariable("email") String email,
			@PathVariable("password") String password, @PathVariable("userId") int userId,
			@PathVariable("position") String position, @PathVariable("company") String company,
			@PathVariable("location") String location) throws IllegalArgumentException {
		// @formatter:on
		Employer employer = null;
		if (service.getEmployer(userId) == null) {
			employer = service.createEmployer(userId, phone, email, firstName, lastName, password, position, company,
					location);
			return convertToDto(employer);
		} else {
			employer = service.getEmployer(userId);
		}
		return convertToDto(employer);
	}

	@GetMapping(value = { "/employers", "/employers/" })
	public List<EmployerDto> getAllEmployers() {
		List<EmployerDto> employerDtos = new ArrayList<>();
		for (Employer employer : service.getAllEmployers()) {
			employerDtos.add(convertToDto(employer));
		}
		return employerDtos;
	}

	@GetMapping(value = { "/reminders/send", "/reminders/send/" })
	public List<ReminderDto> sendReminders() {
		System.out.println("GET /reminders/send");

		 	List<ReminderDto> reminderDtos = new ArrayList<>();
		for (Reminder reminder : service.sendReminders()) {
			reminderDtos.add(convertToDto(reminder));
		}
		return reminderDtos;
	}

	private EmployerDto convertToDto(Employer e) {
		EmployerDto employerDto = new EmployerDto(e.getUserId(), e.getPhone(), e.getEmail(), e.getFirstName(),
				e.getLastName(), e.getPassword(), e.getPosition(), e.getCompany(), e.getLocation());
		return employerDto;
	}
	
	private ReminderDto convertToDto(Reminder r) {
		CoopDto coopDto = null;
		if (r.getCoop() != null) {
			coopDto = convertToDto(r.getCoop());
		} else {
			coopDto = new CoopDto();
		}
		ReminderDto reminderDto = new ReminderDto(r.getReminderId(),r.getSubject(),r.getDate(), r.getDeadLine(),
				r.getDescription(), r.getUrgency(),coopDto);
		return reminderDto;
	}

	// Form
	@PostMapping(value = { "/form/acceptanceForm/{formId}/{submissionDate}/{coopId}",
			"/form/acceptanceForm/{formId}/{submissionDate}/{coopId}/" })
	public FormDto createForm(@PathVariable("formId") int formId,
			@PathVariable("submissionDate") String submissionDateStr, @PathVariable("coopId") int coopId)
			throws IllegalArgumentException {
		// @formatter:on
		Coop coop = service.getCoop(coopId);
		Date submissionDate = new Date(createDate(submissionDateStr));

		AcceptanceForm aForm = service.createAcceptanceForm(formId, submissionDate, coop);
		return convertToDto(aForm);
	}

	// Form
	@PostMapping(value = {
			"/form/coopEvaluation/{formId}/{submissionDate}/{workExperience}/{employerEvaluation}/{softwareTechnologies}/{usefulCourses}/{coopId}",
			"/form/coopEvaluation/{formId}/{submissionDate}/{workExperience}/{employerEvaluation}/{softwareTechnologies}/{usefulCourses}/{coopId}/" })
	public FormDto createForm(@PathVariable("formId") int formId,
			@PathVariable("submissionDate") String submissionDateStr,
			@PathVariable("workExperience") String workExperience,
			@PathVariable("employerEvaluation") int employerEvaluation,
			@PathVariable("softwareTechnologies") String softwareTechnologies,
			@PathVariable("usefulCourses") String usefulCourses, @PathVariable("coopId") int coopId)
			throws IllegalArgumentException {
		// @formatter:on
		Coop coop = service.getCoop(coopId);
		Date submissionDate = new Date(createDate(submissionDateStr));

		CoopEvaluation cForm = service.createCoopEvaluation(formId, submissionDate, workExperience, employerEvaluation,
				softwareTechnologies, usefulCourses, coop);
		return convertToDto(cForm);

	}

	// Form
	@PostMapping(value = {
			"/form/studentEvaluation/{formId}/{submissionDate}/{studentPerformance}/{studentWorkExperience}/{coopId}",
			"/form/studentEvaluation/{formId}/{submissionDate}/{studentPerformance}/{studentWorkExperience}/{coopId}/" })
	public FormDto createForm(@PathVariable("formId") int formId,
			@PathVariable("submissionDate") String submissionDateStr,
			@PathVariable("studentPerformance") int studentPerformance,
			@PathVariable("studentWorkExperience") String studentWorkExperience, @PathVariable("coopId") int coopId)
			throws IllegalArgumentException {
		// @formatter:on
		Coop coop = service.getCoop(coopId);
		Date submissionDate = new Date(createDate(submissionDateStr));

		StudentEvaluation sForm = service.createStudentEvaluation(formId, submissionDate, studentWorkExperience,
				studentPerformance, coop);
		return convertToDto(sForm);

	}

	// Form
	@PostMapping(value = {
			"/form/tasksWorkloadReport/{formId}/{submissionDate}/{hoursPerWeek}/{tasks}/{training}/{wage}/{coopId}",
			"/form/tasksWorkloadReport/{formId}/{submissionDate}/{hoursPerWeek}/{tasks}/{training}/{wage}/{coopId}/" })
	public FormDto createForm(@PathVariable("type") String type, @PathVariable("formId") int formId,
			@PathVariable("submissionDate") String submissionDateStr, @PathVariable("hoursPerWeek") int hoursPerWeek,
			@PathVariable("tasks") String tasks, @PathVariable("training") String training,
			@PathVariable("wage") int wage, @PathVariable("coopId") int coopId) throws IllegalArgumentException {
		// @formatter:on
		Coop coop = service.getCoop(coopId);
		Date submissionDate = new Date(createDate(submissionDateStr));

		TasksWorkloadReport tForm = service.createTasksWorkloadReport(formId, submissionDate, tasks, hoursPerWeek, wage,
				training, coop);
		return convertToDto(tForm);

	}

	@GetMapping(value = { "/forms", "/forms/" })
	public List<FormDto> getAllForms() {
		List<FormDto> formDtos = new ArrayList<>();
		for (Form form : service.getAllForms()) {
			formDtos.add(convertToDto(form));
		}
		return formDtos;
	}

	private FormDto convertToDto(Form f) {
		if (f.getClass().getName().equalsIgnoreCase("ca.mcgill.ecse321.cooperator.model.AcceptanceForm")) {
			return convertToDtoAForm((AcceptanceForm) f);
		} else if (f.getClass().getName().equalsIgnoreCase("ca.mcgill.ecse321.cooperator.model.CoopEvaluation")) {
			return convertToDtoCForm((CoopEvaluation) f);
		} else if (f.getClass().getName().equalsIgnoreCase("ca.mcgill.ecse321.cooperator.model.StudentEvaluation")) {
			return convertToDtoSForm((StudentEvaluation) f);
		} else if (f.getClass().getName().equalsIgnoreCase("ca.mcgill.ecse321.cooperator.model.TasksWorkloadReport")) {
			return convertToDtoTForm((TasksWorkloadReport) f);
		}
		return null;
	}

	private FormDto convertToDtoAForm(AcceptanceForm f) {
		FormDto formDto = new FormDto(f.getFormId(), f.getSubmissionDate(), f.getCoop().getCoopId());
		return formDto;
	}

	private FormDto convertToDtoCForm(CoopEvaluation f) {
		FormDto formDto = new FormDto(f.getFormId(), f.getSubmissionDate(), f.getCoop().getCoopId(),
				f.getEmployerEvaluation(), f.getSoftwareTechnologies(), f.getUsefulCourses(), f.getWorkExperience());
		return formDto;
	}

	private FormDto convertToDtoSForm(StudentEvaluation f) {
		FormDto formDto = new FormDto(f.getFormId(), f.getSubmissionDate(), f.getCoop().getCoopId(),
				f.getStudentWorkExperience(), f.getStudentPerformance());
		return formDto;
	}

	private FormDto convertToDtoTForm(TasksWorkloadReport f) {
		FormDto formDto = new FormDto(f.getFormId(), f.getSubmissionDate(), f.getCoop().getCoopId(), f.getTasks(),
				f.getHoursPerWeek(), f.getWage(), f.getTraining());
		return formDto;
	}

	// Student Forms
	@GetMapping(value = { "/getStudentForms/{userId}/{semester}/{year}", "/getForms/{userId}/{semester}/{year}/" })
	public List<FormDto> getFormsFromStudent(@PathVariable("userId") int userId, @PathVariable("semester") String semesterStr,
			@PathVariable("year") int year) throws IllegalArgumentException {
		List<FormDto> formDtos = new ArrayList<>();
		Semester semester = getSemester(semesterStr);
		for (Form form : service.getFormsFromStudent(userId, semester, year)) {
			formDtos.add(convertToDto(form));
		}
		return formDtos;
	}
	
	// Employer Forms
	@GetMapping(value = { "/getEmployerForms/{userId}/{semester}/{year}", "/getForms/{userId}/{semester}/{year}/" })
	public List<FormDto> getFormsFromEmployer(@PathVariable("userId") int userId, @PathVariable("semester") String semesterStr,
			@PathVariable("year") int year) throws IllegalArgumentException {
		List<FormDto> formDtos = new ArrayList<>();
		Semester semester = getSemester(semesterStr);
		for (Form form : service.getFormsFromEmployer(userId, semester, year)) {
			formDtos.add(convertToDto(form));
		}
		return formDtos;
	}
	
	// Edit Forms
	@PostMapping(value = { "/editForm/{formId}/{type}/{attribute}/{value}", "/editForm/{coopId}/{type}/{attribute}/{value}/" })
	public void editForm(@PathVariable("formId") int formId, @PathVariable("type") String type, 
			@PathVariable("attribute") String attribute, @PathVariable("value") Object value) 
					throws IllegalArgumentException {
		switch(type.toLowerCase()) {
		case "acceptanceform" :
			service.editAcceptanceForm(formId, attribute, value);
			break;
		case "coopevaluation" :
			service.editCoopEvaluation(formId, attribute, value);
			break;
		case "studentevaluation" :
			service.editStudentEvaluation(formId, attribute, value);
			break;
		case "tasksworkloadreport" :
			service.editTasksWorkloadReport(formId, attribute, value);
			break;
		};
	}
	
	@GetMapping(value = { "/student/problem/{term}", "/student/problem/{term}" })
	public List<StudentDto> getAllStudentsWithFormError(@PathVariable("term") String term) {
		List<StudentDto> studentDtos = new ArrayList<>();
		for (Student student : service.getAllStudentsWithFormError(term)) {
			studentDtos.add(convertToDto(student));
		}
		return studentDtos;
	}

	public Semester getSemester(String input) {
		switch (input.toLowerCase()) {
		case "fall":
			return Semester.Fall;
		case "winter":
			return Semester.Winter;
		case "summer":
			return Semester.Summer;
		}
		return null;
	}

	public static long createDate(String date) {
		java.util.Date dateFormat = null;
		try {
			dateFormat = new SimpleDateFormat("dd-MM-yyyy").parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dateFormat.getTime();
	}
}
