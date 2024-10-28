
### How can I ensure that my code, program, or software is functional and secure?

Over the past eight weeks, we learned that ensuring functionality and security in software requires a multi-layered approach:

1. **Functional Testing**: We used unit tests to verify that individual methods or functions, like those in `Contact.java` and `ContactService.java`, perform as expected. By writing comprehensive unit tests (e.g., `ContactTest.java` and `ContactServiceTest.java`), we were able to identify and correct bugs early in the development cycle, ensuring that each component behaved according to the requirements.
   
2. **Automated Testing**: Automation was key to speeding up the testing process and making sure that changes in the code didn’t introduce new errors. Tools like JUnit were critical in creating repeatable tests that we could run automatically with every change.

3. **Security Practices**: To secure our programs, we incorporated principles like input validation, avoiding common security vulnerabilities (e.g., injection attacks), and ensuring data privacy. The importance of encryption and secure communication was emphasized, especially when handling sensitive data, which relates to our Project Two lessons about handling user needs with care.

### How do I interpret user needs and incorporate them into a program?

Understanding and interpreting user needs was a key focus during our coursework:

1. **Requirement Gathering**: Early in the project, we focused on gathering clear and detailed requirements. This included analyzing user stories, reviewing acceptance criteria, and understanding both functional and non-functional requirements. In Project One, the contact service had to manage basic contact information, and the constraints (e.g., non-null contact IDs, unique IDs) were designed to meet user expectations of reliability and functionality.

2. **Incorporating Feedback**: Throughout both projects, we practiced iterative development where we revisited and refined our designs based on user feedback. This not only helped to align the program with user needs but also allowed for flexibility in adjusting requirements as they evolved.

3. **Testing Against Requirements**: Our unit tests were structured to directly test whether user requirements were met. For example, we verified that contacts could be created, updated, and deleted in `ContactService.java` according to the provided specifications, ensuring that the program met its functional objectives.

### How do I approach designing software?

Our approach to software design involved several key steps:

1. **Planning and Structuring**: We emphasized the importance of initial planning and design before jumping into coding. This involved creating diagrams (e.g., class diagrams for `Contact` and `ContactService`), outlining methods, and defining clear interfaces.

2. **Modular Design**: Throughout the projects, we were encouraged to design software that was modular and easy to test. By separating concerns (e.g., keeping the contact model separate from the service layer), we made our codebase more maintainable and flexible for future changes. This was especially useful when writing unit tests because we could isolate each module for thorough testing.

3. **Design Patterns and Best Practices**: We also applied common design patterns such as the Service pattern in `ContactService.java` to handle business logic, keeping our code clean, organized, and scalable.

4. **Testing-Driven Development (TDD)**: Using TDD principles, we often wrote tests before or alongside the implementation. This ensured that we had a clear understanding of the desired outcomes before writing the actual code, which helped improve both design and functionality.
