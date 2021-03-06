/**************************************************************************************************
  Filename:       jni.c
  Revised:        $Date: 2016-2-27 14:19:43 $
  Revision:       $Revision: 0.1 $

  Description:    jni接口实现
**************************************************************************************************/
//
// Created by Kurtis Hu on 3/3/16.
//

/*********************************************************************
 * INCLUDES
 */
#include "bluetooth-lib.h"
#include "com_dreamfactory_library_convert_BluetoothConvert.h"
#include <string.h>
#include <android/log.h>

#ifndef LOG_TAG
#define LOG_TAG "ANDROID_LAB"
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)
#endif

/*********************************************************************
 * CONSTANTS
 */
#define READ_INDEX_LEN  8            //读操作序列长度
#define WRITE_INDEX_LEN 13           //写操作序列长度

#define ONE_WRITE_LEN       1
#define DATE_WRITE_LEN      6
#define TIME_ADDR_START     65

/*********************************************************************
 * GLOBAL VARIABLES
 */
unsigned char CRC8Table[256]   =   {
        0x00, 0x5e, 0xbc, 0xe2, 0x61, 0x3f, 0xdd, 0x83,
        0xc2, 0x9c, 0x7e, 0x20, 0xa3, 0xfd, 0x1f, 0x41,
        0x9d, 0xc3, 0x21, 0x7f, 0xfc, 0xa2, 0x40, 0x1e,
        0x5f, 0x01, 0xe3, 0xbd, 0x3e, 0x60, 0x82, 0xdc,
        0x23, 0x7d, 0x9f, 0xc1, 0x42, 0x1c, 0xfe, 0xa0,
        0xe1, 0xbf, 0x5d, 0x03, 0x80, 0xde, 0x3c, 0x62,
        0xbe, 0xe0, 0x02, 0x5c, 0xdf, 0x81, 0x63, 0x3d,
        0x7c, 0x22, 0xc0, 0x9e, 0x1d, 0x43, 0xa1, 0xff,
        0x46, 0x18, 0xfa, 0xa4, 0x27, 0x79, 0x9b, 0xc5,
        0x84, 0xda, 0x38, 0x66, 0xe5, 0xbb, 0x59, 0x07,
        0xdb, 0x85, 0x67, 0x39, 0xba, 0xe4, 0x06, 0x58,
        0x19, 0x47, 0xa5, 0xfb, 0x78, 0x26, 0xc4, 0x9a,
        0x65, 0x3b, 0xd9, 0x87, 0x04, 0x5a, 0xb8, 0xe6,
        0xa7, 0xf9, 0x1b, 0x45, 0xc6, 0x98, 0x7a, 0x24,
        0xf8, 0xa6, 0x44, 0x1a, 0x99, 0xc7, 0x25, 0x7b,
        0x3a, 0x64, 0x86, 0xd8, 0x5b, 0x05, 0xe7, 0xb9,
        0x8c, 0xd2, 0x30, 0x6e, 0xed, 0xb3, 0x51, 0x0f,
        0x4e, 0x10, 0xf2, 0xac, 0x2f, 0x71, 0x93, 0xcd,
        0x11, 0x4f, 0xad, 0xf3, 0x70, 0x2e, 0xcc, 0x92,
        0xd3, 0x8d, 0x6f, 0x31, 0xb2, 0xec, 0x0e, 0x50,
        0xaf, 0xf1, 0x13, 0x4d, 0xce, 0x90, 0x72, 0x2c,
        0x6d, 0x33, 0xd1, 0x8f, 0x0c, 0x52, 0xb0, 0xee,
        0x32, 0x6c, 0x8e, 0xd0, 0x53, 0x0d, 0xef, 0xb1,
        0xf0, 0xae, 0x4c, 0x12, 0x91, 0xcf, 0x2d, 0x73,
        0xca, 0x94, 0x76, 0x28, 0xab, 0xf5, 0x17, 0x49,
        0x08, 0x56, 0xb4, 0xea, 0x69, 0x37, 0xd5, 0x8b,
        0x57, 0x09, 0xeb, 0xb5, 0x36, 0x68, 0x8a, 0xd4,
        0x95, 0xcb, 0x29, 0x77, 0xf4, 0xaa, 0x48, 0x16,
        0xe9, 0xb7, 0x55, 0x0b, 0x88, 0xd6, 0x34, 0x6a,
        0x2b, 0x75, 0x97, 0xc9, 0x4a, 0x14, 0xf6, 0xa8,
        0x74, 0x2a, 0xc8, 0x96, 0x15, 0x4b, 0xa9, 0xf7,
        0xb6, 0xe8, 0x0a, 0x54, 0xd7, 0x89, 0x6b, 0x35
};

