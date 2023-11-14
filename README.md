# Project Name
M-trac

## Introduction
An android app that calculates expenses and income based on incoming mpesa messages.
It automatically obtains that amount and type of transactions by checking for certain key words in a message.
###App link: 
###Blog:
###Linkedin:


##Installation
###Prerequisites
Before you begin, ensure you have the following installed:

1. Android Studio with the latest Android SDK.
2. Git for version control (optional, but recommended).
###Clone the Repository
If you have Git installed, you can clone the repository using the following command:
	git clone https://github.com/deking254/M-trac.git
Alternatively, you could download the repo as a zip.
###Open the Project in Android Studio
1. Launch Android Studio.
2. Click on "Open an existing Android Studio project."
3. Navigate to the directory where you cloned or extracted the repository and select the project folder.
###Install Dependencies
The project may rely on external libraries or dependencies. Android Studio will usually prompt you to install any missing dependencies automatically. 
If not, you can check for missing dependencies in the "Messages" or "Event Log" tab at the bottom of Android Studio.
###Connect a Device or Emulator
To run the app, you'll need an Android device.

1. Connect your Android device to your computer using a USB cable.
2. Enable USB debugging on your Android device. (You can find this option in the "Developer options" section in your device's settings. If Developer options are not visible, go to "About phone" and tap "Build number" seven times to enable it.)
3. In Android Studio, select your device from the list of available devices.
###Run the App
Click the "Run" button (usually a green triangle) in Android Studio, and the app should be installed and launched on your connected device or emulator.
##Usage
###1. Open the App
Launch the app on your Android device either by tapping its icon on the home screen or through the app drawer.
###2.Add Starting Amounts and person
If you are opening the app for the first time, you will be promted to enter the amounts that you have at the given moment. You will then save the details and a page to add people will be displayed.After entering and saving the person, an empty transactions page will be displayed.
You will not be promted to add any person or amounts if you have previously entered them. The transactions page will be displayed immediately.
###3. Transaction
In the transactions page, use the floating button at the bottom right to add a new transaction. A page that prompts you to select the person you are transacting with, enter the transaction amount, select the type of transaction eg income, select the nature of the transaction eg cash or mpesa.
![Add transaction](add_transaction.jpg)
Save the transaction and if successful you will see it on the transactions page.
You can edit or delete a transaction by long pressing the transaction and selecting the appropriate command in the pop up. You can also set a transaction as transactional by selecting the command in the pop up. A transaction can only be set as transactional if it is of type mpesa. What setting a transaction as transactional means is that the money was only transferred from mpesa to cash and vice versa. So no money was actually lost or gained. This helps with keeping track of the cash and mpesa amounts at all times.
![Transactions list](transactions.jpg)

###4. People
Check the list of people by pressing the people button on the top right of the screen. In the people page. You can add a new person by pressing the float button. You can edit and delete a person by long pressing the person on the list.
![People](people.jpg)
###5. Totals
This page can be accessed by clicking the home button on the transactions page(Main activity). This page shows the amount of cash in hand and the amount in the mpesa accounts of the sim card in the phone at the time.
![Totals](totals.jpg)
###6. Feedback
Enjoy using M-trac! If you have any questions or encounter difficulties, refer to the app's documentation or contact the support team for assistance.
##Contributing
We welcome contributions from the community to enhance M-trac. Whether you're a developer, designer, or have other skills to bring to the table, your contributions are valuable.

To contribute to the project, follow these steps:

1. Fork the Repository
Click the "Fork" button at the top right corner of the repository to create your copy.

2. Clone Your Fork
Clone your fork of the repository to your local machine using the following command:

bash
Copy code
git clone [URL of your fork]
3. Create a New Branch
Create a new branch for your contribution. Use a descriptive branch name that reflects the nature of your contribution.

4. Make Changes
Make your desired changes to the codebase. Ensure that your changes align with the project's guidelines and coding standards.

5. Test Your Changes
If applicable, test your changes thoroughly to ensure they do not introduce bugs or break existing functionalities.

6. Commit Your Changes
Commit your changes with a clear and concise commit message:

7. Push Changes
Push your changes to your forked repository:

8. Create a Pull Request
Go to the original repository on GitHub and click on the "New Pull Request" button. Provide a clear title and description for your pull request, explaining the purpose of your contribution.

9. Participate in the Discussion
Respond to any feedback or questions from maintainers. Be open to making additional changes based on the feedback received.

10. Code Review and Merge
Once your contribution has been reviewed and approved, a maintainer will merge your changes into the main branch.

Thank you for contributing to M-trac! Your efforts help improve the app for everyone. If you have any questions or need assistance, feel free to reach out to the project maintainers or community.
#Related projects
Ledger : https://github.com/MarkNjunge/Ledger
#Licensing
MIT License

Copyright (c) [2023] [Dennis Kiptoo Ngetich]

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS," WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.

