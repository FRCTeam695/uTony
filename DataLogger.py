### PREREQUISITE LIBRARIES ###

# pip install pandas
# pip install tk
# pip install openpyxl
# pip install pynetworktables

import tkinter as tk
import pandas as pd
import time

from networktables import NetworkTableEntry, NetworkTables 

### INITIALIZATIONS ###
fileName = ""
tableName = ""
### Get File Name and Table Name using tkinter ###

def submit():
    global fileName
    fileName = fileName_tk.get()
    global tableName
    tableName = tableName_tk.get()
    
    root.destroy() 

root = tk.Tk()
fileName_tk = tk.StringVar()
tableName_tk = tk.StringVar()

fileNameLabel_tk = tk.Label(root, text = 'File Name', font=('calibre',10, 'bold'))
fileNameEntry_tk = tk.Entry(root,textvariable = fileName_tk, font=('calibre',10,'normal'))

tableNameLabel_tk = tk.Label(root, text = 'Network Table', font=('calibre',10, 'bold'))
tableNameEntry_tk = tk.Entry(root, textvariable = tableName_tk, font=('calibre',10,'normal'))

sub_btn=tk.Button(root,text = 'Submit', command = submit)

fileNameLabel_tk.grid(row =0, column = 0)
fileNameEntry_tk.grid(row =0, column = 1)

tableNameLabel_tk.grid(row =1, column = 0)
tableNameEntry_tk.grid(row =1, column = 1)

sub_btn.grid(row =2, column = 1)
root.mainloop() 

### NETWORK TABLES INIT ###

def connectionListener(connected, info):
    print(info, "; Connected=%s" % connected)

# set the roborio IP address
ip = "10.6.95.2"

NetworkTables.initialize(server=ip)
NetworkTables.addConnectionListener(connectionListener, immediateNotify=True)
print(tableName)

nt = NetworkTables.getTable(tableName)

### PANDAS DATAFRAME INIT ###
#create column lables
time.sleep(1)
ntKeyList = nt.getKeys()

data = pd.DataFrame(columns = ["Time"] + ntKeyList) 
print(data)

######################### MAIN LOOP #########################
isSaved = False
def insert (source_str, insert_str, pos):
    return source_str[:pos] + insert_str + source_str[pos:]

def sample():
    t = time.ctime()
    t = insert(t, str( round(time.time() % 1, 3) )[1:] , -5)  #Adds milisecond percision
    newRow = {'Time': t}
    for key in ntKeyList:
        newRow = newRow | {key : nt.getEntry(key).get()}
        
    #print(newRow)
    data.loc[len(data)] = newRow
    print(data.tail(1))

def sampleInt():
    for i in range(0,sampleNum_tk.get()):
        sample()
        time.sleep(sampleInt_tk.get())

def save():
    currentTime = time.localtime(time.time())
    currentFileName = fileName + "-" + time.strftime("%Y%m%d%H%M%S",currentTime)
    data.to_csv(currentFileName + ".csv", index=False)
    data.to_excel(currentFileName + ".xlsx", sheet_name= currentFileName , index = False)
    global isSaved
    isSaved = True

root = tk.Tk()
sampleInt_tk = tk.DoubleVar()
sampleNum_tk = tk.IntVar()

sampleLabel_tk = tk.Label(root, text = 'One Sample', font=('calibre',10, 'bold'))
sample_btn=tk.Button(root,text = 'Sample', command = sample)

orLabel_tk = tk.Label(root, text = "OR", font = ('calibre',10,'bold'))

sampleIntLabel_tk = tk.Label(root, text = 'Sample Interval (seconds)', font=('calibre',10, 'bold'))
sampleIntEntry_tk = tk.Entry(root,textvariable = sampleInt_tk, font=('calibre',10,'normal'))

sampleNumLabel_tk = tk.Label(root, text = 'Number of Samples', font=('calibre',10, 'bold'))
sampleNumEntry_tk = tk.Entry(root, textvariable = sampleNum_tk, font=('calibre',10,'normal'))

sampInt_btn=tk.Button(root,text = 'Sample Interval', command = sampleInt )

save_btn = tk.Button(root, text = "Save", command = save)

sampleLabel_tk.grid(row = 0, column = 0)
sample_btn.grid(row = 1, column = 0)

sampleIntLabel_tk.grid(row = 0, column = 2)
sampleIntEntry_tk.grid(row = 1, column = 2)

orLabel_tk.grid(row = 0, column = 1)

sampleNumLabel_tk.grid(row = 0, column = 3)
sampleNumEntry_tk.grid(row = 1, column = 3)

sampInt_btn.grid(row = 2, column = 3)

save_btn.grid(row = 4, column = 2)

root.mainloop()

if(not isSaved):
    save()