jint ReadIndexArr[]  = {0,1,2,3,40,41,63,64};
jint WriteIndexArr[] = {10,11,12,13,14, 5,6,7,8,9, 60,61,62};

/*********************************************************************
 * LOCAL FUNCTIONS
 */
jchar CrcSum(const jchar *crcData, jchar crcDataLen);


/*********************************************************************
 * PUBLIC FUNCTIONS
 */


/*
 * Class:     com_dreamfactory_library_convert_BluetoothConvert
 * Method:    encapsulateShowData
 * Signature: ()[B
 */
JNIEXPORT jbyteArray JNICALL Java_com_dreamfactory_library_convert_BluetoothConvert_encapsulateShowData
        (JNIEnv * env, jclass obj) {
    //新添加代码
    jchar i;

    jbyteArray dat = (*env)->NewByteArray(env, READ_INDEX_LEN+4);
    jbyte *datPtr = (*env)->GetByteArrayElements(env, dat, 0 );     //获得byte数组指针

    jcharArray cdat = (*env)->NewCharArray(env, READ_INDEX_LEN+4);
    jchar *cdatPtr = (*env)->GetCharArrayElements(env, cdat, 0 );     //获得char数组指针

    cdatPtr[0] = 0x01;                       //从设备id
    cdatPtr[1] = 0x02;                       //随机写操作
    cdatPtr[2] = READ_INDEX_LEN;            //读数据长度

    for (i = 0; i <READ_INDEX_LEN; ++i)
        cdatPtr[3+i]   = ReadIndexArr[i];   //写入地址

    cdatPtr[READ_INDEX_LEN+3] = CrcSum( cdatPtr, READ_INDEX_LEN+3 );          //crc检验值

    LOGE("Encapsulate show data--->");
    for (i = 0; i <READ_INDEX_LEN+4 ; ++i) {
        datPtr[i] = cdatPtr[i];
        LOGE("%x--%x",cdatPtr[i],datPtr[i]);
    }

    (*env)->ReleaseByteArrayElements(env, dat, datPtr, 0);                  //释放byte数组指针
    (*env)->ReleaseCharArrayElements(env, cdat, cdatPtr, 0);                  //释放byte数组指针
    return dat;
}

/*
 * Class:     com_dreamfactory_library_convert_BluetoothConvert
 * Method:    decapsulateShowData
 * Signature: ([B)[I
 */
