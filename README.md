# MediReQ-User
Medical Records for the Mobile Generation


  What is it?
  -----------
  
This project is the user application part of the MediReQ Project. This project was selected as top ten idea in the Qualcomm Intern IdeaQuest Contest. The MediReQ project consists of two Android applications, MediReQ-User and MediReQ-Reader, and a web application, MediReQ-Web (not posted). These three components work together to enables patients to check in at the doctor's office as they arrive and it gives doctors an easy way to browse through a patient’s medical files for relevant information before a check up. MediReQ displays all information in a easy-to-read visual layout that facilitates more efficient processing, which reduces the total time required for a simple check-up or even a longer consultation. 

MediReQ-User is a password protected, user friendly Android app that allows the user to enter information standard on most medical history forms: contact information, surgical history, medical allergies, family medical history, health habits/behavior, and past conditions. The application also supports multiple profiles so that parents can store the medical information for their children on the app as well. We used NFC technology to transfer the data from the user’s phone to the hospital. The MediReQ-Web application displays this information to the doctor/hospital. This is the same technology that mobile payments use, and it is both convenient and secure. The app has to be open and consciously choose to send data, it’s not broadcasting. This saves people time that would otherwise be spent filling out the same medical forms for every new doctor they visit. 


  Authors
  ------------------
  Andriy Katkov, Cassidy Burden, Cindy Li, Connie Wang, Sneha Shrotriya

  FAQ
  -------------

  Is this secure?
  All of the user’s data stays encrypted on their phone. Nothing is uploaded to the cloud, and none of the data can go anywhere until the user consciously “beams” it away. The hospital side files would have to be secured by the hospital, since we can’t assume anything about their file system.
  
  Why use MediReQ?
Users only need to fill out the information once, then hospitals and doctors’ offices can get the data whenever they visit. It’s a simpler check-in process, especially for first time visitors to a hospital/doctor. 

  What's the future for this project?
MediReQ could use dragonboards if they supported NFC, and then Qualcomm could market and sell the MediReQ system as a complete package that hospitals could use. This would allow the hospital side of the system to secure their data transfers (instead of having a web app). Also, if MediReQ had a dedicated hardware NFC secure chip (like the iPhone does for Apple Pay), then hospital overrides could be implemented in the case of the unconscious patient. That’s something that we didn’t want to implement for the prototype because it caused too many security holes if anyone could just override the password protected data. The mobile app could also be refined further, to link family members without having to re-enter data. (e.g. mom has profile, adds profile for kid and then just links kid as her kid so that she doesn’t have to reenter her age, illnesses, etc.)
