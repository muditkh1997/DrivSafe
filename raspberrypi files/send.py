#!/usr/bin/python
import re
import gc
import serial
import time
import requests
import json
import re
from decimal import Decimal
from decimal import *
from firebase import firebase
from random import randint
firebase_url = 'https://drivsafe-e0083.firebaseio.com/'
ser = serial.Serial('/dev/ttyACM0', 9600,timeout=1)

while 1:
        try:

                temperature_c = ser.readline()
                temp2 = 0.000
                print temperature_c
                if '\n' in temperature_c:
                        continue
                if '\n' not in temperature_c:
                        break
        except IOError:
                print('Error! Something went wrong.')

#Connect to Serial Port for communication
#Setup a loop to send Temperature values at fixed intervals
#in seconds
while 1:
        try:
                #temperature value obtained from Arduino + DHT11 Temp Sensor

                temperature_c = ser.readline()
                temp2 = 0.000
                print temperature_c
                if '\n' not in temperature_c:
                        continue
                if '\n' not in temperature_c:
                        break
        except IOError:
                print('Error! Something went wrong.')

#Connect to Serial Port for communication
#Setup a loop to send Temperature values at fixed intervals
#in seconds
while 1:
        try:
                #temperature value obtained from Arduino + DHT11 Temp Sensor

                temperature_c = ser.readline()
                temp2 = 0.000
                print temperature_c
                if '\n' not in temperature_c:
                        continue
                print "hiii"
                print temperature_c
                print "hello"
                temp2=float(temperature_c)
                temp2 = str(temp2)
                #temperature_c.replace('\r','')
                #temperature_c.replace('\n','')
                #current time and date
                time_hhmmss = time.strftime('%H:%M:%S')
                date_mmddyyyy = time.strftime('%d/%m/%Y')

                #current location name
                location = 'Readings';
                print
                print temperature_c + ',' + time_hhmmss + ',' + date_mmddyyyy + ',' + location

                pressure = randint(15,25)
                #insert record
                data = {'date':date_mmddyyyy,'time':time_hhmmss,'temperature': temp2,'pressure':pressure}

                print "hiii"
                print temperature_c
                print "hello"
                temp2=float(temperature_c)
                temp2 = str(temp2)
                #temperature_c.replace('\r','')
                #temperature_c.replace('\n','')
                #current time and date
                time_hhmmss = time.strftime('%H:%M:%S')
                date_mmddyyyy = time.strftime('%d/%m/%Y')

                #current location name
                location = 'Readings';
                print
                print temperature_c + ',' + time_hhmmss + ',' + date_mmddyyyy + ',' + location

                pressure = randint(15,25)
                #insert record
                data = {'date':date_mmddyyyy,'time':time_hhmmss,'temperature': temp2,'pressure':pressure}
                result = requests.post(firebase_url + '/' + location + '.json'  ,  data=json.dumps(data))

                print 'Record inserted. Result Code = ' + str(result.status_code) + ',' + result.text


        except IOError:
                print('Error! Something went wrong.')
