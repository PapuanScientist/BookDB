#include <jni.h>
#include <string>
#include "base64.h"
#include <iostream>

static const std::string base64_chars =
        "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                "abcdefghijklmnopqrstuvwxyz"
                "0123456789.:+/";

static inline bool is_base64(unsigned char c) {
    return (isalnum(c) || (c == '+') || (c == '/'));
}

std::string base64_encode(unsigned char const *bytes_to_encode, unsigned int in_len) {
    std::string ret;
    int i = 0;
    int j = 0;
    unsigned char char_array_3[3];
    unsigned char char_array_4[4];

    while (in_len--) {
        char_array_3[i++] = *(bytes_to_encode++);
        if (i == 3) {
            char_array_4[0] = (char_array_3[0] & 0xfc) >> 2;
            char_array_4[1] = ((char_array_3[0] & 0x03) << 4) + ((char_array_3[1] & 0xf0) >> 4);
            char_array_4[2] = ((char_array_3[1] & 0x0f) << 2) + ((char_array_3[2] & 0xc0) >> 6);
            char_array_4[3] = char_array_3[2] & 0x3f;

            for (i = 0; (i < 4); i++)
                ret += base64_chars[char_array_4[i]];
            i = 0;
        }
    }

    if (i) {
        for (j = i; j < 3; j++)
            char_array_3[j] = '\0';

        char_array_4[0] = (char_array_3[0] & 0xfc) >> 2;
        char_array_4[1] = ((char_array_3[0] & 0x03) << 4) + ((char_array_3[1] & 0xf0) >> 4);
        char_array_4[2] = ((char_array_3[1] & 0x0f) << 2) + ((char_array_3[2] & 0xc0) >> 6);

        for (j = 0; (j < i + 1); j++)
            ret += base64_chars[char_array_4[j]];

        while ((i++ < 3))
            ret += '=';

    }

    return ret;
}

std::string base64_decode(std::string const &encoded_string) {
    int in_len = encoded_string.size();
    int i = 0;
    int j = 0;
    int in_ = 0;
    unsigned char char_array_4[4], char_array_3[3];
    std::string ret;

    while (in_len-- && (encoded_string[in_] != '=') && is_base64(encoded_string[in_])) {
        char_array_4[i++] = encoded_string[in_];
        in_++;
        if (i == 4) {
            for (i = 0; i < 4; i++)
                char_array_4[i] = base64_chars.find(char_array_4[i]);

            char_array_3[0] = (char_array_4[0] << 2) + ((char_array_4[1] & 0x30) >> 4);
            char_array_3[1] = ((char_array_4[1] & 0xf) << 4) + ((char_array_4[2] & 0x3c) >> 2);
            char_array_3[2] = ((char_array_4[2] & 0x3) << 6) + char_array_4[3];

            for (i = 0; (i < 3); i++)
                ret += char_array_3[i];
            i = 0;
        }
    }

    if (i) {
        for (j = 0; j < i; j++)
            char_array_4[j] = base64_chars.find(char_array_4[j]);

        char_array_3[0] = (char_array_4[0] << 2) + ((char_array_4[1] & 0x30) >> 4);
        char_array_3[1] = ((char_array_4[1] & 0xf) << 4) + ((char_array_4[2] & 0x3c) >> 2);

        for (j = 0; (j < i - 1); j++) ret += char_array_3[j];
    }

    return ret;
}

extern "C" {

JNIEXPORT jstring JNICALL
Java_app_com_bookdb_controller_BaseActivity_upin(JNIEnv *env, jobject instance, jstring piss) {
    jboolean isCopy = true;
    const char *s = env->GetStringUTFChars(piss, &isCopy);
    return env->NewStringUTF(base64_decode(std::string(s)).c_str());
}

JNIEXPORT jstring JNICALL
Java_app_com_bookdb_controller_BaseActivity_ipin(JNIEnv *env, jobject instance, jstring pass) {
    jboolean isCopy = true;
    const char *s = env->GetStringUTFChars(pass, &isCopy);
    return env->NewStringUTF(
            base64_encode(reinterpret_cast<const unsigned char *>(std::string(s).c_str()),
                          std::string(s).length()).c_str());
}

JNIEXPORT jstring JNICALL Java_app_com_bookdb_controller_BaseActivity_hola(
        JNIEnv *env,
        jobject, jint para_onde_ir) {
    std::string perfecto = "goToHell";

    switch (para_onde_ir) {
        case 0:
            perfecto = "aHR0cDovL3Rlc3QuaW5jZW5wbHVzLmNvbTo1MDAwL3VzZXJzL2xvZ2lu";
            break;
        case 1:
            perfecto = "aHR0cDovL3Rlc3QuaW5jZW5wbHVzLmNvbTo1MDAwL3VzZXJzL2xvZ291dA==";
            break;
        case 2:
            perfecto = "aHR0cDovL3Rlc3QuaW5jZW5wbHVzLmNvbTo1MDAwL2Jvb2tzL2luc2VydA==";
            break;
        case 3:
            perfecto = "aHR0cDovL3Rlc3QuaW5jZW5wbHVzLmNvbTo1MDAwL2Jvb2tzL2VkaXQ=";
            break;
        case 4:
            perfecto = "aHR0cDovL3Rlc3QuaW5jZW5wbHVzLmNvbTo1MDAwL2Jvb2tzL2RldGFpbA==";
            break;
        case 5:
            perfecto = "aHR0cDovL3Rlc3QuaW5jZW5wbHVzLmNvbTo1MDAwL2Jvb2tz";
            break;
        case 6:
            perfecto = "aHR0cDovL3Rlc3QuaW5jZW5wbHVzLmNvbTo1MDAwL3VzZXJzL21l";
            break;
        default:
            perfecto = "whatAr3Y0uDo1ng?";
            break;
    }
    return env->NewStringUTF(perfecto.c_str());
}
}


