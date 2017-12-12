#!/bin/bash
applicationId=com.xiaolian.amigo
versionName=""
storepass=EOl5t9aSlb1j4whEIPIku/0K0rfHTOB1JxXSS31czZk=
keypass=WrzwBIrMPHN5q3pzDBWFHuo0FmeReiJ3guFSX4ZYP74=
filePath=app/build/outputs/apk/prod/release/

sign() {
    jarsigner -digestalg SHA1 -sigalg MD5withRSA -verbose -keystore android_amigo.jks -storepass ${storepass} -signedjar ${filePath}${applicationId}_${versionName}.sign.apk ${filePath}${applicationId}_${versionName}.apk android_amigo -keypass ${keypass}
    echo "sign finish"
}

zipalign() {
    ~/Library/Android/sdk/build-tools/26.0.2/zipalign -v 4 ${filePath}${applicationId}_${versionName}.sign.apk ${filePath}${applicationId}_${versionName}.sign.zipalign.apk
}

if [ $# -gt 0 ];then
    versionName=$1
    sign
    if [[ -a ${filePath}${applicationId}_${versionName}.sign.zipalign.apk ]];then
        rm ${filePath}${applicationId}_${versionName}.sign.zipalign.apk
    fi;
    zipalign
else
    echo "please input version name"
fi

