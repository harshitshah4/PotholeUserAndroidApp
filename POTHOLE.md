This Pothole app immaculates the solution to Pothole Problem for Governments.

## Problem Description
The state / or any government , faces Pothole problem  , and there is lack of solution that provides an end to end flow between Government (who fixes the Pothoel )and User (who registers the Pothole ) , The existing solution are not sufficient and they discourage user , since, they generally have the bad user experience , also the Government does not have a good solution to track and filter those Potholes also update the concerned authorities and users . 

Our aim of this project is to provide a solid and effective solution to the above problem.

## Solution

Our project provides set of two app : One App is for Civil Authority and the Other App is for User . The civil authority app , as the name suggests is used by Government and Civil Agents to track , update and filter the Potholes,  while the User App is used by users / actors who reports the pothole . The detailed description about each of the app will be found in their's respective repositories.

## Flow 

1) The User uploads the Pothole  , by filling fields such as image , description , location ( Or current location is taken )
2) The image is uploaed to storage AWS S3 and other data is uploaded to database along with pointer to the image.
3) The image is then passed to AI model (AWS Rekognition) , which assigns the score to that image based on different factors , 
      if that image have sufficient score (i.e.Here , score means how much confidence is there that an image is actually Pothole) , then it goes into next step
      or else the post is rejected , and is reflected to the user
4) Civil Authority operating within the location of Pothole is notified about the Pothole 
    if there are multiple potholes reported within same vicinity , then they are merged into a single pothole , and hence are tracked together as one, 
5) Then Civil Authority filters all the Pothole and updates the status of the Pothole , status is also reflected to the user , and is notified too .

## Architecture

#### Now we will go into technical details of the app 

The app is hosted on AWS , and is architecutered in such a way that it could scale well , and could handle upto around millions of users.
The app makes use of JWT token for authentication , and from storage to user details are all secured and are available only to authenticated / authorized users.


#### Now I will discuss each components from the above image 

> The almost similar architecture applies for both the app.

- **User / Actor** : 
  It is the frontend application build on Android
  
- **Elastic Load Balancer** : 
  Elastic Load Balancer is in front of the Server , and it manages and spreads the traffic acrros the Server.
  
- **Server** : 
  These are Node js application on the EC2  instances deployed by Elastic BeanStalk , are under autoscaling group , and hence , the scale automatically up and down based on the load . 
  
- **Database** : 
  MongoDB Atlas , hosted on AWS  , is used as fully managed NoSQL database, they are internally replicated and shraded across AZs.
  
- **S3** : 
  It is highly reliable and scalable Storage provided by AWS , All the media files are stored in the bucket , The bucket is private and can only be accessible via CloudFront CDN , Also the CloudFront CDN , only allows access via Signed Url , so that only authenticated users have access to media files. And Signed Post is used , so that only the authenticated users could upload the media.
  
- **Lambda Pothole Recognizer  (AWS Lambda)** : 
This is AWS Lambda function that gets triggered as soon as the image realted to Pothole Report is uploaded in the S3 bucket , this function assigns a score that how much confidence that the image is Pothole , to the image uploaded by the user , This function in turns calls AWS Rekognition , which is scalable API provided by AWS , to Label the image.

- **Lambda CivilAuthoritySelection (AWS Lambda)** : 
This is AWS Lambda function that gets triggered as soon as the previous Lambda function passes , This function finds and assigns the CivilAuthority that operates in the location where the Pothole is reported. This is then updated into database.

- **AWS Rekognition** : 
This is the fully managed AI API provided by the AWS , to label the images , that helps us in scoring the images to calculate Pothole confidences.

- **CDN** : 
The media files from AWS S3 are served via CloudFront CDN  , So that users may have reliable latency , This CloudFront CDN , have access to the S3 bucket , and is accessed on via Signed URL , so that only the authentication users could view the media. 


> As you can see each component of the above architecture is higly scalable , secure and reliable , at each step JWT tokens and authentication are used to , and all the components are generally self sufficient so the arcitecture could be easily modified according to needs.

Repo to components of the app:
