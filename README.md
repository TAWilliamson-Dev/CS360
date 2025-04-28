# CS360
CS360 Mobile Archicecture and Programming - Inventory Management Application

Briefly summarize the requirements and goals of the app you developed. What user needs was this app designed to address?

The inventory application was developed to enable a user to keep a catalogue of items, most effectively as part of a larger enterprise application.
The requirements for this vertical slice were:
The User is able to sign in, login creditials are checked against a database of existing users
If the user does not exist in the database, allow the user to create a new account
Once signed in the user can Add, Update, or Delete items in the inventory.
Items are stored in a local database, with dynamic queries for deletion / update attached to the appropriate buttons in the UI.

What screens and features were necessary to support user needs and produce a user-centered UI for the app? How did your UI designs keep users in mind? Why were your designs successful?
The application required a Login screen, enabling a user to sign in or create a new account, a screen that displays a Grid based inventory where a user can add, delete, or update an item.
The UI in its current form is designed to be simple and easily readable. While the designs work for the scope of the project, the application requires an additional usability pass, with
focus given to ensuring that each element of the interface is immediately identifiable and that the users interactions flow logically from one to another, regardless of their experience with the
application.

How did you approach the process of coding your app? What techniques or strategies did you use? How could those techniques or strategies be applied in the future?

I maintained an Agile approach to development, with a constant cycle of implementing new features, testing them as I went to ensure the application remained stable. 
Maintaining an iterative approach in my workflow is one of the most effective ways I know to ensure that my codebase remains as stable and secure as possible. Unfortunately,
this course coincided with CS320 Software Test, Automation QA. Had I taken that course previously, I would have utilized an automated test suite to speed up the testing phase
during development.

How did you test to ensure your code was functional? Why is this process important, and what did it reveal?

In order to test the application, I used a combination of functional and regression testing, with a focus towards maintaining the technical functionality of the application.
Ultimately, this was one of the shortfalls of the process, as a lack of thorough usability testing hampered the overall user experience.

Consider the full app design and development process from initial planning to finalization. Where did you have to innovate to overcome a challenge?

In what specific component of your mobile app were you particularly successful in demonstrating your knowledge, skills, and experience?
The most successful component in the application, in my opinion, was the usage of the two SQLite databases for storing Users and Inventory items. I feel
that the process of integrating them into the application was both the most successful portion of the application and the most rewarding in terms of
enhancing my body of knowledge.
