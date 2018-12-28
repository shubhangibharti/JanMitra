# JanMitra

The Challenge - Smart India Hackathon
Smart India Hackathon , a unique initiative to identify new and disruptive digital technology innovations for solving the challenges faced by our country. In order to work towards our Hon’ble Prime Minister, Shri Narendra Modi‘s vision for Digital India and promote digital literacy, MHRD, AICTE, i4C, and Persistent Systems organized Smart India Hackathon, the world’s largest nation-building initiative to harness the innovative brilliance of the youth and develop digital products for solving some of the country’s pressing problems.

Smart India Hackathon, in to its second year, had two editions – Software and Hardware Editions. The grand finale of the Software edition was held on March 30th & 31st, whereas the newly-introduced Hardware edition was scheduled in June, entailed the teams working five straight days to build their hardware solutions.

# Problem Statement 

Machine Reading of Customer Feedback -- under the ministry of railways

# The Solution

Our team, Janmitra, from IIT ISM Dhanbad, represented the college in Udaipur. Their problem statement was "Machine Reading of Customer Feedback" under the ministry of railways. We gave a robust solution of fetching complaints, classifying them, sending complaints to concerned departments of concerned divisions of the zones and finally making the algorithm self learning by taking feedback from the ministry of Railways. The complaints were taken from Twitter, SMS, handwritten Telegram and Voice. The complaint tweets were getting replies in real time asking for additional information if needed. Finally, the complaints were showing up on the department application with the feature of feedback. This solution won the 2nd prize and glory to the college. 

## Android Apps

#### Complaint App
##### SMS Inbox Activity

This app shows the msgs recieved in the layout desired. This was used to display all the SMS complaints in the app in the layout so that all the complaints on selection was sent to an email address to store the SMS complaints in the database.

##### Send Email Activity 

This activity sent the SMS in the form of email in order to save the complaints to the database.

##### Voice Activity

This activity takes voice complaints using Google Speech to Text package.

#### Department App

This app shows all the complaints for the official to view after classification to mark as resolved or provide feedback if the complaint is incorrectly classified which is added to training data and in this way the model improves.

### Camera App 

This app is used for handwritten telegram complaints using Microsoft's Image Processing.
