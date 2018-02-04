import time
import threading
from threading import Thread
from thread import *
import os

def startprgm(i):
    print "Running thread %d" % i
    if (i == 0):
        time.sleep(1)
        print('Running: try2.py')
        #os.system("sudo python /home/pi/project/try2.py")
    elif (i == 1):
        time.sleep(1)
        print('Running: send.py')
        #os.system("sudo python /home/pi/project/send.py")
    else:
        pass
for i in range(2):
    t = threading.Thread(target=startprgm, args=(i,))

    t.start()

