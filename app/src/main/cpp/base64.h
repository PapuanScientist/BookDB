//
// Created by Ori Syun on 3/13/2018.
//

#ifndef MOBILE_APPS_BASE64_H
#define MOBILE_APPS_BASE64_H

#include <string>

std::string base64_encode(unsigned char const*, unsigned int len);
std::string base64_decode(std::string const& s);

#endif //MOBILE_APPS_BASE64_H