JNIEXPORT jintArray JNICALL Java_com_dreamfactory_library_convert_BluetoothConvert_decapsulateShowData
        (JNIEnv *env, jclass obj, jbyteArray array) {

    //新增代码
    jbyte* arrPtr;
    jint i, crcSum=0;

    arrPtr = (*env)->GetByteArrayElements(env, array, 0);            //传入byte数组指针
    if( arrPtr == NULL )
        return NULL;

    jcharArray cdat = (*env)->NewCharArray(env, READ_INDEX_LEN+4);
    jchar *cdatPtr = (*env)->GetCharArrayElements(env, cdat, 0 );     //获得char数组指针

    LOGE("Decapsulate show data--->");
    for (i = 0; i <READ_INDEX_LEN+4 ; ++i) {
        cdatPtr[i] = arrPtr[i];
        LOGE("%x--%x",cdatPtr[i],arrPtr[i]);
    }

    crcSum = CrcSum( cdatPtr, READ_INDEX_LEN+3 );

    if( crcSum == arrPtr[READ_INDEX_LEN+3] )                            //如果检验和正确
    {
        jintArray dat = (*env)->NewIntArray(env,READ_INDEX_LEN );
        jint *datPtr = (*env)->GetIntArrayElements(env, dat, 0 );       //获得int数组指针

        for (i = 0; i < READ_INDEX_LEN; ++i)
            datPtr[i] = arrPtr[3+i];

        (*env)->ReleaseByteArrayElements(env, array, arrPtr, 0);        //释放int数组指针
        (*env)->ReleaseIntArrayElements(env, dat, datPtr, 0);           //释放byte数组指针

        return dat;
    }

    (*env)->ReleaseByteArrayElements(env, array, arrPtr, 0);        //释放int数组指针
    (*env)->ReleaseCharArrayElements(env, cdat, cdatPtr, 0);        //释放int数组指针
    return NULL;
}

/*
 * Class:     com_dreamfactory_library_convert_BluetoothConvert
 * Method:    encapsulateSetData
 * Signature: ([I)[B
 */
JNIEXPORT jbyteArray JNICALL Java_com_dreamfactory_library_convert_BluetoothConvert_encapsulateSetData
        (JNIEnv *env, jclass obj, jintArray array) {

    //新添加代码
    jint* arrPtr;
    jchar i,lowbyte;

    arrPtr = (*env)->GetIntArrayElements(env, array, 0);            //传入int数组指针
    if( arrPtr == NULL )    //没有写入的数据
        return NULL;

    jcharArray cdat = (*env)->NewCharArray(env, ONE_WRITE_LEN*2+4);
    jchar *cdatPtr = (*env)->GetCharArrayElements(env, cdat, 0 );     //获得char数组指针

    cdatPtr[0] = 0x01;                       //从设备id
    cdatPtr[1] = 0x04;                       //随机写操作
    cdatPtr[2] = ONE_WRITE_LEN*2;          //写数据长度

    for (i = 0; i <ONE_WRITE_LEN*2; ++i)
    {
        lowbyte   = arrPtr[i] & 0xFF;           //取int数值的低字节
        cdatPtr[3+i] = lowbyte;  //写入数据
    }

    cdatPtr[2*ONE_WRITE_LEN+3] = CrcSum( cdatPtr, 2*ONE_WRITE_LEN+3 );    //crc检验值

    jbyteArray dat = (*env)->NewByteArray(env, ONE_WRITE_LEN*2+4);
    jbyte *datPtr = (*env)->GetByteArrayElements(env, dat, 0 );               //获得byte数组指针

    LOGE("Encapsulate setting data--->");
    for (i = 0; i <ONE_WRITE_LEN*2+4 ; ++i) {
        datPtr[i] = cdatPtr[i];
        LOGE("%x--%x",cdatPtr[i],datPtr[i]);
    }

    (*env)->ReleaseIntArrayElements(env, array, arrPtr, 0);                 //释放int数组指针
    (*env)->ReleaseByteArrayElements(env, dat, datPtr, 0);                  //释放byte数组指针
    (*env)->ReleaseCharArrayElements(env, cdat, cdatPtr, 0);                  //释放byte数组指针

    return dat;
}

/*
 * Class:     com_dreamfactory_library_convert_BluetoothConvert
 * Method:    decapsulateSetData
 * Signature: ([B)I
 */
