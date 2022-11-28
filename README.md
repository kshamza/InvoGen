# InvoGen
This is my implementation of the Sales Invoice Generator Application which is part of the Udacity Testing Nanodegree

This application is developed using the Java language and SWING API. The application allows the user to maintain a record of invoices generated.

An invoice consists of the following fields:

* Invoice ID
* Invoice Date (in the format dd-mm-yyyy)
* Customer Name
* Total which represents the total price of all invoice items.
* List of Items.

Each Invoice Item is represented using the following fields:

* Invoice ID
* Item Name
* Item Price
* Count
* Total Cost (count x Item Price)

The total of the invoice is the sum of the total cost of all the items in the list of items.

## Authors
- [Kamal Hamza](https://github.com/kshamza)

## What You Are Getting

```
├── README.md - This file.
│
├── resources
│   │── assets
│   │   └── images - images and assets mainly used for this file.
│   │── hide - Temp directory to hide CSV files to test exception handling.
│   │── InvoiceHeader.csv - CSV file contains invoice headers
│   └── InvoiceLine.csv - CSV file contains invoice items
│
└── src - Directory that contains all project packages.
    ├── app - application package
    │   └── Main.java - Application driver class with main method.
    │
    ├── controller - controller package
    │   └── Controller.java - Controller class  used to manage the view
    │
    ├── exceptions - exceptions package containing user defined exceptions.
    │   └── FileFormatException.java - Exception class for malformed files.
    │
    ├── model - package containing all the core classes of the project's model
    │   │── FileOperations.java - Class used to perform reading/writing operations
    │   │── InvoiceHeader.java - Class to represent invoice header object
    │   └── InvoiceLine.java - Class to represent invoice item object
    │
    └── view - package containing UI and views of the project.
        │── NewInvoiceInfo.java - Class representing a dialog used to enter new invoice.
        └── SIGFrame.java - The major project frame where all invoices and buttons are viewed.
```

## Getting Started

You will need to get the project's files on your machine in order to run it.

Notice that no build automation is used to manage this project. Please follow the next steps to get started:

1. Download the project as a ZIP file from [here](https://github.com/kshamza/InvoGen/archive/refs/heads/main.zip)
2. Unzip the file
3. Open the project using [IntelliJ IDEA](https://www.jetbrains.com/idea/) or other preferred IDE.
4. Open and run the file ```src/app/Main.java```


### Preview
![A gif showing how to run the project](/resources/assets/images/SIG%20Preview_Run%20Project.gif "Starting the project")


## UI

The UI follows the specifications of the project.

The following are some design aspects taken into consideration while building the SIG Application:

### Main Window
* The application is viewed in a full-screen
* The screen is resizeable
* The minimum screen dimensions are chosen intentionally to maintain readable button descriptions such as Create, Delete, Save, and Cancel.

### Invoice Display

* Selected invoice in the invoices table on the left side will display the invoice info in the right panel.
* Only the Date and Customer Name fields are made editable. The specifications were not clear on whether the invoice items can be edited as well.
* An invoice must be selected before the Delete button can delete an invoice.
* Edited Date and/or Customer Name can be saved using Save button. Changes must be made for the Save button to work.
* Cancel removes any edits the user made to one or both of the fields and reloads the original invoice.

### New Invoice Form

This form is used to create a new invoice.

* The invoice number is automatically populated based on the highest invoice number that was loaded from the file.
* Invoice date is a drop-down selection item so this guarantees the correctness of the format of the input.
* Customer name must be populated and can't be empty
* Invoices are entered in a specific format and verified before the Add button can add the new invoice.
* Reset resets the form
* Cancel closes the form and cancels the entry of the new invoice.
  * The form can also be closed using ESC button.
* New invoice once confirmed is added to the file and the view immediately.

### Data Verification

At different points of the project, data is verified as follows:

* Data must be a non-empty valid date (short months and leap years is not part of the verification)
* Customer name has to be non-empty non-blank input.
* Invoice entry is basic text format (can be improved in the future) but the entry is parsed into Strings and verified.

### Exceptions

This was one of the most challenging parts of this project. The following scenarios are handled:

* Non-existing files in the specified loading directories.
    * If it is the InvoiceHeader.csv file, an error will show and nothing will be loaded.
    * If it is the InvoiceLine.csv file, a message will show and still nothing will be loaded. I was loading partial info, but decided to cancel this.
* Malformed files - Handled in a similar way to the FileNotFound above.
* I am still throwing some print statements in the terminal with details - Not sure if that is a good practice though.


## Tests

There was a required test to verify the read file operations from the ```FileOperations.java```.

The following are the screenshots of the output after reading the file, writing to the file after adding one more invoice with invoice id of 
52, and then reading the file again.

![Screenshot of FileOperations.java](/resources/assets/images/main%20testing%20file%20operations.png "FileOperations.java Screenshot")

![Screenshot of output after reading invoices](/resources/assets/images/main%20testing%20file%20operations_output1.png "Reading")

![Screenshot of output after writing to file and reading again.](/resources/assets/images/main%20testing%20file%20operations_output2.png "Writing then Reading")
  
The choice of the invoice ID of 52 was intentional to show that the New Invoice will automatically assign the correct Invoice ID to the new ID to be created.

## Known Issues

The following are some issues:

* Conflicting ```ActionListener``` and ```FocusListener``` for Date and Customer Name fields.
  * There is a situation in which if the Date or Customer Name are modified to an invalid value and the user used enter, the action will fire twice since both action and focus listener (which calls action) are both triggered.
* Date Verification needs to be more detailed
* No verification is done on the Customer Name forming proper names.
* When coming back from the New Invoice form, the focus does not go back to the left panel and the button down does not navigate invoices.
* Was presenting partial data if invoices are loaded, however, opted out since that required implementing other logic to prevent writing to file from using the dummy values used to present missing data.
  * For example, invoice lines are missing, I can present all invoice data but for total I can say -1. Now I have to prevent the user from writing to the file otherwise -1 will get to the file.


## Lessons Learned
* Unclear Specifications can lead to very complicated design and implementation challenges.
* Need to do a better job at committing more to GitHub and less offline work.
* While searching for answers to a few challenges, found a lot of discussion about NetBeans IDE. It seems to be more of a drag and drop.
