# ST.ART Copyright@AmbieSoft

ST.ART is an android app helping build a busker and busking community. 

- Iteration 1
- Iteration 2 (Current Phase)

### Change log
****
1.0
14 Aug 2016
Initialization of the ST.ART application;
Implement Google Places API

1.1
15 Aug 2016
Implement Create Performance Function;
Save performance into Firebase;
Show performance as markers on map;
Implement the function on showing all Artworks

1.2
16 Aug 2016
Implement Bottom Bar;
Add floating action button for switching between list/map view

1.3
17 Aug 2016
Implement list to show the Performance stored in Firebase;
Add filter performance function based on date

1.4
19 Aug 2016
Implement full filter function;
Fixing bugs and logical errors on Firebase connection
****
2.0
24 Aug 2016
Start replacing new User Interface design;
Change the coding approach into Model-View-Presenter (MVP)

2.1
25 Aug 2016
Improve UI design;
Change the bottom bar layout and library

2.2
29 Aug 2016
Update Create Performance functions

2.3
30 Aug 2016
Implement Heat Map function

2.4
31 Aug 2016
Add Welcome Page for start of application;
Implement Image moving animation

2.5
1 Sep 2016
Implement edit performance function;
Implement User Registration and Login function

2.6
3 Sep 2016
Implement validation on date and time when create/edit performance

2.7
4 Sep 2016
Implement photo uploading functions on user registration

2.8
5 Sep 2016
Improve UI and fixing bugs on user registration and login system;
Improve UI layout on all functions
****

### Application Architecture

This application are designed based on the Model-View-Presenter (MVP) architectual pattern.
Model: Defining the data that will be displayed to user, and also actions involving other external connections like API.
Presenter: Connection between Model and View. It retrieves data from Model, and formats it for display in the view. It also accepts user events, which update models or perform methods in model afterwards.
View: Responsible for display the data from model to user, and directly interact with the user.

![alt tag](https://upload.wikimedia.org/wikipedia/commons/d/dc/Model_View_Presenter_GUI_Design_Pattern.png)

For example, when the user want to view a performance detail:
1. View class named "PerformanceDetailFragment" will be initialised, and setup the view for the fragment.
2. Presenter class called "PerformanceDetailFragmentPresenter" will be initialised after the view is created. Methods are called, like getting the values passed from the previous calling fragment, and update the view's Text Fields with the value accordingly.
3. The presenter also responsible for handling the user events, like back to previous fragment when user clicks the "back" button.
4. Model class like FirebaseUtility or User data class will be called by the presenter also, as we have to retrieve the Firebase for grabbing and matching the creator of that particular performance, and get the creator's nickname and profile picture from Firebase, finally sending these information to view and display to user.