JNIEXPORT jint JNICALL Java_com_dreamfactory_library_convert_BluetoothConvert_decapsulateSetData
        (JNIEnv *env, jclass obj, jbyteArray array) {

    jbyte *arrPtr;
    jchar i, crcSum = 0, ret = -1;

    arrPtr = (*env)->GetByteArrayElements(env, array, 0);            //传入byte数组指针
    if (arrPtr == NULL)
        return -1;

    jcharArray cdat = (*env)->NewCharArray(env, 4);
    jchar *cdatPtr = (*env)->GetCharArrayElements(env, cdat, 0);     //获得char数组指针

    LOGE("Decapsulate setting data--->");
    for (i = 0; i < 4; ++i) {
        cdatPtr[i] = arrPtr[i] &0xff;
        LOGE("%x--%x", cdatPtr[i], arrPtr[i]);
    }
    crcSum = CrcSum(cdatPtr, 3);
    LOGE("crc--%x", crcSum);

    if (crcSum == cdatPtr[3] ) {
    if (arrPtr[2] == ONE_WRITE_LEN)      //写入数据长度正常
        ret = 1;
    else
        ret = 0;
    }
    else
        ret = -1;

    (*env)->ReleaseCharArrayElements(env, cdat, cdatPtr, 0);        //释放char数组指针
    (*env)->ReleaseByteArrayElements(env, array, arrPtr, 0);        //释放byte数组指针
    return ret;
}

/*
 * Class:     Java_com_dreamfactory_library_convert_BluetoothConvert_encapsulateSysTime
 * Method:    encapsulateSetData
 * Signature: ([I)[B
 */
JNIEXPORT jbyteArray JNICALL Java_com_dreamfactory_library_convert_BluetoothConvert_encapsulateSysTime
        (JNIEnv *env, jclass obj, jintArray array) {
    //新添加代码
    jint* arrPtr;
    jchar i,lowbyte;

    arrPtr = (*env)->GetIntArrayElements(env, array, 0);            //传入int数组指针
    if( arrPtr == NULL )    //没有写入的数据
        return NULL;

    jcharArray cdat = (*env)->NewCharArray(env, DATE_WRITE_LEN+5);
    jchar *cdatPtr = (*env)->GetCharArrayElements(env, cdat, 0 );     //获得char数组指针

    cdatPtr[0] = 0x01;                       //从设备id
    cdatPtr[1] = 0x03;                       //随机写操作
    cdatPtr[2] = TIME_ADDR_START;            //起始地址
    cdatPtr[3] = DATE_WRITE_LEN;             //写数据长度

    for (i = 0; i <DATE_WRITE_LEN; ++i)
    {
        lowbyte   = arrPtr[i] & 0xFF;           //取int数值的低字节
        cdatPtr[4+i] = lowbyte;  //写入数据
    }

    cdatPtr[DATE_WRITE_LEN+4] = CrcSum( cdatPtr, DATE_WRITE_LEN+4 );    //crc检验值

    jbyteArray dat = (*env)->NewByteArray(env, DATE_WRITE_LEN+5);
    jbyte *datPtr = (*env)->GetByteArrayElements(env, dat, 0 );               //获得byte数组指针

    LOGE("Encapsulate setting data--->");
    for (i = 0; i <DATE_WRITE_LEN+5 ; ++i) {
        datPtr[i] = cdatPtr[i];
        LOGE("%x--%x",cdatPtr[i],datPtr[i]);
    }

    (*env)->ReleaseIntArrayElements(env, array, arrPtr, 0);                 //释放int数组指针
    (*env)->ReleaseByteArrayElements(env, dat, datPtr, 0);                  //释放byte数组指针
    (*env)->ReleaseCharArrayElements(env, cdat, cdatPtr, 0);                  //释放byte数组指针

    return dat;
}

/*********************************************************************
 * @fn      Bti_Write_Transport
 *
 * @brief   G(x)=x^8+x^5+x^4+1
 *
 * @param   none
 *
 * @return  none
 */
jchar CrcSum(const jchar *crcData, jchar crcDataLen)
{
    jchar crc8 = 0;
    for(; crcDataLen > 0; crcDataLen--)
    {
        crc8 = CRC8Table[crc8^*crcData];
        crcData++;
    }

    return(crc8);
}