/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
#include "predict.h"
#include "train.h"
#include "samsung_lab411_svm_LibSVM.h"
/* Header for class samsung_lab411_svm_LibSVM */

JNIEXPORT jint JNICALL Java_samsung_lab411_svm_LibSVM_trainClassifierNative
  (JNIEnv *env, jclass obj, jstring trainingFileS,
	        jint kernelType, jint cost, jfloat gamma, jint isProb, jstring modelFileS)
{
	 jboolean isCopy;
	    const char *trainingFile = env->GetStringUTFChars(trainingFileS, &isCopy);
	    const char *modelFile = env->GetStringUTFChars(modelFileS, &isCopy);

	    int v = train(trainingFile, kernelType, cost, gamma, isProb, modelFile);

	    env->ReleaseStringUTFChars(trainingFileS, trainingFile);
	    env->ReleaseStringUTFChars(modelFileS, modelFile);
	   // LOGD("cmd: train( %s,%d,%d,%f,%d,%s)",trainingFile,kernelType,cost,gamma,isProb,modelFile);
	    return v;
	}


JNIEXPORT jint JNICALL Java_samsung_lab411_svm_LibSVM_doClassificationNative
  (JNIEnv *env, jclass obj, jint probability_estimate,jstring classifyFileS,jstring modelFileS, jstring outputFileS)
{
	//LOGD("Do classification");
	    jboolean isCopy;
	    const char *classifyFile = env->GetStringUTFChars(classifyFileS, &isCopy);
	    const char *modelFile = env->GetStringUTFChars(modelFileS, &isCopy);
	    const char *outputFile = env->GetStringUTFChars(outputFileS, &isCopy);

	    int resultLabel = predict(probability_estimate,classifyFile, modelFile, outputFile);
	    env->ReleaseStringUTFChars(classifyFileS, classifyFile);
	    env->ReleaseStringUTFChars(modelFileS, modelFile);
	    env->ReleaseStringUTFChars(outputFileS, outputFile);
	    return resultLabel;

	}
