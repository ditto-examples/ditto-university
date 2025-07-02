# Ditto University ("root folder")
- Ditto University is a collection of courses that are then broken down into lessons and in lessons we have labs.  
- Currently Ditto University is just a set of markdown files that can be viewed in GitHub in the repository.  Because of this, the markdown files follow the rules and syntax for GitHub rendering the markdown.     
- The root folder README.md file is an introduction to the repository and the courses.  It includes an index of courses and links to the folders that each course is stored in. 
- The README.md file also explains that this repository is a "Proof of Concept" and that the courses are not yet complete.  It also explains that the courses are a work in progress.
- Any new courses added should have a link in the main README.md file.

# Course
- Each course is a self contained folder.  
- Each course name is the word "course" followed by a number.  The numbers are the course number and follow University course numbering conventions from the United States Computer Science curriculum. 
- Each course has a README.md file that describes the course along with a list of lessons in the course and links to the folders that each lesson is stored in.
- Each course README.md file has a list of the starting code for the developers to use for the course per platform. The break down of the folder names are:
	- Android: android
	- Swift: swift
	- Flutter: flutter
- Each course has an assets directory that contains the assets for the course. 
- Assets can be images, videos, or any other files needed for the course or labs.
- The asset directory also includes two files for each platform:  A DittoManager-Finished and a DittoManager-Lesson.  
  - The DittoManager-Lesson file is an unmodified version of the DittoManager class for each platform and should be the one that is included in the course starting code before the developer starts the labs.  
  - The DittoManager-Finished file is the completed version of the DittoManager class for each platform. This is what we consider the solution to all lab changes in all labs for a given course.  This is used as a sanity check to make sure that the app compiles and works as expected when the Ditto SDK is updated without having to manually redo all the labs by hand.  This is also the file the developer can use for building a lab.
  - A shell script file is included that can copy the lesson code to the finished code for each platform and vice versa.  This is useful when you are creating a new course and want to work on the finished code, then move it back to the lesson code to try the lab out to make sure it all makes sense and works as expected.

# Lessons
- In a lession, we give generic information and instructions to the developer - we never talk about platform specific API details.  All platform specific API details are in the labs.  This is simply for maintaining consistency across all platforms and lowering how complex it is to maintain the lessons or confuse the developer with platform specific details that they don't need to know about on platforms they don't use. 
- A lesson might need to retroduce a concept several times for a developer to learn something.  Most developers learn by repitition, so lessons and labs might have a developer do something similar to a previous lesson or lab just to get them to do a similar task again to get them to remember the concept better.  An example of this is instead of having one flexible function that allows a developer to update a document, we might have several different function that update a document but just pass in the field values you want to update in the document so that the developer has to write a DQL UPDATE statement multiple times over the course so that they might better understand and remember the DQL UPDATE statement syntax.  
- Because of how we want to teach developers, the code is never a "best practice" on how to refactor code to be the most efficient way and structure, it's setup in a way that is easy to understand and follow along with.  This is to help developers understand the concepts and not get lost in the code.
- A lesson is a self contained folder.  It contains a README.md file that describes the lesson along with a list of labs in the lesson and links to the folders that each lab is stored in.
- A lesson has one knowledge check section that is a multiple chioce quiz on the content of the lesson.  The answers are stored in a file .answer text file that just lists the question number and the letter to the correct answer.  
- Two answers are always wrong and any developer could see this.  
- Two answers are close to the proper answer and are created in such a way that if you read the content of the lesson you know the answer, but if you skimmed the content, you could possibly get it wrong. 

# Labs
- In a lab, we break down the lab into seperate files for each platform.  Currently the structure is:
  - Swift:  swift.md
  - Android: android.md
  - Flutter: flutter.md
  - All labs for Android are done in Kotlin.
- Labs are built in a way that we try to give the developer enough information for them to complete a task.
- Tasks can be writing new code, observing an application, or testing the application to validate that any changes produce the desired result. 
- All intractions with the Ditto SDK should be done in the DittoManager class.  This is to help developers understand the code and not get lost in the concepts.  This is also to help developers understand the concepts and not get lost in the code. 
- We should always try to limit the developer to the number of changes they need to make to complete a task in a lab.  This is also to help developers understand the concepts and not get lost in the code of having to modify too many files in a single lab. 

# General Rules
- Ditto University complements the Ditto SDK documentation that can be found at:  https://docs.ditto.live/
- If you don't know how to do something, and you can't find accurate and up-to-date information from sources such as online documentation, content in Notion or Linear, or a tool's help output or manpages, then ask about an approach before doing it instead of guessing.
- Before checking in any changes, you should validate that the apps for each platform compile and run as expected with the "DittoManager-Finished" file.  This is a sanity check to make sure that the app compiles and works as expected when the Ditto SDK is updated without having to manually redo all the labs by hand.
- Before checking in, you should revert the changes to the "DittoManager-Finished" file to the "DittoManager-Lesson" file, otherwise developers will already have all the code completed for them for all labs in the course which is NOT WHAT WE WANT. 
- All changes should be checked into a seperate branch with a PR posted with a clear description of the changes.
- You should only have 1 course changes in your PR.  If you are working on multiple courses, you should create a separate PR for each course.
- The description of the PR should be clear and concise.  It should explain the changes made and the reasoning behind the changes.  
