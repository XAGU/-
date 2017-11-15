# -*- coding: utf-8 -*-

from __future__ import print_function
import os, sys
import oss2

filePath = ""

def percentage(consumed_bytes, total_bytes):
    global filePath
    if total_bytes:
        rate = int(100 * (float(consumed_bytes) / float(total_bytes)))
        print('\r{0}% '.format(rate), end=filePath)
        sys.stdout.flush()

def upload():
    global filePath
    if ( len(sys.argv) > 6 ):
        AccessKeyId     = sys.argv[1]
        AccessKeySecret = sys.argv[2]
        SecurityToken   = sys.argv[3]
        Endpoint        = sys.argv[4] 
        Bucket          = sys.argv[5]
        fileName        = sys.argv[6]
        filePath        = sys.argv[7]
        print(AccessKeyId, AccessKeySecret, SecurityToken, Endpoint, Bucket, fileName, filePath)
        auth = oss2.StsAuth(AccessKeyId , AccessKeySecret, SecurityToken)
        bucket = oss2.Bucket(auth,  Endpoint, Bucket)
        oss2.resumable_upload(bucket, fileName, filePath, progress_callback=percentage)
        print('\rUpload %s to OSS Success!' % filePath)
    else:
        print("Example: %s AccessKeyId AccessKeySecret SecurityToken Endpoint Bucket fileName filePath" % sys.argv[0])
        exit()



if __name__ == '__main__':
    upload()
