//
// Created by Kurtis Hu on 3/3/16.
//

#include "bluetooth-lib.h"
#include "com_dreamfactory_library_convert_BluetoothConvert.h"

/*
 * Class:     com_dreamfactory_library_convert_BluetoothConvert
 * Method:    encapsulateShowData
 * Signature: ()[B
 */
JNIEXPORT jbyteArray JNICALL Java_com_dreamfactory_library_convert_BluetoothConvert_encapsulateShowData
        (JNIEnv * env, jclass obj) {
   jbyteArray  byteArrays = (*env)->NewByteArray(env, 2);

    jbyte *b = (*env)->GetByteArrayElements(env, byteArrays, 0);
    b[0] = 1;
    b[1] = 2;

    return  byteArrays;
}

/*
 * Class:     com_dreamfactory_library_convert_BluetoothConvert
 * Method:    decapsulateShowData
 * Signature: ([B)[I
 */
JNIEXPORT jintArray JNICALL Java_com_dreamfactory_library_convert_BluetoothConvert_decapsulateShowData
        (JNIEnv *env, jclass obj, jbyteArray array) {

    jintArray jintArray1 = (*env)->NewIntArray(env, 2);

    jint *i = (*env)->GetIntArrayElements(env, jintArray1, 0);
    i[0] = 1;
    i[0] = 2;
    return jintArray1;
}

/*
 * Class:     com_dreamfactory_library_convert_BluetoothConvert
 * Method:    encapsulateSetData
 * Signature: ([I)[B
 */
JNIEXPORT jbyteArray JNICALL Java_com_dreamfactory_library_convert_BluetoothConvert_encapsulateSetData
        (JNIEnv *env, jclass obj, jintArray array) {

    jbyteArray  byteArrays = (*env)->NewByteArray(env, 2);

    jbyte *b = (*env)->GetByteArrayElements(env, byteArrays, 0);
    b[0] = 3;
    b[1] = 4;

    return  byteArrays;
}

/*
 * Class:     com_dreamfactory_library_convert_BluetoothConvert
 * Method:    decapsulateSetData
 * Signature: ([B)I
 */
JNIEXPORT jint JNICALL Java_com_dreamfactory_library_convert_BluetoothConvert_decapsulateSetData
        (JNIEnv *env, jclass obj, jbyteArray array) {

    // TODO

    return 1;
}