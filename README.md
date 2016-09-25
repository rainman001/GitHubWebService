# GitHubWebService

#Paul, thanks for the opportunity to work on this project. I learned a lot and I think most important grew in confidence in my ability to learn, as there were several things I had to do for this project that I hadn't done before and didn't really know where to start.

#First, I made the assumption that for the sake of simplicity the user would edit the code and manually enter usernames, passwords, etc, rather than have those things entered via any sort of UI. Here are notes of locations where you would need to enter information to make the project work:

#GitHubWebService.java
#- line 50 (fill in your org name)
#- line 52 (fill in your GitHub username and password
#- line 91 (your gmail username, ex: blahblah@gmail.com)
#- line 92 (your app password generated through gmail)

#Bucket.java
#- line 20 (the bucket name you want; all lowercase, periods allowed, etc)
#- line 64 (fill in the name of the file that you want your list to be called)

#Additional setup I did for the project: I set up the organization in GitHub. I set up an app password in google to connect to my gmail account for sending emails. I set up my IDE (Eclipse) with the Amazon SDK by going to Help > Install New Software, and once that was done, adding the two keys I got from the AWS console via Window >Preferences > AWS Toolkit. Those two keys were generated in the IAM section of the AWS console as part of a new user that I created. That user was granted a full security profile. I can't think of anything else I did for setup, but I could be missing something.

#Much of the code I used for connecting to Google and connecting to Amazon was taken from documentation, examples, etc, and then modified to meet my specific needs. That was one thing that I enjoyed about this was seeing my own ability to use existing code and make it fit my needs grow with this project. 

#I chose to use Java and Spring because that's where I'm probably most comfortable in terms of setting up a project from scratch, though I think I could have done a similar job in JavaScript. I prefer using the Eclipse IDE and I've primarily used that for Java in the past, so that's why I did it this way.

#Instructions: I created and ran the project in Spring Tool Suite. When you run the project, it first goes out to GitHub and gets a list of users from the organization, based on the org name and credentials that you provide. I tested this with my account email being both public and private, and it worked both times (I assumed that was what was meant by a private user). Then it goes out and gets the full information about those users, including name and email. It writes the logins out to a file, and then for each user, an email is sent if they don't have the name populated on their GitHub account. Finally, it creates an S3 Bucket if one doesn't already exist, and then writes the previously created file out to that bucket.

#I feel a great deal of satisfaction upon completion of this project. I got stuck several times, but each time was able to either debug the issue in the code or find a simpler workaround that ended up being simpler than my initial solution.
