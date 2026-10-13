package com.university;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RegistrationServiceTest {

    @Test
    void shouldRegisterStudentForValidCourse() {

        Student student = new Student("S101", "Anita");

        Course course = new Course(
                "CS301",
                "Data Structures",
                4,
                30,
                true,
                null);

        RegistrationService service = new RegistrationService();

        boolean result = service.register(student, course);

        assertTrue(result);
        assertTrue(student.isRegistered("CS301"));
        assertEquals(4, student.getTotalCredits());
        assertEquals(1, course.getCurrentEnrollment());
    }

    @Test
    void shouldRejectInactiveCourse() {

        Student student = new Student("S101", "Anita");

        Course course = new Course(
                "CS301",
                "Data Structures",
                4,
                30,
                false,
                null);

        RegistrationService service = new RegistrationService();

        boolean result = service.register(student, course);

        assertFalse(result);
        assertFalse(student.isRegistered("CS301"));
    }

    @Test
    void shouldRejectMissingPrerequisite() {

        Student student = new Student("S101", "Anita");

        Course course = new Course(
                "CS401",
                "Advanced Algorithms",
                4,
                30,
                true,
                "CS301");

        RegistrationService service = new RegistrationService();

        boolean result = service.register(student, course);

        assertFalse(result);
        assertFalse(student.isRegistered("CS401"));
    }

    @Test
    void shouldAllowRegistrationWhenPrerequisiteIsCompleted() {

        Student student = new Student("S101", "Anita");
        student.addCompletedPrerequisite("CS301");

        Course course = new Course(
                "CS401",
                "Advanced Algorithms",
                4,
                30,
                true,
                "CS301");

        RegistrationService service = new RegistrationService();

        boolean result = service.register(student, course);

        assertTrue(result);
        assertTrue(student.isRegistered("CS401"));
    }

    @Test
    void shouldRejectDuplicateRegistration() {

        Student student = new Student("S101", "Anita");

        Course course = new Course(
                "CS301",
                "Data Structures",
                4,
                30,
                true,
                null);

        RegistrationService service = new RegistrationService();

        assertTrue(service.register(student, course));

        boolean secondAttempt = service.register(student, course);

        assertFalse(secondAttempt);
        assertEquals(4, student.getTotalCredits());
        assertEquals(1, course.getCurrentEnrollment());
    }

    @Test
    void shouldRejectRegistrationWhenCreditLimitIsExceeded() {

        Student student = new Student("S101", "Anita");

        // Simulate 20 existing credits
        student.addRegistration("CS101", 4);
        student.addRegistration("CS102", 4);
        student.addRegistration("CS201", 4);
        student.addRegistration("CS202", 4);
        student.addRegistration("CS203", 4);

        Course course = new Course(
                "CS301",
                "Data Structures",
                4,
                30,
                true,
                null);

        RegistrationService service = new RegistrationService();

        boolean result = service.register(student, course);

        assertFalse(result);
        assertFalse(student.isRegistered("CS301"));
        assertEquals(20, student.getTotalCredits());
        assertEquals(0, course.getCurrentEnrollment());
    }

    @Test
    void shouldRejectRegistrationWhenCourseIsFull() {
        Student student = new Student("S102", "Ravi");

        Course course = new Course(
                "CS302", "Operating Systems",
                3, 1, true, null);

        course.incrementEnrollment(); // Capacity is already full.

        RegistrationService service = new RegistrationService();

        boolean result = service.register(student, course);

        assertFalse(result);
        assertFalse(student.isRegistered("CS302"));
        assertEquals(0, student.getTotalCredits());
        assertEquals(1, course.getCurrentEnrollment());
    }

    @Test
    void shouldAllowRegistrationForLastAvailableSeat() {
        Student student = new Student("S103", "Meera");

        Course course = new Course(
                "CS303", "Computer Networks",
                3, 2, true, null);

        course.incrementEnrollment(); // One of two seats is occupied.

        RegistrationService service = new RegistrationService();

        boolean result = service.register(student, course);

        assertTrue(result);
        assertTrue(student.isRegistered("CS303"));
        assertEquals(3, student.getTotalCredits());
        assertEquals(2, course.getCurrentEnrollment());
    }

    @Test
    void shouldAcceptMinimumAndMaximumAllowedCredits() {
        RegistrationService service = new RegistrationService();

        Student student1 = new Student("S104", "Kiran");
        Course oneCredit = new Course(
                "CS304", "One Credit", 1, 10, true, null);

        assertTrue(service.register(student1, oneCredit));
        assertEquals(1, student1.getTotalCredits());
        assertEquals(1, oneCredit.getCurrentEnrollment());

        Student student2 = new Student("S105", "Priya");
        Course sixCredits = new Course(
                "CS305", "Six Credits", 6, 10, true, null);

        assertTrue(service.register(student2, sixCredits));
        assertEquals(6, student2.getTotalCredits());
        assertEquals(1, sixCredits.getCurrentEnrollment());
    }

    @Test
    void shouldRejectInvalidCreditsWithoutChangingState() {
        int[] invalidCredits = { 0, -1, 7 };

        RegistrationService service = new RegistrationService();

        for (int credits : invalidCredits) {
            Student student = new Student("S106", "Arun");

            Course course = new Course(
                    "CS306", "Invalid Credit Course",
                    credits, 10, true, null);

            boolean result = service.register(student, course);

            assertFalse(result, "Credits " + credits + " must be rejected");
            assertFalse(student.isRegistered("CS306"));
            assertEquals(0, student.getTotalCredits());
            assertEquals(0, course.getCurrentEnrollment());
        }
    }

}