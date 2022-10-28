//
// Created by develop on 2022/10/27.
//

#ifndef DROIDJS_MOBILE_REDIRECTING_OUPUT_H
#define DROIDJS_MOBILE_REDIRECTING_OUPUT_H


#include <pthread.h>
#include <unistd.h>
#include <android/log.h>
#define LOG_TAG "nodejs"
int start_redirecting_stdout_stderr();

#endif //DROIDJS_MOBILE_REDIRECTING_OUPUT_H