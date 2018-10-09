# -*- coding: utf-8 -*-

from __future__ import print_function
import os, sys, requests, json
import oss2

filePath        = ''
login_url       = 'https://api.xiaolian365.com/c/login'
login_payload   = ''
headers         = {'content-type':'application/json'}
oss_url         = 'https://api.xiaolian365.com/c/oss/credential/one'
oss_payload     = ''
token           = ''
AccessKeyId     = ''
AccessKeySecret = ''
SecurityToken   = ''
Endpoint        = ''
Bucket          = ''

package_name    = 'apk/com.xiaolian.amigo_{}.apk'
package_path    = 'app/build/outputs/apk/prod/release/com.xiaolian.amigo_{}.sign.zipalign.apk'
fileName        = ''
constantName    = 'apk/com.xiaolian.amigo.apk'
uploadConstant  = False


def percentage(consumed_bytes, total_bytes):
    global filePath
    if total_bytes:
        rate = int(100 * (float(consumed_bytes) / float(total_bytes)))
        print('\r{0}% '.format(rate), end=filePath)
        sys.stdout.flush()

def upload():
    global AccessKeyId
    global AccessKeySecret
    global SecurityToken
    global Endpoint
    global Bucket
    global fileName
    global uploadConstant
    print(AccessKeyId, AccessKeySecret, SecurityToken, Endpoint, Bucket, fileName, constantName, filePath)
    auth = oss2.StsAuth(AccessKeyId , AccessKeySecret, SecurityToken)
    bucket = oss2.Bucket(auth,  Endpoint, Bucket)
    if uploadConstant:
        print('Uploading constant')
        oss2.resumable_upload(bucket, constantName, filePath, progress_callback=percentage)
        print('\r{} Upload {} to OSS Success!'.format(constantName, filePath))
    else:
        print('Uploading {}'.format(fileName))
        oss2.resumable_upload(bucket, fileName, filePath, progress_callback=percentage)
        print('\r{} Upload {} to OSS Success!'.format(fileName, filePath))

def getParam():
    global AccessKeyId
    global AccessKeySecret
    global SecurityToken
    global Endpoint
    global Bucket
    global headers
    global oss_url
    headers = {'content-type':'application/json', 'token': token}
    response = requests.post(oss_url, headers=headers)
    data = json.loads(response.text)
    AccessKeyId     = data['data']['accessKeyId']
    AccessKeySecret = data['data']['accessKeySecret']
    SecurityToken   = data['data']['securityToken']
    Endpoint        = data['data']['endpoint']
    Bucket          = data['data']['bucket']
    upload()

def login():
    global filePath
    global fileName
    global login_url
    global login_payload
    global token
    global uploadConstant
    if ( len(sys.argv) > 2 ):
        mobile     = sys.argv[1]
        password = sys.argv[2]
        version = sys.argv[3]
        uploadConstant = sys.argv[4].lower() == 'true'
        if uploadConstant:
            print("Upload constant")
        else:
            print("Do not upload constant")
        fileName = package_name.format(version)
        filePath = package_path.format(version)
        login_payload = {'mobile': mobile, 'password': password ,'appVersion':version,'system':2 }
        response = requests.post(login_url, data=json.dumps(login_payload), headers=headers)
        print(response.text)
        token = json.loads(response.text)['data']['token']
        getParam()
    else:
        print("Example: %s mobile password version" % sys.argv[0])
        exit()

if __name__ == '__main__':
    #  upload()
    login()
