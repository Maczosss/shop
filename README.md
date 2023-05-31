# shop
Simple shop app where I want to implement some design patterns and partition into smaller modules.

This application can be opened at this moment through the UI module. Inside the displayed window, there is a requirement for the path to your.properties file.
In it, define:

filePath=[THIS PATH IS FOR STORING TXT FILE WITH DATA]
jsonFileLocation= [THIS PATH IS FOR STORING A JSON FILE WITH DATA]

databaseURL=[AS NAME SUGGEST, PATH TO YOUR DB WITH IT'S NAME SPECIFIED]
databaseURL=...
databaseUser=...
databasePassword=...

maxNumberOfProducts = [MAX NUMBER OF PRODUCTS INSIDE ONE POSSIBLE ORDER]
numberOfOrders = [NUMBER OF GENERATED ORDERS]
databaseName=[SAME AS IN databaseURL PROPERTY]

When properties are provided, the user can initialize them inside the program with the button "set data-source connection",
If everything is set up correctly, then a connection with the data source will be created.

If you want to create dummy data, then after initialization, click on "Initialize data", after choosing the correct data source.
That would create new records inside the database, a JSON file, or a TXT file to work on.
