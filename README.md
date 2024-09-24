# DataLoggerPy
Logs Data in a given networktable

### PREREQUISITE LIBRARIES ###

# pip install pandas
# pip install tkinter
# pip install openpyxl
# pip install pynetworktables

Description:
Create a python script to run on the driver station that will log roborio generated metrics to a local CSV file with optional upload to google sheets.  See the attached image for the way that we are currently logging such data.  In this example, we are "logging" the absolute and relative encoder values for shooter pitch.
On startup, the script should prompt for a filename, and a list of roborio value labels (network tables data labels) to be logged.  The script should then create a CSV file for output and write a header line that is the list of network table labels as its columns.  A first column called "timestamp" should automatically be included.
The format of the filename should be "filename-YYYYMMDDHHMMSS.csv", so that the user can reuse the base "filename" and we include a timestamp to tell potentially multiple files apart.
Once the file is created, the script will enter logging mode where the user can click a button (call it "Sample"), and the script will pull the network table values, generate a timestamp, and append the row to the file.
Optionally, the user can enter a number of samples and sample interval, so clicking "Sample" will cause the script to run for a duration while logging data to the file.  It would be nice to have this happen conditionally, eg:  when the robot is enabled.
Once sampling is complete, the script should have the option to upload the file to a google sheet, creating a new sheet and/or tab as needed.