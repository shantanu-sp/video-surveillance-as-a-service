import os
from threading import *
from time import sleep
import requests

# stream = os.popen('ps -ef | grep darknet | wc -l')
#
# stream = stream.read().strip()

#files = ['1','2','3','4','5','6','7','8','9','10','11','12','13','14','15','16']
#files = ['temp1','temp2','temp3','temp4','temp5','temp6','temp7','temp8','temp9','temp10','temp11','temp12','temp13','temp14','temp15','temp16']
files = ['test.h264','test1.h264','test2.h264','test3.h264','test4.h264','test5.h264','test6.h264']
piFree = True
pi = []
controller = []

server = 'http://192.168.0.181:8080'
endpoint_url_controller = '/acceptVideoFile'
endpoint_url_pi_processing = '/acceptVideoAndOpFile'
video_file_key = 'file1'
output_file_key = 'file2'

def postVideoFile(fileName):
    url = server + endpoint_url_controller
    upload_files = {video_file_key: open(fileName, 'rb')}
    r = requests.post(url, files=upload_files)
    print(r)

def postVideoAndOpFile(videoFileName,opFileName):
    url = server + endpoint_url_pi_processing
    upload_files = {video_file_key: open(videoFileName, 'rb'), output_file_key: open(opFileName, 'rb')}
    r = requests.post(url, files=upload_files)
    print(r)

def runModelOnPi(videoName):
    opFileName = videoName + 'output.txt'
    f = open(opFileName,"w+")
    #runCommand = 'ls -l > ./' + opFileName
    runCommand = './darknet detector demo cfg/coco.data cfg/yolov3-tiny.cfg yolov3-tiny.weights ' + videoName + ' > ./' + opFileName
    os.popen(runCommand)
    f.close()
    return opFileName

def thread_runClassificationOnPi(video_file_name):
    global  piFree
    print('Processing on Pi ' + str(video_file_name))
    opFileGenerated = runModelOnPi(video_file_name)
    print('Model finished running on Pi.')
    if opFileGenerated:
        postVideoAndOpFile(video_file_name, opFileGenerated)
    #sleep(20)
    print('Processed video on Pi :' + str(video_file_name))
    pi.append(video_file_name)
    piFree = True

def thread_runOnController(video_file_name):
    print ('Sending file to controller  ' + str(video_file_name))
    postVideoFile(video_file_name)
    #sleep(5)
    print ('Sent file to controller ' + str(video_file_name))
    controller.append(video_file_name)

for file in files:
    t1 = Thread(target=thread_runClassificationOnPi, args=(file,))
    t2 = Thread(target=thread_runOnController, args=(file,))
    if not piFree:
        # Send video to controller
        t2.start()
        t2.join()
    else:
        # Spawn a thread to process video here and later send video and op to controller.
        piFree = False
        t1.start()
        #t1.join()

if t1.is_alive() is True:
    print('Pi is still processing the last video!')
    t1.join()

print('Finished exec.')
print(pi)
print(controller)
